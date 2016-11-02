package bing.thread;

import bing.AppUI;
import bing.Constants;
import bing.bean.Attendance;
import bing.bean.CardRecord;
import bing.bean.Config;
import bing.util.ConfigUtils;
import bing.util.ExcelCellStyleUtils;
import bing.util.ExceptionUtils;
import bing.util.WorkDayUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 考勤统计线程
 *
 * @author IceWee
 */
public class StatThread implements Runnable {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(StatThread.class);
    private final AppUI app; // 用于回调
    private final String cardExcelPath; // 打卡记录表文件绝对路径
    private final String attendExcelPath; // 考勤模板表文件绝对路径
    
    public StatThread(AppUI app, String cardExcelPath, String attendExcelPath) {
        this.app = app;
        this.cardExcelPath = cardExcelPath;
        this.attendExcelPath = attendExcelPath;
    }

    @Override
    public void run() {
        // 1.读取配置文件
        Config config = ConfigUtils.getConfig();
        if (config == null) {
            this.app.buttonsEnabled();
            return;
        }
        // 2.统计考勤并标识问题打卡记录
        List<Attendance> attendances = statAndSign(config);
        if (attendances == null) {
            this.app.buttonsEnabled();
            return;
        }
        // 3.填充考勤模板表
        fillAttendance(config, attendances); // 填充考勤记录表
        this.app.completed();
    }

    /**
     * 根据打卡记录表统计每个人的出勤数并标识出有问题的打卡信息
     *
     * @param config
     * @return
     */
    private List<Attendance> statAndSign(Config config) {
        List<Attendance> attendances = new ArrayList<>(); // 用于填充考勤表
        List<CardRecord> cardRecords = new ArrayList<>(); // 用户生成标识后的打卡记录表
        Set<String> excludeNames = config.getExcludeNames(); // 排除统计的人名
        HSSFWorkbook workbook = null;
        try {
            workbook = new HSSFWorkbook(new FileInputStream(new File(cardExcelPath)));
            String yearMonth = getCardYearMonth(workbook, config); // 解析出打卡年月
            Set<Integer> workDays = WorkDayUtils.getWorkDays(yearMonth); // 获得当前工作月份应该上班的天
            HSSFSheet sheet = workbook.getSheetAt(0);
            int beginIndex = config.getCardDataBeginRow() - 1;
            HSSFRow row;
            HSSFCell nameCell, dateCell, ondutyCell, offdutyCell;
            String lastName = null; // 上次统计的人名
            String currentName; // 当前统计的人名
            String tempVal, attendate, ondutyTime, offdutyTime;
            int day;
            Attendance attendance = new Attendance();
            int dutyDays = 0; // 出勤天数
            StringBuilder remarkBuilder = new StringBuilder(); // 备注
            String remark;
            CardRecord cardRecord;
            for (int i = beginIndex; i < sheet.getLastRowNum(); i++) {
                cardRecord = new CardRecord();
                row = sheet.getRow(i);
                nameCell = row.getCell(config.getCardNameColumn() - 1); // 员工名称
                currentName = nameCell.getStringCellValue();
                if (excludeNames.contains(currentName)) { // 遇到不统计的人员跳过
                    continue;
                }
                cardRecord.setUsername(currentName);
                dateCell = row.getCell(config.getCardAttendateColumn() - 1); // 考勤日期
                attendate = StringUtils.trim(dateCell.getStringCellValue());
                cardRecord.setAttendate(attendate);
                tempVal = StringUtils.substringAfterLast(attendate, "-");
                day = Integer.valueOf(tempVal); // 日期-日考勤日期列
                ondutyCell = row.getCell(config.getCardOndutyColumn() - 1); // 上班打卡时间
                if (ondutyCell == null) {
                    ondutyTime = "";
                } else {
                    ondutyTime = StringUtils.trim(ondutyCell.getStringCellValue());
                }
                offdutyCell = row.getCell(config.getCardOffdutyColumn() - 1); // 下班打卡时间
                if (offdutyCell == null) {
                    offdutyTime = "";
                } else {
                    offdutyTime = StringUtils.trim(offdutyCell.getStringCellValue());
                }
                int code = verifyCardTime(config, ondutyTime, offdutyTime, workDays, day);
                if (Constants.CARD_CORRECT == code) {
                    dutyDays++; // 打卡记录完全正确，考勤天数自加
                } 
                if (Constants.CARD_CORRECT != code && Constants.CARD_EMPTY != code){
                    remarkBuilder.append(day).append("日,");
                }
                cardRecord.setOndutyTime(ondutyTime);
                cardRecord.setOffdutyTime(offdutyTime);
                cardRecord.setCode(code);
                cardRecords.add(cardRecord);
                if (!StringUtils.equals(currentName, lastName)) { // 换人后需要初始化
                    if (StringUtils.isNotBlank(lastName)) {
                        attendance.setName(lastName);
                        attendance.setDutyDays(String.valueOf(dutyDays));
                        remark = remarkBuilder.toString();
                        attendance.setRemark(remark);
                        attendances.add(attendance);
                    }
                    attendance = new Attendance();
                    remarkBuilder = new StringBuilder();
                    dutyDays = 0;
                    lastName = currentName;
                }
            }
            createCardExcel(cardRecords); // 生成标识打卡记录表
        } catch (Exception e) {
            attendances = null;
            String error = ExceptionUtils.createExceptionString(e);
            LOGGER.error("解析打卡记录表时出现了异常...\n{}", error);
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                }
            }
        }
        return attendances;
    }

    /**
     * 填充考勤模板
     *
     * @param config
     * @param attendances
     */
    private void fillAttendance(Config config, List<Attendance> attendances) {
        String filename = Constants.ATTEND_EXCEL_MODIFIED + "." + Constants.EXT_XLS;
        String filepath = ConfigUtils.getReportPath() + filename;
        FileInputStream in = null;
        FileOutputStream out = null;
        HSSFWorkbook workbook = null;
        try {
            in = new FileInputStream(this.attendExcelPath);
            workbook = new HSSFWorkbook(in);
            int sheetIndex = workbook.getNumberOfSheets() - 1;
            HSSFSheet sheet = workbook.getSheetAt(sheetIndex);
            int rowIndex = config.getAttendDataBeginRow() - 1;
            int nameCellIndex = config.getAttendNameColumn() - 1;
            int daysCellIndex = config.getAttendDaysColumn() - 1;
            int remarkCellIndex = config.getAttendRemarkColumn() - 1;
            HSSFRow row;
            HSSFCell nameCell, daysCell, remarkCell;
            String name;
            Attendance attendance;
            HSSFCellStyle remarkCellStyle = ExcelCellStyleUtils.createRemarkCellStyle(workbook);
            for (int i = rowIndex; i < sheet.getLastRowNum(); i++) {
                row = sheet.getRow(i);
                nameCell = row.getCell(nameCellIndex);
                name = nameCell.getStringCellValue();
                attendance = getAttendance(name, attendances);
                if (StringUtils.isBlank(name) || attendance == null) {
                    continue;
                }
                LOGGER.info(attendance.toString());
                daysCell = row.getCell(daysCellIndex);
                if (daysCell == null) {
                    daysCell = row.createCell(daysCellIndex);
                }
                daysCell.setCellValue(attendance.getDutyDays());
                remarkCell = row.getCell(remarkCellIndex);
                if (remarkCell == null) {
                    remarkCell = row.createCell(remarkCellIndex);
                }
                remarkCell.setCellStyle(remarkCellStyle);
                remarkCell.setCellValue(attendance.getRemark());
            }
            out = new FileOutputStream(filepath);
            workbook.write(out);
            out.flush();
        } catch (Exception e) {
            String error = ExceptionUtils.createExceptionString(e);
            LOGGER.error("填充考勤记录表时出现了异常...\n{}", error);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ex) {
                }
            }
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
    }
    
    /**
     * 根据名称查找
     * 
     * @param name
     * @param attendances
     * @return 
     */
    private Attendance getAttendance(String name, List<Attendance> attendances) {
        Attendance attendance = null;
        for (Attendance currAttendance : attendances) {
            if (StringUtils.equals(name, currAttendance.getName())) {
                attendance = currAttendance;
                break;
            }
        }
        return attendance;
    }
    
    /**
     * 判断打卡记录是否正常 判断依据： 
     * 1）上班打卡时间和下班时间打卡都为空 
     * 2）上班或下班打卡时间任意一个为空
     * 3）上班和下班打卡时间都非空，需要分别判断上班时间是否迟到，下班时间是否早退
     *
     * @param config
     * @param ondutyTime 上班时间
     * @param offdutyTime 下班时间
     * @param workDays 工作日集合；1,2,3...
     * @param day 天；1
     * @return
     */
    private int verifyCardTime(Config config, String ondutyTime, String offdutyTime, Set<Integer> workDays, int day) {
        int code = Constants.CARD_CORRECT;
        if (StringUtils.isBlank(ondutyTime) && StringUtils.isBlank(offdutyTime)) { // 全天无打卡记录
            if (workDays.contains(day)) { // 工作日未打卡
                code = Constants.CARD_ERROR;
            } else {
                code = Constants.CARD_EMPTY;
            }
        } else if (StringUtils.isNotBlank(ondutyTime) && StringUtils.isNotBlank(offdutyTime)) { // 上下班都有打卡记录
            if (ondutyTime.compareTo(config.getOndutyTime()) <= 0 && offdutyTime.compareTo(config.getOffdutyTime()) >= 0) { // 打卡正常
                code = Constants.CARD_CORRECT;
            } else if (ondutyTime.compareTo(config.getOndutyTime()) > 0 && offdutyTime.compareTo(config.getOffdutyTime()) >= 0) { // 上班迟到
                code = Constants.CARD_ONDUTY_ERROR;
            } else if (ondutyTime.compareTo(config.getOndutyTime()) <= 0 && offdutyTime.compareTo(config.getOffdutyTime()) < 0) { // 下班早退
                code = Constants.CARD_OFFDUTY_ERROR;
            } else { // 上下班打卡都异常，即迟到又早退
                code = Constants.CARD_ERROR;
            }
        } else {// 上班或下班有打卡记录
            if (StringUtils.isBlank(ondutyTime) && StringUtils.isNotBlank(offdutyTime)) { // 无上班打卡记录
                if (offdutyTime.compareTo(config.getOffdutyTime()) < 0) { // 下班早退
                    code = Constants.CARD_ERROR;
                } else { // 下班打卡记录正常，则认为该员工上班时忘记打卡
                    code = Constants.CARD_CORRECT;
                }
            } else if (StringUtils.isNotBlank(ondutyTime) && StringUtils.isBlank(offdutyTime)) { // 无下班打卡记录
                if (ondutyTime.compareTo(config.getOndutyTime()) > 0) { // 上班迟到
                    code = Constants.CARD_ERROR;
                } else { // 上班打卡记录正常，则认为该员工下班时忘记打卡
                    code = Constants.CARD_CORRECT;
                }
            }
        }
        return code;
    }
    
    /**
     * 生成新的打卡记录表
     * 
     * @param workbook
     * @param cardRecords
     */
    private void createCardExcel(List<CardRecord> cardRecords) {
        String filename = Constants.CARD_EXCEL_MODIFIED + "." + Constants.EXT_XLS;
        String filepath = ConfigUtils.getReportPath() + filename;
        HSSFWorkbook workbook = new HSSFWorkbook();
        CellStyle headerStyle = ExcelCellStyleUtils.createHeaderCellStyle(workbook);
        CellStyle defaultStyle = ExcelCellStyleUtils.createCellStyle(workbook, Constants.CELL_STYLE_DEFAULT);
        CellStyle yellowStyle = ExcelCellStyleUtils.createCellStyle(workbook, Constants.CELL_STYLE_YELLOW);
        CellStyle redStyle = ExcelCellStyleUtils.createCellStyle(workbook, Constants.CELL_STYLE_RED);
        HSSFSheet sheet = workbook.createSheet("打卡记录");
        int rowIndex = 0;
        createCardHeader(sheet, headerStyle, rowIndex++); // 生成表头
        // 循环插入数据行
        for (CardRecord cardRecord : cardRecords) {
            insertCardRow(sheet, defaultStyle, yellowStyle, redStyle, rowIndex++, cardRecord);
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(filepath);
            workbook.write(out);
            out.flush();
        } catch (Exception e) {
            String error = ExceptionUtils.createExceptionString(e);
            LOGGER.error("生成新打卡记录表时出现了异常...\n{}", error);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ex) {
                }
            }
            try {
                workbook.close();
            } catch (IOException ex) {
            }
        }
    }
    
    /**
     * 生成表头
     * 
     * @param sheet
     * @param headerStyle
     * @param rowIndex 
     */
    private void createCardHeader(HSSFSheet sheet, CellStyle headerStyle, int rowIndex) {
        HSSFRow header = sheet.createRow(rowIndex);
        int cellIndex = 0;
        HSSFCell nameCell = header.createCell(cellIndex++);
        nameCell.setCellStyle(headerStyle);
        nameCell.setCellValue("姓名");
        HSSFCell dateCell = header.createCell(cellIndex++);
        dateCell.setCellStyle(headerStyle);
        dateCell.setCellValue("考勤日期");
        HSSFCell ondutyCell = header.createCell(cellIndex++);
        ondutyCell.setCellStyle(headerStyle);
        ondutyCell.setCellValue("上班时间");
        HSSFCell offdutyCell = header.createCell(cellIndex++);
        offdutyCell.setCellStyle(headerStyle);
        offdutyCell.setCellValue("下班时间");
    }
    
    /**
     * 插入打卡记录
     * 
     * @param sheet
     * @param defaultStyle
     * @param yellowStyle
     * @param redStyle
     * @param rowIndex
     * @param cardRecord 
     */
    private void insertCardRow(HSSFSheet sheet, CellStyle defaultStyle, CellStyle yellowStyle, CellStyle redStyle, int rowIndex, CardRecord cardRecord) {
        HSSFRow row = sheet.createRow(rowIndex);
        int cellIndex = 0;
        HSSFCell nameCell = row.createCell(cellIndex++);
        nameCell.setCellValue(cardRecord.getUsername());
        HSSFCell dateCell = row.createCell(cellIndex++);
        dateCell.setCellValue(cardRecord.getAttendate());
        HSSFCell ondutyCell = row.createCell(cellIndex++);
        ondutyCell.setCellValue(cardRecord.getOndutyTime());
        HSSFCell offdutyCell = row.createCell(cellIndex++);
        offdutyCell.setCellValue(cardRecord.getOffdutyTime());
        sign(defaultStyle, yellowStyle, redStyle, cardRecord.getCode(), nameCell, dateCell, ondutyCell, offdutyCell);
    }
    
    /**
     * 异常打卡记录样式标记
     * 
     * @param defaultStyle
     * @param yellowStyle
     * @param redStyle
     * @param code
     * @param nameCell
     * @param dateCell
     * @param ondutyCell
     * @param offdutyCell 
     */
    private void sign(CellStyle defaultStyle, CellStyle yellowStyle, CellStyle redStyle, int code, HSSFCell nameCell, HSSFCell dateCell, HSSFCell ondutyCell, HSSFCell offdutyCell) {
        switch (code){
            case 0: // 打卡正常 Constants.CARD_CORRECT
                nameCell.setCellStyle(defaultStyle);
                dateCell.setCellStyle(defaultStyle);
                ondutyCell.setCellStyle(defaultStyle);
                offdutyCell.setCellStyle(defaultStyle);
                break;
            case 1: // 全天无打卡记录，不标识 Constants.CARD_EMPTY
                nameCell.setCellStyle(defaultStyle);
                dateCell.setCellStyle(defaultStyle);
                ondutyCell.setCellStyle(defaultStyle);
                offdutyCell.setCellStyle(defaultStyle);
                break;
            case 4: // 全天打卡有误 Constants.CARD_ERROR
                nameCell.setCellStyle(yellowStyle);
                dateCell.setCellStyle(yellowStyle);
                ondutyCell.setCellStyle(redStyle);
                offdutyCell.setCellStyle(redStyle);
                break;
            case 2: // 上班打卡有误 Constants.CARD_ONDUTY_ERROR
                nameCell.setCellStyle(yellowStyle);
                dateCell.setCellStyle(yellowStyle);
                ondutyCell.setCellStyle(redStyle);
                offdutyCell.setCellStyle(defaultStyle);
                break;
            case 3: // 下班打卡有误 Constants.CARD_OFFDUTY_ERROR
                nameCell.setCellStyle(yellowStyle);
                dateCell.setCellStyle(yellowStyle);
                ondutyCell.setCellStyle(defaultStyle);
                offdutyCell.setCellStyle(redStyle);
                break;
        }
    }
    
    /**
     * 解析打卡记录表获得打卡的年月
     * 格式：2016-10
     * 
     * @param workbook
     * @param config
     * @return 2016-08
     */
    private String getCardYearMonth(HSSFWorkbook workbook, Config config) {
        String yearMonth = null;
        try {
            HSSFSheet sheet = workbook.getSheetAt(0);
            int beginIndex = config.getCardDataBeginRow() - 1;
            HSSFRow firstRow = sheet.getRow(beginIndex);
            HSSFCell dateCell = firstRow.getCell(config.getCardAttendateColumn() - 1); // 考勤日期
            String attendate = StringUtils.trim(dateCell.getStringCellValue());
            yearMonth = StringUtils.substringBeforeLast(attendate, "-");
        } catch (Exception e) {
            String error = ExceptionUtils.createExceptionString(e);
            LOGGER.error("解析打卡记录表获得打卡年月时出现了异常...\n{}", error);
        }
        return yearMonth;
    }
    
}

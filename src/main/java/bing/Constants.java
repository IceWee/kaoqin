package bing;

/**
 * 常量类
 *
 * @author IceWee
 */
public class Constants {

    /**
     * 软件图标路径
     */
    public static final String ICON_APP_PATH = "/bing/ui/images/app.png";
    
    /**
     * 复制图标路径
     */
    public static final String ICON_COPY_PATH = "/bing/ui/images/copy.png";

    /**
     * 剪切图标路径
     */
    public static final String ICON_CUT_PATH = "/bing/ui/images/cut.png";

    /**
     * 清空图标路径
     */
    public static final String ICON_CLEAR_PATH = "/bing/ui/images/clear.png";

    public static final String TEXT_COPY = "复制";
    public static final String TEXT_CUT = "剪切";
    public static final String TEXT_CLEAR = "清除";
    
    public static final String ENCODING_UTF8 = "UTF-8";
    
    /**
     * 配置文件路径
     */
    public static final String CONFIG_FILE_PATH = "config.ini";
    
    /**
     * 统计结果生成目录
     */
    public static final String REPORT_FOLDER = "report";

    /**
     * 确认
     */
    public static final int OPTION_OK = 0;

    /**
     * 2007+版本Excel文件后缀
     */
    public static final String EXT_XLSX = "xlsx";

    /**
     * 2003-版本Excel文件后缀
     */
    public static final String EXT_XLS = "xls";

    /**
     * 配置文件-不统计的人员名称
     */
    public static final String CFG_EXCLUDE_NAME = "exclude.name";

    /**
     * 配置文件-规定-上班时间
     */
    public static final String CFG_TIME_ONDUTY = "time.onduty";

    /**
     * 配置文件-规定-下班时间
     */
    public static final String CFG_TIME_OFFDUTY = "time.offduty";

    /**
     * 配置文件-打卡记录表-人名列
     */
    public static final String CFG_CARD_COLUMN_NAME = "card.column.name";

    /**
     * 配置文件-打卡记录表-考勤日期列
     */
    public static final String CFG_CARD_COLUMN_ATTENDATE = "card.column.attendate";

    /**
     * 配置文件-打卡记录表-上班时间列
     */
    public static final String CFG_CARD_COLUMN_ONDUTY = "card.column.onduty";

    /**
     * 配置文件-打卡记录表-下班时间列
     */
    public static final String CFG_CARD_COLUMN_OFFDUTY = "card.column.offduty";

    /**
     * 配置文件-打开记录表-数据起始行
     */
    public static final String CFG_CARD_ROW_DATABEGIN = "card.row.databegin";

    /**
     * 配置文件-考勤模板表-数据起始行
     */
    public static final String CFG_ATTEND_ROW_DATABEGIN = "attend.row.databegin";

    /**
     * 配置文件-考勤模板表-人名列
     */
    public static final String CFG_ATTEND_COLUMN_NAME = "attend.column.name";
    
     /**
     * 配置文件-考勤模板表-实出勤列
     */
    public static final String CFG_ATTEND_COLUMN_DAYS = "attend.column.days";

    /**
     * 配置文件-考勤模板表-备注列
     */
    public static final String CFG_ATTEND_COLUMN_REMARK = "attend.column.remark";

    /**
     * 打卡信息-完全正确
     */
    public static final int CARD_CORRECT = 0;

    /**
     * 打卡信息-全天无打卡记录
     */
    public static final int CARD_EMPTY = 1;

    /**
     * 打卡信息-上班无打卡记录异常（包含未打卡和迟到情况），下班打卡正常
     */
    public static final int CARD_ONDUTY_ERROR = 2;

    /**
     * 打卡信息-下班无打卡记录异常（包含未打卡和早退情况），上班打卡正常
     */
    public static final int CARD_OFFDUTY_ERROR = 3;

    /**
     * 打卡信息-完全错误，上班（未打卡、迟到）；下班（未打卡、早退）
     */
    public static final int CARD_ERROR = 4;
    
    /**
     * 重新生成打卡记录表名称
     */
    public static final String CARD_EXCEL_MODIFIED = "打卡记录表";
    
    /**
     * 重新生成考勤记录表名称
     */
    public static final String ATTEND_EXCEL_MODIFIED = "考勤记录表";

    /**
     * 单元格样式-默认
     */
    public static final int CELL_STYLE_DEFAULT = 0;
    
    /**
     * 单元格样式-黄色
     */
    public static final int CELL_STYLE_YELLOW = 1;
    
    /**
     * 单元格样式-红色
     */
    public static final int CELL_STYLE_RED = 2;
    
}

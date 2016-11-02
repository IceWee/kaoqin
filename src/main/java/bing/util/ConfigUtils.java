package bing.util;

import bing.Constants;
import bing.bean.Config;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 配置加载
 *
 * @author IceWee
 */
public class ConfigUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigUtils.class);

    public static Config getConfig() {
        Config config = null;
        BufferedReader reader = null;
        try {
            config = new Config();
            ClassLoader classLoader = ClassLoader.getSystemClassLoader();  
            File configFile = new File(classLoader.getResource(Constants.CONFIG_FILE_PATH).getFile());
            reader = new BufferedReader(new FileReader(configFile));
            String line, value;
            String[] array;
            while ((line = reader.readLine()) != null) {
                LOGGER.debug(line);
                if (StringUtils.startsWith(line, "#")) { // #为注释
                    continue;
                }
                array = StringUtils.split(line, "=");
                value = array[1];
                if (StringUtils.startsWith(line, Constants.CFG_EXCLUDE_NAME)) {
                    config.setExcludeName(value);
                } else if (StringUtils.startsWith(line, Constants.CFG_TIME_ONDUTY)) {
                    config.setOndutyTime(value);
                } else if (StringUtils.startsWith(line, Constants.CFG_TIME_OFFDUTY)) {
                    config.setOffdutyTime(value);
                } else if (StringUtils.startsWith(line, Constants.CFG_CARD_COLUMN_NAME)) {
                    config.setCardNameColumn(Integer.valueOf(value));
                } else if (StringUtils.startsWith(line, Constants.CFG_CARD_COLUMN_ATTENDATE)) {
                    config.setCardAttendateColumn(Integer.valueOf(value));
                } else if (StringUtils.startsWith(line, Constants.CFG_CARD_COLUMN_ONDUTY)) {
                    config.setCardOndutyColumn(Integer.valueOf(value));
                } else if (StringUtils.startsWith(line, Constants.CFG_CARD_COLUMN_OFFDUTY)) {
                    config.setCardOffdutyColumn(Integer.valueOf(value));
                } else if (StringUtils.startsWith(line, Constants.CFG_CARD_ROW_DATABEGIN)) {
                    config.setCardDataBeginRow(Integer.valueOf(value));
                } else if (StringUtils.startsWith(line, Constants.CFG_ATTEND_COLUMN_NAME)) {
                    config.setAttendNameColumn(Integer.valueOf(value));
                } else if (StringUtils.startsWith(line, Constants.CFG_ATTEND_COLUMN_DAYS)) {
                    config.setAttendDaysColumn(Integer.valueOf(value));
                } else if (StringUtils.startsWith(line, Constants.CFG_ATTEND_COLUMN_REMARK)) {
                    config.setAttendRemarkColumn(Integer.valueOf(value));
                } else if (StringUtils.startsWith(line, Constants.CFG_ATTEND_ROW_DATABEGIN)) {
                    config.setAttendDataBeginRow(Integer.valueOf(value));
                }
            }
        } catch (Exception e) {
            config = null;
            String error = ExceptionUtils.createExceptionString(e);
            LOGGER.error("加载配置文件[config.ini]时出现了异常...\n{}", error);
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
            }
        }
        return config;
    }
    
    /**
     * 获取当前位置
     * 
     * @return 
     */
    public static String getReportPath() {
        File file = new File("");
        String path = file.getAbsolutePath();
        String reportPath = path + File.separator + Constants.REPORT_FOLDER + File.separator;
        new File(reportPath).mkdirs();
        return reportPath;
    }

}

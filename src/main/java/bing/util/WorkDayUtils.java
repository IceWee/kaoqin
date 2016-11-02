package bing.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 工作日工具类
 *
 * @author IceWee
 */
public class WorkDayUtils {

    static final String ENCODING_UTF8 = "UTF-8";
    static final String YYYY_MM_DD = "yyyy-MM-dd";
    static final String YYYYMMDD = "yyyyMMdd";
    static final String WORK_DAY_NUM = "0";
    static final ThreadLocal<SimpleDateFormat> YYYY_MM_DD_FORMAT = new ThreadLocal<>();
    static final ThreadLocal<SimpleDateFormat> YYYYMMDD_FORMAT = new ThreadLocal<>();

    // 免费节假日API请求地址
    static final String APP_URL = "http://tool.bitefu.net/jiari?d=";

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkDayUtils.class);

    /**
     * 获取某年某月的全部工作日集合
     *
     * @param yearMonth 2016-08
     * @return
     */
    public static Set<Integer> getWorkDays(String yearMonth) {
        Set<Integer> workDays = new HashSet<>();
        String[] ymArray = StringUtils.split(yearMonth, "-");
        int year = Integer.parseInt(ymArray[0]);
        int month = Integer.parseInt(ymArray[1]) - 1;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        int minDay = 1;
        int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        List<String> list = new ArrayList<>();
        String date;
        for (int i = minDay; i <= maxDay; i++) {
            calendar.set(year, month, i);
            date = getYYYYMMDDFormat().format(calendar.getTime());
            list.add(date);
        }
        Object[] array = list.toArray();
        String dates = StringUtils.join(array, ",");
        String json = get(APP_URL, dates);
        if (StringUtils.isNotBlank(json)) {
            Map<String, String> map = JSON.parseObject(json, new TypeReference<Map<String, String>>() {});
            String day;
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String key = entry.getKey(); // 20160801
                String value = entry.getValue();
                if (WORK_DAY_NUM.equals(value)) { // 工作日
                    day = StringUtils.substring(key, 6, 8);
                    workDays.add(Integer.parseInt(day));
                }
            }
        }
        return workDays;
    }

    /**
     * 格式：yyyy-MM-dd
     *
     * @return
     */
    public static SimpleDateFormat getYYYY_MM_DDFormat() {
        SimpleDateFormat sdf = YYYY_MM_DD_FORMAT.get();
        if (sdf == null) {
            sdf = new SimpleDateFormat(YYYY_MM_DD);
        }
        return sdf;
    }

    /**
     * 格式：yyyy-MM-dd
     *
     * @return
     */
    public static SimpleDateFormat getYYYYMMDDFormat() {
        SimpleDateFormat sdf = YYYYMMDD_FORMAT.get();
        if (sdf == null) {
            sdf = new SimpleDateFormat(YYYYMMDD);
        }
        return sdf;
    }

    /**
     * 判断所给日期是否为工作日
     *
     * @param dates 20130101,20130103,20130105,20130201
     * @return
     */
    public static boolean isWorkDay(String dates) {
        boolean isWorkDay = false;
        try {
            String param = "d=" + dates;
            // 工作日对应结果为 0, 休息日对应结果为 1, 节假日对应的结果为 2
            String num = get(APP_URL, param);
            if (WORK_DAY_NUM.equals(num)) {
                isWorkDay = true;
            }
        } catch (Exception ex) {
        }
        return isWorkDay;
    }

    /**
     * GET+
     *
     * @param httpUrl
     * @param httpParam
     * @return
     */
    public static String get(String httpUrl, String httpParam) {
        BufferedReader reader = null;
        HttpURLConnection httpConn = null;
        String result = null;
        String url = httpUrl + httpParam;
        try {
            StringBuilder builder = new StringBuilder();
            URL getUrl = new URL(url);
            httpConn = (HttpURLConnection) getUrl.openConnection();
            httpConn.setRequestMethod("GET");
            httpConn.connect();
            if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), ENCODING_UTF8));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line).append("\r\n");
                }
                result = builder.toString();
            } else {
                LOGGER.error("请求URL[{}]未得到响应...", url);
            }
        } catch (Exception e) {
            String error = ExceptionUtils.createExceptionString(e);
            LOGGER.error("请求URL[{}]时出现了异常...\n{}", url, error);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ex) {
                }
            }
            if (httpConn != null) {
                httpConn.disconnect();
            }
        }
        return result;
    }

}

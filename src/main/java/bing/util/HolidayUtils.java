package bing.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

/**
 * 节假日工具类
 *
 * @author IceWee
 */
public class HolidayUtils {

    private static final String ENCODING_UTF8 = "UTF-8";
    private static final String WORKDAY_FLAG = "0";
    private static final int MAX_DAY_NUM = 31;

    // 免费节假日API请求地址
    private static final String API_KEY = "p308.816290524e6364fb25cfa75f5758d3ba";
    private static final String API_URL = "http://www.easybots.cn/api/holiday.php?ak=" + API_KEY + "&m=";

    private static final Logger LOGGER = LoggerFactory.getLogger(HolidayUtils.class);

    /**
     * 获取某年某月的全部节假日集合
     *
     * @param yearMonth 201608
     * @return
     */
    public static List<Integer> getHoliays(String yearMonth) {
        List<Integer> holidays = new ArrayList<>();
        String json = get(API_URL, yearMonth);
        if (StringUtils.isNotBlank(json)) {
            JSONObject object = JSONObject.parseObject(json);
            JSONObject holiday = object.getJSONObject(yearMonth);
            String dayFlag;
            String key;
            for (int i = 1; i <= MAX_DAY_NUM; i++) {
                key = String.format("%02d", i);
                dayFlag = holiday.getString(key);
                if (StringUtils.isNotBlank(dayFlag) && !WORKDAY_FLAG.equals(dayFlag)) {
                    holidays.add(i);
                }
            }
        }
        return holidays;
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

package org.example.Util;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: houlintao
 * @Date:2020/6/6 上午6:29
 * 日期处理类
 */
public class DateTool {

    private static final Logger logger = Logger.getLogger(DateTool.class);

    public static final String DATE_PATTERN = "yyyy-MM-dd";

    public static String DATE_TIME_PATTERN_YYYY_MM_DD_HH_MM_SS_SSS = "yyyyMMddHHmmssSSS";

    public final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    // 日期转换格式数组
    public static String[][] regularExp = new String[][]{

            // 默认格式
            {"\\d{4}-((([0][1,3-9]|[1][0-2]|[1-9])-([0-2]\\d|[3][0,1]|[1-9]))|((02|2)-(([1-9])|[0-2]\\d)))\\s+([0,1]\\d|[2][0-3]|\\d):([0-5]\\d|\\d):([0-5]\\d|\\d)",
                    DATE_TIME_PATTERN},
            // 仅日期格式 年月日
            {"\\d{4}-((([0][1,3-9]|[1][0-2]|[1-9])-([0-2]\\d|[3][0,1]|[1-9]))|((02|2)-(([1-9])|[0-2]\\d)))",
                    DATE_PATTERN},
            //  带毫秒格式
            {"\\d{4}((([0][1,3-9]|[1][0-2]|[1-9])([0-2]\\d|[3][0,1]|[1-9]))|((02|2)(([1-9])|[0-2]\\d)))([0,1]\\d|[2][0-3])([0-5]\\d|\\d)([0-5]\\d|\\d)\\d{1,3}",
                    DATE_TIME_PATTERN_YYYY_MM_DD_HH_MM_SS_SSS}
    };

    public static String format(Date date,String pattern){
        if (date != null){
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            return dateFormat.format(date);
        }
        return null;
    }
    //将日期格式化为"yyyy-MM-dd"的形式
    public static String format(Date date){
        return format(date,DATE_PATTERN);
    }


    public static String getDateFormat(String dateStr){
        String style = null;
        if (StringUtils.isEmpty(dateStr)){
            return null;
        }
        boolean b=false;

        for (int i=0;i<regularExp.length;i++){

            b=dateStr.matches(regularExp[i][0]);
            if (b){
                style=regularExp[i][1];
            }

        }
        if (StringUtils.isEmpty(style)){
            logger.info("date_str:" + dateStr);
            logger.info("日期格式获取出错，未识别的日期格式");
        }
        return style;
        }

    //将日期对象转化为字符串
    public static String timeToString(Long time,String pattern){
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        if (time.toString().length()<13){
            time=time*1000L;
        }
        Date date = new Date(time);
        String format = dateFormat.format(date);
        return format;
    }
    //将字符串转化为time类型
    public static long strToTime(String timeStr) throws ParseException {

            Date date = stringToDate(timeStr);
            return date.getTime()/1000;
    }

    //将字符串转化为日期对象
    public static Date stringToDate(String str) throws ParseException {
        String style = getDateFormat(str);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(style);
        return new Date((simpleDateFormat.parse(str).getTime()));
    }
}

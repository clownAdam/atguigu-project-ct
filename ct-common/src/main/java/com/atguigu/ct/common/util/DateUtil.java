package com.atguigu.ct.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期工具类
 * @author clown
 */
public class DateUtil {
    /**
     * 将日期字符串按照指定的格式解析为日期对象
     * @param dateString 日期字符串
     * @param format 格式字符串
     * @return 日期对象
     */
    public static Date parse(String dateString,String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 将指定的日期按照指定的格式格式化为字符串
     * @param date 日期
     * @param format 格式方式
     * @return 返回日期字符串
     */
    public static String format(Date date,String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String dateString = sdf.format(date);
        return dateString;
    }

}

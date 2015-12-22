/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.htp.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


/**
 * 时间工具(Concurrent)
 * @author zhangrc
 *
 */
public class DatetimeUtils {
	public static final String YMDHMS = "yyyy-MM-dd HH:mm:ss";
	public static final String YMDHM = "yyyy-MM-dd HH:mm";
    public static final String YMD = "yyyy-MM-dd";
    public static final String HMS = "HH:mm:ss";
    public static final String China_YM = "yyyy年MM月";
    public static final String EASY_YMDHS = "yyyyMMddHHmmss";
    public static final String MDHM ="MM-dd HH:mm";
	public static final String DeafultPattrn = EASY_YMDHS;
    
    //与volatile ConcurrentHashMap<string ThradLocal<>> 相比  现在用的不用考虑线程安全 效率且提高 
	private static ThreadLocal<Map<String, SimpleDateFormat>> sdfMapThreadLocal = new ThreadLocal<Map<String,SimpleDateFormat>>();
    
    public static SimpleDateFormat getSimpleDateFormat(String pattern) {
    	Map<String, SimpleDateFormat> sdfMap = sdfMapThreadLocal.get();
    	if (sdfMap == null) {
    		sdfMap = new HashMap<String, SimpleDateFormat>();
    	}
    	SimpleDateFormat sdf = sdfMap.get(pattern);
    	if (sdf == null) {
    		sdf = new SimpleDateFormat(pattern, Locale.US);
    		sdfMap.put(pattern, sdf);
    	}
    	return sdf;
    }
    
    public static Date parse(String datetime) throws ParseException {
    	SimpleDateFormat sdf = getSimpleDateFormat(DeafultPattrn);
    	return sdf.parse(datetime);
    }
    
    public static Date parse(String datetime, String pattern) throws ParseException {
    	SimpleDateFormat sdf = getSimpleDateFormat(pattern);
    	return sdf.parse(datetime);
    }
   

    /**
     * 获取格式化后的当前日期字符串
     *
     * @return
     */
    public static String getCurrentDateTime() {
        SimpleDateFormat sdf = getSimpleDateFormat(DeafultPattrn);
        return sdf.format(new Date());
    }

    /**
     * 获取指定格式的当前时间字符串
     *
     * @return
     */
    public static String getCurrentDateTime(String pattern) {
        SimpleDateFormat sdf = getSimpleDateFormat(pattern);
        return sdf.format(new Date());
    }

    /**
     * @param date
     * @return 格式化后的时�?     
     * */
    public static String formatDate(Date date) {
    	 SimpleDateFormat sdf = getSimpleDateFormat(DeafultPattrn);
         return sdf.format(date);
    }
    
    /**
     * 
     * @param pattern 格式
     * @param date 时间
     * @return 格式化后的时�?     
     * */
    public static String formatDate(Date date, String pattern) {
        SimpleDateFormat sdf = getSimpleDateFormat(pattern);
        return sdf.format(date);
    }

    /**
     * 格式化当前时间
     * @param format
     * @return
     */
    public static String getDataTime(String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(new Date());
    }

}

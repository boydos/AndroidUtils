package com.ds.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
	 private static final String DefaultTimeFormat = "yyyy/MM/dd HH:mm:ss";
	 private static final String LOG_TAG="DateUtils";
	 private static final Locale DefaultLocale =Locale.CHINA;
	 
	 public static String getDate(long time) {
		 return getDate(time, DefaultTimeFormat);
	 }
	 
	 public static String  getDate(long time,String format) {
          Calendar cal = Calendar.getInstance();
          cal.setTimeInMillis(time);
          Date date = cal.getTime();
          if(StringUtils.isEmpty(format)) {
        	  format =DefaultTimeFormat;
          }
          SimpleDateFormat timeFormat = new SimpleDateFormat(format,DefaultLocale);
          return timeFormat.format(date);
     }
	 
	 public static long parseDate(String time) {
		 return parseDate(time, DefaultTimeFormat);
	 }
	 
	 public static long parseDate(String time,String format) {
		 if(StringUtils.isEmpty(format)) {
       	  format =DefaultTimeFormat;
         }
		 SimpleDateFormat timeFormat = new SimpleDateFormat(format,DefaultLocale);
		 try {
			Date date=timeFormat.parse(time);
			return date.getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			LogUtils.d(LOG_TAG, String.format("ERROR --format=%s --message=%s", format,e.getMessage()));
		}
		return -1;
	 }
	 
	 public static String changeFormat(String time,String from,String to) {
		return getDate(parseDate(time, from),to);
	 }
	 public static long getBeforeDay(int before) {
		 return getBeforeDay(System.currentTimeMillis(),before);
	 }
	 public static long getBeforeDay(long time,int before) {
		 Calendar cal = Calendar.getInstance();
		 cal.setTimeInMillis(time);
		 cal.set(Calendar.DATE, cal.get(Calendar.DATE)-before);
		 return cal.getTimeInMillis();
	 }
	 
	 public static long getBeforeMonth(int before) {
		 return getBeforeMonth(System.currentTimeMillis(),before);
	 }
	 public static long getBeforeMonth(long time,int before) {
		 Calendar cal = Calendar.getInstance();
		 cal.setTimeInMillis(time);
		 cal.set(Calendar.MONTH, cal.get(Calendar.MONTH)-before);
		 return cal.getTimeInMillis();
	 }
	 
	 public static long getBeforeYear(int before) {
		 return getBeforeYear(System.currentTimeMillis(),before);
	 }
	 public static long getBeforeYear(long time,int before) {
		 Calendar cal = Calendar.getInstance();
		 cal.setTimeInMillis(time);
		 cal.set(Calendar.YEAR, cal.get(Calendar.YEAR)-before);
		 return cal.getTimeInMillis();
	 }
}

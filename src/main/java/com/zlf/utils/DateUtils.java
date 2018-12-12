package com.zlf.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 * @author Administrator
 *
 */
public class DateUtils {

	//获取传入的日期是几月份
	public static String getMonthFromDate(Date date){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		String string = sdf.format(date);
		String month = string.substring(5, 7);
		return month;
	}
	
	//获取传入的值是周几
	public static int dayForWeek(String pTime) throws Exception { 
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");  
		 Calendar c = Calendar.getInstance();  
		 c.setTime(format.parse(pTime));  
		 int dayForWeek = 0;  
		 if(c.get(Calendar.DAY_OF_WEEK) == 1){  
		  dayForWeek = 7;  
		 }else{  
		  dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;  
		 }  
//		 System.out.println("入参："+pTime+"出产："+dayForWeek);
		 return dayForWeek;  
		}  
//	public static void main(String[] args) throws Exception {
//		int week = DateUtils.dayForWeek("2017-09-06");
//		System.out.println(week);
//	}
}

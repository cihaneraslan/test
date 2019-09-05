package com.cgiser.moka.action;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Hello world!
 * 
 */
public class App {
	public static void main(String[] args) {
//		Calendar calendar = Calendar.getInstance();
//		calendar.setTime(new Date());
//		System.out.println(calendar.get(Calendar.DAY_OF_MONTH));// 今天的日期
//		calendar.set(Calendar.DAY_OF_MONTH,
//				calendar.get(Calendar.DAY_OF_MONTH) + 1);// 让日期加1
//		System.out.println(calendar.getTime());// 加1之后的日期Top
//		Date date = new Date();
//		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
//		String str = dateFormat.format(date);
//		Date date1 = new Date(date.getYear(),date.getMonth(),date.getDay());
//		str = dateFormat.format(date1);
//		System.out.println(str);// 加1之后的日期Top
		Calendar curDate = Calendar.getInstance();
		Calendar tommorowDate = new GregorianCalendar(curDate
		.get(Calendar.YEAR), curDate.get(Calendar.MONTH),
		curDate.get(Calendar.DATE) + 1, 0, 0, 0);
		System.out.println((tommorowDate.getTimeInMillis() - curDate
		.getTimeInMillis()) / 1000);
	}
}

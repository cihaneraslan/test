/**
 * $Id$
 *
 *
 */
package com.cgiser.moka.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * @Title: StringUtil.java
 * @Copyright: Copyright (c) 2009
 * @Description: <br>工具类
 *               <br>
 * @Company: 互动
 * @Created on 2010-10-27 下午02:33:22
 * @author  liaoxiandong
 * @version $Revision: 1.0 $
 * @see　HISTORY
 * @since 1.0
 */
public class StringUtils {
	/**
	 * 时间转换成字符串：带format
	 * formatStr:yyyyMMddHHmmss、yyyyMMddHHmmssSSS、yyyyMMdd等等
	 * @param format
	 * @param date
	 * @return
	 */
	 public static String date2Str(String format,Date date){
		 	if(date == null){
		 		return "";
		 	}
	        SimpleDateFormat sdf = new SimpleDateFormat(format);
	        return sdf.format(date); 
	    }
	 /**
	  * 转换成时间戳
	  * @param date
	  * @return
	  */
	 public static String date2TimeStream(Date date){
		 if(date == null){
		 		return "";
		 	}
		long time= date.getTime();
		String timeStr = (time+"").trim();
		 return timeStr;
	 }
	 /**
	  * 把时间戳转换成时间格式的串
	  * @param datelong
	  * @param format
	  * @return
	  */
	 public static String long2DateStr(long datelong,String format){
		Date date= new Date(datelong);
		return date2Str(format, date);
	 }
	/**
	 * 验证用户名是否有效(由数字和26个英文字母或者下划线组成的字符串)
	 */
	public static boolean checkUserNameValid(String name) {
		return Pattern.matches(
				"^(?!_)(?!.*?_$)[a-zA-Z0-9_\\u4e00-\\u9fa5]{2,6}+$", name);
	}
	 /**
	  * test
	  * @param args
	  */
	 public static void main(String[] args){
		 System.out.println(StringUtils.checkUserNameValid("大家好"));
		 /*long l1 =Long.parseLong("1289468005468");
		 long l2 =Long.parseLong("1289467240609");
		 long l3 =l1 -l2;
		 System.out.println(l3);*/
	 }
}

/**
 * $Id$
 *
 *
 */
package com.cgiser.moka.common.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * @Title: ServletUtil.java
 * @Copyright: Copyright (c) 2009
 * @Description: <br>servlet 工具类
 *               <br>
 * @Company: 互动
 * @Created on 2010-10-25 下午02:11:46
 * @author  liaoxiandong
 * @version $Revision: 1.0 $
 * @see　HISTORY
 * @since 1.0
 */
public class ServletUtil {
	/**
	 * 
	 * @param request
	 * @param paraName
	 * @param defaultValue
	 * @return
	 */
	//TODO:其他包中是否有此工具类？
	public static   String getDefaultValue(HttpServletRequest request,
			String paraName, String defaultValue) {
		
		String result = request.getParameter(paraName) == null ? "" : request
				.getParameter(paraName).trim();
		if (result.equals("")) {
			return defaultValue;
		}
		return result;
	}
	/**
	 * 获取访问IP
	 * @return
	 */
	public static String getUserIP(HttpServletRequest request) {

		String ip = request.getRemoteAddr();
		//获取： x-forwarded-for
		String originIP = request.getHeader("x-forwarded-for");
		//获取：Proxy-Client-IP
		if (originIP == null || originIP.length() == 0 || "unknown".equalsIgnoreCase(originIP)) {
			originIP = request.getHeader("Proxy-Client-IP");
		} 
		//获取：WL-Proxy-Client-IP
		if (originIP == null || originIP.length() == 0 || "unknown".equalsIgnoreCase(originIP)) {
			originIP = request.getHeader("WL-Proxy-Client-IP");
		} 
		//判断是否有最原始的ip：没有就返回RemoteAddr;
		if (originIP == null || originIP.length() == 0 || "unknown".equalsIgnoreCase(originIP)) {
			return ip;
		} else {
			if (originIP != null && originIP.split(",").length > 1) {// >1
				//取X-Forwarded-For中第一个非unknown的有效IP字符串
				String[] ips = originIP.split(",");
					for(int i=0;i<ips.length;i++){
						originIP = originIP.split(",")[i];
						if(!"unknown".equalsIgnoreCase(originIP)){
							break;//退出循环
						}
					}
			}
			if (originIP != null && originIP.length() > 15) {
				originIP = originIP.substring(0, 15);
			}
			return originIP;
		}
		
	}
	
	
	
	

	
	
	
	/**
	 * enCode URL
	 * @param content
	 * @return
	 */
	public static String encodeCHStr(String content) {

		int flag = 0;

		byte b[];

		int chlen = 0, strlen = 0;

		StringBuffer urlBuffer = new StringBuffer();

		for (int i = 0; i < content.length(); i++) {
			b = String.valueOf(content.charAt(i)).getBytes();
			chlen = b.length;
			if (chlen > 1) {
			    //mod by liaoxiandong 2011.11.15 调用统一的方法
			    urlBuffer.append(ResponseUrlUtil.encodeUrl(String.valueOf(content.charAt(i)), "UTF-8"));
			} else {
				urlBuffer.append(String.valueOf(content.charAt(i)));
			}
		}

		return urlBuffer.toString();
	}
}

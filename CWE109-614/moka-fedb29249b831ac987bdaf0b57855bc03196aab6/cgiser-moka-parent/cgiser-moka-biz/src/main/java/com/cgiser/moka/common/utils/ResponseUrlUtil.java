/**
 * $Id$
 *
 *
 */
package com.cgiser.moka.common.utils;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * @Title: ResponseUrlUtil.java
 * @Copyright: Copyright (c) 2009
 * @Description: url处理工具类<br>
 *               所有的encode和decode都必须使用这个类的方法<br>
 * @Company: 互动百科
 * @Created on 2011-3-28 下午03:08:05
 * @author  liaoxiandong
 * @version $Revision: 1.0 $
 * @see　HISTORY
 * @since 1.0
 */
public class ResponseUrlUtil {
	private final static Logger logger = LoggerFactory.getLogger(ResponseUrlUtil.class);
	/**
	 * 根据ifencode判断是否对参数的值做encode
	 * @param url
	 * @param parameters
	 * @param ifencode
	 * @return
	 */
	public static String constructUrl( String url,
	         Map<String, String> parameters,boolean ifencode) {
	        final StringBuilder builder = new StringBuilder(
	            parameters.size() * 40 + 100);
	        boolean isFirst = true;
	        
	        builder.append(url);
	        
	        for (final Map.Entry<String, String> entry : parameters.entrySet()) {
	            if (entry.getValue() != null) {
	                if (isFirst) {
	                    builder.append(url.contains("?") ? "&" : "?");
	                    isFirst = false;
	                } else {
	                    builder.append("&");   
	                }
	                builder.append(entry.getKey());
	                builder.append("=");
	                if(ifencode){
	                	try {
	                		builder.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
	                	} catch (final Exception e) {
	                		builder.append(entry.getValue());
	                	}
	                }else{
	                	builder.append(entry.getValue());
	                }
	            }
	        }
	        return builder.toString();
	}
	/**
	 * 对参数的值不做encode
	 * @param url
	 * @param parameters
	 * @return
	 */
	public static String constructUrl( String url,
	         Map<String, String> parameters){
		return constructUrl( url,parameters,false);
	}
	
	/**
	 * encode url
	 * @param url
	 * @return
	 */
	public static String encodeUrl(String url) {
		return encodeUrl(url,"UTF-8");
	}
	/**
	 * dcode url
	 * @param url
	 * @return
	 */
	public static String dcodeUrl(String url){
		return dcodeUrl(url,"UTF-8");
	}
	/**
	 * 
	 * @param url
	 * @param formart
	 * @return
	 */
	public static String encodeUrl(String url,String formart) {
	        if (url == null) {
	            return null;
	        }
	        try {
	            return URLEncoder.encode(url, formart);
	        } catch (Exception e) {
	            logger.error("encode Url["+url+"] error!",e);
	            return url;
	        }
	    }
	/**
	 * 
	 * @param url
	 * @param formart
	 * @return
	 */
    public static String dcodeUrl(String url,String formart){
        if (url == null) {
            return null;
        }
        try {
            return URLDecoder.decode(url,formart);
        } catch (Exception e) {
            logger.error("decode Url["+url+"] error!",e);
            return url;
        }
    }	
	/**
	 * 先decode再encode
	 * @param url
	 * @return
	 * @deprecated
	 * 此方法不再使用
	 */
	public static String dcodeAndEcodeUrl(String url){
		if (url == null) {
			return null;
		}
		try {
			url = URLDecoder.decode(url,"UTF-8");
			return URLEncoder.encode(url, "UTF-8");
		} catch (Exception e) {
			return url;
		}
	}
	
	public static void main(String args[]){
		//String url ="http://w.baike.com/8fbd2eace6d241759f60289d5b746f2a.html";
	/*	String url ="http://www.baike.com/z/w/bingdu/index.html?prd=shouye_toutiao&ji=okde";
		Map map =new HashMap();
		map.put("ticket","ST-8-0gFk67vQMYHUEVDU6n7e-sso");
		map.put("serviceori","http://i.baike.com/lidn.do?arg=deji&arg2=dim");
		System.out.println(constructUrl(url,map,true));*/
	/*	String url =dcodeUrl("http%3A%2F%2Fpassport.baike.com%2Flogout.do%3Furl%3Dhttp%253A%252F%252Fwww.baike.com%252F");
		System.out.println("url:"+url);
		System.out.println(url.indexOf(HDSSOServerConstant.URL_LOGOUT)>-1);*/
	    System.out.println(encodeUrl("3E06476881EE507E7ADB51B9B53D24D6"));
	}	
}

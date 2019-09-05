package com.cgiser.moka.tencent.sdk;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import com.cgiser.moka.manager.support.HttpAccessClient;

public class TencentSdk {
	private String product = "http://openapi.tencentyun.com/mpay/buy_goods_m";
	private String sandbox = "http://119.147.19.43/mpay/buy_goods_m";
	private String appid = "1101232786";
	private String env = "sandbox";
	private HttpAccessClient httpAccessClient;

	public int checkLogin(String uid, String token) {
		return 1;
	}
	public JSONObject buyGoods(Map<String, String> parmMap){
		try {
			parmMap.put("appid", appid);
			parmMap.put("ts", String.valueOf(System.currentTimeMillis()/1000));
			
			
			String sig = SnsSigCheck.makeSig("POST", "/mpay/buy_goods_m", parmMap, "G77xIApCWkBpOl0Y&");
			parmMap.put("sig", sig);
			StringBuilder sb = new StringBuilder(64);
	        sb.append("http").append("://").append("119.147.19.43").append("/mpay/buy_goods_m");
	        String url = sb.toString(); 

	        // cookie
	        HashMap<String, String> cookies = new HashMap<String, String>();
			cookies.put("session_id", "openid");
			cookies.put("session_type", "kp_actoken");
			cookies.put("org_loc", "/mpay/buy_goods_m");
	        long startTime = System.currentTimeMillis();
			
			//通过调用以下方法，可以打印出最终发送到openapi服务器的请求参数以及url，默认注释
			//printRequest(url,method,params);
			
	        // 发送请求
	        String resp = SnsNetwork.postRequest(url, parmMap, cookies, "http");
			return JSONObject.fromObject(resp);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 获取WDJ服务器返回的结果
	 * 
	 * @param jsonStr
	 * @return
	 * @throws Exception
	 */
	private int GetResult(String jsonStr) throws Exception {
		// Pattern p = Pattern.compile("(?<=\"ErrorCode\":\")\\d{1,3}(?=\")");
		// Matcher m = p.matcher(jsonStr);
		// m.find();
		// return Integer.parseInt(m.group());

		// 这里需要引入JSON-LIB包内的JAR
		if (jsonStr.equals("true")) {
			return 1;
		}
		return 0;
	}

	/**
	 * 发送GET请求并获取结果
	 * 
	 * @param getUrl
	 * @return
	 * @throws Exception
	 */
	private String HttpGetGo(Map<String,String> parmMap) throws Exception {
		StringBuffer cookies = new StringBuffer();
		cookies.append("session_id=openid;");
		cookies.append("session_type=kp_actoken;");
		cookies.append("org_loc=/mpay/buy_goods_m;");
//		cookies.append("appip=183.60.233.156;");
		String content = httpAccessClient.requestDataByUrl(env
				.equals("sandbox") ? sandbox : product, "POST", parmMap,
						URLEncoder.encode(cookies.toString(), "utf-8"));
		return content;
	}

	public HttpAccessClient getHttpAccessClient() {
		return httpAccessClient;
	}

	public void setHttpAccessClient(HttpAccessClient httpAccessClient) {
		this.httpAccessClient = httpAccessClient;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getEnv() {
		return env;
	}

	public void setEnv(String env) {
		this.env = env;
	}
}

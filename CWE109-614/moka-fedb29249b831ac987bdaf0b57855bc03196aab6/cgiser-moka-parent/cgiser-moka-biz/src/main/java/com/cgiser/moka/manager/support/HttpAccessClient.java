package com.cgiser.moka.manager.support;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HttpAccessClient {
	private static final Logger logger = LoggerFactory.getLogger(HttpAccessClient.class);

	  private static MultiThreadedHttpConnectionManager connectionManager = null;// 多线程管理器

	    private static HttpClient httpClient = null;

	    private int maxThreadsTotal = 128;// 最大线程数

	    private int maxThreadsPerHost = 32; // 分配给每个客户端的最大线程数

	    private int connectionTimeout = 15000;// 连接超时时间,毫秒

	    private int soTimeout = 14000;// 读取数据超时时间，毫秒

	    public void init() {
	        connectionManager = new MultiThreadedHttpConnectionManager();
	        HttpConnectionManagerParams params = new HttpConnectionManagerParams();
	        params.setConnectionTimeout(connectionTimeout);
	        params.setMaxTotalConnections(maxThreadsTotal);
	        params.setSoTimeout(soTimeout);
	        if (maxThreadsTotal > maxThreadsPerHost) {
	            params.setDefaultMaxConnectionsPerHost(maxThreadsPerHost);
	        } else {
	            params.setDefaultMaxConnectionsPerHost(maxThreadsTotal);
	        }
	        connectionManager.setParams(params);
	        httpClient = new HttpClient(connectionManager);
	        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(connectionTimeout);
	        httpClient.getHttpConnectionManager().getParams().setSoTimeout(soTimeout);
	    }

	    /*
	     * (non-Javadoc)
	     * 
	     * @see
	     * com.hudong.appagent.client.access.AccessClient#requestDataByUrl(java.
	     * lang.String, com.hudong.appagent.client.bean.enu.RequestTypeEnum,
	     * com.hudong.appagent.client.bean.enu.EncodeEnum, java.util.Map)
	     */
	    public String requestDataByUrl(String url, String callType, Map<String, String> parmMap,String cookies) {
	        if (logger.isDebugEnabled()) {
	            logger.debug("in requestDataByUrl,url:" + url + "\r\ncallType:" + callType + "\r\nparmMap:" + parmMap);
	        }
	        if (StringUtils.isBlank(callType)) {
	            // 默认get方式
	            callType = "GET";
	        }
	        // get方式
	        if (url != null && (!url.equals("")) && "GET".equals(callType)) {
	            return this.callByGet(url, callType, parmMap,cookies);
	        } else if (url != null && (!url.equals("")) && "POST".equals(callType)) {
	            // post方式
	            return this.callByPost(url, callType, parmMap,cookies);
	        } else {
	            // 参数异常
	            return null;
	        }

	    }

	    /**
	     * get方式访问
	     * 
	     * @param url
	     * @param callType
	     * @param parmMap
	     * @return
	     */
	    private String callByGet(String url, String callType, Map<String, String> parmMap,String cookies) {
	        String sendUrl = ParameterAssemblyUtil.constructUrl(url, parmMap);
	        if (logger.isDebugEnabled()) {
	            logger.debug("in get,sendUrl:" + sendUrl);
	        }
	        GetMethod getMethod = new GetMethod(sendUrl);
	        getMethod.setRequestHeader("cookie", cookies);
	        int statusCode = 0;
	        try {
	            statusCode = httpClient.executeMethod(getMethod);
	            if (logger.isDebugEnabled()) {
	                logger.debug("in get,statusCode:" + statusCode);
	            }
	            if (statusCode != HttpStatus.SC_OK) {
	                // 访问失败
	                logger.error("in get,访问appagent失败。网络问题。statusCode：" + statusCode);
	                return null;
	            }
	            InputStream inputStream = getMethod.getResponseBodyAsStream();
	            if (inputStream == null) {
	                // 访问正常：获取到的数据为空
	                logger.error("in get,从appagent返回的数据为空！");
	                return null;
	            }
	            String rtn = IOUtils.toString(inputStream, "utf-8");
	            return rtn;
	        } catch (HttpException e) {
	            logger.error("get方式访问appagent失败！",e);
	            return null;
	        } catch (IOException e) {
	            logger.error("get方式访问appagent失败！",e);
	            return null;
	        } finally {
	            getMethod.releaseConnection();
	        }
	    }

	    /**
	     * post方式访问
	     * 
	     * @param url
	     * @param callType
	     * @param parmMap
	     * @return
	     */
	    private String callByPost(String url, String callType, Map<String, String> parmMap,String cookies) {
	        if (logger.isDebugEnabled()) {
	            logger.debug("in post,sendUrl:" + url);
	        }
	        PostMethod p = new UTF8PostMethod(url);
	        p.setRequestHeader("cookie", cookies);
	        if (parmMap != null) {
	            NameValuePair[] params = new NameValuePair[parmMap.size()];
	            AtomicInteger atomicInteger = new AtomicInteger(0);
	            for (Map.Entry<String, String> parm : parmMap.entrySet()) {
	                NameValuePair parmValue = new NameValuePair(parm.getKey(), parm.getValue());
	                params[atomicInteger.getAndIncrement()] = parmValue;
	            }

	            p.setRequestBody(params);
	        }
	        try {
	            int statusCode = httpClient.executeMethod(p);
	            logger.debug("in get,statusCode:" + statusCode);
	            if (statusCode != HttpStatus.SC_OK) {
	                // 异常
	                logger.error("in post,访问appagent失败。网络问题。statusCode：" + statusCode);
	                return null;
	            }
	            InputStream inputStream = p.getResponseBodyAsStream();
	            if (inputStream == null) {
	                // 访问正常
	                logger.error("in post,从appagent返回的数据为空！");
	                return null;
	            }
	            String rtn = IOUtils.toString(inputStream, "utf-8");
	            return rtn;
	        } catch (HttpException e) {
	            logger.error( "post方式访问appagent失败！",e);
	            return null;
	        } catch (IOException e) {
	            logger.error("post方式访问appagent失败！",e);
	            return null;
	        } finally {
	            p.releaseConnection();
	        }
	    }

	    public int getMaxThreadsTotal() {
	        return maxThreadsTotal;
	    }

	    public void setMaxThreadsTotal(int maxThreadsTotal) {
	        this.maxThreadsTotal = maxThreadsTotal;
	    }

	    public int getMaxThreadsPerHost() {
	        return maxThreadsPerHost;
	    }

	    public void setMaxThreadsPerHost(int maxThreadsPerHost) {
	        this.maxThreadsPerHost = maxThreadsPerHost;
	    }

	    public int getConnectionTimeout() {
	        return connectionTimeout;
	    }

	    public void setConnectionTimeout(int connectionTimeout) {
	        this.connectionTimeout = connectionTimeout;
	    }

	    public int getSoTimeout() {
	        return soTimeout;
	    }

	    public void setSoTimeout(int soTimeout) {
	        this.soTimeout = soTimeout;
	    }
}

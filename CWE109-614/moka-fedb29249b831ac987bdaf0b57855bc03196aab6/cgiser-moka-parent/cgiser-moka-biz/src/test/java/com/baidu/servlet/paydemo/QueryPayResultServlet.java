package com.baidu.servlet.paydemo;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.URLClassLoader;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 支付结果查询的demo类
 * 
 * @author zhanghaiyun
 * @since 2013-12-03
 */
public class QueryPayResultServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        // TODO Auto-generated method stub
        Logger logger = Logger.getLogger("queryResult");
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        // 构造参数
        String transdata = "{\"appid\":" + Constants.appid + ",\"exorderno\":\"1385614772792\"}";
        String sign = Md5Util.getMD5((transdata + Constants.appkey).getBytes(), false);
        String param = "r=FromIapppayToUserAction&transdata=" + transdata + "&sign=" + sign;
        logger.log(Level.WARNING, param);
        URL url = new URL("http://gameopen.baidu.com/index.php");
        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();

        // 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在
        // http正文内，因此需要设为true, 默认情况下是false;
        urlConn.setDoOutput(true);

        // 设置是否从httpUrlConnection读入，默认情况下是true;
        urlConn.setDoInput(true);

        // Post 请求不能使用缓存
        urlConn.setUseCaches(false);

        // 设定传送的内容类型是可序列化的java对象
        // (如果不设此项,在传送序列化对象时,当WEB服务默认的不是这种类型时可能抛java.io.EOFException)
        urlConn.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        urlConn.setRequestProperty("Connection", "Keep-Alive");
        // 设定请求的方法为"POST"，默认是GET
        urlConn.setRequestMethod("POST");

        // 连接，上面对urlConn的所有配置必须要在connect之前完成，
        urlConn.connect();
        // 此处getOutputStream会隐含的进行connect (即：如同调用上面的connect()方法，
        // 所以在开发中不调用上述的connect()也可以)。
        OutputStream outStrm = urlConn.getOutputStream();
        // 向对象输出流写出数据，这些数据将存到内存缓冲区中
        outStrm.write(param.getBytes());
        // 调用HttpURLConnection连接对象的getInputStream()函数,
        // 将内存缓冲区中封装好的完整的HTTP请求电文发送到服务端。
        InputStream inStrm;
        int respCode = urlConn.getResponseCode();
        if (200 == respCode) {
            inStrm = urlConn.getInputStream();
        } else {
            inStrm = urlConn.getErrorStream();
        }

        StringBuilder sb = new StringBuilder();
        BufferedReader bf = new BufferedReader(new InputStreamReader(inStrm), 1000);
        String str = null;
        while ((str = bf.readLine()) != null) {
            sb.append(str);
        }
        logger.log(Level.WARNING, sb.toString());
        response.getWriter().println(sb.toString());
        bf.close();
    }
}

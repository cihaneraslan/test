package com.baidu.servlet.paydemo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 处理支付结果通知的Demo类
 * 
 * @author zhanghaiyun
 * @since 2013-12-03
 */
public class PayNotifyServlet extends HttpServlet {

  	@Override
  	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
        Logger logger = Logger.getLogger("notify");
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
        String transdata = request.getParameter("transdata");
        String sign = request.getParameter("sign");
        logger.log(Level.WARNING, transdata);
		if (sign.equals(Md5Util.getMD5((transdata+Constants.appkey).getBytes(), false))) {
		    //给支付服务器应答
          response.getWriter().println("SUCCESS");
          logger.log(Level.WARNING, "success");
		  //签名正确，在这里处理业务
		}else{
            //签名失败
          response.getWriter().println("fail");
          logger.log(Level.WARNING, "fail");
           }
	}
}

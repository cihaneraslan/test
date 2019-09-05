/**
 * $Id$
 *
 *
 */
package com.cgiser.moka.common.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @Title: Dom4jUtil.java
 * @Copyright: Copyright (c) 2009
 * @Description: <br>
 * <br>
 * @Company: 互动
 * @Created on 2010-10-22 下午01:34:02
 * @author liaoxiandong
 * @version $Revision: 1.0 $
 * @see　HISTORY
 * @since 1.0
 */
public class Dom4jUtil {
    static Logger logger = LoggerFactory.getLogger(Dom4jUtil.class);

    /**
     * 从XML Document对象输出XML串到客户端网页
     * 
     * @param response
     * @param doc
     */
    public static void outElementToXML(HttpServletResponse response, Element element) {
        // 以下代码请注意编码顺序
        String resultXml = element.asXML();
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/xml;charset=utf-8");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        PrintWriter out = null;
        try {
            // String xml =new String(resultXml.getBytes(),"UTF-8");
            out = response.getWriter();
            out.print(resultXml);
        } catch (IOException e) {
            logger.error("输出验证结果出错！", e);
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }
        }
    }

    /**
     * 
     * @param response
     * @param hm
     * @param elenames
     * @return
     */
    public static Element makeSimpleXMLElement(HttpServletResponse response, Map<String, String> hm, String[] elenames) {

        // 生成XML文档
        Element root = DocumentHelper.createElement("response");
        for (int i = 0; i < elenames.length; i++) {
            root.addElement(elenames[i]).addCDATA(hm.get(elenames[i]));
        }
        return root;

    }

    /**
     * 从XML Document对象输出XML串到客户端网页
     * 
     * @param response
     * @param doc
     * @deprecated
     */
    public static void outDocToXML(HttpServletResponse response, Document doc) {
        // 以下代码请注意编码顺序
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/xml;charset=UTF-8");

        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        PrintWriter out = null;

        StringWriter writer = new StringWriter();

        OutputFormat format = OutputFormat.createPrettyPrint(); //
        format.setEncoding("UTF-8");

        XMLWriter xmlwriter = new XMLWriter(writer, format);

        try {
            out = response.getWriter();
            xmlwriter.write(doc);
        } catch (Exception e) {

            logger.error(e.getMessage(), e.fillInStackTrace());
        }
        out.close();
    }

    /**
     * 生成简单XML（单层）
     * 
     * @param response
     * @param hm
     * @param elenames
     * @return
     * @deprecated
     */
    public static Document makeSimpleXMLDoc(HttpServletResponse response, HashMap hm, String[] elenames) {

        // 建立document对象
        Document doc = DocumentHelper.createDocument();

        // 生成XML文档
        Element root = DocumentHelper.createElement("response");

        // Element eTest = DocumentHelper.createElement("cas:serviceResponse" );
        for (int i = 0; i < elenames.length; i++) {
            root.addElement(elenames[i]).addCDATA((String) hm.get(elenames[i]));
        }

        return doc;

    }

}

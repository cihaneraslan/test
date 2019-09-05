package com.cgiser.moka.mm.goods.action;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.cgiser.core.common.cache.mem.MemCachedManager;
import com.cgiser.keel.http.utils.HttpSpringUtils;
import com.cgiser.moka.action.AbstractAction;
import com.cgiser.moka.manager.AchievementManager;
import com.cgiser.moka.manager.ProductManager;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.manager.SalaryManager;
import com.cgiser.moka.manager.WDJProductLogManager;
import com.cgiser.moka.model.Product;
import com.cgiser.moka.model.ProductLog;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.model.SalaryEnum;
import com.cgiser.moka.model.UserAchievement;
import com.cgiser.moka.model.WdjProductLog;
import com.cgiser.moka.result.VipInfo;

public class PaymentVerifyAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger("mmstore");

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			
			StringBuffer info=new java.lang.StringBuffer();
		    InputStream in=request.getInputStream();
		    BufferedInputStream buf=new BufferedInputStream(in);
		    byte[] buffer=new byte[1024]; 
		    int iRead;
		    while((iRead=buf.read(buffer))!=-1)   
		    {
		    	info.append(new String(buffer,0,iRead,"UTF-8"));
		    }
		    response.setCharacterEncoding("UTF-8");
		    response.setContentType("text/xml");
		    StringBuffer result =new StringBuffer();
		    result.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		    
			if (StringUtils.isEmpty(info.toString())) {
				result.append("<response>");
				result.append("<hRet>1</hRet>");
				result.append("<message>failure</message>");
				result.append("</response>");
				XMLWriter w = new XMLWriter(response.getWriter());
				Document doc = DocumentHelper.parseText(result.toString());
			    w.write(doc);
			    w.flush();
			    w.close();
				return null;
			}
			Map<String,String> map = readStringXmlOut(info.toString());
			String cooOrderSerial = map.get("cpparam");
			ProductManager productManager = (ProductManager) HttpSpringUtils
					.getBean("productManager");
			WDJProductLogManager wdjProductLogManager = (WDJProductLogManager) HttpSpringUtils
					.getBean("wdjProductLogManager");
			WdjProductLog wdjProductLog = wdjProductLogManager
					.getWdjProductLogByCooOrderSerial(cooOrderSerial);
			if (wdjProductLog == null) {
				result.append("<response>");
				result.append("<hRet>1</hRet>");
				result.append("<message>failure</message>");
				result.append("</response>");
				XMLWriter w = new XMLWriter(response.getWriter());
				Document doc = DocumentHelper.parseText(result.toString());
			    w.write(doc);
			    w.flush();
			    w.close();
				return null;
			}
			if (wdjProductLog.getPayment() == 1
					&& wdjProductLog.getStatus() == 1) {
				result.append("<response>");
				result.append("<hRet>0</hRet>");
				result.append("<message>successful</message>");
				result.append("</response>");
				XMLWriter w = new XMLWriter(response.getWriter());
				Document doc = DocumentHelper.parseText(result.toString());
			    w.write(doc);
			    w.flush();
			    w.close();
				return null;
			}
			String productId = wdjProductLog.getProductId();
			Product product = productManager.getProductById(productId);
			if (product == null) {
				result.append("<response>");
				result.append("<hRet>1</hRet>");
				result.append("<message>failure</message>");
				result.append("</response>");
				XMLWriter w = new XMLWriter(response.getWriter());
				Document doc = DocumentHelper.parseText(result.toString());
			    w.write(doc);
			    w.flush();
			    w.close();
				return null;
			}
			if (wdjProductLog.getPayment() == 0) {
				if (wdjProductLogManager.updateWdjProductLogPayment(
						cooOrderSerial, info.toString(), 1, 1) < 1) {
					logger.error("修改订单支付状态失败订单号为:" + cooOrderSerial);
				}
				result.append("<response>");
				result.append("<hRet>1</hRet>");
				result.append("<message>failure</message>");
				result.append("</response>");
				XMLWriter w = new XMLWriter(response.getWriter());
				Document doc = DocumentHelper.parseText(result.toString());
			    w.write(doc);
			    w.flush();
			    w.close();
				return null;
			}
			if (wdjProductLogManager.updateWdjProductLogStatus(cooOrderSerial,
					1) < 1) {
				logger.error("修改订单发放状态失败订单号为:" + cooOrderSerial);
				result.append("<response>");
				result.append("<hRet>1</hRet>");
				result.append("<message>failure</message>");
				result.append("</response>");
				XMLWriter w = new XMLWriter(response.getWriter());
				Document doc = DocumentHelper.parseText(result.toString());
			    w.write(doc);
			    w.flush();
			    w.close();
				return null;
			} else {
				MemCachedManager payCachedManager = (MemCachedManager) HttpSpringUtils
						.getBean("payCachedManager");
				if (!payCachedManager.add("pay_" + "wdj" + cooOrderSerial,
						5 * 60, new Date())) {
					logger.error("正在验证订单有效性，请不要重复提交！");
					result.append("<response>");
					result.append("<hRet>1</hRet>");
					result.append("<message>failure</message>");
					result.append("</response>");
					XMLWriter w = new XMLWriter(response.getWriter());
					Document doc = DocumentHelper.parseText(result.toString());
				    w.write(doc);
				    w.flush();
				    w.close();
					return null;
				}
				RoleManager roleManager = (RoleManager) HttpSpringUtils
						.getBean("roleManager");
				Role role = roleManager.getRoleById(wdjProductLog.getRoleId());
				if (role == null) {
					logger.error("订单所拥有角色不存在！");
					result.append("<response>");
					result.append("<hRet>1</hRet>");
					result.append("<message>failure</message>");
					result.append("</response>");
					XMLWriter w = new XMLWriter(response.getWriter());
					Document doc = DocumentHelper.parseText(result.toString());
				    w.write(doc);
				    w.flush();
				    w.close();
					return null;
				}
				int cash = product.getValue();
				if (!roleManager.addCash(role.getRoleName(), cash)) {
					logger.error("购买产品：" + product.getIdentifier() + ";交易ID"
							+ cooOrderSerial + ";给用户加元宝失败");
				} else {
					logger.debug("给用户加元宝成功");
				}
				try {
					List<ProductLog> productLogList = productManager
							.getProductLogByRoleId(role.getRoleId());
					List<WdjProductLog> wdjProductLogs = wdjProductLogManager
							.getWdjProductLogByRoleId(role.getRoleId(), 1);
					int money = 0;
					if (!CollectionUtils.isEmpty(productLogList)) {
						for (ProductLog proLog : productLogList) {
							Product product2 = productManager
									.getProductById(proLog.getProductId());
							money = money + product2.getValue();
						}
					}
					if (!CollectionUtils.isEmpty(wdjProductLogs)) {
						for (WdjProductLog proLog : wdjProductLogs) {
							Product product2 = productManager
									.getProductById(proLog.getProductId());
							money = money + product2.getValue();
						}
					}
					int vip = VipInfo.getVipInfo(money);
					roleManager.upgradeVip(role.getRoleId(), vip);
					AchievementManager achievementManager = (AchievementManager) HttpSpringUtils
							.getBean("achievementManager");
					UserAchievement userAchievement = achievementManager
							.getUserAchievementById(role.getRoleId(), 44);
					if (userAchievement == null) {
						if (achievementManager.saveUserAchievement(44, role
								.getRoleId(), 1) > 0) {
							SalaryManager salaryManager = (SalaryManager) HttpSpringUtils
									.getBean("salaryManager");
							salaryManager.extendSalary(2, cash * 2, new Date(),
									SalaryEnum.OtherSalary, "首次充值奖励", role
											.getRoleId());
							salaryManager.extendSalary(1, 200000, new Date(),
									SalaryEnum.OtherSalary, "首次充值奖励", role
											.getRoleId());
							salaryManager.extendSalary(7, 13, new Date(),
									SalaryEnum.OtherSalary, "首次充值奖励", role
											.getRoleId());
							salaryManager.extendSalary(10, 2, new Date(),
									SalaryEnum.OtherSalary, "首次充值奖励", role
											.getRoleId());
						}

					}
				} catch (Exception e) {
					logger.error(e.getMessage(), e);// TODO: handle exception
					payCachedManager.delete("pay_" + "wdj" + cooOrderSerial);
				}
				payCachedManager.delete("pay_" + "wdj" + cooOrderSerial);
				result.append("<response>");
				result.append("<hRet>0</hRet>");
				result.append("<message>successful</message>");
				result.append("</response>");
				XMLWriter w = new XMLWriter(response.getWriter());
				Document doc = DocumentHelper.parseText(result.toString());
			    w.write(doc);
			    w.flush();
			    w.close();
				return null;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		try {
			StringBuffer result =new StringBuffer();
		    result.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		    result.append("<response>");
			result.append("<hRet>1</hRet>");
			result.append("<message>failure</message>");
			result.append("</response>");
			XMLWriter w = new XMLWriter(response.getWriter());
			Document doc = DocumentHelper.parseText(result.toString());
		    w.write(doc);
		    w.flush();
		    w.close();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);// TODO: handle exception
		}
		return null;

	}

	public static void main(String[] args) throws ParserConfigurationException,
			IOException, DocumentException {
		// DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		// DocumentBuilder db = dbf.newDocumentBuilder();
		// Document doc =
		// db.parse("<request><userId>dsadad</userId></request>");
		// NodeList list = doc.getElementsByTagName("userId");
		// System.out.println(list.item(0).getNodeValue());
		StringBuffer result =new StringBuffer();
	    result.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		result.append("<response><hRet>1</hRet>");
		result.append("<message>failure</message></response>");
		Document doc = DocumentHelper.parseText(result.toString());
		System.out.println(doc);
	}

	/**
	 * @description 将xml字符串转换成map
	 * @param xml
	 * @return Map
	 */
	@SuppressWarnings("unchecked")
	public static Map<String,String> readStringXmlOut(String xml) {
		Map<String,String> map = new HashMap<String,String>();
		Document doc = null;
		try {
			// 将字符串转为XML
			doc = DocumentHelper.parseText(xml);
			// 获取根节点
			Element rootElt = doc.getRootElement();
			// 拿到根节点的名称
			System.out.println("根节点：" + rootElt.getName());
			// 获取根节点下的子节点head
			Iterator<Element> iter = rootElt.elementIterator();
			// 遍历head节点
			while (iter.hasNext()) {
				Element recordEle = (Element) iter.next();
				map.put(recordEle.getName(), recordEle.getTextTrim());
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
}

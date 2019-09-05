package com.cgiser.moka.baidu.goods.action;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.cgiser.core.common.cache.mem.MemCachedManager;
import com.cgiser.keel.http.utils.HttpSpringUtils;
import com.cgiser.moka.action.AbstractAction;
import com.cgiser.moka.baidu.sdk.Md5Util;
import com.cgiser.moka.manager.AchievementManager;
import com.cgiser.moka.manager.BaiDuProductLogManager;
import com.cgiser.moka.manager.ProductManager;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.manager.SalaryManager;
import com.cgiser.moka.model.BaiDuProductLog;
import com.cgiser.moka.model.Product;
import com.cgiser.moka.model.ProductLog;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.model.SalaryEnum;
import com.cgiser.moka.model.UserAchievement;
import com.cgiser.moka.result.ReturnType;
import com.cgiser.moka.result.VipInfo;

public class PaymentVerifyAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger("wdjstore");

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		ReturnType<String> returnType = new ReturnType<String>();
		try {

			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			String transdata = request.getParameter("transdata");
			String sign = request.getParameter("sign");
			if (!sign
					.equals(Md5Util
							.getMD5((transdata + "sBLeO41MkRL7S2YEnU8GGsvi")
									.getBytes(), false))) {
				// 签名失败
				response.getWriter().println("FAILURE");
				return null;
			}
			
			net.sf.json.JSONObject obj = net.sf.json.JSONObject
					.fromObject(transdata);
			if( obj.getInt("result")==0){
				response.getWriter().println("FAILURE");
				return null;
			}
			String cooOrderSerial = obj.getString("exorderno");
			ProductManager productManager = (ProductManager) HttpSpringUtils
					.getBean("productManager");
			BaiDuProductLogManager baiduProductLogManager = (BaiDuProductLogManager) HttpSpringUtils
					.getBean("baiDuProductLogManager");
			BaiDuProductLog baiduProductLog = baiduProductLogManager
					.getBaiDuProductLogByCooOrderSerial(cooOrderSerial);
			if (baiduProductLog == null) {
				response.getWriter().print("fail");
				return null;
			}
			if (baiduProductLog.getPayment() == 1
					&& baiduProductLog.getStatus() == 1) {
				response.getWriter().print("success");
				return null;
			}
			String productId = baiduProductLog.getProductId();
			Product product = productManager.getProductById(productId);
			if (product == null) {
				// 当购买的产品不存在时返回true 取消掉订单，一般不存在这种情况
				response.getWriter().print("success");
				return null;
			}
			if (baiduProductLog.getPayment() == 0) {
				if (baiduProductLogManager.updateBaiDuProductLogPayment(
						cooOrderSerial, transdata, 1, 1) < 1) {
					logger.error("修改订单支付状态失败订单号为:" + cooOrderSerial);
				}
				response.getWriter().print("fail");
				return null;
			}
			if (baiduProductLogManager.updateBaiDuProductLogStatus(
					cooOrderSerial, 1) < 1) {
				logger.error("修改订单发放状态失败订单号为:" + cooOrderSerial);
				response.getWriter().print("fail");
				return null;
			} else {
				MemCachedManager payCachedManager = (MemCachedManager) HttpSpringUtils
						.getBean("payCachedManager");
				if (!payCachedManager.add("pay_"+"baidu" + cooOrderSerial, 5 * 60,
						new Date())) {
					logger.error("正在验证订单有效性，请不要重复提交！");
					response.getWriter().print("fail");
					return null;
				}
				RoleManager roleManager = (RoleManager) HttpSpringUtils
						.getBean("roleManager");
				Role role = roleManager
						.getRoleById(baiduProductLog.getRoleId());
				if (role == null) {
					logger.error("订单所拥有角色不存在！");
					response.getWriter().print("fail");
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
					List<BaiDuProductLog> baiduProductLogs = baiduProductLogManager
							.getBaiDuProductLogByRoleId(role.getRoleId(), 1);
					int money = 0;
					if (!CollectionUtils.isEmpty(productLogList)) {
						for (ProductLog proLog : productLogList) {
							Product product2 = productManager
									.getProductById(proLog.getProductId());
							money = money + product2.getValue();
						}
					}
					if (!CollectionUtils.isEmpty(baiduProductLogs)) {
						for (BaiDuProductLog proLog : baiduProductLogs) {
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
					payCachedManager.delete("pay_"+"baidu" + cooOrderSerial);
				}
				payCachedManager.delete("pay_"+"baidu" + cooOrderSerial);
				response.getWriter().print("success");
				return null;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		try {
			response.getWriter().print("fail");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);// TODO: handle exception
		}
		return null;

	}
}

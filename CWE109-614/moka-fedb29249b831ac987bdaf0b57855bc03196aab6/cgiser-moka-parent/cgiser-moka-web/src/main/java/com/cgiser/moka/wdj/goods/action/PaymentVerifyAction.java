package com.cgiser.moka.wdj.goods.action;

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
import com.cgiser.moka.common.utils.ServletUtil;
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
import com.cgiser.moka.wdj.sdk.WandouRsa;

public class PaymentVerifyAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger("wdjstore");
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		try{
			String content = ServletUtil.getDefaultValue(request, "content", null);
			String signType = ServletUtil.getDefaultValue(request, "signType", null);
			String sign = ServletUtil.getDefaultValue(request, "sign", null);	
			if(content==null||sign==null||signType==null){
				response.getWriter().print("fail");
				return null;
			}
			boolean check = WandouRsa.doCheck(content, sign);
			if(!check){
				response.getWriter().print("fail");
				return null;
			}
			net.sf.json.JSONObject obj = net.sf.json.JSONObject.fromObject(content);
			String cooOrderSerial = obj.getString("out_trade_no");
			ProductManager productManager = (ProductManager)HttpSpringUtils.getBean("productManager");
			WDJProductLogManager wdjProductLogManager = (WDJProductLogManager)HttpSpringUtils.getBean("wdjProductLogManager");
			WdjProductLog wdjProductLog = wdjProductLogManager.getWdjProductLogByCooOrderSerial(cooOrderSerial);
			if(wdjProductLog==null){
				response.getWriter().print("fail");
				return null;
			}
			if(wdjProductLog.getPayment()==1&&wdjProductLog.getStatus()==1){
				response.getWriter().print("success");
				return null;
			}
			String productId = wdjProductLog.getProductId();
			Product product = productManager.getProductById(productId);
			if(product==null){
				//当购买的产品不存在时返回true 取消掉订单，一般不存在这种情况
				response.getWriter().print("success");
				return null;
			}
			if(wdjProductLog.getPayment()==0){
				if(wdjProductLogManager.updateWdjProductLogPayment(cooOrderSerial, content, 1, 1)<1){
					logger.error("修改订单支付状态失败订单号为:"+cooOrderSerial);
				}
				response.getWriter().print("fail");
				return null;
			}
			if(wdjProductLogManager.updateWdjProductLogStatus(cooOrderSerial, 1)<1){
				logger.error("修改订单发放状态失败订单号为:"+cooOrderSerial);
				response.getWriter().print("fail");
				return null;
			}else{
				MemCachedManager payCachedManager = (MemCachedManager)HttpSpringUtils.getBean("payCachedManager");
				if(!payCachedManager.add("pay_"+"wdj"+cooOrderSerial, 5*60, new Date())){
					logger.error("正在验证订单有效性，请不要重复提交！");
					response.getWriter().print("fail");
					return null;
				}
				RoleManager roleManager = (RoleManager)HttpSpringUtils.getBean("roleManager");
				Role role = roleManager.getRoleById(wdjProductLog.getRoleId());
				if(role==null){
					logger.error("订单所拥有角色不存在！");
					response.getWriter().print("fail");
					return null;
				}
				int cash = product.getValue();
				if(!roleManager.addCash(role.getRoleName(), cash)){
					logger.error("购买产品："+product.getIdentifier()+";交易ID"+cooOrderSerial+";给用户加元宝失败");
				}else{
					logger.debug("给用户加元宝成功");
				}
				try{
					List<ProductLog> productLogList = productManager.getProductLogByRoleId(role.getRoleId());
    				List<WdjProductLog> wdjProductLogs = wdjProductLogManager.getWdjProductLogByRoleId(role.getRoleId(), 1);
    				int money = 0;
    				if(!CollectionUtils.isEmpty(productLogList)){
    					for(ProductLog proLog :productLogList){
    						Product product2 = productManager.getProductById(proLog.getProductId());
    						money = money + product2.getValue();
    					}
    				}
    				if(!CollectionUtils.isEmpty(wdjProductLogs)){
    					for(WdjProductLog proLog :wdjProductLogs){
    						Product product2 = productManager.getProductById(proLog.getProductId());
    						money = money + product2.getValue();
    					}
    				}
					int vip = VipInfo.getVipInfo(money);
	    			roleManager.upgradeVip(role.getRoleId(),vip);
	    			AchievementManager achievementManager =(AchievementManager)HttpSpringUtils.getBean("achievementManager");
	    			UserAchievement userAchievement = achievementManager.getUserAchievementById(role.getRoleId(), 44); 
					if(userAchievement==null){
						if(achievementManager.saveUserAchievement(44, role.getRoleId(),1)>0){
							SalaryManager salaryManager = (SalaryManager)HttpSpringUtils.getBean("salaryManager");
							salaryManager.extendSalary(2, cash*2, new Date(), SalaryEnum.OtherSalary,"首次充值奖励", role.getRoleId());
							salaryManager.extendSalary(1, 200000, new Date(), SalaryEnum.OtherSalary,"首次充值奖励", role.getRoleId());
							salaryManager.extendSalary(7, 13, new Date(), SalaryEnum.OtherSalary,"首次充值奖励", role.getRoleId());
							salaryManager.extendSalary(10, 2, new Date(), SalaryEnum.OtherSalary,"首次充值奖励", role.getRoleId());
						}
						
					}
    			}catch (Exception e) {
    				logger.error(e.getMessage(),e);// TODO: handle exception
    				payCachedManager.delete("pay_"+"wdj"+cooOrderSerial);
				}
    			payCachedManager.delete("pay_"+"wdj"+cooOrderSerial);
    			response.getWriter().print("success");
				return null;
			}
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		try{
			response.getWriter().print("fail");
		}catch (Exception e) {
			logger.error(e.getMessage(),e);// TODO: handle exception
		}
		return null;
		
	}
}

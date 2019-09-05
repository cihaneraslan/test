package com.cgiser.moka.apple.goods.action;

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
import com.cgiser.moka.model.Product;
import com.cgiser.moka.model.ProductLog;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.model.SalaryEnum;
import com.cgiser.moka.model.UserAchievement;
import com.cgiser.moka.result.ReturnType;
import com.cgiser.moka.result.VipInfo;

public class PaymentVerifyAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger("applestore");
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		ReturnType<String> returnType = new ReturnType<String>();
		try{
			Role role = super.getCurrentRole(request);
			if(null==role){
				returnType.setMsg("您还没登录哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			String receipt = ServletUtil.getDefaultValue(request, "receipt", null);
			if(receipt==null){
				returnType.setMsg("验证失败！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
//			HttpAccessClient httpAccessClient = (HttpAccessClient)HttpSpringUtils.getBean("httpAccessClient");
//			Map<String, String> parmMap = new HashMap<String, String>();
//	    	parmMap.put("data", "{receipt-data:"+receipt+"}");
//	    	String dataResponse = httpAccessClient.requestDataByUrl("https://sandbox.itunes.apple.com/verifyReceipt", "POST", parmMap);
			
	    	try {
	    		ProductManager productManager = (ProductManager)HttpSpringUtils.getBean("productManager");
				String dataResponse = productManager.verifyReceipt(receipt);
	            // 正常信息和异常信息返回的串结构不一样
	    		net.sf.json.JSONObject obj = net.sf.json.JSONObject.fromObject(dataResponse);
	    		
	    		if(obj == null){
					returnType.setMsg("验证失败！");
					returnType.setStatus(0);
					super.printReturnType2Response(response, returnType);
					return null;
	    		}
	    		if(obj.getString("status").equals("21007")){
	    			dataResponse = productManager.verifyReceiptForSandBox(receipt);
	    			obj = net.sf.json.JSONObject.fromObject(dataResponse);
	    		}
	    		if(obj.getString("status").equals("0")){
	    			String transaction_id ="";
	    			String product_id ="";
	    			if(obj.get("receipt")!=null){
	    				transaction_id = net.sf.json.JSONObject.fromObject(obj.get("receipt")).getString("transaction_id");
	    				product_id = net.sf.json.JSONObject.fromObject(obj.get("receipt")).getString("product_id");
	    			}
	    			ProductLog productLog = productManager.getProductLogByTransactionId(transaction_id);
	    			if(productLog==null){
	    				productManager.infoStoreProduct(role.getRoleId(), product_id,transaction_id,dataResponse,receipt, 0,0);
	    				productLog = productManager.getProductLogByTransactionId(transaction_id);
	    			}
	    			if(productLog.getPayment()==1){
	    				returnType.setMsg("该订单已经验证并发放了！");
						returnType.setStatus(0);
						super.printReturnType2Response(response, returnType);
						return null;
	    			}
//	    			if(!role.getRoleId().equals(productLog.getRoleId())){
//	    				logger.error(role.getRoleName()+"购买的transaction_id不相同");
//	    				returnType.setMsg("验证失败！");
//						returnType.setStatus(0);
//						super.printReturnType2Response(response, returnType);
//						return null;
//	    			}
	    			RoleManager roleManager = (RoleManager)HttpSpringUtils.getBean("roleManager");
	    			Product product = productManager.getProductById(product_id);
	    			if(product==null){
	    				logger.error(role.getRoleName()+"购买的产品不存在:"+product_id);
	    				returnType.setMsg("验证失败！");
						returnType.setStatus(0);
						super.printReturnType2Response(response, returnType);
						return null;
	    			}
	    			if(productLog!=null){
	    				if(productLog.getStatus()==0){
	    					if(productLog.getPayment()==0){
	    						MemCachedManager payCachedManager = (MemCachedManager)HttpSpringUtils.getBean("payCachedManager");
	    						//add提交的时候如果缓存里面已经存在了则返回false
	    						if(!payCachedManager.add("pay_"+"apple"+productLog.getTransactionId(), 5*60, new Date())){
	    							logger.error("正在验证订单有效性，请不要重复提交！");
	    		    				returnType.setMsg("正在验证订单有效性，请不要重复提交！");
	    							returnType.setStatus(0);
	    							super.printReturnType2Response(response, returnType);
	    							return null;
	    						}
	    						returnType.setMsg("购买成功！");
			    				logger.debug("购买产品："+productLog.getProductId()+";交易ID"+productLog.getTransactionId());
			    				int cash = product.getValue();
			    				if(!roleManager.addCash(role.getRoleName(), cash)){
			    					logger.error("购买产品："+product.getIdentifier()+";交易ID"+productLog.getTransactionId()+";给用户加元宝失败");
			    				}else{
			    					logger.debug("给用户加元宝成功");
			    				}
			    				if(!(productManager.updatePaymentByStoreId(productLog.getStoreId())>0)){
			    					logger.error("购买产品："+product.getIdentifier()+";交易ID"+productLog.getTransactionId()+";修改状态失败");
			    				}
			    				try{
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
				    				payCachedManager.delete("pay_"+"apple"+productLog.getTransactionId());
								}
				    			payCachedManager.delete("pay_"+"apple"+productLog.getTransactionId());
	    					}
	    					returnType.setStatus(1);
		    				returnType.setValue(productLog.getTransactionId());
		    				super.printReturnType2Response(response, returnType);
	    				}else{
	    					returnType.setMsg("您还没付账！");
		    				returnType.setStatus(0);
//		    				returnType.setValue(transaction_id);
		    				super.printReturnType2Response(response, returnType);
		    				logger.error("购买产品："+ product_id+";交易ID"+transaction_id);
	    				}

	    			}else{
	    				returnType.setMsg("参数验证成功但是购买失败！");
	    				returnType.setStatus(0);
//	    				returnType.setValue(transaction_id);
	    				super.printReturnType2Response(response, returnType);
	    				logger.error("购买产品："+ product_id+";交易ID"+transaction_id);
	    			}
	    			List<ProductLog> productLogList = productManager.getProductLogByRoleId(role.getRoleId());
	    			int money = 0;
	    			if(!CollectionUtils.isEmpty(productLogList)){
	    				for(ProductLog proLog :productLogList){
	    					Product product2 = productManager.getProductById(proLog.getProductId());
	    					money = money + product2.getValue();
	    				}
	    			}
	    			int vip = VipInfo.getVipInfo(money);
	    			roleManager.upgradeVip(role.getRoleId(),vip);
					return null;
	    		}else{
					returnType.setMsg("验证失败！");
					returnType.setStatus(0);
					returnType.setValue(obj.getString("status"));
					logger.error(obj.getString("status"));
					super.printReturnType2Response(response, returnType);
					return null;
	    		}
	    		
	        } catch (Exception e) {
	            logger.error("转换结果串出错！", e);
	        }
			return null;
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		try{
			returnType.setValue(null);
			returnType.setStatus(0);
			super.printReturnType2Response(response, returnType);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);// TODO: handle exception
		}
		return null;
		
	}
}

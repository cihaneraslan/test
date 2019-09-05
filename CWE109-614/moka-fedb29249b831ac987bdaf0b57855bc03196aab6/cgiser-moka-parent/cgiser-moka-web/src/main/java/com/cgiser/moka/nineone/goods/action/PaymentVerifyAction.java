package com.cgiser.moka.nineone.goods.action;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.cgiser.keel.http.utils.HttpSpringUtils;
import com.cgiser.moka.action.AbstractAction;
import com.cgiser.moka.common.utils.ServletUtil;
import com.cgiser.moka.manager.AchievementManager;
import com.cgiser.moka.manager.ProductManager;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.manager.SalaryManager;
import com.cgiser.moka.manager.support.NineOneSdk;
import com.cgiser.moka.model.NineOneProductLog;
import com.cgiser.moka.model.Product;
import com.cgiser.moka.model.ProductLog;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.model.SalaryEnum;
import com.cgiser.moka.model.UserAchievement;
import com.cgiser.moka.result.ReturnType;
import com.cgiser.moka.result.VipInfo;

public class PaymentVerifyAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger("91store");
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
			String cooOrderSerial = ServletUtil.getDefaultValue(request, "cooOrderSerial", null);
			if(cooOrderSerial==null){
				returnType.setMsg("验证失败！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
	    	try {
	    		ProductManager productManager = (ProductManager)HttpSpringUtils.getBean("productManager");
	    		NineOneProductLog nineOneProductLog = productManager.getNineOneProductLogByCooOrderSerial(cooOrderSerial);
				if(nineOneProductLog==null){
					returnType.setMsg("验证失败！");
					returnType.setStatus(0);
					super.printReturnType2Response(response, returnType);
					return null;
				}
	            // 正常信息和异常信息返回的串结构不一样
	    		NineOneSdk nineOneSdk = (NineOneSdk)HttpSpringUtils.getBean("nineOneSdk");
	    		JSONObject obj = nineOneSdk.queryPayResult(cooOrderSerial);
	    		
	    		if(Integer.parseInt(obj.getString("ErrorCode"))==1){
	    			if(Integer.parseInt(obj.getString("PayStatus"))==1){
	    				Product product = productManager.getProductById(obj.getString("GoodsId"));
	    				if(product==null){
	    					returnType.setMsg("您购买的产品状态有误，请联系客服！");
	    					logger.error("pay error:购买的产品状态有误！");
	    					returnType.setStatus(0);
	    					productManager.update91StoreProductStatus(cooOrderSerial, 3);
	    					super.printReturnType2Response(response, returnType);
	    					return null;
	    				}
		    			if(!obj.getString("GoodsId").equals(product.getIdentifier())){
		    				returnType.setMsg("您购买的产品状态有误，请联系客服！");
	    					logger.error("pay error:购买的产品状态有误！");
	    					returnType.setStatus(0);
	    					productManager.update91StoreProductStatus(cooOrderSerial, 3);
	    					super.printReturnType2Response(response, returnType);
	    					return null;
		    			}
		    			if(!obj.getString("GoodsInfo").equals(product.getTitle())){
		    				returnType.setMsg("您购买的产品状态有误，请联系客服！");
	    					logger.error("pay error:购买的产品状态有误！");
	    					returnType.setStatus(0);
	    					productManager.update91StoreProductStatus(cooOrderSerial, 3);
	    					super.printReturnType2Response(response, returnType);
	    					return null;
		    			}
		    			if(!obj.getString("GoodsCount").equals("1")){
		    				returnType.setMsg("您购买的产品状态有误，请联系客服！");
	    					logger.error("pay error:购买的产品状态有误！");
	    					returnType.setStatus(0);
	    					productManager.update91StoreProductStatus(cooOrderSerial, 3);
	    					super.printReturnType2Response(response, returnType);
	    					return null;
		    			}
		    			//商品价格
		    			if(!(Float.parseFloat(obj.getString("OriginalMoney"))==Float.parseFloat(product.getPrice()))){
		    				returnType.setMsg("您购买的产品状态有误，请联系客服！");
	    					logger.error("pay error:购买的产品状态有误！");
	    					returnType.setStatus(0);
	    					productManager.update91StoreProductStatus(cooOrderSerial, 3);
	    					super.printReturnType2Response(response, returnType);
	    					return null;
		    			}
		    			//实际价格
		    			if(!(Float.parseFloat(obj.getString("OrderMoney"))==Float.parseFloat(product.getPriceLocale()))){
		    				returnType.setMsg("您购买的产品状态有误，请联系客服！");
	    					logger.error("pay error:购买的产品状态有误！");
	    					returnType.setStatus(0);
	    					productManager.update91StoreProductStatus(cooOrderSerial, 3);
	    					super.printReturnType2Response(response, returnType);
	    					return null;
		    			}
//		    			Long storeId = productManager.info91StoreProduct(role.getRoleId(), obj.getString("GoodsId"),  obj.getString("CooOrderSerial"),obj.getString("ConsumeStreamId"), obj.toString(), Integer.parseInt(obj.getString("GoodsCount")), 1, Integer.parseInt(obj.getString("PayStatus")));
		    			int id = productManager.update91StoreProduct(obj.getString("GoodsId"), obj.getString("ConsumeStreamId"), obj.toString(), Integer.parseInt(obj.getString("GoodsCount")), 1, cooOrderSerial);
		    			if(id>0){

		    				RoleManager roleManager = (RoleManager)HttpSpringUtils.getBean("roleManager");
	    					returnType.setMsg("购买成功！");
	    					logger.debug("购买产品："+obj.getString("GoodsId")+";交易ID"+obj.getString("CooOrderSerial"));
	    					int cash = product.getValue()*Integer.parseInt(obj.getString("GoodsCount"));
//	    					if(!roleManager.addCash(role.getRoleName(), cash)){
//	    						logger.error("购买产品："+obj.getString("GoodsId")+";交易ID"+obj.getString("CooOrderSerial")+";给用户加元宝失败");
//	    					}else{
//	    						logger.debug("给用户加元宝成功");
//	    						productManager.update91StoreProductStatus(obj.getString("CooOrderSerial"),1);
//	    					}
	    					if(productManager.update91StoreProductStatus(obj.getString("CooOrderSerial"),1)>0){
	    						roleManager.addCash(role.getRoleName(), cash);
	    					}else{
	    						returnType.setMsg("数据库出错，请联系客服！");
		    					logger.error("pay error:购买的产品状态有误！");
		    					returnType.setStatus(0);
		    					super.printReturnType2Response(response, returnType);
		    					return null;
	    					}
	    					
		    				List<ProductLog> productLogList = productManager.getProductLogByRoleId(role.getRoleId());
		    				List<NineOneProductLog> nineOneProductLogs = productManager.getNineOneProductLogByStatus(1,role.getRoleId());
		    				int money = 0;
		    				if(!CollectionUtils.isEmpty(productLogList)){
		    					for(ProductLog proLog :productLogList){
		    						Product product2 = productManager.getProductById(proLog.getProductId());
		    						money = money + product2.getValue();
		    					}
		    				}
		    				if(!CollectionUtils.isEmpty(nineOneProductLogs)){
		    					for(NineOneProductLog proLog :nineOneProductLogs){
		    						Product product2 = productManager.getProductById(proLog.getProductId());
		    						money = money + product2.getValue();
		    					}
		    				}
			    			int vip = VipInfo.getVipInfo(money);
			    			roleManager.upgradeVip(role.getRoleId(),vip);
		    				try{
				    			AchievementManager achievementManager =(AchievementManager)HttpSpringUtils.getBean("achievementManager");
				    			UserAchievement userAchievement = achievementManager.getUserAchievementById(role.getRoleId(), 44); 
								if(userAchievement==null){
									achievementManager.saveUserAchievement(44, role.getRoleId(),1);
									SalaryManager salaryManager = (SalaryManager)HttpSpringUtils.getBean("salaryManager");
									salaryManager.extendSalary(2, cash*2, new Date(), SalaryEnum.OtherSalary,"首次充值奖励", role.getRoleId());
									salaryManager.extendSalary(1, 200000, new Date(), SalaryEnum.OtherSalary,"首次充值奖励", role.getRoleId());
									salaryManager.extendSalary(7, 13, new Date(), SalaryEnum.OtherSalary,"首次充值奖励", role.getRoleId());
									salaryManager.extendSalary(10, 2, new Date(), SalaryEnum.OtherSalary,"首次充值奖励", role.getRoleId());
								}
			    			}catch (Exception e) {
			    				logger.error(e.getMessage(),e);// TODO: handle exception
							}
		    				returnType.setMsg("购买成功！");
							returnType.setStatus(1);
							returnType.setValue(obj.getString("ErrorCode"));
							logger.error(obj.getString("ErrorCode"));
							super.printReturnType2Response(response, returnType);
		    			}else{
							returnType.setMsg("购买失败！");
							returnType.setStatus(0);
							returnType.setValue(obj.getString("ErrorCode"));
							logger.error("91支付验证成功，添加数据库失败");
							super.printReturnType2Response(response, returnType);
							return null;
			    		}
		    			
						return null;
	    			}
	    			
	    		}else{
					returnType.setMsg("验证失败！");
					returnType.setStatus(0);
					returnType.setValue(obj.getString("ErrorCode"));
					logger.error("91支付验证失败"+obj.getString("ErrorCode"));
					super.printReturnType2Response(response, returnType);
					return null;
	    		}
	    		
	        } catch (Exception e) {
	            logger.error("91支付验证出错！", e);
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

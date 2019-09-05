package com.cgiser.moka.nineone.goods.action;

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
import com.cgiser.moka.manager.support.NineOneSdk;
import com.cgiser.moka.model.NineOneProductLog;
import com.cgiser.moka.model.Product;
import com.cgiser.moka.model.ProductLog;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.model.SalaryEnum;
import com.cgiser.moka.model.UserAchievement;
import com.cgiser.moka.result.VipInfo;

public class PaymentVerifyForNineOneAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger("91store");
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		try{
			String cooOrderSerial = ServletUtil.getDefaultValue(request, "cooOrderSerial", null);
			String act = ServletUtil.getDefaultValue(request, "act", null);
			String productName = ServletUtil.getDefaultValue(request, "productName", null);
			String consumeStreamId = ServletUtil.getDefaultValue(request, "consumeStreamId", null);
			String uin = ServletUtil.getDefaultValue(request, "uin", null);
			String goodsId = ServletUtil.getDefaultValue(request, "goodsId", null);
			String goodsInfo = ServletUtil.getDefaultValue(request, "goodsInfo", null);
			String goodsCount = ServletUtil.getDefaultValue(request, "goodsCount", null);
			String originalMoney = ServletUtil.getDefaultValue(request, "originalMoney", null);
			String orderMoney = ServletUtil.getDefaultValue(request, "orderMoney", null);
			String note = ServletUtil.getDefaultValue(request, "note", null);
			String payStatus = ServletUtil.getDefaultValue(request, "payStatus", null);
			String createTime = ServletUtil.getDefaultValue(request, "createTime", null);
			String fromSign = ServletUtil.getDefaultValue(request, "fromSign", null);
			if(cooOrderSerial==null){
				response.getWriter().print("{\"ErrorCode\":\"0\",\"ErrorDesc\":\"接收失败\"}");
				return null;
			}
			MemCachedManager payCachedManager = (MemCachedManager)HttpSpringUtils.getBean("payCachedManager");
	    	try {
	    		ProductManager productManager = (ProductManager)HttpSpringUtils.getBean("productManager");
	    		NineOneProductLog nineOneProductLog = productManager.getNineOneProductLogByCooOrderSerial(cooOrderSerial);
				if(nineOneProductLog==null){
					response.getWriter().print("{\"ErrorCode\":\"0\",\"ErrorDesc\":\"接收失败\"}");
					return null;
				}
	            // 正常信息和异常信息返回的串结构不一样
	    		NineOneSdk nineOneSdk = (NineOneSdk)HttpSpringUtils.getBean("nineOneSdk");
	    		int result = nineOneSdk.payResultNotify(act, productName, consumeStreamId, cooOrderSerial, uin, goodsId, goodsInfo, goodsCount, originalMoney, orderMoney, note, payStatus, createTime, fromSign);
//	    		JSONObject obj = nineOneSdk.queryPayResult(cooOrderSerial);
	    		
	    		if(result==1){
	    			if(Integer.parseInt(payStatus)==1){
	    				Product product = productManager.getProductById(goodsId);
	    				if(product==null){
	    					logger.error("pay error:购买的产品状态有误！");
	    					response.getWriter().print("{\"ErrorCode\":\"0\",\"ErrorDesc\":\"接收失败\"}");
	    					return null;
	    				}
		    			if(!goodsId.equals(product.getIdentifier())){
	    					logger.error("pay error:购买的产品状态有误！");
	    					response.getWriter().print("{\"ErrorCode\":\"0\",\"ErrorDesc\":\"接收失败\"}");
	    					return null;
		    			}
		    			if(!goodsInfo.equals(product.getTitle())){
		    				logger.error("pay error:购买的产品状态有误！");
	    					response.getWriter().print("{\"ErrorCode\":\"0\",\"ErrorDesc\":\"接收失败\"}");
	    					return null;
		    			}
		    			if(!goodsCount.equals("1")){
		    				logger.error("pay error:购买的产品状态有误！");
	    					response.getWriter().print("{\"ErrorCode\":\"0\",\"ErrorDesc\":\"接收失败\"}");
	    					return null;
		    			}
		    			//商品价格
		    			if(!(Float.parseFloat(originalMoney)==Float.parseFloat(product.getPrice()))){
		    				logger.error("pay error:购买的产品状态有误！");
	    					response.getWriter().print("{\"ErrorCode\":\"0\",\"ErrorDesc\":\"接收失败\"}");
	    					return null;
		    			}
		    			//实际价格
		    			if(!(Float.parseFloat(orderMoney)==Float.parseFloat(product.getPriceLocale()))){
		    				logger.error("pay error:购买的产品状态有误！");
	    					response.getWriter().print("{\"ErrorCode\":\"0\",\"ErrorDesc\":\"接收失败\"}");
	    					return null;
		    			}
						if(!payCachedManager.add("pay_"+"nineone"+cooOrderSerial, 5*60, new Date())){
							logger.error("正在验证订单有效性，请不要重复提交！");
							response.getWriter().print("fail");
							return null;
						}
//		    			Long storeId = productManager.info91StoreProduct(role.getRoleId(), obj.getString("GoodsId"),  obj.getString("CooOrderSerial"),obj.getString("ConsumeStreamId"), obj.toString(), Integer.parseInt(obj.getString("GoodsCount")), 1, Integer.parseInt(obj.getString("PayStatus")));
		    			int id = productManager.update91StoreProduct(goodsId, consumeStreamId, "", Integer.parseInt(goodsCount), 1, cooOrderSerial);
		    			RoleManager roleManager = (RoleManager)HttpSpringUtils.getBean("");
		    			Role role = roleManager.getRoleById(nineOneProductLog.getRoleId());
		    			if(id>0){
	    					logger.debug("购买产品："+goodsId+";交易ID"+cooOrderSerial);
	    					int cash = product.getValue()*Integer.parseInt(goodsCount);
//	    					if(!roleManager.addCash(role.getRoleName(), cash)){
//	    						logger.error("购买产品："+obj.getString("GoodsId")+";交易ID"+obj.getString("CooOrderSerial")+";给用户加元宝失败");
//	    					}else{
//	    						logger.debug("给用户加元宝成功");
//	    						productManager.update91StoreProductStatus(obj.getString("CooOrderSerial"),1);
//	    					}
	    					if(productManager.update91StoreProductStatus(cooOrderSerial,1)>0){
	    						roleManager.addCash(role.getRoleName(), cash);
	    					}else{
		    					logger.error("pay error:购买的产品状态有误！");
		    					response.getWriter().print("{\"ErrorCode\":\"0\",\"ErrorDesc\":\"接收失败\"}");
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
			    			response.getWriter().print("{\"ErrorCode\":\"1\",\"ErrorDesc\":\"接收成功\"}");
		    			}else{
		    				response.getWriter().print("{\"ErrorCode\":\"0\",\"ErrorDesc\":\"接收失败\"}");
							return null;
			    		}
						return null;
	    			}
	    			
	    		}else{
	    			response.getWriter().print("{\"ErrorCode\":\"0\",\"ErrorDesc\":\"接收失败\"}");
					return null;
	    		}
	    		
	        } catch (Exception e) {
	        	response.getWriter().print("{\"ErrorCode\":\"0\",\"ErrorDesc\":\"接收失败\"}");
	            logger.error("91支付验证出错！", e);
	        }finally{
	        	payCachedManager.delete("pay_"+"nineone"+cooOrderSerial);
	        }
			return null;
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		try{
			response.getWriter().print("{\"ErrorCode\":\"0\",\"ErrorDesc\":\"接收失败\"}");
		}catch (Exception e) {
			logger.error(e.getMessage(),e);// TODO: handle exception
		}
		return null;
		
	}
}

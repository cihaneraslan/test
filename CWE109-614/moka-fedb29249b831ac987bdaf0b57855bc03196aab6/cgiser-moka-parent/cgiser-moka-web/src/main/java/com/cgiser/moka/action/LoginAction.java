package com.cgiser.moka.action;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.Cookie;
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
import com.cgiser.moka.common.utils.ServletUtil;
import com.cgiser.moka.dao.util.DigestUtils;
import com.cgiser.moka.manager.AchievementManager;
import com.cgiser.moka.manager.LoginAwardManager;
import com.cgiser.moka.manager.ProductManager;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.manager.SalaryManager;
import com.cgiser.moka.manager.VipInfoManager;
import com.cgiser.moka.manager.support.NineOneSdk;
import com.cgiser.moka.model.Achievement;
import com.cgiser.moka.model.LoginAward;
import com.cgiser.moka.model.NineOneProductLog;
import com.cgiser.moka.model.Product;
import com.cgiser.moka.model.ProductLog;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.model.SalaryEnum;
import com.cgiser.moka.model.UserAchievement;
import com.cgiser.moka.result.ReturnType;
import com.cgiser.moka.result.VipInfo;

public class LoginAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		ReturnType<Role> returnType = new ReturnType<Role>();
		
		try{
			String roleName = ServletUtil.getDefaultValue(request, "roleName",
			"");
			RoleManager roleManager  = (RoleManager)HttpSpringUtils.getBean("roleManager");
			Role role = roleManager.getRoleByName(roleName);
			if(role!=null){
				if(role.getState()!=1){
					returnType.setStatus(0);
				}else{
					VipInfoManager vipInfoManager = (VipInfoManager)HttpSpringUtils.getBean("vipInfoManager");
					if(role.getVip()>0){
						com.cgiser.moka.model.VipInfo info = vipInfoManager.getVipInfoByRoleId(role.getRoleId());
						if(info==null){
							vipInfoManager.saveVipInfo(role.getRoleId(), role.getVip());
						}else{
							vipInfoManager.updateVipInfo(role.getRoleId(), role.getVip());
						}
					}
					AchievementManager achievementManager = (AchievementManager)HttpSpringUtils.getBean("achievementManager");
					Achievement achievement = achievementManager.getAchievementById(47);
					UserAchievement userAchievement = achievementManager
							.getUserAchievementById(role.getRoleId(), 47);
					if (userAchievement != null&&userAchievement.getFinishState()<achievement.getFinishNum()
							&& role.getInviteNum()>=achievement.getFinishNum()) {
						achievementManager.saveUserAchievement(47, role
								.getRoleId(),
								achievement.getFinishNum());
					}
					achievement = achievementManager.getAchievementById(57);
					userAchievement = achievementManager
							.getUserAchievementById(role.getRoleId(), 57);
					if (userAchievement != null&&userAchievement.getFinishState()<achievement.getFinishNum()
							&& role.getInviteNum()>=achievement.getFinishNum()) {
						achievementManager.saveUserAchievement(57, role
								.getRoleId(),
								achievement.getFinishNum());
					}
					roleManager.updateRoleStatus(roleName, 0);
					response.addCookie(new Cookie(cookieId, DigestUtils.digest(roleName)));
					Date date = new Date(); 
					if(role.getLastLoginTime().getYear()==date.getYear()&&role.getLastLoginTime().getMonth()==date.getMonth()){
						if(role.getLastLoginTime().getDay()==date.getDay()){
							
						}else{
							roleManager.updateRoleSendEnergyTimes(role.getRoleId(), 0);
							role.setLoginContinueTimes(role.getLoginContinueTimes()>20?21:role.getLoginContinueTimes()+1);
							LoginAwardManager loginAwardManager = (LoginAwardManager)HttpSpringUtils.getBean("loginAwardManager");
							int month = Calendar.getInstance().get(Calendar.MONTH)+1;
							List<LoginAward> loginAwards = loginAwardManager.getLoginAwardType(month);
							LoginAward loginAward = this.getLoginAwardByDay(role.getLoginContinueTimes(), loginAwards);
							if(loginAward!=null){
								SalaryManager salaryManager = (SalaryManager)HttpSpringUtils.getBean("salaryManager");
								if(loginAward.getAwardType()>0){
									salaryManager.extendSalary(loginAward.getAwardType(), loginAward.getAwardValue(), new Date(), SalaryEnum.LoginSalary, "登录奖励",role.getRoleId());
								}
							}

						}

					}else{
						role.setLoginContinueTimes(1);
						LoginAwardManager loginAwardManager = (LoginAwardManager)HttpSpringUtils.getBean("loginAwardManager");
						int month = Calendar.getInstance().get(Calendar.MONTH)+1;
						List<LoginAward> loginAwards = loginAwardManager.getLoginAwardType(month);
						LoginAward loginAward = loginAwards.get(role.getLoginContinueTimes()-1);
						SalaryManager salaryManager = (SalaryManager)HttpSpringUtils.getBean("salaryManager");
						if(loginAward.getAwardType()>0){
							salaryManager.extendSalary(loginAward.getAwardType(), loginAward.getAwardValue(), new Date(), SalaryEnum.LoginSalary,"登录奖励" ,role.getRoleId());
						}
					}
					NineOneSdk nineOneSdk = (NineOneSdk)HttpSpringUtils.getBean("nineOneSdk");
		    		
					ProductManager productManager = (ProductManager)HttpSpringUtils.getBean("productManager");
					List<NineOneProductLog> nOneProductLogs = productManager.getNineOneProductLogByStatus(0,role.getRoleId());
					if(!CollectionUtils.isEmpty(nOneProductLogs)){
						for(NineOneProductLog nineOneProductLog:nOneProductLogs){
							JSONObject obj = nineOneSdk.queryPayResult(nineOneProductLog.getCooOrderSerial());
							if(Integer.parseInt(obj.getString("ErrorCode"))==1&&Integer.parseInt(obj.getString("PayStatus"))==1){
								Product product = productManager.getProductById(obj.getString("GoodsId"));
			    				if(product==null){
			    					returnType.setMsg("您购买的产品状态有误，请联系客服！");
			    					logger.error("pay error:购买的产品状态有误！");
			    					returnType.setStatus(0);
			    					super.printReturnType2Response(response, returnType);
			    					return null;
			    				}
			    				String cooOrderSerial = obj.getString("CooOrderSerial");
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
				    			int id = productManager.update91StoreProduct(obj.getString("GoodsId"), obj.getString("ConsumeStreamId"), obj.toString(), Integer.parseInt(obj.getString("GoodsCount")), 1, nineOneProductLog.getCooOrderSerial());
				    			if(id>0){
				    				
			    					logger.debug("购买产品："+obj.getString("GoodsId")+";交易ID"+obj.getString("CooOrderSerial"));
			    					int cash = product.getValue()*Integer.parseInt(obj.getString("GoodsCount"));
			    					if(productManager.update91StoreProductStatus(obj.getString("CooOrderSerial"),1)>0){
			    						roleManager.addCash(role.getRoleName(), cash);
			    					}else{
			    						returnType.setMsg("数据库出错，请联系客服！");
				    					logger.error("pay error:购买的产品状态有误！");
				    					returnType.setStatus(0);
				    					super.printReturnType2Response(response, returnType);
				    					return null;
			    					}
//			    					if(!roleManager.addCash(role.getRoleName(), cash)){
//			    						logger.error("购买产品："+obj.getString("GoodsId")+";交易ID"+obj.getString("CooOrderSerial")+";给用户加元宝失败");
//			    					}else{
//			    						productManager.update91StoreProductStatus(obj.getString("CooOrderSerial"),1);
//			    						logger.debug("给用户加元宝成功");
//			    					}
			    					returnType.setStatus(1);
			    					super.printReturnType2Response(response, returnType);
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
						    			userAchievement = achievementManager.getUserAchievementById(role.getRoleId(), 44); 
										if(userAchievement==null){
											achievementManager.saveUserAchievement(44, role.getRoleId(),1);
											SalaryManager salaryManager = (SalaryManager)HttpSpringUtils.getBean("salaryManager");
											salaryManager.extendSalary(2, cash*2, new Date(), SalaryEnum.OtherSalary,"首次充值奖励", role.getRoleId());
										}
					    			}catch (Exception e) {
					    				logger.error(e.getMessage(),e);// TODO: handle exception
									}
				    			}
				    		}else{
				    			productManager.update91StoreProductStatus(nineOneProductLog.getCooOrderSerial(), 2);
				    		}
						}
					}
					role.setLastLoginTime(date);
					roleManager.updateRoleLastLoginAndLonginTimes(date, role.getLoginContinueTimes(), role.getRoleId());
					returnType.setStatus(1);
					returnType.setValue(role);
				}
			}else{
				returnType.setStatus(0);
			}
			super.printReturnType2Response(response, returnType);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		
		return null;
	}
	private LoginAward getLoginAwardByDay(int loginConuteTimes,List<LoginAward> loginAwards){
		if(!CollectionUtils.isEmpty(loginAwards)){
			for(LoginAward loginAward:loginAwards){
				if(loginAward.getDay()==loginConuteTimes){
					return loginAward;
				}
			}
		}
		return null;
	}
	public static void main(String[] args) {
		System.out.println(Calendar.getInstance().get(Calendar.MONTH)+1);
	}
}

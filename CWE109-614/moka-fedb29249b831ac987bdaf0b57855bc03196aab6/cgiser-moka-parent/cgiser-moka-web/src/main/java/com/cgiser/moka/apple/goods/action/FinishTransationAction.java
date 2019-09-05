package com.cgiser.moka.apple.goods.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.cgiser.keel.http.utils.HttpSpringUtils;
import com.cgiser.moka.action.AbstractAction;
import com.cgiser.moka.common.utils.ServletUtil;
import com.cgiser.moka.manager.ProductManager;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.model.Product;
import com.cgiser.moka.model.ProductLog;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.result.ReturnType;
import com.cgiser.moka.result.VipInfo;

public class FinishTransationAction extends AbstractAction {
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
			String transactionId = ServletUtil.getDefaultValue(request, "transactionId", null);
			String productId = ServletUtil.getDefaultValue(request, "productId", null);
			if(transactionId==null){
				returnType.setMsg("参数有误！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			ProductManager productManager = (ProductManager)HttpSpringUtils.getBean("productManager");
			ProductLog productLog = productManager.getProductLogByTransactionId(transactionId);
			Product product = productManager.getProductById(productId);
			if(product==null){
				returnType.setMsg("您购买的产品状态有误，请联系客服！");
				logger.error("pay error:购买的产品状态有误！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(productLog==null){
				returnType.setMsg("充值记录不存在！");
				logger.error("pay error:充值记录不存在！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(!productLog.getReceipt().equals(receipt)){
				returnType.setMsg("充值票据有误！");
				logger.error("pay error:充值票据有误！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(!productLog.getProductId().equals(productId)){
				returnType.setMsg("充值产品类型有误！");
				logger.error("pay error:充值产品类型有误！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			RoleManager roleManager = (RoleManager)HttpSpringUtils.getBean("roleManager");
			if(productManager.updatePaymentByStoreId(productLog.getStoreId())>0){
				returnType.setMsg("购买成功！");
				logger.debug("购买产品："+productId+";交易ID"+transactionId);
				int cash = product.getValue();
				if(!roleManager.addCash(role.getRoleName(), cash)){
					logger.error("购买产品："+productId+";交易ID"+transactionId+";给用户加元宝失败");
				}else{
					logger.debug("给用户加元宝成功");
				}
				returnType.setStatus(1);
				super.printReturnType2Response(response, returnType);
			}else{
				returnType.setMsg("参数验证成功但是购买失败！");
				returnType.setStatus(0);
				returnType.setValue(transactionId);
				logger.error("购买产品："+productId+";交易ID"+transactionId);
				super.printReturnType2Response(response, returnType);
			}
			List<ProductLog> productLogList = productManager.getProductLogByRoleId(role.getRoleId());
			int money = 0;
			if(!CollectionUtils.isEmpty(productLogList)){
				for(ProductLog proLog :productLogList){
					Product product2 = productManager.getProductById(proLog.getProductId());
					money = money + Integer.parseInt(product2.getPrice());
				}
			}
			int vip = VipInfo.getVipInfo(money);
			roleManager.upgradeVip(role.getRoleId(),vip);
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

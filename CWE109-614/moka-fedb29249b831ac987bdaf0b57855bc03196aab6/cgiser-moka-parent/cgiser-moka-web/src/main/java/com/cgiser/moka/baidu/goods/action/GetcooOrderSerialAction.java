package com.cgiser.moka.baidu.goods.action;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cgiser.keel.http.utils.HttpSpringUtils;
import com.cgiser.moka.action.AbstractAction;
import com.cgiser.moka.common.utils.ServletUtil;
import com.cgiser.moka.manager.BaiDuProductLogManager;
import com.cgiser.moka.manager.ProductManager;
import com.cgiser.moka.model.Product;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.result.ReturnType;

public class GetcooOrderSerialAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger("wdjstore");
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
			String productId = ServletUtil.getDefaultValue(request, "productId", null);
			if(productId==null){
				returnType.setMsg("获取订单号失败，产品ID不能为空！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			String cooOrderSerial  = UUID.randomUUID().toString();
			BaiDuProductLogManager baiduProductLogManager = (BaiDuProductLogManager)HttpSpringUtils.getBean("baiDuProductLogManager");
			ProductManager productManager = (ProductManager)HttpSpringUtils.getBean("productManager");
			Product p = productManager.getProductById(productId);
			if(p==null){
				returnType.setMsg("获取订单号失败，您购买的产品不存在或者状态不正常");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			
			Long storeId = baiduProductLogManager.infoBaiDuProductLog(role.getRoleId(), productId, cooOrderSerial, "", 0, 0, 0);
			if(storeId>0){
				returnType.setStatus(1);
				returnType.setValue(cooOrderSerial);
				super.printReturnType2Response(response, returnType);
				return null;
			}else{
				returnType.setMsg("获取订单号失败，下单失败");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
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

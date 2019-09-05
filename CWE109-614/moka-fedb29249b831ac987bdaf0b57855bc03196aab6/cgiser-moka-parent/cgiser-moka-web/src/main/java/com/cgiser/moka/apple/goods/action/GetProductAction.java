package com.cgiser.moka.apple.goods.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cgiser.keel.http.utils.HttpSpringUtils;
import com.cgiser.moka.action.AbstractAction;
import com.cgiser.moka.common.utils.ServletUtil;
import com.cgiser.moka.manager.ProductManager;
import com.cgiser.moka.model.Product;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.result.ReturnType;

public class GetProductAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		ReturnType<List<Product>> returnType = new ReturnType<List<Product>>();
		try{
			Role role = super.getCurrentRole(request);
			if(null==role){
				returnType.setMsg("您还没登录哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			String type = ServletUtil.getDefaultValue(request, "type", "1");
			if(!StringUtils.isNumeric(type)){
				returnType.setMsg("参数有误哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			ProductManager productManager = (ProductManager)HttpSpringUtils.getBean("productManager");
			List<Product> products = productManager.getProductsByType(Integer.parseInt(type));
			if(products==null){
				returnType.setMsg("获取商品信息失败！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
			}else{
				returnType.setValue(products);
				returnType.setStatus(1);
				super.printReturnType2Response(response, returnType);
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
			logger.error(e.getMessage());// TODO: handle exception
		}
		return null;
		
	}
}

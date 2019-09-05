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
import com.cgiser.moka.model.AllProductLog;
import com.cgiser.moka.result.ReturnType;

public class GetProductLogAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		ReturnType<List<AllProductLog>> returnType = new ReturnType<List<AllProductLog>>();
		try{
//			Role role = super.getCurrentRole(request);
//			if(null==role){
//				returnType.setMsg("您还没登录哦！");
//				returnType.setStatus(0);
//				super.printReturnType2Response(response, returnType);
//				return null;
//			}
			String pass = ServletUtil.getDefaultValue(request, "pass", "");
			if(!pass.equals("cgiserpass")){
				returnType.setMsg("您没有权限哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			String type = ServletUtil.getDefaultValue(request, "type", "0");
			String a = ServletUtil.getDefaultValue(request, "a", "0");
			String b = ServletUtil.getDefaultValue(request, "b", "-3");
			if(!StringUtils.isNumeric(type)){
				returnType.setMsg("参数有误哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			ProductManager productManager = (ProductManager)HttpSpringUtils.getBean("productManager");
			List<AllProductLog> productLogs = productManager.getAllProductLog(Integer.parseInt(type),Integer.parseInt(a),Integer.parseInt(b));
			returnType.setValue(productLogs);
			returnType.setStatus(1);
			super.printReturnType2Response(response, returnType);
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

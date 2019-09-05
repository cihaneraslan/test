package com.cgiser.moka.goods.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cgiser.keel.http.utils.HttpSpringUtils;
import com.cgiser.moka.action.AbstractAction;
import com.cgiser.moka.manager.GoodManager;
import com.cgiser.moka.model.Good;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.result.ReturnType;

public class GetGoodAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		ReturnType<List<Good>> returnType = new ReturnType<List<Good>>();
		try{
			Role role = super.getCurrentRole(request);
			if(null==role){
				returnType.setMsg("您还没登录哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			GoodManager goodManager = (GoodManager)HttpSpringUtils.getBean("goodManager");
			List<Good> goods = goodManager.getGoods();
			if(goods==null){
				returnType.setMsg("获取商品信息失败！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
			}else{
				returnType.setValue(goods);
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

package com.cgiser.moka.apple.push;

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
import com.cgiser.moka.manager.PushNotificationManager;
import com.cgiser.moka.model.Product;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.result.ReturnType;

public class UpdateRoleTokenIdAction extends AbstractAction {
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
			String tokenId = ServletUtil.getDefaultValue(request, "tokenId", "");
			if(StringUtils.isBlank(tokenId)){
				returnType.setMsg("tokenId不能为空哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			PushNotificationManager pushNotificationManager = (PushNotificationManager)HttpSpringUtils.getBean("pushNotificationManager");
			
			Long id = pushNotificationManager.updateRoleTokenId(role.getRoleId(), tokenId);
			if(id>0){
				returnType.setMsg("修改玩家tokenId成功！");
				returnType.setStatus(1);
				super.printReturnType2Response(response, returnType);
			}else{
				returnType.setMsg("修改玩家tokenId失败！");
				returnType.setStatus(0);
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

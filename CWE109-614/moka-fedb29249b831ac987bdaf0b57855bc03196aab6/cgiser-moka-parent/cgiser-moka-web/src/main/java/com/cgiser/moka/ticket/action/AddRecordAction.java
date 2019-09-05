package com.cgiser.moka.ticket.action;

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
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.manager.ThirdManager;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.result.ReturnType;

public class AddRecordAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		ReturnType<String> returnType = new ReturnType<String>();
		String first = ServletUtil.getDefaultValue(request, "first", null);
		String second = ServletUtil.getDefaultValue(request, "second", null);
		String third = ServletUtil.getDefaultValue(request, "third", null);
		String count = ServletUtil.getDefaultValue(request, "count", null);
		
		try{
			Role role = super.getCurrentRole(request);
			if(null==role){
				returnType.setMsg("您还没登录哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(StringUtils.isBlank(first)||!StringUtils.isNumeric(first)){
				returnType.setMsg("参数有误哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(StringUtils.isBlank(second)||!StringUtils.isNumeric(second)){
				returnType.setMsg("参数有误哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(StringUtils.isBlank(third)||!StringUtils.isNumeric(third)){
				returnType.setMsg("参数有误哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(StringUtils.isBlank(count)||!StringUtils.isNumeric(count)||Integer.parseInt(count)<1){
				returnType.setMsg("参数有误哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			
			if(role.getCash()<50*Integer.parseInt(count)){
				returnType.setStatus(0);
				returnType.setMsg("您的元宝不够");
				super.printReturnType2Response(response, returnType);
			}
			ThirdManager thirdManager = (ThirdManager)HttpSpringUtils.getBean("thirdManager");
			String str = thirdManager.getNowThirdIssue();
			if(!StringUtils.isNumeric(str)){
				returnType.setStatus(0);
				returnType.setMsg("当前期已经结束了哦");
				super.printReturnType2Response(response, returnType);
			}
			Long id = thirdManager.addRecord(role.getRoleId(), Integer.parseInt(first), Integer.parseInt(second), Integer.parseInt(third), Integer.parseInt(count), Integer.parseInt(str));
			if(id>0){
				RoleManager roleManager = (RoleManager)HttpSpringUtils.getBean("roleManager");
				if(!roleManager.updateCash(role.getRoleName(), 50*Integer.parseInt(count))){
					logger.error("用户投注成功但是减元宝失败");
				}
				returnType.setStatus(1);
				returnType.setValue(String.valueOf(id));
				super.printReturnType2Response(response, returnType);
			}else{
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
			}
			
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
}

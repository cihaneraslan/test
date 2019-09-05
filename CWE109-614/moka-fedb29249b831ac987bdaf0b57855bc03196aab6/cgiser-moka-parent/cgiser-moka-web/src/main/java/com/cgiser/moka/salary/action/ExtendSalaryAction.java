package com.cgiser.moka.salary.action;

import java.text.SimpleDateFormat;
import java.util.Date;

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
import com.cgiser.moka.manager.SalaryManager;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.model.SalaryEnum;
import com.cgiser.moka.result.ReturnType;

public class ExtendSalaryAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		try{
			String awardType = ServletUtil.getDefaultValue(request, "awardType", null);
			String awardValue = ServletUtil.getDefaultValue(request, "awardValue", null);
			String roleId = ServletUtil.getDefaultValue(request, "roleId", null);
			String type = ServletUtil.getDefaultValue(request, "type", null);
			String desc = ServletUtil.getDefaultValue(request, "desc", null);
			String pass = ServletUtil.getDefaultValue(request, "pass", null);
			ReturnType<String> returnType = new ReturnType<String>();
			if(!pass.equals("cgiserpass")){
				returnType.setMsg("您还没有权限哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(!StringUtils.isNumeric(awardType)||!StringUtils.isNumeric(awardValue)||!StringUtils.isNumeric(roleId)||!StringUtils.isNumeric(type)){
				returnType.setMsg("参数有误哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(Integer.parseInt(awardValue)>8000){
				returnType.setMsg("发送的元宝超限！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			RoleManager  roleManager = (RoleManager)HttpSpringUtils.getBean("roleManager");
			Role role = roleManager.getRoleById(new Long(roleId));
			if(role==null){
				returnType.setMsg("发放奖励的角色不存在哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//小写的mm表示的是分钟  
 
			
			SalaryManager salaryManager = (SalaryManager)HttpSpringUtils.getBean("salaryManager");
			Long salaryId = salaryManager.extendSalary(Integer.parseInt(awardType), Integer.parseInt(awardValue), new Date(),SalaryEnum.getSalaryEnum(Integer.parseInt(type)), desc,new Long(roleId));
			if(salaryId>0){
				returnType.setMsg("发放成功！");
				returnType.setStatus(1);
				super.printReturnType2Response(response, returnType);
				return null;
			}else{
				returnType.setMsg("发放失败！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
		}catch (Exception e) {
			logger.error("bind error:",e);
		}

		return null;
	}
}

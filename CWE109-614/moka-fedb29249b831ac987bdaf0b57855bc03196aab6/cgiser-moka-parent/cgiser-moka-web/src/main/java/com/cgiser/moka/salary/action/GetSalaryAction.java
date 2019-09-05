package com.cgiser.moka.salary.action;

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
import com.cgiser.moka.manager.SalaryManager;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.model.Salary;
import com.cgiser.moka.result.ReturnType;


public class GetSalaryAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		try{
			ReturnType<List<Salary>> returnType = new ReturnType<List<Salary>>();
			Role role = super.getCurrentRole(request);
			if(role==null){
				returnType.setMsg("您还没登录哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			SalaryManager salaryManager = (SalaryManager)HttpSpringUtils.getBean("salaryManager");
			List<Salary> salaries = salaryManager.getSalaryByRoleId(role.getRoleId());
			returnType.setStatus(1);
			returnType.setValue(salaries);
			super.printReturnType2Response(response, returnType);
		}catch (Exception e) {
			logger.error("bind error:",e);
		}

		return null;
	}
}

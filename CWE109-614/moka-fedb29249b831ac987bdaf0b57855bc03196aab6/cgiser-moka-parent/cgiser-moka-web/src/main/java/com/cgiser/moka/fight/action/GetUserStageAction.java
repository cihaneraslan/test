package com.cgiser.moka.fight.action;

import java.util.Date;
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
import com.cgiser.moka.manager.UserStageManager;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.model.UserStage;
import com.cgiser.moka.result.ReturnType;
import com.cgiser.moka.result.UserStageResult;

public class GetUserStageAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		ReturnType<UserStageResult> returnType = new ReturnType<UserStageResult>();
		try{
			Role role = super.getCurrentRole(request);
			if(null==role){
				returnType.setMsg("您还没登录哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			UserStageResult result = new UserStageResult();
			UserStageManager userStageManager = (UserStageManager)HttpSpringUtils.getBean("userStageManager");
			List<UserStage> userStages = userStageManager.getUserStageByRoleId(role.getRoleId());
			result.setUserStages(userStages);
			Date mapLastIncome = role.getMapLastIncome();
			Date date = new Date();
			if(mapLastIncome==null){
				result.setMapCoinsTime(0L);
			}else if(mapLastIncome.before(date)&&(date.getTime() - mapLastIncome.getTime())>=24*60*60*1000L){
				result.setMapCoinsTime(0L);
			}else if(mapLastIncome.before(date)&&(date.getTime() - mapLastIncome.getTime())<24*60*60*1000L){
				result.setMapCoinsTime(24*60*60*1000L-(date.getTime() - mapLastIncome.getTime()));
			}else{
				result.setMapCoinsTime(24*60*60*1000L);
			}
			returnType.setStatus(1);
			returnType.setValue(result);
			super.printReturnType2Response(response, returnType);
			return null;
		}catch (Exception e) {
			logger.error(e.getMessage());
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

package com.cgiser.moka.fight.group.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.cgiser.keel.http.utils.HttpSpringUtils;
import com.cgiser.moka.action.AbstractAction;
import com.cgiser.moka.common.utils.ServletUtil;
import com.cgiser.moka.manager.StageManager;
import com.cgiser.moka.manager.UserStageManager;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.model.Stage;
import com.cgiser.moka.model.UserStage;
import com.cgiser.moka.result.ReturnType;

public class IsShowTeamAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		ReturnType<Integer> returnType = new ReturnType<Integer>();
		String mapId = ServletUtil.getDefaultValue(request, "mapId", null);
		try{
			Role role = super.getCurrentRole(request);
			if(null==role){
				returnType.setMsg("您还没登录哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(!StringUtils.isNumeric(mapId)){
				returnType.setMsg("参数有误哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			UserStageManager userStageManager = (UserStageManager)HttpSpringUtils.getBean("userStageManager");
			StageManager stageManager = (StageManager)HttpSpringUtils.getBean("stageManager");
			List<UserStage> userStages = userStageManager.getUserStageByRoleIdMapId(role.getRoleId(), Integer.parseInt(mapId));
			List<Stage> stages = stageManager.getStagesByMapId(Integer.parseInt(mapId));
			Map<Integer, Integer> map = new HashMap<Integer, Integer>();
			if(!CollectionUtils.isEmpty(userStages)){
				for(UserStage userStage:userStages){
					map.put(userStage.getStageId(), userStage.getFinishedStage());
				}
			}
			int result = 1;
			if(!CollectionUtils.isEmpty(stages)){
				for(Stage stage:stages){
					if(map.get(stage.getRank())==null&&stage.getType()!=2){
						result = 0;
					}
				}
			}else{
				result = 0;
			}
			returnType.setStatus(1);
			returnType.setValue(result);
			super.printReturnType2Response(response, returnType);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
}

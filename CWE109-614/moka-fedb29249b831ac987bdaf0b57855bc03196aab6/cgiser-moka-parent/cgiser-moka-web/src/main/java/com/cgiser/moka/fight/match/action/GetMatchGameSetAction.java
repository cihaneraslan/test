package com.cgiser.moka.fight.match.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cgiser.keel.http.utils.HttpSpringUtils;
import com.cgiser.moka.action.AbstractAction;
import com.cgiser.moka.manager.MatchGameManager;
import com.cgiser.moka.manager.impl.MatchGameManagerImpl;
import com.cgiser.moka.model.MatchRoleCache;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.result.MatchGameSet;
import com.cgiser.moka.result.ReturnType;

public class GetMatchGameSetAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public static int cd = 1800;
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		ReturnType<MatchGameSet> returnType = new ReturnType<MatchGameSet>();
		try{
			Role role = super.getCurrentRole(request);
			if(null==role){
				returnType.setMsg("您还没登录哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			MatchGameSet matchGameSet = new MatchGameSet();
			MatchGameManager matchGameManager = (MatchGameManager)HttpSpringUtils.getBean("matchGameManager");
			MatchRoleCache matchRoleCache = matchGameManager.getMacthRole(role.getRoleId());
			matchGameSet.setMatchHasTimes(matchRoleCache.getMatchHasTimes());
			matchGameSet.setMatchTimesCash(10);
			matchGameSet.setMatchCutDown(matchRoleCache.getMatchCutDown());
			if(MatchGameManagerImpl.isStart){
				matchGameSet.setIsStart(1);
			}else{
				matchGameSet.setIsStart(0);
			}
			returnType.setStatus(1);
			returnType.setValue(matchGameSet);
			super.printReturnType2Response(response, returnType);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	} 
}

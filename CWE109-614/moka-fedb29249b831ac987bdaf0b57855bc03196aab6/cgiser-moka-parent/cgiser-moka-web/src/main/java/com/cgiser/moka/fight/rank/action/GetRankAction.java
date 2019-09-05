package com.cgiser.moka.fight.rank.action;

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
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.model.RankRole;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.result.RankRoleResult;
import com.cgiser.moka.result.ReturnType;

public class GetRankAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		ReturnType<RankRoleResult> returnType = new ReturnType<RankRoleResult>();
		try{
			Role role = super.getCurrentRole(request);
			if(null==role){
				returnType.setMsg("您还没登录哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			int rank = role.getRank();
			String strRank = ServletUtil.getDefaultValue(request, "rank", "not");
			if(StringUtils.isNumeric(strRank)){
				rank = Integer.parseInt(strRank);
			}
			RoleManager roleManager = (RoleManager)HttpSpringUtils.getBean("roleManager");
			List<RankRole> roles = roleManager.getRankRoles(rank);
			RankRoleResult result = new RankRoleResult();
			returnType.setStatus(1);
			result.setRank(role.getRank());
			result.setCompetitors(roles);
			returnType.setValue(result);
			super.printReturnType2Response(response, returnType);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	} 
}

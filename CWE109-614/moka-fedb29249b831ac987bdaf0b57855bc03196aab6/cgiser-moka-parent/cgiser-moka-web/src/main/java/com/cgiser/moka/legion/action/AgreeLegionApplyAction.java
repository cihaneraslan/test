package com.cgiser.moka.legion.action;

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
import com.cgiser.moka.manager.AchievementManager;
import com.cgiser.moka.manager.LegionManager;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.model.Legion;
import com.cgiser.moka.model.LegionApply;
import com.cgiser.moka.model.LegionContext;
import com.cgiser.moka.model.LegionEventEnum;
import com.cgiser.moka.model.Legioner;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.model.UserAchievement;
import com.cgiser.moka.result.ReturnType;

public class AgreeLegionApplyAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String id = ServletUtil.getDefaultValue(request,"id","");
		String type = ServletUtil.getDefaultValue(request,"type","");
		ReturnType<Long> returnType = new ReturnType<Long>();
		try{
			Role role = super.getCurrentRole(request);
			if(null==role){
				returnType.setMsg("您还没登录哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(StringUtils.isBlank(id)||!StringUtils.isNumeric(id)){
				returnType.setMsg("参数有误！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(StringUtils.isBlank(type)||!StringUtils.isNumeric(type)){
				returnType.setMsg("参数有误！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			
			LegionManager legionManager = (LegionManager)HttpSpringUtils.getBean("legionManager");
			Legioner legioner = legionManager.getLegioner(role.getRoleId());
			if(legioner.getDuty()!=1&&legioner.getDuty()!=2&&legioner.getDuty()!=3){
				returnType.setMsg("您没有权限批准哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			LegionApply legionApply = legionManager.getLegionApply(new Long(id));
			if(legionApply==null){
				returnType.setMsg("申请已经处理了哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			RoleManager roleManager = (RoleManager)HttpSpringUtils.getBean("roleManager");
			Role legionRole = roleManager.getRoleById(legionApply.getRoleId());
			if(legionRole==null){
				legionManager.delLegionApply(new Long(id));
				returnType.setMsg("申请已经处理了哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			Legioner legioner2 = legionManager.getLegioner(legionRole.getRoleId());
			if(legioner2!=null){
				legionManager.delLegionApply(new Long(id));
				returnType.setMsg("该玩家已经加入了其他的帮派");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			Legion legion = legionManager.getLegionById(legioner.getLegionId());
			List<Legioner> legioners = legionManager.getLegioner(legioner.getLegionId(), 1, 80);
			if(legioners.size()>=(LegionContext.LegionBaseMemberCount+LegionContext.LegionMemberEmblemLevel[legion.getLegionLevel()-1])){
				legionManager.delLegionApply(new Long(id));
				returnType.setMsg("帮派已经满员了");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(type.equals("1")){
				if(legionManager.AgreeLegionApply(new Long(id))>0){
					//加入帮派成就
					AchievementManager achievementManager = (AchievementManager)HttpSpringUtils.getBean("achievementManager");
					UserAchievement userAchievement = achievementManager.getUserAchievementById(legionRole.getRoleId(), 62);
					if(userAchievement==null){
						achievementManager.saveUserAchievement(62, legionRole.getRoleId(),1);
					}
					legionManager.delLegionApplyByRoleId(role.getRoleId());
					legionManager.saveLegionEvent(legioner.getLegionId(),legioner.getRoleId(), role.getRoleName()+"批准["+legionRole.getRoleName()+"]的入帮申请", LegionEventEnum.APPLYTOLEGION.getCode());
					returnType.setStatus(1);
					super.printReturnType2Response(response, returnType);
				}else{
					returnType.setStatus(0);
					super.printReturnType2Response(response, returnType);
				}	
			}else{
				if(legionManager.delLegionApply(new Long(id))>0){
					legionManager.saveLegionEvent(legioner.getLegionId(),legioner.getRoleId(), role.getRoleName()+"拒绝["+legionRole.getRoleName()+"]的入帮申请", LegionEventEnum.DELLEGION.getCode());
					returnType.setStatus(1);
					super.printReturnType2Response(response, returnType);
				}else{
					returnType.setStatus(0);
					super.printReturnType2Response(response, returnType);
				}	
			}
					
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
}

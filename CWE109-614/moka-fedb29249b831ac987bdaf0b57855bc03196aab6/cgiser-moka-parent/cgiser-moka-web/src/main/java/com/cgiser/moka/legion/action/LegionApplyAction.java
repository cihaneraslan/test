package com.cgiser.moka.legion.action;

import java.util.Date;
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
import com.cgiser.moka.manager.LegionManager;
import com.cgiser.moka.manager.support.DateUtils;
import com.cgiser.moka.model.Legion;
import com.cgiser.moka.model.LegionApply;
import com.cgiser.moka.model.LegionContext;
import com.cgiser.moka.model.Legioner;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.result.ReturnType;

public class LegionApplyAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		String legionId = ServletUtil.getDefaultValue(request, "legionId", "");
		ReturnType<Long> returnType = new ReturnType<Long>();
		try{
			Role role = super.getCurrentRole(request);
			if(null==role){
				returnType.setMsg("您还没登录哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(StringUtils.isBlank(legionId)&&!StringUtils.isNumeric(legionId)){
				logger.debug("获取帮派参数错误");
				returnType.setMsg("获取帮派参数错误");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			LegionManager legionManager = (LegionManager)HttpSpringUtils.getBean("legionManager");
			Legioner legioner = legionManager.getLegioner(role.getRoleId());
			if(legioner!=null){
				returnType.setMsg("您已经加入其他帮派");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			Legion legion = legionManager.getLegionById(new Long(legionId));
			if(legion==null){
				logger.debug("申请帮派不存在");
				returnType.setMsg("申请帮派不存在");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			List<Legioner> legioners = legionManager.getLegioner(legion.getId(), 1, 80);
			if(legioners.size()>=(LegionContext.LegionBaseMemberCount+LegionContext.LegionMemberEmblemLevel[legion.getLegionLevel()-1])){
				returnType.setMsg("帮派已经满员了");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			LegionApply legionApply = legionManager.getLegionApplyByRoleId(new Long(legionId),role.getRoleId());
			if(legionApply!=null){
				returnType.setStatus(1);
				returnType.setValue(legionApply.getUid());
				super.printReturnType2Response(response, returnType);
				return null;
			}
			Date date = legionManager.getOutLegionTime(role.getRoleId());
			if(date==null||!DateUtils.isSameDay(date, new Date())){
				Long id = legionManager.ApplyAddToLegion(new Long(legionId), role.getRoleId());
				if(id>0){
					returnType.setStatus(1);
					returnType.setValue(id);
					super.printReturnType2Response(response, returnType);
				}else{
					returnType.setStatus(0);
					super.printReturnType2Response(response, returnType);
				}
			}else{
				returnType.setStatus(0);
				returnType.setMsg("您只能等明天才能申请加入帮派");
				super.printReturnType2Response(response, returnType);
			}
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
}

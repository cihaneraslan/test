package com.cgiser.moka.card.action;

import java.util.List;

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
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.manager.UserCardGroupManager;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.model.UserCardGroup;
import com.cgiser.moka.result.ReturnType;

public class UpdateUserCardGroupAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		ReturnType<String> returnType = new ReturnType<String>();
		try{
			String strGroupId = ServletUtil.getDefaultValue(request, "groupId", "");
			Role role = super.getCurrentRole(request);
			if(null==role){
				returnType.setMsg("您还没登录哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(StringUtils.isEmpty(strGroupId)||!StringUtils.isNumeric(strGroupId)){
				returnType.setMsg("卡组ID不正确哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			Long groupId = new Long(strGroupId);
			UserCardGroupManager userCardGroupManager = (UserCardGroupManager)HttpSpringUtils.getBean("userCardGroupManager");
			
			List<UserCardGroup> cards = userCardGroupManager.getUserCardGroup(role.getRoleId());
			int flag = 0;
			UserCardGroup group = null;
			for(UserCardGroup userCardGroup : cards){
				if(userCardGroup.getGroupId().equals(groupId)){
					group = userCardGroup;
					flag=1;
				}
			}
			if(flag==0){
				returnType.setMsg("卡组ID不正确哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(CollectionUtils.isEmpty(group.getUserCardInfo())){
				returnType.setMsg("卡组中必须至少有一个卡牌！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			RoleManager roleManager = (RoleManager)HttpSpringUtils.getBean("roleManager");
			if(roleManager.updateRoleDefaultGroup(role.getRoleId(), groupId)>0){
				returnType.setStatus(1);
				returnType.setValue(strGroupId);
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

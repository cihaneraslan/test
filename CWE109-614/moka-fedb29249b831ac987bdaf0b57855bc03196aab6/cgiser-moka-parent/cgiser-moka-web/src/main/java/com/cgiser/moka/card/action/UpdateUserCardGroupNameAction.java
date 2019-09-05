package com.cgiser.moka.card.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cgiser.keel.http.utils.HttpSpringUtils;
import com.cgiser.moka.action.AbstractAction;
import com.cgiser.moka.common.utils.ServletUtil;
import com.cgiser.moka.manager.UserCardGroupManager;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.model.UserCardGroup;
import com.cgiser.moka.result.ReturnType;

public class UpdateUserCardGroupNameAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String groupName = ServletUtil.getDefaultValue(request, "groupName", null);
		String groupId = ServletUtil.getDefaultValue(request, "groupId", null);
		ReturnType<UserCardGroup> returnType = new ReturnType<UserCardGroup>();
		try{
			Role role = super.getCurrentRole(request);
			if(null==role){
				returnType.setMsg("您还没登录哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(groupId==null){
				returnType.setMsg("卡组ID不能为空");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(null==groupName){
				returnType.setMsg("名称不能为空");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			UserCardGroupManager userCardGroupManager = (UserCardGroupManager)HttpSpringUtils.getBean("userCardGroupManager");
			UserCardGroup userCardGroup = userCardGroupManager.getUserCardGroupById(new Long(groupId));
			if(null==userCardGroup){
				returnType.setMsg("修改的卡组不存在");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(userCardGroupManager.updateUserCardGroupName(new Long(groupId), groupName)>0){
				returnType.setStatus(1);
				userCardGroup.setGroupName(groupName);
				returnType.setValue(userCardGroup);
			}else{
				returnType.setStatus(0);
			}
			super.printReturnType2Response(response, returnType);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
}

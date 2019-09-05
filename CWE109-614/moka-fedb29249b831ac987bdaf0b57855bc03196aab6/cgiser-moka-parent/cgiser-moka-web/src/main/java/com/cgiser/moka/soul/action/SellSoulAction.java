package com.cgiser.moka.soul.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.cgiser.keel.http.utils.HttpSpringUtils;
import com.cgiser.moka.action.AbstractAction;
import com.cgiser.moka.common.utils.ServletUtil;
import com.cgiser.moka.manager.UserCardManager;
import com.cgiser.moka.manager.UserSoulManager;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.model.UserSoul;
import com.cgiser.moka.result.ReturnType;

public class SellSoulAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		ReturnType<Integer> returnType = new ReturnType<Integer>();
		try{
			String userSoulIds = ServletUtil.getDefaultValue(request, "userSoulIds", null);
			Role role = super.getCurrentRole(request);
			if(null==role){
				returnType.setMsg("您还没登录哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(userSoulIds==null){
				returnType.setMsg("先把要卖的东西放出来吧,亲！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			UserSoulManager userSoulManager = (UserSoulManager)HttpSpringUtils.getBean("userSoulManager");
			UserCardManager userCardManager = (UserCardManager)HttpSpringUtils.getBean("userCardManager");
			String[] ids = userSoulIds.split("_");
			UserSoul userSoul;
			List<String> idFlag = new ArrayList<String>(); 
			for(int i=0;i<ids.length;i++){
				userSoul = userSoulManager.getUserSoulById(new Long(ids[i]));
				if(userSoul==null){
					continue;
				}else{
					if(userCardManager.GetUserCardByUserSoulId(role.getRoleId(), userSoul.getUserSoulId())!=null){
						idFlag.add(ids[i]);
					}
				}
			}
			if(!CollectionUtils.isEmpty(idFlag)){
				returnType.setMsg("您有已经装备的武器被出售哦,亲！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			returnType.setStatus(1);
			returnType.setValue(userSoulManager.sellSoul(role.getRoleId(), userSoulIds));
			super.printReturnType2Response(response, returnType);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
}

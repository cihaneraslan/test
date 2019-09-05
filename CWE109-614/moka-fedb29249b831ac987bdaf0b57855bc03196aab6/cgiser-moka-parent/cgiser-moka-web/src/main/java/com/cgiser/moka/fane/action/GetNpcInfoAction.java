package com.cgiser.moka.fane.action;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cgiser.core.common.cache.mem.MemCachedManager;
import com.cgiser.keel.http.utils.HttpSpringUtils;
import com.cgiser.moka.action.AbstractAction;
import com.cgiser.moka.dao.util.DigestUtils;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.result.FaneResult;
import com.cgiser.moka.result.ReturnType;

public class GetNpcInfoAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		ReturnType<FaneResult> returnType = new ReturnType<FaneResult>();
		try{
			Role role = super.getCurrentRole(request);
			if(role==null){
				returnType.setValue(null);
				returnType.setStatus(0);
				returnType.setMsg("您还没登录哦!");
				super.printReturnType2Response(response, returnType);
				return null;
			}
			MemCachedManager faneCachedManager = (MemCachedManager)HttpSpringUtils.getBean("faneCachedManager");
			FaneResult result = (FaneResult)faneCachedManager.get("fane_"+DigestUtils.digest(role.getRoleName()));
			if(result==null){
				result = new FaneResult();
				Set<Integer> npcList = new HashSet<Integer>();
				npcList.add(0);
				result.setNpcList(npcList);
				faneCachedManager.set("fane_"+DigestUtils.digest(role.getRoleName()), 0, result);
			}
			result.setCoins(role.getCoins());
			returnType.setValue(result);
			returnType.setStatus(1);
			super.printReturnType2Response(response, returnType);
			return null;
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
}

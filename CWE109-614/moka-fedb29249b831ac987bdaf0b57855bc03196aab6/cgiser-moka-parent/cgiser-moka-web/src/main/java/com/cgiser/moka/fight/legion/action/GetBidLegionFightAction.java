package com.cgiser.moka.fight.legion.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cgiser.moka.action.AbstractAction;
import com.cgiser.moka.common.utils.ServletUtil;
import com.cgiser.moka.model.BidLegion;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.result.ReturnType;

public class GetBidLegionFightAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		ReturnType<List<BidLegion>> returnType = new ReturnType<List<BidLegion>>();
		String city = ServletUtil.getDefaultValue(request, "city", null);
		try{
			Role role = super.getCurrentRole(request);
			if(null==role){
				returnType.setMsg("您还没登录哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(null==city){
				returnType.setMsg("参数不正确");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			
//			LegionFightManager legionFightManager = (LegionFightManager)HttpSpringUtils.getBean("legionFightManager");
//			List<BidLegion> bidLegions = legionFightManager.getBidLegions(city);
			returnType.setStatus(1);
//			returnType.setValue(bidLegions);
			super.printReturnType2Response(response, returnType);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
}

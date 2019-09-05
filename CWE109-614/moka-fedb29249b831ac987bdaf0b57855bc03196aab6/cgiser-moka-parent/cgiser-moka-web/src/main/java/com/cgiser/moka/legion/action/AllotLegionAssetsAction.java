package com.cgiser.moka.legion.action;

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
import com.cgiser.moka.manager.LegionManager;
import com.cgiser.moka.model.Legion;
import com.cgiser.moka.model.LegionEventEnum;
import com.cgiser.moka.model.Legioner;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.result.ReturnType;

public class AllotLegionAssetsAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String info = ServletUtil.getDefaultValue(request,"info","");
		ReturnType<String> returnType = new ReturnType<String>();
		try{
			Role role = super.getCurrentRole(request);
			if(null==role){
				returnType.setMsg("您还没登录哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			LegionManager legionManager = (LegionManager)HttpSpringUtils.getBean("legionManager");
			Legioner legioner = legionManager.getLegioner(role.getRoleId());
			if(legioner==null){
				returnType.setMsg("您还没加入帮派哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			Legion legion  = legionManager.getLegionById(legioner.getLegionId());
			if(legion==null){
				returnType.setMsg("帮派不存在！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(legioner.getDuty()!=1){
				returnType.setMsg("只有帮主有权限分配资产哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
		    }
			if(StringUtils.isEmpty(info)){
				returnType.setMsg("资产分配错误哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			String[] infos = info.split(",");
			if(infos.length!=5){
				returnType.setMsg("资产分配错误哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			Map<Integer,Integer> map = new HashMap<Integer, Integer>();
			int duty = 0;
			int resourse = 0;
			int coins = 0;
			for(String ids:infos){
				if(!StringUtils.isNumeric(ids.split("_")[0])||!StringUtils.isNumeric(ids.split("_")[1])){
					returnType.setMsg("资产分配错误哦！");
					returnType.setStatus(0);
					super.printReturnType2Response(response, returnType);
					return null;
				}
				duty = Integer.parseInt(ids.split("_")[0]);
				resourse = Integer.parseInt(ids.split("_")[1]);
				if(duty>5||duty<1){
					returnType.setMsg("资产分配错误哦！");
					returnType.setStatus(0);
					super.printReturnType2Response(response, returnType);
					return null;
				}
				if(resourse<0){
					returnType.setMsg("资产分配错误哦！");
					returnType.setStatus(0);
					super.printReturnType2Response(response, returnType);
					return null;
				}
				duty = Integer.parseInt(ids.split("_")[0]);
				resourse = Integer.parseInt(ids.split("_")[1]);
				map.put(duty, resourse);
				
			}
			for(int i=1;i<6;i++){
				List<Legioner> legioners = legionManager.getLegionersByduty(legion.getId(), i);
				if(!CollectionUtils.isEmpty(legioners)){
					coins = map.get(i)*legioners.size()+coins;
				}
			}
			if(legion.getResources()<coins){
				returnType.setMsg("资产不够哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(legionManager.delLegionResources(legion.getId(), coins)>0){
				for(int i=1;i<6;i++){
					if(map.get(i)>0){
						List<Legioner> legioners = legionManager.getLegionersByduty(legion.getId(), i);
						if(!CollectionUtils.isEmpty(legioners)){
							legionManager.saveLegionEvent(legion.getId(), role.getRoleId(), role.getRoleName()+"给"+getLegionDutyName(i)+"发放了"+map.get(i)+"个功勋", LegionEventEnum.ALLOTRESOURCE.getCode());
							for(Legioner legioner2:legioners){
								legionManager.addLegionerHonor(legioner2.getId(), map.get(i));
							}
						}
					}
				}
			}
			returnType.setStatus(1);
			super.printReturnType2Response(response, returnType);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
	private String getLegionDutyName(int duty){
		if(duty==1){
			return "帮主";
		}
		if(duty==2){
			return "副帮主";
		}
		if(duty==3){
			return "堂主";
		}
		if(duty==4){
			return "长老";
		}
		if(duty==5){
			return "帮众";
		}
		return "帮众";
	}
}

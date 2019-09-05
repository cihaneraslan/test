package com.cgiser.moka.legion.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cgiser.moka.action.AbstractAction;
import com.cgiser.moka.model.LegionSetting;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.result.ReturnType;

public class GetLegionSettingAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		ReturnType<LegionSetting> returnType = new ReturnType<LegionSetting>();
		try{
			Role role = super.getCurrentRole(request);
			if(null==role){
				returnType.setMsg("您还没登录哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			
			LegionSetting legionSetting = new LegionSetting();
			legionSetting.setCash_contribute_limit(10000);
			legionSetting.setConis_contribute_limit(1000000);
			legionSetting.setCreate_legion_cash(200);
			legionSetting.setCreate_legion_coins(100000);
			legionSetting.setCreate_level_limit(25);
			legionSetting.setBuy_legionerFightAttack_cash(10);
			//可掠夺的资产除数
			legionSetting.setRob_legion_res(5);
			legionSetting.setBuy_legionFightAttack_cash(50);
			Map<String, String> duty = new HashMap<String, String>();
			duty.put("1", "{amount:1,num:1,name:帮主}");
			duty.put("2", "{amount:1,num:1,name:副帮主}");
			duty.put("3", "{amount:1,num:3,name:长老}");
			duty.put("4", "{amount:1,num:5,name:堂主}");
			duty.put("5", "{amount:1,num:0,name:帮众}");
			legionSetting.setDuty(duty);
			legionSetting.setJoin_legion_level(25);
			returnType.setStatus(1);
			returnType.setValue(legionSetting);
			super.printReturnType2Response(response, returnType);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
}

package com.cgiser.moka.fight.legion.action;

import java.lang.reflect.Method;
import java.util.ArrayList;
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

import com.cgiser.keel.http.utils.HttpSpringUtils;
import com.cgiser.moka.action.AbstractAction;
import com.cgiser.moka.common.utils.ServletUtil;
import com.cgiser.moka.manager.LegionFightManager;
import com.cgiser.moka.manager.LegionManager;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.model.Legion;
import com.cgiser.moka.model.LegionContext;
import com.cgiser.moka.model.LegionFight;
import com.cgiser.moka.model.Legioner;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.result.LegionFightResult;
import com.cgiser.moka.result.ReturnType;

public class GetEndLegionFightAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		ReturnType<Object> returnType = new ReturnType<Object>();
		try {
			Role role = super.getCurrentRole(request);
			if (null == role) {
				returnType.setMsg("您还没登录哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			String id = ServletUtil.getDefaultValue(request, "id", "");
			if(!StringUtils.isNumeric(id)){
				returnType.setMsg("参数有误哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			LegionManager legionManager = (LegionManager) HttpSpringUtils
					.getBean("legionManager");
			Legioner legioner = legionManager.getLegioner(role.getRoleId());
			if (legioner == null) {
				returnType.setMsg("您还没加入帮派哦");
				returnType.setStatus(0);
				returnType.setValue(1);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			LegionFightManager legionFightManager = (LegionFightManager) HttpSpringUtils
					.getBean("legionFightManager");
			Legion attack = legionManager.getLegionById(legioner.getLegionId());
			LegionFight legionFight = legionFightManager.getEndLegionFightById(new Long(id));
			if (legionFight != null) {
				returnType.setMsg("成功获取帮派战");
				returnType.setStatus(1);
				Legion defend = legionManager.getLegionById(legionFight
						.getDefend());
				if (defend == null) {
					returnType.setMsg("您还没加入帮派哦");
					returnType.setStatus(0);
					returnType.setValue(1);
					super.printReturnType2Response(response, returnType);
					return null;
				}
				int guardNum = legionFight.getLevel() > 5 ? 9 : 5;
				LegionFightResult val = new LegionFightResult();
				val.setLastTime(20 * 60 - (System.currentTimeMillis() - legionFight
								.getTime().getTime()) / 1000);
				val.setLegionFightId(legionFight.getId());
				val.setLegionId(legionFight.getDefend());
				val.setLegionName(defend.getName());
				List<Map<String, String>> listGuard = new ArrayList<Map<String, String>>();
				Map<String, String> map = new HashMap<String, String>();
				Role aRole;
				Role dRole;
				RoleManager roleManager = (RoleManager) HttpSpringUtils
						.getBean("roleManager");
				String guardInfo;
				int[] LegionGuardCoins = guardNum>5?LegionContext.LegionGuardCoins69:LegionContext.LegionGuardCoins05;
				for (int i = 1; i <= guardNum; i++) {
					map = new HashMap<String, String>();
					guardInfo = (String) getFieldValueByName("guard" + i,
							legionFight);
					Long aRoleId = new Long(guardInfo.split("_")[0]);
					int win = Integer.parseInt(guardInfo.split("_")[1]);
					map.put("coins", LegionGuardCoins[i-1]+"%");
					if (aRoleId > 0) {
						aRole = roleManager.getRoleById(aRoleId);
						map.put("guard", aRole.getRoleName());
						map.put("win", guardInfo.split("_")[1]);
						if (win > 0) {
							Long dRoleId = new Long(guardInfo.split("_")[2]);
							dRole = roleManager.getRoleById(dRoleId);
							map.put("winner", dRole.getRoleName());
						} else {
							map.put("winner", "0");
						}
					} else {
						map.put("guard", "此处无守卫");
						map.put("win", guardInfo.split("_")[1]);
						if(win>0){
							Long dRoleId = new Long(guardInfo.split("_")[2]);
							dRole = roleManager.getRoleById(dRoleId);
							map.put("winner", dRole.getRoleName());
						}else{
							map.put("winner", "0");
						}
						
					}
					listGuard.add(map);
				}
				val.setListGuard(listGuard);
				val.setLegionerLastAttack(legioner.getLastAttack());
			    val.setLegionLastAttack(attack.getLastAttack());
			    val.setWin(legionFight.getWin());
			    val.setResource(legionFight.getResource());
				returnType.setValue(val);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			returnType.setMsg("您是帮主赶紧去发起帮派战吧");
			returnType.setValue(2);
			returnType.setStatus(0);
			super.printReturnType2Response(response, returnType);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	private Object getFieldValueByName(String fieldName, Object o) {
		try {
			String firstLetter = fieldName.substring(0, 1).toUpperCase();
			String getter = "get" + firstLetter + fieldName.substring(1);
			Method method = o.getClass().getMethod(getter, new Class[] {});
			Object value = method.invoke(o, new Object[] {});
			return value;
		} catch (Exception e) {
			return null;
		}
	}
}

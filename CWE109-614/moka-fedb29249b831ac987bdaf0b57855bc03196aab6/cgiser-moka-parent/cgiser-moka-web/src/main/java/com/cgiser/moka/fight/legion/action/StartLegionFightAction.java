package com.cgiser.moka.fight.legion.action;

import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
import com.cgiser.moka.model.Legion;
import com.cgiser.moka.model.LegionFight;
import com.cgiser.moka.model.Legioner;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.result.ReturnType;

public class StartLegionFightAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	private final static Lock legionAttackLock = new ReentrantLock();
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		ReturnType<String> returnType = new ReturnType<String>();
		String LegionId = ServletUtil.getDefaultValue(request, "legionId", null);
		try{
			Role role = super.getCurrentRole(request);
			if(null==role){
				returnType.setMsg("您还没登录哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(!StringUtils.isNumeric(LegionId)){
				returnType.setMsg("参数不正确哦");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			LegionManager legionManager = (LegionManager)HttpSpringUtils.getBean("legionManager");
			Legioner legioner = legionManager.getLegioner(role.getRoleId());
			if(legioner==null){
				returnType.setMsg("您没有权限哦");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(legioner.getDuty()!=1&&legioner.getDuty()!=2){
				returnType.setMsg("您没有权限哦");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			LegionFightManager legionFightManager = (LegionFightManager)HttpSpringUtils.getBean("legionFightManager");
			legionAttackLock.lock();
			Legion attack = legionManager.getLegionById(legioner.getLegionId());
			
			LegionFight legionFight1 = legionFightManager.getLegionFightByattack(attack.getId());
			if(legionFight1!=null&&!legionFightManager.isEnd(legionFight1)){
				returnType.setMsg("帮派正在进攻别的帮派");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(attack.getLastAttack()<1){
				returnType.setMsg("今天的帮战次数用完了哦");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			Legion defend = legionManager.getLegionById(new Long(LegionId));
			if(defend==null){
				returnType.setMsg("您进攻的帮派不存在");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(defend.getRobTimes()>2){
				returnType.setMsg("该帮派今天已经破产了");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			LegionFight legionFight2 = legionFightManager.getLegionFightBydefend(defend.getId());
			if(legionFight2!=null&&!legionFightManager.isEnd(legionFight2)){
				returnType.setMsg("该帮派正在被进攻");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			Long id = legionFightManager.addLegionFight(role.getRoleId(), attack.getId(), defend.getId(), new Date());
			if(id<1){
				returnType.setMsg("未能成功发起帮派战");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			legionManager.updateLegionFightAttackTimes(attack.getId());
			returnType.setMsg("成功发起了帮派战");
			returnType.setStatus(1);
			super.printReturnType2Response(response, returnType);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}finally{
			legionAttackLock.unlock();
		}
		return null;
	}
}

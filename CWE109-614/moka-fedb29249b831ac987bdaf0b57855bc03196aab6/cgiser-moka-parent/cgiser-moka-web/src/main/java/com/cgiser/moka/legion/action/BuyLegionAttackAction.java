package com.cgiser.moka.legion.action;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cgiser.keel.http.utils.HttpSpringUtils;
import com.cgiser.moka.action.AbstractAction;
import com.cgiser.moka.manager.LegionManager;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.model.Legion;
import com.cgiser.moka.model.LegionContext;
import com.cgiser.moka.model.LegionEventEnum;
import com.cgiser.moka.model.Legioner;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.result.ReturnType;

public class BuyLegionAttackAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	private final static Lock legionAttackLock = new ReentrantLock();
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
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
			legionAttackLock.lock();
			Legion legion  = legionManager.getLegionById(legioner.getLegionId());
			if(legion==null){
				returnType.setMsg("帮派不存在！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			int a = legion.getBuyAttack()>3?3: legion.getBuyAttack();
			if(role.getCash()<LegionContext.BuyAttackCash[a]){
				returnType.setMsg("您的元宝不够哦！");
				returnType.setValue(String.valueOf(LegionContext.BuyAttackCash[a]));
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			RoleManager roleManager = (RoleManager)HttpSpringUtils.getBean("roleManager");
			if(roleManager.updateCash(role.getRoleName(), LegionContext.BuyAttackCash[a])){
				if(legionManager.addLegionFightAttackTimes(legion.getId())<1){
					logger.error("buyLegionerTimesError"+role.getRoleName());
				}
				legionManager.addLegionFightBuyAttackTimes(legion.getId());
				legionManager.saveLegionEvent(legion.getId(), role.getRoleId(), role.getRoleName()+"购买了一次帮派战", LegionEventEnum.BUYLEGIONTIMES.getCode());
			}else{
				returnType.setMsg("购买失败哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			returnType.setValue(String.valueOf(LegionContext.BuyAttackCash[a+1>3?3:a+1]));
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

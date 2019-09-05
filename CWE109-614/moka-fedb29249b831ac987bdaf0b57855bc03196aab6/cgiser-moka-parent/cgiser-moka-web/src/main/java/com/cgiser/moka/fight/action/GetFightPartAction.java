package com.cgiser.moka.fight.action;

import java.util.ArrayList;
import java.util.List;

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
import com.cgiser.moka.manager.FightManager;
import com.cgiser.moka.manager.FreeFightManager;
import com.cgiser.moka.model.Battle;
import com.cgiser.moka.model.FightResult;
import com.cgiser.moka.model.Player;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.model.Round;
import com.cgiser.moka.result.FightPartResult;
import com.cgiser.moka.result.ReturnType;

public class GetFightPartAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String battleId = ServletUtil.getDefaultValue(request, "battleid", null);
		String roundStr = ServletUtil.getDefaultValue(request, "round", null);
		String messageKey = ServletUtil.getDefaultValue(request, "messageKey", null);
		ReturnType<FightPartResult> returnType = new ReturnType<FightPartResult>();
		try{
			Role role = super.getCurrentRole(request);
			if(null==role){
				returnType.setMsg("您还没登录哦,亲！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(battleId==null||roundStr==null||messageKey==null){
				returnType.setValue(null);
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
			}
			FightManager fightManager = (FightManager)HttpSpringUtils.getBean("fightManager");
			FightResult fightResult = fightManager.getFight(battleId);
			Battle battle = fightResult.getBattle();
			boolean isHand = false;
			if(battle==null){
				returnType.setMsg("战斗不存在哦,亲！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
/*			if(strIsHand.equals("1")){
				isHand = true;
			}*/
			FreeFightManager freeFightManager = (FreeFightManager)HttpSpringUtils.getBean("freeFightManager");
			freeFightManager.removeFightMessage(messageKey);
			int j = Integer.parseInt(roundStr);
			List<Round> rounds = new ArrayList<Round>();
			Round round;
			//计算当前回合数
			for(int i=0;i<battle.getRounds().size();i++){
				round = battle.getRounds().get(i);
				if(round.getRound()>=j){
					rounds.add(round);
					round.setIsAttack(!round.getIsAttack());
				}
			}
			if(!fightResult.getAttackPlayer().getNickName().equals(role.getRoleName())){
				Player dPlayer = fightResult.getDefendPlayer();
				fightResult.setDefendPlayer(fightResult.getAttackPlayer());
				fightResult.setAttackPlayer(dPlayer);
				fightResult.setSalaries(fightResult.getdSalaries());
				if(fightResult.getWin()==1){
					fightResult.setWin(2);
				}else if(fightResult.getWin()==2){
					fightResult.setWin(1);
				}
				
			}
			fightResult.getBattle().setRounds(rounds);
			returnType.setStatus(1);
			FightPartResult result = new FightPartResult();
			result.setMessageKey(messageKey);
			result.setResult(fightResult);
			returnType.setValue(result);
			super.printReturnType2Response(response, returnType);
			return null;
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		try{
			returnType.setValue(null);
			returnType.setStatus(0);
			super.printReturnType2Response(response, returnType);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);// TODO: handle exception
		}
		return null;
		
	}
}

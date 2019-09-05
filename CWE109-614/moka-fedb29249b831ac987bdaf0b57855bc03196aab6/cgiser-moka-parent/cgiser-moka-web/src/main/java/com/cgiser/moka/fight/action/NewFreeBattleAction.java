package com.cgiser.moka.fight.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.DynamicChannelBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.cgiser.core.common.cache.mem.MemCachedManager;
import com.cgiser.keel.http.utils.HttpSpringUtils;
import com.cgiser.moka.action.AbstractAction;
import com.cgiser.moka.common.utils.ServletUtil;
import com.cgiser.moka.dao.util.DigestUtils;
import com.cgiser.moka.manager.FightManager;
import com.cgiser.moka.manager.FighterManager;
import com.cgiser.moka.manager.LegionFightManager;
import com.cgiser.moka.manager.LegionManager;
import com.cgiser.moka.manager.MessageManager;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.message.netty.MessageUtil;
import com.cgiser.moka.model.Battle;
import com.cgiser.moka.model.FightCard;
import com.cgiser.moka.model.FightResult;
import com.cgiser.moka.model.Legion;
import com.cgiser.moka.model.LegionFight;
import com.cgiser.moka.model.Legioner;
import com.cgiser.moka.model.Opp;
import com.cgiser.moka.model.Player;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.model.Round;
import com.cgiser.moka.result.ReturnType;

public class NewFreeBattleAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		Logger logger = LoggerFactory.getLogger("fight");
		String battleId = ServletUtil.getDefaultValue(request, "battleid", null);
		String strIsHand = ServletUtil.getDefaultValue(request, "ishand", null);
		String cardIds = ServletUtil.getDefaultValue(request, "cards", null);
		ReturnType<FightResult> returnType = new ReturnType<FightResult>();
		try{
			Role role = super.getCurrentRole(request);
			if(null==role){
				returnType.setMsg("您还没登录哦,亲！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(battleId==null||strIsHand==null){
				returnType.setMsg("参数有误,亲！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
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
			if(strIsHand.equals("1")){
				isHand = true;
			}
			int type = fightResult.getType();
			//计算当前回合数
			Round nRound = battle.getRounds().get(battle.getRounds().size()-1);
			List<Round> rounds = new ArrayList<Round>();
			//记录手动玩家当前上牌玩家的上牌操作
			List<Opp> oppsNewCard = new ArrayList<Opp>();
			//当前玩家手动上牌的ID数组
			String[] uuids;
			if(cardIds==null){
				uuids = new String[0];
			}else{
				uuids = cardIds.split(",");
			}
			//玩家
			Player aPlayer = fightResult.getAttackPlayer();
			Player dPlayer = fightResult.getDefendPlayer();
			//战斗区域卡牌
			Map<String, FightCard> dFightCards;
			Map<String, FightCard> aFightCards;
			if(fightResult.getDefendPlayer().getFightCards()!=null){
				dFightCards = dPlayer.getFightCards();
			}else{
				dFightCards = new HashMap<String, FightCard>();
				dPlayer.setFightCards(dFightCards);
			}
			if(fightResult.getAttackPlayer().getFightCards()!=null){
				aFightCards = aPlayer.getFightCards();
			}else{
				aFightCards = new HashMap<String, FightCard>();
				aPlayer.setFightCards(aFightCards);
			}
			//玩家墓地
			List<FightCard> aTumbCards;
			List<FightCard> dTumbCards;
			if(aPlayer.getTumbCards()!=null){
				aTumbCards = aPlayer.getTumbCards();
			}else{
				aTumbCards = new ArrayList<FightCard>();
				aPlayer.setTumbCards(aTumbCards);
			}
			if(dPlayer.getTumbCards()!=null){
				dTumbCards = dPlayer.getTumbCards();
			}else{
				dTumbCards = new ArrayList<FightCard>();
				dPlayer.setTumbCards(dTumbCards);
			}
			boolean isDef = false;;
			if(role.getRoleName().equals(fightResult.getDefendPlayer().getNickName())){
				isDef =true;
			}
			if(isDef){
				dPlayer = fightResult.getDefendPlayer();
				fightResult.setDefendPlayer(fightResult.getAttackPlayer());
				fightResult.setAttackPlayer(dPlayer);
				aPlayer = fightResult.getAttackPlayer();
				dPlayer = fightResult.getDefendPlayer();
			}
			aPlayer.setHand(isHand);

			//循环战斗回合
			fightManager.runRound1(aPlayer, dPlayer,nRound.getRound(),rounds,fightResult,uuids,oppsNewCard);
			
			if(this.isWin(aPlayer, dPlayer)&&fightResult.getWin()==0){
				FighterManager fighterManager = (FighterManager)HttpSpringUtils.getBean("fighterManager");
				fighterManager.handlerFightResult(aPlayer, dPlayer, type,fightResult);
				//设置角色状态为空闲
				RoleManager roleManager = (RoleManager)HttpSpringUtils.getBean("roleManager");
				roleManager.updateRoleStatus(role.getRoleName(), 0);
				LegionFightManager legionFightManager = (LegionFightManager)HttpSpringUtils.getBean("legionFightManager");
				LegionManager legionManager = (LegionManager)HttpSpringUtils.getBean("legionManager");
				Legioner legioner = legionManager.getLegioner(role.getRoleId());
				Legion attack = legionManager.getLegionById(legioner.getLegionId());
				LegionFight legionFight = legionFightManager.getLegionFightByattack(attack.getId());
				MemCachedManager groupCachedManager = (MemCachedManager)HttpSpringUtils.getBean("groupCachedManager");
				Role dRole = roleManager.getRoleByName(dPlayer.getNickName());
				String guardInfo = (String)groupCachedManager.get("group_"+DigestUtils.digest(legionFight.getDefend()+"guard"+dRole.getRoleId()));
				if(isWinner(aPlayer, dPlayer)){
					legionFightManager.defeatGuard(legionFight.getId(), Integer.parseInt(guardInfo.split("_")[1]), role.getRoleId());
				}
				
				groupCachedManager.delete("group_"+DigestUtils.digest(legionFight.getDefend()+"guard"+dRole.getRoleId()));
//				legionFightManager
			}
			//初始生成战斗模型后通知挑战者开始战斗
			this.setFightResult(aPlayer, dPlayer, role);
			battle.setRounds(rounds);
			fightResult.setAttackPlayer(aPlayer);
			fightResult.setBattle(battle);
			fightResult.setDefendPlayer(dPlayer);
			fightManager.saveFight(fightResult);
			returnType.setStatus(1);
			returnType.setValue(fightResult);
			super.printReturnType2Response(response, returnType);
			//区分手动操作玩家与对对方播放自己上牌动画的OPP操作
			if(!CollectionUtils.isEmpty(oppsNewCard)){
				logger.error(JSONArray.fromObject(oppsNewCard).toString());
				List<Opp> opps1 = new ArrayList<Opp>();
				opps1.addAll(oppsNewCard);
				opps1.addAll(rounds.get(0).getOpps());
				rounds.get(0).setOpps(opps1);
				battle.setRounds(rounds);
				fightResult.setBattle(battle);
				fightManager.saveFight(fightResult);
			}
			
		}catch (Exception e) {
			logger.error("fight error:",e);
		}

		return null;
	}
	private void setFightResult(Player aPlayer,Player dPlayer,Role role){
		boolean isWin = false;
		if(aPlayer.getHP()<=0||(aPlayer.getFightCards().size()==0&&aPlayer.getPreCards().size()==0&&aPlayer.getHandsCards().size()==0)){
			isWin = true;
		}
		if(this.isWin(aPlayer, dPlayer)){
			ChannelBuffer responseBuffer = new DynamicChannelBuffer(200);
			responseBuffer.writeInt(1002);
			MessageUtil.writeString(responseBuffer, "[战报]", "UTF-8");
			MessageUtil.writeString(responseBuffer, aPlayer.getNickName(), "UTF-8");
			responseBuffer.writeInt(2);
			MessageUtil.writeString(responseBuffer, "您在刚才的切磋中".concat(isWin?"输给":"赢").concat("了").concat(dPlayer.getNickName()), "UTF-8");
			MessageManager messageManager = (MessageManager)HttpSpringUtils.getBean("messageManager");
			messageManager.sendMessageToRole(aPlayer.getNickName(),responseBuffer.copy());
			responseBuffer = new DynamicChannelBuffer(200);
			responseBuffer.writeInt(1002);
			MessageUtil.writeString(responseBuffer, "[战报]", "UTF-8");
			MessageUtil.writeString(responseBuffer, dPlayer.getNickName(), "UTF-8");
			responseBuffer.writeInt(2);
			MessageUtil.writeString(responseBuffer, "您在刚才的切磋中".concat(isWin?"赢":"输给").concat("了").concat(aPlayer.getNickName()), "UTF-8");
			messageManager.sendMessageToRole(dPlayer.getNickName(),responseBuffer);
		}
	}
	private boolean isWin(Player aPlayer,Player dPlayer){
		if(aPlayer.getHP()<=0||(aPlayer.getFightCards().size()==0&&aPlayer.getPreCards().size()==0&&aPlayer.getHandsCards().size()==0)){
			return true;
		}
		if(dPlayer.getHP()<=0||(dPlayer.getFightCards().size()==0&&dPlayer.getPreCards().size()==0&&dPlayer.getHandsCards().size()==0)){
			return true;
		}
		return false;
	}
	private boolean isWinner(Player aPlayer,Player dPlayer){
		if(aPlayer.getHP()<=0||(aPlayer.getFightCards().size()==0&&aPlayer.getPreCards().size()==0&&aPlayer.getHandsCards().size()==0)){
			return false;
		}
		if(dPlayer.getHP()<=0||(dPlayer.getFightCards().size()==0&&dPlayer.getPreCards().size()==0&&dPlayer.getHandsCards().size()==0)){
			return true;
		}
		return false;
	}
	
}

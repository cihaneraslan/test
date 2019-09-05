package com.cgiser.moka.fight.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.cgiser.moka.common.utils.ListUtil;
import com.cgiser.moka.common.utils.ServletUtil;
import com.cgiser.moka.manager.CardManager;
import com.cgiser.moka.manager.FightManager;
import com.cgiser.moka.manager.FighterManager;
import com.cgiser.moka.manager.FreeFightManager;
import com.cgiser.moka.manager.HeroLevelManager;
import com.cgiser.moka.manager.MatchGameManager;
import com.cgiser.moka.manager.MessageManager;
import com.cgiser.moka.manager.RobRoleCacheManager;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.manager.SkillManager;
import com.cgiser.moka.manager.impl.MatchGameManagerImpl;
import com.cgiser.moka.manager.support.RoleStatusScanThread;
import com.cgiser.moka.message.netty.MessageUtil;
import com.cgiser.moka.model.Battle;
import com.cgiser.moka.model.Card;
import com.cgiser.moka.model.ExtData;
import com.cgiser.moka.model.FightCard;
import com.cgiser.moka.model.FightResult;
import com.cgiser.moka.model.FightRune;
import com.cgiser.moka.model.HeroLevel;
import com.cgiser.moka.model.MatchRoleCache;
import com.cgiser.moka.model.MessageType;
import com.cgiser.moka.model.Opp;
import com.cgiser.moka.model.Player;
import com.cgiser.moka.model.RankRoleCache;
import com.cgiser.moka.model.RobRoleCache;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.model.Room;
import com.cgiser.moka.model.Round;
import com.cgiser.moka.model.Skill;
import com.cgiser.moka.result.ReturnType;

public class FreeFightAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	private int[] vipEnergy = {10,10,15,23};
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		try{
			String dRoleName = ServletUtil.getDefaultValue(request, "role", null);
			String strIsHand = ServletUtil.getDefaultValue(request, "ishand", null);
			String strType = ServletUtil.getDefaultValue(request, "type", "1");
			int type = Integer.parseInt(strType);
			RoleManager roleManager = (RoleManager)HttpSpringUtils.getBean("roleManager");
			ReturnType<FightResult> returnType = new ReturnType<FightResult>();
			if(type==1){
				//匹配战
				if(!MatchGameManagerImpl.isStart){
					returnType.setMsg("匹配战还没开启哦！");
					returnType.setStatus(0);
					super.printReturnType2Response(response, returnType);
					return null;
				}
				Role aRole = super.getCurrentRole(request);
				MatchGameManager matchGameManager = (MatchGameManager)HttpSpringUtils.getBean("matchGameManager");
				MatchRoleCache matchRoleCache = matchGameManager.getMacthRole(aRole.getRoleId());
				if(matchRoleCache.getMatchHasTimes()<1){
					returnType.setMsg("今天没有匹配次数了哦，明天再来吧,亲！");
					returnType.setStatus(0);
					super.printReturnType2Response(response, returnType);
					return null;
				}
				matchGameManager.updateRoleMatchTimeAndCD(aRole.getRoleId());
				Role dRole = roleManager.getRoleByName(dRoleName);
				matchRoleCache = matchGameManager.getMacthRole(dRole.getRoleId());
				if(matchRoleCache.getMatchHasTimes()<1){
					returnType.setMsg("今天没有匹配次数了哦，明天再来吧,亲！");
					returnType.setStatus(0);
					super.printReturnType2Response(response, returnType);
					return null;
				}
				matchGameManager.updateRoleMatchTimeAndCD(dRole.getRoleId());
			}else if(type==2){
				//排名战
				Role role = super.getCurrentRole(request);
				MemCachedManager rankCachedManager = (MemCachedManager)HttpSpringUtils.getBean("rankCachedManager");
				RankRoleCache rankRoleCache = (RankRoleCache)rankCachedManager.get("rank_"+String.valueOf(role.getRoleId()));
				if(rankRoleCache.getRankHasTimes()<1){
					returnType.setMsg("今天没有排名战次数了哦，明天再来吧,亲！");
					returnType.setStatus(0);
					super.printReturnType2Response(response, returnType);
					return null;
				}
			}else if(type==3){
				//入侵玩家
				Role role = super.getCurrentRole(request);
				RobRoleCacheManager robRoleCacheManager = (RobRoleCacheManager)HttpSpringUtils.getBean("robRoleCacheManager");
				RobRoleCache robRoleCache = robRoleCacheManager.getRobRoleCache(role.getRoleId());
				if(robRoleCache==null||robRoleCache.getRobHasTimes()<1){
					returnType.setMsg("今天没有入侵次数了哦，明天再来吧,亲！");
					returnType.setStatus(0);
					super.printReturnType2Response(response, returnType);
					return null;
				}
			}else{
				//自由切磋
				Role aRole = super.getCurrentRole(request);
				Role dRole = roleManager.getRoleByName(dRoleName);
				Date date = new Date();
				if(aRole.getFreeFightTime()==null){
					aRole.setFreeFightTime(date);
					aRole.setSendEnergyTimes(0);
					roleManager.updateRoleFreeFightTime(aRole.getRoleId(), date);
					roleManager.updateRoleSendEnergyTimes(aRole.getRoleId(), 0);
				}
				if(dRole.getFreeFightTime()==null){
					dRole.setFreeFightTime(date);
					dRole.setSendEnergyTimes(0);
					roleManager.updateRoleFreeFightTime(dRole.getRoleId(), date);
					roleManager.updateRoleSendEnergyTimes(dRole.getRoleId(), 0);
				}
				if(aRole.getFreeFightTime().before(date)&&aRole.getFreeFightTime().getDate()!=date.getDate()){
					aRole.setSendEnergyTimes(0);
					roleManager.updateRoleSendEnergyTimes(aRole.getRoleId(), 0);
				}
				if(dRole.getFreeFightTime().before(date)&&dRole.getFreeFightTime().getDate()!=date.getDate()){
					dRole.setSendEnergyTimes(0);
					roleManager.updateRoleSendEnergyTimes(dRole.getRoleId(), 0);
				}
				int vip = aRole.getVip();
				MessageManager messageManager = (MessageManager)HttpSpringUtils.getBean("messageManager");
				if(aRole.getSendEnergyTimes()<vipEnergy[vip]){
					if(aRole.getEnergy()<aRole.getEnergyMax()){
						roleManager.addEnergy(aRole.getRoleName(), 1);
						ChannelBuffer buffer = new DynamicChannelBuffer(200);
						buffer.writeInt(MessageType.SYSTEM.getCode());
						buffer.writeInt(MessageType.MATCHSTART.getCode());
						MessageUtil.writeString(buffer, "您刚才在切磋中赚到了一点体力，总体力收益次数还剩"+(vipEnergy[vip]-aRole.getSendEnergyTimes()-1)+"次", "UTF-8");
						messageManager.sendMessageToRole(aRole.getRoleName(), buffer);
					}
				}
				vip = dRole.getVip();
				if(dRole.getSendEnergyTimes()<vipEnergy[vip]){
					if(dRole.getEnergy()<dRole.getEnergyMax()){
						roleManager.addEnergy(dRole.getRoleName(), 1);
						ChannelBuffer buffer = new DynamicChannelBuffer(200);
						buffer.writeInt(MessageType.SYSTEM.getCode());
						buffer.writeInt(MessageType.MATCHSTART.getCode());
						MessageUtil.writeString(buffer, "您刚才在切磋中赚到了一点体力，总体力收益次数还剩"+(vipEnergy[vip]-dRole.getSendEnergyTimes()-1)+"次", "UTF-8");
						messageManager.sendMessageToRole(dRole.getRoleName(), buffer);
					}
				}
				aRole.setSendEnergyTimes(aRole.getSendEnergyTimes()+1);
				dRole.setSendEnergyTimes(dRole.getSendEnergyTimes()+1);
				aRole.setFreeFightTime(date);
				dRole.setFreeFightTime(date);

				roleManager.updateRoleFreeFightTime(aRole.getRoleId(), date);
				roleManager.updateRoleSendEnergyTimes(aRole.getRoleId(), aRole.getSendEnergyTimes());
				roleManager.updateRoleFreeFightTime(dRole.getRoleId(), date);
				roleManager.updateRoleSendEnergyTimes(dRole.getRoleId(), dRole.getSendEnergyTimes());
			}
			Role role = super.getCurrentRole(request);
			if(null==role){
				returnType.setMsg("您还没登录哦,亲！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(null==dRoleName){
				returnType.setMsg("您还没选择对手,亲！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			
			Role dRole = roleManager.getRoleByName(dRoleName);
			//设置角色状态为忙碌
			role.setStatus(1);
			roleManager.updateRoleStatus(role.getRoleName(), 1);
			role = roleManager.getRoleByName(role.getRoleName());
			if(type==1||type==0){
				dRole.setStatus(1);
				dRole = roleManager.getRoleByName(dRole.getRoleName());
			}


			SkillManager skillManager = (SkillManager)HttpSpringUtils.getBean("skillManager");
			CardManager cardManager = (CardManager)HttpSpringUtils.getBean("cardManager");

			boolean isHand = false;
			if(strIsHand.equals("1")){
				isHand = true;
			}
			//构造攻击玩家
			FightCard fightCard;
			Card card;
			FighterManager fighterManager = (FighterManager)HttpSpringUtils.getBean("fighterManager");
			Player attackPlayer = fighterManager.initPlayer(role,"atk");
			attackPlayer.setHand(isHand);
			List<FightCard> aHandsCards = ListUtil.copyTo(attackPlayer.getCards(), FightCard.class);
			Collections.shuffle(aHandsCards);
			//构造对手玩家
			Player defendPlayer = fighterManager.initPlayer(dRole,"def");
			defendPlayer.setName("def");
			if(dRole.getStatus()!=0){
				defendPlayer.setHand(false);
			}else{
				defendPlayer.setHand(true);
			}
			if(type==3||type==2){
				defendPlayer.setHand(false);
			}
			List<FightCard> dHandsCards = ListUtil.copyTo(defendPlayer.getCards(), FightCard.class);
			Collections.shuffle(dHandsCards);

			//构造战斗模型
			Battle battle = new Battle();
			List<Round> rounds = new ArrayList<Round>();
			Round round;
			Opp opp;
			int i=1;
			boolean isAttack = true;
			List<Opp> opps;
			boolean flag = false;
			List<FightCard> aPreCards = new ArrayList<FightCard>();
			List<FightCard> dPreCards = new ArrayList<FightCard>();
			Map<String, FightCard> dFightCards = new HashMap<String,FightCard>();
			Map<String, FightCard> aFightCards = new HashMap<String,FightCard>();
			int a=aHandsCards.size();
			int b=dHandsCards.size();
			//存储新上场卡牌
			List<FightCard> newFightCard = new ArrayList<FightCard>();
			attackPlayer.setHandsCards(aHandsCards);
			attackPlayer.setPreCards(aPreCards);
			attackPlayer.setFightCards(aFightCards);
			defendPlayer.setFightCards(dFightCards);
			defendPlayer.setHandsCards(dHandsCards);
			defendPlayer.setPreCards(dPreCards);
			attackPlayer.setTumbCards(new ArrayList<FightCard>());
			defendPlayer.setTumbCards(new ArrayList<FightCard>());
			while(i>0){
				a=aHandsCards.size();
				b=dHandsCards.size();
				round = new Round();
				opps = new ArrayList<Opp>();
				isAttack = i%2==0?false:true;
				round.setRound(i);
				if(i>50){
					if(isAttack){
						attackPlayer.setHP(attackPlayer.getHP()-50);
						opp = new Opp();
						opp.setOpp(1022);
						opp.setTarget(attackPlayer.getName());
						opp.setValue(-50);
						opps.add(opp);
					}else{
						defendPlayer.setHP(defendPlayer.getHP()-50);
						opp = new Opp();
						opp.setOpp(1022);
						opp.setTarget(defendPlayer.getName());
						opp.setValue(-50);
						opps.add(opp);
					}
				}
				//更新CD时间的操作
				for(int j=0;j<attackPlayer.getPreCards().size();j++){
					attackPlayer.getPreCards().get(j).setWait(attackPlayer.getPreCards().get(j).getWait()-1);
				}
				for(int j=defendPlayer.getPreCards().size()-1;j>-1;j--){
					defendPlayer.getPreCards().get(j).setWait(defendPlayer.getPreCards().get(j).getWait()-1);
				}
				opp = new Opp();
				opp.setOpp(1021);
				opp.setValue(-1);
				opps.add(opp);
				if(isAttack){
					if(a>0){
						attackPlayer.getPreCards().add(attackPlayer.getHandsCards().get(a-1));
						//随机手牌
						opp = new Opp();
						opp.setOpp(1001);
						opp.setUUID(attackPlayer.getHandsCards().get(a-1).getUUID());
						opp.setValue(0);
						opps.add(opp);
						attackPlayer.getHandsCards().remove(a-1);
						a--;
					}
				}else{
					if(b>0){
						defendPlayer.getPreCards().add(defendPlayer.getHandsCards().get(b-1));
						//随机手牌
						opp = new Opp();
						opp.setOpp(1001);
						opp.setUUID(defendPlayer.getHandsCards().get(b-1).getUUID());
						opp.setValue(0);
						opps.add(opp);
						defendPlayer.getHandsCards().remove(b-1);
						b--;
					}
				}
				//卡牌上场
				//新上场卡牌区置空
				newFightCard.clear();
				if(isAttack){
					for(FightCard fi:attackPlayer.getPreCards()){
						if(fi.getWait()<=0){
							if(attackPlayer.isHand()){
								flag = true;
							}else{
								opp = new Opp();
								opp.setOpp(1002);
								opp.setUUID(fi.getUUID());
								opp.setValue(0);
								opps.add(opp);
								newFightCard.add(fi);
								attackPlayer.getFightCards().put(String.valueOf(attackPlayer.getFightCards().size()), fi);
//								aPreCards.remove(fi);
							}
	  
						}
					}
					for(int j=0;j<newFightCard.size();j++){
						if(attackPlayer.getPreCards().contains(newFightCard.get(j))){
							attackPlayer.getPreCards().remove(newFightCard.get(j));
						}
					}
				}else{
					for(FightCard fi:defendPlayer.getPreCards()){
						if(fi.getWait()<=0){
							if(defendPlayer.isHand()){
								flag = true;
							}else{
								opp = new Opp();
								opp.setOpp(1002);
								opp.setUUID(fi.getUUID());
								opp.setValue(0);
								opps.add(opp);
								newFightCard.add(fi);
								defendPlayer.getFightCards().put(String.valueOf(defendPlayer.getFightCards().size()), fi);
	//							dPreCards.remove(fi);
							}
						}
					} 
					for(int j=0;j<newFightCard.size();j++){
						if(defendPlayer.getPreCards().contains(newFightCard.get(j))){
							defendPlayer.getPreCards().remove(newFightCard.get(j));
						}
					}
				}
				
				if(flag){
					round.setIsAttack(isAttack);
					round.setOpps(opps);
					rounds.add(round);
					break;
				}
				//星辰是否还有发动次数
				if(isAttack){
					for(int j=0;j<attackPlayer.getRunes().size();j++){
						FightRune fightRune = attackPlayer.getRunes().get(j);
						if(fightRune.getRemainTimes()>0){
							List<Opp> oppsRunes = skillManager.getRuneOpps(fightRune, attackPlayer, defendPlayer,i);
							if(!CollectionUtils.isEmpty(oppsRunes)){
								opps.addAll(oppsRunes);
								opp = new Opp();
								opp.setOpp(50);
								opp.setUUID(fightRune.getuUID());
								opp.setValue(fightRune.getRemainTimes());
								opps.add(opp);
								fightRune.setRemainTimes(fightRune.getRemainTimes()-1);
							}else{
								opp = new Opp();
								opp.setOpp(50);
								opp.setUUID(fightRune.getuUID());
								opp.setValue(-1);
								opps.add(opp);
							}
							if(fightRune.getRemainTimes()<=0){
								opp = new Opp();
								opp.setOpp(1050);
								opp.setUUID(fightRune.getuUID());
								opp.setTarget(fightRune.getuUID());
								opp.setValue(0);
								opps.add(opp);
							}
						}else{
							opp = new Opp();
							opp.setOpp(1050);
							opp.setUUID(fightRune.getuUID());
							opp.setTarget(fightRune.getuUID());
							opp.setValue(0);
							opps.add(opp);
						}
					}
				}else{
					for(int j=0;j<defendPlayer.getRunes().size();j++){
						FightRune fightRune = defendPlayer.getRunes().get(j);
						if(fightRune.getRemainTimes()>0){
							List<Opp> oppsRunes = skillManager.getRuneOpps(fightRune, defendPlayer, attackPlayer,i);
							if(!CollectionUtils.isEmpty(oppsRunes)){
								opps.addAll(oppsRunes);
								opp = new Opp();
								opp.setOpp(50);
								opp.setUUID(fightRune.getuUID());
								opp.setValue(fightRune.getRemainTimes());
								opps.add(opp);
								fightRune.setRemainTimes(fightRune.getRemainTimes()-1);
							}else{
								opp = new Opp();
								opp.setOpp(50);
								opp.setUUID(fightRune.getuUID());
								opp.setValue(-1);
								opps.add(opp);
							}
							if(fightRune.getRemainTimes()<=0){
								opp = new Opp();
								opp.setOpp(1050);
								opp.setUUID(fightRune.getuUID());
								opp.setTarget(fightRune.getuUID());
								opp.setValue(0);
								opps.add(opp);
							}

						}else{
							opp = new Opp();
							opp.setOpp(1050);
							opp.setUUID(fightRune.getuUID());
							opp.setTarget(fightRune.getuUID());
							opp.setValue(0);
							opps.add(opp);
						}
					}
				}
				//卡牌上场时查看场上卡牌是否有buff技能
				if(newFightCard.size()>0){
					if(isAttack){
						opps.addAll(skillManager.getSkillModelInCardToPlay(attackPlayer, defendPlayer, newFightCard));
					}else{
						opps.addAll(skillManager.getSkillModelInCardToPlay(defendPlayer, attackPlayer, newFightCard));
					}
				}
				//新上场卡牌是否有上场技能
				if(newFightCard.size()>0){
					for(int j=0;j<newFightCard.size();j++){
						fightCard = newFightCard.get(j);
						card = cardManager.getCardById(new Long(fightCard.getCardId()));
						
						if(card.getSkill()>0){
							Skill skill = skillManager.getSkillById(card.getSkill());
							List<Opp> opps1 = null;
							if(isAttack){
								opps1 = skillManager.getSkillModelByType(skill, attackPlayer, defendPlayer, fightCard);
							}else{
								opps1 = skillManager.getSkillModelByType(skill, defendPlayer, attackPlayer, fightCard);
							}
							if(opps1!=null){
								opps.addAll(opps1);
							}
						}
						if(fightCard.getLevel()>=5){
							Skill skill = skillManager.getSkillById(card.getLockSkill1());
							List<Opp> opps1 = null;
							if(isAttack){
								opps1 = skillManager.getSkillModelByType(skill, attackPlayer, defendPlayer, fightCard);
							}else{
								opps1 = skillManager.getSkillModelByType(skill, defendPlayer, attackPlayer, fightCard);
							}
							if(opps1!=null){
								opps.addAll(opps1);
							}
						}
						if(fightCard.getLevel()>=10){
							Skill skill = skillManager.getSkillById(card.getLockSkill2());
							List<Opp> opps1 = null;
							if(isAttack){
								opps1 = skillManager.getSkillModelByType(skill, attackPlayer, defendPlayer, fightCard);
							}else{
								opps1 = skillManager.getSkillModelByType(skill, defendPlayer, attackPlayer, fightCard);
							}
							if(opps1!=null){
								opps.addAll(opps1);
							}
						}

					}
					attackPlayer.setFightCards(this.resetFightCard(attackPlayer.getFightCards()));
					defendPlayer.setFightCards(this.resetFightCard(defendPlayer.getFightCards()));
					//更新卡牌区域和战斗区域
					opp = new Opp();
					opp.setOpp(1060);
					opp.setValue(3);
					opps.add(opp);
					opp = new Opp();
					opp.setOpp(1060);
					opp.setValue(3);
					opps.add(opp);
					if(isWin(attackPlayer, defendPlayer)){
						break;
					}
				}

				if(isAttack){
					for(int j=0;j<=this.getLastIndexFromFightregion(attackPlayer.getFightCards());j++){
						List<Skill> skills = new ArrayList<Skill>();
						fightCard = attackPlayer.getFightCards().get(String.valueOf(j));
						if(fightCard==null){
							continue;
						}
						card = cardManager.getCardById(new Long(fightCard.getCardId()));
						
						if(card.getSkill()>0){
							Skill skill = skillManager.getSkillById(card.getSkill());
							skills.add(skill);
						}
						if(fightCard.getLevel()>=5){
							Skill skill = skillManager.getSkillById(card.getLockSkill1());							skills.add(skill);
						}
						if(fightCard.getLevel()>=10){
							Skill skill = skillManager.getSkillById(card.getLockSkill2());
							skills.add(skill);
						}
						if(fightCard.getSkillNew()!=null){
							skills.addAll(fightCard.getSkillNew());
						}
						opps.addAll(skillManager.getActiveSkillModelByType(skills, attackPlayer, defendPlayer, fightCard,fightCard.getSkillBuff(),this.isNewCard(newFightCard, fightCard)));
						fightCard.getSkillNew().clear();
						if(isWin(attackPlayer, defendPlayer)){
							break;
						}
					}
				}else{
					for(int j=0;j<=this.getLastIndexFromFightregion(defendPlayer.getFightCards());j++){
						List<Skill> skills = new ArrayList<Skill>();
						fightCard = defendPlayer.getFightCards().get(String.valueOf(j)); 
						if(fightCard==null){
							continue;
						}
						card = cardManager.getCardById(new Long(fightCard.getCardId()));
						
						if(card.getSkill()>0){
							Skill skill = skillManager.getSkillById(card.getSkill());
							skills.add(skill);
						}
						if(fightCard.getLevel()>=5){
							Skill skill = skillManager.getSkillById(card.getLockSkill1());
							skills.add(skill);
						}
						if(fightCard.getLevel()>=10){
							Skill skill = skillManager.getSkillById(card.getLockSkill2());
							skills.add(skill);
						}
						if(fightCard.getSkillNew()!=null){
							skills.addAll(fightCard.getSkillNew());
						}
						opps.addAll(skillManager.getActiveSkillModelByType(skills, defendPlayer, attackPlayer, fightCard,fightCard.getSkillBuff(),this.isNewCard(newFightCard, fightCard)));
						fightCard.getSkillNew().clear();
						if(isWin(attackPlayer, defendPlayer)){
							break;
						}
					}
				}
				attackPlayer.setFightCards(this.resetFightCard(attackPlayer.getFightCards()));
				defendPlayer.setFightCards(this.resetFightCard(defendPlayer.getFightCards()));
				//更新卡牌区域和战斗区域
				opp = new Opp();
				opp.setOpp(1060);
				opp.setValue(3);
				opps.add(opp);
				opp = new Opp();
				opp.setOpp(1060);
				opp.setValue(3);
				opps.add(opp);
				round.setOpps(opps);
				round.setIsAttack(isAttack);
				rounds.add(round);
				i++;
				if(flag){
					break;
				}
				if(isWin(attackPlayer, defendPlayer)){
					break;
				}
			}
			HeroLevelManager heroLevelManager = (HeroLevelManager)HttpSpringUtils.getBean("heroLevelManager");
			ExtData extData = new ExtData();
			extData.UserLevel = role.getLevel();
			HeroLevel roleLevel = heroLevelManager.getHeroLevelByLevel(role.getLevel());
			extData.Exp = roleLevel.getExp();
			if(roleLevel.getLevel()>2){
				extData.setPrevExp(heroLevelManager.getHeroLevelByLevel(role.getLevel()-1).getExp());
			}else{
				extData.setPrevExp(0);
			}
			if(roleLevel.getLevel()>=100){
				extData.setNextExp(extData.getExp());
			}else{
				extData.setNextExp(heroLevelManager.getHeroLevelByLevel(role.getLevel()+1).getExp());
			}
//			attackPlayer.setHandsCards(aHandsCards);
//			attackPlayer.setPreCards(aPreCards);
//			attackPlayer.setFightCards(aFightCards);
//			defendPlayer.setFightCards(dFightCards);
//			defendPlayer.setHandsCards(dHandsCards);
//			defendPlayer.setPreCards(dPreCards);

			battle.setRounds(rounds);
			FightManager fightManager = (FightManager)HttpSpringUtils.getBean("fightManager");
			FightResult result = new FightResult();
			result.setExtData(extData);
			result.setAttackPlayer(attackPlayer);
			result.setBattle(battle);
			result.setBattleId(UUID.randomUUID().toString());
			result.setDefendPlayer(defendPlayer);
			result.setType(type);
			fightManager.saveFight(result);
			returnType.setStatus(1);
			returnType.setValue(result);
			//初始化游戏创建房间
			if(result.getWin()==0){
				Room room = new Room();
				room.setaPlayer(attackPlayer.getNickName());
				room.setdPlayer(defendPlayer.getNickName());
				RoleStatusScanThread.freeFightRoom.put(result.getBattleId(), room);
			}

			
			if(isWin(attackPlayer, defendPlayer)&&result.getWin()==0&&RoleStatusScanThread.freeFightRoom.containsKey(result.getBattleId())){
				RoleStatusScanThread.freeFightRoom.remove(result.getBattleId());
				roleManager.updateRoleStatus(role.getRoleName(), 0);
				if(type==1||type==0){
					dRole.setStatus(1);
					dRole = roleManager.getRoleByName(dRole.getRoleName());
				}
				fighterManager.handlerFightResult(attackPlayer, defendPlayer, type,result);
				this.setFightResult(attackPlayer, defendPlayer, result, role);
			}else{
				result.setWin(0);
			}


			//初始生成战斗模型后通知挑战者开始战斗
			if(type==1||type==0){
				FreeFightManager freeFightManager = (FreeFightManager)HttpSpringUtils.getBean("freeFightManager");
				freeFightManager.addFightMessage(role.getRoleName(), dRole.getRoleName(), result.getBattleId(),1);
			}
			
			//			ChannelBuffer buffer = new DynamicChannelBuffer(200);
//			buffer.writeInt(1004);
//			MessageUtil.writeString(buffer, attackPlayer.getNickName(), "UTF-8");
//			MessageUtil.writeString(buffer, defendPlayer.getNickName(), "UTF-8");
//			buffer.writeInt(2);
//			MessageUtil.writeString(buffer,result.getBattleId(), "UTF-8");
//			buffer.writeInt(1);
//			MessageManager messageManager = (MessageManager)HttpSpringUtils.getBean("messageManager");
//			messageManager.sendMessageToRole(defendPlayer.getNickName(), buffer);
			super.printReturnType2Response(response, returnType);

		}catch (Exception e) {
			logger.error("fight error:",e);
		}
		return null;
	}
	private void setFightResult(Player aPlayer,Player dPlayer,FightResult fightResult,Role role){
		if(aPlayer.getHP()<=0||(aPlayer.getFightCards().size()==0&&aPlayer.getPreCards().size()==0&&aPlayer.getHandsCards().size()==0)){
			fightResult.setWin(2);

			return;
		}
		if(dPlayer.getHP()<=0||(dPlayer.getFightCards().size()==0&&dPlayer.getPreCards().size()==0&&dPlayer.getHandsCards().size()==0)){
			fightResult.setWin(1);
			
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
	private boolean isNewCard(List<FightCard> fightCards,FightCard fightCard){
		for(int i=0;i<fightCards.size();i++){
			if(fightCards.get(i).getUUID().equals(fightCard.getUUID())){
				return true;
			}
		}
		return false;
	}
	public Map<String,FightCard> resetFightCard(Map<String,FightCard> fightCards){
		int j=0;
		int s=0;
		Map<String, FightCard> fightCards1 = new HashMap<String, FightCard>();
		while(!fightCards.isEmpty()){
			if(fightCards.get(String.valueOf(j))!=null){
				fightCards1.put(String.valueOf(s), fightCards.get(String.valueOf(j)));
				fightCards.remove(String.valueOf(j));
				s++;
			}
			j++;
		}
		return fightCards1;
	}
	private int getLastIndexFromFightregion(Map<String,FightCard> fightCards){
		String j = "0";
		Iterator<String> ite = fightCards.keySet().iterator();
		String key;
		while(ite.hasNext()){
			key =ite.next();
			if(Integer.parseInt(key)>Integer.parseInt(j)){
				j=key;
			}
		}
		return Integer.parseInt(j)+1;
	}
}

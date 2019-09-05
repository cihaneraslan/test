package com.cgiser.moka.fight.rank.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.cgiser.keel.http.utils.HttpSpringUtils;
import com.cgiser.moka.action.AbstractAction;
import com.cgiser.moka.common.utils.ServletUtil;
import com.cgiser.moka.manager.AchievementManager;
import com.cgiser.moka.manager.CardManager;
import com.cgiser.moka.manager.EmailManager;
import com.cgiser.moka.manager.FightManager;
import com.cgiser.moka.manager.HeroLevelManager;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.manager.RuneManager;
import com.cgiser.moka.manager.SkillManager;
import com.cgiser.moka.manager.UserCardGroupManager;
import com.cgiser.moka.model.Battle;
import com.cgiser.moka.model.Card;
import com.cgiser.moka.model.ExtData;
import com.cgiser.moka.model.FightCard;
import com.cgiser.moka.model.FightResult;
import com.cgiser.moka.model.FightRune;
import com.cgiser.moka.model.HeroLevel;
import com.cgiser.moka.model.Opp;
import com.cgiser.moka.model.Player;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.model.Round;
import com.cgiser.moka.model.Rune;
import com.cgiser.moka.model.Skill;
import com.cgiser.moka.model.UserAchievement;
import com.cgiser.moka.model.UserCard;
import com.cgiser.moka.model.UserCardGroup;
import com.cgiser.moka.model.UserRune;
import com.cgiser.moka.result.ReturnType;

public class RankFightAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String dRoleName = ServletUtil.getDefaultValue(request, "role", null);
		String strIsHand = ServletUtil.getDefaultValue(request, "ishand", null);
		ReturnType<FightResult> returnType = new ReturnType<FightResult>();
		try{
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
			Thread.sleep(5);
			RoleManager roleManager = (RoleManager)HttpSpringUtils.getBean("roleManager");
			Role dRole = roleManager.getRoleByName(dRoleName);
			//设置角色状态为忙碌
			role.setStatus(1);
			roleManager.updateRoleStatus(role.getRoleName(), 1);
			//构造攻击玩家
			Player attackPlayer = new Player();
			attackPlayer.setName("atk");
			attackPlayer.setAvatar(role.getAvatar());
			attackPlayer.setHP(role.getHP());
			attackPlayer.setRemainHP(role.getHP());
			attackPlayer.setLevel(role.getLevel());
			attackPlayer.setNickName(role.getRoleName());
			attackPlayer.setRoleId(role.getRoleId());
			attackPlayer.setSex(role.getSex());
			attackPlayer.setHand(false);
			ArrayList<FightCard> aHandsCards = new ArrayList<FightCard>();
//			UserCardManager userCardManager = (UserCardManager)HttpSpringUtils.getBean("userCardManager");
			SkillManager skillManager = (SkillManager)HttpSpringUtils.getBean("skillManager");
			UserCardGroupManager userCardGroupManager = (UserCardGroupManager)HttpSpringUtils.getBean("userCardGroupManager");
			CardManager cardManager = (CardManager)HttpSpringUtils.getBean("cardManager");
			List<UserCardGroup> userCardGroups = userCardGroupManager.getUserCardGroup(role.getRoleId());
			UserCardGroup userCardGroup = userCardGroups.get(0);
			
//			List<UserCard> userCards = userCardManager.getUserCard(role.getRoleId());
			if(CollectionUtils.isEmpty(userCardGroup.getUserCardInfo())){
				returnType.setMsg("您还没配置卡牌哦,亲！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			boolean isHand = false;
			if(strIsHand.equals("1")){
				isHand = true;
			}
			UserCard userCard;
			FightCard fightCard;
			Card card;
			
			for(int i=0;i<userCardGroup.getUserCardInfo().size();i++){
				userCard = userCardGroup.getUserCardInfo().get(i);
				card = cardManager.getCardById(new Long(userCard.getCardId()));
				fightCard = new FightCard();
				fightCard.setAttack(card.getAttackArray().get(userCard.getLevel()));
				fightCard.setCardId(userCard.getCardId());
//				fightCard.setEvolution(0);
				fightCard.sethP(card.getHpArray().get(userCard.getLevel()));
				fightCard.setLevel(userCard.getLevel());
				fightCard.setSkillNew(null);
				fightCard.setUserCardId(new Long(userCard.getUserCardId()));
				fightCard.setUUID("atk_"+(i+1));
				fightCard.setWait(card.getWait());
				aHandsCards.add(fightCard);
				attackPlayer.getCards().add(fightCard.clone());
			}
			Collections.sort(aHandsCards, new Comparator<FightCard>() {
				private final int[] vs = {-1,0,1};
				private final Random rnd = new Random(System.currentTimeMillis());
				@Override
				public int compare(FightCard arg0, FightCard arg1) {
					return vs[rnd.nextInt(vs.length)];
				}			
			});
			if(userCardGroup.getUserRuneInfo()!=null){
				RuneManager runeManager = (RuneManager)HttpSpringUtils.getBean("runeManager");
				UserRune userRune;
				Rune rune;
				FightRune fightRune;
				Skill skill;
				for(int i=0;i<userCardGroup.getUserRuneInfo().size();i++){
					fightRune = new FightRune();
					userRune = userCardGroup.getUserRuneInfo().get(i);
					rune = runeManager.getRuneById(userRune.getRuneId());
					skill = skillManager.getSkillById(rune.getSkillIdByLevel(userRune.getLevel()));
					fightRune.setLevel(userRune.getLevel());
					fightRune.setRemainTimes(rune.getSkillTimes());
					fightRune.setRuneId(userRune.getRuneId());
					fightRune.setSkill(skill);
					fightRune.setUserRuneId(userRune.getUserRuneId());
					fightRune.setuUID("atkrune_"+(i+1));
					attackPlayer.getRunes().add(fightRune);
				}
			}
			//构造对手玩家
			Player defendPlayer = new Player();
			defendPlayer.setName("def");
			defendPlayer.setAvatar(dRole.getAvatar());
			defendPlayer.setHP(dRole.getHP());
			defendPlayer.setRemainHP(dRole.getHP());
			defendPlayer.setLevel(dRole.getLevel());
			defendPlayer.setNickName(dRole.getRoleName());
			defendPlayer.setRoleId(dRole.getRoleId());
			defendPlayer.setSex(dRole.getSex());
			defendPlayer.setHand(false);
			ArrayList<FightCard> dHandsCards = new ArrayList<FightCard>();
//			UserCardManager userCardManager = (UserCardManager)HttpSpringUtils.getBean("userCardManager");
			List<UserCardGroup> dUserCardGroups = userCardGroupManager.getUserCardGroup(dRole.getRoleId());
			UserCardGroup dUserCardGroup = dUserCardGroups.get(0);
			
//			List<UserCard> userCards = userCardManager.getUserCard(role.getRoleId());
			if(CollectionUtils.isEmpty(dUserCardGroup.getUserCardInfo())){
				returnType.setMsg("您还没配置卡牌哦,亲！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			UserCard dUserCard;
			FightCard dFightCard;
			Card dCard;
			
			for(int i=0;i<dUserCardGroup.getUserCardInfo().size();i++){
				dUserCard = dUserCardGroup.getUserCardInfo().get(i);
				dCard = cardManager.getCardById(new Long(dUserCard.getCardId()));
				dFightCard = new FightCard();
				dFightCard.setAttack(dCard.getAttackArray().get(dUserCard.getLevel()));
				dFightCard.setCardId(dUserCard.getCardId());
//				fightCard.setEvolution(0);
				dFightCard.sethP(dCard.getHpArray().get(dUserCard.getLevel()));
				dFightCard.setLevel(dUserCard.getLevel());
				dFightCard.setSkillNew(null);
				dFightCard.setUserCardId(new Long(dUserCard.getUserCardId()));
				dFightCard.setUUID("def_"+(i+1));
				dFightCard.setWait(dCard.getWait());
				dHandsCards.add(dFightCard);
				defendPlayer.getCards().add(dFightCard.clone());
			}
			Collections.sort(dHandsCards, new Comparator<FightCard>() {
				private final int[] vs = {-1,0,1};
				private final Random rnd = new Random(System.currentTimeMillis());
				@Override
				public int compare(FightCard arg0, FightCard arg1) {
					return vs[rnd.nextInt(vs.length)];
				}			
			});
			if(dUserCardGroup.getUserRuneInfo()!=null){
				RuneManager runeManager = (RuneManager)HttpSpringUtils.getBean("runeManager");
				UserRune userRune;
				Rune rune;
				FightRune fightRune;
				Skill skill;
				for(int i=0;i<dUserCardGroup.getUserRuneInfo().size();i++){
					fightRune = new FightRune();
					userRune = dUserCardGroup.getUserRuneInfo().get(i);
					rune = runeManager.getRuneById(userRune.getRuneId());
					skill = skillManager.getSkillById(rune.getSkillIdByLevel(userRune.getLevel()));
					fightRune.setLevel(userRune.getLevel());
					fightRune.setRemainTimes(rune.getSkillTimes());
					fightRune.setRuneId(userRune.getRuneId());
					fightRune.setSkill(skill);
					fightRune.setUserRuneId(userRune.getUserRuneId());
					fightRune.setuUID("atkrune_"+(i+1));
					defendPlayer.getRunes().add(fightRune);
				}
			}
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
				round = new Round();
				opps = new ArrayList<Opp>();
				isAttack = i%2==0?false:true;
				round.setRound(i);
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
					if(isWin(attackPlayer, defendPlayer)>0){
						break;
					}
				}
				//星辰是否还有发动次数
				if(isAttack){
					for(int j=0;j<attackPlayer.getRunes().size();j++){
						FightRune fightRune = attackPlayer.getRunes().get(j);
						if(fightRune.getRemainTimes()>0){
							opps.addAll(skillManager.getRuneOpps(fightRune, attackPlayer, defendPlayer,i));
							if(CollectionUtils.isEmpty(opps)){
								opp = new Opp();
								opp.setUUID(fightRune.getuUID());
								opp.setTarget(fightRune.getuUID());
								opp.setValue(0);
							}
						}else{
							opp = new Opp();
							opp.setUUID(fightRune.getuUID());
							opp.setTarget(fightRune.getuUID());
							opp.setValue(0);
						}
					}
				}else{
					for(int j=0;j<defendPlayer.getRunes().size();j++){
						FightRune fightRune = defendPlayer.getRunes().get(j);
						if(fightRune.getRemainTimes()>0){
							opps.addAll(skillManager.getRuneOpps(fightRune, defendPlayer, attackPlayer,i));
							if(CollectionUtils.isEmpty(opps)){
								opp = new Opp();
								opp.setUUID(fightRune.getuUID());
								opp.setTarget(fightRune.getuUID());
								opp.setValue(0);
							}
						}else{
							opp = new Opp();
							opp.setUUID(fightRune.getuUID());
							opp.setTarget(fightRune.getuUID());
							opp.setValue(0);
						}
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
						if(isWin(attackPlayer, defendPlayer)>0){
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
						if(isWin(attackPlayer, defendPlayer)>0){
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
				if(isWin(attackPlayer, defendPlayer)>0){
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
			AchievementManager achievementManager =(AchievementManager)HttpSpringUtils.getBean("achievementManager");
			int win = isWin(attackPlayer, defendPlayer);
			EmailManager emailManager = (EmailManager)HttpSpringUtils.getBean("emailManager");
			if(win==2){
				this.setFightResult(attackPlayer, defendPlayer, result, role);
				//排名战打败后，交换排名
				role = roleManager.getRoleByName(role.getRoleName());
				dRole = roleManager.getRoleByName(dRoleName);
				if(role.getRank()>dRole.getRank()){
					roleManager.swapRoleRank(role, dRole);
				}
				roleManager.updateRoleStatus(role.getRoleName(), 0);
				//通知被挑战者被人打败
				emailManager.sendEmail("排名战通知", "您在排名战中被"+role.getRoleName()+"打败，排名倒退到"+role.getRank()+"位，赶紧去打回来吧！", 0L, dRole.getRoleId(), 1);
				
				UserAchievement userAchievement = achievementManager.getUserAchievementById(role.getRoleId(), 38); 
				if(userAchievement==null){
					achievementManager.saveUserAchievement(38, role.getRoleId(),1);
				}
				result.setWin(1);
//				role.setRankStreakTimes(role.getRankStreakTimes()+1);
				
//				if(role.getRankStreakTimes()<=10){
//					userAchievement = achievementManager.getUserAchievementById(role.getRoleId(), 39); 
//					if(userAchievement==null){
//						achievementManager.saveUserAchievement(39, role.getRoleId(),role.getRankStreakTimes());
//					}
//					if(userAchievement!=null&&userAchievement.getFinishState()<10){
//						achievementManager.saveUserAchievement(39, role.getRoleId(),role.getRankStreakTimes());
//					}
//				}
//				if(role.getRank()<=500){
//					userAchievement = achievementManager.getUserAchievementById(role.getRoleId(), 40); 
//					if(userAchievement==null){
//						achievementManager.saveUserAchievement(40, role.getRoleId(),1);
//					}
//				}
			}else{
				roleManager.updateRoleStatus(role.getRoleName(), 0);
//				role.setRankStreakTimes(0);
				UserAchievement userAchievement = achievementManager.getUserAchievementById(role.getRoleId(), 39); 
				if(userAchievement!=null&&userAchievement.getFinishState()>0&&userAchievement.getFinishState()<10){
//					achievementManager.saveUserAchievement(39, role.getRoleId(),role.getRankStreakTimes());
				}
				emailManager.sendEmail("排名战通知", "您在排名战中被"+dRole.getRoleName()+"打败，排名倒退到"+dRole.getRank()+"位，赶紧去打回来吧！", 0L, role.getRoleId(), 1);
				result.setWin(2);
			}
			role = roleManager.getRoleByName(role.getRoleName());
//			role.setRankFightTime(new Date());
//			role.setRankHasTimes(role.getRankHasTimes()-1);
//			roleManager.updateRoleCache(role);
			fightManager.saveFight(result);
			returnType.setStatus(1);
			returnType.setValue(result);
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
	private int isWin(Player aPlayer,Player dPlayer){
		if(aPlayer.getHP()<=0||(aPlayer.getFightCards().size()==0&&aPlayer.getPreCards().size()==0&&aPlayer.getHandsCards().size()==0)){
			return 1;
		}
		if(dPlayer.getHP()<=0||(dPlayer.getFightCards().size()==0&&dPlayer.getPreCards().size()==0&&dPlayer.getHandsCards().size()==0)){
			return 2;
		}
		return 0;
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

package com.cgiser.moka.manager.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.util.CollectionUtils;

import com.cgiser.moka.dao.SkillDao;
import com.cgiser.moka.manager.CardManager;
import com.cgiser.moka.manager.RuneManager;
import com.cgiser.moka.manager.SkillManager;
import com.cgiser.moka.manager.SoulManager;
import com.cgiser.moka.model.Card;
import com.cgiser.moka.model.FightCard;
import com.cgiser.moka.model.FightRune;
import com.cgiser.moka.model.Opp;
import com.cgiser.moka.model.Player;
import com.cgiser.moka.model.Rune;
import com.cgiser.moka.model.Skill;
import com.cgiser.moka.model.Soul;
import com.cgiser.moka.model.UserSoul;

public class SkillManagerImpl implements SkillManager {
	private SkillDao skillDao;
	private CardManager cardManager;
	private RuneManager runeManager;
	private SoulManager soulManager;
	private SkillManager skillManager;
	@Override
	public List<Skill> getAllSkill() {
		return MapListToSkillList(skillDao.getSkills());
	}
	@Override
	public Skill getSkillById(int skillId) {
		// TODO Auto-generated method stub
		return MapToSkill(skillDao.getSkillById(skillId));
	}
	@Override
	public Skill getSkillByName(String skillName) {
		// TODO Auto-generated method stub
		return MapToSkill(skillDao.getSkillByName(skillName));
	}
	//卡牌上场时
	@Override
	public List<Opp> getSkillModelInCardToPlay(Player aPlayer, Player dPlayer,
			List<FightCard> newFightCards) {
		List<Opp> opps = new ArrayList<Opp>();
		Map<String, FightCard> aFightCards = aPlayer.getFightCards();
		Map<String, FightCard> dFightCards = dPlayer.getFightCards();
		FightCard fightCard;
		Opp opp;
		for(int c=0;c<=this.getLastIndexFromFightregion(aFightCards);c++){
			fightCard = aFightCards.get(String.valueOf(c));
			if(fightCard==null){
				continue;
			}
			List<Skill> skills = new ArrayList<Skill>();
			Card card = cardManager.getCardById(new Long(fightCard.getCardId()));
			if(card.getSkill()>0){
				Skill skill = this.getSkillById(card.getSkill());
				skills.add(skill);
			}
			if(fightCard.getLevel()>=5){
				Skill skill = this.getSkillById(card.getLockSkill1());
				skills.add(skill);
			}
			if(fightCard.getLevel()>=10){
				Skill skill = this.getSkillById(card.getLockSkill2());
				skills.add(skill);
			}
			if(fightCard.getSkillNew()!=null){
				skills.addAll(fightCard.getSkillNew());
			}
			for(int i=0;i<skills.size();i++){
				Skill skill = skills.get(i);
				//卡牌上场时发动
				if(skill.getLanchType()==6){
					Opp oppNew = new Opp();
					oppNew.setOpp(skill.getType());
					oppNew.setUUID(fightCard.getUUID());
					oppNew.setTarget("");
					opps.add(oppNew);
					FightCard fightCard2;
					int val = skill.getAffectValue();
					List<FightCard> oppFightCards = new ArrayList<FightCard>();
					if(newFightCards.contains(fightCard)){
						oppFightCards.addAll(aFightCards.values());
					}else{
						oppFightCards.addAll(newFightCards);
					}
					for(int j=0;j<oppFightCards.size();j++){
						fightCard2 =oppFightCards.get(j);
						if(fightCard2.getUUID()!=fightCard.getUUID()){
							Card card2 = cardManager.getCardById(new Long(fightCard2.getCardId()));
							if(skill.getAffectType()==24){
								if(skill.getAffectValue2()==card2.getRace()){
									opp = new Opp();
									opp.setOpp(1020);
									opp.setUUID(fightCard.getUUID());
									opp.setTarget(fightCard2.getUUID());
									opp.setValue(val);
									opps.add(opp);
									fightCard2.setAttack(fightCard2.getAttack()+val);
									oppNew.setTarget(oppNew.getTarget().concat(fightCard2.getUUID()).concat(","));
								}
								if(skill.getAffectValue2()==5){
									opp = new Opp();
									opp.setOpp(1020);
									opp.setUUID(fightCard.getUUID());
									opp.setTarget(fightCard2.getUUID());
									opp.setValue(val);
									opps.add(opp);
									fightCard2.setAttack(fightCard2.getAttack()+val);
									oppNew.setTarget(oppNew.getTarget().concat(fightCard2.getUUID()).concat(","));
								}
							}
							if(skill.getAffectType()==25){
								if(skill.getAffectValue2()==card2.getRace()){
									opp = new Opp();
									opp.setOpp(1040);
									opp.setUUID(fightCard.getUUID());
									opp.setTarget(fightCard2.getUUID());
									opp.setValue(val);
									opps.add(opp);
									fightCard2.sethP(fightCard2.gethP()+val);
									oppNew.setTarget(oppNew.getTarget().concat(fightCard2.getUUID()).concat(","));
								}
								if(skill.getAffectValue2()==5){
									opp = new Opp();
									opp.setOpp(1040);
									opp.setUUID(fightCard.getUUID());
									opp.setTarget(fightCard2.getUUID());
									opp.setValue(val);
									opps.add(opp);
									fightCard2.sethP(fightCard2.gethP()+val);
									oppNew.setTarget(oppNew.getTarget().concat(fightCard2.getUUID()).concat(","));
								}
							}
						}
					}
				}
			}
		}
		for(int j=0;j<newFightCards.size();j++){
			fightCard = newFightCards.get(j);
			List<Skill> skills = new ArrayList<Skill>();
			Card card = cardManager.getCardById(new Long(fightCard.getCardId()));
			if(card.getSkill()>0){
				Skill skill = this.getSkillById(card.getSkill());
				skills.add(skill);
			}
			if(fightCard.getLevel()>=5){
				Skill skill = this.getSkillById(card.getLockSkill1());
				skills.add(skill);
			}
			if(fightCard.getLevel()>=10){
				Skill skill = this.getSkillById(card.getLockSkill2());
				skills.add(skill);
			}
			if(fightCard.getSkillNew()!=null){
				skills.addAll(fightCard.getSkillNew());
			}
			for(int i=0;i<skills.size();i++){
				Skill skill = skills.get(i);
				if(skill.getLanchType()==1){
					if(skill.getAffectType()==30){
						fightCard.getSkillBuff().add(skill);
					}
				}
				if(skill.getLanchType()==4){
					if(skill.getAffectType()==48){
						fightCard.getSkillBuff().add(skill);
					}
				}
			}

		}

		
		return opps;
	}
	//卡牌上场后
	@Override
	public List<Opp> getSkillModelByType(Skill skill,Player aPlayer,Player dPlayer,FightCard fightCard) {
		List<Opp> opps = new ArrayList<Opp>();
//		List<FightCard> aHandsCards = aPlayer.getHandsCards();
		List<FightCard> dHandsCards = dPlayer.getHandsCards();
		Map<String, FightCard> dFightCards = dPlayer.getFightCards();
		Map<String, FightCard> aFightCards = aPlayer.getFightCards();
		//玩家准备区
//		List<FightCard> aPreCards = aPlayer.getPreCards();
		List<FightCard> dPreCards = dPlayer.getPreCards();
		//玩家墓地
		List<FightCard> aTumbCards = aPlayer.getTumbCards();
		List<FightCard> dTumbCards = dPlayer.getTumbCards();
		//随机函数
		Random rnd = new Random();
		Opp opp;
		 
		if(skill.getLanchType()==10){
			//出场时，降低我方英雄生命值。
			if(skill.getAffectType()==34){
				opp = new Opp();
				opp.setOpp(skill.getType());
				opp.setUUID(fightCard.getUUID());
				opp.setTarget(aPlayer.getName());
				opp.setValue(-skill.getAffectValue());
				opps.add(opp);
				opps.addAll(this.delPlayerHp(fightCard, skill.getAffectValue(), aFightCards,aPlayer));
			}
			//上场时如果己方场上有卡牌，则随机选择1张下场，使自己的攻击力和生命值提高30%。
			if(skill.getAffectType()==56){
				int index = this.getIndex(aFightCards, fightCard);
				if(aFightCards.size()>1&&index>0){
					int r = -1;
					//以后待测试是否会出现死循环，用户上两张吃牌的卡牌
//					while(aFightCards.get(String.valueOf(r))==null){
//						r = rnd.nextInt(index);
//					}
					for(int f=0;f<index;f++){
						if(aFightCards.get(String.valueOf(f))!=null){
							r = f;
						}
					}
					if(r>=0){
						//使卡牌进入墓地操作
						opp = new Opp();
						opp.setOpp(skill.getType());
						opp.setUUID(fightCard.getUUID());
						opp.setTarget(aFightCards.get(String.valueOf(r)).getUUID());
						opp.setValue(0);
						opps.add(opp);
						opps.addAll(this.cardDieToTumb(dPlayer, aPlayer, dFightCards.get(String.valueOf(r)), dFightCards, aFightCards.get(String.valueOf(r)), aFightCards));
						opp = new Opp();
						opp.setOpp(1003);
						opp.setUUID(aFightCards.get(String.valueOf(r)).getUUID());
						opp.setTarget(aFightCards.get(String.valueOf(r)).getUUID());
						opp.setValue(0);
						opps.add(opp);
						opp = new Opp();
						//卡牌加攻击操作
						opp.setOpp(1020);
						opp.setUUID(fightCard.getUUID());
						opp.setTarget(fightCard.getUUID());
						//攻击加成[x]/%
						opp.setValue(fightCard.getAttack()*skill.getAffectValue()/100);
						opps.add(opp);
						opp = new Opp();
						//卡牌加血操作
						opp.setOpp(1040);
						opp.setUUID(fightCard.getUUID());
						opp.setTarget(fightCard.getUUID());
						//血量加成[x]/%
						opp.setValue(fightCard.gethP()*skill.getAffectValue()/100);
						opps.add(opp);
						//移除卡牌到墓地
						aTumbCards.add(aFightCards.get(String.valueOf(r)));
						aFightCards.remove(String.valueOf(r));
						fightCard.setAttack(fightCard.getAttack()+(fightCard.getAttack()*skill.getAffectValue()/100));
						fightCard.sethP(fightCard.gethP()+(fightCard.gethP()*skill.getAffectValue()/100));
						opps.addAll(this.cardDieToTumbAfter(dPlayer, aPlayer, dFightCards.get(String.valueOf(r)), dFightCards, aFightCards.get(String.valueOf(r)), aFightCards));
					}
					
				}
			}
			if(skill.getAffectType()==59){
				if(skill.getType()==96){
					Skill skillnew = this.getSkillById(skill.getAffectValue());
					if(skillnew.getLanchType()==4&&skillnew.getAffectType()==14){
						//卡牌加成播放动画
						Opp oppNew = new Opp();
						oppNew.setOpp(skillnew.getType());
						oppNew.setUUID(fightCard.getUUID());
						oppNew.setTarget("");
						opps.add(oppNew);
						int val = skillnew.getAffectValue()+rnd.nextInt(skillnew.getAffectValue());
						for(int s=0;s<dFightCards.size();s++){
							if(dFightCards.get(String.valueOf(s))==null){
								continue;
							}
							oppNew.setTarget(oppNew.getTarget().concat(dFightCards.get(String.valueOf(s)).getUUID().concat(",")));
							if(!hasImmunitySkill(dFightCards.get(String.valueOf(s)))){
								opps.addAll(this.magicAttackCards(aPlayer,dPlayer,fightCard, dFightCards.get(String.valueOf(s)), skillnew,val));
							}else{
								Skill skillImmunity = this.getSkillById(689);
								opp = new Opp();
								opp.setOpp(skillImmunity.getType());
								opp.setUUID(dFightCards.get(String.valueOf(s)).getUUID());
								opp.setTarget(dFightCards.get(String.valueOf(s)).getUUID());
								opps.add(opp);
							}
							
							
						}
						
					}
				}
				if(skill.getType()==97||skill.getType()==98||skill.getType()==99||skill.getType()==100||skill.getType()==105){
					Skill skillnew = this.getSkillById(skill.getAffectValue());
					if(skillnew.getLanchType()==4){
						//卡牌加成播放动画
						Opp oppNew = new Opp();
						oppNew.setOpp(skillnew.getType());
						oppNew.setUUID(fightCard.getUUID());
						opps.add(oppNew);
						oppNew.setTarget("");
						int val = skillnew.getAffectValue();
						for(int s=0;s<dFightCards.size();s++){
							if(dFightCards.get(String.valueOf(s))==null){
								continue;
							}
							if(!hasImmunitySkill(dFightCards.get(String.valueOf(s)))){
								if(skill.getType()==97){
									if(rnd.nextInt(100)<skillnew.getAffectValue2()){
										opp = new Opp();
										opp.setOpp(1102);
										opp.setUUID(fightCard.getUUID());
										opp.setTarget(dFightCards.get(String.valueOf(s)).getUUID());
										opp.setValue(1);
										opps.add(opp);
										dFightCards.get(String.valueOf(s)).getSkillBuff().add(skillnew);
									}
								}else if(skill.getType()==98){
									if(rnd.nextInt(100)<skillnew.getAffectValue2()){
										opp = new Opp();
										opp.setOpp(1103);
										opp.setUUID(fightCard.getUUID());
										opp.setTarget(dFightCards.get(String.valueOf(s)).getUUID());
										opp.setValue(1);
										opps.add(opp);
										dFightCards.get(String.valueOf(s)).getSkillBuff().add(skillnew);
									}
								}else if(skill.getType()==99){
									dFightCards.get(String.valueOf(s)).getSkillNew().add(skillnew);
								}else if(skill.getType()==100){
									int valskill = val;
									if(dFightCards.get(String.valueOf(s)).getAttack()<valskill){
										valskill = dFightCards.get(String.valueOf(s)).getAttack();
									}
									dFightCards.get(String.valueOf(s)).setAttack(dFightCards.get(String.valueOf(s)).getAttack()-valskill);
									opp = new Opp();
									opp.setOpp(1020);
									opp.setUUID(fightCard.getUUID());
									opp.setTarget(dFightCards.get(String.valueOf(s)).getUUID());
									//攻击减少val
									opp.setValue(-valskill);
									opps.add(opp);  
								}
								if(skill.getType()==105){
									int valskill = val;
									if(dFightCards.get(String.valueOf(s)).getAttack()<valskill){
										valskill = dFightCards.get(String.valueOf(s)).getAttack();
									}
									dFightCards.get(String.valueOf(s)).setAttack(dFightCards.get(String.valueOf(s)).getAttack()-valskill);
									opp = new Opp();
									opp.setOpp(1020);
									opp.setUUID(fightCard.getUUID());
									opp.setTarget(dFightCards.get(String.valueOf(s)).getUUID());
									//攻击减少val
									opp.setValue(-valskill);
									opps.add(opp);  
								}else{
									opps.addAll(this.magicAttackCards(aPlayer,dPlayer,fightCard, dFightCards.get(String.valueOf(s)), skillnew,val));
	//								dFightCards.get(String.valueOf(s)).sethP(dFightCards.get(String.valueOf(s)).gethP()-val);
	//								opp = new Opp();
	//								opp.setOpp(1040);
	//								opp.setUUID(fightCard.getUUID());
	//								opp.setTarget(dFightCards.get(String.valueOf(s)).getUUID());
	//								//血量减少val
	//								opp.setValue(-val);
	//								opps.add(opp);                                 
								}       
							}else{
								Skill skillImmunity = this.getSkillById(689);
								opp = new Opp();
								opp.setOpp(skillImmunity.getType());
								opp.setUUID(dFightCards.get(String.valueOf(s)).getUUID());
								opp.setTarget(dFightCards.get(String.valueOf(s)).getUUID());
								opps.add(opp);
							}
							oppNew.setTarget(oppNew.getTarget().concat(dFightCards.get(String.valueOf(s)).getUUID().concat(",")));
						}
						
					}
				}
				if(skill.getType()==101){
					//妙手回春
					Skill skillnew = this.getSkillById(skill.getAffectValue());
					//获取生命值损失最多的卡牌索引
					int index = this.getMaxLoseHpCard(aPlayer.getCards(), aFightCards);
					if(index>-1){
						int hp = aFightCards.get(String.valueOf(index)).gethP()+skillnew.getAffectValue();
						aFightCards.get(String.valueOf(index)).sethP(hp);
						opp = new Opp();
						opp.setOpp(skillnew.getType());
						opp.setUUID(fightCard.getUUID());
						opp.setTarget(aFightCards.get(String.valueOf(index)).getUUID());
						opps.add(opp);
						opp.setOpp(1040);
						opp.setUUID(fightCard.getUUID());
						opp.setTarget(aFightCards.get(String.valueOf(index)).getUUID());
						//血量减少val
						opp.setValue(skillnew.getAffectValue());
						opps.add(opp);   
					}
            
				}
				if(skill.getType()==102){
					//普渡众生
					Skill skillnew = this.getSkillById(skill.getAffectValue());
					Opp oppNew = new Opp();
					oppNew.setOpp(skillnew.getType());
					oppNew.setUUID(fightCard.getUUID());
					oppNew.setTarget("");
					opps.add(oppNew);
					int hp = skillnew.getAffectValue();
					for(int s=0;s<aFightCards.size();s++){
						aFightCards.get(String.valueOf(s)).sethP(aFightCards.get(String.valueOf(s)).gethP()+hp);
						opp = new Opp();
						opp.setOpp(1040);
						opp.setUUID(fightCard.getUUID());
						opp.setTarget(aFightCards.get(String.valueOf(s)).getUUID());
						//血量减少val
						opp.setValue(hp);
						opps.add(opp);  
						oppNew.setTarget(oppNew.getTarget().concat(aFightCards.get(String.valueOf(s)).getUUID().concat(",")));
					}
					

				}
				if(skill.getType()==103){
					if(aPlayer.getHP()<aPlayer.getRemainHP()){
						Skill skillnew = this.getSkillById(skill.getAffectValue());
						int val = skillnew.getAffectValue();
						opp = new Opp();
						opp.setOpp(skillnew.getType());
						opp.setUUID(fightCard.getUUID());
						opp.setTarget(aPlayer.getName());
						opp.setValue(val);
						opps.add(opp);     
						opps.addAll(this.addPlayerHp(fightCard, val, aPlayer));
					}
				}
				if(skill.getType()==104){
					Skill skillnew = this.getSkillById(skill.getAffectValue());
					int val = skillnew.getAffectValue();
					opp = new Opp();
					opp.setOpp(skillnew.getType());
					opp.setUUID(fightCard.getUUID());
					opp.setTarget(dPlayer.getName());
					opps.add(opp);
					opps.addAll(this.delPlayerHp(fightCard, val,dFightCards, dPlayer));   
				}
				if(skill.getType()==106){
					//炎火焚身
					Skill skillnew = this.getSkillById(skill.getAffectValue());
					Opp oppNew = new Opp();
					oppNew.setOpp(skillnew.getType());
					oppNew.setUUID(fightCard.getUUID());
					oppNew.setTarget("");
					opps.add(oppNew);
					opp = new Opp();
					opp.setOpp(1107);
					opp.setUUID(fightCard.getUUID());
					opp.setTarget("");
					opp.setValue(1);
					opps.add(opp);
					for(int s=0;s<dFightCards.size();s++){
						oppNew.setTarget(oppNew.getTarget().concat(dFightCards.get(String.valueOf(s)).getUUID().concat(",")));
						if(!hasImmunitySkill(dFightCards.get(String.valueOf(s)))){
							//每回合都释放炎火焚身
							dFightCards.get(String.valueOf(s)).getSkillBuff().add(skillnew);
							opp.setTarget(opp.getTarget().concat(dFightCards.get(String.valueOf(s)).getUUID().concat(",")));
						}else{
							Skill skillImmunity = this.getSkillById(689);
							opp = new Opp();
							opp.setOpp(skillImmunity.getType());
							opp.setUUID(dFightCards.get(String.valueOf(s)).getUUID());
							opp.setTarget(dFightCards.get(String.valueOf(s)).getUUID());
							opps.add(opp);
						}
					}
					
					

				}
				if(skill.getType()==107){
					Skill skillnew = this.getSkillById(skill.getAffectValue());
					Opp oppNew = new Opp();
					oppNew.setOpp(skillnew.getType());
					oppNew.setUUID(fightCard.getUUID());
					oppNew.setTarget("");
					opps.add(oppNew);
					List<String> targets = this.getIndexRandom(dFightCards, skillnew.getAffectValue());
					FightCard fightCard2;
					for(int i=0;i<targets.size();i++){
						if(rnd.nextInt(100)<skillnew.getAffectValue2()){
							fightCard2 = this.getFightCardByUuidFromFightRegion(targets.get(i), dFightCards);
							oppNew.setTarget(oppNew.getTarget().concat(fightCard2.getUUID()).concat(","));
							if(!hasImmunitySkill(fightCard2)){
								opp = new Opp();
								opp.setOpp(1101);
								opp.setUUID(fightCard.getUUID());
								opp.setTarget(fightCard2.getUUID());
								opp.setValue(1);
								opps.add(opp);
								fightCard2.getSkillBuff().add(skillnew);
							}else{
								Skill skillImmunity = this.getSkillById(689);
								opp = new Opp();
								opp.setOpp(skillImmunity.getType());
								opp.setUUID(fightCard2.getUUID());
								opp.setTarget(fightCard2.getUUID());
								opps.add(opp);
							}
						}
					}
				}
				if(skill.getType()==108){
					Skill skillnew = this.getSkillById(skill.getAffectValue());
					opp = new Opp();
					opp.setOpp(skillnew.getType());
					opp.setUUID(fightCard.getUUID());
					opps.add(opp);
					int index = this.getIndex(aFightCards, fightCard);
					if(index>-1){
						if(dFightCards.get(String.valueOf(index))!=null){
							FightCard xFightCard = this.getFightCardByUuid(dFightCards.get(String.valueOf(index)).getUUID(), dPlayer.getCards());
							if(xFightCard!=null){
								if(!hasImmunitySkill(xFightCard)){
									dHandsCards.add(xFightCard);
									opp = new Opp();
									opp.setOpp(1007);
									opp.setUUID(dFightCards.get(String.valueOf(index)).getUUID());
									opps.add(opp);
									dFightCards.remove(String.valueOf(index));
								}else{
									Skill skillImmunity = this.getSkillById(689);
									opp = new Opp();
									opp.setOpp(skillImmunity.getType());
									opp.setUUID(xFightCard.getUUID());
									opp.setTarget(xFightCard.getUUID());
									opps.add(opp);
								}
							}
						

						}
					}
				}
				if(skill.getType()==109){
					if(!CollectionUtils.isEmpty(dFightCards)){
						Skill skillnew = this.getSkillById(skill.getAffectValue());
						opp = new Opp();
						opp.setOpp(skillnew.getType());
						opp.setUUID(fightCard.getUUID());
						opps.add(opp);
						int index = rnd.nextInt(dFightCards.size());
						if(index>-1){
							if(dFightCards.get(String.valueOf(index))!=null){
								opp.setTarget(dFightCards.get(String.valueOf(index)).getUUID());
								if(!hasImmunitySkill(dFightCards.get(String.valueOf(index)))&&!hasBuDongSkill(dFightCards.get(String.valueOf(index)))){
									opps.addAll(this.cardDieToTumb(aPlayer, dPlayer, aFightCards.get(String.valueOf(index)), aFightCards, dFightCards.get(String.valueOf(index)), dFightCards));
									opp = new Opp();
									opp.setOpp(1003);
									opp.setUUID(dFightCards.get(String.valueOf(index)).getUUID());
									opps.add(opp);
									dTumbCards.add(dFightCards.get(String.valueOf(index)));
									dFightCards.remove(String.valueOf(index));
									opps.addAll(this.cardDieToTumbAfter(aPlayer, dPlayer, aFightCards.get(String.valueOf(index)), aFightCards, dFightCards.get(String.valueOf(index)), dFightCards));
								}else{
									Skill skillImmunity = this.getSkillById(689);
									if(hasBuDongSkill(dFightCards.get(String.valueOf(index)))){
										skillImmunity = this.getSkillById(688);
									}
									opp = new Opp();
									opp.setOpp(skillImmunity.getType());
									opp.setUUID(dFightCards.get(String.valueOf(index)).getUUID());
									opp.setTarget(dFightCards.get(String.valueOf(index)).getUUID());
									opps.add(opp);
								}

							}
						}
					}
				}
				if(skill.getType()==110){
					if(!CollectionUtils.isEmpty(dPreCards)){
						Skill skillnew = this.getSkillById(skill.getAffectValue());
						opp = new Opp();
						opp.setOpp(skillnew.getType());
						opp.setUUID(fightCard.getUUID());
						opps.add(opp);
						int index = this.getMaxWaitCard(dPreCards);
						if(index>-1){
							if(dPreCards.get(index)!=null){
								opp.setTarget(dPreCards.get(index).getUUID());
								if(!hasImmunitySkill(dPreCards.get(index))){
									FightCard fightCard2 = dPreCards.get(index);
									opps.addAll(this.cardDieToTumb(aPlayer, dPlayer, fightCard, aFightCards, dPreCards.get(index), dFightCards));
									opp = new Opp();
									opp.setOpp(1003);
									opp.setUUID(dPreCards.get(index).getUUID());
									opps.add(opp);
									dTumbCards.add(dPreCards.get(index));
									dPreCards.remove(dPreCards.get(index));
									opps.addAll(this.cardDieToTumbAfter(aPlayer, dPlayer, fightCard, aFightCards, fightCard2, dFightCards));
								}else{
									Skill skillImmunity = this.getSkillById(689);
									opp = new Opp();
									opp.setOpp(skillImmunity.getType());
									opp.setUUID(dPreCards.get(index).getUUID());
									opp.setTarget(dPreCards.get(index).getUUID());
									opps.add(opp);
								}

							}
						}
					}
				}
			}
		}
		opps.addAll(this.arrangeCard(aPlayer,dPlayer,aFightCards, dFightCards, aTumbCards, dTumbCards));
		return opps;
	}
	private List<Opp> attackPlayer(FightCard fightCard,Player dPlayer){
		Opp opp;
		List<Opp> opps = new ArrayList<Opp>();
		//开始物理攻击
		int val = fightCard.getAttack();
		opp = new Opp();
		opp.setOpp(1030);
		opp.setUUID(fightCard.getUUID());
		opp.setTarget(dPlayer.getName());
		opps.add(opp);
		opps.addAll(this.delPlayerHp(fightCard, val, dPlayer.getFightCards(),dPlayer));
		return opps;
	}
	private List<Skill> MapListToSkillList(List<Map<String, Object>> list){
		if(CollectionUtils.isEmpty(list)){
			return null;
		}
		List<Skill> listSkill = new ArrayList<Skill>();
		for(int i=0;i<list.size();i++){
			listSkill.add(MapToSkill(list.get(i)));
		}
		return listSkill;
	}
	private Skill MapToSkill(Map<String, Object> map){
		if(CollectionUtils.isEmpty(map)){
			return null;
		}
		Skill skill = new Skill();
		skill.setAffectType(Integer.parseInt(map.get("AFFECTTYPE").toString()));
		skill.setAffectValue(Integer.parseInt(map.get("AFFECTVALUE").toString()));
		skill.setAffectValue2(Integer.parseInt(map.get("AFFECTVALUE2").toString()));
		skill.setDesc(map.get("DESC").toString());
		skill.setLanchCondition(Integer.parseInt(map.get("LANCHCONDITION").toString()));
		skill.setLanchConditionValue(Integer.parseInt(map.get("LANCHCONDITIONVALUE").toString()));
		skill.setLanchType(Integer.parseInt(map.get("LANCHTYPE").toString()));
		skill.setName(map.get("NAME").toString());
		skill.setSkillCategory(Integer.parseInt(map.get("SKILLCATEGORY").toString()));
		skill.setSkillId(Integer.parseInt(map.get("SKILLID").toString()));
		skill.setType(Integer.parseInt(map.get("TYPE").toString()));
		return skill;
	}
	
	public SkillDao getSkillDao() {
		return skillDao;
	}

	public void setSkillDao(SkillDao skillDao) {
		this.skillDao = skillDao;
	}
	public CardManager getCardManager() {
		return cardManager;
	}
	public void setCardManager(CardManager cardManager) {
		this.cardManager = cardManager;
	}
	 //获取主动技能,物理攻击前
	@Override
	public List<Opp> getActiveSkillModelByType(List<Skill> skills,
			Player aPlayer, Player dPlayer, FightCard fightCard,List<Skill> skillBuffList, boolean isNewCard) {
		List<Opp> opps = new ArrayList<Opp>();
//		List<FightCard> aHandsCards = aPlayer.getHandsCards();
//		List<FightCard> dHandsCards = dPlayer.getHandsCards();
		Map<String, FightCard> dFightCards = dPlayer.getFightCards();
		Map<String, FightCard> aFightCards = aPlayer.getFightCards();
		//玩家准备区
//		List<FightCard> aPreCards = aPlayer.getPreCards();
//		List<FightCard> dPreCards = dPlayer.getPreCards();
		//玩家墓地
		List<FightCard> aTumbCards = aPlayer.getTumbCards();
		List<FightCard> dTumbCards = dPlayer.getTumbCards();
		Opp opp;
		Skill skill;
		//对于横扫技能
		Skill skillNew = null;
		//回合开始前判断卡牌是否装备了血量型武器
		UserSoul userSoul = fightCard.getSoul();
		if(userSoul!=null&&fightCard.getSoulRound()>0){
			Soul soul = soulManager.getSoulById(userSoul.getSoulId()); 
			//血量型武器生效
			if(soul.getSoulType()==5){
				opps.addAll(soulManager.getOppsBySoul(soul, fightCard));
				fightCard.setSoulRound(fightCard.getSoulRound()-1);
				if(fightCard.getSoulRound()==0){
					//如果武器回合数用完则武器失效
					opp = new Opp();
					opp.setOpp(1090);
					opp.setUUID(fightCard.getUUID());
					opp.setTarget(fightCard.getUUID());
					opps.add(opp);
				}
			}
			
		}
		//判断是否有丧失这一回合的buff
		if(!isNewCard){
			for(int i=skillBuffList.size()-1;i>=0;i--){
				Skill skillBuff =skillBuffList.get(i); 
				if(skillBuff.getLanchType()==4){
					if(skillBuff.getAffectType()==5){
						//取消卡牌buff
						opp = new Opp();
						opp.setOpp(1101);
						opp.setUUID(fightCard.getUUID());
						opp.setTarget(fightCard.getUUID());
						opp.setValue(0);
						opps.add(opp);
						//卡牌丧失这一回合
						skillBuffList.remove(i);
						return opps;
						
					}
					if(skillBuff.getAffectType()==9){
						//取消卡牌buff
						opp = new Opp();
						opp.setOpp(1103);
						opp.setUUID(fightCard.getUUID());
						opp.setTarget(fightCard.getUUID());
						opp.setValue(0);
						opps.add(opp);
						skillBuffList.remove(i);
						//卡牌丧失这一回合
						return opps;
					}
					if(skillBuff.getAffectType()==10){
						//取消卡牌buff
						opp = new Opp();
						opp.setOpp(1103);
						opp.setUUID(fightCard.getUUID());
						opp.setTarget(fightCard.getUUID());
						opp.setValue(0);
						opps.add(opp);
						skillBuffList.remove(i);
						//卡牌丧失这一回合
						return opps;
					}
					if(skillBuff.getAffectType()==11){
						//取消卡牌buff
						opp = new Opp();
						opp.setOpp(1103);
						opp.setUUID(fightCard.getUUID());
						opp.setTarget(fightCard.getUUID());
						opp.setValue(0);
						opps.add(opp);
						skillBuffList.remove(i);
						//卡牌丧失这一回合
						return opps;
					}
					if(skillBuff.getAffectType()==48){
						//取消卡牌buff
						opp = new Opp();
						opp.setOpp(1101);
						opp.setUUID(fightCard.getUUID());
						opp.setTarget(fightCard.getUUID());
						opp.setValue(0);
						opps.add(opp);
						skillBuffList.remove(i);
						//卡牌丧失这一回合
						return opps;
					}
				}
			}
		}
		
		//新上场卡牌释放出场回合技能
		if(isNewCard){
			for(int i=skillBuffList.size()-1;i>=0;i--){
				Skill skillBuff =skillBuffList.get(i); 
				if(skillBuff.getLanchType()==1){
					if(skillBuff.getAffectType()==30){
						opp = new Opp();
						opp.setOpp(skillBuff.getType());
						opp.setUUID(fightCard.getUUID());
						opp.setTarget(fightCard.getUUID());
						opps.add(opp);
						opp = new Opp();
						opp.setOpp(1020);
						opp.setUUID(fightCard.getUUID());
						opp.setTarget(fightCard.getUUID());
						fightCard.setAttack(fightCard.getAttack()+skillBuff.getAffectValue());
						opp.setValue(skillBuff.getAffectValue());
						opps.add(opp);
					}
				}
				if(skillBuff.getLanchType()==4){
					if(skillBuff.getAffectType()==48){
						//取消卡牌buff
						opp = new Opp();
						opp.setOpp(1101);
						opp.setUUID(fightCard.getUUID());
						opp.setTarget("");
						opp.setValue(1);
						opps.add(opp);
						FightCard fightCard2;
						for(int j=0;j<=this.getLastIndexFromFightregion(aFightCards);j++){
							fightCard2 = dFightCards.get(String.valueOf(j));
							if(fightCard2==null){
								continue;
							}
							fightCard2.getSkillBuff().add(skillBuff);
							opp.setTarget(opp.getTarget().concat(fightCard2.getUUID()).concat(","));
						}
						skillBuffList.remove(skillBuff);
					}
				}
			}
		}
		//整理卡牌
		opps.addAll(this.arrangeCard(aPlayer,dPlayer,aFightCards, dFightCards, aTumbCards, dTumbCards));
		//判断卡牌释放完出场回合技能时是否已经死亡
		if(fightCard.gethP()<=0){
			return opps;
		}
		//卡牌有主动技能则释放主动技能
		opps.addAll(this.getActiveOpps(skills, aPlayer, dPlayer, fightCard));
		//整理卡牌
		opps.addAll(this.arrangeCard(aPlayer,dPlayer,aFightCards, dFightCards, aTumbCards, dTumbCards));
		//判断卡牌释放完出场回合技能时是否已经死亡
		if(fightCard.gethP()<=0){
			return opps;
		}
		//判断是否有丧失这一回合的buff
		for(int i=skillBuffList.size()-1;i>=0;i--){
			Skill skillBuff =skillBuffList.get(i); 
			if(skillBuff.getLanchType()==4){
				if(skillBuff.getAffectType()>=6&&skillBuff.getAffectType()<=8){
					//取消卡牌buff
					opp = new Opp();
					opp.setOpp(1102);
					opp.setUUID(fightCard.getUUID());
					opp.setTarget(fightCard.getUUID());
					opp.setValue(0);
					opps.add(opp);
					//卡牌丧失这一回合
					skillBuffList.remove(i);
					return opps;
					
				}
			}
		}
		//物理攻击前，判断有没有装备武器（增加伤害的攻击型武器）
		if(userSoul!=null&&fightCard.getSoulRound()>0){
			Soul soul = soulManager.getSoulById(userSoul.getSoulId()); 
			//攻击型武器生效
			if(soul.getSoulType()==1||soul.getSoulType()==4){
				opps.addAll(soulManager.getOppsBySoul(soul, fightCard));
			}
			//当卡牌血量剩余10%时物理攻击增加50%伤害
			if(soul.getSoulType()==9){
				if(fightCard.gethP()<(this.getFightCardInitHp(aPlayer.getCards(), fightCard)*soul.getHp()/100)){
					opps.addAll(soulManager.getOppsBySoul(soul, fightCard));
				}
				
			}

		}
		//物理攻击
		if(skillNew!=null){
			int index = this.getIndex(aFightCards, fightCard);
			Opp oppNew = new Opp();
			oppNew.setOpp(skillNew.getType());
			oppNew.setUUID(fightCard.getUUID());
			oppNew.setTarget("");
			opps.add(oppNew);
			if(dFightCards.get(String.valueOf(index))!=null){
				//对左侧
				if(dFightCards.get(String.valueOf(index-1))!=null){
					opps.addAll(this.attackCards(aPlayer,dPlayer,fightCard, aFightCards, dFightCards.get(String.valueOf(index-1)), dFightCards, fightCard.getAttack()));
					oppNew.setTarget(oppNew.getTarget().concat(dFightCards.get(String.valueOf(index-1)).getUUID())+",");
				}
				//对正面
				opps.addAll(this.attackCards(aPlayer,dPlayer,fightCard, aFightCards, dFightCards.get(String.valueOf(index)), dFightCards, fightCard.getAttack()));
				oppNew.setTarget(oppNew.getTarget().concat(dFightCards.get(String.valueOf(index)).getUUID())+",");
				//对右侧
				if(dFightCards.get(String.valueOf(index+1))!=null){
					opps.addAll(this.attackCards(aPlayer,dPlayer,fightCard, aFightCards, dFightCards.get(String.valueOf(index+1)), dFightCards, fightCard.getAttack()));
					oppNew.setTarget(oppNew.getTarget().concat(dFightCards.get(String.valueOf(index+1)).getUUID())+",");
				}
			}else{
				//开始物理攻击英雄
				opps.addAll(this.attackPlayer(fightCard, dPlayer));
			}
		}else{
			int index = this.getIndex(aFightCards, fightCard);
			if(dFightCards.get(String.valueOf(index))!=null){
				opps.addAll(this.attackCards(aPlayer,dPlayer,fightCard, aFightCards, dFightCards.get(String.valueOf(index)), dFightCards, fightCard.getAttack()));
			}else{
				//开始物理攻击英雄
				opps.addAll(this.attackPlayer(fightCard, dPlayer));
			}
		}
		//物理攻击后如果卡牌装备了武器，则先减去武器加成
		if(userSoul!=null&&fightCard.getSoulRound()>0){
			Soul soul = soulManager.getSoulById(userSoul.getSoulId()); 
			//攻击型武器
			if(soul.getSoulType()==1&&fightCard.getSoulRound()>0){
				opp = new Opp();
				opp.setOpp(1020);
				opp.setUUID(fightCard.getUUID());
				opp.setTarget(fightCard.getUUID());
				opp.setValue(-soul.getAttack());
				opps.add(opp);
				fightCard.setAttack(fightCard.getAttack()-soul.getAttack());
				fightCard.setSoulRound(fightCard.getSoulRound()-1);
				if(fightCard.getSoulRound()==0){
					//如果武器回合数用完则武器失效
					opp = new Opp();
					opp.setOpp(1090);
					opp.setUUID(fightCard.getUUID());
					opp.setTarget(fightCard.getUUID());
					opps.add(opp);
				}
			}
			//暴击
			if(soul.getSoulType()==4&&fightCard.getSoulRound()>0&&fightCard.getSoul().getIsFight()>0){
				opp = new Opp();
				opp.setOpp(1020);
				opp.setUUID(fightCard.getUUID());
				opp.setTarget(fightCard.getUUID());
				opp.setValue(-fightCard.getAttack()/2);
				opps.add(opp);
				fightCard.setAttack(fightCard.getAttack()-fightCard.getAttack()/2);
				fightCard.setSoulRound(fightCard.getSoulRound()-1);
				//标示暴击是否生效
				fightCard.getSoul().setIsFight(0);
				if(fightCard.getSoulRound()==0){
					//如果武器回合数用完则武器失效
					opp = new Opp();
					opp.setOpp(1090);
					opp.setUUID(fightCard.getUUID());
					opp.setTarget(fightCard.getUUID());
					opps.add(opp);
				}
			}
			//垂死挣扎
			if(soul.getSoulType()==9&&fightCard.getSoulRound()>0&&fightCard.getSoul().getIsFight()>0){
				opp = new Opp();
				opp.setOpp(1020);
				opp.setUUID(fightCard.getUUID());
				opp.setTarget(fightCard.getUUID());
				opp.setValue(fightCard.getAttack()*soul.getAttack()/(100+soul.getAttack()));
				opps.add(opp);
				fightCard.setAttack(fightCard.getAttack()*100/(soul.getAttack()+100));
				fightCard.setSoulRound(fightCard.getSoulRound()-1);
				//标示暴击是否生效
				fightCard.getSoul().setIsFight(0);
				if(fightCard.getSoulRound()==0){
					//如果武器回合数用完则武器失效
					opp = new Opp();
					opp.setOpp(1090);
					opp.setUUID(fightCard.getUUID());
					opp.setTarget(fightCard.getUUID());
					opps.add(opp);
				}
			}
			
		}
		//物理攻击后恢复卡牌buff
		for(int j=0;j<skills.size();j++){
			skill = skills.get(j);
			int index = this.getIndex(aFightCards, fightCard);
			if(skill.getLanchType()==1){
				if(skill.getLanchCondition()==1){
					if(dFightCards.get(String.valueOf(index))!=null){
						Card card = cardManager.getCardById(new Long(dFightCards.get(String.valueOf(index)).getCardId()));
						if(card.getRace()==skill.getLanchConditionValue()){
							int val1 = fightCard.getAttack()*skill.getAffectValue()/(100+skill.getAffectValue());
							opp = new Opp();
							opp.setOpp(1020);
							opp.setUUID(fightCard.getUUID());
							opp.setTarget(fightCard.getUUID());
							opp.setValue(-val1);
							opps.add(opp);
							fightCard.setAttack(fightCard.getAttack()-val1);
						}
					}
				}
			}
		}
		//行动结束后减少生命值和攻击力
		for(int i=skillBuffList.size()-1;i>=0;i--){
			Skill skillBuff =skillBuffList.get(i); 
			if(skillBuff.getLanchType()==1){
				if(skillBuff.getAffectType()==30){
					opp = new Opp();
					opp.setOpp(1020);
					opp.setUUID(fightCard.getUUID());
					opp.setTarget(fightCard.getUUID());
					opp.setValue(-skillBuff.getAffectValue());
					opps.add(opp);
					fightCard.setAttack(fightCard.getAttack()-skillBuff.getAffectValue());
					skillBuffList.remove(skillBuff);
				}
				if(skillBuff.getLanchCondition()==0){
					if(skillBuff.getAffectType()==57){
						int val1 = fightCard.getAttack()*skillBuff.getAffectValue2()/(100+skillBuff.getAffectValue2());
						opp = new Opp();
						opp.setOpp(1020);
						opp.setUUID(fightCard.getUUID());
						opp.setTarget(fightCard.getUUID());
						opp.setValue(-val1);
						opps.add(opp);
						fightCard.setAttack(fightCard.getAttack()-val1);
						fightCard.getSkillBuff().remove(skillBuff);
					}
				}
			}
			if(skillBuff.getLanchType()==3){
				if(skillBuff.getAffectType()==45){
					opp = new Opp();
					opp.setOpp(1040);
					opp.setUUID(fightCard.getUUID());
					opp.setTarget(fightCard.getUUID());
					opp.setValue(-skillBuff.getAffectValue());
					opps.add(opp);
					fightCard.sethP(fightCard.gethP()-skillBuff.getAffectValue());
				}
			}
			if(skillBuff.getLanchType()==4){
				if(skillBuff.getAffectType()>=16&&skillBuff.getAffectType()<=18){
					opp = new Opp();
					opp.setOpp(1040);
					opp.setUUID(fightCard.getUUID());
					opp.setTarget(fightCard.getUUID());
					opp.setValue(-skillBuff.getAffectValue());
					opps.add(opp);
					fightCard.sethP(fightCard.gethP()-skillBuff.getAffectValue());
					opp = new Opp();
					opp.setOpp(1104);
					opp.setUUID(fightCard.getUUID());
					opp.setTarget(fightCard.getUUID());
					opp.setValue(0);
					opps.add(opp);
					skillBuffList.remove(i);
				}
				if(skillBuff.getAffectType()==46){
					opp = new Opp();
					opp.setOpp(1040);
					opp.setUUID(fightCard.getUUID());
					opp.setTarget(fightCard.getUUID());
					opp.setValue(-skillBuff.getAffectValue());
					opps.add(opp);
					fightCard.sethP(fightCard.gethP()-skillBuff.getAffectValue());
				}
			}
		}
		
		//整理卡牌
		opps.addAll(this.arrangeCard(aPlayer,dPlayer,aFightCards, dFightCards, aTumbCards, dTumbCards));
		return opps;
		
	}
	@Override
	public List<Opp> getRuneOpps(FightRune fightRune, Player aPlayer,
			Player dPlayer,int round) {
		Rune rune = runeManager.getRuneById(fightRune.getRuneId());
		List<Opp> opps = new ArrayList<Opp>();
		List<Skill> skills = new ArrayList<Skill>();
		FightCard fightCard = new FightCard();
		fightCard.setUUID(fightRune.getuUID());
		Skill skill = fightRune.getSkill();
		int size=0;
		if(rune.getSkillConditionType()==1){
			size=round;
		}
		if(rune.getSkillConditionType()==2){
			size = this.getRaceCountInFightregion(aPlayer.getFightCards(), rune.getSkillConditionRace());
		}
		if(rune.getSkillConditionType()==3){
			size = this.getRaceCountInList(aPlayer.getTumbCards(), rune.getSkillConditionRace());
		}
		if(rune.getSkillConditionType()==4){
			size = aPlayer.getTumbCards().size();
		}
		if(rune.getSkillConditionType()==5){
			size = this.getRaceCountInList(aPlayer.getPreCards(), rune.getSkillConditionRace());
		}
		if(rune.getSkillConditionType()==6){
			size = aPlayer.getHandsCards().size();
		}
		if(rune.getSkillConditionType()==7){
			size = aPlayer.getHP()*100/aPlayer.getRemainHP();
			
		}
		if(rune.getSkillConditionType()==8){
			size = this.getRaceCountInFightregion(dPlayer.getFightCards(), rune.getSkillConditionRace());
		}
		if(rune.getSkillConditionType()==9){
			size = dPlayer.getFightCards().size();
		}
		if(rune.getSkillConditionType()==10){
			size = this.getRaceCountInList(dPlayer.getTumbCards(), rune.getSkillConditionRace());
		}
		if(rune.getSkillConditionType()==11){
			size = dPlayer.getTumbCards().size();
		}
		if(rune.getSkillConditionType()==12){
			size = dPlayer.getPreCards().size();
		}
		if(rune.getSkillConditionCompare()==1){
			if(size>rune.getSkillConditionValue()){
				skills.add(skill);
//				fightRune.setRemainTimes(fightRune.getRemainTimes()-1);
			}
		}else{
			if(size<rune.getSkillConditionValue()){
				skills.add(skill);
//				fightRune.setRemainTimes(fightRune.getRemainTimes()-1);
			}
		}
		if(CollectionUtils.isEmpty(skills)){
			return null;
		}else{
			opps.addAll(this.getActiveOpps(skills, aPlayer, dPlayer, fightCard));
		}
		
		return opps;
	}
	//主动技能执行模型
	private List<Opp> getActiveOpps(List<Skill> skills,Player aPlayer, Player dPlayer, FightCard fightCard){
		List<FightCard> aHandsCards = aPlayer.getHandsCards();
		List<FightCard> dHandsCards = dPlayer.getHandsCards();
		Map<String, FightCard> dFightCards = dPlayer.getFightCards();
		Map<String, FightCard> aFightCards = aPlayer.getFightCards();
		//玩家准备区
//		List<FightCard> aPreCards = aPlayer.getPreCards();
//		List<FightCard> dPreCards = dPlayer.getPreCards();
		//玩家墓地
		List<FightCard> aTumbCards = aPlayer.getTumbCards();
		List<FightCard> dTumbCards = dPlayer.getTumbCards();
		List<Opp> opps = new ArrayList<Opp>();
		Skill skill;
		Opp opp;
		Random rnd = new Random();
		//卡牌主动技能
		for(int j=0;j<skills.size();j++){
			skill = skills.get(j);
			int index = this.getIndex(aFightCards, fightCard);
			if(skill.getLanchType()==1){
				if(skill.getLanchCondition()==1){
					if(dFightCards.get(String.valueOf(index))!=null){
						Card card = cardManager.getCardById(new Long(dFightCards.get(String.valueOf(index)).getCardId()));
						if(card.getRace()==skill.getLanchConditionValue()){
							int val1 = fightCard.getAttack()*skill.getAffectValue()/100;
							opp = new Opp();
							opp.setOpp(skill.getType());
							opp.setUUID(fightCard.getUUID());
							opp.setTarget(fightCard.getUUID());
							opps.add(opp);
							opp = new Opp();
							opp.setOpp(1020);
							opp.setUUID(fightCard.getUUID());
							opp.setTarget(fightCard.getUUID());
							opp.setValue(val1);
							opps.add(opp);
							fightCard.setAttack(fightCard.getAttack()+val1);
						}
					}
				}
				if(skill.getLanchCondition()==0){
					if(skill.getAffectType()==57){
						if(dFightCards.get(String.valueOf(index))!=null){
							int s = rnd.nextInt(10);
							int val1 = fightCard.getAttack()*skill.getAffectValue2()/100;
							if(s>4){
								opp = new Opp();
								opp.setOpp(skill.getType());
								opp.setUUID(fightCard.getUUID());
								opp.setTarget(fightCard.getUUID());
								opps.add(opp);
								opp = new Opp();
								opp.setOpp(1020);
								opp.setUUID(fightCard.getUUID());
								opp.setTarget(fightCard.getUUID());
								opp.setValue(val1);
								opps.add(opp);
								fightCard.setAttack(fightCard.getAttack()+val1);
								fightCard.getSkillBuff().add(skill);
							}
						}
					}
				}

			}
			if(skill.getLanchType()==4){
				if(skill.getAffectType()==5){
					Opp oppNew = new Opp();
					oppNew.setOpp(skill.getType());
					oppNew.setUUID(fightCard.getUUID());
					oppNew.setTarget("");
					opps.add(oppNew);
					int s = 0;
					for(int c=0;c<dFightCards.size();c++){
						if(s<skill.getAffectValue()&&dFightCards.get(String.valueOf(c))!=null){
							int r = rnd.nextInt(100);
							oppNew.setTarget(oppNew.getTarget().concat(dFightCards.get(String.valueOf(c)).getUUID()).concat(","));
							if(r<65){
								if(!hasImmunitySkill(dFightCards.get(String.valueOf(c)))){
									opp = new Opp();
									opp.setOpp(1101);
									opp.setTarget(dFightCards.get(String.valueOf(c)).getUUID());
									opp.setUUID(fightCard.getUUID());
									opp.setValue(1);
									opps.add(opp);
									dFightCards.get(String.valueOf(c)).getSkillBuff().add(skill);
								}else{
									Skill skillImmunity = this.getSkillById(689);
									opp = new Opp();
									opp.setOpp(skillImmunity.getType());
									opp.setUUID(dFightCards.get(String.valueOf(c)).getUUID());
									opp.setTarget(dFightCards.get(String.valueOf(c)).getUUID());
									opps.add(opp);
								}
							}
							s++;
						}

					}
				}
				//使敌方n张卡牌受到25点伤害，50%概率无法物理攻击。
				if(skill.getAffectType()>=6&&skill.getAffectType()<=18){
					int size = 1;
					if(skill.getAffectType()==7||skill.getAffectType()==10||skill.getAffectType()==13||skill.getAffectType()==17){
						size = 3;
					}
					if(skill.getAffectType()==8||skill.getAffectType()==11||skill.getAffectType()==14||skill.getAffectType()==18){
						size = dFightCards.size();
					}
					List<String> targetArr = this.getIndexRandom(dFightCards, size);
					Opp oppNew = new Opp();
					oppNew.setOpp(skill.getType());
					oppNew.setUUID(fightCard.getUUID());
					oppNew.setTarget("");
					opps.add(oppNew);
					Opp oppNew1=null;
					if(skill.getAffectType()>=16&&skill.getAffectType()<=18){
						oppNew1 = new Opp();
						oppNew1.setOpp(1104);
						oppNew1.setUUID(fightCard.getUUID());
						oppNew1.setTarget("");
						opps.add(oppNew1);
					}
					int val = skill.getAffectValue();
					if(skill.getAffectType()>=12&&skill.getAffectType()<=14){
						val = val+rnd.nextInt(val);
					}

					for(int c =0;c<targetArr.size();c++){
						FightCard dFight = this.getFightCardByUuidFromFightRegion(targetArr.get(c), dFightCards);
						if(dFight.gethP()>0){
							oppNew.setTarget(oppNew.getTarget().concat(targetArr.get(c)).concat(","));
							if(!hasImmunitySkill(dFight)){
								opps.addAll(this.magicAttackCards(aPlayer,dPlayer,fightCard, dFight, skill,val));
//								opp = new Opp();
//								opp.setOpp(1040);
//								opp.setUUID(fightCard.getUUID());
//								opp.setTarget(targetArr.get(c));
//								opp.setValue(-skill.getAffectValue());
//								opps.add(opp);
//								dFight.sethP(dFight.gethP()-skill.getAffectValue());
								if(skill.getAffectType()>=6&&skill.getAffectType()<=11){
									if(skill.getAffectType()>=6&&skill.getAffectType()<=8){
										int r = rnd.nextInt(100);
										if(r<skill.getAffectValue2()){
											opp = new Opp();
											opp.setOpp(1102);
											opp.setTarget(dFight.getUUID());
											opp.setUUID(fightCard.getUUID());
											opp.setValue(1);
											opps.add(opp);
											dFight.getSkillBuff().add(skill);
										}
									}
									if(skill.getAffectType()>=9&&skill.getAffectType()<=11){
										int r = rnd.nextInt(100);
										if(r<skill.getAffectValue2()){
											opp = new Opp();
											opp.setOpp(1103);
											opp.setTarget(dFight.getUUID());
											opp.setUUID(fightCard.getUUID());
											opp.setValue(1);
											opps.add(opp);
											dFight.getSkillBuff().add(skill);
										}
									}

								}
								if(skill.getAffectType()==15){
									opp = new Opp();
									opp.setOpp(1040);
									opp.setUUID(fightCard.getUUID());
									opp.setTarget(fightCard.getUUID());
									opp.setValue(skill.getAffectValue());
									opps.add(opp);
									fightCard.sethP(fightCard.gethP()+skill.getAffectValue());
								}
								if(skill.getAffectType()>=16&&skill.getAffectType()<=18){
									opp = new Opp();
									opp.setOpp(1104);
									opp.setTarget(dFight.getUUID());
									opp.setUUID(fightCard.getUUID());
									opp.setValue(1);
									opps.add(opp);
									oppNew1.setTarget(oppNew1.getTarget().concat(dFight.getUUID()).concat(","));
									dFight.getSkillBuff().add(skill);
								}
							}else{
								Skill skillImmunity = this.getSkillById(689);
								opp = new Opp();
								opp.setOpp(skillImmunity.getType());
								opp.setUUID(dFight.getUUID());
								opp.setTarget(dFight.getUUID());
								opps.add(opp);
							}
						}
					}
					
				}
				if(skill.getAffectType()==20){
					int index1 = this.getMaxLoseHpCard(aPlayer.getCards(), aFightCards);
					if(index1>-1){

						int val = skill.getAffectValue();
						if(aFightCards.get(String.valueOf(index1))!=null){
							if(!this.hasLaceratedSkill(aFightCards.get(String.valueOf(index1)))){
								opp = new Opp();
								opp.setOpp(skill.getType());
								opp.setUUID(fightCard.getUUID());
								opp.setTarget(aFightCards.get(String.valueOf(index1)).getUUID());
								opps.add(opp);
								opp = new Opp();
								opp.setOpp(1040);
								opp.setUUID(fightCard.getUUID());
								opp.setTarget(aFightCards.get(String.valueOf(index1)).getUUID());
								opp.setValue(val);
								opps.add(opp);
								aFightCards.get(String.valueOf(index1)).sethP(aFightCards.get(String.valueOf(index1)).gethP()+val);
							}
							
						}
					}

				}
				if(skill.getAffectType()==21){
					Opp oppNew = new Opp();
					oppNew.setOpp(skill.getType());
					oppNew.setUUID(fightCard.getUUID());
					oppNew.setTarget("");
					opps.add(oppNew);
					FightCard fightCard2;
					int val = skill.getAffectValue();
					for(int i=0;i<=this.getLastIndexFromFightregion(aFightCards);i++){
						fightCard2 = aFightCards.get(String.valueOf(i));
						if(fightCard2!=null){
							if(!this.hasLaceratedSkill(fightCard2)){
								opp = new Opp();
								opp.setOpp(1040);
								opp.setUUID(fightCard.getUUID());
								opp.setTarget(fightCard2.getUUID());
								opp.setValue(val);
								opps.add(opp);
								oppNew.setTarget(oppNew.getTarget().concat(fightCard2.getUUID()).concat(","));
								fightCard2.sethP(fightCard2.gethP()+val);
							}

						}
						
					}
					
				}
				if(skill.getAffectType()==22){
					String key = this.getMinHpCard(dFightCards);
					if(!key.equals("-1")){
						opp = new Opp();
						opp.setOpp(skill.getType());
						opp.setUUID(fightCard.getUUID());
						opp.setTarget(dFightCards.get(key).getUUID());
						opp.setValue(skill.getAffectValue());
						opps.add(opp);
						if(!hasImmunitySkill(dFightCards.get(key))){
							opps.addAll(this.magicAttackCards(aPlayer,dPlayer,fightCard, dFightCards.get(key), skill,skill.getAffectValue()));
						}else{
							Skill skillImmunity = this.getSkillById(689);
							opp = new Opp();
							opp.setOpp(skillImmunity.getType());
							opp.setUUID(dFightCards.get(key).getUUID());
							opp.setTarget(dFightCards.get(key).getUUID());
							opps.add(opp);
						}
					
//						opp = new Opp();
//						opp.setOpp(1040);
//						opp.setUUID(fightCard.getUUID());
//						opp.setTarget(dFightCards.get(key).getUUID());
//						opp.setValue(-skill.getAffectValue());
//						opps.add(opp);
//						dFightCards.get(key).sethP(dFightCards.get(key).gethP()-skill.getAffectValue());
					}

				}
				if(skill.getAffectType()==29){
					Opp oppNew = new Opp();
					oppNew.setOpp(skill.getType());
					oppNew.setUUID(fightCard.getUUID());
					oppNew.setTarget("");
					opps.add(oppNew);
					Iterator<FightCard> ite = dFightCards.values().iterator();
					FightCard fightCard2;
					int val = skill.getAffectValue();
					while(ite.hasNext()){
						fightCard2 =ite.next();
						oppNew.setTarget(oppNew.getTarget().concat(fightCard2.getUUID()).concat(","));
						if(!hasImmunitySkill(fightCard2)){
							int valskill = val;
							if(fightCard2.getAttack()<valskill){
								valskill = fightCard2.getAttack();
							}
							opp = new Opp();
							opp.setOpp(1020);
							opp.setUUID(fightCard.getUUID());
							opp.setTarget(fightCard2.getUUID());
							opp.setValue(-valskill);
							opps.add(opp);
							fightCard2.setAttack(fightCard2.getAttack()-valskill);
						}else{
							Skill skillImmunity = this.getSkillById(689);
							opp = new Opp();
							opp.setOpp(skillImmunity.getType());
							opp.setUUID(fightCard2.getUUID());
							opp.setTarget(fightCard2.getUUID());
							opps.add(opp);
						}
					}
				}
				if(skill.getAffectType()==32){
					if(aPlayer.getHP()<aPlayer.getRemainHP()){
						opp = new Opp();
						opp.setOpp(skill.getType());
						opp.setUUID(fightCard.getUUID());
						opp.setTarget(aPlayer.getName());
						opps.add(opp);
						opp = new Opp();
						opps.addAll(this.addPlayerHp(fightCard, skill.getAffectValue(), aPlayer));
					}
				}
				if(skill.getAffectType()==33){
					opp = new Opp();
					opp.setOpp(skill.getType());
					opp.setUUID(fightCard.getUUID());
					opp.setTarget(dPlayer.getName());
					opps.add(opp);
					opps.addAll(this.delPlayerHp(fightCard, skill.getAffectValue(),dFightCards,dPlayer));
				}
				if(skill.getAffectType()==38){
					Opp oppNew = new Opp();
					oppNew.setOpp(skill.getType());
					oppNew.setUUID(fightCard.getUUID());
					oppNew.setTarget("");
					opps.add(oppNew);
					Iterator<FightCard> ite = dFightCards.values().iterator();
					FightCard fightCard2;
					int val = skill.getAffectValue();
					while(ite.hasNext()){
						fightCard2 =ite.next();
						oppNew.setTarget(oppNew.getTarget().concat(fightCard2.getUUID()).concat(","));
						if(!hasImmunitySkill(fightCard2)){
							int valskill = val;
							if(fightCard2.getAttack()<valskill){
								valskill = fightCard2.getAttack();
							}
							opp = new Opp();
							opp.setOpp(1020);
							opp.setUUID(fightCard.getUUID());
							opp.setTarget(fightCard2.getUUID());
							opp.setValue(-valskill);
							opps.add(opp);
							opps.addAll(this.magicAttackCards(aPlayer,dPlayer,fightCard, fightCard2, skill,skill.getAffectValue()));
//							opp = new Opp();
//							opp.setOpp(1040);
//							opp.setUUID(fightCard.getUUID());
//							opp.setTarget(fightCard2.getUUID());
//							opp.setValue(-val);
//							opps.add(opp);
							fightCard2.setAttack(fightCard2.getAttack()-valskill);
//							fightCard2.sethP(fightCard2.gethP()-val);
						}else{
							Skill skillImmunity = this.getSkillById(689);
							opp = new Opp();
							opp.setOpp(skillImmunity.getType());
							opp.setUUID(fightCard2.getUUID());
							opp.setTarget(fightCard2.getUUID());
							opps.add(opp);
						}

					}
				}
				if(skill.getAffectType()==39){
					Opp oppNew = new Opp();
					oppNew.setOpp(skill.getType());
					oppNew.setUUID(fightCard.getUUID());
					FightCard fightCard2;
					FightCard fightCard3;
					Card card;
					for(int c=aTumbCards.size()-1;c>=0;c--){
						fightCard2 =aTumbCards.get(c);
						card = cardManager.getCardById(new Long(fightCard2.getCardId()));
						if(card.getSkill()!=597&&card.getLockSkill1()!=597&&card.getLockSkill2()!=597){
							opps.add(oppNew);
							opp = new Opp();
							opp.setOpp(1006);
							opp.setUUID(fightCard2.getUUID());
							opp.setTarget(fightCard2.getUUID());
							opps.add(opp);
							oppNew.setTarget(fightCard2.getUUID());
							fightCard3 = this.getFightCardByUuid(fightCard2.getUUID(), aPlayer.getCards());
							aTumbCards.remove(c);
							aFightCards.put(String.valueOf(this.getLastIndexFromFightregion(aFightCards)), fightCard3);
							break;
						}
					}
				}
				if(skill.getAffectType()==40){
					if(!CollectionUtils.isEmpty(dPlayer.getPreCards())){
						int key = this.getMaxWaitCard(dPlayer.getPreCards());
						FightCard fightCard2 = dPlayer.getPreCards().get(key);
						if(!hasImmunitySkill(fightCard2)&&!hasBuDongSkill(fightCard2)){
							opp = new Opp();
							opp.setOpp(skill.getType());
							opp.setUUID(fightCard.getUUID());
							opp.setTarget(fightCard2.getUUID());
							opps.add(opp);
							opps.addAll(this.cardDieToTumb(dPlayer, aPlayer, fightCard2, dFightCards, null, aFightCards));
							opp = new Opp();
							opp.setOpp(1003);
							opp.setUUID(fightCard.getUUID());
							opp.setTarget(fightCard2.getUUID());
							opps.add(opp);
							dPlayer.getPreCards().remove(key);
							dTumbCards.add(fightCard2);
							opps.addAll(this.cardDieToTumbAfter(dPlayer, aPlayer, fightCard2, dFightCards, null, aFightCards));
						}else{
							Skill skillImmunity = this.getSkillById(689);
							if(hasBuDongSkill(fightCard2)){
								skillImmunity = this.getSkillById(688);
							}
							opp = new Opp();
							opp.setOpp(skillImmunity.getType());
							opp.setUUID(fightCard2.getUUID());
							opp.setTarget(fightCard2.getUUID());
							opps.add(opp);
						}
						
					}
				}
				if(skill.getAffectType()==42){
					if(dFightCards.get(String.valueOf(index))!=null){
						FightCard xFightCard = this.getFightCardByUuid(dFightCards.get(String.valueOf(index)).getUUID(), dPlayer.getCards());
						if(xFightCard!=null){
							opp = new Opp();
							opp.setOpp(skill.getType());
							opp.setUUID(fightCard.getUUID());
							opp.setTarget(dFightCards.get(String.valueOf(index)).getUUID());
							opps.add(opp);
							if(!hasImmunitySkill(dFightCards.get(String.valueOf(index)))&&!hasBuDongSkill(dFightCards.get(String.valueOf(index)))){
								dHandsCards.add(xFightCard);
								opp = new Opp();
								opp.setOpp(1007);
								opp.setUUID(dFightCards.get(String.valueOf(index)).getUUID());
								opp.setTarget(dFightCards.get(String.valueOf(index)).getUUID());
								opps.add(opp);
								dFightCards.remove(String.valueOf(index));
							}else{
								Skill skillImmunity = this.getSkillById(689);
								if(hasBuDongSkill(dFightCards.get(String.valueOf(index)))){
									skillImmunity = this.getSkillById(688);
								}
								opp = new Opp();
								opp.setOpp(skillImmunity.getType());
								opp.setUUID(dFightCards.get(String.valueOf(index)).getUUID());
								opp.setTarget(dFightCards.get(String.valueOf(index)).getUUID());
								opps.add(opp);
							}

						}
					}
				}
				if(skill.getAffectType()==43){
					List<String> indexes = this.getIndexRandom(dFightCards, 1);
					if(!CollectionUtils.isEmpty(indexes)){
						FightCard fightCard2 = this.getFightCardByUuidFromFightRegion(indexes.get(0), dFightCards);
						opp = new Opp();
						opp.setOpp(skill.getType());
						opp.setUUID(fightCard.getUUID());
						opp.setTarget(fightCard2.getUUID());
						opps.add(opp);
						if(!hasImmunitySkill(fightCard2)&&!hasBuDongSkill(fightCard2)){
							int s = this.getIndex(dFightCards, fightCard2);
							opps.addAll(this.cardDieToTumb(aPlayer, dPlayer, aFightCards.get(String.valueOf(s)), aFightCards, fightCard2, dFightCards));
							opp = new Opp();
							opp.setOpp(1003);
							opp.setUUID(fightCard.getUUID());
							opp.setTarget(fightCard2.getUUID());
							opps.add(opp);
							dFightCards.remove(indexes.get(0));
							dTumbCards.add(fightCard2);
							opps.addAll(this.cardDieToTumbAfter(aPlayer, dPlayer, aFightCards.get(String.valueOf(s)), aFightCards, fightCard2, dFightCards));
						}else{
							Skill skillImmunity = this.getSkillById(689);
							if(hasBuDongSkill(fightCard2)){
								skillImmunity = this.getSkillById(688);
							}
							opp = new Opp();
							opp.setOpp(skillImmunity.getType());
							opp.setUUID(fightCard2.getUUID());
							opp.setTarget(fightCard2.getUUID());
							opps.add(opp);
						}
					}
					
				}
				if(skill.getAffectType()==44){
					if(!CollectionUtils.isEmpty(aTumbCards)){
						FightCard fightCard2 = aTumbCards.get(aTumbCards.size()-1);
						if(fightCard2!=null){
							opp = new Opp();
							opp.setOpp(skill.getType());
							opp.setUUID(fightCard.getUUID());
							opp.setTarget(fightCard2.getUUID());
							opps.add(opp);
							aHandsCards.add(this.getFightCardByUuid(fightCard2.getUUID(),aPlayer.getCards()));
							opp = new Opp();
							opp.setOpp(1007);
							opp.setUUID(fightCard2.getUUID());
							opp.setTarget(fightCard2.getUUID());
							opps.add(opp);
							aTumbCards.remove(fightCard2);
						}
					}
				}
				if(skill.getAffectType()==46){
					//燃烧伤害
					Opp oppNew = new Opp();
					oppNew.setOpp(skill.getType());
					oppNew.setUUID(fightCard.getUUID());
					oppNew.setTarget("");
					opps.add(oppNew);
					opp = new Opp();
					opp.setOpp(1107);
					opp.setUUID(fightCard.getUUID());
					opp.setTarget("");
					opp.setValue(1);
					opps.add(opp);
					Iterator<FightCard> ite = dFightCards.values().iterator();
					FightCard fightCard2;
					while(ite.hasNext()){
						fightCard2 =ite.next();
						if(!fightCard2.getSkillBuff().contains(skill)){
							oppNew.setTarget(oppNew.getTarget().concat(fightCard2.getUUID()).concat(","));
							opp.setTarget(opp.getTarget().concat(fightCard2.getUUID()).concat(","));
							fightCard2.getSkillBuff().add(skill);
						}
					}
				}
//				if(skill.getAffectType()==48){
//					//出场回合施放，使敌方所有卡牌丧失下回合的攻击能力。
//					Opp oppNew = new Opp();
//					oppNew.setOpp(skill.getType());
//					oppNew.setUUID(fightCard.getUUID());
//					oppNew.setTarget("");
//					opps.add(oppNew);
//					opp = new Opp();
//					opp.setOpp(1101);
//					opp.setUUID(fightCard.getUUID());
//					opp.setTarget("");
//					opp.setValue(1);
//					opps.add(opp);
//					Iterator<FightCard> ite = dFightCards.values().iterator();
//					FightCard fightCard2;
//					while(ite.hasNext()){
//						fightCard2 =ite.next();
//						opp.setTarget(opp.getTarget().concat(fightCard2.getUUID()).concat(","));
//						oppNew.setTarget(oppNew.getTarget().concat(fightCard2.getUUID()).concat(","));
//					}
//				}
				
			}
			
			if(skill.getLanchType()==7){
				if(!this.hasLaceratedSkill(fightCard)){
					opp = new Opp();
					opp.setOpp(skill.getType());
					opp.setUUID(fightCard.getUUID());
					opp.setTarget(fightCard.getUUID());
					opps.add(opp);
					int val = skill.getAffectValue();
					opp = new Opp();
					opp.setOpp(1040);
					opp.setUUID(fightCard.getUUID());
					opp.setTarget(fightCard.getUUID());
					opp.setValue(val);
					opps.add(opp);
					fightCard.sethP(fightCard.gethP()+val);
				}

			}
			if(skill.getLanchType()==15){
				if(skill.getAffectType()==60){
					//我方所有卡牌获得一项新技能
					Opp oppNew = new Opp();
					oppNew.setOpp(skill.getType());
					oppNew.setUUID(fightCard.getUUID());
					oppNew.setTarget("");
					opps.add(oppNew);
					Skill skill2 = this.getSkillById(skill.getAffectValue());
					Iterator<FightCard> ite = aFightCards.values().iterator();
					FightCard fightCard2;
					while(ite.hasNext()){
						fightCard2 =ite.next();
						if(!fightCard2.getSkillNew().contains(skill2)){
							fightCard2.getSkillNew().add(skill2);
							oppNew.setTarget(oppNew.getTarget().concat(fightCard2.getUUID()).concat(","));
						}
					}
				}
				if(skill.getAffectType()==61||skill.getAffectType()==62){

					int size = 1;
					if(skill.getAffectType()==62){
						size = aFightCards.size();
					}
					List<String> indexes = this.getIndexRandom(aFightCards, size);
					if(indexes.size()>0){
						Opp oppNew = new Opp();
						oppNew.setOpp(skill.getType());
						oppNew.setUUID(fightCard.getUUID());
						oppNew.setTarget("");
						opps.add(oppNew);
						FightCard fightCard2;
						for(int i=0;i<indexes.size();i++){
							fightCard2 = this.getFightCardByUuidFromFightRegion(indexes.get(i), aFightCards);
							opp = new Opp();
							opp.setOpp(1040);
							opp.setUUID(fightCard.getUUID());
							opp.setTarget(fightCard2.getUUID());
							opp.setValue(skill.getAffectValue());
							opps.add(opp);
							fightCard2.sethP(fightCard2.gethP()+skill.getAffectValue());
							oppNew.setTarget(oppNew.getTarget().concat(fightCard2.getUUID()).concat(","));
						}
					}
					
					
				}
				if(skill.getAffectType()==63){
					Opp oppNew = new Opp();
					oppNew.setOpp(skill.getType());
					oppNew.setUUID(fightCard.getUUID());
					oppNew.setTarget("");
					opps.add(oppNew);
					List<String> indexes = this.getIndexRandom(aFightCards, 1);
					FightCard fightCard2;
					for(int i=0;i<indexes.size();i++){
						fightCard2 =this.getFightCardByUuidFromFightRegion(indexes.get(i), aFightCards);
						opp = new Opp();
						opp.setOpp(1020);
						opp.setUUID(fightCard.getUUID());
						opp.setTarget(fightCard2.getUUID());
						opp.setValue(skill.getAffectValue());
						opps.add(opp);
						fightCard2.setAttack(fightCard2.getAttack()+skill.getAffectValue());
						oppNew.setTarget(oppNew.getTarget().concat(fightCard2.getUUID()).concat(","));
					}
				}
				if(skill.getAffectType()==64){
					Opp oppNew = new Opp();
					oppNew.setOpp(skill.getType());
					oppNew.setUUID(fightCard.getUUID());
					oppNew.setTarget("");
					opps.add(oppNew);
					Iterator<FightCard> ite = aFightCards.values().iterator();
					FightCard fightCard2;
					while(ite.hasNext()){
						fightCard2 =ite.next();
						opp = new Opp();
						opp.setOpp(1020);
						opp.setUUID(fightCard.getUUID());
						opp.setTarget(fightCard2.getUUID());
						opp.setValue(skill.getAffectValue());
						opps.add(opp);
						fightCard2.setAttack(fightCard2.getAttack()+skill.getAffectValue());
						oppNew.setTarget(oppNew.getTarget().concat(fightCard2.getUUID()).concat(","));
					}
				}
			}
			
			
		}
		return opps;
	}
	private List<Opp> arrangeCard(Player aPlayer,Player dPlayer,Map<String,FightCard> aFightCards,Map<String,FightCard> dFightCards,List<FightCard> aTumbCards,List<FightCard> dTumbCards){
		List<Opp> opps = new ArrayList<Opp>();
		FightCard fightCard;
		Opp opp;
		for(int i=0;i<=this.getLastIndexFromFightregion(aFightCards);i++){
			fightCard = aFightCards.get(String.valueOf(i));
			if(fightCard!=null&&fightCard.gethP()<=0){
				UserSoul userSoul = fightCard.getSoul();
				if(userSoul!=null){
					Soul soul=soulManager.getSoulById(userSoul.getSoulId());
					if(soul.getSoulType()==10){
						opp = new Opp();
						opp.setOpp(1091);
						opp.setUUID(fightCard.getUUID());
						opp.setTarget(fightCard.getUUID());
						opp.setValue(soul.getSoulId());
						opps.add(opp);
						List<String> strs = this.getIndexRandom(dFightCards, 1);
						FightCard fightCard2 = this.getFightCardByUuidFromFightRegion(strs.get(0), dFightCards);
						this.attackCards(aPlayer, dPlayer, fightCard, aFightCards, fightCard2, dFightCards, soul.getHp());
					}
				}
				int index = this.getIndex(aFightCards, fightCard);
				opps.addAll(this.cardDieToTumb(dPlayer, aPlayer, dFightCards.get(String.valueOf(index)), dFightCards, fightCard, aFightCards));
				opp = new Opp();
				opp.setOpp(1003);
				opp.setUUID(fightCard.getUUID());
				opp.setTarget(fightCard.getUUID());
				opps.add(opp);
				aTumbCards.add(fightCard);
				aFightCards.remove(String.valueOf(i));
				opps.addAll(this.cardDieToTumbAfter(dPlayer, aPlayer, dFightCards.get(String.valueOf(index)), dFightCards, fightCard, aFightCards));
			}
		}
		for(int i=0;i<=this.getLastIndexFromFightregion(dFightCards);i++){
			fightCard = dFightCards.get(String.valueOf(i));
			if(fightCard!=null&&fightCard.gethP()<=0){
				UserSoul userSoul = fightCard.getSoul();
				if(userSoul!=null){
					Soul soul=soulManager.getSoulById(userSoul.getSoulId());
					if(soul.getSoulType()==10){
						List<String> strs = this.getIndexRandom(dFightCards, 1);
						FightCard fightCard2 = this.getFightCardByUuidFromFightRegion(strs.get(0), aFightCards);
						this.attackCards(dPlayer, aPlayer, fightCard, dFightCards, fightCard2, aFightCards, soul.getHp());
					}
				}
				int index = this.getIndex(aFightCards, fightCard);
				opps.addAll(this.cardDieToTumb(aPlayer, dPlayer, aFightCards.get(String.valueOf(index)), aFightCards, fightCard, dFightCards));
				opp = new Opp();
				opp.setOpp(1003);
				opp.setUUID(fightCard.getUUID());
				opp.setTarget(fightCard.getUUID());
				opps.add(opp);
				dTumbCards.add(fightCard);
				dFightCards.remove(String.valueOf(i));
				opps.addAll(this.cardDieToTumbAfter(aPlayer, dPlayer, aFightCards.get(String.valueOf(index)), aFightCards, fightCard, dFightCards));
			}
		}
		return opps;
	}
	private List<Opp> cardDieToTumbAfter(Player aPlayer,Player dPlayer,FightCard aFightCard,Map<String,FightCard> aFightCards,FightCard dFightCard,Map<String,FightCard> dFightCards){
		List<Opp> opps = new ArrayList<Opp>();
		if(dFightCard==null){
			return opps;
		}
		Random rnd = new Random();
		Opp opp;
		List<Skill> skills = new ArrayList<Skill>();
		Card card = cardManager.getCardById(new Long(dFightCard.getCardId()));
		if(card.getSkill()>0){
			Skill skill = this.getSkillById(card.getSkill());
			skills.add(skill);
		}
		if(dFightCard.getLevel()>=5){
			Skill skill = this.getSkillById(card.getLockSkill1());
			skills.add(skill);
		}
		if(dFightCard.getLevel()>=10){
			Skill skill = this.getSkillById(card.getLockSkill2());
			skills.add(skill);
		}
		if(dFightCard.getSkillNew()!=null){
			skills.addAll(dFightCard.getSkillNew());
		}
		for(int i=0;i<skills.size();i++){
			Skill skill = skills.get(i);
			if(skill.getLanchType()==8){
				if(skill.getAffectType()==27){
					if(rnd.nextInt(100)<skill.getAffectValue()){
						FightCard dFightCard1 = this.getFightCardByUuid(dFightCard.getUUID(), dPlayer.getCards());
						if(dFightCard1!=null){
							opp = new Opp();
							opp.setOpp(skill.getType());
							opp.setUUID(dFightCard.getUUID());
							opp.setTarget(dFightCard.getUUID());
							opp.setValue(0);
							opps.add(opp);
							dPlayer.getTumbCards().remove(dFightCard);
							if(dPlayer.getPreCards().size()<5){
								opp = new Opp();
								opp.setOpp(1005);
								opp.setUUID(dFightCard.getUUID());
								opp.setTarget(dFightCard.getUUID());
								opp.setValue(0);	
								opps.add(opp);
								dPlayer.getPreCards().add(dFightCard1);
							}else{
								opp = new Opp();
								opp.setOpp(1007);
								opp.setUUID(dFightCard.getUUID());
								opp.setTarget(dFightCard.getUUID());
								opp.setValue(0);
								opps.add(opp);
								dPlayer.getHandsCards().add(dFightCard1);
							}
						}

					}
				}
			}
		}
		return opps;
	}
	private List<Opp> cardDieToTumb(Player aPlayer,Player dPlayer,FightCard aFightCard,Map<String,FightCard> aFightCards,FightCard dFightCard,Map<String,FightCard> dFightCards){
		List<Opp> opps = new ArrayList<Opp>();
		if(dFightCard==null){
			return opps;
		}
		Random rnd = new Random();
		Opp opp;
		List<Skill> skills = new ArrayList<Skill>();
		Card card = cardManager.getCardById(new Long(dFightCard.getCardId()));
		if(card.getSkill()>0){
			Skill skill = this.getSkillById(card.getSkill());
			skills.add(skill);
		}
		if(dFightCard.getLevel()>=5){
			Skill skill = this.getSkillById(card.getLockSkill1());
			skills.add(skill);
		}
		if(dFightCard.getLevel()>=10){
			Skill skill = this.getSkillById(card.getLockSkill2());
			skills.add(skill);
		}
		if(dFightCard.getSkillNew()!=null){
			skills.addAll(dFightCard.getSkillNew());
		}
		for(int i=0;i<skills.size();i++){
			Skill skill = skills.get(i);
			if(skill.getLanchType()==9){
				if(skill.getAffectType()==31){
					//卡牌先自爆
					opp = new Opp();
					opp.setOpp(skill.getType());
					opp.setUUID(dFightCard.getUUID());
					opp.setTarget(dFightCard.getUUID());
					opp.setValue(0);
					opps.add(opp);
					
					Opp oppNew = new Opp();
					oppNew.setOpp(1030);
					oppNew.setUUID(dFightCard.getUUID());
					oppNew.setTarget("");
					opps.add(oppNew);
					int val = skill.getAffectValue();
					int index = this.getIndex(dFightCards, dFightCard);
					if(aFightCards.get(String.valueOf(index-1))!=null){
						oppNew.setTarget(oppNew.getTarget().concat(aFightCards.get(String.valueOf(index-1)).getUUID()).concat(","));
						opps.addAll(this.attackCardsRel2(dFightCard, dFightCards, aFightCards.get(String.valueOf(index-1)), aFightCards, val));
					}
					if(aFightCards.get(String.valueOf(index))!=null){
						oppNew.setTarget(oppNew.getTarget().concat(aFightCards.get(String.valueOf(index)).getUUID()).concat(","));
						opps.addAll(this.attackCardsRel2(dFightCard, dFightCards, aFightCards.get(String.valueOf(index)), aFightCards, val));
					}
					if(aFightCards.get(String.valueOf(index+1))!=null){
						oppNew.setTarget(oppNew.getTarget().concat(aFightCards.get(String.valueOf(index+1)).getUUID()).concat(","));
						opps.addAll(this.attackCardsRel2(dFightCard, dFightCards, aFightCards.get(String.valueOf(index+1)), aFightCards, val));
					}
				}
				if(skill.getAffectType()==53){
					if(skill.getType()==79){
						Skill skillNew  = this.getSkillById(skill.getAffectValue());
						int val =skillNew.getAffectValue()+rnd.nextInt(skillNew.getAffectValue());
						Opp oppNew = new Opp();
						oppNew.setOpp(skillNew.getType());
						oppNew.setUUID(dFightCard.getUUID());
						oppNew.setTarget("");
						opps.add(oppNew);
						FightCard fightCard;
						for(int j=0;j<=this.getLastIndexFromFightregion(aFightCards);j++){
							fightCard = aFightCards.get(String.valueOf(j));
							if(fightCard!=null){
								oppNew.setTarget(oppNew.getTarget().concat(fightCard.getUUID()).concat(","));
								opps.addAll(this.attackCardsRel2(dFightCard, dFightCards, fightCard, aFightCards, val));
							}
						}
					}
					if(skill.getType()==80){
						Skill skillNew  = this.getSkillById(skill.getAffectValue());
						int val =skillNew.getAffectValue();
						Opp oppNew = new Opp();
						oppNew.setOpp(skillNew.getType());
						oppNew.setUUID(dFightCard.getUUID());
						oppNew.setTarget("");
						opps.add(oppNew);
						opp =new Opp();
						opp.setOpp(1102);
						opp.setUUID(dFightCard.getUUID());
						opp.setTarget("");
						opp.setValue(1);
						opps.add(opp);
						FightCard fightCard;
						for(int j=0;j<=this.getLastIndexFromFightregion(aFightCards);j++){
							fightCard = aFightCards.get(String.valueOf(j));
							if(fightCard!=null){
								oppNew.setTarget(oppNew.getTarget().concat(fightCard.getUUID()).concat(","));
								opps.addAll(this.attackCardsRel2(dFightCard, dFightCards, fightCard, aFightCards, val));
								if(rnd.nextInt(100)<skillNew.getAffectValue2()){
									opp.setTarget(opp.getTarget().concat(fightCard.getUUID()).concat(","));
									fightCard.getSkillBuff().add(skillNew);
								}
							}
						}
					}
					if(skill.getType()==81){
						Skill skillNew  = this.getSkillById(skill.getAffectValue());
						int val =skillNew.getAffectValue();
						Opp oppNew = new Opp();
						oppNew.setOpp(skillNew.getType());
						oppNew.setUUID(dFightCard.getUUID());
						oppNew.setTarget("");
						opps.add(oppNew);
						opp =new Opp();
						opp.setOpp(1103);
						opp.setUUID(dFightCard.getUUID());
						opp.setTarget("");
						opp.setValue(1);
						opps.add(opp);
						FightCard fightCard;
						for(int j=0;j<=this.getLastIndexFromFightregion(aFightCards);j++){
							fightCard = aFightCards.get(String.valueOf(j));
							if(fightCard!=null){
								oppNew.setTarget(oppNew.getTarget().concat(fightCard.getUUID()).concat(","));
								opps.addAll(this.attackCardsRel2(dFightCard, dFightCards, fightCard, aFightCards, val));
								if(rnd.nextInt(100)<skillNew.getAffectValue2()){
									opp.setTarget(opp.getTarget().concat(fightCard.getUUID()).concat(","));
									fightCard.getSkillBuff().add(skillNew);
								}
							}
						}
					}
					
					if(skill.getType()==82){
						Skill skillNew  = this.getSkillById(skill.getAffectValue());
						int val =skillNew.getAffectValue();
						Opp oppNew = new Opp();
						oppNew.setOpp(skillNew.getType());
						oppNew.setUUID(dFightCard.getUUID());
						oppNew.setTarget("");
						opps.add(oppNew);
						opp =new Opp();
						opp.setOpp(1104);
						opp.setUUID(dFightCard.getUUID());
						opp.setTarget("");
						opp.setValue(1);
						opps.add(opp);
						FightCard fightCard;
						for(int j=0;j<=this.getLastIndexFromFightregion(aFightCards);j++){
							fightCard = aFightCards.get(String.valueOf(j));
							if(fightCard!=null){
								oppNew.setTarget(oppNew.getTarget().concat(fightCard.getUUID()).concat(","));
								opps.addAll(this.attackCardsRel2(dFightCard, dFightCards, fightCard, aFightCards, val));
								opp.setTarget(opp.getTarget().concat(fightCard.getUUID()).concat(","));
								fightCard.getSkillBuff().add(skillNew);
							}
						}
					}
					if(skill.getType()==83){
						Skill skillNew  = this.getSkillById(skill.getAffectValue());
						int val =skillNew.getAffectValue();
						Opp oppNew = new Opp();
						oppNew.setOpp(skillNew.getType());
						oppNew.setUUID(dFightCard.getUUID());
						oppNew.setTarget("");
						opps.add(oppNew);
						FightCard fightCard;
						for(int j=0;j<=this.getLastIndexFromFightregion(aFightCards);j++){
							fightCard = aFightCards.get(String.valueOf(j));
							if(fightCard!=null){
								oppNew.setTarget(oppNew.getTarget().concat(fightCard.getUUID()).concat(","));
								int valskill = val;
								if(fightCard.getAttack()<val){
									valskill = fightCard.getAttack();
								}
								opp =new Opp();
								opp.setOpp(1020);
								opp.setUUID(dFightCard.getUUID());
								opp.setTarget(fightCard.getUUID());
								opp.setValue(-valskill);
								fightCard.setAttack(fightCard.getAttack()-valskill);
								opps.addAll(this.attackCardsRel2(dFightCard, dFightCards, fightCard, aFightCards, val));		
							}
						}
					}
					if(skill.getType()==84){
						Skill skillNew  = this.getSkillById(skill.getAffectValue());
						int index = this.getMaxLoseHpCard(dPlayer.getCards(), dFightCards);
						if(index>-1){
							FightCard fightCard = dFightCards.get(String.valueOf(index));
							int val =skillNew.getAffectValue();
							opp = new Opp();
							opp.setOpp(skillNew.getType());
							opp.setUUID(dFightCard.getUUID());
							opp.setTarget(fightCard.getUUID());
							opps.add(opp);
							opp = new Opp();
							opp.setOpp(1040);
							opp.setUUID(dFightCard.getUUID());
							opp.setTarget(fightCard.getUUID());
							opp.setValue(val);
							opps.add(opp);
							fightCard.sethP(fightCard.gethP()+val);
						}

					}
					if(skill.getType()==85){
						Skill skillNew  = this.getSkillById(skill.getAffectValue());
						int val =skillNew.getAffectValue();
						Opp oppNew = new Opp();
						oppNew.setOpp(skillNew.getType());
						oppNew.setUUID(dFightCard.getUUID());
						oppNew.setTarget("");
						opps.add(oppNew);
						FightCard fightCard;
						for(int j=0;j<=this.getLastIndexFromFightregion(dFightCards);j++){
							fightCard = dFightCards.get(String.valueOf(j));
							if(fightCard!=null&&!fightCard.getUUID().equals(dFightCard.getUUID())){
								oppNew.setTarget(oppNew.getTarget().concat(fightCard.getUUID()).concat(","));
								opp =new Opp();
								opp.setOpp(1040);
								opp.setUUID(dFightCard.getUUID());
								opp.setTarget(fightCard.getUUID());
								opp.setValue(val);
								fightCard.sethP(fightCard.gethP()+val);
							}
						}
					}
					if(skill.getType()==86){
						Skill skillNew  = this.getSkillById(skill.getAffectValue());
						int val =skillNew.getAffectValue();
						opp = new Opp();
						opp.setOpp(skillNew.getType());
						opp.setUUID(dFightCard.getUUID());
						opp.setTarget(dPlayer.getName());
						opps.add(opp);
						opp = new Opp();
						opp.setOpp(1040);
						opp.setUUID(dFightCard.getUUID());
						opp.setTarget(dPlayer.getName());
						opp.setValue(val);
						opps.add(opp);
						dPlayer.setHP(dPlayer.getHP()+val);
					}
					if(skill.getType()==87){
						Skill skillNew  = this.getSkillById(skill.getAffectValue());
						int val =skillNew.getAffectValue();
						opp = new Opp();
						opp.setOpp(skillNew.getType());
						opp.setUUID(dFightCard.getUUID());
						opp.setTarget(aPlayer.getName());
						opps.add(opp);
//						opp = new Opp();
//						opp.setOpp(1022);
//						opp.setUUID(dFightCard.getUUID());
//						opp.setTarget(aPlayer.getName());
//						opp.setValue(-val);
//						opps.add(opp);
//						aPlayer.setHP(aPlayer.getHP()-val);
						opps.addAll(this.delPlayerHp(dFightCard, val,aFightCards,aPlayer));
					}
					if(skill.getType()==88){
						Skill skillNew  = this.getSkillById(skill.getAffectValue());
						int val =skillNew.getAffectValue();
						Opp oppNew = new Opp();
						oppNew.setOpp(skillNew.getType());
						oppNew.setUUID(dFightCard.getUUID());
						oppNew.setTarget("");
						opps.add(oppNew);
						FightCard fightCard;
						for(int j=0;j<=this.getLastIndexFromFightregion(aFightCards);j++){
							fightCard = aFightCards.get(String.valueOf(j));
							if(fightCard!=null){
								oppNew.setTarget(oppNew.getTarget().concat(fightCard.getUUID()).concat(","));
								int valskill = val;
								if(fightCard.getAttack()<val){
									valskill = fightCard.getAttack();
								}
								opp =new Opp();
								opp.setOpp(1020);
								opp.setUUID(dFightCard.getUUID());
								opp.setTarget(fightCard.getUUID());
								opp.setValue(-valskill);
								fightCard.setAttack(fightCard.getAttack()-valskill);		
								opps.add(opp);
							}
						}
					}
					if(skill.getType()==89){
						Skill skillNew  = this.getSkillById(skill.getAffectValue());
						Opp oppNew = new Opp();
						oppNew.setOpp(skillNew.getType());
						oppNew.setUUID(dFightCard.getUUID());
						oppNew.setTarget("");
						opps.add(oppNew);
						opp =new Opp();
						opp.setOpp(1107);
						opp.setUUID(dFightCard.getUUID());
						opp.setTarget("");
						opp.setValue(1);
						opps.add(opp);
						FightCard fightCard;
						for(int j=0;j<=this.getLastIndexFromFightregion(aFightCards);j++){
							fightCard = aFightCards.get(String.valueOf(j));
							if(fightCard!=null){
								oppNew.setTarget(oppNew.getTarget().concat(fightCard.getUUID()).concat(","));
								opp.setTarget(opp.getTarget().concat(fightCard.getUUID()).concat(","));
								fightCard.getSkillBuff().add(skillNew);
							}
						}
					}
					if(skill.getType()==90){
						Opp oppNew = new Opp();
						oppNew.setOpp(skill.getType());
						oppNew.setUUID(dFightCard.getUUID());
						oppNew.setTarget("");
						opps.add(oppNew);
						int j=0;
						for(int c=0;c<=this.getLastIndexFromFightregion(aFightCards);c++){
							if(j<skill.getAffectValue()&&aFightCards.get(String.valueOf(c))!=null){
								int s = rnd.nextInt(100);
								if(s<65){
									opp = new Opp();
									opp.setOpp(1101);
									opp.setTarget(aFightCards.get(String.valueOf(c)).getUUID());
									opp.setUUID(dFightCard.getUUID());
									opp.setValue(1);
									opps.add(opp);
									oppNew.setTarget(oppNew.getTarget().concat(dFightCards.get(String.valueOf(c)).getUUID()).concat(","));
									dFightCards.get(String.valueOf(c)).getSkillBuff().add(skill);
								}
								j++;
							}

						}
					}
				}
			}
			//卡牌上场时给卡牌增加的攻击力恢复
			if(skill.getLanchType()==6){
				FightCard fightCard2;
				int val = skill.getAffectValue();
				Card card2;
				for(int j=0;j<this.getLastIndexFromFightregion(dFightCards);j++){
					fightCard2 =dFightCards.get(String.valueOf(j));
					if(fightCard2!=null&&fightCard2.getUUID()!=dFightCard.getUUID()){
						if(skill.getAffectType()==24){
							card2 = cardManager.getCardById(new Long(fightCard2.getCardId()));
							if(skill.getAffectValue2()==card2.getRace()){
								opp = new Opp();
								opp.setOpp(1020);
								opp.setUUID(dFightCard.getUUID());
								opp.setTarget(fightCard2.getUUID());
								opp.setValue(-val);
								opps.add(opp);
								fightCard2.setAttack(fightCard2.getAttack()-val);
							}
							if(skill.getAffectValue2()==5){
								opp = new Opp();
								opp.setOpp(1020);
								opp.setUUID(dFightCard.getUUID());
								opp.setTarget(fightCard2.getUUID());
								opp.setValue(-val);
								opps.add(opp);
								fightCard2.setAttack(fightCard2.getAttack()-val);
							}
						}
					}
					
					
				}
			}
		}
		return opps;
	}
	//物理攻击
	private List<Opp> attackCards(Player aPlayer,Player dPlayer,FightCard aFightCard,Map<String,FightCard> aFightCards,FightCard dFightCard,Map<String,FightCard> dFightCards,int val){
		Random rnd = new Random();
		List<Opp> opps = new ArrayList<Opp>();
		Opp opp;
		List<Skill> skills = new ArrayList<Skill>();
		List<Skill> skills1 = new ArrayList<Skill>();
		Card card = cardManager.getCardById(new Long(aFightCard.getCardId()));
		Card card1 = cardManager.getCardById(new Long(dFightCard.getCardId()));
		if(card.getSkill()>0){
			Skill skill = this.getSkillById(card.getSkill());
			skills.add(skill);
		}
		if(aFightCard.getLevel()>=5){
			Skill skill = this.getSkillById(card.getLockSkill1());
			skills.add(skill);
		}
		if(aFightCard.getLevel()>=10){
			Skill skill = this.getSkillById(card.getLockSkill2());
			skills.add(skill);
		}
		if(aFightCard.getSkillNew()!=null){
			skills.addAll(aFightCard.getSkillNew());
		}
		
		if(card1.getSkill()>0){
			Skill skill = this.getSkillById(card1.getSkill());
			skills1.add(skill);
		}
		if(dFightCard.getLevel()>=5){
			Skill skill = this.getSkillById(card1.getLockSkill1());
			skills1.add(skill);
		}
		if(dFightCard.getLevel()>=10){
			Skill skill = this.getSkillById(card1.getLockSkill2());
			skills1.add(skill);
		}
		if(dFightCard.getSkillNew()!=null){
			skills1.addAll(dFightCard.getSkillNew());
		}
		//播放物理攻击动画
		opps.addAll(this.attackCardsRel1(aFightCard, aFightCards, dFightCard, dFightCards, val));
		//如果被攻击卡牌有防守技能则释放防守技能
		for(int i=0;i<skills1.size();i++){
			Skill skill = skills1.get(i);
			if(skill.getLanchType()==5){

				if(skill.getAffectType()==19){
					opp = new Opp();
					opp.setOpp(skill.getType());
					opp.setUUID(dFightCard.getUUID());
					opp.setTarget(dFightCard.getUUID());
					opps.add(opp);
					val = val-skill.getAffectValue();
				}
				if(skill.getAffectType()==47){
					opp = new Opp();
					opp.setOpp(skill.getType());
					opp.setUUID(dFightCard.getUUID());
					opp.setTarget(dFightCard.getUUID());
					opps.add(opp);
					if(val>skill.getAffectValue()){
						val = skill.getAffectValue();
					}
				}
				if(skill.getAffectType()==55){
					
					if(rnd.nextInt(100)<skill.getAffectValue()){
						opp = new Opp();
						opp.setOpp(skill.getType());
						opp.setUUID(dFightCard.getUUID());
						opp.setTarget(dFightCard.getUUID());
						opps.add(opp);
						val = 0;
					}
				}
				
			}
		}
		UserSoul userSoul = dFightCard.getSoul();
		//卡牌被物理攻击后如果卡牌装备了武器，则先减去武器抵挡的攻击
		if(userSoul!=null){
			Soul soul = soulManager.getSoulById(userSoul.getSoulId()); 
			//物理防御型武器
			if(soul.getSoulType()==2&&dFightCard.getSoulRound()>0){
				opp = new Opp();
				opp.setOpp(1091);
				opp.setUUID(dFightCard.getUUID());
				opp.setTarget(dFightCard.getUUID());
				opp.setValue(soul.getSoulId());
				opps.add(opp);
				val = val - soul.getHp();
				dFightCard.setSoulRound(dFightCard.getSoulRound()-1);
				if(dFightCard.getSoulRound()==0){
					//如果武器回合数用完则武器失效
					opp = new Opp();
					opp.setOpp(1090);
					opp.setUUID(dFightCard.getUUID());
					opp.setTarget(dFightCard.getUUID());
					opps.add(opp);
				}
			}
			//金钟罩
			if(soul.getSoulType()==7&&dFightCard.getSoulRound()>0){
				if(dFightCard.gethP()<this.getFightCardInitHp(dPlayer.getCards(), dFightCard)*3/10){
					opp = new Opp();
					opp.setOpp(1091);
					opp.setUUID(dFightCard.getUUID());
					opp.setTarget(dFightCard.getUUID());
					opp.setValue(soul.getSoulId());
					opps.add(opp);
					val = 0;
					dFightCard.setSoulRound(dFightCard.getSoulRound()-1);
					if(dFightCard.getSoulRound()==0){
						//如果武器回合数用完则武器失效
						opp = new Opp();
						opp.setOpp(1090);
						opp.setUUID(dFightCard.getUUID());
						opp.setTarget(dFightCard.getUUID());
						opps.add(opp);
					}
				}
			}


		}
		if(val<0){
			val=0;
		}
		//真实攻击减血
		opps.addAll(this.attackCardsRel2(aFightCard, aFightCards, dFightCard, dFightCards, val));
		if(val>0){
			UserSoul userSoul2 = aFightCard.getSoul();
			//装备了物理吸血武器
			if(userSoul2!=null){
				Soul soul = soulManager.getSoulById(userSoul2.getSoulId()); 
				if(soul.getSoulType()==6){
					//给攻击卡牌增加吸血血量
					opp = new Opp();
					opp.setOpp(1091);
					opp.setUUID(aFightCard.getUUID());
					opp.setTarget(aFightCard.getUUID());
					opp.setValue(soul.getSoulId());
					opps.add(opp);
					
					opp = new Opp();
					opp.setOpp(1040);
					opp.setUUID(aFightCard.getUUID());
					opp.setTarget(aFightCard.getUUID());
					opp.setValue(val/20);
					opps.add(opp);
					aFightCard.sethP(aFightCard.gethP()+val/20);
					aFightCard.setSoulRound(aFightCard.getSoulRound()-1);
					if(aFightCard.getSoulRound()==0){
						//如果武器回合数用完则武器失效
						opp = new Opp();
						opp.setOpp(1090);
						opp.setUUID(aFightCard.getUUID());
						opp.setTarget(aFightCard.getUUID());
						opps.add(opp);
					}
				}
				//每击杀一张卡牌，则增加本身攻击力50点，可叠加2层
				if(soul.getSoulType()==8&&aFightCard.getSoulRound()>0){
					if(dFightCard.gethP()<=0){
						aFightCard.setSoulRound(aFightCard.getSoulRound()-1);
						opp = new Opp();
						opp.setOpp(1091);
						opp.setUUID(aFightCard.getUUID());
						opp.setTarget(aFightCard.getUUID());
						opp.setValue(soul.getSoulId());
						opps.add(opp);
						opp = new Opp();
						opp.setOpp(1020);
						opp.setUUID(aFightCard.getUUID());
						opp.setTarget(aFightCard.getUUID());
						opp.setValue(soul.getAttack());
						opps.add(opp);
						aFightCard.setAttack(aFightCard.getAttack()+soul.getAttack());
						if(aFightCard.getSoulRound()==0){
							//如果武器回合数用完则武器失效
							opp = new Opp();
							opp.setOpp(1090);
							opp.setUUID(aFightCard.getUUID());
							opp.setTarget(aFightCard.getUUID());
							opps.add(opp);
						}
					}
					
				}
				


			}
			for(int i=0;i<skills1.size();i++){
				Skill skill = skills1.get(i);
				if(skill.getLanchType()==3){
					if(skill.getAffectType()==3){
						opp = new Opp();
						opp.setOpp(skill.getType());
						opp.setUUID(dFightCard.getUUID());
						opp.setTarget(aFightCard.getUUID());
						opps.add(opp);
						opp = new Opp();
						opp.setOpp(1040);
						opp.setUUID(dFightCard.getUUID());
						opp.setTarget(aFightCard.getUUID());
						opp.setValue(-skill.getAffectValue());
						opps.add(opp);
						aFightCard.sethP(aFightCard.gethP()-skill.getAffectValue());
					}
					if(skill.getAffectType()==4){
						Opp oppNew = new Opp();
						oppNew.setOpp(skill.getType());
						oppNew.setUUID(dFightCard.getUUID());
						oppNew.setTarget("");
						opps.add(oppNew);
						int index = this.getIndex(dFightCards, dFightCard);
						//对左侧
						if(aFightCards.get(String.valueOf(index-1))!=null){
							opp = new Opp();
							opp.setOpp(1040);
							opp.setUUID(dFightCard.getUUID());
							opp.setTarget(aFightCards.get(String.valueOf(index-1)).getUUID());
							opp.setValue(-skill.getAffectValue());
							opps.add(opp);
							oppNew.setTarget(oppNew.getTarget().concat(aFightCards.get(String.valueOf(index-1)).getUUID())+",");
							aFightCards.get(String.valueOf(index-1)).sethP(aFightCards.get(String.valueOf(index-1)).gethP()-skill.getAffectValue());
						}
						//对正面
						opp = new Opp();
						opp.setOpp(1040);
						opp.setUUID(dFightCard.getUUID());
						opp.setTarget(aFightCards.get(String.valueOf(index)).getUUID());
						opp.setValue(-skill.getAffectValue());
						opps.add(opp);
						oppNew.setTarget(oppNew.getTarget().concat(aFightCards.get(String.valueOf(index)).getUUID())+",");
						aFightCards.get(String.valueOf(index)).sethP(aFightCards.get(String.valueOf(index)).gethP()-skill.getAffectValue());
						//对右侧
						if(aFightCards.get(String.valueOf(index+1))!=null){
							opp = new Opp();
							opp.setOpp(1040);
							opp.setUUID(dFightCard.getUUID());
							opp.setTarget(aFightCards.get(String.valueOf(index+1)).getUUID());
							opp.setValue(-skill.getAffectValue());
							opps.add(opp);
							oppNew.setTarget(oppNew.getTarget().concat(aFightCards.get(String.valueOf(index+1)).getUUID())+",");
							aFightCards.get(String.valueOf(index+1)).sethP(aFightCards.get(String.valueOf(index+1)).gethP()-skill.getAffectValue());
						}
					}
					if(skill.getAffectType()==36){
						opp = new Opp();
						opp.setOpp(skill.getType());
						opp.setUUID(dFightCard.getUUID());
						opp.setTarget(dFightCard.getUUID());
						opps.add(opp);
						opp = new Opp();
						opp.setOpp(1020);
						opp.setUUID(dFightCard.getUUID());
						opp.setTarget(dFightCard.getUUID());
						opp.setValue(skill.getAffectValue());
						opps.add(opp);
						dFightCard.setAttack(dFightCard.getAttack()+skill.getAffectValue());
						
					}
					if(skill.getAffectType()==45){
						opp = new Opp();
						opp.setOpp(skill.getType());
						opp.setUUID(dFightCard.getUUID());
						opp.setTarget(aFightCard.getUUID());
						opps.add(opp);
						opp = new Opp();
						opp.setOpp(1107);
						opp.setUUID(dFightCard.getUUID());
						opp.setValue(1);
						opp.setTarget(aFightCard.getUUID());
						opps.add(opp);
						aFightCard.getSkillBuff().add(skill);
					}
				}
			}
			for(int i=0;i<skills.size();i++){
				Skill skill = skills.get(i);
				if(skill.getLanchType()==1){
					if(skill.getAffectType()==23){
						//分身合击
						Opp oppNew = new Opp();
						oppNew.setOpp(skill.getType());
						oppNew.setUUID(aFightCard.getUUID());
						oppNew.setTarget("");
						opps.add(oppNew);
						int index = this.getIndex(aFightCards, aFightCard);
						//对左侧
						if(dFightCards.get(String.valueOf(index-1))!=null){
							oppNew.setTarget(oppNew.getTarget().concat(dFightCards.get(String.valueOf(index-1)).getUUID())+",");
							if(!hasImmunitySkill(dFightCards.get(String.valueOf(index-1)))){
								opp = new Opp();
								opp.setOpp(1040);
								opp.setUUID(aFightCard.getUUID());
								opp.setTarget(dFightCards.get(String.valueOf(index-1)).getUUID());
								opp.setValue(-val);
								opps.add(opp);
								dFightCards.get(String.valueOf(index-1)).sethP(dFightCards.get(String.valueOf(index-1)).gethP()-val);
							}else{
								Skill skillImmunity = this.getSkillById(689);
								opp = new Opp();
								opp.setOpp(skillImmunity.getType());
								opp.setUUID(dFightCards.get(String.valueOf(index-1)).getUUID());
								opp.setTarget(dFightCards.get(String.valueOf(index-1)).getUUID());
								opps.add(opp);
							}
						}
						//对右侧
						if(dFightCards.get(String.valueOf(index+1))!=null){
							oppNew.setTarget(oppNew.getTarget().concat(dFightCards.get(String.valueOf(index+1)).getUUID())+",");
							if(!hasImmunitySkill(dFightCards.get(String.valueOf(index+1)))){
								opp = new Opp();
								opp.setOpp(1040);
								opp.setUUID(aFightCard.getUUID());
								opp.setTarget(dFightCards.get(String.valueOf(index+1)).getUUID());
								opp.setValue(-val);
								opps.add(opp);
								dFightCards.get(String.valueOf(index+1)).sethP(dFightCards.get(String.valueOf(index+1)).gethP()-val);
							}else{
								Skill skillImmunity = this.getSkillById(689);
								opp = new Opp();
								opp.setOpp(skillImmunity.getType());
								opp.setUUID(dFightCards.get(String.valueOf(index+1)).getUUID());
								opp.setTarget(dFightCards.get(String.valueOf(index+1)).getUUID());
								opps.add(opp);
							}
						}
					}
				}
				if(skill.getLanchType()==2){
					if(skill.getAffectType()==2){
						//当对敌方卡牌造成物理伤害时，回复伤害值10%的生命值。
						opp = new Opp();
						opp.setOpp(skill.getType());
						opp.setUUID(aFightCard.getUUID());
						opp.setTarget(aFightCard.getUUID());
						opps.add(opp);
						opp = new Opp();
						opp.setOpp(1040);
						opp.setUUID(aFightCard.getUUID());
						opp.setTarget(aFightCard.getUUID());
						opp.setValue(val*skill.getAffectValue()/100);
						opps.add(opp);
						aFightCard.sethP(aFightCard.gethP()+val*skill.getAffectValue()/100);
					}
					if(skill.getAffectType()==28){
						//当对敌方造成物理伤害后，降低对方10点攻击力。
						opp = new Opp();
						opp.setOpp(skill.getType());
						opp.setUUID(aFightCard.getUUID());
						opp.setTarget(dFightCard.getUUID());
						opps.add(opp);
						if(!hasImmunitySkill(dFightCard)){
							int valskill = skill.getAffectValue();
							if(dFightCard.getAttack()<valskill){
								valskill = dFightCard.getAttack();
							}
							opp = new Opp();
							opp.setOpp(1020);
							opp.setUUID(aFightCard.getUUID());
							opp.setTarget(dFightCard.getUUID());
							opp.setValue(-valskill);
							opps.add(opp);
							dFightCard.setAttack(dFightCard.getAttack()-valskill);
						}else{
							Skill skillImmunity = this.getSkillById(689);
							opp = new Opp();
							opp.setOpp(skillImmunity.getType());
							opp.setUUID(dFightCard.getUUID());
							opp.setTarget(dFightCard.getUUID());
							opps.add(opp);
						}

					}
					if(skill.getAffectType()==35){
						//当攻击并对敌方造成伤害时，提升10点攻击力。
						opp = new Opp();
						opp.setOpp(skill.getType());
						opp.setUUID(aFightCard.getUUID());
						opp.setTarget(aFightCard.getUUID());
						opps.add(opp);
						opp = new Opp();
						opp.setOpp(1020);
						opp.setUUID(aFightCard.getUUID());
						opp.setTarget(aFightCard.getUUID());
						opp.setValue(skill.getAffectValue());
						opps.add(opp);
						aFightCard.setAttack(aFightCard.getAttack()+skill.getAffectValue());
					}
					if(skill.getAffectType()==37){
						//当攻击并对目标造成物理伤害时，对方丧失10点攻击力和生命值。
						opp = new Opp();
						opp.setOpp(skill.getType());
						opp.setUUID(aFightCard.getUUID());
						opp.setTarget(dFightCard.getUUID());
						opps.add(opp);
						if(!hasImmunitySkill(dFightCard)){
							opp = new Opp();
							opp.setOpp(1020);
							opp.setUUID(aFightCard.getUUID());
							opp.setTarget(dFightCard.getUUID());
							opp.setValue(-skill.getAffectValue());
							opps.add(opp);
							dFightCard.setAttack(dFightCard.getAttack()-skill.getAffectValue());
							opp = new Opp();
							opp.setOpp(1040);
							opp.setUUID(aFightCard.getUUID());
							opp.setTarget(dFightCard.getUUID());
							opp.setValue(-skill.getAffectValue());
							opps.add(opp);
							dFightCard.sethP(dFightCard.gethP()-skill.getAffectValue());
						}else{
							Skill skillImmunity = this.getSkillById(689);
							opp = new Opp();
							opp.setOpp(skillImmunity.getType());
							opp.setUUID(dFightCard.getUUID());
							opp.setTarget(dFightCard.getUUID());
							opps.add(opp);
						}
						
					}
					if(skill.getAffectType()==49){
						//当攻击并对敌方造成物理伤害时施放，使对方无法回春和被治疗
//						dFightCard.getSkillNew().add(skill);
						if(!hasImmunitySkill(dFightCard)){
							opp = new Opp();
							opp.setOpp(skill.getType());
							opp.setUUID(dFightCard.getUUID());
							opp.setTarget(dFightCard.getUUID());
							opps.add(opp);
							dFightCard.getSkillBuff().add(skill);
						}else{
							Skill skillImmunity = this.getSkillById(689);
							opp = new Opp();
							opp.setOpp(skillImmunity.getType());
							opp.setUUID(dFightCard.getUUID());
							opp.setTarget(dFightCard.getUUID());
							opps.add(opp);
						}
					}
					if(skill.getAffectType()==58){
						//当对敌方卡牌造成伤害时，敌方英雄同时受到15%的伤害。
						opp = new Opp();
						opp.setOpp(skill.getType());
						opp.setUUID(aFightCard.getUUID());
						opp.setTarget(dPlayer.getName());
						opps.add(opp);
						opp = new Opp();
						opps.addAll(this.delPlayerHp(aFightCard, val*skill.getAffectType()/100, dFightCards,dPlayer));
					}
				}
				
			}
		}
		return opps;
	}
	//物理攻击伤害动画
	private List<Opp> attackCardsRel1(FightCard aFightCard,Map<String,FightCard> aFightCards,FightCard dFightCard,Map<String,FightCard> dFightCards,int val){
//		int index = this.getIndex(aFightCards, aFightCard);
		List<Opp> opps = new ArrayList<Opp>();
		Opp opp;
		//开始物理攻击
		opp = new Opp();
		opp.setOpp(1030);
		opp.setUUID(aFightCard.getUUID());
		opp.setValue(-val);
		opp.setTarget(dFightCard.getUUID());
		opps.add(opp);
		return opps;
	}
	//物理攻击,魔法攻击伤害
	private List<Opp> attackCardsRel2(FightCard aFightCard,Map<String,FightCard> aFightCards,FightCard dFightCard,Map<String,FightCard> dFightCards,int val){
//		int index = this.getIndex(aFightCards, aFightCard);
		List<Opp> opps = new ArrayList<Opp>();
		if(dFightCard==null){
			return opps;
		}
		Opp opp;
		opp = new Opp();
		opp.setOpp(1040);
		opp.setUUID(aFightCard.getUUID());
		opp.setValue(-val);
		opp.setTarget(dFightCard.getUUID());
		opps.add(opp);
		dFightCard.sethP(dFightCard.gethP()-val);
		return opps;
	}
	//给英雄加血
	private List<Opp> addPlayerHp(FightCard fightCard,int val,Player player){
		if((player.getHP()+val)>player.getRemainHP()){
			val = player.getRemainHP()-player.getHP();
		}
		player.setHP(player.getHP()+val);
		List<Opp> opps = new ArrayList<Opp>();
		Opp opp = new Opp();
		opp.setOpp(1022);
		opp.setUUID(fightCard.getUUID());
		opp.setTarget(player.getName());
		opp.setValue(val);
		opps.add(opp);   
		return opps;
	}
	//给英雄减血
	private List<Opp> delPlayerHp(FightCard fightCard,int val,Map<String,FightCard> fightCards,Player player){
		List<Opp> opps = new ArrayList<Opp>();
		Opp opp;
		//当自己英雄受到伤害时，替代英雄扣除生命值。
		if(val>0){
			Iterator<String> ite = fightCards.keySet().iterator();
			FightCard dFightCard;
			Card card;
			String key;
			List<Skill> skillList = new ArrayList<Skill>();
			while(ite.hasNext()){
				key =ite.next();
				dFightCard =fightCards.get(key);
				card = cardManager.getCardById(new Long(fightCard.getCardId()));
				
				if(card.getSkill()>0){
					Skill skill = skillManager.getSkillById(card.getSkill());
					skillList.add(skill);
				}
				if(fightCard.getLevel()>=5){
					Skill skill = skillManager.getSkillById(card.getLockSkill1());
					skillList.add(skill);
				}
				if(fightCard.getLevel()>=10){
					Skill skill = skillManager.getSkillById(card.getLockSkill2());
					skillList.add(skill);
				}
				if(fightCard.getSkillNew()!=null){
					skillList.addAll(fightCard.getSkillNew());
				}
				for(Skill skill:skillList){
					if(skill.getLanchType()==14){
						if(dFightCard.gethP()<val){
							val = dFightCard.gethP();
						}
						opp = new Opp();
						opp.setOpp(1040);
						opp.setUUID(fightCard.getUUID());
						opp.setTarget(dFightCard.getUUID());
						opp.setValue(-val);
						opps.add(opp);  
						dFightCard.sethP(dFightCard.gethP()-val);
						return opps;
					}
				}
			}
		}
		if(player.getHP()<val){
			val = player.getHP();
		}
		player.setHP(player.getHP()-val);
		
		opp = new Opp();
		opp.setOpp(1022);
		opp.setUUID(fightCard.getUUID());
		opp.setTarget(player.getName());
		opp.setValue(-val);
		opps.add(opp);   
		return opps;
	}
	private List<Opp> magicAttackCards(Player aPlayer,Player dPlayer,FightCard aFightCard,FightCard dFightCard,Skill skill,int val){
		List<Opp> opps = new ArrayList<Opp>();
		if(dFightCard==null){
			return opps;
		}
		Opp opp;
		UserSoul userSoul = dFightCard.getSoul();
		//卡牌被物理攻击后如果卡牌装备了武器，则先减去武器抵挡的攻击
		if(userSoul!=null){
			Soul soul = soulManager.getSoulById(userSoul.getSoulId()); 
			//法术防御型武器
			if(soul.getSoulType()==3&&dFightCard.getSoulRound()>0){
				opp = new Opp();
				opp.setOpp(1091);
				opp.setUUID(dFightCard.getUUID());
				opp.setTarget(dFightCard.getUUID());
				opp.setValue(soul.getSoulId());
				opps.add(opp);
				val = val - soul.getHp();
				dFightCard.setSoulRound(dFightCard.getSoulRound()-1);
				if(dFightCard.getSoulRound()==0){
					//如果武器回合数用完则武器失效
					opp = new Opp();
					opp.setOpp(1090);
					opp.setUUID(dFightCard.getUUID());
					opp.setTarget(dFightCard.getUUID());
					opps.add(opp);
				}
			}
		}
		if(val<0){
			val = 0;
		}
		if(val==0){
			return opps;
		}
		Skill dSkill;
		List<Skill> skills = new ArrayList<Skill>();
		Card card = cardManager.getCardById(new Long(dFightCard.getCardId()));
		if(card.getSkill()>0){
			dSkill = this.getSkillById(card.getSkill());
			skills.add(dSkill);
		}
		if(dFightCard.getLevel()>=5){
			dSkill = this.getSkillById(card.getLockSkill1());
			skills.add(dSkill);
		}
		if(dFightCard.getLevel()>=10){
			dSkill = this.getSkillById(card.getLockSkill2());
			skills.add(dSkill);
		}
		if(dFightCard.getSkillNew()!=null){
			skills.addAll(dFightCard.getSkillNew());
		}
		for(int i=0;i<skills.size();i++){
			dSkill = skills.get(i);
			if(dSkill.getLanchType()==11){
				//魔法反噬
				if(dSkill.getAffectType()==41){
					val = 0;
					opp = new Opp();
					opp.setOpp(dSkill.getType());
					opp.setUUID(dFightCard.getUUID());
					opp.setTarget(dFightCard.getUUID());
					
					opps.add(opp); 
					
					opp = new Opp();
					if(aFightCard.getUUID().indexOf("rune")!=-1){
						opp.setOpp(1022);
						aPlayer.setHP(aPlayer.getHP()-dSkill.getAffectValue());
						
					}else{
						opp.setOpp(1040);
						aFightCard.sethP(aFightCard.gethP()-dSkill.getAffectValue());
					}
					
					opp.setUUID(dFightCard.getUUID());
					if(aFightCard.getUUID().indexOf("rune")!=-1){
						opp.setTarget(aPlayer.getName());
					}else{
						opp.setTarget(aFightCard.getUUID());
					}
					//血量减少dSkill.getAffectValue()
					opp.setValue(-dSkill.getAffectValue());
					opps.add(opp); 
				}
				if(dSkill.getAffectType()==50){
					if(val>dSkill.getAffectValue()){
						val = dSkill.getAffectValue();
					}
					opp = new Opp();
					opp.setOpp(dSkill.getType());
					opp.setUUID(dFightCard.getUUID());
					opp.setTarget(dFightCard.getUUID());
					opps.add(opp); 
				}
			}
		}
		dFightCard.sethP(dFightCard.gethP()-val);
		opp = new Opp();
		opp.setOpp(1040);
		opp.setUUID(aFightCard.getUUID());
		opp.setTarget(dFightCard.getUUID());
		//血量减少val
		opp.setValue(-val);
		opps.add(opp); 
		userSoul = aFightCard.getSoul();
		if(userSoul!=null){
			Soul soul = soulManager.getSoulById(userSoul.getSoulId()); 
			//每击杀一张卡牌，则增加本身攻击力50点，可叠加2层
			if(soul.getSoulType()==8&&aFightCard.getSoulRound()>0){
				if(dFightCard.gethP()<=0){
					aFightCard.setSoulRound(aFightCard.getSoulRound()-1);
					opp = new Opp();
					opp.setOpp(1020);
					opp.setUUID(aFightCard.getUUID());
					opp.setTarget(aFightCard.getUUID());
					opp.setValue(soul.getAttack());
					opps.add(opp);
					aFightCard.setAttack(aFightCard.getAttack()+soul.getAttack());
					if(dFightCard.getSoulRound()==0){
						//如果武器回合数用完则武器失效
						opp = new Opp();
						opp.setOpp(1090);
						opp.setUUID(dFightCard.getUUID());
						opp.setTarget(dFightCard.getUUID());
						opps.add(opp);
					}
				}
				
			}
		}
		return opps;
	}
	//随机获取战斗区卡牌
	private List<String> getIndexRandom(Map<String,FightCard> dFigtCards,int size){
		List<String> indexs = new ArrayList<String>();
		List<FightCard> fights = CollectionUtils.arrayToList(dFigtCards.values().toArray());
		Collections.sort(fights, new Comparator<FightCard>() {
			private final int[] vs = {-1,0,1};
			private final Random rnd = new Random(System.currentTimeMillis());
			@Override
			public int compare(FightCard arg0, FightCard arg1) {
				return vs[rnd.nextInt(vs.length)];
			}			
		});
		if(fights.size()<size){
			size = fights.size();
		}
		for(int i=0;i<size;i++){
			indexs.add(fights.get(i).getUUID());
		}
		return indexs;
	}
	//获取卡牌的索引
	private int getIndex(Map<String,FightCard> fightCards,FightCard fightCard){
		for(int i=0;i<this.getLastIndexFromFightregion(fightCards);i++){
			if(fightCards.get(String.valueOf(i))!=null&&fightCards.get(String.valueOf(i)).getUUID().equals(fightCard.getUUID())){
				return i;
			}
		}
		return -1;
	}
	//获取当前血最少的卡牌
	private String getMinHpCard(Map<String,FightCard> fightCards){
		int hp=1000000;
		String j = "-1";
		Iterator<String> ite = fightCards.keySet().iterator();
		FightCard fightCard;
		String key;
		while(ite.hasNext()){
			key =ite.next();
			fightCard =fightCards.get(key);
			if(fightCard.gethP()<hp){
				hp = fightCard.gethP();
				j=key;
			}
		}
		return j;
	}
	//获取损失生命值最多的卡牌
	private int getMaxLoseHpCard(List<FightCard> userCards,Map<String,FightCard> fightCards){
		int hp=0;
		String j = "-1";
		Iterator<String> ite = fightCards.keySet().iterator();
		while(ite.hasNext()){
			String key =ite.next();
			if(fightCards.get(key)==null){
				continue;
			}                   
			if(this.getFightCardByUuid(fightCards.get(key).getUUID(), userCards).gethP()-fightCards.get(key).gethP()>hp){
				hp = this.getFightCardByUuid(fightCards.get(key).getUUID(), userCards).gethP()-fightCards.get(key).gethP();
				j=key;
			}
		}
		return Integer.parseInt(j);
	}
	//获取当前卡牌的初始血量
	private int getFightCardInitHp(List<FightCard> userCards,FightCard fightCard){
		for(FightCard aFightCard:userCards){
			if(aFightCard.getUUID().equals(fightCard.getUUID())){
				return aFightCard.gethP();
			}
		}
		return 0;
	}
	private FightCard getFightCardByUuid(String uuid,List<FightCard> fightCards){
		for(int i=0;i<fightCards.size();i++){
			if(fightCards.get(i).getUUID().equals(uuid)){
				return fightCards.get(i).clone();
			}
		}
		return null;
	}
	//从战斗区获取卡牌
	private FightCard getFightCardByUuidFromFightRegion(String uuid,Map<String,FightCard> fightCards){
		FightCard fightCard = null;
		Iterator<FightCard> ite = fightCards.values().iterator();
		while(ite.hasNext()){
			fightCard =ite.next();
			if(fightCard.getUUID().equals(uuid)){
				break;
			}
		}
		return fightCard;
	}
	//获取等待时间最长的卡牌
	private int getMaxWaitCard(List<FightCard> userCards){
		int wait=0;
		int j = 0;
		for(int i=0;i<userCards.size();i++){
			if(userCards.get(i).getWait()>wait){
				wait = userCards.get(i).getWait();
				j=i;
			}
		}
		return j;
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
	private int getRaceCountInFightregion(Map<String,FightCard> fightCards,int race){
		int j = 0;
		Iterator<FightCard> ite = fightCards.values().iterator();
		FightCard fightCard;
		Card card;
		while(ite.hasNext()){
			fightCard =ite.next();
			card = cardManager.getCardById(new Long(fightCard.getCardId()));
			if(card.getRace()==race){
				j++;
			}
		}
		return j;
	}
	private int getRaceCountInList(List<FightCard> fightCards,int race){
		int j = 0;
		FightCard fightCard;
		Card card;
		for(int i=0;i<fightCards.size();i++){
			fightCard =fightCards.get(i);
			card = cardManager.getCardById(new Long(fightCard.getCardId()));
			if(card.getRace()==race){
				j++;
			}
		}
		return j;
	}
	//判断卡牌是否被加上了裂伤技能
	private boolean hasLaceratedSkill(FightCard fightCard){
		if(fightCard.getSkillNew()!=null){
			for(Skill skill:fightCard.getSkillBuff()){
				if(skill.getLanchType()==2&&skill.getAffectType()==49){
					return true;
				}
			}
			
		}
		return false;
	}
	//判断卡牌是否有免疫技能
	private boolean hasImmunitySkill(FightCard fightCard){
		if(fightCard==null){
			return false;
		}
		Card card = cardManager.getCardById(new Long(fightCard.getCardId()));
		if(card.getSkill()>0){
			Skill skill = skillManager.getSkillById(card.getSkill());
			if(skill.getLanchType()==13){
				return true;
			}
		}
		if(fightCard.getLevel()>=5){
			Skill skill = skillManager.getSkillById(card.getLockSkill1());
			if(skill.getLanchType()==13){
				return true;
			}
		}
		if(fightCard.getLevel()>=10){
			Skill skill = skillManager.getSkillById(card.getLockSkill2());
			if(skill.getLanchType()==13){
				return true;
			}
		}
		if(fightCard.getSkillNew()!=null){
			for(Skill skill:fightCard.getSkillNew()){
				if(skill.getLanchType()==13){
					return true;
				}
			}
			
		}
		return false;
	}
	//判断卡牌是否有不动技能
	private boolean hasBuDongSkill(FightCard fightCard){
		if(fightCard==null){
			return false;
		}
		Card card = cardManager.getCardById(new Long(fightCard.getCardId()));
		if(card.getSkill()>0){
			Skill skill = skillManager.getSkillById(card.getSkill());
			if(skill.getLanchType()==12){
				return true;
			}
		}
		if(fightCard.getLevel()>=5){
			Skill skill = skillManager.getSkillById(card.getLockSkill1());
			if(skill.getLanchType()==12){
				return true;
			}
		}
		if(fightCard.getLevel()>=10){
			Skill skill = skillManager.getSkillById(card.getLockSkill2());
			if(skill.getLanchType()==12){
				return true;
			}
		}
		if(fightCard.getSkillNew()!=null){
			for(Skill skill:fightCard.getSkillNew()){
				if(skill.getLanchType()==12){
					return true;
				}
			}
			
		}
		return false;
	}
	public RuneManager getRuneManager() {
		return runeManager;
	}
	public void setRuneManager(RuneManager runeManager) {
		this.runeManager = runeManager;
	}
	public SoulManager getSoulManager() {
		return soulManager;
	}
	public void setSoulManager(SoulManager soulManager) {
		this.soulManager = soulManager;
	}
	public SkillManager getSkillManager() {
		return skillManager;
	}
	public void setSkillManager(SkillManager skillManager) {
		this.skillManager = skillManager;
	}



	

}

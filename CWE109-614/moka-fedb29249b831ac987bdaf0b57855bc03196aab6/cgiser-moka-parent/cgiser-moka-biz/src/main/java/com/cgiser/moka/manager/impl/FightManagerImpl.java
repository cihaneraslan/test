package com.cgiser.moka.manager.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;
import net.sf.json.processors.PropertyNameProcessor;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.cgiser.core.common.cache.mem.MemCachedManager;
import com.cgiser.moka.common.utils.ListUtil;
import com.cgiser.moka.dao.FightDao;
import com.cgiser.moka.manager.AchievementManager;
import com.cgiser.moka.manager.CardManager;
import com.cgiser.moka.manager.FightManager;
import com.cgiser.moka.manager.LegionManager;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.manager.RuneManager;
import com.cgiser.moka.manager.SalaryManager;
import com.cgiser.moka.manager.SkillManager;
import com.cgiser.moka.manager.StageLevelManager;
import com.cgiser.moka.manager.StageManager;
import com.cgiser.moka.manager.UserCardManager;
import com.cgiser.moka.manager.UserRuneManager;
import com.cgiser.moka.manager.UserStageManager;
import com.cgiser.moka.manager.support.BeanUtils;
import com.cgiser.moka.model.Battle;
import com.cgiser.moka.model.Card;
import com.cgiser.moka.model.ExtData;
import com.cgiser.moka.model.FightCard;
import com.cgiser.moka.model.FightResult;
import com.cgiser.moka.model.FightRune;
import com.cgiser.moka.model.Legion;
import com.cgiser.moka.model.LegionTech;
import com.cgiser.moka.model.Legioner;
import com.cgiser.moka.model.Opp;
import com.cgiser.moka.model.Player;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.model.Round;
import com.cgiser.moka.model.Rune;
import com.cgiser.moka.model.Salary;
import com.cgiser.moka.model.SalaryEnum;
import com.cgiser.moka.model.Skill;
import com.cgiser.moka.model.Soul;
import com.cgiser.moka.model.Stage;
import com.cgiser.moka.model.StageLevel;
import com.cgiser.moka.model.StageLevelConditions;
import com.cgiser.moka.model.UserAchievement;
import com.cgiser.moka.model.UserCard;
import com.cgiser.moka.model.UserStage;

public class FightManagerImpl implements FightManager {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	private MemCachedManager battleCachedManager;
	private RoleManager roleManager;
	private StageManager stageManager;
	private UserStageManager userStageManager;
	private CardManager cardManager;
	private AchievementManager achievementManager;
	private StageLevelManager stageLevelManager;
	private SalaryManager salaryManager;
	private RuneManager runeManager;
	private UserCardManager userCardManager;
	private UserRuneManager userRuneManager;
	private SkillManager skillManager;
	private FightDao fightDao;
	private JsonConfig jsonConfig;
	private LegionManager legionManager;

	@Override
	public FightResult runFight(Player attackPlayer, Player defendPlayer,
			int type) {
		try {
			// 构造战斗模型

			// 初始化战斗模型
			Battle battle = new Battle();
			List<Round> rounds = new ArrayList<Round>();

			// 初始化手牌
			List<FightCard> aHandsCards = ListUtil.copyTo(attackPlayer
					.getCards(), FightCard.class);
			List<FightCard> dHandsCards = ListUtil.copyTo(defendPlayer
					.getCards(), FightCard.class);
			// 初始化准备区
			List<FightCard> aPreCards = new ArrayList<FightCard>();
			List<FightCard> dPreCards = new ArrayList<FightCard>();
			// 初始化战斗区
			Map<String, FightCard> dFightCards = new HashMap<String, FightCard>();
			Map<String, FightCard> aFightCards = new HashMap<String, FightCard>();

			// 攻击者和被攻击者初始化
			attackPlayer.setHandsCards(aHandsCards);
			attackPlayer.setPreCards(aPreCards);
			attackPlayer.setFightCards(aFightCards);
			attackPlayer.setTumbCards(new ArrayList<FightCard>());

			defendPlayer.setFightCards(dFightCards);
			defendPlayer.setHandsCards(dHandsCards);
			defendPlayer.setPreCards(dPreCards);
			defendPlayer.setTumbCards(new ArrayList<FightCard>());
			// 循环战斗回合
			this.runRound(attackPlayer, defendPlayer, 1, rounds);

			battle.setRounds(rounds);
			FightResult result = new FightResult();
			// result.setExtData(extData);
			result.setAttackPlayer(attackPlayer);
			result.setBattle(battle);
			result.setBattleId(UUID.randomUUID().toString());
			result.setDefendPlayer(defendPlayer);
			result.setType(type);
			this.setFightResult(attackPlayer, defendPlayer, result);
			if (result.getWin() != 0) {
				this.saveFightRel(result);
			}
			return result;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}

	}

	private void setFightResult(Player aPlayer, Player dPlayer,
			FightResult fightResult) {
		if (aPlayer.getHP() <= 0
				|| (aPlayer.getFightCards().size() == 0
						&& aPlayer.getPreCards().size() == 0 && aPlayer
						.getHandsCards().size() == 0)) {
			fightResult.setWin(2);

			return;
		}
		if (dPlayer.getHP() <= 0
				|| (dPlayer.getFightCards().size() == 0
						&& dPlayer.getPreCards().size() == 0 && dPlayer
						.getHandsCards().size() == 0)) {
			fightResult.setWin(1);

		}

	}

	@Override
	public void runRound(Player attackPlayer, Player defendPlayer, int nRound,
			List<Round> rounds) {
		// 存储新上场卡牌
		List<FightCard> newFightCard = new ArrayList<FightCard>();
		List<FightCard> aHandsCards = attackPlayer.getHandsCards();
		List<FightCard> dHandsCards = defendPlayer.getHandsCards();
		// 初始化循环用到的变量
		FightCard fightCard;
		Card card;
		Round round;
		Opp opp;
		List<Opp> opps;
		// 保存手牌数量
		int a = aHandsCards.size();
		int b = dHandsCards.size();
		int i = nRound;
		// 是否主攻
		boolean isAttack = true;
		// 是否手动操作局部变量
		boolean flag = false;
		// 开始攻击
		while (i > 0) {
			a = aHandsCards.size();
			b = dHandsCards.size();
			round = new Round();
			opps = new ArrayList<Opp>();
			isAttack = i % 2 == 0 ? false : true;
			round.setRound(i);
			if (i > 50) {
				if (isAttack) {
					attackPlayer.setHP(attackPlayer.getHP() - 50);
					opp = new Opp();
					opp.setOpp(1022);
					opp.setTarget(attackPlayer.getName());
					opp.setValue(-50);
					opps.add(opp);
				} else {
					defendPlayer.setHP(defendPlayer.getHP() - 50);
					opp = new Opp();
					opp.setOpp(1022);
					opp.setTarget(defendPlayer.getName());
					opp.setValue(-50);
					opps.add(opp);
				}
			}
			// 更新CD时间的操作
			for (int j = 0; j < attackPlayer.getPreCards().size(); j++) {
				attackPlayer.getPreCards().get(j).setWait(
						attackPlayer.getPreCards().get(j).getWait() - 1);
			}
			for (int j = defendPlayer.getPreCards().size() - 1; j > -1; j--) {
				defendPlayer.getPreCards().get(j).setWait(
						defendPlayer.getPreCards().get(j).getWait() - 1);
			}
			opp = new Opp();
			opp.setOpp(1021);
			opp.setValue(-1);
			opps.add(opp);
			if (isAttack) {
				if (a > 0) {
					attackPlayer.getPreCards().add(
							attackPlayer.getHandsCards().get(a - 1));
					// 随机手牌
					opp = new Opp();
					opp.setOpp(1001);
					opp.setUUID(attackPlayer.getHandsCards().get(a - 1)
							.getUUID());
					opp.setValue(0);
					opps.add(opp);
					attackPlayer.getHandsCards().remove(a - 1);
					a--;
				}
			} else {
				if (b > 0) {
					defendPlayer.getPreCards().add(
							defendPlayer.getHandsCards().get(b - 1));
					// 随机手牌
					opp = new Opp();
					opp.setOpp(1001);
					opp.setUUID(defendPlayer.getHandsCards().get(b - 1)
							.getUUID());
					opp.setValue(0);
					opps.add(opp);
					defendPlayer.getHandsCards().remove(b - 1);
					b--;
				}
			}
			// 卡牌上场
			// 新上场卡牌区置空
			newFightCard.clear();
			if (isAttack) {
				for (FightCard fi : attackPlayer.getPreCards()) {
					if (fi.getWait() <= 0) {
						if (attackPlayer.isHand()) {
							flag = true;
						} else {
							opp = new Opp();
							opp.setOpp(1002);
							opp.setUUID(fi.getUUID());
							opp.setValue(0);
							opps.add(opp);
							newFightCard.add(fi);
							attackPlayer.getFightCards().put(
									String.valueOf(attackPlayer.getFightCards()
											.size()), fi);
							// aPreCards.remove(fi);
						}

					}
				}
				for (int j = 0; j < newFightCard.size(); j++) {
					if (attackPlayer.getPreCards()
							.contains(newFightCard.get(j))) {
						attackPlayer.getPreCards().remove(newFightCard.get(j));
					}
				}
			} else {
				for (FightCard fi : defendPlayer.getPreCards()) {
					if (fi.getWait() <= 0) {
						if (defendPlayer.isHand()) {
							flag = true;
						} else {
							opp = new Opp();
							opp.setOpp(1002);
							opp.setUUID(fi.getUUID());
							opp.setValue(0);
							opps.add(opp);
							newFightCard.add(fi);
							defendPlayer.getFightCards().put(
									String.valueOf(defendPlayer.getFightCards()
											.size()), fi);
							// dPreCards.remove(fi);
						}
					}
				}
				for (int j = 0; j < newFightCard.size(); j++) {
					if (defendPlayer.getPreCards()
							.contains(newFightCard.get(j))) {
						defendPlayer.getPreCards().remove(newFightCard.get(j));
					}
				}
			}

			if (flag) {
				round.setIsAttack(isAttack);
				round.setOpps(opps);
				rounds.add(round);
				break;
			}
			// 星辰是否还有发动次数
			if (isAttack) {
				for (int j = 0; j < attackPlayer.getRunes().size(); j++) {
					FightRune fightRune = attackPlayer.getRunes().get(j);
					if (fightRune.getRemainTimes() > 0) {
						List<Opp> oppsRunes = skillManager.getRuneOpps(
								fightRune, attackPlayer, defendPlayer, i);
						if (!CollectionUtils.isEmpty(oppsRunes)) {
							opps.addAll(oppsRunes);
							opp = new Opp();
							opp.setOpp(50);
							opp.setUUID(fightRune.getuUID());
							opp.setValue(fightRune.getRemainTimes());
							opps.add(opp);
							fightRune
									.setRemainTimes(fightRune.getRemainTimes() - 1);
						} else {
							opp = new Opp();
							opp.setOpp(50);
							opp.setUUID(fightRune.getuUID());
							opp.setValue(-1);
							opps.add(opp);
						}
						if (fightRune.getRemainTimes() <= 0) {
							opp = new Opp();
							opp.setOpp(1050);
							opp.setUUID(fightRune.getuUID());
							opp.setTarget(fightRune.getuUID());
							opp.setValue(0);
							opps.add(opp);
						}
					} else {
						opp = new Opp();
						opp.setOpp(1050);
						opp.setUUID(fightRune.getuUID());
						opp.setTarget(fightRune.getuUID());
						opp.setValue(0);
						opps.add(opp);
					}
				}
			} else {
				for (int j = 0; j < defendPlayer.getRunes().size(); j++) {
					FightRune fightRune = defendPlayer.getRunes().get(j);
					if (fightRune.getRemainTimes() > 0) {
						List<Opp> oppsRunes = skillManager.getRuneOpps(
								fightRune, defendPlayer, attackPlayer, i);
						if (!CollectionUtils.isEmpty(oppsRunes)) {
							opps.addAll(oppsRunes);
							opp = new Opp();
							opp.setOpp(50);
							opp.setUUID(fightRune.getuUID());
							opp.setValue(fightRune.getRemainTimes());
							opps.add(opp);
							fightRune
									.setRemainTimes(fightRune.getRemainTimes() - 1);
						} else {
							opp = new Opp();
							opp.setOpp(50);
							opp.setUUID(fightRune.getuUID());
							opp.setValue(-1);
							opps.add(opp);
						}
						if (fightRune.getRemainTimes() <= 0) {
							opp = new Opp();
							opp.setOpp(1050);
							opp.setUUID(fightRune.getuUID());
							opp.setTarget(fightRune.getuUID());
							opp.setValue(0);
							opps.add(opp);
						}

					} else {
						opp = new Opp();
						opp.setOpp(1050);
						opp.setUUID(fightRune.getuUID());
						opp.setTarget(fightRune.getuUID());
						opp.setValue(0);
						opps.add(opp);
					}
				}
			}
			// 卡牌上场时查看场上卡牌是否有buff技能
			if (newFightCard.size() > 0) {
				if (isAttack) {
					opps.addAll(skillManager.getSkillModelInCardToPlay(
							attackPlayer, defendPlayer, newFightCard));
				} else {
					opps.addAll(skillManager.getSkillModelInCardToPlay(
							defendPlayer, attackPlayer, newFightCard));
				}
			}
			// 新上场卡牌是否有上场技能
			if (newFightCard.size() > 0) {
				for (int j = 0; j < newFightCard.size(); j++) {
					fightCard = newFightCard.get(j);
					card = cardManager.getCardById(new Long(fightCard
							.getCardId()));

					if (card.getSkill() > 0) {
						Skill skill = skillManager
								.getSkillById(card.getSkill());
						List<Opp> opps1 = null;
						if (isAttack) {
							opps1 = skillManager.getSkillModelByType(skill,
									attackPlayer, defendPlayer, fightCard);
						} else {
							opps1 = skillManager.getSkillModelByType(skill,
									defendPlayer, attackPlayer, fightCard);
						}
						if (opps1 != null) {
							opps.addAll(opps1);
						}
					}
					if (fightCard.getLevel() >= 5) {
						Skill skill = skillManager.getSkillById(card
								.getLockSkill1());
						List<Opp> opps1 = null;
						if (isAttack) {
							opps1 = skillManager.getSkillModelByType(skill,
									attackPlayer, defendPlayer, fightCard);
						} else {
							opps1 = skillManager.getSkillModelByType(skill,
									defendPlayer, attackPlayer, fightCard);
						}
						if (opps1 != null) {
							opps.addAll(opps1);
						}
					}
					if (fightCard.getLevel() >= 10) {
						Skill skill = skillManager.getSkillById(card
								.getLockSkill2());
						List<Opp> opps1 = null;
						if (isAttack) {
							opps1 = skillManager.getSkillModelByType(skill,
									attackPlayer, defendPlayer, fightCard);
						} else {
							opps1 = skillManager.getSkillModelByType(skill,
									defendPlayer, attackPlayer, fightCard);
						}
						if (opps1 != null) {
							opps.addAll(opps1);
						}
					}

				}
				attackPlayer.setFightCards(this.resetFightCard(attackPlayer
						.getFightCards()));
				defendPlayer.setFightCards(this.resetFightCard(defendPlayer
						.getFightCards()));
				// 更新卡牌区域和战斗区域
				opp = new Opp();
				opp.setOpp(1060);
				opp.setValue(3);
				opps.add(opp);
				opp = new Opp();
				opp.setOpp(1060);
				opp.setValue(3);
				opps.add(opp);
				if (isWin(attackPlayer, defendPlayer)) {
					break;
				}
			}

			if (isAttack) {
				for (int j = 0; j <= this
						.getLastIndexFromFightregion(attackPlayer
								.getFightCards()); j++) {
					List<Skill> skills = new ArrayList<Skill>();
					fightCard = attackPlayer.getFightCards().get(
							String.valueOf(j));
					if (fightCard == null) {
						continue;
					}
					card = cardManager.getCardById(new Long(fightCard
							.getCardId()));

					if (card.getSkill() > 0) {
						Skill skill = skillManager
								.getSkillById(card.getSkill());
						skills.add(skill);
					}
					if (fightCard.getLevel() >= 5) {
						Skill skill = skillManager.getSkillById(card
								.getLockSkill1());
						skills.add(skill);
					}
					if (fightCard.getLevel() >= 10) {
						Skill skill = skillManager.getSkillById(card
								.getLockSkill2());
						skills.add(skill);
					}
					if (fightCard.getSkillNew() != null) {
						skills.addAll(fightCard.getSkillNew());
					}
					opps.addAll(skillManager.getActiveSkillModelByType(skills,
							attackPlayer, defendPlayer, fightCard, fightCard
									.getSkillBuff(), this.isNewCard(
									newFightCard, fightCard)));
					fightCard.getSkillNew().clear();
					if (isWin(attackPlayer, defendPlayer)) {
						break;
					}
				}
			} else {
				for (int j = 0; j <= this
						.getLastIndexFromFightregion(defendPlayer
								.getFightCards()); j++) {
					List<Skill> skills = new ArrayList<Skill>();
					fightCard = defendPlayer.getFightCards().get(
							String.valueOf(j));
					if (fightCard == null) {
						continue;
					}
					card = cardManager.getCardById(new Long(fightCard
							.getCardId()));

					if (card.getSkill() > 0) {
						Skill skill = skillManager
								.getSkillById(card.getSkill());
						skills.add(skill);
					}
					if (fightCard.getLevel() >= 5) {
						Skill skill = skillManager.getSkillById(card
								.getLockSkill1());
						skills.add(skill);
					}
					if (fightCard.getLevel() >= 10) {
						Skill skill = skillManager.getSkillById(card
								.getLockSkill2());
						skills.add(skill);
					}
					if (fightCard.getSkillNew() != null) {
						skills.addAll(fightCard.getSkillNew());
					}
					opps.addAll(skillManager.getActiveSkillModelByType(skills,
							defendPlayer, attackPlayer, fightCard, fightCard
									.getSkillBuff(), this.isNewCard(
									newFightCard, fightCard)));
					fightCard.getSkillNew().clear();
					if (isWin(attackPlayer, defendPlayer)) {
						break;
					}
				}
			}
			attackPlayer.setFightCards(this.resetFightCard(attackPlayer
					.getFightCards()));
			defendPlayer.setFightCards(this.resetFightCard(defendPlayer
					.getFightCards()));
			// 更新卡牌区域和战斗区域
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
			if (flag) {
				break;
			}
			if (isWin(attackPlayer, defendPlayer)) {
				break;
			}
		}
	}

	@Override
	public void runRound1(Player aPlayer, Player dPlayer, int nRound,
			List<Round> rounds, FightResult fightResult, String[] uuids,
			List<Opp> oppsNewCard) {
		// 存储新上场卡牌
		List<FightCard> newFightCard = new ArrayList<FightCard>();
		List<FightCard> aHandsCards = aPlayer.getHandsCards();
		List<FightCard> dHandsCards = dPlayer.getHandsCards();
		boolean isFirst = true;
		// 初始化循环用到的变量
		FightCard fightCard;
		Card card;
		Round round;
		Opp opp;
		List<Opp> opps;
		// 保存手牌数量
		int a = aHandsCards.size();
		int b = dHandsCards.size();
		int i = nRound;
		// 是否主攻
		boolean isAttack = true;
		// 是否手动操作局部变量
		boolean flag = false;
		while (i > 0) {
			a = aPlayer.getHandsCards().size();
			b = dPlayer.getHandsCards().size();
			// 初始化新的回合
			round = new Round();
			opps = new ArrayList<Opp>();
			round.setRound(i);

			if (fightResult.getAttackPlayer().getName().equals("def")) {
				isAttack = i % 2 == 0 ? true : false;
			} else {
				isAttack = i % 2 == 0 ? false : true;
			}
			if (isFirst) {
				isFirst = false;
				// 手动战斗上牌到战斗区
				for (int j = 0; j < uuids.length; j++) {
					if (StringUtils.isBlank(uuids[j])) {
						continue;
					}
					for (int c = 0; c < aPlayer.getPreCards().size(); c++) {
						if (aPlayer.getPreCards().get(c).getUUID().equals(
								uuids[j].trim())) {
							newFightCard.add(aPlayer.getPreCards().get(c));
							opp = new Opp();
							opp.setOpp(1002);
							opp.setUUID(aPlayer.getPreCards().get(c).getUUID());
							opp.setValue(0);
							oppsNewCard.add(opp);
							aPlayer.getFightCards().put(
									String.valueOf(aPlayer.getFightCards()
											.size()),
									aPlayer.getPreCards().get(c));
							break;
						}
					}
					for (int f = 0; f < newFightCard.size(); f++) {
						if (aPlayer.getPreCards().contains(newFightCard.get(f))) {
							aPlayer.getPreCards().remove(newFightCard.get(f));
						}
					}
				}
				// 自动战斗的时候将cd时间为0的卡牌全上场
				if (!aPlayer.isHand()) {
					for (FightCard fi : aPlayer.getPreCards()) {
						if (fi.getWait() <= 0) {
							opp = new Opp();
							opp.setOpp(1002);
							opp.setUUID(fi.getUUID());
							opp.setValue(0);
							opps.add(opp);
							newFightCard.add(fi);
							aPlayer.getFightCards().put(
									String.valueOf(aPlayer.getFightCards()
											.size()), fi);

						}
					}
					for (int j = 0; j < newFightCard.size(); j++) {
						if (aPlayer.getPreCards().contains(newFightCard.get(j))) {
							aPlayer.getPreCards().remove(newFightCard.get(j));
						}
					}
				}

			} else {
				// 大于50回合双方英雄开始减血
				if (i > 50) {
					if (isAttack) {
						aPlayer.setHP(aPlayer.getHP() - 50);
						opp = new Opp();
						opp.setOpp(1022);
						opp.setTarget(aPlayer.getName());
						opp.setValue(-50);
						opps.add(opp);
					} else {
						dPlayer.setHP(dPlayer.getHP() - 50);
						opp = new Opp();
						opp.setOpp(1022);
						opp.setTarget(dPlayer.getName());
						opp.setValue(-50);
						opps.add(opp);
					}
				}
				// 更新CD时间数据操作开始
				for (int j = 0; j < aPlayer.getPreCards().size(); j++) {
					aPlayer.getPreCards().get(j).setWait(
							aPlayer.getPreCards().get(j).getWait() - 1);
					if (aPlayer.getPreCards().get(j).getWait() <= 0) {
						if (isAttack) {
							if (aPlayer.isHand()) {
								flag = true;
							}
						}
					}
				}
				for (int j = dPlayer.getPreCards().size() - 1; j > -1; j--) {
					dPlayer.getPreCards().get(j).setWait(
							dPlayer.getPreCards().get(j).getWait() - 1);
					if (dPlayer.getPreCards().get(j).getWait() <= 0) {
						if (!isAttack) {
							if (dPlayer.isHand()) {
								flag = true;
							}
						}
					}
				}

				opp = new Opp();
				opp.setOpp(1021);
				opp.setValue(-1);
				opps.add(opp);
				// 更新CD时间操作完成
				// 推手牌开始
				if (isAttack) {
					if (a > 0 && aPlayer.getPreCards().size() < 5) {
						aPlayer.getPreCards().add(
								aPlayer.getHandsCards().get(a - 1));
						// 随机手牌
						opp = new Opp();
						opp.setOpp(1001);
						opp.setUUID(aPlayer.getHandsCards().get(a - 1)
								.getUUID());
						opp.setValue(0);
						opps.add(opp);
						aPlayer.getHandsCards().remove(a - 1);
						a--;
					}
				} else {
					if (b > 0 && dPlayer.getPreCards().size() < 5) {
						dPlayer.getPreCards().add(
								dPlayer.getHandsCards().get(b - 1));
						// 随机手牌
						opp = new Opp();
						opp.setOpp(1001);
						opp.setUUID(dPlayer.getHandsCards().get(b - 1)
								.getUUID());
						opp.setValue(0);
						opps.add(opp);
						dPlayer.getHandsCards().remove(b - 1);
						b--;
					}
				}
				// 卡牌上场
				// 新上场卡牌区置空
				newFightCard.clear();
				if (isAttack) {
					for (FightCard fi : aPlayer.getPreCards()) {
						if (fi.getWait() <= 0) {
							if (aPlayer.isHand()) {
								flag = true;
							} else {
								opp = new Opp();
								opp.setOpp(1002);
								opp.setUUID(fi.getUUID());
								opp.setValue(0);
								opps.add(opp);
								newFightCard.add(fi);
								aPlayer.getFightCards().put(
										String.valueOf(aPlayer.getFightCards()
												.size()), fi);
							}

						}
					}
					for (int j = 0; j < newFightCard.size(); j++) {
						if (aPlayer.getPreCards().contains(newFightCard.get(j))) {
							aPlayer.getPreCards().remove(newFightCard.get(j));
						}
					}
				} else {
					for (FightCard fi : dPlayer.getPreCards()) {
						if (fi.getWait() <= 0) {
							if (dPlayer.isHand()) {
								flag = true;
							} else {
								opp = new Opp();
								opp.setOpp(1002);
								opp.setUUID(fi.getUUID());
								opp.setValue(0);
								opps.add(opp);
								newFightCard.add(fi);
								dPlayer.getFightCards().put(
										String.valueOf(dPlayer.getFightCards()
												.size()), fi);
							}
						}
					}
					for (int j = 0; j < newFightCard.size(); j++) {
						if (dPlayer.getPreCards().contains(newFightCard.get(j))) {
							dPlayer.getPreCards().remove(newFightCard.get(j));
						}
					}
				}
			}
			if (flag) {
				round.setIsAttack(isAttack);
				round.setOpps(opps);
				rounds.add(round);
				break;
			}
			// 星辰是否还有发动次数
			if (isAttack) {
				for (int j = 0; j < aPlayer.getRunes().size(); j++) {
					FightRune fightRune = aPlayer.getRunes().get(j);
					if (fightRune.getRemainTimes() > 0) {
						List<Opp> oppsRunes = skillManager.getRuneOpps(
								fightRune, aPlayer, dPlayer, i);
						if (!CollectionUtils.isEmpty(oppsRunes)) {
							opps.addAll(oppsRunes);
							opp = new Opp();
							opp.setOpp(50);
							opp.setUUID(fightRune.getuUID());
							opp.setValue(fightRune.getRemainTimes());
							opps.add(opp);
							fightRune
									.setRemainTimes(fightRune.getRemainTimes() - 1);
						} else {
							opp = new Opp();
							opp.setOpp(50);
							opp.setUUID(fightRune.getuUID());
							opp.setValue(-1);
							opps.add(opp);
						}
						if (fightRune.getRemainTimes() <= 0) {
							opp = new Opp();
							opp.setOpp(1050);
							opp.setUUID(fightRune.getuUID());
							opp.setTarget(fightRune.getuUID());
							opp.setValue(0);
							opps.add(opp);
						}
					} else {
						opp = new Opp();
						opp.setOpp(1050);
						opp.setUUID(fightRune.getuUID());
						opp.setTarget(fightRune.getuUID());
						opp.setValue(0);
						opps.add(opp);
					}
				}
			} else {
				for (int j = 0; j < dPlayer.getRunes().size(); j++) {
					FightRune fightRune = dPlayer.getRunes().get(j);
					if (fightRune.getRemainTimes() > 0) {
						List<Opp> oppsRunes = skillManager.getRuneOpps(
								fightRune, dPlayer, aPlayer, i);
						if (!CollectionUtils.isEmpty(oppsRunes)) {
							opps.addAll(oppsRunes);
							opp = new Opp();
							opp.setOpp(50);
							opp.setUUID(fightRune.getuUID());
							opp.setValue(fightRune.getRemainTimes());
							opps.add(opp);
							fightRune
									.setRemainTimes(fightRune.getRemainTimes() - 1);
						} else {
							opp = new Opp();
							opp.setOpp(50);
							opp.setUUID(fightRune.getuUID());
							opp.setValue(-1);
							opps.add(opp);
						}
						if (fightRune.getRemainTimes() <= 0) {
							opp = new Opp();
							opp.setOpp(1050);
							opp.setUUID(fightRune.getuUID());
							opp.setValue(0);
							opps.add(opp);
						}

					} else {
						opp = new Opp();
						opp.setOpp(1050);
						opp.setUUID(fightRune.getuUID());
						opp.setTarget(fightRune.getuUID());
						opp.setValue(0);
						opps.add(opp);
					}
				}
			}
			// 卡牌上场时查看场上卡牌是否有buff技能
			if (newFightCard.size() > 0) {
				if (isAttack) {
					opps.addAll(skillManager.getSkillModelInCardToPlay(aPlayer,
							dPlayer, newFightCard));
				} else {
					opps.addAll(skillManager.getSkillModelInCardToPlay(dPlayer,
							aPlayer, newFightCard));
				}
			}
			// 新上场卡牌是否有上场技能
			if (newFightCard.size() > 0) {
				for (int j = 0; j < newFightCard.size(); j++) {
					fightCard = newFightCard.get(j);
					card = cardManager.getCardById(new Long(fightCard
							.getCardId()));

					if (card.getSkill() > 0) {
						Skill skill = skillManager
								.getSkillById(card.getSkill());
						List<Opp> opps1 = null;
						if (isAttack) {
							opps1 = skillManager.getSkillModelByType(skill,
									aPlayer, dPlayer, fightCard);
						} else {
							opps1 = skillManager.getSkillModelByType(skill,
									dPlayer, aPlayer, fightCard);
						}
						if (opps1 != null) {
							opps.addAll(opps1);
						}
					}
					if (fightCard.getLevel() >= 5) {
						Skill skill = skillManager.getSkillById(card
								.getLockSkill1());
						List<Opp> opps1 = null;
						if (isAttack) {
							opps1 = skillManager.getSkillModelByType(skill,
									aPlayer, dPlayer, fightCard);
						} else {
							opps1 = skillManager.getSkillModelByType(skill,
									dPlayer, aPlayer, fightCard);
						}
						if (opps1 != null) {
							opps.addAll(opps1);
						}
					}
					if (fightCard.getLevel() >= 10) {
						Skill skill = skillManager.getSkillById(card
								.getLockSkill2());
						List<Opp> opps1 = null;
						if (isAttack) {
							opps1 = skillManager.getSkillModelByType(skill,
									aPlayer, dPlayer, fightCard);
						} else {
							opps1 = skillManager.getSkillModelByType(skill,
									dPlayer, aPlayer, fightCard);
						}
						if (opps1 != null) {
							opps.addAll(opps1);
						}
					}

				}
				aPlayer.setFightCards(this.resetFightCard(aPlayer
						.getFightCards()));
				dPlayer.setFightCards(this.resetFightCard(dPlayer
						.getFightCards()));
				// 更新卡牌区域和战斗区域
				opp = new Opp();
				opp.setOpp(1060);
				opp.setValue(3);
				opps.add(opp);
				opp = new Opp();
				opp.setOpp(1060);
				opp.setValue(3);
				opps.add(opp);
				if (isWin(aPlayer, dPlayer)) {
					break;
				}
			}

			if (isAttack) {
				for (int j = 0; j <= this.getLastIndexFromFightregion(aPlayer
						.getFightCards()); j++) {
					List<Skill> skills = new ArrayList<Skill>();
					fightCard = aPlayer.getFightCards().get(String.valueOf(j));
					if (fightCard == null) {
						continue;
					}
					card = cardManager.getCardById(new Long(fightCard
							.getCardId()));

					if (card.getSkill() > 0) {
						Skill skill = skillManager
								.getSkillById(card.getSkill());
						skills.add(skill);
					}
					if (fightCard.getLevel() >= 5) {
						Skill skill = skillManager.getSkillById(card
								.getLockSkill1());
						skills.add(skill);
					}
					if (fightCard.getLevel() >= 10) {
						Skill skill = skillManager.getSkillById(card
								.getLockSkill2());
						skills.add(skill);
					}
					if (fightCard.getSkillNew() != null) {
						skills.addAll(fightCard.getSkillNew());
					}
					opps.addAll(skillManager.getActiveSkillModelByType(skills,
							aPlayer, dPlayer, fightCard, fightCard
									.getSkillBuff(), this.isNewCard(
									newFightCard, fightCard)));
					if (isWin(aPlayer, dPlayer)) {
						break;
					}
				}
				for (int j = 0; j <= this.getLastIndexFromFightregion(dPlayer
						.getFightCards()); j++) {
					fightCard = dPlayer.getFightCards().get(String.valueOf(j));
					if (fightCard == null) {
						continue;
					}
					fightCard.getSkillNew().clear();
				}
			} else {
				for (int j = 0; j <= this.getLastIndexFromFightregion(dPlayer
						.getFightCards()); j++) {
					List<Skill> skills = new ArrayList<Skill>();
					fightCard = dPlayer.getFightCards().get(String.valueOf(j));
					if (fightCard == null) {
						continue;
					}
					card = cardManager.getCardById(new Long(fightCard
							.getCardId()));

					if (card.getSkill() > 0) {
						Skill skill = skillManager
								.getSkillById(card.getSkill());
						skills.add(skill);
					}
					if (fightCard.getLevel() >= 5) {
						Skill skill = skillManager.getSkillById(card
								.getLockSkill1());
						skills.add(skill);
					}
					if (fightCard.getLevel() >= 10) {
						Skill skill = skillManager.getSkillById(card
								.getLockSkill2());
						skills.add(skill);
					}
					if (fightCard.getSkillNew() != null) {
						skills.addAll(fightCard.getSkillNew());
					}
					opps.addAll(skillManager.getActiveSkillModelByType(skills,
							dPlayer, aPlayer, fightCard, fightCard
									.getSkillBuff(), this.isNewCard(
									newFightCard, fightCard)));
					if (isWin(aPlayer, dPlayer)) {
						break;
					}
				}
				for (int j = 0; j <= this.getLastIndexFromFightregion(aPlayer
						.getFightCards()); j++) {
					fightCard = aPlayer.getFightCards().get(String.valueOf(j));
					if (fightCard == null) {
						continue;
					}
					fightCard.getSkillNew().clear();
				}
			}
			aPlayer.setFightCards(this.resetFightCard(aPlayer.getFightCards()));
			dPlayer.setFightCards(this.resetFightCard(dPlayer.getFightCards()));
			// 更新卡牌区域和战斗区域
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
			if (flag) {
				break;
			}
			if (isWin(aPlayer, dPlayer)) {
				break;
			}

		}

	}

	@Override
	public void saveFightRel(FightResult result) {
		if (jsonConfig == null) {
			jsonConfig = new JsonConfig();
			PropertyNameProcessor processor = new PropertyNameProcessor() {
				@Override
				public String processPropertyName(Class beanClass, String name) {
					name = name.replaceFirst(name.substring(0, 1), name
							.substring(0, 1).toUpperCase());
					return name;
				}
			};
			JsonValueProcessor dateprocessor = new JsonValueProcessor() {
				@Override
				public Object processObjectValue(String key, Object value,
						JsonConfig jsonConfig) {
					if (value instanceof Date) {
						try {
							SimpleDateFormat dateFormat = new SimpleDateFormat(
									"yyyy-MM-dd hh:mm:ss");
							return dateFormat.format(value);
						} catch (Exception e) {
							return value;
						}
					}
					return value;
				}

				@Override
				public Object processArrayValue(Object value,
						JsonConfig jsonConfig) {
					// TODO Auto-generated method stub
					return null;
				}
			};
			jsonConfig.registerJsonValueProcessor(Date.class, dateprocessor);
			jsonConfig.registerJsonPropertyNameProcessor(Card.class, processor);
			jsonConfig
					.registerJsonPropertyNameProcessor(Skill.class, processor);
			jsonConfig.registerJsonPropertyNameProcessor(UserCard.class,
					processor);
			jsonConfig.registerJsonPropertyNameProcessor(Rune.class, processor);
			jsonConfig.registerJsonPropertyNameProcessor(FightResult.class,
					processor);
			jsonConfig.registerJsonPropertyNameProcessor(Player.class,
					processor);
			jsonConfig.registerJsonPropertyNameProcessor(Battle.class,
					processor);
			jsonConfig.registerJsonPropertyNameProcessor(FightCard.class,
					processor);
			jsonConfig
					.registerJsonPropertyNameProcessor(Round.class, processor);
			jsonConfig.registerJsonPropertyNameProcessor(Opp.class, processor);
			jsonConfig.registerJsonPropertyNameProcessor(ExtData.class,
					processor);
			jsonConfig.registerJsonPropertyNameProcessor(Soul.class, processor);
		}
		JSONObject fightObj = JSONObject.fromObject(result, jsonConfig);
		// TODO Auto-generated method stub
		fightDao.addFightInfo(result.getBattleId(), result.getAttackPlayer()
				.getRoleId(), result.getDefendPlayer().getRoleId(), fightObj
				.toString(), result.getType(), 1);
	}

	@Override
	public void saveFight(FightResult result) {
		battleCachedManager.set("battle_"+result.getBattleId(), 5 * 60 * 60, result);
	}

	@Override
	public FightResult getFight(String battleId) {
		// TODO Auto-generated method stub
		return (FightResult) battleCachedManager.get("battle_"+battleId);
	}

	public MemCachedManager getBattleCachedManager() {
		return battleCachedManager;
	}

	public void setBattleCachedManager(MemCachedManager battleCachedManager) {
		this.battleCachedManager = battleCachedManager;
	}

	@Override
	public void setFightResult(Player aPlayer, Player dPlayer,
			FightResult fightResult, String stageId, Role role) {
		if (aPlayer.getHP() <= 0
				|| (aPlayer.getFightCards().size() == 0
						&& aPlayer.getPreCards().size() == 0 && aPlayer
						.getHandsCards().size() == 0)) {
			fightResult.setWin(2);
			Stage stage = stageManager.getStageById(Integer.parseInt(stageId));
			UserStage userStage = userStageManager.getUserStageByRoleIdStageId(
					role.getRoleId(), Integer.parseInt(stageId));
			StageLevel stageLevel = stage.getLevels().get(
					userStage == null ? 0 : userStage.getFinishedStage());
			if (stage.getRank() == 7) {
				roleManager.updateFreshStep(4, 2, role.getRoleId());
			}
			List<String> bonus = new ArrayList<String>();
			bonus.add("Exp_" + stageLevel.getBonusLoseExp());
			bonus.add("Coins_" + stageLevel.getBonusLoseGold());
			fightResult.getExtData().setBonus(bonus);
			fightResult.getExtData().setStarUp(0);
			// 增加数据库奖励值
			roleManager.addCoin(role.getRoleName(), stageLevel
					.getBonusLoseGold());
			roleManager
					.addExp(role.getRoleName(), stageLevel.getBonusLoseExp());
			roleManager.updateEnergy(role.getRoleName(), stageLevel
					.getEnergyExplore());
			role = roleManager.getRoleById(role.getRoleId());
			fightResult.getExtData().setExp(role.getExp());
			fightResult.getExtData().setPrevExp(role.getPrevExp());
			fightResult.getExtData().setNextExp(role.getNextExp());
			fightResult.getExtData().setUserLevel(role.getLevel());
			List<Salary> salaries = new ArrayList<Salary>();
			// 铜钱奖励
			Salary salary = new Salary();
			salary.setRoleId(role.getRoleId());
			salary.setTime(new Date());
			salary.setType(SalaryEnum.StageSalary.getValue());
			salary.setAwardType(1);
			salary.setAwardValue(stageLevel.getBonusLoseGold());
			salaries.add(salary);
			// 经验奖励
			salary = new Salary();
			salary.setRoleId(role.getRoleId());
			salary.setTime(new Date());
			salary.setType(SalaryEnum.StageSalary.getValue());
			salary.setAwardType(12);
			salary.setAwardValue(stageLevel.getBonusLoseExp());
			salaries.add(salary);
			fightResult.setSalaries(salaries);
			return;
		}
		if (dPlayer.getHP() <= 0
				|| (dPlayer.getFightCards().size() == 0
						&& dPlayer.getPreCards().size() == 0 && dPlayer
						.getHandsCards().size() == 0)) {
			fightResult.setWin(1);
			Stage stage = stageManager.getStageById(Integer.parseInt(stageId));
			UserStage userStage = userStageManager.getUserStageByRoleIdStageId(
					role.getRoleId(), Integer.parseInt(stageId));
			StageLevel stageLevel = stage.getLevels().get(
					userStage == null ? 0 : userStage.getFinishedStage());
			Legioner legioner = legionManager.getLegioner(role.getRoleId());
			if (legioner != null) {
				Legion legion = legionManager.getLegionById(legioner
						.getLegionId());
				if (legion != null) {
					List<LegionTech> techs = legionManager.getLegionTechs();
					LegionTech legionTech;
					for (int i = 0; i < techs.size(); i++) {
						legionTech = techs.get(i);
						if (legion.getLegionLevel() >= legionTech
								.getLegionLevel()) {
							legionTech.setLock(1);
							Long resource = (Long) BeanUtils
									.getFieldValueByName(
											"contribute" + (i + 1), legion);
							int level = legionTech.getLegionTechLevel(resource);
							if (level > 0) {
								if (legionTech.getType() == 3) {
									stageLevel.setBonusWinExp(stageLevel.getBonusWinExp()+stageLevel.getBonusWinExp()*level/100);
								}
								if (legionTech.getType() == 4) {
									stageLevel.setBonusWinGold(stageLevel.getBonusWinGold()+stageLevel.getBonusWinGold()*level/100);
								}
							}
						}
					}

				}
			}
			List<String> bonus = new ArrayList<String>();
			bonus.add("Exp_" + stageLevel.getBonusWinExp());
			bonus.add("Coins_" + stageLevel.getBonusWinGold());
			fightResult.getExtData().setBonus(bonus);

			roleManager.addCoin(role.getRoleName(), stageLevel
					.getBonusWinGold());
			roleManager.addExp(role.getRoleName(), stageLevel.getBonusWinExp());
			roleManager.updateEnergy(role.getRoleName(), stageLevel
					.getEnergyExplore());

			role = roleManager.getRoleById(role.getRoleId());
			fightResult.getExtData().setExp(role.getExp());
			fightResult.getExtData().setPrevExp(role.getPrevExp());
			fightResult.getExtData().setNextExp(role.getNextExp());
			fightResult.getExtData().setUserLevel(role.getLevel());
			if (this.getFightResultForCompareConditions(aPlayer, dPlayer,
					fightResult, stageId, role) > 0) {
				List<String> firstBonusWin = new ArrayList<String>();
				firstBonusWin.add("Card_" + stageLevel.getFirstBonusWinCard());
				if (stageLevel.getFirstBonusWinCard() > 0) {
					userCardManager.saveUserCard(stageLevel
							.getFirstBonusWinCard(), role.getRoleId());
				}
				firstBonusWin.add("Rune_" + stageLevel.getFirstBonusWinRune());
				if (stageLevel.getFirstBonusWinRune() > 0) {
					userRuneManager.saveUserRune(stageLevel
							.getFirstBonusWinRune(), role.getRoleId());
				}
				fightResult.getExtData().setFirstBonusWin(firstBonusWin);
				if (userStage == null) {
					userStage = new UserStage();
					userStage.setCounterAttackTime(new Long(0));
					userStage.setFinishedStage(1);
					userStage.setLastFinishedTime(new Date());
					userStage.setMapId(stage.getMapId());
					userStage.setStageId(Integer.parseInt(stageId));
					userStage.setType(stage.getType());
					userStage.setRoleId(role.getRoleId());
					userStageManager.saveUserStage(userStage);
				} else {
					userStageManager.updateUserStage(userStage
							.getFinishedStage() + 1, userStage.getId());
				}
				if (userStage.getStageId() == 1
						&& userStage.getFinishedStage() == 1) {
					roleManager.updateFreshStep(1, 22, role.getRoleId());
				}
				if (userStage.getStageId() == 7
						&& userStage.getFinishedStage() == 1) {
					roleManager.updateFreshStep(4, 3, role.getRoleId());
				}
				if (userStage.getStageId() == 7
						&& userStage.getFinishedStage() == 3) {
					roleManager.updateFreshStep(4, 17, role.getRoleId());
				}
				if (userStage.getStageId() == 7
						&& userStage.getFinishedStage() == 2) {
					roleManager.updateFreshStep(4, 17, role.getRoleId());
				}
				if (userStage.getStageId() == 14
						&& userStage.getFinishedStage() == 1) {
					roleManager.updateFreshStep(0, 2, role.getRoleId());
				}
				fightResult.getExtData().setStarUp(1);
			} else {
				if (stage.getRank() == 7) {
					roleManager.updateFreshStep(4, 2, role.getRoleId());
				}
				fightResult.getExtData().setStarUp(0);
			}

			// List<StageLevelColor> stageLevelColors =
			// stageLevelManager.getStageLevelColor(stageLevel.getId());
			List<Salary> salaries = new ArrayList<Salary>();
			// 铜钱奖励
			Salary salary = new Salary();
			salary.setRoleId(role.getRoleId());
			salary.setTime(new Date());
			salary.setType(SalaryEnum.StageSalary.getValue());
			salary.setAwardType(1);
			salary.setAwardValue(stageLevel.getBonusWinGold());
			salaries.add(salary);
			// 经验奖励
			salary = new Salary();
			salary.setRoleId(role.getRoleId());
			salary.setTime(new Date());
			salary.setType(SalaryEnum.StageSalary.getValue());
			salary.setAwardType(12);
			salary.setAwardValue(stageLevel.getBonusWinExp());
			salaries.add(salary);

			// Random rnd = new Random();
			// if(!CollectionUtils.isEmpty(stageLevelColors)){
			// for(StageLevelColor stageLevelColor:stageLevelColors){
			// if(rnd.nextInt(100)<stageLevelColor.getColor()){
			// salary = new Salary();
			// salary.setRoleId(role.getRoleId());
			// salary.setTime(new Date());
			// salary.setAwardType(stageLevelColor.getType());
			// salary.setAwardValue(stageLevelColor.getValue());
			// salary.setType(SalaryEnum.StageSalary.getValue());
			// salaryManager.extendSalaryAdd(salary.getAwardType(),
			// salary.getAwardValue(), role.getRoleId());
			// salaries.add(salary);
			// break;
			// }
			// }
			//				
			// }
			fightResult.setSalaries(salaries);
			if (stage.getMapId() == 1 && stage.getStageId() <= 6) {
				UserAchievement userAchievement = achievementManager
						.getUserAchievementById(role.getRoleId(), 30);
				if (userAchievement == null) {
					achievementManager.saveUserAchievement(30,
							role.getRoleId(), stage.getStageId());
				}
				if (userAchievement != null
						&& userAchievement.getFinishState() < stage
								.getStageId()) {
					achievementManager.saveUserAchievement(30,
							role.getRoleId(), stage.getStageId());
				}
			}
			if (stage.getMapId() <= 4 && stage.getStageId() == 1) {
				UserAchievement userAchievement = achievementManager
						.getUserAchievementById(role.getRoleId(), 31);
				if (userAchievement == null) {
					achievementManager.saveUserAchievement(31,
							role.getRoleId(), stage.getMapId());
				}
				if (userAchievement != null
						&& userAchievement.getFinishState() < stage.getMapId()) {
					achievementManager.saveUserAchievement(31,
							role.getRoleId(), stage.getMapId());
				}
			}
			if (aPlayer.getHP() >= aPlayer.getRemainHP()) {
				UserAchievement userAchievement = achievementManager
						.getUserAchievementById(role.getRoleId(), 48);
				if (userAchievement == null) {
					achievementManager.saveUserAchievement(48,
							role.getRoleId(), 1);
				}
			}
			int star = 0;
			List<UserStage> userStages = userStageManager
					.getUserStageByRoleIdMapId(role.getRoleId(), 1);
			if (!CollectionUtils.isEmpty(userStages)) {
				for (UserStage userStage2 : userStages) {
					star = star + userStage2.getFinishedStage();
				}
				if (star <= 18) {
					UserAchievement userAchievement = achievementManager
							.getUserAchievementById(role.getRoleId(), 50);
					if (userAchievement == null) {
						achievementManager.saveUserAchievement(50, role
								.getRoleId(), star);
					}
					if (userAchievement != null
							&& userAchievement.getFinishState() < 18) {
						achievementManager.saveUserAchievement(50, role
								.getRoleId(), star);
					}
				}
			}
			if (stage.getMapId() == 8 && stage.getStageId() <= 11) {
				UserAchievement userAchievement = achievementManager
						.getUserAchievementById(role.getRoleId(), 51);
				if (userAchievement == null) {
					achievementManager.saveUserAchievement(51,
							role.getRoleId(), stage.getStageId());
				}
				if (userAchievement != null
						&& userAchievement.getFinishState() < stage
								.getStageId()) {
					achievementManager.saveUserAchievement(51,
							role.getRoleId(), stage.getStageId());
				}
			}
			int remainRace = cardManager.getCardById(
					new Long(aPlayer.getCards().get(0).getCardId())).getRace();
			int race = remainRace;
			for (FightCard card : aPlayer.getCards()) {
				if (remainRace != cardManager.getCardById(
						new Long(card.getCardId())).getRace()) {
					race = cardManager.getCardById(new Long(card.getCardId()))
							.getRace();
				}
			}
			if (race == remainRace) {
				if (race == 1) {
					// 仙族
					UserAchievement userAchievement = achievementManager
							.getUserAchievementById(role.getRoleId(), 55);
					if (userAchievement == null) {
						achievementManager.saveUserAchievement(55, role
								.getRoleId(), 1);
					}
				}
				if (race == 2) {
					// 人族
					UserAchievement userAchievement = achievementManager
							.getUserAchievementById(role.getRoleId(), 53);
					if (userAchievement == null) {
						achievementManager.saveUserAchievement(53, role
								.getRoleId(), 1);
					}
				}
				if (race == 3) {
					// 妖族
					UserAchievement userAchievement = achievementManager
							.getUserAchievementById(role.getRoleId(), 54);
					if (userAchievement == null) {
						achievementManager.saveUserAchievement(54, role
								.getRoleId(), 1);
					}
				}
				if (race == 4) {
					// 佛族
					UserAchievement userAchievement = achievementManager
							.getUserAchievementById(role.getRoleId(), 52);
					if (userAchievement == null) {
						achievementManager.saveUserAchievement(52, role
								.getRoleId(), 1);
					}
				}
			}
		}

	}

	private int getFightResultForCompareConditions(Player aPlayer,
			Player dPlayer, FightResult fightResult, String stageId, Role role) {
		Stage stage = stageManager.getStageById(Integer.parseInt(stageId));
		UserStage userStage = userStageManager.getUserStageByRoleIdStageId(role
				.getRoleId(), Integer.parseInt(stageId));
		StageLevel stageLevel = stage.getLevels().get(
				userStage == null ? 0 : userStage.getFinishedStage());
		StageLevelConditions stageLevelConditions = stageLevelManager
				.getStageConditionsByStageConditionsId(stageLevel
						.getAchievementId());
		if (stageLevelConditions.getType() == 1) {
			if (dPlayer.getHP() <= 0
					|| (dPlayer.getFightCards().size() == 0
							&& dPlayer.getPreCards().size() == 0 && dPlayer
							.getHandsCards().size() == 0)) {
				return 1;
			}
		}
		if (stageLevelConditions.getType() == 2) {
			if ((dPlayer.getFightCards().size() == 0
					&& dPlayer.getPreCards().size() == 0 && dPlayer
					.getHandsCards().size() == 0)) {
				return 1;
			}
		}
		if (stageLevelConditions.getType() == 3) {
			if (aPlayer.getHP() >= aPlayer.getRemainHP()
					* stageLevelConditions.getValue1() / 100) {
				return 1;
			}
		}
		if (stageLevelConditions.getType() == 4) {
			if (fightResult.getBattle().getRounds().get(
					fightResult.getBattle().getRounds().size() - 1).getRound() < stageLevelConditions
					.getValue1()) {
				return 1;
			}
		}
		if (stageLevelConditions.getType() == 5) {
			if (dPlayer.getHP() <= 0) {
				return 1;
			}
		}
		if (stageLevelConditions.getType() == 6) {
			if (aPlayer.getTumbCards().size() < stageLevelConditions
					.getValue1()) {
				return 1;
			}
		}
		if (stageLevelConditions.getType() == 7) {
			int num = 0;
			Card card;
			for (FightCard fightCard : aPlayer.getCards()) {
				card = cardManager.getCardById(new Long(fightCard.getCardId()));
				if (card.getColor() == stageLevelConditions.getValue1()) {
					num++;
				}
			}
			if (num >= stageLevelConditions.getValue2()) {
				return 1;
			}
		}
		if (stageLevelConditions.getType() == 8) {
			int num = 0;
			Card card;
			for (FightCard fightCard : aPlayer.getCards()) {
				card = cardManager.getCardById(new Long(fightCard.getCardId()));
				if (card.getRace() == stageLevelConditions.getValue1()) {
					num++;
				}
			}
			if (num >= stageLevelConditions.getValue2()) {
				return 1;
			}
		}
		if (stageLevelConditions.getType() == 9) {
			if (aPlayer.getRunes().size() == 0) {
				return 1;
			}
		}
		if (stageLevelConditions.getType() == 10) {
			Rune rune;
			for (FightRune fightRune : aPlayer.getRunes()) {
				rune = runeManager.getRuneById(fightRune.getRuneId());
				if (rune.getProperty() == stageLevelConditions.getValue1()) {
					return 1;
				}
			}
		}
		return 0;
	}

	public RoleManager getRoleManager() {
		return roleManager;
	}

	public void setRoleManager(RoleManager roleManager) {
		this.roleManager = roleManager;
	}

	public StageManager getStageManager() {
		return stageManager;
	}

	public void setStageManager(StageManager stageManager) {
		this.stageManager = stageManager;
	}

	public UserStageManager getUserStageManager() {
		return userStageManager;
	}

	public void setUserStageManager(UserStageManager userStageManager) {
		this.userStageManager = userStageManager;
	}

	public AchievementManager getAchievementManager() {
		return achievementManager;
	}

	public void setAchievementManager(AchievementManager achievementManager) {
		this.achievementManager = achievementManager;
	}

	public CardManager getCardManager() {
		return cardManager;
	}

	public void setCardManager(CardManager cardManager) {
		this.cardManager = cardManager;
	}

	public StageLevelManager getStageLevelManager() {
		return stageLevelManager;
	}

	public void setStageLevelManager(StageLevelManager stageLevelManager) {
		this.stageLevelManager = stageLevelManager;
	}

	public SalaryManager getSalaryManager() {
		return salaryManager;
	}

	public void setSalaryManager(SalaryManager salaryManager) {
		this.salaryManager = salaryManager;
	}

	public RuneManager getRuneManager() {
		return runeManager;
	}

	public void setRuneManager(RuneManager runeManager) {
		this.runeManager = runeManager;
	}

	public UserCardManager getUserCardManager() {
		return userCardManager;
	}

	public void setUserCardManager(UserCardManager userCardManager) {
		this.userCardManager = userCardManager;
	}

	public UserRuneManager getUserRuneManager() {
		return userRuneManager;
	}

	public void setUserRuneManager(UserRuneManager userRuneManager) {
		this.userRuneManager = userRuneManager;
	}

	private boolean isWin(Player aPlayer, Player dPlayer) {
		if (aPlayer.getHP() <= 0
				|| (aPlayer.getFightCards().size() == 0
						&& aPlayer.getPreCards().size() == 0 && aPlayer
						.getHandsCards().size() == 0)) {
			return true;
		}
		if (dPlayer.getHP() <= 0
				|| (dPlayer.getFightCards().size() == 0
						&& dPlayer.getPreCards().size() == 0 && dPlayer
						.getHandsCards().size() == 0)) {
			return true;
		}
		return false;
	}

	private boolean isNewCard(List<FightCard> fightCards, FightCard fightCard) {
		for (int i = 0; i < fightCards.size(); i++) {
			if (fightCards.get(i).getUUID().equals(fightCard.getUUID())) {
				return true;
			}
		}
		return false;
	}

	public Map<String, FightCard> resetFightCard(
			Map<String, FightCard> fightCards) {
		int j = 0;
		int s = 0;
		Map<String, FightCard> fightCards1 = new HashMap<String, FightCard>();
		while (!fightCards.isEmpty()) {
			if (fightCards.get(String.valueOf(j)) != null) {
				fightCards1.put(String.valueOf(s), fightCards.get(String
						.valueOf(j)));
				fightCards.remove(String.valueOf(j));
				s++;
			}
			j++;
		}
		return fightCards1;
	}

	private int getLastIndexFromFightregion(Map<String, FightCard> fightCards) {
		String j = "0";
		Iterator<String> ite = fightCards.keySet().iterator();
		String key;
		while (ite.hasNext()) {
			key = ite.next();
			if (Integer.parseInt(key) > Integer.parseInt(j)) {
				j = key;
			}
		}
		return Integer.parseInt(j) + 1;
	}

	public SkillManager getSkillManager() {
		return skillManager;
	}

	public void setSkillManager(SkillManager skillManager) {
		this.skillManager = skillManager;
	}

	public FightDao getFightDao() {
		return fightDao;
	}

	public void setFightDao(FightDao fightDao) {
		this.fightDao = fightDao;
	}

	public LegionManager getLegionManager() {
		return legionManager;
	}

	public void setLegionManager(LegionManager legionManager) {
		this.legionManager = legionManager;
	}

}

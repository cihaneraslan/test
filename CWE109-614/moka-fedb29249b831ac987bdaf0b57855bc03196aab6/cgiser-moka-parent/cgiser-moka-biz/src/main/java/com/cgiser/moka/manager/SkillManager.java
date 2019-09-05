package com.cgiser.moka.manager;

import java.util.List;

import com.cgiser.moka.model.FightCard;
import com.cgiser.moka.model.FightRune;
import com.cgiser.moka.model.Opp;
import com.cgiser.moka.model.Player;
import com.cgiser.moka.model.Skill;

public interface SkillManager {
	/**
	 * 获取所有的技能
	 * @return
	 */
	public List<Skill> getAllSkill();
	/**
	 * 根据Id获取技能
	 * @param skillId
	 * @return
	 */
	public Skill getSkillById(int skillId);
	/**
	 * 根据名称获取技能
	 * @param skillId
	 * @return
	 */
	public Skill getSkillByName(String skillName);
	/**
	 * 卡牌上场时
	 * @param aPlayer
	 * @param dPlayer
	 * @param newFightCards
	 * @return
	 */
	public List<Opp> getSkillModelInCardToPlay(Player aPlayer,Player dPlayer,List<FightCard> newFightCards);
	/**
	 * 卡牌上场后
	 * @param skill
	 * @param aPlayer
	 * @param dPlayer
	 * @param fightCard
	 * @return
	 */
	public List<Opp> getSkillModelByType(Skill skill,Player aPlayer,Player dPlayer,FightCard fightCard);
	/**
	 * 获取主动技能,物理攻击前
	 * @param skills
	 * @param aPlayer
	 * @param dPlayer
	 * @param fightCard
	 * @param skillBuff
	 * @param isNewCard
	 * @return
	 */
	public List<Opp> getActiveSkillModelByType(List<Skill> skills,Player aPlayer,Player dPlayer,FightCard fightCard,List<Skill> skillBuff,boolean isNewCard);
	/**
	 * 获取星辰技能执行模型
	 * @param fightRune
	 * @param aPlayer
	 * @param dPlayer
	 * @param round
	 * @return
	 */
	public List<Opp> getRuneOpps(FightRune fightRune,Player aPlayer,Player dPlayer,int round);
}

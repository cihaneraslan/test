package com.cgiser.moka.manager;

import java.util.List;

import com.cgiser.moka.model.FightCard;
import com.cgiser.moka.model.Opp;
import com.cgiser.moka.model.Soul;

public interface SoulManager {
	/**
	 * 获取所有的武魂战魂
	 * @return
	 */
	public List<Soul> getSouls();
	/**
	 * 根据Id获取武魂战魂
	 * @param id
	 * @return
	 */
	public Soul getSoulById(int id);
	/**
	 * 
	 * @return
	 */
	public Soul randomSoul(int id1,int id2);
	/**
	 * 获取soul加成的技能模型
	 */
	public List<Opp> getOppsBySoul(Soul soul,FightCard fightCard);
}

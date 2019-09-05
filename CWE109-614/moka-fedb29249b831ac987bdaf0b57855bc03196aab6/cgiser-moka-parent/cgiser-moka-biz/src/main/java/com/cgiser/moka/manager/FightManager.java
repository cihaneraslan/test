package com.cgiser.moka.manager;

import java.util.List;

import com.cgiser.moka.model.FightResult;
import com.cgiser.moka.model.Opp;
import com.cgiser.moka.model.Player;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.model.Round;

public interface FightManager {
	/**
	 * 战斗进行过程
	 * @param attackPlayer
	 * @param defendPlayer
	 * @param type
	 */
	public FightResult runFight(Player attackPlayer,Player defendPlayer,int type);
	/**
	 * 循环战斗回合1
	 * @param attackPlayer
	 * @param defendPlayer
	 */
	public void runRound(Player attackPlayer,Player defendPlayer,int round,List<Round> rounds);
	/**
	 * 循环战斗回合2 仅在手动战斗时使用
	 * @param attackPlayer
	 * @param defendPlayer
	 * @param round
	 * @param rounds
	 */
	public void runRound1(Player attackPlayer,Player defendPlayer,int round,List<Round> rounds,FightResult fightResult,String[] uuids,List<Opp> oppsNewCard);
	/**
	 * 保存战斗结果到缓存
	 * @param result
	 */
	public void saveFight(FightResult result);
	/**
	 * 保存战斗结果到数据库
	 * @param result
	 */
	public void saveFightRel(FightResult result);
	/**
	 * 获取正在进行的战斗数据
	 * @param battleId
	 * @return
	 */
	public FightResult getFight(String battleId);
	/**
	 * 战斗结束设置战斗结果
	 * @param aPlayer
	 * @param dPlayer
	 * @param fightResult
	 * @param stageId
	 * @param role
	 */
	public void setFightResult(Player aPlayer,Player dPlayer,FightResult fightResult,String stageId,Role role);
}

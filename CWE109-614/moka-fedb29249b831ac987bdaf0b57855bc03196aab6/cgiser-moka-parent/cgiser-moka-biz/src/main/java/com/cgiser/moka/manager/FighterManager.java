package com.cgiser.moka.manager;

import com.cgiser.moka.model.FightResult;
import com.cgiser.moka.model.Player;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.model.VirtualRole;

public interface FighterManager {
	/**
	 * 初始化玩家数据
	 * @param role
	 * @return
	 */
	public Player initPlayer(Role role,String name);
	/**
	 * 初始化玩家数据
	 * @param role
	 * @return
	 */
	public Player initVirtualPlayer(VirtualRole role,String name);
	
	/**
	 * 构造关卡英雄数据
	 * @param role
	 * @return
	 */
	public Player createHeroPlayer(int stageId,Role role);
	
	/**
	 * 战斗完成后结果处理,主要是更新战斗CD
	 */
	public void handlerFightResult(Player aPlayer,Player dPlayer,int type,FightResult result);
}

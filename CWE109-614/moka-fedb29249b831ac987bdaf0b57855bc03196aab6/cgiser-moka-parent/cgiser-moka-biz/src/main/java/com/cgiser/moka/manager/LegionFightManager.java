package com.cgiser.moka.manager;

import java.util.Date;
import java.util.List;

import com.cgiser.moka.model.LegionFight;

public interface LegionFightManager {
//	/**
//	 * 获取军团战可占领城市
//	 * @return
//	 */
//	public List<LegionCity> getLegionCityInfos();
//	public LegionCity getLegionCityInfo(String name);
//	/**
//	 * 开启军团战
//	 * @param name
//	 */
//	public void startLegionFight(String name);
//	/**
//	 * 军团竞价
//	 * @return
//	 */
//	public Long bidLegionFight(String name,String legionName,int coins);
//	/**
//	 * 获取指定军团的竞价
//	 * @return
//	 */
//	public BidLegion getBidLegion(String cityName,String legionName);
//	/**
//	 * 获取当前所有的竞价军团
//	 * @return
//	 */
//	public List<BidLegion> getBidLegions(String name);
//	/**
//	 * 获取当前出价最高的两个军团
//	 * @return
//	 */
//	public List<Legion> getMaxBidLegion(String name);
//	public LegionCity inLegionFight(String name,Long roleId,String roleName);
//	/**
//	 * 获取军团战对手
//	 * @param city
//	 * @param LegionFighter
//	 * @return
//	 */
//	public LegionFighter getDefend(String city,Role role);
	/**
	 * 发起帮派战
	 * @param attack
	 * @param defend
	 * @param date
	 * @return
	 */
	public Long addLegionFight(Long roleId,Long attack,Long defend,Date date);
	/**
	 * 获取自己帮派的帮派战
	 * @param attack
	 * @param defend
	 * @param date
	 * @return
	 */
	public LegionFight getLegionFight(Long roleId);
	/**
	 * 获取自己帮派的帮派战
	 * @param date
	 * @return
	 */
	public LegionFight getEndLegionFightById(Long id);
	/**
	 * 获取所有未结束的帮派战
	 * @return
	 */
	public List<LegionFight> getLegionFights();
	/**
	 * 获取指定帮派是否被进攻
	 * @param defend
	 * @return
	 */
	public LegionFight getLegionFightBydefend(Long defend);
	/**
	 * 获取指定帮派
	 * @param defend
	 * @return
	 */
	public LegionFight getLegionFightById(Long id);
	/**
	 * 获取本帮派正在进行的帮派战
	 * @param defend
	 * @return
	 */
	public LegionFight getLegionFightByattack(Long attack);
	/**
	 * 修改帮派守卫状态为被打败
	 * @param legionFightId
	 * @param id
	 * @param roleId
	 * @return
	 */
	public int defeatGuard(Long legionFightId,int id,Long roleId);
	/**
	 * 扫描所有的帮派战，判断是否结束
	 */
	public void scanAllLegionFight();
	/**
	 * 判断帮派战是否胜利
	 * @param legionFight
	 * @return
	 */
	public int isWin(LegionFight legionFight);
	/**
	 * 修改帮派战的胜利状态和掠夺资产
	 * @return
	 */
	public int updateLegionFightWinResource(Long id,int win,Long resource);
	/**
	 * 判断帮派战是否结束
	 * @param legionFight
	 * @return
	 */
	public boolean isEnd(LegionFight legionFight);
	/**
	 * 主动结束帮派战
	 * @param legionFight
	 * @return
	 */
	public boolean end(LegionFight legionFight);
	
}

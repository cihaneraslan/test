package com.cgiser.moka.manager;

import java.util.List;

import com.cgiser.moka.model.StageLevel;
import com.cgiser.moka.model.StageLevelColor;
import com.cgiser.moka.model.StageLevelConditions;

public interface StageLevelManager {
	/**
	 * 获取关卡难度
	 * @param stageId
	 * @return
	 */
	public List<StageLevel> getSageLevelsByStageId(int stageId);
	/**
	 * 获取关卡难度
	 * @param stageLevelId
	 * @return
	 */
	public StageLevel getSageLevelByStageLevelId(int stageLevelId);
	/**
	 * 获取关卡奖励
	 * @param stageLevelId
	 * @return
	 */
	public List<StageLevelColor> getStageLevelColor(int stageLevelId);
	/**
	 * 增加关卡奖励
	 * @param stageLevelId
	 * @return
	 */
	public Long addStageLevelColor(int stageLevelId,int type,int value,Float color);
	/**
	 * 删除关卡奖励
	 * @param stageLevelId
	 * @return
	 */
	public int deleteStageLevelColor(Long colorId);
	/**
	 * 装备武器
	 * @param stageLevelId
	 * @param soulList
	 * @return
	 */
	public int installSoul(int stageLevelId,String soulList);
	/**
	 * 获取关卡通过条件ID
	 * @return
	 */
	public StageLevelConditions getStageConditionsByStageConditionsId(int id);
}

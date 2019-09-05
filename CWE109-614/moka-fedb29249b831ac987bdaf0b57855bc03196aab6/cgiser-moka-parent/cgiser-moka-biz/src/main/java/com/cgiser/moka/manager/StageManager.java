package com.cgiser.moka.manager;

import java.util.List;

import com.cgiser.moka.model.Stage;
import com.cgiser.moka.model.Story;

public interface StageManager {
	/**
	 * 获取所有关卡
	 * @return
	 */
	public List<Stage> getStages();
	/**
	 * 获取指定地图的关卡
	 * @param mapId
	 * @return
	 */
	public List<Stage> getStagesByMapId(int mapId);
	/**
	 * 获取关卡
	 * @param stageId
	 * @return
	 */
	public Stage getStageById(int stageId);
	/**
	 * 获取关卡剧情
	 * @return
	 */
	public List<Story> getStorys();
}

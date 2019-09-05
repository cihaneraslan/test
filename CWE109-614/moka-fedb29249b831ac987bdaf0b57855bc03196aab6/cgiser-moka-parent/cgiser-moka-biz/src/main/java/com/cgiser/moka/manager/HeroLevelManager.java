package com.cgiser.moka.manager;

import java.util.List;

import com.cgiser.moka.model.HeroLevel;

public interface HeroLevelManager {
	/**
	 * 获取所有的英雄级别数据
	 * @return
	 */
	public List<HeroLevel> getHeroLevels();
	/**
	 * 获取英雄级别数据
	 * @param level
	 * @return
	 */
	public HeroLevel getHeroLevelByLevel(int level);
}

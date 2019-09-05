package com.cgiser.moka.manager;

import java.util.List;

import com.cgiser.moka.model.Achievement;
import com.cgiser.moka.model.UserAchievement;

public interface AchievementManager {
	/**
	 * 获取所有的成就
	 * @return
	 */
	public List<Achievement> getAllAchievements();
	/**
	 * 根据ID获取成就
	 * @param id
	 * @return
	 */
	public Achievement getAchievementById(int id);
	/**
	 * 保存用户达到的成就
	 * @param achievementId
	 * @param roleId
	 * @return
	 */
	public Long saveUserAchievement(int achievementId,Long roleId,int finishState);
	/**
	 * 获取用户已经达到的成就
	 * @param roleId
	 * @return
	 */
	public List<UserAchievement> getUserAchievements(Long roleId);
	/**
	 * 获取用户已经达到的某个成就
	 * @param roleId
	 * @return
	 */
	public UserAchievement getUserAchievementById(Long roleId,int achievementId);
}

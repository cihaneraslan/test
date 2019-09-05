package com.cgiser.moka.manager;

import com.cgiser.moka.model.RobRoleCache;

public interface RobRoleCacheManager {
	/**
	 * 获取入侵玩家缓存
	 * @param roleId
	 * @return
	 */
	public RobRoleCache getRobRoleCache(Long roleId);
	/**
	 * 修改入侵玩家缓存
	 * @param roleId
	 * @param robRoleCache
	 * @return
	 */
	public int updateRobRoleCache(Long roleId,RobRoleCache robRoleCache);
}

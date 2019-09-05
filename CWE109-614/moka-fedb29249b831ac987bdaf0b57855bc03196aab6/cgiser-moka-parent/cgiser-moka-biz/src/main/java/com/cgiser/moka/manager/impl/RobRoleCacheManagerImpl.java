package com.cgiser.moka.manager.impl;

import com.cgiser.core.common.cache.mem.MemCachedManager;
import com.cgiser.moka.dao.util.DigestUtils;
import com.cgiser.moka.manager.RobRoleCacheManager;
import com.cgiser.moka.model.RobRoleCache;

public class RobRoleCacheManagerImpl implements RobRoleCacheManager {
	private MemCachedManager robCachedManager;
	@Override
	public RobRoleCache getRobRoleCache(Long roleId) {
		// TODO Auto-generated method stub
		return (RobRoleCache)robCachedManager.get("rob_"+DigestUtils.digest(String.valueOf(roleId)));
	}

	@Override
	public int updateRobRoleCache(Long roleId, RobRoleCache robRoleCache) {
		// TODO Auto-generated method stub
		if(robCachedManager.set("rob_"+DigestUtils.digest(String.valueOf(roleId)), 0, robRoleCache)){
			return 1;
		}
		return 0;
	}

	public MemCachedManager getRobCachedManager() {
		return robCachedManager;
	}

	public void setRobCachedManager(MemCachedManager robCachedManager) {
		this.robCachedManager = robCachedManager;
	}

}

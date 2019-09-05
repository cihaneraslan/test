package com.cgiser.moka.manager.impl;

import java.util.Map;

import org.springframework.util.CollectionUtils;

import com.cgiser.moka.dao.VersionDao;
import com.cgiser.moka.manager.VersionManager;
import com.cgiser.moka.model.Version;

public class VersionManagerImpl implements VersionManager {
	VersionDao versionDao;
	@Override
	public Version getLastVersion() {
		// TODO Auto-generated method stub
		return MapToVersion(versionDao.getLastVersion());
	}
	private Version MapToVersion(Map<String,Object> map){
		if(CollectionUtils.isEmpty(map)){
			return null;
		}
		Version version = new Version();
		version.setAppurl(map.get("APPURL").toString());
		version.setAppversion(map.get("APPVERSION").toString());
		version.setHttp(map.get("HTTP").toString());
		version.setId(Integer.parseInt(map.get("ID").toString()));
		version.setStop(map.get("STOP")==null?"":map.get("STOP").toString());
		return version;
	}
	public VersionDao getVersionDao() {
		return versionDao;
	}
	public void setVersionDao(VersionDao versionDao) {
		this.versionDao = versionDao;
	}

}

package com.cgiser.moka.manager;

import com.cgiser.moka.model.Version;

public interface VersionManager {
	/**
	 * 获取最新版本
	 * @return
	 */
	public Version getLastVersion();
}

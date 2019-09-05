package com.cgiser.moka.manager;

import java.util.Map;

public interface LanguageManager {
	/**
	 * 根据语言包类型，获取语言包
	 * @param type
	 * @return
	 */
	public Map<String, String> getLanByLanType(String type);
}

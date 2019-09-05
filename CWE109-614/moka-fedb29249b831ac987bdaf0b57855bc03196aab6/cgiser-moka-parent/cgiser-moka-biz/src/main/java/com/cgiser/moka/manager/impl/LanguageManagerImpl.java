package com.cgiser.moka.manager.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import com.cgiser.moka.dao.LanguageDao;
import com.cgiser.moka.manager.LanguageManager;

public class LanguageManagerImpl implements LanguageManager {
	private LanguageDao languageDao;
	@Override
	public Map<String, String> getLanByLanType(String type) {
		List<Map<String,Object>> list = languageDao.getLanByLanType(type);
		return MapListToLanMap(list);
	}
	private Map<String,String> MapListToLanMap(List<Map<String,Object>> list){
		if(CollectionUtils.isEmpty(list)){
			return null;
		}
		Map<String,String> map = new HashMap<String, String>();
		for(int i=0;i<list.size();i++){
			map.put(list.get(i).get("T_KEY").toString(), list.get(i).get("T_VALUE").toString());
		}
		return map;
		
	}
	public LanguageDao getLanguageDao() {
		return languageDao;
	}
	public void setLanguageDao(LanguageDao languageDao) {
		this.languageDao = languageDao;
	}

}

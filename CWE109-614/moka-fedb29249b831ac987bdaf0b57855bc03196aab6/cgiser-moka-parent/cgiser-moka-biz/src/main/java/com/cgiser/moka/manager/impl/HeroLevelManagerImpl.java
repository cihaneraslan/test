package com.cgiser.moka.manager.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import com.cgiser.moka.dao.HeroLevelDao;
import com.cgiser.moka.manager.HeroLevelManager;
import com.cgiser.moka.model.HeroLevel;

public class HeroLevelManagerImpl implements HeroLevelManager {
	private HeroLevelDao heroLevelDao;
	@Override
	public HeroLevel getHeroLevelByLevel(int level) {
		// TODO Auto-generated method stub
		return MapToHeroLevel(heroLevelDao.getHeroLevelByLevel(level));
	}

	@Override
	public List<HeroLevel> getHeroLevels() {
		// TODO Auto-generated method stub
		return MapListToHeroLevelList(heroLevelDao.getHeroLevels());
	}
	private List<HeroLevel> MapListToHeroLevelList(List<Map<String,Object>> list){
		if(CollectionUtils.isEmpty(list)){
			return null;
		}
		List<HeroLevel> listHeroLevel = new ArrayList<HeroLevel>();
		for(int i=0;i<list.size();i++){
			listHeroLevel.add(MapToHeroLevel(list.get(i)));
		}
		return listHeroLevel;
	}
	private HeroLevel MapToHeroLevel(Map<String,Object> map){
		if(CollectionUtils.isEmpty(map)){
			return null;
		}
		HeroLevel heroLevel = new HeroLevel();
		heroLevel.setCost(Integer.parseInt(map.get("COST").toString()));
		heroLevel.setExp(Integer.parseInt(map.get("EXP").toString()));
		heroLevel.setFriendNum(Integer.parseInt(map.get("FRIENDNUM").toString()));
		heroLevel.setHp(Integer.parseInt(map.get("HP").toString()));
		heroLevel.setLevel(Integer.parseInt(map.get("LEVEL").toString()));
		return heroLevel;
	}

	public HeroLevelDao getHeroLevelDao() {
		return heroLevelDao;
	}

	public void setHeroLevelDao(HeroLevelDao heroLevelDao) {
		this.heroLevelDao = heroLevelDao;
	}

}

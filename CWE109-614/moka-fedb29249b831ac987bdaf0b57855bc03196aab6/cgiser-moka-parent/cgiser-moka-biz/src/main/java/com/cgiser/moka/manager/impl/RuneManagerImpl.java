package com.cgiser.moka.manager.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.cgiser.moka.dao.RuneDao;
import com.cgiser.moka.manager.RuneManager;
import com.cgiser.moka.model.Rune;

public class RuneManagerImpl implements RuneManager {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	private RuneDao runeDao;
	List<Rune> runes = new ArrayList<Rune>();
	List<Integer> star1 = new ArrayList<Integer>();
	List<Integer> star2 = new ArrayList<Integer>();
	List<Integer> star3 = new ArrayList<Integer>();
	List<Integer> star4 = new ArrayList<Integer>();
	List<Integer> star5 = new ArrayList<Integer>();
	@Override
	public Rune getRuneById(int runeId) {
		return MapToRune(runeDao.getRuneById(runeId));
	}

	@Override
	public List<Rune> getRunes() {
		if(CollectionUtils.isEmpty(runes)){
			List<Map<String,Object>> map = runeDao.getRunes();
			runes = MapListToRuneList(map);
		}
		return runes;
	}
	@Override
	public Integer RandomRune(int star) {
		if(CollectionUtils.isEmpty(runes)){
			runes = this.getRunes();
		}
		if(CollectionUtils.isEmpty(star1)){
			for(int i=0;i<runes.size();i++){
				if(runes.get(i).getColor()==1&&runes.get(i).getThinkGet()==1){
					star1.add(runes.get(i).getRuneId());
				}
				if(runes.get(i).getColor()==2&&runes.get(i).getThinkGet()==1){
					star2.add(runes.get(i).getRuneId());
				}
				if(runes.get(i).getColor()==3&&runes.get(i).getThinkGet()==1){
					star3.add(runes.get(i).getRuneId());
				}
				if(runes.get(i).getColor()==4&&runes.get(i).getThinkGet()==1){
					star4.add(runes.get(i).getRuneId());
				}
				if(runes.get(i).getColor()==5&&runes.get(i).getThinkGet()==1){
					star5.add(runes.get(i).getRuneId());
				}
			}
		}
		Random rnd = new Random();
		if(star==1){
			return star1.get(rnd.nextInt(star1.size()));
		}
		if(star==2){
			return star2.get(rnd.nextInt(star2.size()));
		}
		if(star==3){
			return star3.get(rnd.nextInt(star3.size()));
		}
		if(star==4){
			return star4.get(rnd.nextInt(star4.size()));
		}
		if(star==5){
			return star5.get(rnd.nextInt(star5.size()));
		}
		return 0;
	}
	private List<Rune> MapListToRuneList(List<Map<String,Object>> list){
		if(CollectionUtils.isEmpty(list)){
			return null;
		}
		List<Rune> runeList = new ArrayList<Rune>();
		for(int i=0;i<list.size();i++){
			runeList.add(MapToRune(list.get(i)));
		}
		return runeList;
		
	}
	private Rune MapToRune(Map<String,Object> map){
		if(CollectionUtils.isEmpty(map)){
			return null;
		}
		Rune rune = new Rune();
		rune.setBaseExp(Integer.parseInt(map.get("BASEEXP").toString()));
		rune.setColor(Integer.parseInt(map.get("COLOR").toString()));
		rune.setCondition(map.get("CONDITION").toString());
		rune.setExp(0);
		String strExp = map.get("EXPARRAY").toString();
		List<String> listStrExp = Arrays.asList(strExp.split("_"));
		List<Integer> expArray = new ArrayList<Integer>();
		for(int i = 0;i<listStrExp.size();i++){
			expArray.add(Integer.parseInt(listStrExp.get(i)));
		}
		rune.setExpArray(expArray);
		rune.setFragment(Integer.parseInt(map.get("FRAGMENT").toString()));
//		rune.setLevel(Integer.parseInt(map.get("LEVEL").toString()));
		rune.setLockSkill1(Integer.parseInt(map.get("LOCKSKILL1").toString()));
		rune.setLockSkill2(Integer.parseInt(map.get("LOCKSKILL2").toString()));
		rune.setLockSkill3(Integer.parseInt(map.get("LOCKSKILL3").toString()));
		rune.setLockSkill4(Integer.parseInt(map.get("LOCKSKILL4").toString()));
		rune.setLockSkill5(Integer.parseInt(map.get("LOCKSKILL5").toString()));
		rune.setPrice(Integer.parseInt(map.get("PRICE").toString()));
		rune.setProperty(Integer.parseInt(map.get("PROPERTY").toString()));
		rune.setRuneId(Integer.parseInt(map.get("RUNEID").toString()));
		rune.setRuneName(map.get("RUNENAME").toString());
		rune.setSkillConditionColor(Integer.parseInt(map.get("SKILLCONDITIONCOLOR").toString()));
		rune.setSkillConditionCompare(Integer.parseInt(map.get("SKILLCONDITIONCOMPARE").toString()));
		rune.setSkillConditionRace(Integer.parseInt(map.get("SKILLCONDITIONRACE").toString()));
		rune.setSkillConditionSlide(Integer.parseInt(map.get("SKILLCONDITIONSLIDE").toString()));
		rune.setSkillConditionType(Integer.parseInt(map.get("SKILLCONDITIONTYPE").toString()));
		rune.setSkillConditionValue(Integer.parseInt(map.get("SKILLCONDITIONVALUE").toString()));
		rune.setSkillTimes(Integer.parseInt(map.get("SKILLTIMES").toString()));
		rune.setThinkGet(Integer.parseInt(map.get("THINKGET").toString()));
		return rune;
	}
	public RuneDao getRuneDao() {
		return runeDao;
	}

	public void setRuneDao(RuneDao runeDao) {
		this.runeDao = runeDao;
	}



}

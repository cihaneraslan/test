package com.cgiser.moka.manager.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import com.cgiser.moka.dao.StageLevelDao;
import com.cgiser.moka.manager.StageLevelManager;
import com.cgiser.moka.model.StageLevel;
import com.cgiser.moka.model.StageLevelColor;
import com.cgiser.moka.model.StageLevelConditions;

public class StageLevelManagerImpl implements StageLevelManager {
	private StageLevelDao stageLevelDao;
	@Override
	public List<StageLevel> getSageLevelsByStageId(int stageId) {
		// TODO Auto-generated method stub
		return MapListToStageLevelList(stageLevelDao.getStageLevelsByStageId(stageId));
	}
	
	private List<StageLevel> MapListToStageLevelList(List<Map<String, Object>> list){
		if(CollectionUtils.isEmpty(list)){
			return null;
		}
		List<StageLevel> listStageLevel = new ArrayList<StageLevel>();
		for(int i=0;i<list.size();i++){
			listStageLevel.add(MapToStageLevel(list.get(i)));
		}
		return listStageLevel;
	}
	private StageLevel MapToStageLevel(Map<String, Object> map){
		if(CollectionUtils.isEmpty(map)){
			return null;
		}
		StageLevel stageLevel = new StageLevel();
		stageLevel.setAchievementId(Integer.parseInt(map.get("ACHIEVEMENTID").toString()));
		if(stageLevel.getAchievementId()>0){
			StageLevelConditions stageLevelConditions = this.getStageConditionsByStageConditionsId(stageLevel.getAchievementId());
			if(stageLevelConditions!=null){
				stageLevel.setAchievementText(stageLevelConditions.getDesc());
			}
		}
		stageLevel.setAddedBonus(Integer.parseInt(map.get("ADDEDBONUS").toString()));
		stageLevel.setBonusExplore(Integer.parseInt(map.get("BONUSEXPLORE").toString()));
		stageLevel.setBonusLoseExp(Integer.parseInt(map.get("BONUSLOSEEXP").toString()));
		stageLevel.setBonusLoseGold(Integer.parseInt(map.get("BONUSLOSEGOLD").toString()));
		stageLevel.setBonusWinExp(Integer.parseInt(map.get("BONUSWINEXP").toString()));
		stageLevel.setBonusWinGold(Integer.parseInt(map.get("BONUSWINGOLD").toString()));
		stageLevel.setCardList(map.get("CARDLIST").toString());
		stageLevel.setEnergyExpEnd(Integer.parseInt(map.get("ENERGYEXPEND").toString()));
		stageLevel.setEnergyExplore(Integer.parseInt(map.get("ENERGYEXPLORE").toString()));
		stageLevel.setFirstBonusWinCard(Integer.parseInt(map.get("FIRSTBONUSWINCARD").toString()));
		stageLevel.setFirstBonusWinRune(Integer.parseInt(map.get("FIRSTBONUSWINRUNE").toString()));
		stageLevel.setHeroLevel(Integer.parseInt(map.get("HEROLEVEL").toString()));
		stageLevel.setLevel(Integer.parseInt(map.get("LEVEL").toString()));
		stageLevel.setHint(Integer.parseInt(map.get("HINT").toString()));
		stageLevel.setRuneList(map.get("RUNELIST").toString());
		stageLevel.setStageId(Integer.parseInt(map.get("STAGEID").toString()));
		stageLevel.setId(Integer.parseInt(map.get("ID").toString()));
		List<StageLevelColor> stageLevelColors = this.getStageLevelColor(Integer.parseInt(map.get("ID").toString()));
		stageLevel.setStageLevelColors(stageLevelColors);
		stageLevel.setSoulList(map.get("SOULLIST").toString());
		return stageLevel;
	}

	public StageLevelDao getStageLevelDao() {
		return stageLevelDao;
	}

	public void setStageLevelDao(StageLevelDao stageLevelDao) {
		this.stageLevelDao = stageLevelDao;
	}

	@Override
	public List<StageLevelColor> getStageLevelColor(int stageLevelId) {
		// TODO Auto-generated method stub
		return MapListToStageLevelColorList(stageLevelDao.getStageLevelColorByStageLevelId(stageLevelId));
	}
	private List<StageLevelColor> MapListToStageLevelColorList(List<Map<String, Object>> list){
		if(CollectionUtils.isEmpty(list)){
			return null;
		}
		List<StageLevelColor> listStageLevel = new ArrayList<StageLevelColor>();
		for(int i=0;i<list.size();i++){
			listStageLevel.add(MapToStageLevelColor(list.get(i)));
		}
		return listStageLevel;
	}
	private StageLevelColor MapToStageLevelColor(Map<String, Object> map){
		if(CollectionUtils.isEmpty(map)){
			return null;
		}
		StageLevelColor stageLevelColor = new StageLevelColor();
		stageLevelColor.setColor(new Float((String)map.get("COLOR")));
		stageLevelColor.setId(new Long((String)(map.get("ID"))));
		stageLevelColor.setStageLevelId(Integer.parseInt((String)map.get("STAGELEVELID")));
		stageLevelColor.setType(Integer.parseInt((String)map.get("TYPE")));
		stageLevelColor.setValue(Integer.parseInt((String)map.get("VALUE")));
		return stageLevelColor;
	}

	@Override
	public StageLevel getSageLevelByStageLevelId(int stagelevelid) {
		// TODO Auto-generated method stub
		return MapToStageLevel(stageLevelDao.getStageLevelByStageLevelId(stagelevelid));
	}

	@Override
	public int installSoul(int stageLevelId, String soulList) {
		// TODO Auto-generated method stub
		return stageLevelDao.installSoul(stageLevelId, soulList);
	}

	@Override
	public Long addStageLevelColor(int stageLevelId, int type,
			int value, Float color) {
		// TODO Auto-generated method stub
		return stageLevelDao.addStageLevelColor(stageLevelId, type, value, color);
	}

	@Override
	public int deleteStageLevelColor(Long colorId) {
		// TODO Auto-generated method stub
		return stageLevelDao.deleteStageLevelColor(colorId);
	}

	@Override
	public StageLevelConditions getStageConditionsByStageConditionsId(int id) {
		// TODO Auto-generated method stub
		return MapToStageLevelConditions(stageLevelDao.getStageConditionsByStageConditionsId(id));
	}
	private StageLevelConditions MapToStageLevelConditions(Map<String,Object> map){
		if(CollectionUtils.isEmpty(map)){
			return null;
		}
		StageLevelConditions stageLevelConditions = new StageLevelConditions();
		stageLevelConditions.setId(Integer.parseInt(map.get("ID").toString()));
		stageLevelConditions.setType(Integer.parseInt(map.get("TYPE").toString()));
		stageLevelConditions.setValue1(Integer.parseInt(map.get("VALUE1").toString()));
		stageLevelConditions.setValue2(Integer.parseInt(map.get("VALUE2").toString()));
		stageLevelConditions.setDesc(map.get("DESC").toString());
		return stageLevelConditions;
	}
}

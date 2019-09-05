package com.cgiser.moka.manager.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import com.cgiser.moka.dao.StageDao;
import com.cgiser.moka.manager.StageLevelManager;
import com.cgiser.moka.manager.StageManager;
import com.cgiser.moka.model.Stage;
import com.cgiser.moka.model.StageLevel;
import com.cgiser.moka.model.Story;

public class StageManagerImpl implements StageManager {
	private StageDao stageDao;
	private StageLevelManager stageLevelManager;
	@Override
	public List<Stage> getStages() {
		// TODO Auto-generated method stub
		return MapListToStageList(stageDao.getStages());
	}

	@Override
	public List<Stage> getStagesByMapId(int mapId) {
		// TODO Auto-generated method stub
		return MapListToStageList(stageDao.getStagesByMapId(mapId));
	}
	@Override
	public Stage getStageById(int stageId) {
		return MapToStage(stageDao.getStageByStageId(stageId));
	}
	private Stage MapToStage(Map<String,Object> map){
		if(CollectionUtils.isEmpty(map)){
			return null;
		}
		Stage stage = new Stage();
		stage.setFightImg(Integer.parseInt(map.get("FIGHTIMG").toString()));
		stage.setFightName(map.get("FIGHTNAME").toString());
		stage.setMapId(Integer.parseInt(map.get("MAPID").toString()));
		stage.setStageId(Integer.parseInt(map.get("STAGEID").toString()));
		stage.setName(map.get("NAME").toString());
		stage.setNext(Integer.parseInt(map.get("NEXT").toString()));
		stage.setNextBranch(Integer.parseInt(map.get("NEXTBRANCH").toString()));
		stage.setPrev(Integer.parseInt(map.get("PREV").toString()));
		stage.setRank(Integer.parseInt(map.get("RANK").toString()));
		List<StageLevel> levels = stageLevelManager.getSageLevelsByStageId(stage.getRank());
		stage.setLevels(levels);
		stage.setType(Integer.parseInt(map.get("TYPE").toString()));
		stage.setX(new Double(map.get("X").toString()));
		stage.setY(new Double(map.get("Y").toString()));
		return stage;
	}
	private List<Stage> MapListToStageList(List<Map<String,Object>> list){
		if(CollectionUtils.isEmpty(list)){
			return null;
		}
		List<Stage> stages = new ArrayList<Stage>();
		
		for(int i=0;i<list.size();i++){
			stages.add(MapToStage(list.get(i)));
		}
		return stages;
	}
	private List<Story> MapListToStoryList(List<Map<String,Object>> list){
		if(CollectionUtils.isEmpty(list)){
			return null;
		}
		List<Story> stories = new ArrayList<Story>();
		
		for(int i=0;i<list.size();i++){
			stories.add(MapToStory(list.get(i)));
		}
		return stories;
	}
	private Story MapToStory(Map<String, Object> map) {
		if(CollectionUtils.isEmpty(map)){
			return null;
		}
		Story story = new Story();
		story.setStoryId(Integer.parseInt(map.get("STORYID").toString()));
		story.setCardId(Integer.parseInt(map.get("CARDID").toString()));
		story.setLeft(Integer.parseInt(map.get("LEFT").toString()));
		story.setStageId(Integer.parseInt(map.get("STAGEID").toString()));
		story.setState(Integer.parseInt(map.get("STATE").toString()));
		story.setStoryInfo(map.get("STORYINFO").toString());
		return story;
	}

	public StageDao getStageDao() {
		return stageDao;
	}

	public void setStageDao(StageDao stageDao) {
		this.stageDao = stageDao;
	}

	public StageLevelManager getStageLevelManager() {
		return stageLevelManager;
	}

	public void setStageLevelManager(StageLevelManager stageLevelManager) {
		this.stageLevelManager = stageLevelManager;
	}

	@Override
	public List<Story> getStorys() {
		// TODO Auto-generated method stub
		return MapListToStoryList(stageDao.getStorys());
	}



}

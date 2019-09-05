package com.cgiser.moka.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

public class StageDao {
	private static final Logger logger = LoggerFactory.getLogger(MapDao.class);
	private JdbcTemplate mokaJdbcTemplate;
	public List<Map<String,Object>> getStages(){
		List list = new ArrayList();
        String sql = "select * From t_cgiser_stage";
        list = mokaJdbcTemplate.queryForList(sql);
        return list;
	}	
	public List<Map<String,Object>> getStagesByMapId(int mapId){
		List list = new ArrayList();
        String sql = "select * From t_cgiser_stage where mapid = ?";
        String[] para = new String[1];
        para[0] = String.valueOf(mapId);
        list = mokaJdbcTemplate.queryForList(sql,para);
        return list;
	}
	public List<Map<String,Object>> getStorys(){
		List list = new ArrayList();
        String sql = "select * From t_cgiser_story where state = ?";
        String[] para = new String[1];
        para[0] = String.valueOf(1);
        list = mokaJdbcTemplate.queryForList(sql,para);
        return list;
	}
	public Map<String,Object> getStageByStageId(int stageId){
        String sql = "select * From t_cgiser_stage where rank = ?";
        String[] para = new String[1];
        para[0] = String.valueOf(stageId);
        return mokaJdbcTemplate.queryForMap(sql,para);
	}
	public JdbcTemplate getMokaJdbcTemplate() {
		return mokaJdbcTemplate;
	}
	public void setMokaJdbcTemplate(JdbcTemplate mokaJdbcTemplate) {
		this.mokaJdbcTemplate = mokaJdbcTemplate;
	}
}

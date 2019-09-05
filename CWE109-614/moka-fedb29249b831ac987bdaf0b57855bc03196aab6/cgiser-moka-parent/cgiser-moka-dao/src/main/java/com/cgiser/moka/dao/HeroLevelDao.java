package com.cgiser.moka.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

public class HeroLevelDao {
	private static final Logger logger = LoggerFactory.getLogger(MapDao.class);
	private JdbcTemplate mokaJdbcTemplate;
	public List<Map<String,Object>> getHeroLevels(){
		List list = new ArrayList();
        String sql = "select * From t_cgiser_hero";
        list = mokaJdbcTemplate.queryForList(sql);
        return list;
	}	
	public Map<String,Object> getHeroLevelByLevel(int level){
        String sql = "select * From t_cgiser_hero where level = ?";
        String[] para = new String[1];
        para[0] = String.valueOf(level);
        return mokaJdbcTemplate.queryForMap(sql,para);
	}
	public Map<String,Object> getStageByStageId(int stageId){
        String sql = "select * From t_cgiser_hero where mapstageid = ?";
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

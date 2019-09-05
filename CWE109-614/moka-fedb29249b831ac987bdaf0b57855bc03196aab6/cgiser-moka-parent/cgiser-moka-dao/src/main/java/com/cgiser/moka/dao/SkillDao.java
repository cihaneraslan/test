package com.cgiser.moka.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

public class SkillDao {
	private static final Logger logger = LoggerFactory.getLogger(SkillDao.class);
	private JdbcTemplate mokaJdbcTemplate;
	public List<Map<String,Object>> getSkills(){
		List list = new ArrayList();
        String sql = "select * From t_cgiser_skill";
        list = mokaJdbcTemplate.queryForList(sql);
        return list;
		
	}
	public Map<String,Object> getSkillById(int skillId){
		String[] para = new String[1];
		para[0] = String.valueOf(skillId);
        String sql = "select * From t_cgiser_skill where skillid=?";
        return mokaJdbcTemplate.queryForMap(sql,para);
		
	}
	public Map<String,Object> getSkillByName(String skillName){
		String[] para = new String[1];
		para[0] = String.valueOf(skillName);
        String sql = "select * From t_cgiser_skill where name=?";
        return mokaJdbcTemplate.queryForMap(sql,para);
		
	}
	public JdbcTemplate getMokaJdbcTemplate() {
		return mokaJdbcTemplate;
	}
	public void setMokaJdbcTemplate(JdbcTemplate mokaJdbcTemplate) {
		this.mokaJdbcTemplate = mokaJdbcTemplate;
	}
}

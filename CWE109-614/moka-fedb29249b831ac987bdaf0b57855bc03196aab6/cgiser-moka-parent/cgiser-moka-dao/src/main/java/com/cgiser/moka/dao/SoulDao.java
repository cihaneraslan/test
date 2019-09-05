package com.cgiser.moka.dao;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

public class SoulDao {
	private static final Logger logger = LoggerFactory.getLogger(SoulDao.class);
	private JdbcTemplate mokaJdbcTemplate;
	public List<Map<String,Object>> getSouls(){
        String sql = "select * From t_cgiser_soul";
        List list = mokaJdbcTemplate.queryForList(sql);
        return list;
		
	}
	public Map<String,Object> getSoulById(int soulId){
		String[] para = new String[1];
		para[0] = String.valueOf(soulId);
        String sql = "select * From t_cgiser_soul where soulid=?";
        return mokaJdbcTemplate.queryForMap(sql,para);
		
	}
	public Map<String,Object> getRandomSoul(int id1,int id2){
		final String sql = "SELECT * FROM `t_cgiser_soul` AS t1 JOIN (SELECT ROUND(RAND() * " +
		"((SELECT MAX(soulid) FROM `t_cgiser_soul`)-(SELECT MIN(soulid) " +
		"FROM `t_cgiser_soul`))+(SELECT MIN(soulid) FROM `t_cgiser_soul`)) AS soulid) AS t2 " +
		"WHERE t1.soulid >= t2.soulid AND t1.soulid <> ? AND t1.soulid <> ? AND thinkget=1 ORDER BY t1.soulid LIMIT 1";
		String[] para = new String[2];
		para[0] = String.valueOf(id1);
		para[1] = String.valueOf(id2);
        return mokaJdbcTemplate.queryForMap(sql,para);
		
	}
	public List<Map<String,Object>> getAllSoulId(){
		final String sql = "SELECT SOULID FROM `t_cgiser_soul` where thinkget=1";
        return mokaJdbcTemplate.queryForList(sql);
		
	}
	public JdbcTemplate getMokaJdbcTemplate() {
		return mokaJdbcTemplate;
	}
	public void setMokaJdbcTemplate(JdbcTemplate mokaJdbcTemplate) {
		this.mokaJdbcTemplate = mokaJdbcTemplate;
	}
}

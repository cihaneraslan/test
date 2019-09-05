package com.cgiser.moka.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

public class RuneDao {
	private static final Logger logger = LoggerFactory.getLogger(CardDao.class);
	private JdbcTemplate mokaJdbcTemplate;
	public List<Map<String,Object>> getRunes(){
		List list = new ArrayList();
        String sql = "select * From t_cgiser_rune";
        list = mokaJdbcTemplate.queryForList(sql);
        return list;
		
	}
	public Map<String,Object> getRuneById(int runeId){
        String sql = "select * From t_cgiser_rune where RUNEID=?";
        String[] para = new String[1];
        para[0] = String.valueOf(runeId);
        return  mokaJdbcTemplate.queryForMap(sql,para);
		
	}
	public JdbcTemplate getMokaJdbcTemplate() {
		return mokaJdbcTemplate;
	}
	public void setMokaJdbcTemplate(JdbcTemplate mokaJdbcTemplate) {
		this.mokaJdbcTemplate = mokaJdbcTemplate;
	}
}

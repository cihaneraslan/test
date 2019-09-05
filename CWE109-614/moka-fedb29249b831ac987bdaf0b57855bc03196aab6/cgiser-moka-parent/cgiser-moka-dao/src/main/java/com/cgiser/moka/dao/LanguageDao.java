package com.cgiser.moka.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

public class LanguageDao {
	private static final Logger logger = LoggerFactory.getLogger(LanguageDao.class);
	private JdbcTemplate mokaJdbcTemplate;
	public List<Map<String,Object>> getLanByLanType(String type){
		List list = new ArrayList();
        String sql = "select t_key,t_value From t_cgiser_lan where state = 1 and lan = ?";
        String[] para = new String[1];
        para[0] = type;
        list = mokaJdbcTemplate.queryForList(sql,para);
        return list;
		
	}
	public JdbcTemplate getMokaJdbcTemplate() {
		return mokaJdbcTemplate;
	}
	public void setMokaJdbcTemplate(JdbcTemplate mokaJdbcTemplate) {
		this.mokaJdbcTemplate = mokaJdbcTemplate;
	}
}

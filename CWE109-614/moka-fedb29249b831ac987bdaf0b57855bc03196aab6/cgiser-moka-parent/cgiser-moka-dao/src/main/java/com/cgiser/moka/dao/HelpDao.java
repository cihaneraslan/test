package com.cgiser.moka.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

public class HelpDao {
	private static final Logger logger = LoggerFactory.getLogger(HelpDao.class);
	private JdbcTemplate mokaJdbcTemplate;
	public List<Map<String,Object>> getHelps(){
		List list = new ArrayList();
        String sql = "select * From t_cgiser_help";
        list = mokaJdbcTemplate.queryForList(sql);
        return list;
		
	}
	public JdbcTemplate getMokaJdbcTemplate() {
		return mokaJdbcTemplate;
	}
	public void setMokaJdbcTemplate(JdbcTemplate mokaJdbcTemplate) {
		this.mokaJdbcTemplate = mokaJdbcTemplate;
	}
}

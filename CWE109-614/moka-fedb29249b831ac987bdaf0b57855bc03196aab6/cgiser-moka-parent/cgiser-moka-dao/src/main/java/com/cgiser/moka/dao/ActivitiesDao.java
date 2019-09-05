package com.cgiser.moka.dao;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

public class ActivitiesDao {
	private static final Logger logger = LoggerFactory.getLogger(ActivitiesDao.class);
	private JdbcTemplate mokaJdbcTemplate;
	public List<Map<String,Object>>getAllActivities(){
		Calendar curDate = Calendar.getInstance();
		Timestamp time = new Timestamp(curDate.getTime().getTime());
        String sql = "select * From t_cgiser_activities where startdate<'"+time+"' and enddate>'"+time+"' and state=1";
        return  mokaJdbcTemplate.queryForList(sql);
		
	}
	public JdbcTemplate getMokaJdbcTemplate() {
		return mokaJdbcTemplate;
	}
	public void setMokaJdbcTemplate(JdbcTemplate mokaJdbcTemplate) {
		this.mokaJdbcTemplate = mokaJdbcTemplate;
	}
}

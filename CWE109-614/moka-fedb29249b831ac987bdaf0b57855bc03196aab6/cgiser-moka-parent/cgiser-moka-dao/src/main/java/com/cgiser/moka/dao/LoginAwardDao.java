package com.cgiser.moka.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class LoginAwardDao {
	private static final Logger logger = LoggerFactory.getLogger(LoginAwardDao.class);
	private JdbcTemplate mokaJdbcTemplate;

	public JdbcTemplate getMokaJdbcTemplate() {
		return mokaJdbcTemplate;
	}

	public void setMokaJdbcTemplate(JdbcTemplate mokaJdbcTemplate) {
		this.mokaJdbcTemplate = mokaJdbcTemplate;
	}

	public Map<String, Object> getLoginAwardType(int month) {
		String time = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
		if(month<10){
			time = time+"0";
		}
		time = time+month;
		String sql = "select * From t_cgiser_loginaward where time="+time;
		return mokaJdbcTemplate.queryForMap(sql);
	}
	public Long insertLoginAwardType(String sql,int month) {
		Long id = new Long(0);
		String time = String.valueOf(Calendar.getInstance().get(Calendar.YEAR))+month;
		final String insertSql = "insert into t_cgiser_loginaward (award,time)values('"
				+ sql
				+ "',"
				+ time
				+ ")";

		long awardid;

		KeyHolder keyHolder = new GeneratedKeyHolder();
		mokaJdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				PreparedStatement ps = con.prepareStatement(insertSql
						.toString());
				return ps;
			}

		}, keyHolder);
		try {
			awardid = keyHolder.getKey().longValue();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			awardid = 0;
		}
		if (awardid > 0) {
			id = new Long(awardid);
		} else {
			return 0l;
		}
		return id;
	}
	public static void main(String[] args) {
		System.out.println(String.valueOf(Calendar.getInstance().get(Calendar.YEAR))+
				Calendar.getInstance().get(Calendar.MONTH));
	}
}

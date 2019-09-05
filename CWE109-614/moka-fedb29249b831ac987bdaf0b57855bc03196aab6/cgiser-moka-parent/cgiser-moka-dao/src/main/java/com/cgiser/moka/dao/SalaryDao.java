package com.cgiser.moka.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class SalaryDao {
	private static final Logger logger = LoggerFactory.getLogger(MapDao.class);
	private JdbcTemplate mokaJdbcTemplate;
	public Long extendSalary(int awardType, int awardValue, Date time,
			int type, String desc ,Long roleId) {
		Long id = new Long(0);
		// String sql =
		// "select USERID,USERIDEN,USERNAME,CREATETIME,UDID,ISACTIVE,STATE from t_user_user where STATE = 1 and username=?";
		final String insertSql = "insert into t_cgiser_salary (awardtype,awardvalue,time,type,salarydesc,roleid)values("
				+ awardType
				+ ","
				+ awardValue
				+ ",'"
				+ new Timestamp(time.getTime())
				+ "',"
				+ type
				+ ",'"
				+ desc
				+ "',"
				+ roleId
				+ ")";

		long salaryid;

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
			salaryid = keyHolder.getKey().longValue();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			salaryid = 0;
		}
		if (salaryid > 0) {
			id = new Long(salaryid);
		} else {
			return 0l;
		}
		return id;
	}


	public List<Map<String,Object>> getSalaryByRoleId(Long roleId) {
		String sql = "SELECT * FROM `t_cgiser_salary` where roleId=? and TIMESTAMPDIFF(DAY,NOW(),TIME)>-7 and state=1 limit 0,50";
		String[] para = new String[1];
		para[0] = String.valueOf(roleId);
		return mokaJdbcTemplate.queryForList(sql, para);
	}
	public Map<String,Object> getLastRankSalaryTime() {
		String sql = "SELECT Max(date) as DATE FROM `t_cgiser_ranktime`";
		return mokaJdbcTemplate.queryForMap(sql);
	}
	public Long insertRankSalaryTime() {
		Long id = new Long(0);
		// String sql =
		// "select USERID,USERIDEN,USERNAME,CREATETIME,UDID,ISACTIVE,STATE from t_user_user where STATE = 1 and username=?";
		final String insertSql = "insert into t_cgiser_ranktime (date)values('"
				+ new Timestamp(new Date().getTime())
				+ "')";

		long salaryid;

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
			salaryid = keyHolder.getKey().longValue();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			salaryid = 0;
		}
		if (salaryid > 0) {
			id = new Long(salaryid);
		} else {
			return 0l;
		}
		return id;
	}


	public int receiveSalary(Long roleId) {
		String sql = "update t_cgiser_salary set state=2 where roleId=?  and TIMESTAMPDIFF(DAY,NOW(),TIME)>-7 and state=1";
		String[] para = new String[1];
		para[0] = String.valueOf(roleId);
		return mokaJdbcTemplate.update(sql, para);
	}
	public JdbcTemplate getMokaJdbcTemplate() {
		return mokaJdbcTemplate;
	}
	public void setMokaJdbcTemplate(JdbcTemplate mokaJdbcTemplate) {
		this.mokaJdbcTemplate = mokaJdbcTemplate;
	}
}

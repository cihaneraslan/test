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

public class RobLogDao {
	private static final Logger logger = LoggerFactory.getLogger(RobLogDao.class);
	private JdbcTemplate mokaJdbcTemplate;

	public List<Map<String, Object>> getLastRobLog(Long roleId,Date date) {
		String sql = "select * From t_cgiser_rob_log where state=1 and robroleid = "+roleId+" and robtime>'"+new Timestamp(date.getTime())+"'";
		return mokaJdbcTemplate.queryForList(sql);
	}
	public Long infoRobLog(Long roleId, Long robRoleId,int robRoleCoins) {
		Long id = new Long(0);
		// String sql =
		// "select USERID,USERIDEN,USERNAME,CREATETIME,UDID,ISACTIVE,STATE from t_user_user where STATE = 1 and username=?";
		final String insertSql = "insert into t_cgiser_rob_log (roleid,robroleid,robrolecoins,robtime,state)values("
				+ roleId
				+ ","
				+ robRoleId
				+ ","
				+ robRoleCoins
				+ ",'"
				+ new java.sql.Timestamp(System.currentTimeMillis())
				+ "',"
				+ 1
				+ ")";

		long robid;

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
			robid = keyHolder.getKey().longValue();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			robid = 0;
		}
		if (robid > 0) {
			id = new Long(robid);
		} else {
			return 0l;
		}
		return id;
	}
	public int updatePaymentByStoreId(Long storeId) {
		String sql = "update t_cgiser_product_log set payment=1  where storeid=? and status=0";
		String[] para = new String[1];
		para[0] = String.valueOf(storeId);
		return mokaJdbcTemplate.update(sql,para);
	}
	public JdbcTemplate getMokaJdbcTemplate() {
		return mokaJdbcTemplate;
	}

	public void setMokaJdbcTemplate(JdbcTemplate mokaJdbcTemplate) {
		this.mokaJdbcTemplate = mokaJdbcTemplate;
	}
}

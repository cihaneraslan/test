package com.cgiser.moka.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class GiftDao {
	private static final Logger logger = LoggerFactory
			.getLogger(FriendsDao.class);
	private JdbcTemplate mokaJdbcTemplate;

	public Map<String, Object> getGiftById(int giftId) {
		String sql = "select * from t_cgiser_gift where STATE = 1 and id=?";
		String[] para = new String[1];
		para[0] = String.valueOf(giftId);
		return mokaJdbcTemplate.queryForMap(sql, para);

	}

	public Map<String, Object> getGiftRoleByRoleId(Long roleId, int giftId,
			String code) {
		String sql = "select * from t_cgiser_gift_role where STATE = 1 and roleId = ? and giftId = ? and code = ?";
		String[] para = new String[3];
		para[0] = String.valueOf(roleId);
		para[1] = String.valueOf(giftId);
		para[2] = code;
		return mokaJdbcTemplate.queryForMap(sql, para);

	}

	public Map<String, Object> getGiftCode(int giftId, String code) {
		String sql = "select * from t_cgiser_gift_code where STATE = 1 and giftId = ? and code = ?";
		String[] para = new String[2];
		para[0] = String.valueOf(giftId);
		para[1] = code;
		return mokaJdbcTemplate.queryForMap(sql, para);

	}

	public int updateGiftCode(int giftId, String code) {
		Long id = new Long(0);
		// String sql =
		// "select USERID,USERIDEN,USERNAME,CREATETIME,UDID,ISACTIVE,STATE from t_user_user where STATE = 1 and username=?";
		String updateSql = "update t_cgiser_gift_code set state= 0 where giftId="
				+ giftId + " and code='" + code + "'";
		return mokaJdbcTemplate.update(updateSql);
	}

	public Long saveGiftRole(Long roleId, int giftId, String code) {
		Long id = new Long(0);
		final String insertSql = "insert into t_cgiser_gift_role (giftid,roleid,code) values ("
				+ giftId + "," + roleId + ",'" + code + "')";
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
			id = keyHolder.getKey().longValue();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			id = 0L;
		}
		return id;
	}

	public JdbcTemplate getMokaJdbcTemplate() {
		return mokaJdbcTemplate;
	}

	public void setMokaJdbcTemplate(JdbcTemplate mokaJdbcTemplate) {
		this.mokaJdbcTemplate = mokaJdbcTemplate;
	}
}

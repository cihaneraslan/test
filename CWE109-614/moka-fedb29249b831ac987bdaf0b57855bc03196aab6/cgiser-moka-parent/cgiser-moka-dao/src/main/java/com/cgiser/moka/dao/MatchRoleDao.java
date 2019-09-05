package com.cgiser.moka.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class MatchRoleDao {
	private static final Logger logger = LoggerFactory.getLogger(MatchRoleDao.class);
	private JdbcTemplate mokaJdbcTemplate;
	public List<Map<String,Object>> getMaps(){
        String sql = "select * From t_cgiser_map";
        return mokaJdbcTemplate.queryForList(sql);
	}
	public Long saveMatchRole(Long roleId) {
		Long id = new Long(0);
		// String sql =
		// "select USERID,USERIDEN,USERNAME,CREATETIME,UDID,ISACTIVE,STATE from t_user_user where STATE = 1 and username=?";
		final String insertSql = "insert into t_cgiser_match (roleid,matchlasttime,lastaddtime,matchlasttimes,state)values("
				+ roleId
				+ ",'"
				+ new Timestamp(System.currentTimeMillis())
				+ "','"
				+ new Timestamp(System.currentTimeMillis())
				+ "',"
				+ 5
				+ ","
				+ 1
				+ ")";

		long emailid;

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
			emailid = keyHolder.getKey().longValue();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			emailid = 0;
		}
		if (emailid > 0) {
			id = new Long(emailid);
		} else {
			return 0l;
		}
		return id;
	}
	public Map<String,Object> getMatchRoleById(Long roleId){
        String sql = "select * From t_cgiser_match where roleid = ?";
        String[] para = new String[1];
        para[0] = String.valueOf(roleId);
        return mokaJdbcTemplate.queryForMap(sql,para);
	}
	public int addAllMatchRoleCD(){
		String sql = "update t_cgiser_match set matchlasttimes = matchlasttimes + 1, lastaddtime = '"+new Timestamp(System.currentTimeMillis())+"'  where matchlasttimes < 5";
        return mokaJdbcTemplate.update(sql);
	}
	public int addMatchRoleCD(Long roleId){
		String sql = "update t_cgiser_match set matchlasttimes = matchlasttimes + 1 where roleid = ?";
        String[] para = new String[1];
        para[0] = String.valueOf(roleId);
		return mokaJdbcTemplate.update(sql,para);
	}
	public int addMatchRoleCDAndLastAddTime(Long roleId){
		String sql = "update t_cgiser_match set matchlasttimes = matchlasttimes + 1, lastaddtime = '"+new Timestamp(System.currentTimeMillis())+"'  where roleid = ?";
        String[] para = new String[1];
        para[0] = String.valueOf(roleId);
		return mokaJdbcTemplate.update(sql,para);
	}
	public int updateMatchRoleCD(Long roleId){
		String sql = "update t_cgiser_match set matchlasttimes = matchlasttimes - 1  where roleid = ?";
        String[] para = new String[1];
        para[0] = String.valueOf(roleId);
		return mokaJdbcTemplate.update(sql,para);
	}
	public int updateRoleMatchTimeAndTimes(Long roleId){
		String sql = "update t_cgiser_match set matchlasttimes = matchlasttimes - 1 ,matchlasttime = '"+new Timestamp(System.currentTimeMillis())+"'  where roleid = ?";
        String[] para = new String[1];
        para[0] = String.valueOf(roleId);
		return mokaJdbcTemplate.update(sql,para);
	}
	public JdbcTemplate getMokaJdbcTemplate() {
		return mokaJdbcTemplate;
	}
	public void setMokaJdbcTemplate(JdbcTemplate mokaJdbcTemplate) {
		this.mokaJdbcTemplate = mokaJdbcTemplate;
	}
}

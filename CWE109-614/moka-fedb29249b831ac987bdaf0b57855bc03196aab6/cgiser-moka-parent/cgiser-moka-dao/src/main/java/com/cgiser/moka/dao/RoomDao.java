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

public class RoomDao {
	private static final Logger logger = LoggerFactory.getLogger(MapDao.class);
	private JdbcTemplate mokaJdbcTemplate;
	public Long saveRoom(int mapId,Long roleId,int roleNum) {
		Long id = new Long(0);
		// String sql =
		// "select USERID,USERIDEN,USERNAME,CREATETIME,UDID,ISACTIVE,STATE from t_user_user where STATE = 1 and username=?";
		final String insertSql = "insert into t_cgiser_room (roomowner,mapid,rolenum,role1,createtime)values("
				+ roleId
				+ ","
				+ mapId
				+ ","
				+ roleNum
				+ ","
				+ roleId
				+ ",'"
				+ new Timestamp(System.currentTimeMillis())
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


	public Map<String,Object> getRoomById(Long roomId) {
		String sql = "SELECT * FROM `t_cgiser_room` where roomid=? and TIMESTAMPDIFF(MINUTE,NOW(),CREATETIME)>=-30 and state = 1";
		String[] para = new String[1];
		para[0] = String.valueOf(roomId);
		return mokaJdbcTemplate.queryForMap(sql, para);
	}
	public Map<String,Object> getRoomByIdState(Long roomId,int state) {
		String sql = "SELECT * FROM `t_cgiser_room` where roomid=? and state = "+state;
		String[] para = new String[1];
		para[0] = String.valueOf(roomId);
		return mokaJdbcTemplate.queryForMap(sql, para);
	}
	public Map<String,Object> getRoomByOwner(Long roleId) {
		String sql = "SELECT * FROM `t_cgiser_room` where roomowner=? and TIMESTAMPDIFF(MINUTE,NOW(),CREATETIME)>=-30 and state = 1";
		String[] para = new String[1];
		para[0] = String.valueOf(roleId);
		return mokaJdbcTemplate.queryForMap(sql, para);
	}
	public List<Map<String,Object>> getRoomByMapId(int mapId) {
		String sql = "SELECT * FROM `t_cgiser_room` where mapid = ? and TIMESTAMPDIFF(MINUTE,NOW(),CREATETIME)>-30 and state = 1";
		String[] para = new String[1];
		para[0] = String.valueOf(mapId);
		return mokaJdbcTemplate.queryForList(sql, para);
	}
	public List<Map<String,Object>> getAllRoom() {
		String sql = "SELECT * FROM `t_cgiser_room` where TIMESTAMPDIFF(MINUTE,NOW(),CREATETIME)>-30 and state = 1";
		return mokaJdbcTemplate.queryForList(sql);
	}
	public int updateRoomRole(Long roleId,int id,Long roomId) {
		String sql = "update t_cgiser_room set role"+id+"=? where role"+id+"=0  and roomId = ? and TIMESTAMPDIFF(MINUTE,NOW(),CREATETIME)>-30 ";
		String[] para = new String[2];
		para[0] = String.valueOf(roleId);
		para[1] = String.valueOf(roomId);
		return mokaJdbcTemplate.update(sql, para);
	}
	public int updateRoomRoleToNull(int id,Long roomId) {
		String sql = "update t_cgiser_room set role"+id+"=? where roomId = ? and TIMESTAMPDIFF(MINUTE,NOW(),CREATETIME)>-30 ";
		String[] para = new String[2];
		para[0] = "0";
		para[1] = String.valueOf(roomId);
		return mokaJdbcTemplate.update(sql, para);
	}
	public int updateRoomFightResult(Long roomId,String salaryInfo,String turnInfo,int win) {
		String sql = "update t_cgiser_room set win = ?,salaryinfo = ?,turninfo=? where roomId = ? and TIMESTAMPDIFF(MINUTE,NOW(),CREATETIME)>-30 ";
		String[] para = new String[4];
		para[0] = String.valueOf(win);
		para[1] = String.valueOf(salaryInfo);
		para[2] = String.valueOf(turnInfo);
		para[3] = String.valueOf(roomId);
		return mokaJdbcTemplate.update(sql, para);
	}
	public int updateRoomRoleState(Long roomId) {
		String sql = "update t_cgiser_room set state = 0 where roomId = ? and TIMESTAMPDIFF(MINUTE,NOW(),CREATETIME)>-30 ";
		String[] para = new String[1];
		para[0] = String.valueOf(roomId);
		return mokaJdbcTemplate.update(sql, para);
	}
	public JdbcTemplate getMokaJdbcTemplate() {
		return mokaJdbcTemplate;
	}
	public void setMokaJdbcTemplate(JdbcTemplate mokaJdbcTemplate) {
		this.mokaJdbcTemplate = mokaJdbcTemplate;
	}
}

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

public class LegionFightDao {
	private static final Logger logger = LoggerFactory.getLogger(MapDao.class);
	private JdbcTemplate mokaJdbcTemplate;
//	public List<Map<String, Object>> getLegionFightCityInfos(){
//        String sql = "SELECT * FROM t_cgiser_legionbattle";
//        return mokaJdbcTemplate.queryForList(sql);
//       
//	}
//	public Map<String,Object> getLegionFightCityInfo(String city){
//		String sql = "SELECT * FROM t_cgiser_legionbattle where name=?";
//		String[] para = new String[1];
//		para[0] = city;
//		return mokaJdbcTemplate.queryForMap(sql,para);
//	}
//	public List<Map<String, Object>> getMaxBidLegion(String name) {
//		String sql = "SELECT * FROM `t_cgiser_legionbid` WHERE CITYNAME=? AND STATE=1 ORDER BY COINS LIMIT 0,2";
//		String[] para = new String[1];
//		para[0] = name;
//		return mokaJdbcTemplate.queryForList(sql,para);
//	}
//	public Map<String, Object> getBidLegion(String cityName,String legionName) {
//		String sql = "SELECT * FROM `t_cgiser_legionbid` WHERE CITYNAME=? AND LEGIONNAME=? AND STATE=1";
//		String[] para = new String[2];
//		para[0] = cityName;
//		para[0] = legionName;
//		return mokaJdbcTemplate.queryForMap(sql,para);
//	}
//	public List<Map<String, Object>> getBidLegions(String cityName) {
//		String sql = "SELECT * FROM `t_cgiser_legionbid` WHERE CITYNAME=? AND STATE=1 ORDER BY COINS";
//		String[] para = new String[1];
//		para[0] = cityName;
//		return mokaJdbcTemplate.queryForList(sql,para);
//	}
	public int updateLegionState(long id) {
		
        String sql = "update t_cgiser_legion_fight set end=1 where id = ?";
        Long[] para = new Long[1];
        para[0] = id;
        return mokaJdbcTemplate.update(sql,para);
	}
	public int updateLegionFightWinResource(long id,int win,Long resource) {
		
        String sql = "update t_cgiser_legion_fight set win=?,resource=? where id = ?";
        String[] para = new String[3];
        para[0] = String.valueOf(win);
        para[1] = String.valueOf(resource);
        para[2] = String.valueOf(id);
        return mokaJdbcTemplate.update(sql,para);
	}
	public int updateLegionFightGuardState(Long legionFightId,int id,Long roleId1,Long roleId2) {
        String sql = "update t_cgiser_legion_fight set guard"+id+" = '"+roleId1+"_1_"+roleId2+"' where id = ?";
        Long[] para = new Long[1];
        para[0] = legionFightId;
        return mokaJdbcTemplate.update(sql,para);
	}
	public Map<String, Object> getLegionFightByDefend(Long defend) {
		String sql = "SELECT * FROM `t_cgiser_legion_fight` WHERE end=0 AND defend=?";
		String[] para = new String[1];
		para[0] = String.valueOf(defend);
		return mokaJdbcTemplate.queryForMap(sql,para);
	}
	public Map<String, Object> getLegionFightById(Long id) {
		String sql = "SELECT * FROM `t_cgiser_legion_fight` WHERE id=?";
		String[] para = new String[1];
		para[0] = String.valueOf(id);
		return mokaJdbcTemplate.queryForMap(sql,para);
	}
	public Map<String, Object> getLegionFightByAttack(Long attack) {
		String sql = "SELECT * FROM `t_cgiser_legion_fight` WHERE end=0 AND attack=?";
		String[] para = new String[1];
		para[0] = String.valueOf(attack);
		return mokaJdbcTemplate.queryForMap(sql,para);
	}
	public List<Map<String, Object>> getLegionFights() {
		String sql = "SELECT * FROM `t_cgiser_legion_fight` WHERE end=0";
		return mokaJdbcTemplate.queryForList(sql);
	}
	public Long saveLegionFight(Long roleId, Long attack, Long defend, Date date,String guard1,String guard2,String guard3,String guard4
			,String guard5,String guard6,String guard7,String guard8,String guard9,int level,int state) {
		Long id = new Long(0);
		// String sql =
		// "select USERID,USERIDEN,USERNAME,CREATETIME,UDID,ISACTIVE,STATE from t_user_user where STATE = 1 and username=?";
		final String insertSql = "insert into t_cgiser_legion_fight (startid,attack,defend,time,guard1,guard2,guard3,guard4,guard5," +
				"guard6,guard7,guard8,guard9,level,state)values("
				+ roleId
				+ ","
				+ attack
				+ ","
				+ defend
				+ ",'"
				+ new Timestamp(System.currentTimeMillis())
				+ "','"
				+ guard1
				+ "','"
				+ guard2
				+ "','"
				+ guard3
				+ "','"
				+ guard4
				+ "','"
				+ guard5
				+ "','"
				+ guard6
				+ "','"
				+ guard7
				+ "','"
				+ guard8
				+ "','"
				+ guard9
				+ "',"
				+ level
				+ ","
				+ 1
				+ ")";

		long bidid;

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
			bidid = keyHolder.getKey().longValue();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			bidid = 0;
		}
		if (bidid > 0) {
			id = new Long(bidid);
		} else {
			return 0l;
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

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

public class AchievementDao {
	private static final Logger logger = LoggerFactory.getLogger(AchievementDao.class);
	private JdbcTemplate mokaJdbcTemplate;
	public List<Map<String,Object>>getAchievements(){
        String sql = "select * From t_cgiser_achievement";
        return  mokaJdbcTemplate.queryForList(sql);
		
	}
	public Map<String,Object> getAchievementById(int id,int status){
        String sql = "select * From t_cgiser_achievement where achievementid = ? and status = ?";
        String[] para = new String[2];
        para[0] = String.valueOf(id);
        para[1] = String.valueOf(status);
        return mokaJdbcTemplate.queryForMap(sql, para);
		
	}
	public Map<String,Object> getUserAchievementById(Long roleId,int achievementId){
        String sql = "select * From t_cgiser_userachievement where roleid = ? and achievementId = ?";
        String[] para = new String[2];
        para[0] = String.valueOf(roleId);
        para[1] = String.valueOf(achievementId);
        return mokaJdbcTemplate.queryForMap(sql, para);
		
	}
	public List<Map<String,Object>> getUserAchievement(Long roleId){
        String sql = "select * From t_cgiser_userachievement where roleid = ?";
        String[] para = new String[1];
        para[0] = String.valueOf(roleId);
        return mokaJdbcTemplate.queryForList(sql, para);
		
	}
	public int updateUserAchievement(int achievementId,Long roleId,int finishState){
        String sql = "update t_cgiser_userachievement set finishstate=? where roleid = ? and achievementId = ?";
        String[] para = new String[3];
        para[0] = String.valueOf(finishState);
        para[1] = String.valueOf(roleId);
        para[2] = String.valueOf(achievementId);
        return mokaJdbcTemplate.update(sql, para);
		
	}
	public Long saveUserAchievement(int achievementId,Long roleId,int finishState){
		Long id = new Long(0);
        //String sql = "select USERID,USERIDEN,USERNAME,CREATETIME,UDID,ISACTIVE,STATE from t_user_user where STATE = 1 and username=?";
        final String insertSql = "insert into t_cgiser_userachievement (achievementId,roleid,createTime,finishstate)values("+achievementId+","+roleId+",'"+new Timestamp(System.currentTimeMillis())+"',"+finishState+")";
        
        long usercardid;

        KeyHolder keyHolder = new GeneratedKeyHolder();
        mokaJdbcTemplate.update(new PreparedStatementCreator() {

            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(insertSql.toString());
                return ps;
            }

        }, keyHolder);
        try {
        	usercardid = keyHolder.getKey().longValue();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            usercardid = 0;
        }
        if (usercardid > 0) {
            id = new Long(usercardid);
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

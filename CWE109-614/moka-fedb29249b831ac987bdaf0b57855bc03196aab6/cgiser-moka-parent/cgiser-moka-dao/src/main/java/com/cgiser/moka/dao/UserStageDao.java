package com.cgiser.moka.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.cgiser.moka.data.UserStageDo;

public class UserStageDao {
	private static final Logger logger = LoggerFactory.getLogger(MapDao.class);
	private JdbcTemplate mokaJdbcTemplate;
	public List<Map<String,Object>> getUserStagesByRoleId(Long roleId){
		List list = new ArrayList();
        String sql = "select * From t_cgiser_userstage where roleid=?";
        String[] para = new String[1];
        para[0] = String.valueOf(roleId);
        list = mokaJdbcTemplate.queryForList(sql,para);
        return list;
	}	
	public Map<String,Object> getUserStagesByRoleIdStageId(Long roleId,int stageId){
        String sql = "select * From t_cgiser_userstage where roleid=? and stageid=?";
        String[] para = new String[2];
        para[0] = String.valueOf(roleId);
        para[1] = String.valueOf(stageId);
        return mokaJdbcTemplate.queryForMap(sql,para);
	}	
	public List<Map<String,Object>> getUserStagesByRoleIdMapId(Long roleId,int mapId){
        String sql = "select * From t_cgiser_userstage where roleid=? and mapid=?";
        String[] para = new String[2];
        para[0] = String.valueOf(roleId);
        para[1] = String.valueOf(mapId);
        return mokaJdbcTemplate.queryForList(sql,para);
	}
	public Long saveUserStage(UserStageDo userStageDo){
		Long id = new Long(0);
        //String sql = "select USERID,USERIDEN,USERNAME,CREATETIME,UDID,ISACTIVE,STATE from t_user_user where STATE = 1 and username=?";
        final String insertSql = "insert into t_cgiser_userstage (stageid,mapid,lastfinishedtime,finishedstage,counterattacktime,type,roleid)values("+userStageDo.getStageId()+","+userStageDo.getMapId()+",'"+new Timestamp(userStageDo.getLastFinishedTime().getTime())+
        "',"+userStageDo.getFinishedStage()+","+userStageDo.getCounterAttackTime()+","+userStageDo.getType()+","+userStageDo.getRoleId()+")";
        
        long userstageid;

        KeyHolder keyHolder = new GeneratedKeyHolder();
        mokaJdbcTemplate.update(new PreparedStatementCreator() {

            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(insertSql.toString());
                return ps;
            }

        }, keyHolder);
        try {
        	userstageid = keyHolder.getKey().longValue();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            userstageid = 0;
        }
        if (userstageid > 0) {
            id = new Long(userstageid);
        } else {
            return 0l;
        }
		return id;
	}
	public int updateUserStage(int finishedstage,Long userstageid){
        String sql = "update t_cgiser_userstage set finishedstage = ? where id=?";
        String[] para = new String[2];
        para[0] = String.valueOf(finishedstage);
        para[1] = String.valueOf(userstageid);
        return mokaJdbcTemplate.update(sql,para);
	}
	public JdbcTemplate getMokaJdbcTemplate() {
		return mokaJdbcTemplate;
	}
	public void setMokaJdbcTemplate(JdbcTemplate mokaJdbcTemplate) {
		this.mokaJdbcTemplate = mokaJdbcTemplate;
	}
}

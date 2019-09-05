package com.cgiser.moka.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class UserSoulDao {
	private static final Logger logger = LoggerFactory.getLogger(SoulDao.class);
	private JdbcTemplate mokaJdbcTemplate;
	public List<Map<String,Object>> getSoulsByRoleId(Long roleId){
		String[] para = new String[1];
		para[0] = String.valueOf(roleId);
        String sql = "select * From t_cgiser_usersoul where roleId = ? and state =1";
        List list = mokaJdbcTemplate.queryForList(sql,para);
        return list;
		
	}
	public Map<String,Object> getUserSoulById(Long userSoulId){
		String[] para = new String[1];
		para[0] = String.valueOf(userSoulId);
        String sql = "select * From t_cgiser_usersoul where userSoulId=? and state =1";
        return mokaJdbcTemplate.queryForMap(sql,para);
		
	}
	public int deleteUserSoulById(Long userSoulId){
		String[] para = new String[1];
		para[0] = String.valueOf(userSoulId);
        String sql = "update t_cgiser_usersoul set state=0 where userSoulId=?";
        return mokaJdbcTemplate.update(sql, para);
		
	}
	public Long saveUserSoul(int soulId,Long roleId,int level){
		Long id = new Long(0);
        //String sql = "select USERID,USERIDEN,USERNAME,CREATETIME,UDID,ISACTIVE,STATE from t_user_user where STATE = 1 and username=?";
        final String insertSql = "insert into t_cgiser_usersoul (soulid,roleid,level,state)values("+soulId+","+roleId+","+level+","+1+")";
        
        long userSoulId;

        KeyHolder keyHolder = new GeneratedKeyHolder();
        mokaJdbcTemplate.update(new PreparedStatementCreator() {

            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(insertSql.toString());
                return ps;
            }

        }, keyHolder);
        try {
        	userSoulId = keyHolder.getKey().longValue();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            userSoulId = 0;
        }
        if (userSoulId > 0) {
            id = new Long(userSoulId);
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

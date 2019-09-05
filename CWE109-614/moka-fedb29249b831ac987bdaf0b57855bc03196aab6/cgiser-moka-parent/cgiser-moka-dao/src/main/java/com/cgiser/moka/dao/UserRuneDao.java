package com.cgiser.moka.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class UserRuneDao {
	private static final Logger logger = LoggerFactory.getLogger(UserCardDao.class);
	private JdbcTemplate mokaJdbcTemplate;
	public List<Map<String,Object>> getUserRunes(Long roleId){
		List list = new ArrayList();
        String sql = "select * From t_cgiser_userrune where state = 1 and roleid = ?";
        String[] para = new String[1];
        para[0] = String.valueOf(roleId);
        list = mokaJdbcTemplate.queryForList(sql,para);
        return list;
	}
	public List<Map<String,Object>> getUserOwnRunes(Long roleId){
		List list = new ArrayList();
        String sql = "select * From t_cgiser_userrune where roleid = ? group by runeid";
        String[] para = new String[1];
        para[0] = String.valueOf(roleId);
        list = mokaJdbcTemplate.queryForList(sql,para);
        return list;
	}
	public Map<String,Object> getUserRuneById(Long userRuneId){
        String sql = "select * From t_cgiser_userrune where state = 1 and USERRUNEID = ?";
        String[] para = new String[1];
        para[0] = String.valueOf(userRuneId);
        return mokaJdbcTemplate.queryForMap(sql,para);
	}
	public List<Map<String,Object>> getUserGroupRunes(Long roleId,Long groupId){
        String sql = "select * From t_cgiser_userrune where state = 1 and roleid = ? and groupid=?";
        String[] para = new String[2];
        para[0] = String.valueOf(roleId);
        para[1] = String.valueOf(groupId);
        return mokaJdbcTemplate.queryForList(sql,para);
	}
	public int delUserRuneById(Long userRuneId){
        String sql = "update t_cgiser_userrune set state = 0 where userruneid = ? and state = 1";
        String[] para = new String[1];
        para[0] = String.valueOf(userRuneId);
        return mokaJdbcTemplate.update(sql,para);
	}
	public int updateUserRune(Long userRuneId,int level,int exp){
        String sql = "update t_cgiser_userrune set level = ?,exp= ? where userruneid = ? and state = 1";
        String[] para = new String[3];
        para[0] = String.valueOf(level);
        para[1] = String.valueOf(exp);
        para[2] = String.valueOf(userRuneId);
        return mokaJdbcTemplate.update(sql,para);
	}
	public Long saveUserRune(int runeId,Long roleId){
		Long id = new Long(0);
        //String sql = "select USERID,USERIDEN,USERNAME,CREATETIME,UDID,ISACTIVE,STATE from t_user_user where STATE = 1 and username=?";
        final String insertSql = "insert into t_cgiser_userrune (runeid,roleid,state)values("+runeId+","+roleId+","+1+")";
        
        long userruneid;

        KeyHolder keyHolder = new GeneratedKeyHolder();
        mokaJdbcTemplate.update(new PreparedStatementCreator() {

            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(insertSql.toString());
                return ps;
            }

        }, keyHolder);
        try {
        	userruneid = keyHolder.getKey().longValue();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            userruneid = 0;
        }
        if (userruneid > 0) {
            id = new Long(userruneid);
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

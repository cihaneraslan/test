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

public class UserCardDao {
	private static final Logger logger = LoggerFactory.getLogger(UserCardDao.class);
	private JdbcTemplate mokaJdbcTemplate;
	public List<Map<String,Object>> getUserCards(Long roleId,int state){
		List list = new ArrayList();
        String sql = "select * From t_cgiser_usercard where state = ? and roleid = ?";
        String[] para = new String[2];
        para[0] = String.valueOf(state);
        para[1] = String.valueOf(roleId);
        list = mokaJdbcTemplate.queryForList(sql,para);
        return list;
	}
	public List<Map<String,Object>> getUserCardsBySoul(Long roleId,int state){
		List list = new ArrayList();
        String sql = "select * From t_cgiser_usercard where state = ? and roleid = ? and soul != 0";
        String[] para = new String[2];
        para[0] = String.valueOf(state);
        para[1] = String.valueOf(roleId);
        list = mokaJdbcTemplate.queryForList(sql,para);
        return list;
	}
	public Map<String,Object> getUserCardsByUserSoulId(Long roleId,Long userSoulId,int state){
        String sql = "select * From t_cgiser_usercard where state = ? and roleid = ? and soul = ?";
        String[] para = new String[3];
        para[0] = String.valueOf(state);
        para[1] = String.valueOf(roleId);
        para[2] = String.valueOf(userSoulId);
        return mokaJdbcTemplate.queryForMap(sql,para);
	}
	public List<Map<String,Object>> getUserOwnCards(Long roleId){
		List list = new ArrayList();
        String sql = "select * From t_cgiser_usercard where roleid = ? GROUP BY CARDID";
        String[] para = new String[1];
        para[0] = String.valueOf(roleId);
        list = mokaJdbcTemplate.queryForList(sql,para);
        return list;
	}
	public List<Map<String,Object>> getUserGroupCards(Long roleId,Long groupId){
		List list = new ArrayList();
        String sql = "select * From t_cgiser_usercard where state = 1 and roleid = ? and groupid = ?";
        String[] para = new String[2];
        para[0] = String.valueOf(roleId);
        para[1] = String.valueOf(groupId);
        list = mokaJdbcTemplate.queryForList(sql,para);
        return list;
	}
	public Map<String,Object> getUserCardById(Long userCardId){
        String sql = "select * From t_cgiser_usercard where state = 1 and usercardid = ?";
        String[] para = new String[1];
        para[0] = String.valueOf(userCardId);
        return mokaJdbcTemplate.queryForMap(sql,para);
	}
	public int delUserCardById(Long userCardId){
        String sql = "update t_cgiser_usercard set state = 0 where usercardid = ? and state = 1";
        String[] para = new String[1];
        para[0] = String.valueOf(userCardId);
        return mokaJdbcTemplate.update(sql,para);
	}
	public int updateUserCard(Long userCardId,int level,int exp){
        String sql = "update t_cgiser_usercard set level = ?,exp= ? where usercardid = ? and state = 1";
        String[] para = new String[3];
        para[0] = String.valueOf(level);
        para[1] = String.valueOf(exp);
        para[2] = String.valueOf(userCardId);
        return mokaJdbcTemplate.update(sql,para);
	}
	public int updateUserSoul(Long userCardId,Long userSoulId){
        String sql = "update t_cgiser_usercard set soul = ? where usercardid = ? and state = 1";
        String[] para = new String[2];
        para[0] = String.valueOf(userSoulId);
        para[1] = String.valueOf(userCardId);
        return mokaJdbcTemplate.update(sql,para);
	}
	public Long saveUserCard(int cardId,Long roleId){
		Long id = new Long(0);
        //String sql = "select USERID,USERIDEN,USERNAME,CREATETIME,UDID,ISACTIVE,STATE from t_user_user where STATE = 1 and username=?";
        final String insertSql = "insert into t_cgiser_usercard (cardid,roleid,state)values("+cardId+","+roleId+","+1+")";
        
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

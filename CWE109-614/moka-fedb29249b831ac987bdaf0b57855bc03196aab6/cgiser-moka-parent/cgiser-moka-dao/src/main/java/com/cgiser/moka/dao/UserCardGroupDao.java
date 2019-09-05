package com.cgiser.moka.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.CollectionUtils;

public class UserCardGroupDao {
	private static final Logger logger = LoggerFactory.getLogger(UserCardGroupDao.class);
	private JdbcTemplate mokaJdbcTemplate;
	public List<Map<String,Object>> getUserCardGroups(Long roleId){
		List list = new ArrayList();
        String sql = "select * From t_cgiser_usercard_group where state = 1 and roleid = ?";
        String[] para = new String[1];
        para[0] = String.valueOf(roleId);
        list = mokaJdbcTemplate.queryForList(sql,para);
        return list;
		
	}
	public Map<String,Object> getUserCardGroupById(Long groupId){
        String sql = "select * From t_cgiser_usercard_group where state = 1 and groupid = ?";
        String[] para = new String[1];
        para[0] = String.valueOf(groupId);
        return mokaJdbcTemplate.queryForMap(sql,para);
		
	}
	public int updateUserGroup(Long groupId,String cardIds,String runeIds){
//        String sql = "select * From t_cgiser_usercard_group where state = 1 and groupid = ?";
		String sql = "update t_cgiser_usercard_group set ";// cardlist=?,runelist=? where state = 1 and groupid = ?";
		if(!StringUtils.isBlank(cardIds)){
			sql = sql+"cardlist='"+cardIds+"'";
		}
		sql = sql+",runelist='"+runeIds+"' ";
//		if(!StringUtils.isBlank(runeIds)){
//			sql = sql+",runelist='"+runeIds+"' ";
//		}
		sql = sql+" where state = 1 and groupid = ? ";
        String[] para = new String[1];
        para[0] = String.valueOf(groupId);
        return mokaJdbcTemplate.update(sql,para);
		
	}
	public int updateUserGroupName(Long groupId,String groupName){
//      String sql = "select * From t_cgiser_usercard_group where state = 1 and groupid = ?";
		String sql = "update t_cgiser_usercard_group set groupname = ?";// cardlist=?,runelist=? where state = 1 and groupid = ?";
		sql = sql+" where state = 1 and groupid = ? ";
      String[] para = new String[2];
      para[0] = groupName;
      para[1] = String.valueOf(groupId);
      return mokaJdbcTemplate.update(sql,para);
		
	}
	public Long insertUserGroup(Long roleId,String groupName,String cards,String runes){
		Long id = new Long(0);
		final String insertSql;
		Long groupId = new Long(0);
		insertSql = "insert into t_cgiser_usercard_group (ROLEID,GROUPNAME,CARDLIST,RUNELIST,STATE) values ("+roleId+",'"+groupName+"','"+cards+"','"+runes+"',"+1+")";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        mokaJdbcTemplate.update(new PreparedStatementCreator() {

            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(insertSql.toString());
                return ps;
            }

        }, keyHolder);
        if(groupId>0){
        	return groupId;
        }
        try {
        	groupId = keyHolder.getKey().longValue();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            groupId = 0L;
        }
        if (groupId > 0) {
            id = new Long(groupId);
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

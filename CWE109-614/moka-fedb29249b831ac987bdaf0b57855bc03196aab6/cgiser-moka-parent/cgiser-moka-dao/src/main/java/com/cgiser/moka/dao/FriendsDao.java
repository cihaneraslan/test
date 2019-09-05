package com.cgiser.moka.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
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

public class FriendsDao {
	private static final Logger logger = LoggerFactory.getLogger(FriendsDao.class);
	private JdbcTemplate mokaJdbcTemplate;
	
	
	public List<Map<String, Object>> getFriendsByRoleName(String roleName){
		 if (StringUtils.isEmpty(roleName))
	            return null;
	        List list = new ArrayList();
	        String sql = "select ID,ROLENAME1,ROLENAME2 from t_cgiser_friends where STATE = 1 and ISACTIVE = 1 and (roleName1=? or roleName2=?)";
	        String[] para = new String[2];
	        para[0] = StringUtils.trim(roleName);
	        para[1] = StringUtils.trim(roleName);
	        list = mokaJdbcTemplate.queryForList(sql, para);
	        return list;
		
	}
	public Map findFriendInvit(String roleName1,String roleName2,int isActive,int state){
		 if (StringUtils.isEmpty(roleName1)||StringUtils.isEmpty(roleName2))
	            return null;
	        Map map = new HashMap();
	        String sql = "select ID,ROLENAME1,ROLENAME2 from t_cgiser_friends where roleName1=? and roleName2=?";
	        if(isActive!=-1){
	        	sql = sql+ " and ISACTIVE = "+isActive;
	        }
	        if(state!=-1){
	        	sql = sql+ " and STATE = "+state;
	        }
	        
	        String[] para = new String[2];
	        para[0] = StringUtils.trim(roleName1);
	        para[1] = StringUtils.trim(roleName2);
	        map = mokaJdbcTemplate.queryForMap(sql, para);
	        return map;
		
	}
	public List<Map<String, Object>> getFriendsInviteByRoleName(String roleName){
		 if (StringUtils.isEmpty(roleName))
	            return null;
	        List list = new ArrayList();
	        String sql = "select ID,ROLENAME1,ROLENAME2 from t_cgiser_friends where STATE = 1 and roleName2=? and ISACTIVE = 0 ";
	        String[] para = new String[1];
	        para[0] = StringUtils.trim(roleName);
	        list = mokaJdbcTemplate.queryForList(sql, para);
	        return list;
		
	}
	public Long inviteFriend(String roleName1, String roleName2) {
		Long id = new Long(0);
		final String insertSql;
		Map map = findFriendInvit(roleName1,roleName2,-1,0);
		Long friendid = new Long(0);
		if(CollectionUtils.isEmpty(map)){
			insertSql = "insert into t_cgiser_friends (ROLENAME1,ROLENAME2,STATE) values ('"+roleName1+"','"+roleName2+"',"+1+")";
		}else{
			friendid = new Long((String)map.get("ID"));
			insertSql = "update t_cgiser_friends set isactive=0 , state = 1 where ROLENAME1='"+roleName1+"' and ROLENAME2='"+roleName2+"'";;
		}
		
        //String sql = "select USERID,USERIDEN,USERNAME,CREATETIME,UDID,ISACTIVE,STATE from t_user_user where STATE = 1 and username=?";
        
        

        KeyHolder keyHolder = new GeneratedKeyHolder();
        mokaJdbcTemplate.update(new PreparedStatementCreator() {

            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(insertSql.toString());
                return ps;
            }

        }, keyHolder);
        if(friendid>0){
        	return friendid;
        }
        try {
        	friendid = keyHolder.getKey().longValue();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            friendid = 0L;
        }
        if (friendid > 0) {
            id = new Long(friendid);
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
	public Long applyFriendInvite(String roleName1, String roleName2) {
		Long id = new Long(0);
        //String sql = "select USERID,USERIDEN,USERNAME,CREATETIME,UDID,ISACTIVE,STATE from t_user_user where STATE = 1 and username=?";
        String updateSql = "update t_cgiser_friends set isactive="+1+" where ROLENAME1='"+roleName1+"' and ROLENAME2='"+roleName2+"'";

        if(mokaJdbcTemplate.update(updateSql)>0){
        	return 1L;
        }else{
        	return 0L;
        }
	}
	public Long delFriendInvite(String roleName1, String roleName2) {
        //String sql = "select USERID,USERIDEN,USERNAME,CREATETIME,UDID,ISACTIVE,STATE from t_user_user where STATE = 1 and username=?";
        final String updateSql = "update t_cgiser_friends set state="+0+" where ROLENAME1='"+roleName1+"' and ROLENAME2='"+roleName2+"'";
        if(mokaJdbcTemplate.update(updateSql)>0){
        	return 1L;
        }else{
        	return 0L;
        }
	}
}

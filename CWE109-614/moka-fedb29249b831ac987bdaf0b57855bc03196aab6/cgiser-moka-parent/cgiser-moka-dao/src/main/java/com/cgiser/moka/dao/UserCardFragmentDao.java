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
import org.springframework.util.CollectionUtils;

public class UserCardFragmentDao {
	private static final Logger logger = LoggerFactory.getLogger(CardFragmentDao.class);
	private JdbcTemplate mokaJdbcTemplate;
	public List<Map<String,Object>> getUserFragmentByRoleId(Long roleId){
        String sql = "select * From t_cgiser_rolecardfragment where num>0 and roleId=? and state=1";
        String[] para = new String[1];
        para[0] = String.valueOf(roleId);
        return  mokaJdbcTemplate.queryForList(sql,para);
		
	}
	public Long addUserFragment(Long roleId,int fragmentId,int num){
		Map<String,Object> map = getUserFragmentByRoleIdFragmentId(roleId,fragmentId);
		if(CollectionUtils.isEmpty(map)){
			Long id = new Long(0);
	        //String sql = "select USERID,USERIDEN,USERNAME,CREATETIME,UDID,ISACTIVE,STATE from t_user_user where STATE = 1 and username=?";
	        final String insertSql = "insert into t_cgiser_rolecardfragment (roleid,fragmentid,num)values("+roleId+","+fragmentId+","+num+")";
	        
	        long userFragmentId;

	        KeyHolder keyHolder = new GeneratedKeyHolder();
	        mokaJdbcTemplate.update(new PreparedStatementCreator() {

	            @Override
	            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
	                PreparedStatement ps = con.prepareStatement(insertSql.toString());
	                return ps;
	            }

	        }, keyHolder);
	        try {
	        	userFragmentId = keyHolder.getKey().longValue();
	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            userFragmentId = 0;
	        }
	        if (userFragmentId > 0) {
	            id = new Long(userFragmentId);
	        } else {
	            return 0l;
	        }
			return id;
		}
		
		String sql = "update t_cgiser_rolecardfragment set num = num + ? where roleId = ? and fragmentid = ? and state = 1";
		String[] para = new String[3];
        para[0] = String.valueOf(num);
        para[1] = String.valueOf(roleId);
        para[2] = String.valueOf(fragmentId);
        if(mokaJdbcTemplate.update(sql,para)>0){
        	return new Long(map.get("ID").toString());
        }else{
        	return 0L;
        }
          
		
	}
	public Map<String,Object> getUserFragmentByRoleIdFragmentId(Long roleId,int fragmentId){
		 String sql = "select * From t_cgiser_rolecardfragment where roleId="+roleId+" and fragmentId="+fragmentId+" and state=1";
		 return mokaJdbcTemplate.queryForMap(sql);
	}
	/**
	 * 接口暂时不能使用
	 * @param roleId
	 * @param type
	 * @return
	 */
	public List<Map<String,Object>> getUserFragmentByType(Long roleId,int type){
		 String sql = "select * From t_cgiser_rolecardfragment where roleId="+roleId+" and type="+type+" and state=1";
		 return mokaJdbcTemplate.queryForList(sql);
	}
	public int delUserFragment(Long roleId,int fragmentId,int num){
		Map<String,Object> map = getUserFragmentByRoleIdFragmentId(roleId,fragmentId);
		if(CollectionUtils.isEmpty(map)){
			return 0;
		}
		int max = Integer.parseInt((String)map.get("NUM"));
		if(max<num){
			return 0;
		}
		String sql = "update t_cgiser_rolecardfragment set num = num - ? where roleId = ? and fragmentid = ? and state = 1";
		String[] para = new String[3];
        para[0] = String.valueOf(num);
        para[1] = String.valueOf(roleId);
        para[2] = String.valueOf(fragmentId);
        return mokaJdbcTemplate.update(sql,para);
		
	}
	public JdbcTemplate getMokaJdbcTemplate() {
		return mokaJdbcTemplate;
	}
	public void setMokaJdbcTemplate(JdbcTemplate mokaJdbcTemplate) {
		this.mokaJdbcTemplate = mokaJdbcTemplate;
	}
}

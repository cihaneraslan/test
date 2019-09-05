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
import org.springframework.util.CollectionUtils;

public class TokenDao {
	private static final Logger logger = LoggerFactory.getLogger(TokenDao.class);
	private JdbcTemplate mokaJdbcTemplate;
	public List<Map<String,Object>>getAllToken(){
		List list = new ArrayList();
        String sql = "select * From t_cgiser_token where state=1 group by TOKENID";
        list = mokaJdbcTemplate.queryForList(sql);
        return list;
		
	}
	public List<Map<String,Object>>getRoleToken(Long roleId){
		List list = new ArrayList();
        String sql = "select * From t_cgiser_token where  roleid="+roleId+" and state=1";
        list = mokaJdbcTemplate.queryForList(sql);
        return list;
		
	}
	public Map<String,Object> getRoleTokenByRoleTokenId(Long roleId,String tokenId){
        String sql = "select * From t_cgiser_token where roleid= ? and tokenId= ? and state=1";
        String[] para = new String[2];
        para[0] = String.valueOf(roleId);
        para[1] = tokenId;
        return mokaJdbcTemplate.queryForMap(sql,para);
		
	}
	public Map<String,Object> getTokenByTokenId(String tokenId){
        String sql = "select * From t_cgiser_token where tokenId= ? and state=1";
        String[] para = new String[1];
        para[0] = tokenId;
        return mokaJdbcTemplate.queryForMap(sql,para);
		
	}
	public Long addTokenId(Long roleId,String tokenId){
		Map<String,Object> map =  this.getRoleTokenByRoleTokenId(roleId,tokenId);
		if(CollectionUtils.isEmpty(map)){
			final String insertSql = "insert into t_cgiser_token (roleid,tokenid,state) values ("+roleId+",'"+tokenId+"',1)";
	        //String sql = "select USERID,USERIDEN,USERNAME,CREATETIME,UDID,ISACTIVE,STATE from t_user_user where STATE = 1 and username=?";
	        
	        
			Long addId;
	        KeyHolder keyHolder = new GeneratedKeyHolder();
	        mokaJdbcTemplate.update(new PreparedStatementCreator() {

	            @Override
	            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
	                PreparedStatement ps = con.prepareStatement(insertSql.toString());
	                return ps;
	            }

	        }, keyHolder);
	        try {
	        	addId = keyHolder.getKey().longValue();
	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            addId = 0L;
	        }
	        if (addId > 0) {
	           return addId;
	        } else {
	            return 0L;
	        }
		}else{
			return new Long(map.get("ID").toString());
		}
	}
	public JdbcTemplate getMokaJdbcTemplate() {
		return mokaJdbcTemplate;
	}
	public void setMokaJdbcTemplate(JdbcTemplate mokaJdbcTemplate) {
		this.mokaJdbcTemplate = mokaJdbcTemplate;
	}
}

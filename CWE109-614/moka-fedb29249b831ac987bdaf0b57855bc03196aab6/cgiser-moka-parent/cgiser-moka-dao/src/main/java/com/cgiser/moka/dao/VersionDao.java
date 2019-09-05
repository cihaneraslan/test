package com.cgiser.moka.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class VersionDao {
	private static final Logger logger = LoggerFactory.getLogger(VersionDao.class);
	private JdbcTemplate mokaJdbcTemplate;

	public Map<String,Object> getLastVersion(){
        String sql = "SELECT * from t_cgiser_version WHERE CREATETIME=(SELECT MAX(CREATETIME) FROM `t_cgiser_version`) and state=1";
        return mokaJdbcTemplate.queryForMap(sql);
		
	}
	public Long addVersion(String appurl,String appversion,String http,String stop){
		final String insertSql = "insert into t_cgiser_version (appurl,http,appversion,createtime,state) values ('"+appurl+"','"+http+"','"+new Timestamp(new Date().getTime())+"','"+http+"',1)";
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
	}
	public JdbcTemplate getMokaJdbcTemplate() {
		return mokaJdbcTemplate;
	}
	public void setMokaJdbcTemplate(JdbcTemplate mokaJdbcTemplate) {
		this.mokaJdbcTemplate = mokaJdbcTemplate;
	}
}

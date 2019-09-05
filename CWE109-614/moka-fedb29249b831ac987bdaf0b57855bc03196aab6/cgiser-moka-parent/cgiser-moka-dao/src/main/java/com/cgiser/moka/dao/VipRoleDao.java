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

public class VipRoleDao {
	private static final Logger logger = LoggerFactory.getLogger(VersionDao.class);
	private JdbcTemplate mokaJdbcTemplate;

	public Map<String,Object> getVipInfo(Long roleId){
        String sql = "SELECT * from t_cgiser_vip WHERE roleid="+roleId+" and state=1";
        return mokaJdbcTemplate.queryForMap(sql);
		
	}
	public Long addVipInfo(Long roleId,String vipcode,int vip){
		final String insertSql = "insert into t_cgiser_vip (roleid,vipcode,createtime,vip,state) values ("+roleId+",'"+vipcode+"','"+
		new Timestamp(new Date().getTime())+"',"+vip+",1)";
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
	public int updateVipInfo(Long roleId,int vip){
		String sql = "update t_cgiser_vip set vip = ? where roleid=?";
		String[] para = new String[2];
		para[0] = String.valueOf(vip);
		para[1] = String.valueOf(roleId);
		return mokaJdbcTemplate.update(sql, para);
	}
	public JdbcTemplate getMokaJdbcTemplate() {
		return mokaJdbcTemplate;
	}
	public void setMokaJdbcTemplate(JdbcTemplate mokaJdbcTemplate) {
		this.mokaJdbcTemplate = mokaJdbcTemplate;
	}
}

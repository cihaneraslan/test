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

public class ChipDao {
	private static final Logger logger = LoggerFactory.getLogger(ChipDao.class);
	private JdbcTemplate mokaJdbcTemplate;
	public List<Map<String,Object>>getUserChip(Long roleId){
		List list = new ArrayList();
        String sql = "select * From t_cgiser_chip where roleid="+roleId;
        list = mokaJdbcTemplate.queryForList(sql);
        return list;
		
	}
	public Map<String,Object> getUserChip(Long roleId ,int type){
        String sql = "select * From t_cgiser_chip where roleid= ? and type=?";
        String[] para = new String[2];
        para[0] = String.valueOf(roleId);
        para[1] = String.valueOf(type);
        return mokaJdbcTemplate.queryForMap(sql,para);
		
	}
	public int addUserChip(Long roleId, int type, int num){
		if(CollectionUtils.isEmpty(this.getUserChip(roleId, type))){
			final String insertSql = "insert into t_cgiser_chip (roleid,type,num) values ("+roleId+","+type+","+num+")";
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
	           return 1;
	        } else {
	            return 0;
	        }
		}else{
			String sql = "update t_cgiser_chip set num = num + ? where roleid= ? and type=? ";
		    String[] para = new String[3];
		    para[0] = String.valueOf(num);
		    para[1] = String.valueOf(roleId);
		    para[2] = String.valueOf(type);
		    return mokaJdbcTemplate.update(sql,para);
		}
	}
	public int exChange(Long roleId,int type,int num){
		String sql = "update t_cgiser_chip set num = num - ? where roleid= ? and type=? ";
	    String[] para = new String[3];
	    para[0] = String.valueOf(num);
	    para[1] = String.valueOf(roleId);
	    para[2] = String.valueOf(type);
	    return mokaJdbcTemplate.update(sql,para);
	}
	public JdbcTemplate getMokaJdbcTemplate() {
		return mokaJdbcTemplate;
	}
	public void setMokaJdbcTemplate(JdbcTemplate mokaJdbcTemplate) {
		this.mokaJdbcTemplate = mokaJdbcTemplate;
	}
}

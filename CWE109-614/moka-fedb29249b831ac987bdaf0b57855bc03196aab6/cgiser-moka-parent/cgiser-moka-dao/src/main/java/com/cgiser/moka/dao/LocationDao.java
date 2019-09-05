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

public class LocationDao {
	private static final Logger logger = LoggerFactory.getLogger(MapDao.class);
	private JdbcTemplate mokaJdbcTemplate;
	public int saveLocation(String city, String location){
        //String sql = "select USERID,USERIDEN,USERNAME,CREATETIME,UDID,ISACTIVE,STATE from t_user_user where STATE = 1 and username=?";
        String insertSql = "update t_cgiser_city set pnts = '"+location+"' where name like '%"+city+"%'";
        return mokaJdbcTemplate.update(insertSql);
       
	}
	public List<Map<String,Object>> getLocations(){
		String sql = "select * from t_cgiser_city";
		return mokaJdbcTemplate.queryForList(sql);
	}
	public List<Map<String,Object>> getRoleLocationByCity(String city){
		String sql = "select * from t_cgiser_location where city='"+city+"'";
		return mokaJdbcTemplate.queryForList(sql);
	}
	public Map<String,Object> getRoleLocationByRoleId(Long roleId){
		String sql = "select * from t_cgiser_location where roleid="+roleId;
		return mokaJdbcTemplate.queryForMap(sql);
	}
	public Long saveRoleLocation(Long roleId,String roleName,double geoX,double geoY,String city){
		Long id = new Long(0);
        //String sql = "select USERID,USERIDEN,USERNAME,CREATETIME,UDID,ISACTIVE,STATE from t_user_user where STATE = 1 and username=?";
        final String insertSql = "insert into t_cgiser_location (roleid,rolename,geoX,geoY,city)values("+roleId+",'"+roleName+"',"+geoX+","+geoY+",'"+city+"')";
        
        long locationId;

        KeyHolder keyHolder = new GeneratedKeyHolder();
        mokaJdbcTemplate.update(new PreparedStatementCreator() {

            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(insertSql.toString());
                return ps;
            }

        }, keyHolder);
        try {
        	locationId = keyHolder.getKey().longValue();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            locationId = 0;
        }
        if (locationId > 0) {
            id = new Long(locationId);
        } else {
            return 0l;
        }
		return id;
	}
	public Long saveRoleLocationAddress(Long roleId,String roleName,double geoX,double geoY,String city,String address,String addressComponent){
		Long id = new Long(0);
        //String sql = "select USERID,USERIDEN,USERNAME,CREATETIME,UDID,ISACTIVE,STATE from t_user_user where STATE = 1 and username=?";
        final String insertSql = "insert into t_cgiser_location (roleid,rolename,geoX,geoY,city,address,addressComponent)values("+roleId+",'"
        +roleName+"',"+geoX+","+geoY+",'"+city+"','"+address+"','"+addressComponent+"')";
        
        long locationId;

        KeyHolder keyHolder = new GeneratedKeyHolder();
        mokaJdbcTemplate.update(new PreparedStatementCreator() {

            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(insertSql.toString());
                return ps;
            }

        }, keyHolder);
        try {
        	locationId = keyHolder.getKey().longValue();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            locationId = 0;
        }
        if (locationId > 0) {
            id = new Long(locationId);
        } else {
            return 0l;
        }
		return id;
	}
	public int updateRoleLocation(Long roleId,double geoX,double geoY,String city){
        String insertSql = "update t_cgiser_location set geox = "+geoX+",geoy="+geoY+",city='"+city+"' where roleid = "+roleId;
        return mokaJdbcTemplate.update(insertSql);
	}
	public int updateRoleLocationAddress(Long roleId,double geoX,double geoY,String city,String address,String addressComponent){
        String insertSql = "update t_cgiser_location set geox = "+geoX+",geoy="+geoY+",city='"+city+"',address='"+address+"',addressComponent='"+addressComponent+"' where roleid = "+roleId;
        return mokaJdbcTemplate.update(insertSql);
	}
	public JdbcTemplate getMokaJdbcTemplate() {
		return mokaJdbcTemplate;
	}
	public void setMokaJdbcTemplate(JdbcTemplate mokaJdbcTemplate) {
		this.mokaJdbcTemplate = mokaJdbcTemplate;
	}
}

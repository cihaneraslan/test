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

public class StageLevelDao {
	private static final Logger logger = LoggerFactory.getLogger(MapDao.class);
	private JdbcTemplate mokaJdbcTemplate;
	public List<Map<String,Object>> getStageLevels(){
		List list = new ArrayList();
        String sql = "select * From t_cgiser_stagelevel";
        list = mokaJdbcTemplate.queryForList(sql);
        return list;
	}	
	public List<Map<String,Object>> getStageLevelsByStageId(int stageId){
		List list = new ArrayList();
        String sql = "select * From t_cgiser_stagelevel where stageid = ?";
        String[] para = new String[1];
        para[0] = String.valueOf(stageId);
        list = mokaJdbcTemplate.queryForList(sql,para);
        return list;
	}
	public Map<String,Object> getStageLevelByStageLevelId(int stagelevelid){
        String sql = "select * From t_cgiser_stagelevel where id = ?";
        String[] para = new String[1];
        para[0] = String.valueOf(stagelevelid);
        return mokaJdbcTemplate.queryForMap(sql,para);
	}
	public int installSoul(int stagelevelid,String soulList){
        String sql = "update t_cgiser_stagelevel set soullist=? where id = ?";
        String[] para = new String[2];
        para[0] = soulList;
        para[1] = String.valueOf(stagelevelid);
        return mokaJdbcTemplate.update(sql,para);
	}
	public List<Map<String,Object>> getStageLevelColorByStageLevelId(int stageLevelId){
		List list = new ArrayList();
        String sql = "select * From t_cgiser_stagelevel_color where stagelevelid = ? and state=1";
        String[] para = new String[1];
        para[0] = String.valueOf(stageLevelId);
        list = mokaJdbcTemplate.queryForList(sql,para);
        return list;
	}
	public int deleteStageLevelColor(Long colorId){
        String sql = "update t_cgiser_stagelevel_color set state=? where id = ?";
        String[] para = new String[2];
        para[0] = "0";
        para[1] = String.valueOf(colorId);
        return mokaJdbcTemplate.update(sql,para);
	}
	public Map<String,Object> getStageConditionsByStageConditionsId(int id){
        String sql = "select * From t_cgiser_stage_conditions where id = ?";
        String[] para = new String[1];
        para[0] = String.valueOf(id);
        return mokaJdbcTemplate.queryForMap(sql,para);
	}
	public Long addStageLevelColor(int stageLevelId, int type,
			int value, Float color) {
		Long id = new Long(0);
        //String sql = "select USERID,USERIDEN,USERNAME,CREATETIME,UDID,ISACTIVE,STATE from t_user_user where STATE = 1 and username=?";
        final String insertSql = "insert into t_cgiser_stagelevel_color (stagelevelid,type,value,color)values("+stageLevelId+","+type+","+value+","+color+")";
        
        long user3did;

        KeyHolder keyHolder = new GeneratedKeyHolder();
        mokaJdbcTemplate.update(new PreparedStatementCreator() {

            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(insertSql.toString());
                return ps;
            }

        }, keyHolder);
        try {
        	user3did = keyHolder.getKey().longValue();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            user3did = 0;
        }
        if (user3did > 0) {
            id = new Long(user3did);
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

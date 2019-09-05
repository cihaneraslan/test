package com.cgiser.moka.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class ThirdDao {
	private static final Logger logger = LoggerFactory.getLogger(FriendsDao.class);
	private JdbcTemplate mokaJdbcTemplate;
	public Long addRecord(Long roleId,int first,int second,int third,int count,int date) {
		Long id = new Long(0);
        //String sql = "select USERID,USERIDEN,USERNAME,CREATETIME,UDID,ISACTIVE,STATE from t_user_user where STATE = 1 and username=?";
        final String insertSql = "insert into t_cgiser_3d (roleid,first,second,third,count,date,storetime)values("+roleId+","+first+","
        +second+","+third+","+count+","+date+",'"+new Timestamp(new Date().getTime())+"')";
        
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
	public Long addThirdDate(String issue) {
		Long id = new Long(0);
        //String sql = "select USERID,USERIDEN,USERNAME,CREATETIME,UDID,ISACTIVE,STATE from t_user_user where STATE = 1 and username=?";
        final String insertSql = "insert into t_cgiser_3date (issue,state,isnow,date)values('"+issue+"',"+1+","+1+",'"+new Timestamp(new Date().getTime())+"')";
        
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
	/**
	 * 获取玩家当前期与上一期的投注记录
	 * @param roleId
	 * @return
	 */
	public List<Map<String,Object>> getThirdRecords(Long roleId) {
		String sql = "select * from t_cgiser_3d where roleId=? and isrecive = 0";
		String[] para = new String[1];
		para[0] = String.valueOf(roleId);
		return mokaJdbcTemplate.queryForList(sql,para);
	}
	public List<Map<String,Object>> getThirdRecordsByRoleIdIssue(Long roleId,String issue) {
		String sql = "select * from t_cgiser_3d where date = ? and roleId = ?";
		String[] para = new String[2];
		para[0] = String.valueOf(issue);
		para[1] = String.valueOf(roleId);
		return mokaJdbcTemplate.queryForList(sql,para);
	}
	public List<Map<String,Object>> getThirdRecordWinner() {
		String sql = "select * from t_cgiser_3d where iswin = 1 and TIMESTAMPDIFF(DAY,NOW(),storetime)>-6 ";
		return mokaJdbcTemplate.queryForList(sql);
	}
	public List<Map<String,Object>> getThirdRecordsByIssue(String issue) {
		String sql = "select * from t_cgiser_3d where date = ? and isrecive = 0";
		String[] para = new String[1];
		para[0] = String.valueOf(issue);
		return mokaJdbcTemplate.queryForList(sql,para);
	}
	public int updateThirdRecordIsRecive(Long id) {
		String sql = "update t_cgiser_3d set isrecive = 1 where id = ? and isrecive = 0";
		String[] para = new String[1];
		para[0] = String.valueOf(id);
		return mokaJdbcTemplate.update(sql,para);
	}
	public int updateThirdRecordIsWin(Long id) {
		String sql = "update t_cgiser_3d set iswin = 1 where id = ? and isrecive = 0";
		String[] para = new String[1];
		para[0] = String.valueOf(id);
		return mokaJdbcTemplate.update(sql,para);
	}
	/**
	 * 将当前期修改为上一期
	 * @return
	 */
	public int updateThirdate() {
		String sql = "update t_cgiser_3date set isnow = 0 where isnow=?";
		String[] para = new String[1];
		para[0] = String.valueOf(1);
		return mokaJdbcTemplate.update(sql,para);
	}
	/**
	 * 修改上一期状态为已发奖
	 * @return
	 */
	public int updateThirdateState() {
		String sql = "update t_cgiser_3date set state = 0 where isnow=? and state = 1";
		String[] para = new String[1];
		para[0] = String.valueOf(0);
		return mokaJdbcTemplate.update(sql,para);
	}
	/**
	 * 修改之前期号的中奖号码
	 * @return
	 */
	public int updateThirdateNum(String num) {
		String sql = "update t_cgiser_3date set num = ? where isnow=0 and state = 1";
		String[] para = new String[1];
		para[0] = num;
		return mokaJdbcTemplate.update(sql,para);
	}
	/**
	 * 根据期号获取中奖号码
	 * @return
	 */
	public Map<String,Object> getNumByIssue(String issue) {
		String sql = "select * from t_cgiser_3date where issue=?";
		String[] para = new String[1];
		para[0] = String.valueOf(issue);
		return mokaJdbcTemplate.queryForMap(sql,para);
	}
	public Map<String,Object> getNowThirdNumber() {
		String sql = "select * from t_cgiser_3date where isnow=?";
		String[] para = new String[1];
		para[0] = String.valueOf(1);
		return mokaJdbcTemplate.queryForMap(sql,para);
	}
	/**
	 * 获取上一期的期号
	 * @return
	 */
	public Map<String,Object> getThirdDateIssueByStateIsNow() {
		String sql = "select * from t_cgiser_3date where isnow=0 and state = 1";
		return mokaJdbcTemplate.queryForMap(sql);
	}
//	public Map<String,Object> getNotReciveSalary() {
//		String sql = "select * from t_cgiser_3date where isnow=?";
//		String[] para = new String[1];
//		para[0] = String.valueOf(0);
//		return mokaJdbcTemplate.queryForMap(sql,para);
//	}
	public JdbcTemplate getMokaJdbcTemplate() {
		return mokaJdbcTemplate;
	}

	public void setMokaJdbcTemplate(JdbcTemplate mokaJdbcTemplate) {
		this.mokaJdbcTemplate = mokaJdbcTemplate;
	}
}

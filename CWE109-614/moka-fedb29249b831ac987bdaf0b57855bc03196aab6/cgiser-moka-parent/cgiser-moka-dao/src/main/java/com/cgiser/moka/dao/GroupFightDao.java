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

public class GroupFightDao {
	private static final Logger logger = LoggerFactory.getLogger(HelpDao.class);
	private JdbcTemplate mokaJdbcTemplate;
	public Long saveGroupFight(String groupFightId,int turn,int turns,String turnInfo1,String turnInfo2,String turnInfo3
			,String turnInfo4,String turnInfo5,String turnInfo6,Long roomId,Long roleId,int state){
		Long gfId =0L;
		final String insertSql = "insert into t_cgiser_group_fight(groupfightid,turn,turns,turninfo1,turninfo2,turninfo3,turninfo4,turninfo5,turninfo6" +
				",roomid,roleid,state)values('"+groupFightId+"',"+turn+","+turns+",'"+turnInfo1+"','"+turnInfo2+"','"+turnInfo3+"','"+turnInfo4+"','"
				+turnInfo5+"','"+turnInfo6+"',"+roomId+","+roleId+","+state+")";
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
        mokaJdbcTemplate.update(new PreparedStatementCreator() {

            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(insertSql);
                return ps;
            }

        }, keyHolder);
        try {
        	gfId = keyHolder.getKey().longValue();
        } catch (Exception e) {
        	gfId = 0L;
        }
        
		return gfId;
		
	}
	public List<Map<String,Object>> getGroupFightById(Long roomId){
		String sql = "select * From t_cgiser_group_fight where roomId = ?";
		String[] para = new String[1];
		para[0] = String.valueOf(roomId);
        return  mokaJdbcTemplate.queryForList(sql,para);
	}
	public Map<String,Object> getGroupFightByRoomIdTurn(Long roomId,int turn){
		String sql = "select * From t_cgiser_group_fight where roomId = ? and turn = ?";
		String[] para = new String[2];
		para[0] = String.valueOf(roomId);
		para[1] = String.valueOf(turn);
        return  mokaJdbcTemplate.queryForMap(sql,para);
	}
	public JdbcTemplate getMokaJdbcTemplate() {
		return mokaJdbcTemplate;
	}
	public void setMokaJdbcTemplate(JdbcTemplate mokaJdbcTemplate) {
		this.mokaJdbcTemplate = mokaJdbcTemplate;
	}
}

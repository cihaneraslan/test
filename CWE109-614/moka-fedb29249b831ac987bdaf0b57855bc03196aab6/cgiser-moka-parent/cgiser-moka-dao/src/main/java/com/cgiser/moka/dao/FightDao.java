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
import org.springframework.util.CollectionUtils;

public class FightDao {
	private static final Logger logger = LoggerFactory.getLogger(FragmentDao.class);
	private JdbcTemplate mokaJdbcTemplate;

	public Long addFightInfo(String fightId, Long aRoleId, Long dRoleId,String fightInfo,
			int type,int state) {
		Map<String, Object> map=this.getFightInfoByFightId(fightId);
		if(CollectionUtils.isEmpty(map)){
			Long id = new Long(0);
			// String sql =
			// "select USERID,USERIDEN,USERNAME,CREATETIME,UDID,ISACTIVE,STATE from t_user_user where STATE = 1 and username=?";
			final String insertSql = "insert into t_cgiser_fight (fightid,attackroleid,defendroleid,fightinfo,fighttime,type,state)values('"
					+ fightId
					+ "',"
					+ aRoleId
					+ ","
					+ dRoleId
					+ ",'"
					+ fightInfo
					+ "','"
					+ new Timestamp(new Date().getTime())
					+ "',"
					+ type
					+ ","
					+ state
					+ ")";


			KeyHolder keyHolder = new GeneratedKeyHolder();
			mokaJdbcTemplate.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection con)
						throws SQLException {
					PreparedStatement ps = con.prepareStatement(insertSql
							.toString());
					return ps;
				}

			}, keyHolder);
			try {
				id = keyHolder.getKey().longValue();
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				id = 0L;
			}
			return id;
		}else{
			return new Long(map.get("ID").toString());
		}
	}
	public Map<String,Object> getFightInfoByFightId(String fightId) {
		String sql = "select * from t_cgiser_fight where fightid=?";
		String[] para = new String[1];
		para[0] = String.valueOf(fightId);
		return mokaJdbcTemplate.queryForMap(sql, para);
	}

	public JdbcTemplate getMokaJdbcTemplate() {
		return mokaJdbcTemplate;
	}
	public void setMokaJdbcTemplate(JdbcTemplate mokaJdbcTemplate) {
		this.mokaJdbcTemplate = mokaJdbcTemplate;
	}
}

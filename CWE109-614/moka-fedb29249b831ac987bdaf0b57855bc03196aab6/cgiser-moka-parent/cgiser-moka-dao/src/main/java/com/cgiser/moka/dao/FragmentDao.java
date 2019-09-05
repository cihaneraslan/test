package com.cgiser.moka.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.CollectionUtils;

public class FragmentDao {
	private static final Logger logger = LoggerFactory.getLogger(FragmentDao.class);
	private JdbcTemplate mokaJdbcTemplate;

	public Long addFragment(Long roleId, int fragment1, int fragment2,
			int fragment3) {
		Map<String, Object> map=this.getFragmentByRoleId(roleId);
		if(CollectionUtils.isEmpty(map)){
			Long id = new Long(0);
			// String sql =
			// "select USERID,USERIDEN,USERNAME,CREATETIME,UDID,ISACTIVE,STATE from t_user_user where STATE = 1 and username=?";
			final String insertSql = "insert into t_cgiser_fragment (fragment1,fragment2,fragment3,roleid)values("
					+ fragment1
					+ ","
					+ fragment2
					+ ","
					+ fragment3
					+ ","
					+ roleId
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
			String sql = "update t_cgiser_fragment set fragment1=fragment1+"+fragment1+",fragment2=fragment2+"+fragment2+",fragment3=fragment3+"+fragment3+" where roleid=?";
			String[] para = new String[1];
			para[0] = String.valueOf(roleId);
			return new Long(mokaJdbcTemplate.update(sql,para));
		}
	}
	public Map<String,Object> getFragmentByRoleId(Long roleId) {
		String sql = "select * from t_cgiser_fragment where roleid=?";
		String[] para = new String[1];
		para[0] = String.valueOf(roleId);
		return mokaJdbcTemplate.queryForMap(sql, para);
	}

	public int delFragment(Long roleId, int fragment1, int fragment2,
			int fragment3) {
		String sql = "update t_cgiser_fragment set fragment1=fragment1-"+fragment1+",fragment2=fragment2-"+fragment2+",fragment3=fragment3-"+fragment3+" where roleid=?";
		String[] para = new String[1];
		para[0] = String.valueOf(roleId);
		return mokaJdbcTemplate.update(sql,para);
	}
	public JdbcTemplate getMokaJdbcTemplate() {
		return mokaJdbcTemplate;
	}
	public void setMokaJdbcTemplate(JdbcTemplate mokaJdbcTemplate) {
		this.mokaJdbcTemplate = mokaJdbcTemplate;
	}
}

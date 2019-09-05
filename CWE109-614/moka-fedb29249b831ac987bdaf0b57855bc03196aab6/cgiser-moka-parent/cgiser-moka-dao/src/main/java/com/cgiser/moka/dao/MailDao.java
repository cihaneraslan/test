package com.cgiser.moka.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;


public class MailDao {
	private static final Logger logger = LoggerFactory.getLogger(MapDao.class);
	private JdbcTemplate mokaJdbcTemplate;

	public List<Map<String, Object>> getEmailsByFromRoleId(Long roleId,
			int status) {
		List list = new ArrayList();
		Calendar curDate = Calendar.getInstance();
		curDate.add(Calendar.DATE, -7);
		String sql = "select * From t_cgiser_mail where fromrole="+roleId+" and status="+status +" and addtime>'"+new Timestamp(curDate.getTime().getTime())+"'";
		list = mokaJdbcTemplate.queryForList(sql);
		return list;
	}
	/**
	 * 获取系统邮件
	 * @param roleId
	 * @param status
	 * @return
	 */
	public List<Map<String, Object>> getSysEmails() {
		Timestamp curDate = new Timestamp(System.currentTimeMillis());
		String sql = "select * From t_cgiser_sysmail where state=1 and start<='"+curDate+"' and end >='"+curDate+"'";
		return mokaJdbcTemplate.queryForList(sql);
	}
	/**
	 * 获取未删除邮件
	 * @param roleId
	 * @param status
	 * @return
	 */
	public List<Map<String, Object>> getEmailsByToRoleId(Long roleId, int status) {
		List list = new ArrayList();
		Calendar curDate = Calendar.getInstance();
		curDate.add(Calendar.DATE, -7);
		String sql = "select * From t_cgiser_mail where torole="+roleId+" and status !="+status +" and addtime>'"+new Timestamp(curDate.getTime().getTime())+"'";
		list = mokaJdbcTemplate.queryForList(sql);
		return list;
	}
	public int getEmailCountByToRoleIdStatus(Long roleId, int status) {
		List list = new ArrayList();
		Calendar curDate = Calendar.getInstance();
		curDate.add(Calendar.DATE, -7);
		String sql = "select count(*) From t_cgiser_mail where torole="+roleId+" and status="+status +" and addtime>'"+new Timestamp(curDate.getTime().getTime())+"'";
		return mokaJdbcTemplate.queryForInt(sql);
	}
	/**
	 * 更新邮件状态
	 * 
	 * @param emailId
	 * @param status
	 *            0:未读;1:已读;2:删除;
	 * @return
	 */
	public int updateEmailStatus(Long emailId, int state) {
		String sql = "update t_cgiser_mail set status = ? where emailid=?";
		String[] para = new String[2];
		para[0] = String.valueOf(state);
		para[1] = String.valueOf(emailId);
		return mokaJdbcTemplate.update(sql, para);
	}

	public Long saveEmail(String title, String content, Long from, Long to,
			int type) {
		Long id = new Long(0);
		// String sql =
		// "select USERID,USERIDEN,USERNAME,CREATETIME,UDID,ISACTIVE,STATE from t_user_user where STATE = 1 and username=?";
		final String insertSql = "insert into t_cgiser_mail (title,content,fromrole,torole,addtime,type)values('"
				+ title
				+ "','"
				+ content
				+ "',"
				+ from
				+ ","
				+ to
				+ ",'"
				+ new java.sql.Timestamp(System.currentTimeMillis())
				+ "',"
				+ type
				+ ")";

		long emailid;

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
			emailid = keyHolder.getKey().longValue();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			emailid = 0;
		}
		if (emailid > 0) {
			id = new Long(emailid);
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

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

public class WDJProductLogDao {
	private static final Logger logger = LoggerFactory.getLogger(MapDao.class);
	private JdbcTemplate mokaJdbcTemplate;
	public Long infoWdjProductLog(Long roleId,String productId,String cooOrderSerial,String info,int goodsCount,int status,int payment) {
		Long id = new Long(0);
		// String sql =
		// "select USERID,USERIDEN,USERNAME,CREATETIME,UDID,ISACTIVE,STATE from t_user_user where STATE = 1 and username=?";
		final String insertSql = "insert into t_cgiser_product_wdjlog (roleid,productid,cooOrderSerial,storeinfo,goodsCount,payment,status,storetime)values("
				+ roleId
				+ ",'"
				+ productId
				+ "','"
				+ cooOrderSerial
				+ "','"
				+ info
				+ "',"
				+ goodsCount
				+ ","
				+ 0
				+ ","
				+ status
				+ ",'"
				+ new java.sql.Timestamp(System.currentTimeMillis())
				+ "')";

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
	public Map<String, Object> getWdjProductLogByCooOrderSerial(String cooOrderSerial) {
		String sql = "select * From t_cgiser_product_wdjlog where cooOrderSerial=? and status=0";
		String[] para = new String[1];
		para[0] = cooOrderSerial;
		return mokaJdbcTemplate.queryForMap(sql,para);
	}
	public List<Map<String, Object>> getWdjProductLogByRoleIdPayment(Long roleId,int payment) {
		String sql = "select * From t_cgiser_product_wdjlog where roleid=? and payment=?";
		String[] para = new String[2];
		para[0] = String.valueOf(roleId);
		para[1] = String.valueOf(payment);
		return mokaJdbcTemplate.queryForList(sql,para);
	}
	public int updateWdjProductLogPayment(String cooOrderSerial,String info,int goodsCount,int payment) {
		String sql = "update t_cgiser_product_wdjlog set payment=?,storeinfo = ?,goodscount =?  where cooOrderSerial=?";
		String[] para = new String[4];
		para[0] = String.valueOf(payment);
		para[1] = String.valueOf(info);
		para[2] = String.valueOf(goodsCount);
		para[3] = String.valueOf(cooOrderSerial);
		return mokaJdbcTemplate.update(sql,para);
	}
	public int updateWdjProductLogStatus(String cooOrderSerial,int status) {
		String sql = "update t_cgiser_product_wdjlog set status=?  where cooOrderSerial=?";
		String[] para = new String[2];
		para[0] = String.valueOf(status);
		para[1] = String.valueOf(cooOrderSerial);
		return mokaJdbcTemplate.update(sql,para);
	}
	public JdbcTemplate getMokaJdbcTemplate() {
		return mokaJdbcTemplate;
	}
	public void setMokaJdbcTemplate(JdbcTemplate mokaJdbcTemplate) {
		this.mokaJdbcTemplate = mokaJdbcTemplate;
	}
}

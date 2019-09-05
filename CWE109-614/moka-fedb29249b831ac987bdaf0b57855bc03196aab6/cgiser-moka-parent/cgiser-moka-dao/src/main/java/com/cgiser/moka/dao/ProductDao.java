package com.cgiser.moka.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class ProductDao {
	private static final Logger logger = LoggerFactory.getLogger(MapDao.class);
	private JdbcTemplate mokaJdbcTemplate;

	public List<Map<String, Object>> getProducts() {
		String sql = "select * From t_cgiser_product where status=1";
		return mokaJdbcTemplate.queryForList(sql);
	}
	public List<Map<String, Object>> getProductsByType(int type) {
		String sql = "select * From t_cgiser_product where status=1 and type ="+type;
		return mokaJdbcTemplate.queryForList(sql);
	}
	public Map<String, Object> getProductById(String id) {
		String sql = "select * From t_cgiser_product where identifier=? and status=1";
		String[] para = new String[1];
		para[0] = id;
		return mokaJdbcTemplate.queryForMap(sql,para);
	}
	public Long infoStoreProduct(Long roleId, String productId,String transactionId, String info,String receipt,
			int status,int payment) {
		Long id = new Long(0);
		// String sql =
		// "select USERID,USERIDEN,USERNAME,CREATETIME,UDID,ISACTIVE,STATE from t_user_user where STATE = 1 and username=?";
		final String insertSql = "insert into t_cgiser_product_log (roleid,productid,storeinfo,receipt,payment,status,storetime,TRANSACTIONID)values("
				+ roleId
				+ ",'"
				+ productId
				+ "','"
				+ info
				+ "','"
				+ receipt
				+ "',"
				+ payment
				+ ","
				+ status
				+ ",'"
				+ new java.sql.Timestamp(System.currentTimeMillis())
				+ "','"
				+ transactionId
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
	public Long info91StoreProduct(Long roleId, String productId,
			String cooOrderSerial,String consumeStreamId ,String info, int goodsCount, int status,
			int payment) {
		Long id = new Long(0);
		// String sql =
		// "select USERID,USERIDEN,USERNAME,CREATETIME,UDID,ISACTIVE,STATE from t_user_user where STATE = 1 and username=?";
		final String insertSql = "insert into t_cgiser_product_91log (roleid,productid,cooOrderSerial,consumeStreamId,storeinfo,goodsCount,payment,status,storetime)values("
				+ roleId
				+ ",'"
				+ productId
				+ "','"
				+ cooOrderSerial
				+ "','"
				+ consumeStreamId
				+ "','"
				+ info
				+ "',"
				+ goodsCount
				+ ","
				+ payment
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
	public Map<String,Object> getProductLogByTransactionId(String transactionId) {
		String sql = "select * From t_cgiser_product_log where transactionid=? and status=0";
		String[] para = new String[1];
		para[0] = transactionId;
		return mokaJdbcTemplate.queryForMap(sql,para);
	}
	public Map<String,Object> getProductLogByStoreId(Long storeId) {
		String sql = "select * From t_cgiser_product_log where storeId=?";
		String[] para = new String[1];
		para[0] = String.valueOf(storeId);
		return mokaJdbcTemplate.queryForMap(sql,para);
	}
	public List<Map<String,Object>> getProductLogByRoleId(Long roleId,int payment) {
		String sql = "select * From t_cgiser_product_log where payment=? and status=0 and roleId = ?";
		String[] para = new String[2];
		para[0] = String.valueOf(payment);
		para[1] = String.valueOf(roleId);
		return mokaJdbcTemplate.queryForList(sql,para);
	}
	public List<Map<String,Object>> getAllProductLogByRoleId(int type,Long roleId,int a,int b) {
		Calendar curDate1 = Calendar.getInstance();
		curDate1.set(Calendar.HOUR_OF_DAY, 0); 
		curDate1.set(Calendar.SECOND, 0); 
		curDate1.set(Calendar.MINUTE, 0); 
		curDate1.set(Calendar.MILLISECOND, 0); 
		curDate1.add(Calendar.DATE, a);
		Calendar curDate2 = Calendar.getInstance();
		curDate2.set(Calendar.HOUR_OF_DAY, 0); 
		curDate2.set(Calendar.SECOND, 0); 
		curDate2.set(Calendar.MINUTE, 0); 
		curDate2.set(Calendar.MILLISECOND, 0); 
		curDate2.add(Calendar.DATE, b);
		String sql = "select * From t_cgiser_product_alllog where state=1 and roleId = ? and storetime >='"
			+new Timestamp(curDate1.getTime().getTime())+"' and storetime <'"+new Timestamp(curDate2.getTime().getTime())+"'";
		if(type>0){
			sql = sql+" and type = "+type;
		}
		
		String[] para = new String[1];
		para[0] = String.valueOf(roleId);
		return mokaJdbcTemplate.queryForList(sql,para);
	}
	public List<Map<String,Object>> getAllProductLog(int type,int a,int b) {
		Calendar curDate1 = Calendar.getInstance();
		curDate1.set(Calendar.HOUR_OF_DAY, 0); 
		curDate1.set(Calendar.SECOND, 0); 
		curDate1.set(Calendar.MINUTE, 0); 
		curDate1.set(Calendar.MILLISECOND, 0); 
		curDate1.add(Calendar.DATE, a);
		Calendar curDate2 = Calendar.getInstance();
		curDate2.set(Calendar.HOUR_OF_DAY, 0); 
		curDate2.set(Calendar.SECOND, 0); 
		curDate2.set(Calendar.MINUTE, 0); 
		curDate2.set(Calendar.MILLISECOND, 0); 
		curDate2.add(Calendar.DATE, b);
		String sql = "select * From t_cgiser_product_alllog where state=1 and storetime >='"
			+new Timestamp(curDate1.getTime().getTime())+"' and storetime <'"+new Timestamp(curDate2.getTime().getTime())+"'";
		if(type>0){
			sql = sql+" and type = "+type;
		}
		return mokaJdbcTemplate.queryForList(sql);
	}
	public Map<String,Object> getNineOneProductLogByCooOrderSerial(
			String cooOrderSerial) {
		String sql = "select * From t_cgiser_product_91log where cooOrderSerial=?";
		String[] para = new String[1];
		para[0] = String.valueOf(cooOrderSerial);
		return mokaJdbcTemplate.queryForMap(sql,para);
	}
	public List<Map<String,Object>> getNineOneProductLogByStatus(
			int status,Long roleId) {
		String sql = "select * From t_cgiser_product_91log where status=? and roleId= ?";
		String[] para = new String[2];
		para[0] = String.valueOf(status);
		para[1] = String.valueOf(roleId);
		return mokaJdbcTemplate.queryForList(sql,para);
	}

	public int update91StoreProduct(String productId, String consumeStreamId,
			String info, int goodsCount, int payment, String cooOrderSerial) {
		String sql = "update t_cgiser_product_91log set payment=1,productid = ?,consumeStreamId=?,storeinfo = ?,goodscount =?  where cooOrderSerial=?";
		String[] para = new String[5];
		para[0] = String.valueOf(productId);
		para[1] = String.valueOf(consumeStreamId);
		para[2] = String.valueOf(info);
		para[3] = String.valueOf(goodsCount);
		para[4] = String.valueOf(cooOrderSerial);
		return mokaJdbcTemplate.update(sql,para);
	}
	public int update91StoreProductStatus(String cooOrderSerial, int status) {
		String sql = "update t_cgiser_product_91log set status = ? where cooOrderSerial=?";
		String[] para = new String[2];
		para[0] = String.valueOf(status);
		para[1] = String.valueOf(cooOrderSerial);
		return mokaJdbcTemplate.update(sql,para);
	}
	public int updatePaymentByStoreId(Long storeId) {
		String sql = "update t_cgiser_product_log set payment=1  where storeid=? and status=0";
		String[] para = new String[1];
		para[0] = String.valueOf(storeId);
		return mokaJdbcTemplate.update(sql,para);
	}
	/**
	 * 记录用户的所有充值记录
	 * @param roleId
	 * @param productId
	 * @param COOORDERSERIAL 订单号
	 * @param type
	 * @return
	 */
	public Long infoProductAllLog(Long roleId, String productId,String COOORDERSERIAL,int type) {
		Long id = new Long(0);
		// String sql =
		// "select USERID,USERIDEN,USERNAME,CREATETIME,UDID,ISACTIVE,STATE from t_user_user where STATE = 1 and username=?";
		final String insertSql = "insert into t_cgiser_product_alllog (roleid,productid,COOORDERSERIAL,storetime,type,state)values("
				+ roleId
				+ ",'"
				+ productId
				+ "','"
				+ COOORDERSERIAL
				+ "','"
				+ new java.sql.Timestamp(System.currentTimeMillis())
				+ "',"
				+ type
				+ ","
				+ 1
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

package com.cgiser.moka.manager.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import com.cgiser.moka.dao.WDJProductLogDao;
import com.cgiser.moka.manager.ProductManager;
import com.cgiser.moka.manager.WDJProductLogManager;
import com.cgiser.moka.model.WdjProductLog;

public class WDJProductLogManagerImpl implements WDJProductLogManager {
	private WDJProductLogDao wdjProductLogDao;
	private ProductManager productManager;
	@Override
	public Long infoWdjProductLog(Long roleId, String productId,
			String cooOrderSerial, String info,
			int goodsCount, int status, int payment) {
		// TODO Auto-generated method stub
		return wdjProductLogDao.infoWdjProductLog(roleId, productId, cooOrderSerial, info, goodsCount, status, payment);
	}
	public WDJProductLogDao getWdjProductLogDao() {
		return wdjProductLogDao;
	}
	public void setWdjProductLogDao(WDJProductLogDao wdjProductLogDao) {
		this.wdjProductLogDao = wdjProductLogDao;
	}
	@Override
	public WdjProductLog getWdjProductLogByCooOrderSerial(String cooOrderSerial) {
		// TODO Auto-generated method stub
		return MapToWdjProductLog(wdjProductLogDao.getWdjProductLogByCooOrderSerial(cooOrderSerial));
	}
	private WdjProductLog MapToWdjProductLog(Map<String, Object> map) {
		if (CollectionUtils.isEmpty(map)) {
			return null;
		}
		WdjProductLog productLog = new WdjProductLog();
		productLog.setPayment(Integer.parseInt((String) map.get("PAYMENT")));
		productLog.setProductId((String) map.get("PRODUCTID"));
		productLog.setRoleId(new Long((String) map.get("ROLEID")));
		productLog.setStatus(Integer.parseInt((String) map.get("STATUS")));
		productLog.setStoreId(new Long((String) map.get("STOREID")));
		productLog.setStoreInfo((String) map.get("STOREINFO"));
		productLog.setStoreTime(new Date(((Timestamp) map.get("STORETIME"))
				.getTime()));
		productLog.setCooOrderSerial(map.get("COOORDERSERIAL").toString());
		productLog.setGoodsCount(Integer.parseInt(map.get("GOODSCOUNT")
				.toString()));
		return productLog;
	}
	private List<WdjProductLog> MapListToWdjProductLogList(
			List<Map<String, Object>> list) {
		if (CollectionUtils.isEmpty(list)) {
			return null;
		}
		List<WdjProductLog> wdjProductLogs = new ArrayList<WdjProductLog>();
		for (Map<String, Object> map : list) {
			wdjProductLogs.add(MapToWdjProductLog(map));
		}
		return wdjProductLogs;
	}
	@Override
	public int updateWdjProductLogPayment(String cooOrderSerial, String info,
			int goodsCount, int payment) {
		// TODO Auto-generated method stub
		return wdjProductLogDao.updateWdjProductLogPayment(cooOrderSerial, info, goodsCount, payment);
	}
	@Override
	public int updateWdjProductLogStatus(String cooOrderSerial, int status) {
		WdjProductLog wdjProductLog = this.getWdjProductLogByCooOrderSerial(cooOrderSerial);
		int result = wdjProductLogDao.updateWdjProductLogStatus(cooOrderSerial, status);
		if(result>0&&status==1){
			productManager.infoAllProductLog(wdjProductLog.getRoleId(), wdjProductLog.getProductId(), cooOrderSerial, 3);
		}
		return result;
	}
	public ProductManager getProductManager() {
		return productManager;
	}
	public void setProductManager(ProductManager productManager) {
		this.productManager = productManager;
	}
	@Override
	public List<WdjProductLog> getWdjProductLogByRoleId(Long roleId, int payment) {
		// TODO Auto-generated method stub
		return MapListToWdjProductLogList(wdjProductLogDao.getWdjProductLogByRoleIdPayment(roleId, payment));
	}
}

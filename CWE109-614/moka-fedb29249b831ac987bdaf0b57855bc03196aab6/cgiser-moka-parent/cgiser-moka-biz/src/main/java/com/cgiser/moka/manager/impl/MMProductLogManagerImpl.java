package com.cgiser.moka.manager.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import com.cgiser.moka.dao.MMProductLogDao;
import com.cgiser.moka.manager.MMProductLogManager;
import com.cgiser.moka.manager.ProductManager;
import com.cgiser.moka.model.MMProductLog;

public class MMProductLogManagerImpl implements MMProductLogManager {
	private MMProductLogDao mmProductLogDao;
	private ProductManager productManager;
	@Override
	public Long infoMMProductLog(Long roleId, String productId,
			String cooOrderSerial, String info,
			int goodsCount, int status, int payment) {
		// TODO Auto-generated method stub
		return mmProductLogDao.infoMMProductLog(roleId, productId, cooOrderSerial, info, goodsCount, status, payment);
	}
	public MMProductLogDao getMMProductLogDao() {
		return mmProductLogDao;
	}
	public void setMMProductLogDao(MMProductLogDao MMProductLogDao) {
		this.mmProductLogDao = mmProductLogDao;
	}
	@Override
	public MMProductLog getMMProductLogByCooOrderSerial(String cooOrderSerial) {
		// TODO Auto-generated method stub
		return MapToMMProductLog(mmProductLogDao.getMMProductLogByCooOrderSerial(cooOrderSerial));
	}
	private MMProductLog MapToMMProductLog(Map<String, Object> map) {
		if (CollectionUtils.isEmpty(map)) {
			return null;
		}
		MMProductLog productLog = new MMProductLog();
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
	private List<MMProductLog> MapListToMMProductLogList(
			List<Map<String, Object>> list) {
		if (CollectionUtils.isEmpty(list)) {
			return null;
		}
		List<MMProductLog> MMProductLogs = new ArrayList<MMProductLog>();
		for (Map<String, Object> map : list) {
			MMProductLogs.add(MapToMMProductLog(map));
		}
		return MMProductLogs;
	}
	@Override
	public int updateMMProductLogPayment(String cooOrderSerial, String info,
			int goodsCount, int payment) {
		// TODO Auto-generated method stub
		return mmProductLogDao.updateMMProductLogPayment(cooOrderSerial, info, goodsCount, payment);
	}
	@Override
	public int updateMMProductLogStatus(String cooOrderSerial, int status) {
		MMProductLog MMProductLog = this.getMMProductLogByCooOrderSerial(cooOrderSerial);
		int result = mmProductLogDao.updateMMProductLogStatus(cooOrderSerial, status);
		if(result>0&&status==1){
			productManager.infoAllProductLog(MMProductLog.getRoleId(), MMProductLog.getProductId(), cooOrderSerial, 3);
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
	public List<MMProductLog> getMMProductLogByRoleId(Long roleId, int payment) {
		// TODO Auto-generated method stub
		return MapListToMMProductLogList(mmProductLogDao.getMMProductLogByRoleIdPayment(roleId, payment));
	}
}

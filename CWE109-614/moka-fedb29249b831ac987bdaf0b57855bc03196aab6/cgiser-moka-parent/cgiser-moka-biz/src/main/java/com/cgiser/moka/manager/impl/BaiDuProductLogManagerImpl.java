package com.cgiser.moka.manager.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import com.cgiser.moka.dao.BaiDuProductLogDao;
import com.cgiser.moka.manager.BaiDuProductLogManager;
import com.cgiser.moka.model.BaiDuProductLog;

public class BaiDuProductLogManagerImpl implements BaiDuProductLogManager {

	BaiDuProductLogDao baiDuProductLogDao;
	@Override
	public BaiDuProductLog getBaiDuProductLogByCooOrderSerial(
			String cooOrderSerial) {
		// TODO Auto-generated method stub
		return MapToBaiDuProductLog(baiDuProductLogDao.getBaiDuProductLogByCooOrderSerial(cooOrderSerial));
	}

	@Override
	public List<BaiDuProductLog> getBaiDuProductLogByRoleId(Long roleId,
			int payment) {
		// TODO Auto-generated method stub
		return MapListToBaiDuProductLogList(baiDuProductLogDao.getBaiDuProductLogByRoleIdPayment(roleId, payment));
	}

	@Override
	public Long infoBaiDuProductLog(Long roleId, String productId,
			String cooOrderSerial, String info, int goodsCount, int status,
			int payment) {
		// TODO Auto-generated method stub
		return baiDuProductLogDao.infoBaiDuProductLog(roleId, productId, cooOrderSerial, info, goodsCount, status, payment);
	}

	@Override
	public int updateBaiDuProductLogPayment(String cooOrderSerial, String info,
			int goodsCount, int payment) {
		// TODO Auto-generated method stub
		return baiDuProductLogDao.updateBaiDuProductLogPayment(cooOrderSerial, info, goodsCount, payment);
	}

	@Override
	public int updateBaiDuProductLogStatus(String cooOrderSerial, int status) {
		// TODO Auto-generated method stub
		return baiDuProductLogDao.updateBaiDuProductLogStatus(cooOrderSerial, status);
	}
	private BaiDuProductLog MapToBaiDuProductLog(Map<String, Object> map) {
		if (CollectionUtils.isEmpty(map)) {
			return null;
		}
		BaiDuProductLog productLog = new BaiDuProductLog();
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
	private List<BaiDuProductLog> MapListToBaiDuProductLogList(
			List<Map<String, Object>> list) {
		if (CollectionUtils.isEmpty(list)) {
			return null;
		}
		List<BaiDuProductLog> ProductLogs = new ArrayList<BaiDuProductLog>();
		for (Map<String, Object> map : list) {
			ProductLogs.add(MapToBaiDuProductLog(map));
		}
		return ProductLogs;
	}

	public BaiDuProductLogDao getBaiDuProductLogDao() {
		return baiDuProductLogDao;
	}

	public void setBaiDuProductLogDao(BaiDuProductLogDao baiDuProductLogDao) {
		this.baiDuProductLogDao = baiDuProductLogDao;
	}
}

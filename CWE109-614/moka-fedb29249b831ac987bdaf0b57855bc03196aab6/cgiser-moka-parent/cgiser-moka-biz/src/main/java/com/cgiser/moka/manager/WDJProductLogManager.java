package com.cgiser.moka.manager;

import java.util.List;

import com.cgiser.moka.model.WdjProductLog;

public interface WDJProductLogManager {
	/**
	 * 记录豌豆荚订单号
	 * @param roleId
	 * @param productId 产品ID
	 * @param cooOrderSerial 产品订单号
	 * @param info 产品购买信息
	 * @param goodsCount
	 * @param status
	 * @param payment
	 * @return
	 */
	public Long infoWdjProductLog(Long roleId,String productId,String cooOrderSerial,String info,int goodsCount,int status,int payment);
	/**
	 * 根据角色获取获取订单
	 * @param cooOrderSerial
	 * @return
	 */
	public List<WdjProductLog> getWdjProductLogByRoleId(Long roleId,int payment);
	/**
	 * 根据订单号
	 * @param cooOrderSerial
	 * @return
	 */
	public WdjProductLog getWdjProductLogByCooOrderSerial(String cooOrderSerial);
	/**
	 * 修改订单的支付状态
	 * @param cooOrderSerial
	 * @param payment
	 * @return
	 */
	public int updateWdjProductLogPayment(String cooOrderSerial,String info,int goodsCount,int payment);
	/**
	 * 修改订单的发放状态
	 * @param cooOrderSerial
	 * @param payment
	 * @return
	 */
	public int updateWdjProductLogStatus(String cooOrderSerial,int status);
}

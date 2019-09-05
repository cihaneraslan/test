package com.cgiser.moka.manager;

import java.util.List;

import com.cgiser.moka.model.AllProductLog;
import com.cgiser.moka.model.NineOneProductLog;
import com.cgiser.moka.model.Product;
import com.cgiser.moka.model.ProductLog;

public interface ProductManager {
	/**
	 * 获取所有的商品
	 * @return
	 */
	public List<Product> getProducts();
	/**
	 * 根据类型获取所有的商品
	 * @return
	 */
	public List<Product> getProductsByType(int type);
	/**
	 * 根据产品ID获取产品
	 * @param id
	 * @return
	 */
	public Product getProductById(String id);
	/**
	 * 记录91用户充值记录
	 * @param roleId
	 * @param productId
	 * @param cooOrderSerial 商品订单号
	 * @param consumeStreamId 平台消费流水号
	 * @param info
	 * @param status
	 * 
	 * @return
	 */
	public Long info91StoreProduct(Long roleId,String productId,String cooOrderSerial,String consumeStreamId,String info,int goodsCount,int status,int payment);
	/**
	 * 修改91用户充值记录
	 * @param productId
	 * @param cooOrderSerial 商品订单号
	 * @param consumeStreamId 平台消费流水号
	 * @param info
	 * @param status
	 * 
	 * @return
	 */
	public int update91StoreProduct(String productId,String consumeStreamId,String info,int goodsCount,int payment,String cooOrderSerial);
	/**
	 * 修改91用户充值记录
	 * @param productId
	 * @param cooOrderSerial 商品订单号
	 * @param consumeStreamId 平台消费流水号
	 * @param info
	 * @param status
	 * 
	 * @return
	 */
	public int update91StoreProductStatus(String cooOrderSerial,int status);
	/**
	 * 记录用户充值记录
	 * @param roleId
	 * @param productId
	 * @param info
	 * @param status
	 * 21000	App Store不能读取你提供的JSON对象
	   21002	receipt-data域的数据有问题
	   21003	receipt无法通过验证
	   21004	提供的shared secret不匹配你账号中的shared secret
	   21005	receipt服务器当前不可用
       21006	receipt合法，但是订阅已过期。服务器接收到这个状态码时，receipt数据仍然会解码并一起发送
	   21007	receipt是Sandbox receipt，但却发送至生产系统的验证服务
	   21008	receipt是生产receipt，但却发送至Sandbox环境的验证服务
	 * @return
	 */
	public Long infoStoreProduct(Long roleId,String productId,String transactionId,String info,String receipt,int status,int payment);
	/**
	 * 获取充值记录
	 * @param transactionId
	 * @return
	 */
	public ProductLog getProductLogByTransactionId(String transactionId);
	/**
	 * 修改充值记录为成功
	 * @param storeId
	 * @return
	 */
	public int updatePaymentByStoreId(Long storeId);
	/**
	 * 获取充值成功的充值记录
	 * @param roleId
	 * @return
	 */
	public List<ProductLog> getProductLogByRoleId(Long roleId);
	/**
	 * 根据订单号获取91充值记录
	 * @param cooOrderSerial
	 * @return
	 */
	public NineOneProductLog getNineOneProductLogByCooOrderSerial(String cooOrderSerial);
	/**
	 * 根据支付状态获取91充值记录
	 * @param cooOrderSerial
	 * @return
	 */
	public List<NineOneProductLog> getNineOneProductLogByStatus(int status,Long roleId);
	/**
	 * IOS支付验证
	 * @param receipt
	 * @return
	 */
	public String verifyReceipt(String receipt);
	/**
	 * IOS沙箱支付验证
	 * @param receipt
	 * @return
	 */
	public String verifyReceiptForSandBox(String receipt);
	/**
	 * 获取充值记录（管理账户）计算a与b的差值记录
	 * @param type 充值用户类型
	 * @param roleId 充值用户的角色ID
	 * @param a 比当前时间晚几天
	 * @param b 比当前时间晚几天
	 * @return
	 */
	public List<AllProductLog> getAllProductLogByRoleId(int type,Long roleId,int a,int b);
	/**
	 * 获取充值记录（管理账户）计算a与b的差值记录
	 * @param type 充值用户类型
	 * @param a 比当前时间晚几天
	 * @param b 比当前时间晚几天
	 * @return
	 */
	public List<AllProductLog> getAllProductLog(int type,int a,int b);
	/**
	 * 记录总的用户充值记录
	 * @param roleId
	 * @param productId
	 * @param cooOrderSerial
	 * @param type
	 * @return
	 */
	public Long infoAllProductLog(Long roleId,String productId,String cooOrderSerial,int type);
	/**
	 * 获取充值记录根据StoreId
	 * @param storeId
	 * @return
	 */
	public ProductLog getProductLogByStoreId(Long storeId);
}

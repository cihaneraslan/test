package com.cgiser.moka.manager.impl;

import java.io.PrintStream;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.springframework.util.CollectionUtils;

import com.cgiser.moka.dao.ProductDao;
import com.cgiser.moka.manager.ProductManager;
import com.cgiser.moka.model.AllProductLog;
import com.cgiser.moka.model.NineOneProductLog;
import com.cgiser.moka.model.Product;
import com.cgiser.moka.model.ProductLog;

public class ProductManagerImpl implements ProductManager {
	private ProductDao productDao;
	private String payUrl;
	private String sandboxPayUrl = "https://sandbox.itunes.apple.com/verifyReceipt";
	@Override
	public List<Product> getProducts() {
		// TODO Auto-generated method stub
		return MapListToProductList(productDao.getProducts());
	}

	@Override
	public List<Product> getProductsByType(int type) {
		// TODO Auto-generated method stub
		return MapListToProductList(productDao.getProductsByType(type));
	}

	private Product MapToProduct(Map<String, Object> map) {
		if (CollectionUtils.isEmpty(map)) {
			return null;
		}
		Product product = new Product();
		product.setDescription((String) map.get("DESCRIPTION"));
		product.setIdentifier((String) map.get("IDENTIFIER"));
		product.setPrice((String) map.get("PRICE"));
		product.setPriceLocale((String) map.get("PRICELOCALE"));
		product.setTitle((String) map.get("TITLE"));
		product.setValue(Integer.parseInt((String) map.get("VALUE")));
		return product;
	}

	private ProductLog MapToProductLog(Map<String, Object> map) {
		if (CollectionUtils.isEmpty(map)) {
			return null;
		}
		ProductLog productLog = new ProductLog();
		productLog.setPayment(Integer.parseInt((String) map.get("PAYMENT")));
		productLog.setProductId((String) map.get("PRODUCTID"));
		productLog.setReceipt((String) map.get("RECEIPT"));
		productLog.setRoleId(new Long((String) map.get("ROLEID")));
		productLog.setStatus(Integer.parseInt((String) map.get("STATUS")));
		productLog.setStoreId(new Long((String) map.get("STOREID")));
		productLog.setStoreInfo((String) map.get("STOREINFO"));
		productLog.setStoreTime(new Date(((Timestamp) map.get("STORETIME"))
				.getTime()));
		productLog.setTransactionId((String) map.get("TRANSACTIONID"));
		return productLog;
	}

	private NineOneProductLog MapToNineOneProductLog(Map<String, Object> map) {
		if (CollectionUtils.isEmpty(map)) {
			return null;
		}
		NineOneProductLog productLog = new NineOneProductLog();
		productLog.setPayment(Integer.parseInt((String) map.get("PAYMENT")));
		productLog.setProductId((String) map.get("PRODUCTID"));
		productLog.setRoleId(new Long((String) map.get("ROLEID")));
		productLog.setStatus(Integer.parseInt((String) map.get("STATUS")));
		productLog.setStoreId(new Long((String) map.get("STOREID")));
		productLog.setStoreInfo((String) map.get("STOREINFO"));
		productLog.setStoreTime(new Date(((Timestamp) map.get("STORETIME"))
				.getTime()));
		productLog.setConsumeStreamId(map.get("CONSUMESTREAMID").toString());
		productLog.setCooOrderSerial(map.get("COOORDERSERIAL").toString());
		productLog.setGoodsCount(Integer.parseInt(map.get("GOODSCOUNT")
				.toString()));
		return productLog;
	}
	private AllProductLog MapToAllProductLog(Map<String, Object> map) {
		if (CollectionUtils.isEmpty(map)) {
			return null;
		}
		AllProductLog productLog = new AllProductLog();
		productLog.setProductId((String) map.get("PRODUCTID"));
		productLog.setRoleId(new Long((String) map.get("ROLEID")));
		productLog.setStoreId(new Long((String) map.get("STOREID")));
		productLog.setStoreTime(new Date(((Timestamp) map.get("STORETIME"))
				.getTime()));
		productLog.setCooOrderSerial(map.get("COOORDERSERIAL").toString());
		productLog.setState(Integer.parseInt((String) map.get("STATE")));
		productLog.setType(Integer.parseInt((String) map.get("TYPE")));
		return productLog;
	}
	private List<NineOneProductLog> MapListToNineOneProductLogList(
			List<Map<String, Object>> list) {
		if (CollectionUtils.isEmpty(list)) {
			return null;
		}
		List<NineOneProductLog> nineOneProductLogs = new ArrayList<NineOneProductLog>();
		for (Map<String, Object> map : list) {
			nineOneProductLogs.add(MapToNineOneProductLog(map));
		}
		return nineOneProductLogs;
	}

	private List<ProductLog> MapListToProductLogList(
			List<Map<String, Object>> list) {
		if (CollectionUtils.isEmpty(list)) {
			return null;
		}
		List<ProductLog> productLogs = new ArrayList<ProductLog>();
		for (Map<String, Object> map : list) {
			productLogs.add(MapToProductLog(map));
		}
		return productLogs;
	}
	private List<AllProductLog> MapListToAllProductLogList(
			List<Map<String, Object>> list) {
		if (CollectionUtils.isEmpty(list)) {
			return null;
		}
		List<AllProductLog> productLogs = new ArrayList<AllProductLog>();
		for (Map<String, Object> map : list) {
			productLogs.add(MapToAllProductLog(map));
		}
		return productLogs;
	}

	@Override
	public Long infoStoreProduct(Long roleId, String productId,
			String transactionId, String info, String receipt, int status,
			int payment) {
		// TODO Auto-generated method stub
		return productDao.infoStoreProduct(roleId, productId, transactionId,
				info, receipt, status, payment);
	}

	private List<Product> MapListToProductList(List<Map<String, Object>> list) {
		if (CollectionUtils.isEmpty(list)) {
			return null;
		}
		List<Product> products = new ArrayList<Product>();
		for (Map map : list) {
			products.add(MapToProduct(map));
		}
		return products;
	}

	public ProductDao getProductDao() {
		return productDao;
	}

	public void setProductDao(ProductDao productDao) {
		this.productDao = productDao;
	}

	@Override
	public ProductLog getProductLogByTransactionId(String transactionId) {
		// TODO Auto-generated method stub
		return MapToProductLog(productDao
				.getProductLogByTransactionId(transactionId));
	}
	@Override
	public ProductLog getProductLogByStoreId(Long storeId) {
		// TODO Auto-generated method stub
		return MapToProductLog(productDao.getProductLogByStoreId(storeId));
	}
	@Override
	public int updatePaymentByStoreId(Long storeId) {
		// TODO Auto-generated method stub
		ProductLog log = this.getProductLogByStoreId(storeId);
		if(log==null){
			return 0;
		}
		productDao.infoProductAllLog(log.getRoleId(), log.getProductId(), log.getTransactionId(), 2);
		return productDao.updatePaymentByStoreId(storeId);
	}

	@Override
	public Product getProductById(String id) {
		// TODO Auto-generated method stub
		return MapToProduct(productDao.getProductById(id));
	}

	@Override
	public List<ProductLog> getProductLogByRoleId(Long roleId) {
		// TODO Auto-generated method stub
		return MapListToProductLogList(productDao.getProductLogByRoleId(roleId,
				1));
	}

	@Override
	public Long info91StoreProduct(Long roleId, String productId,
			String cooOrderSerial, String consumeStreamId, String info,
			int goodsCount, int status, int payment) {

		return productDao.info91StoreProduct(roleId, productId, cooOrderSerial,
				consumeStreamId, info, goodsCount, status, payment);
	}

	@Override
	public NineOneProductLog getNineOneProductLogByCooOrderSerial(
			String cooOrderSerial) {
		// TODO Auto-generated method stub
		return MapToNineOneProductLog(productDao
				.getNineOneProductLogByCooOrderSerial(cooOrderSerial));
	}

	@Override
	public int update91StoreProduct(String productId, String consumeStreamId,
			String info, int goodsCount, int payment, String cooOrderSerial) {
		// TODO Auto-generated method stub
		return productDao.update91StoreProduct(productId, consumeStreamId,
				info, goodsCount, payment, cooOrderSerial);
	}

	@Override
	public List<NineOneProductLog> getNineOneProductLogByStatus(int status,
			Long roleId) {
		// TODO Auto-generated method stub
		return MapListToNineOneProductLogList(productDao
				.getNineOneProductLogByStatus(status, roleId));
	}

	@Override
	public int update91StoreProductStatus(String cooOrderSerial, int status) {
		NineOneProductLog log = this.getNineOneProductLogByCooOrderSerial(cooOrderSerial);
		if(log==null){
			return 0;
		}
		if(status==1){
			productDao.infoProductAllLog(log.getRoleId(), log.getProductId(), log.getCooOrderSerial(), 1);
		}
		return productDao.update91StoreProductStatus(cooOrderSerial, status);
	}

	@Override
	public String verifyReceipt(String receipt) {
		try {
			int status = -1;
			// This is the URL of the REST webservice in iTunes App Store
			URL url = new URL(payUrl);
			// make connection, use post mode
			HttpsURLConnection connection = (HttpsURLConnection) url
					.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setAllowUserInteraction(false);
			// Encode the binary receipt data into Base 64
			// Here I'm using org.apache.commons.codec.binary.Base64 as an
			// encoder, since commons-codec is already in Grails classpath
			// Base64 encoder = new Base64();
			// String encodedReceipt = new String(encoder.encode(receipt));
			// Create a JSON query object
			// Here I'm using Grails'
			// org.codehaus.groovy.grails.web.json.JSONObject
			Map map = new HashMap();
			map.put("receipt-data", receipt);
			JSONObject jsonObject = new JSONObject(map);
			// Write the JSON query object to the connection output stream
			PrintStream ps = new PrintStream(connection.getOutputStream());
			ps.print(jsonObject.toString());
			ps.close();
			// Call the service
//			BufferedReader br = new BufferedReader(new InputStreamReader(
//					connection.getInputStream()));
			// Extract response
			String rtn = IOUtils.toString(connection.getInputStream(), "utf-8");
//			String str;
//			StringBuffer sb = new StringBuffer();
//			while ((str = br.readLine()) != null) {
//				sb.append(str);
//				sb.append("/n");
//			}
//			br.close();
//			String response = sb.toString();
			// Deserialize response
//			JSONObject result = new JSONObject(response);
//			status = result.getInt("status");
			return rtn;
		} catch (Exception e) {
			return null;
		}

	}

	public String getPayUrl() {
		return payUrl;
	}

	public void setPayUrl(String payUrl) {
		this.payUrl = payUrl;
	}

	@Override
	public String verifyReceiptForSandBox(String receipt) {
		try {
			int status = -1;
			// This is the URL of the REST webservice in iTunes App Store
			URL url = new URL(sandboxPayUrl);
			// make connection, use post mode
			HttpsURLConnection connection = (HttpsURLConnection) url
					.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setAllowUserInteraction(false);
			// Encode the binary receipt data into Base 64
			// Here I'm using org.apache.commons.codec.binary.Base64 as an
			// encoder, since commons-codec is already in Grails classpath
			// Base64 encoder = new Base64();
			// String encodedReceipt = new String(encoder.encode(receipt));
			// Create a JSON query object
			// Here I'm using Grails'
			// org.codehaus.groovy.grails.web.json.JSONObject
			Map map = new HashMap();
			map.put("receipt-data", receipt);
			JSONObject jsonObject = new JSONObject(map);
			// Write the JSON query object to the connection output stream
			PrintStream ps = new PrintStream(connection.getOutputStream());
			ps.print(jsonObject.toString());
			ps.close();
			// Call the service
//			BufferedReader br = new BufferedReader(new InputStreamReader(
//					connection.getInputStream()));
			// Extract response
			String rtn = IOUtils.toString(connection.getInputStream(), "utf-8");
//			String str;
//			StringBuffer sb = new StringBuffer();
//			while ((str = br.readLine()) != null) {
//				sb.append(str);
//				sb.append("/n");
//			}
//			br.close();
//			String response = sb.toString();
			// Deserialize response
//			JSONObject result = new JSONObject(response);
//			status = result.getInt("status");
			return rtn;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<AllProductLog> getAllProductLogByRoleId(int type,Long roleId,int a,int b) {
		// TODO Auto-generated method stub
		return MapListToAllProductLogList(productDao.getAllProductLogByRoleId(type,roleId,a,b));
	}

	@Override
	public List<AllProductLog> getAllProductLog(int type, int a, int b) {
		// TODO Auto-generated method stub
		return MapListToAllProductLogList(productDao.getAllProductLog(type,a,b));
	}

	@Override
	public Long infoAllProductLog(Long roleId,String productId,String cooOrderSerial,int type) {
		// TODO Auto-generated method stub
		return productDao.infoProductAllLog(roleId, productId, cooOrderSerial, type);
	}



}

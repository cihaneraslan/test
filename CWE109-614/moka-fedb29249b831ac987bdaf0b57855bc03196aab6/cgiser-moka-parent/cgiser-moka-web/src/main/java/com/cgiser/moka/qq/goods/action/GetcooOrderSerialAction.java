package com.cgiser.moka.qq.goods.action;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cgiser.keel.http.utils.HttpSpringUtils;
import com.cgiser.moka.action.AbstractAction;
import com.cgiser.moka.common.utils.ServletUtil;
import com.cgiser.moka.manager.ProductManager;
import com.cgiser.moka.model.Product;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.result.ReturnType;
import com.cgiser.moka.tencent.sdk.TencentSdk;
import com.cgiser.moka.wdj.sdk.Base64;

public class GetcooOrderSerialAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger("91store");
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		ReturnType<String> returnType = new ReturnType<String>();
		try{
			Role role = super.getCurrentRole(request);
			if(null==role){
				returnType.setMsg("您还没登录哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			String productId = ServletUtil.getDefaultValue(request, "productId", null);
			String openid = ServletUtil.getDefaultValue(request, "openid", null);
			String openkey = ServletUtil.getDefaultValue(request, "openkey", null);
			String pay_token = ServletUtil.getDefaultValue(request, "pay_token", null);
			String payitem = ServletUtil.getDefaultValue(request, "payitem", null);
			String goodsmeta = ServletUtil.getDefaultValue(request, "goodsmeta", null);
			String goodsurl = ServletUtil.getDefaultValue(request, "goodsurl", null);
			String pf = ServletUtil.getDefaultValue(request, "pf", null);
			String pfkey = ServletUtil.getDefaultValue(request, "pfkey", null);
			String zoneid = ServletUtil.getDefaultValue(request, "zoneid", null);
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("openid", openid);
			paramMap.put("openkey", openkey);
			paramMap.put("pay_token", pay_token);
			paramMap.put("payitem", payitem);
			paramMap.put("goodsmeta", goodsmeta);
			paramMap.put("goodsurl", goodsurl);
			paramMap.put("pf", pf);
			paramMap.put("pfkey", pfkey);
			paramMap.put("zoneid", zoneid);
			TencentSdk tencentSdk = (TencentSdk)HttpSpringUtils.getBean("tencentSdk");
			JSONObject object = tencentSdk.buyGoods(paramMap);
			
			if(productId==null){
				returnType.setMsg("获取订单号失败，产品ID不能为空！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			ProductManager productManager = (ProductManager)HttpSpringUtils.getBean("productManager");
			Product p = productManager.getProductById(productId);
			if(p==null){
				returnType.setMsg("获取订单号失败，您购买的产品不存在或者状态不正常");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			int ret = Integer.parseInt(object.getString("ret"));
			if(ret==0){
				returnType.setStatus(1);
				returnType.setValue(object.getString("token_id")+"_"+object.getString("url_params"));
				super.printReturnType2Response(response, returnType);
				return null;
			}else{
				returnType.setStatus(0);
				returnType.setValue(object.getString("ret")+":"+object.getString("msg"));
				super.printReturnType2Response(response, returnType);
				return null;
			}
			
//			Long storeId = productManager.info91StoreProduct(role.getRoleId(), productId, cooOrderSerial, "", "", 0, 0, 0);
//			if(storeId>0){
//				returnType.setStatus(1);
//				returnType.setValue(cooOrderSerial);
//				super.printReturnType2Response(response, returnType);
//				return null;
//			}else{
//				returnType.setMsg("获取订单号失败，下单失败");
//				returnType.setStatus(0);
//				super.printReturnType2Response(response, returnType);
//				return null;
//			}
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		try{
			returnType.setValue(null);
			returnType.setStatus(0);
			super.printReturnType2Response(response, returnType);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);// TODO: handle exception
		}
		return null;
		
	}
	public static void main(String[] args) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		String url = "key=56abfbcd12fe46f5ad85ad9f12345678&source=GET&%2Fv3%2Fpay%2Fbuy_goods&amt%3D10%26appid%3D15499%26openid%3D00000000000000000000000014BDF6E4%26openkey%3DAB43BF3DC5C3C79D358CC5318E41CF59%26payitem%3Did%2Aname%2Adesc%26pf%3Dqzone%26pfkey%3DCA641BC173479B8C0B35BC84873B3DB9%26ts%3D1340880299%26userip%3D112.90.139.30";
		MessageDigest md = MessageDigest.getInstance("SHA-1");
	    byte[] bDigests = md.digest(url.getBytes("UTF-8"));
	    String sig = new String(Base64.encode(bDigests));
		System.out.println(sig);
	
	}
}

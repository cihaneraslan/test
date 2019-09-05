package com.cgiser.moka.vip.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.util.CollectionUtils;

import com.cgiser.keel.http.utils.HttpSpringUtils;
import com.cgiser.moka.action.AbstractAction;
import com.cgiser.moka.manager.ProductManager;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.model.NineOneProductLog;
import com.cgiser.moka.model.Product;
import com.cgiser.moka.model.ProductLog;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.result.ReturnType;
import com.cgiser.moka.result.VipInfo;

public class GetVipInfoAction extends AbstractAction {
	public static Integer[] vipcash = {0,60,8000,25000,50000,100000,500000,1000000};
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		ReturnType<VipInfo> returnType = new ReturnType<VipInfo>();
		try {
			Role role = super.getCurrentRole(request);
			if (null == role) {
				returnType.setMsg("您还没登录哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			RoleManager roleManager  = (RoleManager)HttpSpringUtils.getBean("roleManager");
			ProductManager productManager = (ProductManager)HttpSpringUtils.getBean("productManager");
			List<ProductLog> productLogList = productManager.getProductLogByRoleId(role.getRoleId());
			List<NineOneProductLog> nineOneProductLogs = productManager.getNineOneProductLogByStatus(1,role.getRoleId());
			int money = 0;
			if(!CollectionUtils.isEmpty(productLogList)){
				for(ProductLog proLog :productLogList){
					Product product2 = productManager.getProductById(proLog.getProductId());
					money = money + product2.getValue();
				}
			}
			if(!CollectionUtils.isEmpty(nineOneProductLogs)){
				for(NineOneProductLog proLog :nineOneProductLogs){
					Product product2 = productManager.getProductById(proLog.getProductId());
					money = money + product2.getValue();
				}
			}
			int vip = VipInfo.getVipInfo(money);
			roleManager.upgradeVip(role.getRoleId(),vip);
			VipInfo vipInfo = new VipInfo();
			vipInfo.setVip(role.getVip());
			vipInfo.setPreCash(vipcash[role.getVip()]);
			vipInfo.setNextCash(vipcash[role.getVip()+1]);
			vipInfo.setCash(money);
			vipInfo.setVipSalary("4_107_1,3_0_2;4_86_1,3_0_3;4_30_1,3_0_5");
			returnType.setStatus(1);
			returnType.setValue(vipInfo);
			super.printReturnType2Response(response, returnType);
			return null;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
}

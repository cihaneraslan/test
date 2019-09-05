package com.cgiser.moka;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.util.CollectionUtils;

import com.cgiser.moka.manager.GiftManager;
import com.cgiser.moka.manager.ProductManager;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.manager.SalaryManager;
import com.cgiser.moka.manager.WDJProductLogManager;
import com.cgiser.moka.model.Gift;
import com.cgiser.moka.model.GiftCode;
import com.cgiser.moka.model.GiftRole;
import com.cgiser.moka.model.Product;
import com.cgiser.moka.model.ProductLog;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.model.VipInfo;
import com.cgiser.moka.model.WdjProductLog;

@ContextConfiguration(locations = {"classpath:/bean/moka-applicationContext.xml" })
public class SalaryTest extends AbstractJUnit4SpringContextTests {
	@Autowired
	private SalaryManager salaryManager;
	@Autowired
	private RoleManager roleManager;
	@Autowired
	private ProductManager productManager;
	@Autowired
	private WDJProductLogManager wdjProductLogManager;
	@Autowired
	private GiftManager giftManager;
	@Test
	public void testExtendSalary() {
//		List<Role> roles = roleManager.getAllRoles(1);
//		for(Role role:roles){
//			salaryManager.extendSalary(2, 100, new Date(), SalaryEnum.OtherSalary, "服务器维护补偿", role.getRoleId());
//		}
//		salaryManager.extendSalary(2, awardValue, time, typ, desc, roleId);
//		System.out.println(roles.size());
		//手动升级vip
//		Role role = roleManager.getRoleByName("giser");
//		List<ProductLog> productLogList = productManager.getProductLogByRoleId(role.getRoleId());
//		List<WdjProductLog> wdjProductLogs = wdjProductLogManager.getWdjProductLogByRoleId(role.getRoleId(), 1);
//		int money = 0;
//		if(!CollectionUtils.isEmpty(productLogList)){
//			for(ProductLog proLog :productLogList){
//				Product product2 = productManager.getProductById(proLog.getProductId());
//				money = money + product2.getValue();
//			}
//		}
//		if(!CollectionUtils.isEmpty(wdjProductLogs)){
//			for(WdjProductLog proLog :wdjProductLogs){
//				Product product2 = productManager.getProductById(proLog.getProductId());
//				money = money + product2.getValue();
//			}
//		}
//		int vip = VipInfo.getVipInfo(money);
//		roleManager.upgradeVip(role.getRoleId(), 1);
		Gift gift = giftManager.getGiftById(1);
		GiftCode giftCode = giftManager.getGiftCodeByCodeGiftId(1, "code");
		giftManager.saveGiftRole(1000000005L, giftCode.getGiftId(), giftCode.getCode());
		
		giftManager.updateGiftCode(giftCode.getGiftId(), giftCode.getCode());
		GiftRole giftRole = giftManager.getGiftRoleByRoleIdCode(1000000005L, giftCode.getGiftId(), giftCode.getCode());
		System.out.println("-------");
		
	}
}

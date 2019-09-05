package com.cgiser.moka;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.cgiser.core.common.cache.mem.MemCachedManager;

@ContextConfiguration(locations = {"classpath:/bean/moka-applicationContext.xml" })
public class NoSqlTest extends AbstractJUnit4SpringContextTests {
	@Autowired
	private MemCachedManager battleCachedManager;
	@Autowired
	private MemCachedManager roleCachedManager;
	@Test
	public void testExtendSalary() throws InterruptedException {
//		List<Role> roles = roleManager.getAllRoles(1);
//		for(Role role:roles){
//			salaryManager.extendSalary(2, 100, new Date(), SalaryEnum.OtherSalary, "服务器维护补偿", role.getRoleId());
//		}
//		salaryManager.extendSalary(2, awardValue, time, typ, desc, roleId);
//		roleManager.getRoleByName("");
//		System.ogut.println(roles.size());
//		battleCachedManager.add("test", 60*1000, "sadasd");
//		Thread.sleep(180000);
		roleCachedManager.set("test", 100000, "yanghao");
		battleCachedManager.get("test");
		System.out.println(System.currentTimeMillis());
		System.out.println(roleCachedManager.get("test"));
		System.out.println(System.currentTimeMillis());
	}
}

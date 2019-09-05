package com.cgiser.moka.manager.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.util.CollectionUtils;


import com.cgiser.moka.dao.VipRoleDao;
import com.cgiser.moka.manager.EmailManager;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.manager.VipInfoManager;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.model.VipInfo;

public class VipInfoManagerImpl implements VipInfoManager {
	private VipRoleDao vipRoleDao;
	private RoleManager roleManager;
	private EmailManager emailManager;
	@Override
	public VipInfo getVipInfoByRoleId(Long roleId) {
		// TODO Auto-generated method stub
		return MapToVipInfo(vipRoleDao.getVipInfo(roleId));
	}

	@Override
	public VipInfo getVipInfoByRoleName(String roleName) {
		Role role = roleManager.getRoleByName(roleName);
		if(role==null){
			return null;
		}
		return this.getVipInfoByRoleId(role.getRoleId());
	}

	@Override
	public Long saveVipInfo(Long roleId, int vip) {
		// TODO Auto-generated method stub
		String vipCode = RandomStringUtils.randomNumeric(6);
		emailManager.sendEmail("VIP邀请函", "【VIP群邀请函】尊贵的玩家，感谢您成为我们VIP服务邀请成员，入群后您将得到我们最新的活动信息和优质服务，我们的群号为：151546858，" +
				"请您入群时填写您尊贵的VIP身份验证码。您的VIP验证码为："+vipCode, 0L, roleId, 1);
		return vipRoleDao.addVipInfo(roleId, vipCode, vip);
	}

	@Override
	public int updateVipInfo(Long roleId, int vip) {
		return vipRoleDao.updateVipInfo(roleId, vip);
	}
	private VipInfo MapToVipInfo(Map<String,Object> map){
		if(CollectionUtils.isEmpty(map)){
			return null;
		}
		VipInfo vipInfo = new VipInfo();
		vipInfo.setCreateTime(new Date(((Timestamp)map.get("CREATETIME")).getTime()));
		vipInfo.setRoleId(new Long(map.get("ROLEID").toString()));
		vipInfo.setState(Integer.parseInt(map.get("STATE").toString()));
		vipInfo.setVip(Integer.parseInt(map.get("VIP").toString()));
		vipInfo.setVipCode(map.get("VIPCODE").toString());
		vipInfo.setVipId(Integer.parseInt(map.get("VIPID").toString()));
		return vipInfo;
	}
	public VipRoleDao getVipRoleDao() {
		return vipRoleDao;
	}

	public void setVipRoleDao(VipRoleDao vipRoleDao) {
		this.vipRoleDao = vipRoleDao;
	}

	public RoleManager getRoleManager() {
		return roleManager;
	}

	public void setRoleManager(RoleManager roleManager) {
		this.roleManager = roleManager;
	}

	public EmailManager getEmailManager() {
		return emailManager;
	}

	public void setEmailManager(EmailManager emailManager) {
		this.emailManager = emailManager;
	}

}

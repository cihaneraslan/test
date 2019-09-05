package com.cgiser.moka.manager;

import com.cgiser.moka.model.VipInfo;

public interface VipInfoManager {
	/**
	 * 获取vip信息
	 * @return
	 */
	public VipInfo getVipInfoByRoleName(String roleName); 
	/**
	 * 获取vip信息
	 * @return
	 */
	public VipInfo getVipInfoByRoleId(Long roleId); 
	/**
	 * 修改用户vip信息
	 * @param roleId
	 * @param vip
	 * @return
	 */
	public int updateVipInfo(Long roleId,int vip);
	/**
	 * 保存vip信息
	 * @param roleId
	 * @param vip
	 * @return
	 */
	public Long saveVipInfo(Long roleId,int vip);
}

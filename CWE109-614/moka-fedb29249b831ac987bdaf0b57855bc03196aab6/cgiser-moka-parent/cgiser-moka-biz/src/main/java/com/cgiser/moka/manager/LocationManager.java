package com.cgiser.moka.manager;

import java.util.List;

import com.cgiser.moka.model.Role;
import com.cgiser.moka.model.RoleLocation;

public interface LocationManager {
	/**
	 * 保存城市坐标范围
	 * @param city
	 * @param location
	 * @return
	 */
	public int saveLocation(String city,String location);
	/**
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public String getCityByXY(String x,String y);
	/**
	 * 调用百度geolocation
	 * @param x
	 * @param y
	 * @return
	 */
	public String getCityByForBaiDu(String x,String y);
	/**
	 * 
	 * @param role
	 * @param geoX
	 * @param geoY
	 * @return
	 */
	public Long saveRoleLocation(Role role,double geoX,double geoY);
	/**
	 * 获取用户的坐标
	 * @param roleId
	 * @return
	 */
	public RoleLocation getRoleLocation(Long roleId);
	/**
	 * 
	 * @param city
	 * @return
	 */
	public List<RoleLocation> getRoleLocationByCity(String city);
}

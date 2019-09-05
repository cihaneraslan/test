package com.cgiser.moka.model;

import java.io.Serializable;

public class UserSoul implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1189501495911562972L;
	private Long userSoulId;
	private Long roleId;
	private int soulId;
	private int level;
	private int state;
	private int isFight;
	private int isEquipment;
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	public int getSoulId() {
		return soulId;
	}
	public void setSoulId(int soulId) {
		this.soulId = soulId;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	/**
	 * 该武器是否已经被使用过（用于战斗中判断）
	 * @return
	 */
	public int getIsFight() {
		return isFight;
	}
	public void setIsFight(int isFight) {
		this.isFight = isFight;
	}
	/**
	 * 该武器是否已装备
	 * @return
	 */
	public int getIsEquipment() {
		return isEquipment;
	}
	public void setIsEquipment(int isEquipment) {
		this.isEquipment = isEquipment;
	}
	public Long getUserSoulId() {
		return userSoulId;
	}
	public void setUserSoulId(Long userSoulId) {
		this.userSoulId = userSoulId;
	}

}

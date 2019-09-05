package com.cgiser.moka.model;

public class MapRoom {
	private  Long roomId;
	private Long roomOwner;
	private int mapId;
	private int roleNum;
	private Long role1;
	private Long role2;
	private Long role3;
	private Long role4;
	private Long role5;
	private Long role6;
	private String turnInfo;
	private String salaryInfo;
	private int state;
	private int win;
	public Long getRoomId() {
		return roomId;
	}
	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}
	public Long getRoomOwner() {
		return roomOwner;
	}
	public void setRoomOwner(Long roomOwner) {
		this.roomOwner = roomOwner;
	}
	public int getMapId() {
		return mapId;
	}
	public void setMapId(int mapId) {
		this.mapId = mapId;
	}
	public int getRoleNum() {
		return roleNum;
	}
	public void setRoleNum(int roleNum) {
		this.roleNum = roleNum;
	}
	public Long getRole1() {
		return role1;
	}
	public void setRole1(Long role1) {
		this.role1 = role1;
	}
	public Long getRole2() {
		return role2;
	}
	public void setRole2(Long role2) {
		this.role2 = role2;
	}
	public Long getRole3() {
		return role3;
	}
	public void setRole3(Long role3) {
		this.role3 = role3;
	}
	public Long getRole4() {
		return role4;
	}
	public void setRole4(Long role4) {
		this.role4 = role4;
	}
	public Long getRole5() {
		return role5;
	}
	public void setRole5(Long role5) {
		this.role5 = role5;
	}
	public Long getRole6() {
		return role6;
	}
	public void setRole6(Long role6) {
		this.role6 = role6;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public int getWin() {
		return win;
	}
	public void setWin(int win) {
		this.win = win;
	}
	public String getTurnInfo() {
		return turnInfo;
	}
	public void setTurnInfo(String turnInfo) {
		this.turnInfo = turnInfo;
	}
	public String getSalaryInfo() {
		return salaryInfo;
	}
	public void setSalaryInfo(String salaryInfo) {
		this.salaryInfo = salaryInfo;
	}
}

package com.cgiser.moka.model;

public class RoleLocation {
	private int id;
	private Long roleId;
	private String roleName;
	private double geoX;
	private double geoY;
	private String city;
	private String address;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public double getGeoX() {
		return geoX;
	}
	public void setGeoX(double geoX) {
		this.geoX = geoX;
	}
	public double getGeoY() {
		return geoY;
	}
	public void setGeoY(double geoY) {
		this.geoY = geoY;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
}

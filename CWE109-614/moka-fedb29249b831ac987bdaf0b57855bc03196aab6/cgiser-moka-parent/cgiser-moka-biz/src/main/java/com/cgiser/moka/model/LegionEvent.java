package com.cgiser.moka.model;

import java.util.Date;

public class LegionEvent {
	private Long eventId;
	private String eventContent;
	private int type;
	private Date createTime;
	private int state;
	private Long legionId;
	/**
	 * 帮派时间的主体
	 */
	private Long roleId;
	public Long getEventId() {
		return eventId;
	}
	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}
	public String getEventContent() {
		return eventContent;
	}
	public void setEventContent(String eventContent) {
		this.eventContent = eventContent;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public Long getLegionId() {
		return legionId;
	}
	public void setLegionId(Long legionId) {
		this.legionId = legionId;
	}
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
}

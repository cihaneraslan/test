package com.cgiser.moka.model;

import java.io.Serializable;

public class StageLevelColor implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -691856569276570225L;
	private Long id;
	private int stageLevelId;
	private int type;
	private int value;
	private Float color;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public int getStageLevelId() {
		return stageLevelId;
	}
	public void setStageLevelId(int stageLevelId) {
		this.stageLevelId = stageLevelId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public Float getColor() {
		return color;
	}
	public void setColor(Float color) {
		this.color = color;
	}
}

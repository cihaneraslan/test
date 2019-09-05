package com.cgiser.moka.model;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Opp implements Serializable {
	/**
	 * 
	 */
	Logger logger = LoggerFactory.getLogger("skill");
	private static final long serialVersionUID = 498834180710581396L;
	private int opp;
	private String target;
	private String UUID;
	private int value;
	public int getOpp() {
		return opp;
	}
	public void setOpp(int opp) {
		this.opp = opp;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getUUID() {
		return UUID;
	}
	public void setUUID(String uUID) {
		UUID = uUID;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		if(this.opp==1022){
			if(value<0){
				logger.debug(this.target+"英雄血量减少"+value);
			}else{
				logger.debug(this.target+"英雄血量增加"+value);
			}
			
		}
		this.value = value;
	}
}

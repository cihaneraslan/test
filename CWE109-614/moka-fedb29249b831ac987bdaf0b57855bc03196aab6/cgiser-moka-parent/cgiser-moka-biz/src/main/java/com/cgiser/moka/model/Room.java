package com.cgiser.moka.model;

import java.io.Serializable;

public class Room implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8557912592709982650L;
	
	private String aPlayer;
	private String dPlayer;
	public String getaPlayer() {
		return aPlayer;
	}
	public void setaPlayer(String aPlayer) {
		this.aPlayer = aPlayer;
	}
	public String getdPlayer() {
		return dPlayer;
	}
	public void setdPlayer(String dPlayer) {
		this.dPlayer = dPlayer;
	}

}

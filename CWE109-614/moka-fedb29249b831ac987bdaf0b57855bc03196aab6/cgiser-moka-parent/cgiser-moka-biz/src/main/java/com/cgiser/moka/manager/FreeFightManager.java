package com.cgiser.moka.manager;

import java.util.concurrent.ConcurrentMap;

import com.cgiser.moka.model.FreeFightModel;


public interface FreeFightManager {
	public void start();
	public void addFightMessage(String aRole,String dRole,String battleId,int round);
	public void removeFightMessage(String key);
	public void matchPlayer();
	public ConcurrentMap<String, FreeFightModel> getFreeFightMessageList();
}

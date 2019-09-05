package com.cgiser.moka.manager.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import com.cgiser.moka.dao.LoginAwardDao;
import com.cgiser.moka.manager.LoginAwardManager;
import com.cgiser.moka.model.LoginAward;

public class LoginAwardManagerImpl implements LoginAwardManager {
	LoginAwardDao loginAwardDao;
	@Override
	public List<LoginAward> getLoginAwardType(int month) {
		// TODO Auto-generated method stub
		return MapToListLoginAward(loginAwardDao.getLoginAwardType(month));
	}
	@Override
	public List<LoginAward> getLoginAwardTypeContainNull(int month) {
		// TODO Auto-generated method stub
		return MapToListLoginAward1(loginAwardDao.getLoginAwardType(month));
	}
	@Override
	public Long insertLoginAwardType(String sql,int month) {
		return loginAwardDao.insertLoginAwardType(sql, month);
	}
	private List<LoginAward> MapToListLoginAward1(Map map){
		if(CollectionUtils.isEmpty(map)){
			return null;
		}
		List<LoginAward> loginAwards = new ArrayList<LoginAward>();
		LoginAward loginAward;
		for(int i=0;i<20;i++){
			int type = Integer.parseInt(map.get("AWARD").toString().split(";")[i].split(",")[0]);
			int value = Integer.parseInt(map.get("AWARD").toString().split(";")[i].split(",")[1]);
			loginAward = new LoginAward();
			loginAward.setAwardType(type);
			loginAward.setAwardValue(value);
			loginAward.setDay(i+1);
			loginAwards.add(loginAward);
		}
		return loginAwards;
		
	}
	private List<LoginAward> MapToListLoginAward(Map map){
		if(CollectionUtils.isEmpty(map)){
			return null;
		}
		List<LoginAward> loginAwards = new ArrayList<LoginAward>();
		LoginAward loginAward;
		for(int i=0;i<20;i++){
			int type = Integer.parseInt(map.get("AWARD").toString().split(";")[i].split(",")[0]);
			int value = Integer.parseInt(map.get("AWARD").toString().split(";")[i].split(",")[1]);
			if(type>0){
				loginAward = new LoginAward();
				loginAward.setAwardType(type);
				loginAward.setAwardValue(value);
				loginAward.setDay(i+1);
				loginAwards.add(loginAward);
			}

		}
		return loginAwards;
		
	}

	public LoginAwardDao getLoginAwardDao() {
		return loginAwardDao;
	}

	public void setLoginAwardDao(LoginAwardDao loginAwardDao) {
		this.loginAwardDao = loginAwardDao;
	}



}

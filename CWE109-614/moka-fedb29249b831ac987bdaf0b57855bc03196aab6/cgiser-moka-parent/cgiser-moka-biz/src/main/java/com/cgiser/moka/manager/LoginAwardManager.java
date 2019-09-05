package com.cgiser.moka.manager;

import java.util.List;

import com.cgiser.moka.model.LoginAward;

public interface LoginAwardManager {
	/**
	 * 获取当前月的登录奖励
	 * @return
	 */
	public List<LoginAward> getLoginAwardType(int month);
	/**
	 * 获取当前月的登录奖励包括没有的
	 * @return
	 */
	public List<LoginAward> getLoginAwardTypeContainNull(int month);
	/**
	 * 增加指定月的当前奖励
	 * @param loginAwards
	 * @return
	 */
	public Long insertLoginAwardType(String loginAwards,int month);
}

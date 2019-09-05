package com.cgiser.moka.manager;

import java.util.Date;
import java.util.List;

import com.cgiser.moka.model.RobLog;

public interface RobLogManager {
	/**
	 * 保存入侵信息
	 * @param roleId
	 * @param robRoleId
	 * @param robRoleCoins
	 * @return
	 */
	public Long infoRobLog(Long roleId, Long robRoleId,int robRoleCoins);
	/**
	 * 获取最后一天的入侵记录
	 * @return
	 */
	public List<RobLog> getLastRobRoleLog(Long roleId,Date date);
}

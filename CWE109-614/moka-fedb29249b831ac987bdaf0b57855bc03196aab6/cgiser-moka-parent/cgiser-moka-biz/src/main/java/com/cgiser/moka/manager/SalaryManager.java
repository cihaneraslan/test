package com.cgiser.moka.manager;

import java.util.Date;
import java.util.List;

import com.cgiser.moka.model.Salary;
import com.cgiser.moka.model.SalaryEnum;

public interface SalaryManager {
	/**
	 * 获取最多5日未领取的奖励
	 * @param roleId
	 * @return
	 */
	public List<Salary> getSalaryByRoleId(Long roleId);
	/**
	 * 发放奖励
	 * @param awardType 奖励的货币类型
	 * @param awardValue 奖励值
	 * @param time 奖励的时间
	 * @param type奖励的类型
	 * @param type奖励的描述
	 * @param roleId 奖励给谁
	 * @return
	 */
	public Long extendSalary(int awardType,int awardValue,Date time,SalaryEnum typ, String desc,Long roleId);
	/**
	 * 发放排名奖励
	 * @return
	 */
	public void extendRankSalary();
	/**
	 * 发放奖励,直接发放不需要领取
	 * @param awardType 奖励的货币类型
	 * @param awardValue 奖励值
	 * @param roleId 奖励给谁
	 * @return
	 */
	public int extendSalaryAdd(int awardType,int awardValue,Long roleId);
	/**
	 * 一键领取奖励
	 * @param roleId
	 * @return
	 */
	public int receiveSalary(Long roleId);
	/**
	 * 一键领取奖励
	 * @param roleId
	 * @return
	 */
	public int getRankSalaryByRank(int rank);
	/**
	 * 获取上次排名战奖励发放时间
	 * @return
	 */
	public Date getLastRankSalaryTime();
}

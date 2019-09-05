package com.cgiser.moka.manager;

import java.util.List;
import java.util.Map;

import com.cgiser.moka.model.ThirdRecord;

public interface ThirdManager {
	//添加投注
	public Long addRecord(Long roleId,int first,int second,int third,int count,int date);
	//获取玩家投注记录
	public List<ThirdRecord> getThirdRecords(Long roleId);
	//获取玩家当前期和上一期的投注记录
	public List<ThirdRecord> getThirdRecordsByRoleIdIssue(Long roleId,String issue);
	//获取当前期期号
	public String getNowThirdIssue();
	//获取上一期期号
	public String getPreThirdIssue();
	//获取上一期中奖号码
	public String getPreThirdNumber();
	//开奖
	public void runLottery();
	//发奖励
	public void reciveSalary();
	
	public Map<Integer,Integer> getCash(int s);
	//获取最后一期的中奖纪录
	public List<ThirdRecord> getWinner(); 
	/**
	 * 根据期号获取中奖号码
	 * @param issue
	 * @return
	 */
	public int getNumByIssue(String issue);
}

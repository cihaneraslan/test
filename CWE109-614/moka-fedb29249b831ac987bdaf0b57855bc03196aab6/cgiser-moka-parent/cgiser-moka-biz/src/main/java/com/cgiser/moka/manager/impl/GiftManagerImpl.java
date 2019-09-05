package com.cgiser.moka.manager.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import com.cgiser.moka.dao.GiftDao;
import com.cgiser.moka.manager.GiftManager;
import com.cgiser.moka.manager.SalaryManager;
import com.cgiser.moka.model.Gift;
import com.cgiser.moka.model.GiftCode;
import com.cgiser.moka.model.GiftRole;
import com.cgiser.moka.model.SalaryEnum;

public class GiftManagerImpl implements GiftManager {

	SalaryManager salaryManager;
	GiftDao giftDao;
	@Override
	public Gift getGiftById(int giftId) {
		// TODO Auto-generated method stub
		return MapToGift(giftDao.getGiftById(giftId));
	}

	@Override
	public GiftCode getGiftCodeByCodeGiftId(int giftId, String code) {
		// TODO Auto-generated method stub
		return MapToGiftCode(giftDao.getGiftCode(giftId, code));
	}

	@Override
	public GiftRole getGiftRoleByRoleId(Long roleId, int giftId) {
		// TODO Auto-generated method stub
		return MapToGiftRole(giftDao.getGiftRoleByRoleId(roleId, giftId, ""));
	}

	@Override
	public synchronized int receiveGift(int giftId, Long roleId, String code) {
		Gift gift = this.getGiftById(giftId);
		if(gift!=null&&gift.getEnd().getTime()>System.currentTimeMillis()&&gift.getStart().getTime()<System.currentTimeMillis()){
			//判断gift还可以领取
			if(gift.getType()==1){
				GiftRole giftRole = this.getGiftRoleByRoleId(roleId, giftId);
				if(giftRole==null){
					String[] giftSalarys = gift.getGiftValue().split(",");
					for(String salary:giftSalarys){
						salaryManager.extendSalary(Integer.parseInt(salary.split("_")[0]), Integer.parseInt(salary.split("_")[1]), new Date(), SalaryEnum.OtherSalary, gift.getGiftName(), roleId);
					}
					this.saveGiftRole(roleId, giftId, "");
				}else{
					return 3;
				}
			}
			if(gift.getType()==2){
				GiftCode giftCode = this.getGiftCodeByCodeGiftId(giftId, code);
				if(giftCode==null||giftCode.getState()==0){
					return 2;
				}
				GiftRole giftRole = this.getGiftRoleByRoleIdCode(roleId, giftId,code);
				if(giftRole==null){
					String[] giftSalarys = gift.getGiftValue().split(",");
					for(String salary:giftSalarys){
						salaryManager.extendSalary(Integer.parseInt(salary.split("_")[0]), Integer.parseInt(salary.split("_")[1]), new Date(), SalaryEnum.OtherSalary, gift.getGiftName(), roleId);
					}
					this.saveGiftRole(roleId, giftId, code);
					this.updateGiftCode(giftId, code);
				}else{
					return 3;
				}
			}
		}else{
			return 0;
		}
		return 1;
	}

	public SalaryManager getSalaryManager() {
		return salaryManager;
	}

	public void setSalaryManager(SalaryManager salaryManager) {
		this.salaryManager = salaryManager;
	}
	private Gift MapToGift(Map<String, Object> map){
		if(CollectionUtils.isEmpty(map)){
			return null;
		}
		Gift gift = new Gift();
		gift.setEnd((Timestamp)map.get("END"));
		gift.setGiftId(Integer.parseInt((String)map.get("ID")));
		gift.setGiftName((String)map.get("GIFTNAME"));
		gift.setGiftValue((String)map.get("GIFTVALUE"));
		gift.setStart((Timestamp)map.get("START"));
		gift.setState(Integer.parseInt((String)map.get("STATE")));
		gift.setType(Integer.parseInt((String)map.get("TYPE")));
		return gift;
	}
	private GiftRole MapToGiftRole(Map<String, Object> map){
		if(CollectionUtils.isEmpty(map)){
			return null;
		}
		GiftRole giftRole = new GiftRole();
		giftRole.setCode((String)map.get("CODE"));
		giftRole.setGiftId(Integer.parseInt((String)map.get("GIFTID")));
		giftRole.setGiftTime((Timestamp)map.get("GIFTTIME"));
		giftRole.setRoleId(new Long((String)map.get("ROLEID")));
		giftRole.setState(Integer.parseInt((String)map.get("STATE")));
		return giftRole;
	}
	private GiftCode MapToGiftCode(Map<String, Object> map){
		if(CollectionUtils.isEmpty(map)){
			return null;
		}
		GiftCode giftCode = new GiftCode();
		giftCode.setCode((String)map.get("CODE"));
		giftCode.setGiftId(Integer.parseInt((String)map.get("GIFTID")));
		giftCode.setState(Integer.parseInt((String)map.get("STATE")));
		return giftCode;
	}
	@Override
	public Long saveGiftRole(Long roleId, int giftId, String code) {
		// TODO Auto-generated method stub
		return giftDao.saveGiftRole(roleId, giftId, code);
	}

	public GiftDao getGiftDao() {
		return giftDao;
	}

	public void setGiftDao(GiftDao giftDao) {
		this.giftDao = giftDao;
	}

	@Override
	public GiftRole getGiftRoleByRoleIdCode(Long roleId, int giftId, String code) {
		// TODO Auto-generated method stub
		return MapToGiftRole(giftDao.getGiftRoleByRoleId(roleId, giftId, code));
	}

	@Override
	public int updateGiftCode(int giftId, String code) {
		// TODO Auto-generated method stub
		return giftDao.updateGiftCode(giftId, code);
	}

}

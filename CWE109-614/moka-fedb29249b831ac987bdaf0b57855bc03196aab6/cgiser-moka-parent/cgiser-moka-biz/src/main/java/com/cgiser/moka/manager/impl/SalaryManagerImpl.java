package com.cgiser.moka.manager.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.cgiser.moka.dao.SalaryDao;
import com.cgiser.moka.manager.ChipManager;
import com.cgiser.moka.manager.FragmentManager;
import com.cgiser.moka.manager.LegionManager;
import com.cgiser.moka.manager.MapManager;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.manager.SalaryManager;
import com.cgiser.moka.manager.UserCardFragmentManager;
import com.cgiser.moka.manager.UserCardManager;
import com.cgiser.moka.manager.UserRuneManager;
import com.cgiser.moka.manager.UserSoulManager;
import com.cgiser.moka.manager.support.DateUtils;
import com.cgiser.moka.model.Legioner;
import com.cgiser.moka.model.RankRole;
import com.cgiser.moka.model.Salary;
import com.cgiser.moka.model.SalaryEnum;

public class SalaryManagerImpl implements SalaryManager {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	private SalaryDao salaryDao;
	private UserCardManager userCardManager;
	private RoleManager roleManager;
	private UserRuneManager userRuneManager;
	private ChipManager chipManager;
	private UserSoulManager userSoulManager;
	private FragmentManager fragmentManager;
	private LegionManager legionManager;
	private UserCardFragmentManager userCardFragmentManager;
	private MapManager mapManager;
	private final static ReentrantLock lock = new ReentrantLock(false);
	@Override
	public Long extendSalary(int awardType, int awardValue, Date time,
			SalaryEnum type, String desc,Long roleId) {
		// TODO Auto-generated method stub
		try{
			int size = 1;
			List<Salary> salaries = this.getSalaryByRoleId(roleId);
			if(!CollectionUtils.isEmpty(salaries)){
				size = size+salaries.size();
			}
			roleManager.updateRoleSalaryCount(roleId, size);
			return salaryDao.extendSalary(awardType, awardValue, time, type.getValue(), desc,roleId);
		}catch (Exception e) {
			logger.error("奖励发放失败"+roleId+desc);
			return 0L;
		}

		
	}
	@Override
	public void extendRankSalary() {
		boolean flag = false;
		Map map = salaryDao.getLastRankSalaryTime();
		if(CollectionUtils.isEmpty(map)){
			salaryDao.insertRankSalaryTime();
			flag = true;
		}else{
			Date date = ((Timestamp) map.get("DATE"));
			if(DateUtils.getDayDiff(date, new Date())>=3){
				salaryDao.insertRankSalaryTime();
				flag = true;
			}
		}
		if(flag){
			int rank = roleManager.getMaxRank();
			if(rank>1000){
				rank = 1000;
			}
			Date date = new Date();
			for(int i=0;i<rank;i++){
				try{
					RankRole role = roleManager.getRoleByRank(i+1);
					if(role==null){
						continue;
					}
					if(role.getLevel()<11){
						continue;
					}
					this.extendSalary(1, getRankSalaryByRank(i+1), date, SalaryEnum.RankSalary, "排名奖励", role.getUid());
				}catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
				
			}
		}
		//重置帮派战次数
		legionManager.resetLegionFightAttackTimes();
		legionManager.resetRoleLegionContribute();
		legionManager.resetRoleLegionFightAttackTimes();
		legionManager.resetLegionRobTimes();
		mapManager.resetStarDay();
	}
	@Override
	public List<Salary> getSalaryByRoleId(Long roleId) {
		// TODO Auto-generated method stub
		return MapListToSalaryList(salaryDao.getSalaryByRoleId(roleId));
	}

	@Override
	public int receiveSalary(Long roleId) {
		// 没有真实发放只是修改了用户的状态
		try{
			lock.lock();
			List<Salary> salaries = this.getSalaryByRoleId(roleId);
			if(CollectionUtils.isEmpty(salaries)){
				return 0;
			}
			Salary salary;
			for(int i=0;i<salaries.size();i++){
				salary = salaries.get(i);
				this.extendSalaryAdd(salary.getAwardType(), salary.getAwardValue(), roleId);
			}
			roleManager.updateRoleSalaryCount(roleId, 0);
			return salaryDao.receiveSalary(roleId);
		}catch (Exception e) {
			logger.error("receive salary error",e);
			return 0;
		}finally{
			lock.unlock();
		}
		
	}
	@Override
	public int extendSalaryAdd(int awardType, int awardValue, Long roleId) {
		if(awardType==1){
			//奖励类型为铜钱
			roleManager.addCoin(roleManager.getRoleById(roleId).getRoleName(), awardValue);
		}else if(awardType==2){
			//奖励类型为元宝
			roleManager.addCash(roleManager.getRoleById(roleId).getRoleName(), awardValue);
		}else if(awardType==3){
			//奖励类型为魔幻券
			roleManager.addTicket(roleManager.getRoleById(roleId).getRoleName(), awardValue);
		}else if(awardType==4){
			//奖励类型为卡牌
			userCardManager.saveUserCard(awardValue, roleId);
		}else if(awardType==5){
			//奖励类型为星辰
			userRuneManager.saveUserRune(awardValue, roleId);
		}else if(awardType==6){
			//神龙碎片
			chipManager.addChip(roleId, awardValue, 1);
		}else if(awardType==7){
			//武器
			userSoulManager.saveUserSoul(roleId, awardValue, 1);
		}else if(awardType==8){
			roleManager.addHonor(roleId, awardValue);
			//荣誉点
		}else if(awardType==9){
			//卡牌碎片
			userCardFragmentManager.addUserFragment(roleId, awardValue, 1);
		}else if(awardType==10){
			//星辰碎片
			if(awardValue==1){
				fragmentManager.addFragment(roleId, 1, 0, 0);
			}else if(awardValue==2){
				fragmentManager.addFragment(roleId, 0, 1, 0);
			}else{
				fragmentManager.addFragment(roleId, 0, 0, 1);
			}
			
		}else if(awardType==11){
			//进化碎片
		}else if(awardType==12){
			//经验
			roleManager.addExp(roleManager.getRoleById(roleId).getRoleName(), awardValue);
		}else if(awardType==13){
			//体力
			roleManager.addEnergy(roleManager.getRoleById(roleId).getRoleName(), awardValue);
		}else if(awardType==14){
			//帮派贡献
			Legioner legioner = legionManager.getLegioner(roleId);
			if(legioner!=null){
//				legionManager.addLegionerHonor(legioner.getId(), awardValue);
				legionManager.addRoleContributeHonor(legioner.getId(), awardValue, awardValue);
			}
			
		}
		return 0;
	}
	private List<Salary> MapListToSalaryList(List<Map<String, Object>> list){
		if(CollectionUtils.isEmpty(list)){
			return null;
		}
		List<Salary> salaries = new ArrayList<Salary>();
		for(int i=0;i<list.size();i++){
			salaries.add(MapToSalary(list.get(i)));
		}
		return salaries;
	}
	private Salary MapToSalary(Map<String, Object> map){
		if(CollectionUtils.isEmpty(map)){
			return null;
		}
		Salary salary = new Salary();
		salary.setAwardType(Integer.parseInt((String)map.get("AWARDTYPE")));
		salary.setAwardValue(Integer.parseInt((String)map.get("AWARDVALUE")));
		salary.setId(Integer.parseInt((String)map.get("ID")));
		salary.setRoleId(new Long((String)map.get("ROLEID")));
		salary.setTime((Timestamp)map.get("TIME"));
		salary.setType(Integer.parseInt((String)map.get("TYPE")));
		salary.setSalaryDesc((String)map.get("SALARYDESC"));
		return salary;
	}
	public SalaryDao getSalaryDao() {
		return salaryDao;
	}

	public void setSalaryDao(SalaryDao salaryDao) {
		this.salaryDao = salaryDao;
	}

	public UserCardManager getUserCardManager() {
		return userCardManager;
	}

	public void setUserCardManager(UserCardManager userCardManager) {
		this.userCardManager = userCardManager;
	}

	public RoleManager getRoleManager() {
		return roleManager;
	}

	public void setRoleManager(RoleManager roleManager) {
		this.roleManager = roleManager;
	}

	public UserRuneManager getUserRuneManager() {
		return userRuneManager;
	}

	public void setUserRuneManager(UserRuneManager userRuneManager) {
		this.userRuneManager = userRuneManager;
	}

	public ChipManager getChipManager() {
		return chipManager;
	}

	public void setChipManager(ChipManager chipManager) {
		this.chipManager = chipManager;
	}

	public UserSoulManager getUserSoulManager() {
		return userSoulManager;
	}

	public void setUserSoulManager(UserSoulManager userSoulManager) {
		this.userSoulManager = userSoulManager;
	}

	public FragmentManager getFragmentManager() {
		return fragmentManager;
	}

	public void setFragmentManager(FragmentManager fragmentManager) {
		this.fragmentManager = fragmentManager;
	}
	@Override
	public int getRankSalaryByRank(int rank) {
		if(rank<50){
			return 260000-2000*(rank-1);
		}
		if(rank<100){
			return 160000-1000*(rank-50);
		}
		if(rank<200){
			return 110000-400*(rank-100);
		}
		if(rank<300){
			return 70000-300*(rank-200);
		}
		if(rank<400){
			return 40000-200*(rank-300);
		}
		if(rank<500){
			return 20000-100*(rank-400);
		}
		if(rank<1000){
			return 10000-20*(rank-500);
		}
		return 0;
	}
	@Override
	public Date getLastRankSalaryTime() {
		Map map = salaryDao.getLastRankSalaryTime();
		Date date;
		if(CollectionUtils.isEmpty(map)){
			salaryDao.insertRankSalaryTime();
			date = new Date();
		}else{
			date = ((Timestamp) map.get("DATE"));
			if(DateUtils.getDayDiff(date, new Date())>=3){
				salaryDao.insertRankSalaryTime();
				date = new Date();
			}
		}
		return date;
	}
	public LegionManager getLegionManager() {
		return legionManager;
	}
	public void setLegionManager(LegionManager legionManager) {
		this.legionManager = legionManager;
	}
	public UserCardFragmentManager getUserCardFragmentManager() {
		return userCardFragmentManager;
	}
	public void setUserCardFragmentManager(
			UserCardFragmentManager userCardFragmentManager) {
		this.userCardFragmentManager = userCardFragmentManager;
	}
	public MapManager getMapManager() {
		return mapManager;
	}
	public void setMapManager(MapManager mapManager) {
		this.mapManager = mapManager;
	}





}

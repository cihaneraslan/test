package com.cgiser.moka.manager.impl;

import java.sql.Timestamp;
import java.util.Map;
import java.util.Set;

import org.springframework.util.CollectionUtils;

import com.cgiser.moka.dao.MatchRoleDao;
import com.cgiser.moka.manager.MatchGameManager;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.model.MatchRoleCache;
import com.cgiser.moka.model.Role;
import com.google.code.yanf4j.util.ConcurrentHashSet;

public class MatchGameManagerImpl implements MatchGameManager {
//	private MemCachedManager matchGameCachedManager;
	public static Set<String> easyRoom = new ConcurrentHashSet<String>();
	public static Set<String> midRoom = new  ConcurrentHashSet<String>();
	public static Set<String> handRoom = new  ConcurrentHashSet<String>();
	public static boolean isStart = false;
	private RoleManager roleManager;
	private MatchRoleDao matchRoleDao;
	@Override
	public int createRoom(Role role) {
		if(role.getLevel()<30){
			easyRoom.add(role.getRoleName());
		}else if(role.getLevel()>=30&&role.getLevel()<45){
			midRoom.add(role.getRoleName());
		}else{
			handRoom.add(role.getRoleName());
		}
		return 1;
	}
	@Override
	public int outRoom(Role role) {
		if(role.getLevel()<30){
			easyRoom.remove(role.getRoleName());
		}else if(role.getLevel()<45&&role.getLevel()>=30){
			midRoom.remove(role.getRoleName());
		}else{
			handRoom.remove(role.getRoleName());
		}
		return 1;
	}
	@Override
	
	public String  findMatchPlayer(Role role,int type) {

//		if(type==1){
//			if(easyRoom.containsKey(DigestUtils.digest(role.getRoleName()))){
//				easyRoom.remove(DigestUtils.digest(role.getRoleName()));
//			}
//			if(CollectionUtils.isEmpty(easyRoom)){
//				return null;
//			}else{
//				String roleName = easyRoom.values().toArray()[0].toString();
//				easyRoom.remove(DigestUtils.digest(roleName));
//				return  roleName;
//			}
//		}else if(type==2){
//			if(midRoom.containsKey(DigestUtils.digest(role.getRoleName()))){
//				midRoom.remove(DigestUtils.digest(role.getRoleName()));
//			}
//			if(CollectionUtils.isEmpty(midRoom)){
//				return null;
//			}else{
//				String roleName = midRoom.values().toArray()[0].toString();
//				midRoom.remove(DigestUtils.digest(roleName));
//				return  roleName;
//			}
//		}else{
//			if(handRoom.containsKey(DigestUtils.digest(role.getRoleName()))){
//				handRoom.remove(DigestUtils.digest(role.getRoleName()));
//			}
//			if(CollectionUtils.isEmpty(handRoom)){
//				return null;
//			}else{
//				String roleName = handRoom.values().toArray()[0].toString();
//				handRoom.remove(DigestUtils.digest(roleName));
//				return  roleName;
//			}
//		}
		return "";
	}

	@Override
	public int joinInRoom() {
		// TODO Auto-generated method stub
		return 0;
	}
//	private int getRoomCount(){
//		Object obj = matchGameCachedManager.get(DigestUtils.digest("roomCount"));
//		if(obj==null){
//			return 0;
//		}else{
//			return Integer.parseInt(obj.toString());
//		}
//	}

//	public MemCachedManager getMatchGameCachedManager() {
//		return matchGameCachedManager;
//	}
//
//	public void setMatchGameCachedManager(MemCachedManager matchGameCachedManager) {
//		this.matchGameCachedManager = matchGameCachedManager;
//	}

	public RoleManager getRoleManager() {
		return roleManager;
	}

	public void setRoleManager(RoleManager roleManager) {
		this.roleManager = roleManager;
	}
	@Override
	public void startMatchGame() {
		// TODO Auto-generated method stub
		isStart = true;
	}
	@Override
	public void stopMatchGame() {
		// TODO Auto-generated method stub
		isStart = false;
	}
	private MatchRoleCache MapToMatchRoleCache(Map<String, Object> map){
		if(CollectionUtils.isEmpty(map)){
			return null;
		}
		MatchRoleCache matchRoleCache = new MatchRoleCache();
		matchRoleCache.setMatchHasTimes(Integer.parseInt((String)map.get("MATCHLASTTIMES")));
		matchRoleCache.setMatchLastAddCd((Timestamp)map.get("LASTADDTIME"));
		matchRoleCache.setMatchLastTime((Timestamp)map.get("MATCHLASTTIME"));
		matchRoleCache.setRoleId(new Long((String)map.get("ROLEID")));
		Long s = (System.currentTimeMillis()-matchRoleCache.getMatchLastAddCd().getTime())/1000;
		matchRoleCache.setMatchCutDown(s%1800);
		return matchRoleCache;
	}
	@Override
	public int addAllRoleMatchCD() {
		// TODO Auto-generated method stub
		return matchRoleDao.addAllMatchRoleCD();
	}
	@Override
	public int addRoleMatchCD(Long roleId) {
		return matchRoleDao.addMatchRoleCD(roleId);
	}
	@Override
	public MatchRoleCache getMacthRole(Long roleId) {
		Map<String,Object> map = matchRoleDao.getMatchRoleById(roleId);
		if(CollectionUtils.isEmpty(map)){
			matchRoleDao.saveMatchRole(roleId);
		}
		return MapToMatchRoleCache(matchRoleDao.getMatchRoleById(roleId));
	}
	@Override
	public int updateRoleMatchCD(Long roleId) {
		// TODO Auto-generated method stub
		return matchRoleDao.updateMatchRoleCD(roleId);
	}
	public MatchRoleDao getMatchRoleDao() {
		return matchRoleDao;
	}
	public void setMatchRoleDao(MatchRoleDao matchRoleDao) {
		this.matchRoleDao = matchRoleDao;
	}
	@Override
	public int updateRoleMatchTimeAndCD(Long roleId) {
		// TODO Auto-generated method stub
		return matchRoleDao.updateRoleMatchTimeAndTimes(roleId);
	}
	

}

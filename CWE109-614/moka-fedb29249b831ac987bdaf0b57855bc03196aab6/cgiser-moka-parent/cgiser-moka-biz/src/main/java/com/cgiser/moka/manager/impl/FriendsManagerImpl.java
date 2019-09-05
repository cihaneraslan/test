package com.cgiser.moka.manager.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import com.cgiser.moka.dao.FriendsDao;
import com.cgiser.moka.manager.AchievementManager;
import com.cgiser.moka.manager.FriendsManager;
import com.cgiser.moka.manager.LegionManager;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.model.Friend;
import com.cgiser.moka.model.Legioner;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.model.UserAchievement;

public class FriendsManagerImpl implements FriendsManager {
	private FriendsDao friendsDao;
	private RoleManager roleManager;
	private LegionManager legionManager;
	private AchievementManager achievementManager;
	@Override
	public List<Friend> getFriendsByRoleName(String roleName) {
		List<Map<String, Object>> list = friendsDao.getFriendsByRoleName(roleName);
		if(CollectionUtils.isEmpty(list)){
			return null;
		}
		List<Friend> friendList = new ArrayList<Friend>();
		Role role;
		Friend friend;
		for(int i=0;i<list.size();i++){
			String roleName1 = (String)list.get(i).get("ROLENAME1");
			String roleName2 = (String)list.get(i).get("ROLENAME2");
			friend = new Friend();
			friend.setUdid(Integer.parseInt(list.get(i).get("ID").toString()));
			if(roleName1.equals(roleName)){
				role = roleManager.getRoleByName(roleName2);
				friend.setRoleName(roleName2);
			}else{
				role =roleManager.getRoleByName(roleName1);
				friend.setRoleName(roleName1);
			}
			
			if(role!=null){
				friend.setAvatar(role.getAvatar());
				friend.setCoins(role.getCoins());
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				friend.setLastLogin(dateFormat.format(role.getLastLoginTime()==null?new Date():role.getLastLoginTime()));
				Legioner legioner = legionManager.getLegioner(role.getRoleId());
				if(legioner!=null){
					friend.setLegionName(legionManager.getLegionById(legioner.getLegionId()).getName());
				}
				friend.setInviteCode(role.getInviteCode());
				friend.setVip(role.getVip());
				friend.setCity(role.getCity());
				friend.setX(role.getX());
				friend.setY(role.getY());
				friend.setLevel(role.getLevel());
				friend.setLose(role.getLose());
				friend.setRank(role.getRank());
				friend.setSex(role.getSex());
				friend.setWin(role.getWin());
				friendList.add(friend);
				friend.setStatus(role.getStatus());
			}
		}
		return friendList;
	}

	@Override
	public List<Friend> getFriendsInviteByRoleName(String roleName) {
		List<Map<String, Object>> list = friendsDao.getFriendsInviteByRoleName(roleName);
		if(CollectionUtils.isEmpty(list)){
			return null;
		}
		List<Friend> friendList = new ArrayList<Friend>();
		Role role;
		Friend friend;
		for(int i=0;i<list.size();i++){
			String roleName1 = (String)list.get(i).get("ROLENAME1");
			friend = new Friend();
			friend.setUdid(Integer.parseInt(list.get(i).get("ID").toString()));
			role = roleManager.getRoleByName(roleName1);
			friend.setRoleName(roleName1);
			
			if(role!=null){
				friend.setAvatar(role.getAvatar());
				friend.setCoins(role.getCoins());
				friend.setLastLogin(String.valueOf(System.currentTimeMillis()));
				friend.setLegionName("");
				friend.setLevel(role.getLevel());
				friend.setLose(role.getRankWin());
				friend.setRank(role.getRank());
				friend.setSex(role.getSex());
				friend.setWin(role.getRankWin());
				friendList.add(friend);
			}
		}
		return friendList;
	}
	@Override
	public Long inviteFriend(String roleName1, String roleName2) {
		return friendsDao.inviteFriend(roleName1, roleName2);
	}
	@Override
	public Long applyFriendInvite(String roleName1, String roleName2) {
		
		Long friendId = friendsDao.applyFriendInvite(roleName1, roleName2);
		if(friendId>0){
			Role role1 = roleManager.getRoleByName(roleName1);
			Role role2 = roleManager.getRoleByName(roleName2);
			List<Friend> friends = this.getFriendsByRoleName(roleName1);
			UserAchievement userAchievement = achievementManager.getUserAchievementById(role1.getRoleId(), 49);
			if(userAchievement==null){
				achievementManager.saveUserAchievement(49, role1.getRoleId(),1);
			}
			if(userAchievement!=null&&userAchievement.getFinishState()<21){
				achievementManager.saveUserAchievement(49, role1.getRoleId(),userAchievement.getFinishState()+1);
			}
			userAchievement = achievementManager.getUserAchievementById(role2.getRoleId(), 49);
			if(userAchievement==null){
				achievementManager.saveUserAchievement(49, role2.getRoleId(),1);
			}
			if(userAchievement!=null&&userAchievement.getFinishState()<21){
				achievementManager.saveUserAchievement(49, role2.getRoleId(),userAchievement.getFinishState()+1);
			}
		}
		return friendId;
	}

	@Override
	public Long delFriendInvite(String roleName1, String roleName2) {
		return friendsDao.delFriendInvite(roleName1, roleName2);
	}
	public FriendsDao getFriendsDao() {
		return friendsDao;
	}

	public void setFriendsDao(FriendsDao friendsDao) {
		this.friendsDao = friendsDao;
	}
	@Override
	public boolean isFriend(String roleName1, String roleName2) {
		boolean a = !CollectionUtils.isEmpty(friendsDao.findFriendInvit(roleName1, roleName2,1,1));
		
		boolean b = !CollectionUtils.isEmpty(friendsDao.findFriendInvit(roleName2, roleName1,1,1));
		
		if(a||b){
			return true;
		}
		return false;
	}

	@Override
	public boolean isInvitFriend(String roleName1, String roleName2) {
		// TODO Auto-generated method stub
		return !CollectionUtils.isEmpty(friendsDao.findFriendInvit(roleName1, roleName2,0,1));
	}

	public RoleManager getRoleManager() {
		return roleManager;
	}

	public void setRoleManager(RoleManager roleManager) {
		this.roleManager = roleManager;
	}

	public LegionManager getLegionManager() {
		return legionManager;
	}

	public void setLegionManager(LegionManager legionManager) {
		this.legionManager = legionManager;
	}

	public AchievementManager getAchievementManager() {
		return achievementManager;
	}

	public void setAchievementManager(AchievementManager achievementManager) {
		this.achievementManager = achievementManager;
	}
}

package com.cgiser.moka.manager.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang.StringUtils;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.DynamicChannelBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.cgiser.core.common.cache.mem.MemCachedManager;
import com.cgiser.moka.dao.RoleDao;
import com.cgiser.moka.dao.util.DigestUtils;
import com.cgiser.moka.data.RoleDo;
import com.cgiser.moka.manager.AchievementManager;
import com.cgiser.moka.manager.HeroLevelManager;
import com.cgiser.moka.manager.LocationManager;
import com.cgiser.moka.manager.MessageManager;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.manager.SalaryManager;
import com.cgiser.moka.manager.SoulManager;
import com.cgiser.moka.manager.UserCardGroupManager;
import com.cgiser.moka.manager.UserCardManager;
import com.cgiser.moka.manager.UserStageManager;
import com.cgiser.moka.manager.VipInfoManager;
import com.cgiser.moka.manager.support.DateUtils;
import com.cgiser.moka.message.netty.MessageUtil;
import com.cgiser.moka.model.Achievement;
import com.cgiser.moka.model.HeroLevel;
import com.cgiser.moka.model.MessageType;
import com.cgiser.moka.model.RankRole;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.model.RoleLocation;
import com.cgiser.moka.model.SalaryEnum;
import com.cgiser.moka.model.Soul;
import com.cgiser.moka.model.UserAchievement;
import com.cgiser.moka.model.UserStage;
import com.cgiser.moka.model.VipInfo;
import com.cgiser.sso.client.UserGameFacade;
import com.cgiser.sso.model.User;

public class RoleManagerImpl implements RoleManager {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	private RoleDao roleDao;
	private UserCardGroupManager userCardGroupManager;
	private MemCachedManager roleCachedManager;
	private HeroLevelManager heroLevelManager;
	private LocationManager locationManager;
	private AchievementManager achievementManager;
	private UserStageManager userStageManager;
	private UserGameFacade userGameFacade;
	private UserCardManager userCardManager;
	private SoulManager soulManager;
	private VipInfoManager vipInfoManager;
	private MessageManager messageManager;
	private SalaryManager salaryManager;
	private final static Lock expLock = new ReentrantLock();
	private final static Lock energyLock = new ReentrantLock();
	private final static Lock cashLock = new ReentrantLock();
	private final static Lock coinsLock = new ReentrantLock();
	@Override
	public List<Role> getRolesByUserIden(String userIden, Long serverId) {
		List list = roleDao.getRolesByUserIden(userIden, serverId);
		List<Role> roleList = MapToListRole(list);
		if (!CollectionUtils.isEmpty(roleList)) {
			for (int i = 0; i < roleList.size(); i++) {
				Role role = (Role) roleCachedManager.get("role_"+DigestUtils
						.digest(roleList.get(i).getRoleName()));
				if (role != null) {
					Date lastRecive = role.getLastReceiveEnergy();
					Date date = new Date();
					int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
					boolean flag = false;
					if ((hour >= 12 && hour < 14)
							|| (hour >= 18 && hour < 20)) {
						if (lastRecive == null) {
							flag = true;
						}else if (DateUtils.isSameDay(lastRecive, date)) {
							if (hour >= 18 && hour < 20) {
								if (lastRecive.getHours() >= 18
										&& lastRecive.getHours() < 20) {
									flag = false;
								} else {
									flag = true;
								}
							}
							if (hour >= 12 && hour < 14) {
								if (lastRecive.getHours() >= 12
										&& lastRecive.getHours() < 14) {
									flag = false;
								} else {
									flag = true;
								}
							}
						} else {
							flag = true;
						}
					}
					roleList.get(i).setEnergyCanRecive(flag ? 1 : 0);
					try {
						this.updateRoleCache(roleList.get(i));
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					}
				}
			}

			return roleList;
		} else {
			return null;
		}
	}

	public Role getRole() {
		return null;
	}

	public Role getRoleByInvitCode(String invitCode) {
		return MapToRole(roleDao.getRoleByInvitCode(invitCode));
	}

	@Override
	public boolean addEnergy(String roleName, int energy) {
		Role role = this.getRoleByName(roleName);
		if (roleDao.updateRoleEnergyByRoleId(role.getRoleId(), energy, 1)) {
			this.updateRoleCache(role);
			return true;
		} else {
			return false;
		}

	}

	@Override
	public boolean addExp(String roleName, int exp) {
		try{
			expLock.lock();
			Role role = this.getRoleByName(roleName);
			HeroLevel heroLevel = heroLevelManager.getHeroLevelByLevel(role
					.getLevel() + 1);
			HeroLevel lastHeroLevel = heroLevelManager.getHeroLevelByLevel(role
					.getLevel());
			if (role.getExp()<heroLevel.getExp()&&(role.getExp() + exp) >= heroLevel.getExp()) {
				role.setLevel(role.getLevel() + 1);
				if (role.getLevel() >= 15) {
					if(!StringUtils.isEmpty(role.getInviteRoleCode())){
						Role inviteRole = this.getRoleByInvitCode(role.getInviteRoleCode());
						if(inviteRole!=null){
							UserAchievement userAchievement = achievementManager
							.getUserAchievementById(inviteRole.getRoleId(), 58);
							if (userAchievement == null) {
								achievementManager.saveUserAchievement(58, inviteRole
										.getRoleId(), 1);
							}
							if (userAchievement != null
									&& userAchievement.getFinishState() <1) {
								achievementManager.saveUserAchievement(58, inviteRole
										.getRoleId(),
										userAchievement.getFinishState() + 1);
							}
						}
					}
				}
				if (role.getLevel() == 15) {
					userCardGroupManager.createUserCardGroup(role.getRoleId(),
							"第二卡组", "", "");
				}
				if (role.getLevel() == 25) {
					userCardGroupManager.createUserCardGroup(role.getRoleId(),
							"第三卡组", "", "");
				}
				if (role.getLevel() == 35) {
					userCardGroupManager.createUserCardGroup(role.getRoleId(),
							"第四卡组", "", "");
				}
				role.setLeaderShip(heroLevel.getCost());
				role.setHP(heroLevel.getHp());
				int max = heroLevel.getFriendNum() - lastHeroLevel.getFriendNum();
				this.addFriendNumMax(role.getRoleId(), max);
				role.setPrevExp(role.getNextExp());
				HeroLevel heroLevel1 = heroLevelManager.getHeroLevelByLevel(role
						.getLevel() + 1);
				role.setNextExp(heroLevel1.getExp());
				roleDao.updateRole(roleName, role.getPrevExp(), role.getNextExp(),
						role.getLevel(), heroLevel.getCost(), heroLevel
								.getFriendNum(), heroLevel.getHp());
			}
			role.setExp(role.getExp() + exp);
			if (roleDao.updateRoleExp(roleName, exp, 1)) {
				this.updateRoleCache(role);
				return true;
			} else {
				return false;
			}
		}catch (Exception e) {
			logger.error("add exp error",e);
			return false;
		}finally{
			expLock.unlock();
		}
		
	}

	@Override
	public boolean updateEnergy(String roleName, int energy) {
		try{
			energyLock.lock();
			Role role = this.getRoleByName(roleName);
			if (roleDao.updateRoleEnergyByRoleId(role.getRoleId(), energy, 0)) {
				this.updateRoleCache(role);
				return true;
			} else {
				return false;
			}
		}catch (Exception e) {
			logger.error("update energy error",e);
			return false;
		}finally{
			energyLock.unlock();
		}
		
	}

	@Override
	public boolean updateCash(String roleName, int cash) {
		try{
			cashLock.lock();
			if (roleDao.updateRoleCash(roleName, cash, 0)) {
				Role role = (Role) roleCachedManager.get("role_"+DigestUtils
						.digest(roleName));
				if (role != null) {
					role.setCash(role.getCash() - cash);
					this.updateRoleCache(role);
				}
				return true;
			} else {
				return false;
			}
		}catch (Exception e) {
			logger.error("update cash error",e);
			return false;
		}finally{
			cashLock.unlock();
		}

	}

	@Override
	public boolean updateCoin(String roleName, int coin) {
		try{
			coinsLock.lock();
			if (roleDao.updateRoleCoin(roleName, coin, 0)) {
				Role role = (Role) roleCachedManager.get("role_"+DigestUtils
						.digest(roleName));
				if (role != null) {
					role.setCoins(role.getCoins() - coin);
					this.updateRoleCache(role);
				}
				return true;
			} else {
				return false;
			}
		}catch (Exception e) {
			logger.error("update coins error",e);
			return false;
		}finally{
			coinsLock.unlock();
		}
		
	}

	@Override
	public boolean addCoin(String roleName, int coin) {
		// TODO Auto-generated method stub
		if (roleDao.updateRoleCoin(roleName, coin, 1)) {
			Role role = (Role) roleCachedManager.get("role_"+DigestUtils
					.digest(roleName));
			if (role != null) {
				role.setCoins(role.getCoins() + coin);
				this.updateRoleCache(role);
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean addTicket(String roleName, int ticket) {
		if (roleDao.updateRoleTicket(roleName, ticket, 1)) {
			Role role = (Role) roleCachedManager.get("role_"+DigestUtils
					.digest(roleName));
			if (role != null) {
				role.setTicket(role.getTicket() + ticket);
				this.updateRoleCache(role);
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean updateTicket(String roleName, int ticket) {
		if (roleDao.updateRoleTicket(roleName, ticket, 0)) {
			Role role = (Role) roleCachedManager.get("role_"+DigestUtils
					.digest(roleName));
			if (role != null) {
				role.setTicket(role.getTicket() - ticket);
				this.updateRoleCache(role);
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean addHonor(Long roleId, int honor) {
		if (roleDao.updateRoleHonor(roleId, honor, 1)) {
			Role role = this.getRoleById(roleId);
			role = (Role) roleCachedManager.get("role_"+DigestUtils.digest(role
					.getRoleName()));
			if (role != null) {
				role.setHonor(role.getHonor() + honor);
				this.updateRoleCache(role);
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean updateHonor(Long roleId, int honor) {
		if (roleDao.updateRoleHonor(roleId, honor, 0)) {
			Role role = this.getRoleById(roleId);
			role = (Role) roleCachedManager.get("role_"+DigestUtils.digest(role
					.getRoleName()));
			if (role != null) {
				role.setHonor(role.getHonor() - honor);
				this.updateRoleCache(role);
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean addCash(String roleName, int cash) {
		// TODO Auto-generated method stub
		if (roleDao.updateRoleCash(roleName, cash, 1)) {
			Role role = (Role) roleCachedManager.get("role_"+DigestUtils
					.digest(roleName));
			if (role != null) {
				role.setCash(role.getCash() + cash);
				this.updateRoleCache(role);
			}
			return true;
		} else {
			return false;
		}
	}

	private List<Role> MapToListRole(List<Map<String, Object>> list) {
		if (CollectionUtils.isEmpty(list)) {
			return null;
		}
		List<Role> roleList = new ArrayList<Role>();
		Role role;
		for (Map map : list) {
			roleList.add(MapToRole(map));
		}
		return roleList;

	}

	public Role MapToRole(Map map) {
		if (CollectionUtils.isEmpty(map)) {
			return null;
		}
		Role role = new Role();
		BigDecimal bd = new BigDecimal((String) map.get("ROLEID"));
		role.setRoleId(Long.parseLong(bd.toPlainString()));
		role.setRoleName((String) map.get("ROLENAME"));
		role.setAvatar(Integer.parseInt(map.get("AVATAR").toString()));
		role.setBoss(Integer.parseInt(map.get("BOSS").toString()));
		role.setBossFightRank((String) map.get("BOSSFIGTRNK"));
		role.setCash(Integer.parseInt(map.get("CASH").toString()));
		role.setCoins(Integer.parseInt(map.get("COINS").toString()));
		role.setDefaultGroupId(Integer.parseInt(map.get("DEFAULTGROUPID")
				.toString()));
		role.setEnergy(Integer.parseInt(map.get("ENERGY").toString()));
		role.setEnergyBuyCount(Integer.parseInt(map.get("ENERGYBUYCOUNT")
				.toString()));
		role.setEnergyBuyTime(((Timestamp) map.get("ENERGYBUYTIME")).getTime());
		role.setEnergyLastTime(((Timestamp) map.get("ENERGYLASTTIME"))
				.getTime());
		role.setEnergyMax(Integer.parseInt(map.get("ENERGYMAX").toString()));
		role.setExp(Integer.parseInt(map.get("EXP").toString()));
		role.setFragment_3(Integer.parseInt(map.get("FRAGMENT_3").toString()));
		role.setFragment_4(Integer.parseInt(map.get("FRAGMENT_4").toString()));
		role.setFragment_5(Integer.parseInt(map.get("FRAGMENT_5").toString()));
//		role.setFreshMan(Integer.parseInt(map.get("FRESHMAN").toString()));
		String strFreshStep = map.get("FRESHSTEP").toString();
		if (StringUtils.isEmpty(strFreshStep)) {
			role.setFreshStep(null);
		} else {
			String[] fresh = (map.get("FRESHSTEP").toString()).split(",");
			if (fresh.length < 15) {
				fresh = new String[15];
				for (int i = 0; i < fresh.length; i++) {
					fresh[i] = "1";
				}
				role.setFreshStep(fresh);
			} else {
				role.setFreshStep(fresh);
			}
		}
		role.setFriendApplyNum(Integer.parseInt(map.get("FRIENDAPPLYNUM")
				.toString()));
		role.setFriendNumMax(Integer.parseInt(map.get("FRIENDNUMMAX")
				.toString()));
		role.setHonor(Integer.parseInt(map.get("HONOR").toString()));
		role.setHP(Integer.parseInt(map.get("HP").toString()));
		role.setInviteCode((String) map.get("INVITECODE"));
		role.setInviteRoleCode((String) map.get("INVITEROLECODE"));
		role.setInviteNum(Integer.parseInt(map.get("INVITENUM").toString()));
		role.setLeaderShip(Integer.parseInt(map.get("LEADERSHIP").toString()));
		role.setLevel(Integer.parseInt(map.get("LEVEL").toString()));
		role.setLoginContinueTimes(Integer.parseInt(map.get(
				"LOGINCONTINUETIMES").toString()));
		role.setLastLoginTime(new Date(((Timestamp) map.get("LASTLOGIONTIME"))
				.getTime()));
		role.setLose(Integer.parseInt(map.get("LOSE").toString()));
		role.setNewEmail(Integer.parseInt(map.get("NEWEMAIL").toString()));
		role.setNextExp(Integer.parseInt(map.get("NEXTEXP").toString()));
		role.setPrevExp(Integer.parseInt(map.get("PREVEXP").toString()));
		role.setRankLost(Integer.parseInt(map.get("RANKLOST").toString()));
		role.setRank(Integer.parseInt(map.get("RANK").toString()));
		role.setRankWin(Integer.parseInt(map.get("RANKWIN").toString()));
		role
				.setSalaryCount(Integer.parseInt(map.get("SALARYCOUNT")
						.toString()));
		role.setSex(Integer.parseInt(map.get("SEX").toString()));
		role.setServerId(new Long((String) map.get("SERVERID")));
		role.setThievesTimes(Integer.parseInt(map.get("THIEVESTIMES")
				.toString()));
		role.setTicket(Integer.parseInt(map.get("TICKET").toString()));
		role.setUserIden((String) map.get("USERIDEN"));
		role.setWin(Integer.parseInt(map.get("WIN").toString()));
		role.setVip(Integer.parseInt(map.get("VIP").toString()));
		role.setCity(map.get("CITY") == null ? "" : map.get("CITY").toString());
		role.setState(Integer.parseInt(map.get("STATUS").toString()));
		RoleLocation roleLocation = locationManager.getRoleLocation(role.getRoleId());
		role.setX(roleLocation==null?0:roleLocation.getGeoX());
		role.setY(roleLocation==null?0:roleLocation.getGeoY());
		//数据库获取的好友状态都为离线
		role.setStatus(3);
		role.setFreeFightTime( map.get("FREEFIGHTTIME")==null?null:(Timestamp)map.get("FREEFIGHTTIME"));
		role.setLastReceiveEnergy(map.get("LASTRECEIVEENERGY")==null?null:(Timestamp) map.get("LASTRECEIVEENERGY"));
		role.setSoulRefreshTime(map.get("SOULREFRESHTIME")==null?null:((Timestamp) map.get("SOULREFRESHTIME")).getTime());
		role.setSendEnergyTimes(Integer.parseInt(map.get("SENDENERGYTIMES").toString()));
		role.setMapLastIncome(map.get("MAPLASTINCOME")==null?null:(Timestamp) map.get("MAPLASTINCOME"));
		String strSoul = (String)map.get("SOULS");
		if(strSoul!=null&&strSoul.length()!=0){
			String[] strSouls = strSoul.split("_");
			List<Soul> souls = new ArrayList<Soul>();
			for(String soulId:strSouls){
				souls.add(soulManager.getSoulById(Integer.parseInt(soulId)));
			}
			role.setSouls(souls);
		}
		return role;
	}

	private List<RankRole> MapToListRankRole(List<Map> list) {
		if (CollectionUtils.isEmpty(list)) {
			return null;
		}
		List<RankRole> roleList = new ArrayList<RankRole>();
		Role role;
		for (Map map : list) {
			roleList.add(MapToRankRole(map));
		}
		return roleList;

	}

	public RankRole MapToRankRole(Map map) {
		if (CollectionUtils.isEmpty(map)) {
			return null;
		}
		RankRole rankRole = new RankRole();
		rankRole.setAvatar(Integer.parseInt(map.get("AVATAR").toString()));
		rankRole.setLevel(Integer.parseInt(map.get("LEVEL").toString()));
		rankRole.setLose(Integer.parseInt(map.get("LOSE").toString()));
		rankRole.setRank(Integer.parseInt(map.get("RANK").toString()));
		rankRole.setRoleName((String) map.get("ROLENAME"));
		rankRole.setSex(Integer.parseInt(map.get("SEX").toString()));
		rankRole.setUid(new Long((String) map.get("ROLEID")));
		rankRole.setWin(Integer.parseInt(map.get("WIN").toString()));
		return rankRole;
	}

	@Override
	public int updateRoleDefaultGroup(Long roleId, Long groupId) {
		// TODO Auto-generated method stub
		if (roleDao.updateRoleDefaultGroup(roleId, groupId) > 0) {
			Role role = this.getRoleById(roleId);
			role = (Role) roleCachedManager.get("role_"+DigestUtils.digest(role.getRoleName()));
			if (role != null) {
				role.setDefaultGroupId(groupId.intValue());
				this.updateRoleCache(role);
			}
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public Long createRole(String gameIden,String userIden, String serverId, String roleName,
			String inviteCode, int sex) {
		RoleDo role = new RoleDo();
		role.init();
		role.setSex(sex);
		role.setAvatar(role.getSex()==0?10000:10001);
		HeroLevel heroLevel = heroLevelManager.getHeroLevelByLevel(1);
		HeroLevel heroLevelNext = heroLevelManager.getHeroLevelByLevel(2);
		role.setRank(this.getMaxRank() + 1);
		role.setLevel(heroLevel.getLevel());
		role.setExp(heroLevel.getExp());
		role.setNextExp(heroLevelNext.getExp());
		role.setRoleName(roleName);
		role.setHP(heroLevel.getHp());
		role.setLeaderShip(heroLevel.getCost());
		role.setFriendNumMax(heroLevel.getFriendNum());
		Long roleId = roleDao.getSequence("t_cgiser_mokarole");
		role.setInviteCode(String.valueOf(roleId%100000));
		role.setServerId(new Long(serverId));
		role.setUserIden(userIden);
		role.setInviteRoleCode(inviteCode);
		roleId = this.saveRole(role);
		if (roleId > 0) {
			if (!StringUtils.isEmpty(inviteCode)) {
				Role inviteRole = this.getRoleByInvitCode(inviteCode);
				if (inviteRole != null && inviteRole.getState() == 1&&!inviteRole.getUserIden().equals(role.getUserIden())) {
					roleDao.addRoleInvitNum(inviteRole.getRoleId(), 1);
					salaryManager.extendSalary(3, 1, new Date(), SalaryEnum.OtherSalary, "好友邀请码奖励", roleId);
					Achievement achievement = achievementManager.getAchievementById(47);
					UserAchievement userAchievement = achievementManager
							.getUserAchievementById(inviteRole.getRoleId(), 47);
					if (userAchievement == null) {
						achievementManager.saveUserAchievement(47, inviteRole
								.getRoleId(), 1);
					}
					if (userAchievement != null
							&& userAchievement.getFinishState() <achievement.getFinishNum()) {
						achievementManager.saveUserAchievement(47, inviteRole
								.getRoleId(),
								userAchievement.getFinishState() + 1);
					}
					achievement = achievementManager.getAchievementById(57);
					userAchievement = achievementManager
					.getUserAchievementById(inviteRole.getRoleId(), 57);
					if (userAchievement == null) {
						achievementManager.saveUserAchievement(57, inviteRole
								.getRoleId(), 1);
					}
					if (userAchievement != null
							&& userAchievement.getFinishState() <achievement.getFinishNum()) {
						achievementManager.saveUserAchievement(57, inviteRole
								.getRoleId(),
								userAchievement.getFinishState() + 1);
					}
				}
			}
			Long groupId = userCardGroupManager.createUserCardGroup(roleId,
					"第一卡组", "31_4_4", "");
			this.updateRoleDefaultGroup(roleId, groupId);
			this.addHonor(roleId, 300);
			List<Role> roleList = this.getRolesByUserIden(userIden,new Long(serverId));
			userGameFacade.updateRoleNum(gameIden, userIden, new Long(serverId), roleList.size());
			return roleId;
		} else {
			return 0L;
		}

	}

	@Override
	public Long saveRole(RoleDo role) {

		return roleDao.saveRole(role);
	}

	public RoleDao getRoleDao() {
		return roleDao;
	}

	public void setRoleDao(RoleDao roleDao) {
		this.roleDao = roleDao;
	}

	@Override
	public Role getRoleByName(String roleName) {
		Object obj = roleCachedManager.get("role_"+DigestUtils.digest(roleName));
		if (obj != null) {
			return (Role) obj;
		} else {
			Map map = roleDao.getRoleByRoleName(roleName);
			if (CollectionUtils.isEmpty(map)) {
				return null;
			}
			Role role = MapToRole(map);
			this.updateRoleCache(role);
			return role;
		}

	}

	@Override
	public void updateRoleStatus(String roleName, int status) {
		Role role = null;
		Object obj = roleCachedManager.get("role_"+DigestUtils.digest(roleName));
		if (obj != null) {
			role = (Role) obj;
			role.setStatus(status);
			this.updateRoleCache(role);
		} else {
			Map map = roleDao.getRoleByRoleName(roleName);
			role = MapToRole(map);
			if (role != null) {
				role.setStatus(status);
				this.updateRoleCache(role);
			}

		}

	}

	@Override
	public List<Role> getRolesByName(String roleName) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> maps = roleDao.getRolesByRoleName(roleName);
		List<Role> roles = MapToListRole(maps);
			if (!CollectionUtils.isEmpty(roles)) {
				Collections.sort(roles, new Comparator<Role>() {
				@Override
				public int compare(Role arg0, Role arg1) {
					return arg0.getLastLoginTime().compareTo(arg1.getLastLoginTime());
				}			
			});
			for (int i = 0; i < roles.size(); i++) {
				Role role = (Role) roleCachedManager.get("role_"+DigestUtils
						.digest(roles.get(i).getRoleName()));
				if (role != null) {
					roles.get(i).setCity(role.getCity());
					roles.get(i).setX(role.getX());
					roles.get(i).setY(role.getY());
					roles.get(i).setStatus(role.getStatus());
				}
			}

		}
		return roles;
	}

	@Override
	public Role getRoleById(Long roleId) {
		Map map = roleDao.getRoleById(roleId);
		Role role = MapToRole(map);
		if(role==null){
			return null;
		}
		return role;
	}

	public UserCardGroupManager getUserCardGroupManager() {
		return userCardGroupManager;
	}

	public void setUserCardGroupManager(
			UserCardGroupManager userCardGroupManager) {
		this.userCardGroupManager = userCardGroupManager;
	}

	public MemCachedManager getRoleCachedManager() {
		return roleCachedManager;
	}

	public void setRoleCachedManager(MemCachedManager roleCachedManager) {
		this.roleCachedManager = roleCachedManager;
	}

	public HeroLevelManager getHeroLevelManager() {
		return heroLevelManager;
	}

	public void setHeroLevelManager(HeroLevelManager heroLevelManager) {
		this.heroLevelManager = heroLevelManager;
	}

	public LocationManager getLocationManager() {
		return locationManager;
	}

	public void setLocationManager(LocationManager locationManager) {
		this.locationManager = locationManager;
	}

	@Override
	public List<Role> getRankCompetitors(int rank) {
		List<Map> list = roleDao.getRankCompetitors(rank);
		if (CollectionUtils.isEmpty(list)) {
			return null;
		}
		List<Role> roleList = new ArrayList<Role>();
		Role role;
		for (int i = list.size(); i > 0; i--) {
			roleList.add(MapToRole(list.get(i - 1)));
		}
		return roleList;
	}

	@Override
	public synchronized int swapRoleRank(Role sRole, Role fRole) {
		if(sRole.getRank()==1||fRole.getRank()==1){
			ChannelBuffer buffer = new DynamicChannelBuffer(200);
			buffer.writeInt(MessageType.SYSTEM.getCode());
			buffer.writeInt(MessageType.MATCHSTART.getCode());
			String message = "";
			if(sRole.getRank()==1){
				message = "恭喜"+fRole.getRoleName()+"成为新的独孤求败，称霸排行榜";
			}else{
				message = "恭喜"+sRole.getRoleName()+"成为新的独孤求败，称霸排行榜";
			}
			MessageUtil.writeString(buffer, message, "UTF-8");
			messageManager.sendMessageToAll(buffer);
		}
		int[] result = roleDao.updateRoleRank(sRole.getRoleId(), fRole
				.getRank(), fRole.getRoleId(), sRole.getRank());
		boolean flag = true;
		for (int i : result) {
			if (i < 0) {
				flag = false;
			}
		}
		if (flag) {
			Role role = (Role) roleCachedManager.get("role_"+DigestUtils.digest(sRole
					.getRoleName()));
			if (role != null) {
				role.setRank(fRole.getRank());
			}
			try {
				this.updateRoleCache(role);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
			role = (Role) roleCachedManager.get("role_"+DigestUtils.digest(fRole
					.getRoleName()));
			if (role != null) {
				role.setRank(sRole.getRank());
			}
			try {
				this.updateRoleCache(role);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
			return 1;
		} else {
			logger.error("更新排名失败");
			return 0;
		}

	}

	@Override
	public List<RankRole> getRankRoles(int rank) {
		// TODO Auto-generated method stub
		return MapToListRankRole(roleDao.getRankRoles(rank));
	}
	@Override
	public RankRole getRoleByRank(int rank) {
		// TODO Auto-generated method stub
		return MapToRankRole(roleDao.getRoleByRank(rank));
	}
	@Override
	public int getMaxRank() {
		// TODO Auto-generated method stub
		return roleDao.getMaxRank();
	}

	@Override
	public int updateRoleLastLoginAndLonginTimes(Date date, int times,
			Long roleId) {
		// TODO Auto-generated method stub
		if (roleDao.updateRoleLastLoginAndLonginTimes(date, times, roleId) > 0) {
			Role role = this.getRoleById(roleId);
			role = (Role) roleCachedManager.get("role_"+DigestUtils.digest(role
					.getRoleName()));
			try {
				role.setLastLoginTime(date);
				role.setLoginContinueTimes(times);
				this.updateRoleCache(role);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
			return 1;
		} else {
			return 0;
		}

	}

	@Override
	public Role getRoleByDigestRoleName(String roleName) {
		// TODO Auto-generated method stub
		return (Role) roleCachedManager.get("role_"+roleName);
	}

	@Override
	public boolean updateRoleCache(Role role) {
		try {
			Role zRole = this.getRoleById(role.getRoleId());
			if(zRole==null){
				return false;
			}
			zRole.setEnergyCanRecive(role.getEnergyCanRecive());
			zRole.setStatus(role.getStatus());
			return roleCachedManager.set("role_"+
					DigestUtils.digest(role.getRoleName()), 0, zRole);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	@Override
	public int updateRoleAvatar(Long roleId, int Id) {
		if (roleDao.updateRoleAvatar(roleId, Id) > 0) {
			Role role = this.getRoleById(roleId);
			role = (Role) roleCachedManager.get("role_"+DigestUtils.digest(role
					.getRoleName()));
			try {
				role.setAvatar(Id);
				this.updateRoleCache(role);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
			return 1;
		} else {
			return 0;
		}

	}

	public AchievementManager getAchievementManager() {
		return achievementManager;
	}

	public void setAchievementManager(AchievementManager achievementManager) {
		this.achievementManager = achievementManager;
	}

	@Override
	public Role RandomRoleByLevel(Long roleId) {
		Long id = roleDao.getSequence("t_cgiser_mokarole");
		Long roleRandom = (id - 1000000000L);
		int num = new Random().nextInt(roleRandom.intValue());
		Role role = MapToRole(roleDao.RandomRoleByLevel(1000000000L+num,roleId));
		if(role==null){
			role = MapToRole(roleDao.RandomRoleByLevel(1000000000L+num/2,roleId));
		}
		return role;
	}

	@Override
	public int updateRoleFriendApply(Long roleId, int num) {
		// TODO Auto-generated method stub
		if (roleDao.updateRoleFriendApplyNum(roleId, num) > 0) {
			Role role = this.getRoleById(roleId);
			role = (Role) roleCachedManager.get("role_"+DigestUtils.digest(role
					.getRoleName()));
			try {
				role.setFriendApplyNum(num);
				this.updateRoleCache(role);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public int updateRoleNewEmail(Long roleId, int num) {
		// TODO Auto-generated method stub
		if (roleDao.updateRoleNewMail(roleId, num) > 0) {
			Role role = this.getRoleById(roleId);
			role = (Role) roleCachedManager.get("role_"+DigestUtils.digest(role
					.getRoleName()));
			try {
				role.setNewEmail(num);
				this.updateRoleCache(role);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public int updateRoleSalaryCount(Long roleId, int num) {
		// TODO Auto-generated method stub
		if (roleDao.updateRoleSalaryCount(roleId, num) > 0) {
			Role role = this.getRoleById(roleId);
			try {
				role.setSalaryCount(num);
				this.updateRoleCache(role);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public int updateRoleMapLastInCome(Long roleId, Date date) {
		if (roleDao.updateRoleMapLastInCome(roleId, date) > 0) {
			Role role = this.getRoleById(roleId);
			this.updateRoleCache(role);
			return 1;
		}else{
			return 0;
		}
		
	}

	@Override
	public int upgradeVip(Long roleId,int vip) {
		Role role = this.getRoleById(roleId);
		if(role.getVip()<vip){
			roleDao.upgradeVip(roleId,vip);
			this.updateRoleCache(role);
			VipInfo info = vipInfoManager.getVipInfoByRoleId(roleId);
			if(info==null){
				vipInfoManager.saveVipInfo(roleId, vip);
			}else{
				vipInfoManager.updateVipInfo(roleId, vip);
			}
			if (vip == 1) {
				this.addFriendNumMax(roleId, 2);
				this.addTicket(role.getRoleName(), 2);
				userCardManager.saveUserCard(107, role.getRoleId());
			}
			if (vip == 2) {
				this.addFriendNumMax(roleId, 5);
				this.addEnergyMax(roleId, 5);
				this.addTicket(role.getRoleName(), 3);
				userCardManager.saveUserCard(86, role.getRoleId());
			}
			if (vip == 3) {
				this.addFriendNumMax(roleId, 10);
				this.addEnergyMax(roleId, 15);
				this.addTicket(role.getRoleName(), 5);
				userCardManager.saveUserCard(30, role.getRoleId());
			}
		}
		return 1;
	}

	@Override
	public int addEnergyMax(Long roleId, int eneryMax) {
		if (roleDao.addEnergyMax(roleId, eneryMax) > 0) {
			Role role = this.getRoleById(roleId);
			role = (Role) roleCachedManager.get("role_"+DigestUtils.digest(role
					.getRoleName()));
			try {
				role.setEnergyMax(role.getEnergyMax() + eneryMax);
				this.updateRoleCache(role);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				return 0;
			}
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public int addFriendNumMax(Long roleId, int friendNum) {
		if (roleDao.addFriendNumMax(roleId, friendNum) > 0) {
			Role role = this.getRoleById(roleId);
			role = (Role) roleCachedManager.get("role_"+DigestUtils.digest(role
					.getRoleName()));
			try {
				role.setFriendNumMax(role.getFriendNumMax() + friendNum);
				this.updateRoleCache(role);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				return 0;
			}
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public RankRole getFirstRank() {
		// TODO Auto-generated method stub
		return MapToRankRole(roleDao.getFirstRank());
	}

	@Override
	public int addLostTimes(Long roleId) {
		// TODO Auto-generated method stub
		if (roleDao.addLostTimes(roleId) > 0) {
			Role role = this.getRoleById(roleId);
			role = (Role) roleCachedManager.get("role_"+DigestUtils.digest(role
					.getRoleName()));
			try {
				role.setLose(role.getLose() + 1);
				this.updateRoleCache(role);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				return 0;
			}
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public int addRankLostTimes(Long roleId) {
		// TODO Auto-generated method stub
		if (roleDao.addRankLostTimes(roleId) > 0) {
			Role role = this.getRoleById(roleId);
			role = (Role) roleCachedManager.get("role_"+DigestUtils.digest(role
					.getRoleName()));
			try {
				role.setRankLost(role.getRankLost() + 1);
				this.updateRoleCache(role);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				return 0;
			}
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public int addRankWinTimes(Long roleId) {
		// TODO Auto-generated method stub
		if (roleDao.addRankWinTimes(roleId) > 0) {
			Role role = this.getRoleById(roleId);
			role = (Role) roleCachedManager.get("role_"+DigestUtils.digest(role
					.getRoleName()));
			try {
				role.setRankWin(role.getRankWin() + 1);
				this.updateRoleCache(role);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				return 0;
			}
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public int addWinTimes(Long roleId) {
		// TODO Auto-generated method stub
		if (roleDao.addWinTimes(roleId) > 0) {
			Role role = this.getRoleById(roleId);
			role = (Role) roleCachedManager.get("role_"+DigestUtils.digest(role
					.getRoleName()));
			try {
				role.setWin(role.getWin() + 1);
				this.updateRoleCache(role);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				return 0;
			}
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public int getRoleEffective(Long roleId) {
		Role role = this.getRoleById(roleId);
		int win = role.getWin();
		int lose = role.getLose();
		int rankWin = role.getRankWin();
		int rankLost = role.getRankLost();
		int base = 294;
		List<UserStage> userStages = userStageManager
				.getUserStageByRoleId(roleId);
		if (!CollectionUtils.isEmpty(userStages)) {
			for (UserStage userStage : userStages) {
				base += userStage.getMapId() * (294 / 55 / 3)
						* userStage.getFinishedStage();
			}
		}
		double winBase = 2 * Math.pow(rankWin, 0.9) + 3
				* Math.pow((win - rankWin), 0.9);
		double winRatio = 0;
		if ((win - rankWin) + (lose - rankLost) > 0) {
			winRatio = 294 * Math.pow(
					((win - rankWin) / ((win - rankWin) + (lose - rankLost))),
					0.9);
		}

		Double max = base + winBase + winRatio;
		return max.intValue();
	}

	public UserStageManager getUserStageManager() {
		return userStageManager;
	}

	public void setUserStageManager(UserStageManager userStageManager) {
		this.userStageManager = userStageManager;
	}

	public static void main(String[] args) {
		// BigDecimal bd = new BigDecimal("1.000000102E9");
		// System.out.println(bd.toPlainString());
		// System.out.println(Long.parseLong(bd.toPlainString()));
		System.out.println(DigestUtils.digest("100000000001"));
		System.out.println(2 * Math.pow(9, 0.9) + 3 * Math.pow(10, 0.9));
	}

	@Override
	public int updateFreshStep(int type, int value, Long roleId) {

		Role role = this.getRoleById(roleId);
		String[] steps = role.getFreshStep();
		if (steps == null || steps.length != 15) {
			steps = new String[15];
			for (int i = 0; i < steps.length; i++) {
				steps[i] = "1";
			}
		}
		steps[type] = String.valueOf(value);
		StringBuffer str = new StringBuffer();
		for (String s : steps) {
			str.append(s);
			str.append(",");
		}
		String strStep = str.substring(0, str.length() - 1);
		if (roleDao.updateFreshStep(strStep, roleId) > 0) {
			role = (Role) roleCachedManager.get("role_"+DigestUtils.digest(role
					.getRoleName()));
			try {
				role.setFreshStep(steps);
				this.updateRoleCache(role);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				return 0;
			}
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public int updateRoleCity(Long roleId, String city) {
		if (roleDao.updateRoleCity(roleId, city) > 0) {
			Role role = this.getRoleById(roleId);
			role = (Role) roleCachedManager.get("role_"+DigestUtils.digest(role
					.getRoleName()));
			try {
				role.setCity(city);
				this.updateRoleCache(role);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				return 0;
			}
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public List<Role> getRoleByCity(String city, int num) {
		// TODO Auto-generated method stub
		List<Role> roleList =  MapToListRole(roleDao.getRoleByCity(city, num));
		if (!CollectionUtils.isEmpty(roleList)) {
			for (int i = 0; i < roleList.size(); i++) {
				Role role = (Role) roleCachedManager.get("role_"+DigestUtils
						.digest(roleList.get(i).getRoleName()));
				if (role != null) {
					roleList.get(i).setX(role.getX());
					roleList.get(i).setY(role.getY());
					roleList.get(i).setStatus(role.getStatus());
				}
			}
		}
		return roleList;
	}

	@Override
	public List<Role> getAllRoles(int status) {
		// TODO Auto-generated method stub
		return MapToListRole(roleDao.getAllRoles(status));
	}

	public UserGameFacade getUserGameFacade() {
		return userGameFacade;
	}

	public void setUserGameFacade(UserGameFacade userGameFacade) {
		this.userGameFacade = userGameFacade;
	}

	public UserCardManager getUserCardManager() {
		return userCardManager;
	}

	public void setUserCardManager(UserCardManager userCardManager) {
		this.userCardManager = userCardManager;
	}

	@Override
	public User getUserByUserIden(String userIden) {
		// TODO Auto-generated method stub
		return userGameFacade.getUserByUserIden(userIden);
	}

	@Override
	public int getRankByRoleId(Long roleId) {
		Map map = roleDao.getRankByRoleId(roleId);
		if(CollectionUtils.isEmpty(map)){
			logger.error("获取排名失败");
		}
		return Integer.parseInt(map.get("RANK").toString());
	}

	@Override
	public int updateRoleFreeFightTime(Long roleId, Date date) {
		if(roleDao.updateRoleFreeFightTime(roleId, date)>0){
			Role role = this.getRoleById(roleId);
			this.updateRoleCache(role);
			return 1;
		}
		return 0;
	}

	@Override
	public int updateRoleLastReceiveEnergy(Long roleId, Date date) {
		if(roleDao.updateRoleLastReceiveEnergy(roleId, date)>0){
			Role role = this.getRoleById(roleId);
			this.updateRoleCache(role);
			return 1;
		}
		return 0;
	}

	@Override
	public int updateRoleSendEnergyTimes(Long roleId, int num) {
		if(roleDao.updateRoleSendEnergyTimes(roleId, num)>0){
			Role role = this.getRoleById(roleId);
			this.updateRoleCache(role);
			return 1;
		}
		return 0;
	}

	@Override
	public int updateRoleSoulRefreshTime(Long roleId, Date date) {
		if(roleDao.updateRoleSoulRefreshTime(roleId, date)>0){
			Role role = this.getRoleById(roleId);
			this.updateRoleCache(role);
			return 1;
		}
		return 0;
	}

	@Override
	public int updateRoleSoul(Long roleId, String souls) {
		if(roleDao.updateRoleSoul(roleId, souls)>0){
			Role role = this.getRoleById(roleId);
			this.updateRoleCache(role);
			return 1;
		}
		return 0;
	}
	@Override
	public int getRoleBuyGoodsTime(Long roleId) {
		// TODO Auto-generated method stub
		return roleDao.getRoleBuyGoodTimes(roleId);
	}

	@Override
	public int updateRoleBuyGoodsTime(Long roleId, int times) {
		// TODO Auto-generated method stub
		return roleDao.updateRoleBuyGoodTimes(roleId, times);
	}

	public SoulManager getSoulManager() {
		return soulManager;
	}

	public void setSoulManager(SoulManager soulManager) {
		this.soulManager = soulManager;
	}

	public VipInfoManager getVipInfoManager() {
		return vipInfoManager;
	}

	public void setVipInfoManager(VipInfoManager vipInfoManager) {
		this.vipInfoManager = vipInfoManager;
	}

	public MessageManager getMessageManager() {
		return messageManager;
	}

	public void setMessageManager(MessageManager messageManager) {
		this.messageManager = messageManager;
	}

	public SalaryManager getSalaryManager() {
		return salaryManager;
	}

	public void setSalaryManager(SalaryManager salaryManager) {
		this.salaryManager = salaryManager;
	}
}

package com.cgiser.moka.manager;

import java.util.Date;
import java.util.List;

import com.cgiser.moka.data.RoleDo;
import com.cgiser.moka.model.RankRole;
import com.cgiser.moka.model.Role;
import com.cgiser.sso.model.User;

public interface RoleManager {
	/**
	 * 根据用户加密ID获取用户创建的角色
	 * @param userIden
	 * @param serverId 可以不传，留着后期使用
	 * @return
	 */
	public List<Role> getRolesByUserIden(String userIden,Long serverId);
	/**
	 * 保存角色
	 * @param role
	 * @return
	 */
	public Long saveRole(RoleDo role);
	/**
	 * 根据角色名获取角色
	 * @param roleName
	 * @return
	 */
	public Role getRoleByName(String roleName);
	/**
	 * 根据角色ID获取角色
	 * @param roleName
	 * @return
	 */
	public Role getRoleById(Long roleId);
	/**
	 * 根据角色名模糊查询角色
	 * @param roleName
	 * @return
	 */
	public List<Role> getRolesByName(String roleName);
	/**
	 * 减掉用户消耗的元宝
	 * @param roleName
	 * @param cash
	 * @return
	 */
	public boolean updateCash(String roleName,int cash);
	/**
	 * 减掉用户消耗的魔幻券
	 * @param roleName
	 * @param ticket
	 * @return
	 */
	public boolean updateTicket(String roleName,int ticket);
	/**
	 * 给用户增加魔幻券
	 * @param roleName
	 * @param ticket
	 * @return
	 */
	public boolean addTicket(String roleName,int ticket);
	/**
	 * 减掉用户消耗的铜钱数
	 * @param roleName
	 * @param coin
	 * @return
	 */
	public boolean updateCoin(String roleName,int coin);
	/**
	 * 给用户增加铜钱
	 * @param roleName
	 * @param coin
	 * @return
	 */
	public boolean addCoin(String roleName,int coin);
	/**
	 * 给用户增加元宝
	 * @param roleName
	 * @param cash
	 * @return
	 */
	public boolean addCash(String roleName,int cash);
	/**
	 * 给用户增加经验
	 * @param roleName
	 * @param cash
	 * @return
	 */
	public boolean addExp(String roleName,int exp);
	/**
	 * 给用户增加体力
	 * @param roleName
	 * @param cash
	 * @return
	 */
	public boolean addEnergy(String roleName,int energy);
	/**
	 * 给用户减少体力
	 * @param roleName
	 * @param cash
	 * @return
	 */
	public boolean updateEnergy(String roleName,int energy);
	/**
	 * 给用户增加荣誉点
	 * @param roleId
	 * @param honor
	 * @return
	 */
	public boolean addHonor(Long roleId,int honor);
	/**
	 * 给用户减少荣誉点
	 * @param roleId
	 * @param honor
	 * @return
	 */
	public boolean updateHonor(Long roleId,int honor);
	/**
	 * 给用户增加好友上线
	 * @param roleId
	 * @param friendNum
	 * @return
	 */
	public int addFriendNumMax(Long roleId,int friendNum);
	/**
	 * 给用户增加行动力上线
	 * @param roleId
	 * @param friendNum
	 * @return
	 */
	public int addEnergyMax(Long roleId,int eneryMax);
	/**
	 * 创建角色
	 * @param userIden
	 * @param serverId
	 * @param roleName
	 * @return
	 */
	public Long createRole(String gameIden,String userIden, String serverId, String roleName,String invitCode,int sex);
	/**
	 * 修改用户角色Id
	 * @param groupId
	 * @return
	 */
	public int updateRoleDefaultGroup(Long roleId,Long groupId);
	/**
	 * 更新角色最后登录时间和连续登录次数
	 * @param date
	 * @param times
	 * @return
	 */
	public int updateRoleLastLoginAndLonginTimes(Date date,int times,Long roleId);
	/**
	 * 修改角色状态
	 * @param roleName
	 * @param status
	 */
	public void updateRoleStatus(String roleName,int status);
	/**
	 * 修改玩家头像
	 * @param roleName
	 * @param status
	 */
	public int updateRoleAvatar(Long roleId,int Id);
	/**
	 * 修改玩家新的Email数量
	 * @param roleName
	 * @param status
	 */
	public int updateRoleNewEmail(Long roleId,int num);
	/**
	 * 修改玩家新的Email数量
	 * @param roleName
	 * @param status
	 */
	public int updateRoleMapLastInCome(Long roleId,Date date);
	/**朋友邀请数量
	 * @param roleName
	 * @param status
	 */
	public int updateRoleFriendApply(Long roleId,int num);
	/**
	 * 修改玩家奖励数量
	 * @param roleName
	 * @param status
	 */
	public int updateRoleSalaryCount(Long roleId,int num);
	/**
	 * 获取玩家排名前的5个玩家
	 * @param rank
	 * @return
	 */
	public List<Role> getRankCompetitors(int rank);
	/**
	 * 排名战结束后，交换玩家排名
	 * @param sRole
	 * @param fRole
	 * @return
	 */
	public int swapRoleRank(Role sRole,Role fRole);
	/**
	 * 获取排名
	 * @return
	 */
	public List<RankRole> getRankRoles(int rank);
	/**
	 * 根据排名获取玩家
	 * @param rank
	 * @return
	 */
	public RankRole getRoleByRank(int rank);
	public int getMaxRank();
	public Role getRoleByDigestRoleName(String roleName);
	public boolean updateRoleCache(Role role);
	/**
	 * 角色升级到指定vip
	 * @param roleId
	 * @param vip
	 * @return
	 */
	public int upgradeVip(Long roleId,int vip);
	/**
	 * 根据排名随机一个角色
	 * @param level
	 * @return
	 */
	public Role RandomRoleByLevel(Long roleId);
	public RankRole getFirstRank();
	public int addWinTimes(Long roleId);
	public int addLostTimes(Long roleId);
	public int addRankWinTimes(Long roleId);
	public int addRankLostTimes(Long roleId);
	/**
	 * 获取角色排名（从数据库获取）
	 * @param roleId
	 * @return
	 */
	public int getRankByRoleId(Long roleId);
	/**
	 * 获取玩家战斗力
	 * @param roleId
	 * @return
	 */
	public int getRoleEffective(Long roleId);
	/**
	 * 保存玩家引导
	 * @param type
	 * @param value
	 * @return
	 */
	public int updateFreshStep(int type,int value,Long roleId);
	/**
	 * 保存玩家所在城市
	 * @param roleId
	 * @param city
	 * @return
	 */
	public int updateRoleCity(Long roleId,String city);
	/**
	 * 获取这个城市的人
	 * @param roleId
	 * @param city
	 * @return
	 */
	public List<Role> getRoleByCity(String city,int num);
	/**
	 * 获取所有状态为正常的角色
	 * @param roleId
	 * @param city
	 * @return
	 */
	public List<Role> getAllRoles(int status);
	/**
	 * 获取角色所属的账号
	 * @param userIden
	 * @return
	 */
	public User getUserByUserIden(String userIden);
	/**
	 * 更新武器刷新时间
	 * @param roleId
	 * @param date
	 * @return
	 */
	public int updateRoleSoulRefreshTime(Long roleId, Date date);
	/**
	 * 更新武器
	 * @param roleId
	 * @param souls
	 * @return
	 */
	public int updateRoleSoul(Long roleId, String souls);
	/**
	 * 更新领取体力时间
	 * @param roleId
	 * @param date
	 * @return
	 */
	public int updateRoleLastReceiveEnergy(Long roleId, Date date);
	/**
	 * 更新自由切磋时间
	 * @param roleId
	 * @param date
	 * @return
	 */
	public int updateRoleFreeFightTime(Long roleId, Date date) ;
	/**
	 * 更新赠送体力次数
	 * @param roleId
	 * @param num
	 * @return
	 */
	public int updateRoleSendEnergyTimes(Long roleId,int num);
	/**
	 * 获取用户抽卡包次数
	 * @param roleId
	 * @return
	 */
	public int getRoleBuyGoodsTime(Long roleId);
	/**
	 * 修改用户抽卡包次数
	 * @param roleId
	 * @param times
	 * @return
	 */
	public int updateRoleBuyGoodsTime(Long roleId,int times);
}

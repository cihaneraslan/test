package com.cgiser.moka.manager.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.DynamicChannelBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.cgiser.moka.dao.AchievementDao;
import com.cgiser.moka.manager.AchievementManager;
import com.cgiser.moka.manager.MessageManager;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.manager.SalaryManager;
import com.cgiser.moka.message.netty.MessageUtil;
import com.cgiser.moka.model.Achievement;
import com.cgiser.moka.model.MessageType;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.model.SalaryEnum;
import com.cgiser.moka.model.UserAchievement;

public class AchievementManagerImpl implements AchievementManager{
	private static final Logger logger = LoggerFactory.getLogger(AchievementManagerImpl.class);
	private AchievementDao achievementDao;
	private SalaryManager salaryManager;
	private MessageManager messageManager;
	private RoleManager roleManager;
//	@Override
	public List<Achievement> getAllAchievements() {
		return MapListToAchievementList(achievementDao.getAchievements());
	}
	
	@Override
	public Achievement getAchievementById(int id) {
		// TODO Auto-generated method stub
		return MapToAchievement(achievementDao.getAchievementById(id, 1));
	}
	@Override
	public Long saveUserAchievement(int achievementId, Long roleId,int finishState) {
		Achievement achievement = this.getAchievementById(achievementId);
		if(achievement==null){
			return 0L;
		}
		if(finishState>=achievement.getFinishNum()){
			if(salaryManager.extendSalary(achievement.getType(), achievement.getValue(), new Date(), SalaryEnum.AchieveSalary,achievement.getName(), roleId)<1){
				logger.error("extendSalary ByAchievement Error role:"+roleId+achievement);
			}
			Role role = roleManager.getRoleById(roleId);
			ChannelBuffer buffer = new DynamicChannelBuffer(200);
			buffer.writeInt(MessageType.SYSTEM.getCode());
			buffer.writeInt(MessageType.NEWACHIEVEMENT.getCode());
			MessageUtil.writeString(buffer, achievement.getName(), "UTF-8");
			messageManager.sendMessageToRole(role.getRoleName(), buffer);
			ChannelBuffer buffer1 = new DynamicChannelBuffer(200);
			buffer1.writeInt(MessageType.SYSTEM.getCode());
			buffer1.writeInt(MessageType.ACHIEVEMENT.getCode());
			MessageUtil.writeString(buffer1, "恭喜<a href=\"event:user_" +role.getRoleName()+ "\"><u>[" + role.getRoleName() + "]</u></a>达到["+achievement.getName()+"]成就,获得"+achievement.getDescByType(), "UTF-8");
			messageManager.sendMessageToAll(buffer1);
		}
		UserAchievement userAchievement = this.getUserAchievementById(roleId, achievementId);
		if(userAchievement!=null){
			if(achievementDao.updateUserAchievement(achievementId, roleId, finishState)>0){
				return userAchievement.getUserAchievementId();
			}else{
				return 0L;
			}
		}
		
		return achievementDao.saveUserAchievement(achievementId, roleId,finishState);
	}
	@Override
	public List<UserAchievement> getUserAchievements(Long roleId) {
		// TODO Auto-generated method stub
		return MapListToUserAchievementList(achievementDao.getUserAchievement(roleId));
	}
	private List<Achievement> MapListToAchievementList(List<Map<String, Object>> list){
		if(CollectionUtils.isEmpty(list)){
			return null;
		}
		List<Achievement> listChip = new ArrayList<Achievement>();
		for(int i=0;i<list.size();i++){
			listChip.add(MapToAchievement(list.get(i)));
		}
		return listChip;
	}
	private Achievement MapToAchievement(Map<String,Object> map){
		if(CollectionUtils.isEmpty(map)){
			return null;
		}
		Achievement achievement = new Achievement();
		achievement.setAchievementId(Integer.parseInt(map.get("ACHIEVEMENTID").toString()));
		achievement.setDesc((String)map.get("DESC"));
		achievement.setStatus(Integer.parseInt(map.get("STATUS").toString()));
		achievement.setType(Integer.parseInt(map.get("TYPE").toString()));
		achievement.setValue(Integer.parseInt(map.get("VALUE").toString()));
		achievement.setFinishNum(Integer.parseInt(map.get("FINISHNUM").toString()));
		achievement.setName(map.get("NAME").toString());
		return achievement;
	}
	private List<UserAchievement> MapListToUserAchievementList(List<Map<String, Object>> list){
		if(CollectionUtils.isEmpty(list)){
			return null;
		}
		List<UserAchievement> listAchievement = new ArrayList<UserAchievement>();
		for(int i=0;i<list.size();i++){
			listAchievement.add(MapToUserAchievement(list.get(i)));
		}
		return listAchievement;
	}
	private UserAchievement MapToUserAchievement(Map<String,Object> map){
		if(CollectionUtils.isEmpty(map)){
			return null;
		}
		UserAchievement userAchievement = new UserAchievement();
		userAchievement.setAchievementId(Integer.parseInt(map.get("ACHIEVEMENTID").toString()));
		userAchievement.setCreateTime(new Date(((Timestamp)map.get("CREATETIME")).getTime()));
		userAchievement.setRoleId(new Long(map.get("ROLEID").toString()));
		userAchievement.setUserAchievementId(new Long(map.get("USERACHIEVEMENTID").toString()));
		userAchievement.setFinishState(Integer.parseInt(map.get("FINISHSTATE").toString()));
		return userAchievement;
	}
	public AchievementDao getAchievementDao() {
		return achievementDao;
	}
	public void setAchievementDao(AchievementDao achievementDao) {
		this.achievementDao = achievementDao;
	}

	@Override
	public UserAchievement getUserAchievementById(Long roleId,
			int achievementId) {
		// TODO Auto-generated method stub
		return MapToUserAchievement(achievementDao.getUserAchievementById(roleId, achievementId));
	}

	public SalaryManager getSalaryManager() {
		return salaryManager;
	}

	public void setSalaryManager(SalaryManager salaryManager) {
		this.salaryManager = salaryManager;
	}

	public MessageManager getMessageManager() {
		return messageManager;
	}

	public void setMessageManager(MessageManager messageManager) {
		this.messageManager = messageManager;
	}

	public RoleManager getRoleManager() {
		return roleManager;
	}

	public void setRoleManager(RoleManager roleManager) {
		this.roleManager = roleManager;
	}


}

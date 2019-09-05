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

import com.cgiser.moka.dao.LegionFightDao;
import com.cgiser.moka.manager.LegionFightManager;
import com.cgiser.moka.manager.LegionManager;
import com.cgiser.moka.manager.MessageManager;
import com.cgiser.moka.manager.PushNotificationManager;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.manager.support.BeanUtils;
import com.cgiser.moka.message.netty.MessageUtil;
import com.cgiser.moka.model.Legion;
import com.cgiser.moka.model.LegionContext;
import com.cgiser.moka.model.LegionEventEnum;
import com.cgiser.moka.model.LegionFight;
import com.cgiser.moka.model.Legioner;
import com.cgiser.moka.model.MessageType;
import com.cgiser.moka.model.Role;

public class LegionFightManagerImpl implements LegionFightManager {
	private LegionFightDao legionFightDao;
	private LegionManager legionManager;
	private MessageManager messageManager;
	private RoleManager roleManager;
	private PushNotificationManager pushNotificationManager;
	Logger logger = LoggerFactory.getLogger("leionfight");
//	@Override
//	public Long bidLegionFight(String name, String legionName, int coins) {
//		Legion legion = legionManager.getLegionByName(legionName);
//		if(legionManager.delLegionResources(legion.getId(), coins)>0){
//			return legionFightDao.bidLegionFight(name, legionName, coins);
//		}else{
//			return 0L;
//		}
//		
//	}
//
//	@Override
//	public LegionCity getLegionCityInfo(String name) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public void startLegionFight(String name) {
//		//初始化战斗数据
//		List<Legion> legions = this.getMaxBidLegion(name);
//
//		
//		LegionCity legionCity = (LegionCity)groupCachedManager.get(DigestUtils.digest(name));
//		if(legionCity!=null){
//			legionCity.setAttackLegion(legions.get(0));
//			legionCity.setDefendLegion(legions.get(1));
//		}else{
//			legionCity = new LegionCity();
//			legionCity.setAttackLegion(legions.get(0));
//			legionCity.setDefendLegion(legions.get(1));
//			legionCity.setName(name);
//		}
//		try {
//			groupCachedManager.set(DigestUtils.digest(name), 0, legionCity);
//		} catch (Exception e) {
//			return;
//		}
//		
//		//
//		//发送socket消息到客户端通知玩家进入军团战
//		List<Legioner> aLegioners = legionManager.getLegioner(legions.get(0).getId(), 1, 80);
//		List<Legioner> dLegioners = legionManager.getLegioner(legions.get(1).getId(), 1, 80);
//		for(int i=0;i<aLegioners.size();i++){
//			ChannelBuffer buffer = new DynamicChannelBuffer(200);
//			buffer.writeInt(MessageType.LEGION.getCode());
//			buffer.writeInt(2);
//			messageManager.sendMessageToRole(aLegioners.get(i).getNickName(), buffer);
//		}
//		for(int i=0;i<dLegioners.size();i++){
//			ChannelBuffer buffer = new DynamicChannelBuffer(200);
//			buffer.writeInt(MessageType.LEGION.getCode());
//			buffer.writeInt(2);
//			messageManager.sendMessageToRole(dLegioners.get(i).getNickName(), buffer);
//		}
//	}
//	@Override
//	public LegionCity inLegionFight(String name,Long roleId, String roleName) {
//		
//		lock.lock();
//		LegionCity legionCity = (LegionCity)groupCachedManager.get(DigestUtils.digest(name));
//		try {
//			if(legionCity==null){
//				return null;
//			}
//			Long aLegionId = legionCity.getAttackLegion().getId();
//			Long dLegionId = legionCity.getDefendLegion().getId();
//			if(this.isAttackOrDefend(roleId, aLegionId, dLegionId)==1){
//				for(int i=0;i<legionCity.getAttackers().size();i++){
//					if(roleName.equals(legionCity.getAttackers().get(i).getRoleName())){
//						return legionCity;
//					}
//				}
//				LegionFighter legionFighter = new LegionFighter();
//				legionFighter.setId(legionCity.getAttackers().size());
//				legionFighter.setRoleId(roleId);
//				legionFighter.setRoleName(roleName);
//				legionFighter.setScore(10);
//				legionCity.getAttackers().add(legionFighter);
//			}else if(this.isAttackOrDefend(roleId, aLegionId, dLegionId)==2){
//				for(int i=0;i<legionCity.getDefenders().size();i++){
//					if(roleName.equals(legionCity.getDefenders().get(i).getRoleName())){
//						return legionCity;
//					}
//				}
//				LegionFighter legionFighter = new LegionFighter();
//				legionFighter.setId(legionCity.getDefenders().size());
//				legionFighter.setRoleId(roleId);
//				legionFighter.setRoleName(roleName);
//				legionFighter.setScore(10);
//				legionCity.getDefenders().add(legionFighter);
//			}else{
//				return legionCity;
//			}
//			groupCachedManager.set(DigestUtils.digest(name), 0, legionCity);
//		} catch (Exception e) {
//			return null;
//		}finally{
//			lock.unlock();
//		}
//		return legionCity;
//	}
//	@Override
//	public LegionFighter getDefend(String city, Role role) {
//		fightLock.lock();
//		try {
//			LegionCity legionCity = (LegionCity)groupCachedManager.get(DigestUtils.digest(city));
//			if(legionCity==null){
//				return null;
//			}
//			Long aLegionId = legionCity.getAttackLegion().getId();
//			Long dLegionId = legionCity.getDefendLegion().getId();
//			if(this.isAttackOrDefend(role.getRoleId(), aLegionId, dLegionId)==1){
//				return this.getLegionFighter(role.getRoleName(),legionCity.getDefenders());
//			}else if(this.isAttackOrDefend(role.getRoleId(), aLegionId, dLegionId)==2){
//				return this.getLegionFighter(role.getRoleName(),legionCity.getAttackers());
//			}else{
//				return null;
//			}
//		} catch (Exception e) {
//			return null;
//		}finally{
//			fightLock.unlock();
//		}
//	}
//	private LegionFighter getLegionFighter(String attacker,List<LegionFighter> defenders){
//		if(CollectionUtils.isEmpty(defenders)){
//			return null;
//		}
//		LegionFighter legionFighter1=null;
//		LegionFighter legionFighter;
//		Role role;
//		for(int i=0;i<defenders.size();i++){
//			legionFighter = defenders.get(i);
//			role = roleManager.getRoleByName(legionFighter.getRoleName());
//			if(role.getStatus()!=4&&legionFighter.getScore()>1&&legionFighter.getDefendName()!=null){
//				legionFighter1 = legionFighter;
//			}
//			if(role.getStatus()==4&&legionFighter.getDefendName().equals(attacker)&&legionFighter.getScore()>1){
//				return legionFighter;
//			}
//		}
//		return legionFighter1;
//	}
//	private int isAttackOrDefend(Long roleId,Long aLegionId,Long dLegionId){
//		Legioner legioner = legionManager.getLegioner(roleId);
//		if(legioner.getLegionId().equals(aLegionId)){
//			return 1;
//		}else if(legioner.getLegionId().equals(dLegionId)){
//			return 2;
//		}else{
//			return 0;
//		}
//	}
//	@Override
//	public List<Legion> getMaxBidLegion(String name) {
//		List<Map<String,Object>> list = legionFightDao.getMaxBidLegion(name);
//		if(CollectionUtils.isEmpty(list)){
//			return null;
//		}
//		List<Legion> legions = new ArrayList<Legion>();
//		for(int i=0;i<list.size();i++){
//			legions.add(legionManager.getLegionByName(list.get(i).get("LEGIONNAME").toString()));
//		}
//		
//		return legions;
//	}
//	
//	@Override
//	public List<LegionCity> getLegionCityInfos() {
//		// TODO Auto-generated method stub
//		return MapToListLegionCity(legionFightDao.getLegionFightCityInfos());
//	}
//	private List<LegionCity> MapToListLegionCity(List<Map<String,Object>> list){
//		if(CollectionUtils.isEmpty(list)){
//			return null;
//		}
//		List<LegionCity> legionCitys = new ArrayList<LegionCity>();
//		for(int i=0;i<list.size();i++){
//			legionCitys.add(MapToLegionCity(list.get(i)));
//		}
//		return legionCitys;
//	}
//	private LegionCity MapToLegionCity(Map map){
//		if(CollectionUtils.isEmpty(map)){
//			return null;
//		}
//		LegionCity legionCity = new LegionCity();
//		//legionCity.setAttackers(attackers);
//		legionCity.setId(Integer.parseInt(map.get("ID").toString()));
//		legionCity.setName(map.get("NAME").toString());
//		legionCity.setLegionName(map.get("LEGIONNAME").toString());
//		legionCity.setCoins(Integer.parseInt(map.get("COINS").toString()));
//		legionCity.setBidTime(((Timestamp)map.get("BIDTIME")));
//		legionCity.setNextStartTime((Timestamp)map.get("NEXTSTARTTIME"));
//		return legionCity;
//	}
//
	public LegionFightDao getLegionFightDao() {
		return legionFightDao;
	}

	public void setLegionFightDao(LegionFightDao legionFightDao) {
		this.legionFightDao = legionFightDao;
	}

	public LegionManager getLegionManager() {
		return legionManager;
	}

	public void setLegionManager(LegionManager legionManager) {
		this.legionManager = legionManager;
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
//
//	@Override
//	public BidLegion getBidLegion(String cityName, String legionName) {
//		return MapToBidLegion(legionFightDao.getBidLegion(cityName, legionName));
//	}
//
//	@Override
//	public List<BidLegion> getBidLegions(String name) {
//		return MapToListBidLegion(legionFightDao.getBidLegions(name));
//	}
//	private BidLegion MapToBidLegion(Map map){
//		if(CollectionUtils.isEmpty(map)){
//			return null;
//		}
//		BidLegion bidLegion = new BidLegion();
//		bidLegion.setCityName(map.get("CITYNAME").toString());
//		bidLegion.setCoins(Integer.parseInt(map.get("COINS").toString()));
//		bidLegion.setId(Integer.parseInt(map.get("ID").toString()));
//		bidLegion.setLegionName(map.get("LEGIONNAME").toString());
//		bidLegion.setState(Integer.parseInt(map.get("STATE").toString()));
//		return bidLegion;
//	}
//	private List<BidLegion> MapToListBidLegion(List<Map<String,Object>> list){
//		if(CollectionUtils.isEmpty(list)){
//			return null;
//		}
//		List<BidLegion> bidLegions = new ArrayList<BidLegion>();
//		for(int i=0;i<list.size();i++){
//			bidLegions.add(MapToBidLegion(list.get(i)));
//		}
//		return bidLegions;
//	}

	@Override
	public Long addLegionFight(final Long roleId, final Long attack, Long defend, Date date) {
		final Legion legion = legionManager.getLegionById(defend);
		LegionFight legionFight = this.getLegionFightBydefend(defend);
		if(legionFight!=null&&!this.isEnd(legionFight)){
			return 0L;
		}
		legionFight = this.getLegionFight(roleId);
		if(legionFight!=null&&!this.isEnd(legionFight)){
			return 0L;
		}
		String guard1 = legion.getGuard1()+"_0_0";
		String guard2 = legion.getGuard2()+"_0_0";
		String guard3 = legion.getGuard3()+"_0_0";
		String guard4 = legion.getGuard4()+"_0_0";
		String guard5 = legion.getGuard5()+"_0_0";
		String guard6 = legion.getGuard6()+"_0_0";
		String guard7 = legion.getGuard7()+"_0_0";
		String guard8 = legion.getGuard8()+"_0_0";
		String guard9 = legion.getGuard9()+"_0_0";
		
		Long id = legionFightDao.saveLegionFight(roleId, attack, defend,date,guard1,guard2,guard3,guard4,guard5,guard6,guard7,guard8,guard9,legion.getLegionLevel(),1);
		if(id>0){
			new Runnable() {
				public void run() {
					try{
						List<Legioner> legioners = legionManager.getLegioner(attack, 1, 80);
						if(!CollectionUtils.isEmpty(legioners)){
							final Role header = roleManager.getRoleById(roleId);
							Role role;
							if(header!=null){
								for(Legioner legioner:legioners){
									role = roleManager.getRoleById(legioner.getRoleId());
									if(role==null){
										continue;
									}
									ChannelBuffer buffer = new DynamicChannelBuffer(200);
									buffer.writeInt(MessageType.LEGION.getCode());
									MessageUtil.writeString(buffer,header.getRoleName(), "UTF-8");
									MessageUtil.writeString(buffer, "亲:"+header.getRoleName()+"帮主召唤您去参加帮派战，赶紧去战斗吧！", "UTF-8");
									messageManager.sendMessageToRole(role.getRoleName(), buffer);
									pushNotificationManager.pushNotification(role.getRoleId(), "", "亲，"+header.getRoleName()+"帮主召唤您去参加帮派战，赶紧去战斗吧！");
								}
							}

						}
					}catch (Exception e) {
						logger.error(e.getMessage(),e);
					}
					
				}
			}.run();
			

			
		}
		return id;
	}
	@Override
	public int defeatGuard(Long legionFightId, int id,Long roleId) {
		LegionFight legionFight = this.getLegionFightById(legionFightId);
		String guardInfo = (String)BeanUtils.getFieldValueByName("guard"+id, legionFight);
		return legionFightDao.updateLegionFightGuardState(legionFightId, id, new Long(guardInfo.split("_")[0]), roleId);
	}
	@Override
	public LegionFight getLegionFightById(Long id) {
		// TODO Auto-generated method stub
		return MapToLegionFight(legionFightDao.getLegionFightById(id));
	}
	@Override
	public LegionFight getLegionFight(Long roleId) {
		Legioner legioner = legionManager.getLegioner(roleId);
		if(legioner==null){
			return null;
		}
		Legion legion = legionManager.getLegionById(legioner.getLegionId());
		if(legion==null){
			return null;
		}
		LegionFight legionFight = this.getLegionFightByattack(legion.getId());
		if(this.isEnd(legionFight)){
			return null;
		}
		return legionFight;
	}

	@Override
	public boolean isEnd(LegionFight legionFight){
		if(legionFight==null){
			return true;
		}
		if(System.currentTimeMillis()-legionFight.getTime().getTime()>20*60*1000){
			if(legionFight.getEnd()==0){
				legionFightDao.updateLegionState(legionFight.getId());
				Legion attack = legionManager.getLegionById(legionFight.getAttack());
				Legion defend = legionManager.getLegionById(legionFight.getDefend());
				if(this.isWin(legionFight)>0){
					legionManager.addLegionRobTimes(legionFight.getDefend());
					int res = (defend.getResources().intValue()/5)*getWinResource(legionFight)/100;
					legionManager.delLegionResources(defend.getId(), res);
					legionManager.addLegionResources(attack.getId(), res);
					this.updateLegionFightWinResource(legionFight.getId(), 1,new Long(res));
					legionManager.saveLegionEvent(legionFight.getAttack(), legionFight.getStartId(), "我帮成功攻下了"+defend.getName()+"帮派的城池，夺取资产"+res+"[<a href=\"event:legionfight_"+legionFight.getId()+"\">查看战况</a>]", LegionEventEnum.STARTFIGHTATTACK.getCode());
					legionManager.saveLegionEvent(legionFight.getDefend(), legionFight.getStartId(), attack.getName()+"攻打了我们 [失守]，掠去"+res+"资产，帮派损失惨重[<a href=\"event:legionfight_"+legionFight.getId()+"\">查看战况</a>]", LegionEventEnum.STARTFIGHTDEFEND.getCode());
					//通知被入侵的帮派
					ChannelBuffer responseBuffer = new DynamicChannelBuffer(200);
					responseBuffer.writeInt(1003);
					MessageUtil.writeString(responseBuffer, defend.getName(), "UTF-8");
					MessageUtil.writeString(responseBuffer, "["+attack.getName()+"]攻打了我们，抢走了我们"+res+"资产，损失惨重啊，要报仇的跟我来啊！！！", "UTF-8");
					List<Legioner> legioners = legionManager.getLegioner(defend.getId(), 1, 80);
					Role toRole;
					for (int i = 0; i < legioners.size(); i++) {
						toRole = roleManager.getRoleById(legioners.get(i).getRoleId());
						this.getMessageManager().sendMessageToRole(toRole.getRoleName(),
								responseBuffer.copy());
					}
					//通知入侵者
					ChannelBuffer responseBuffer1 = new DynamicChannelBuffer(200);
					responseBuffer1.writeInt(1003);
					MessageUtil.writeString(responseBuffer1, attack.getName(), "UTF-8");
					MessageUtil.writeString(responseBuffer1, "我们入侵了帮派["+defend.getName()+"]，抢了他们"+res+"资产！", "UTF-8");
					legioners = legionManager.getLegioner(attack.getId(), 1, 80);
					for (int i = 0; i < legioners.size(); i++) {
						toRole = roleManager.getRoleById(legioners.get(i).getRoleId());
						this.getMessageManager().sendMessageToRole(toRole.getRoleName(),
								responseBuffer1.copy());
					}
				}else{
					legionManager.saveLegionEvent(legionFight.getAttack(), legionFight.getStartId(), "我们向"+defend.getName()+"发起帮派战[未攻破]，无任何收益，小伙伴们加油吧[<a href=\"event:legionfight_"+legionFight.getId()+"\">查看战况</a>]", LegionEventEnum.STARTFIGHTATTACK.getCode());
					legionManager.saveLegionEvent(legionFight.getDefend(), legionFight.getStartId(), attack.getName()+"向我们发起帮派战 [未攻破]，被我们成功阻挡在城池之外[<a href=\"event:legionfight_"+legionFight.getId()+"\">查看战况</a>]", LegionEventEnum.STARTFIGHTDEFEND.getCode());
					//通知被入侵的帮派
					ChannelBuffer responseBuffer = new DynamicChannelBuffer(200);
					responseBuffer.writeInt(1003);
					MessageUtil.writeString(responseBuffer, defend.getName(), "UTF-8");
					MessageUtil.writeString(responseBuffer, "["+attack.getName()+"]入侵了我们，被我们成功阻挡了，赶紧去复仇吧！", "UTF-8");
					List<Legioner> legioners = legionManager.getLegioner(defend.getId(), 1, 80);
					Role toRole;
					for (int i = 0; i < legioners.size(); i++) {
						toRole = roleManager.getRoleById(legioners.get(i).getRoleId());
						this.getMessageManager().sendMessageToRole(toRole.getRoleName(),
								responseBuffer.copy());
					}
					//通知入侵者
					ChannelBuffer responseBuffer1 = new DynamicChannelBuffer(200);
					responseBuffer1.writeInt(1003);
					MessageUtil.writeString(responseBuffer1, attack.getName(), "UTF-8");
					MessageUtil.writeString(responseBuffer1, "我们攻打了帮派["+defend.getName()+"]，帮派实力不够，小伙伴们加油哦，高手都去哪儿了！", "UTF-8");
					legioners = legionManager.getLegioner(attack.getId(), 1, 80);
					for (int i = 0; i < legioners.size(); i++) {
						toRole = roleManager.getRoleById(legioners.get(i).getRoleId());
						this.getMessageManager().sendMessageToRole(toRole.getRoleName(),
								responseBuffer1.copy());
					}
				}
				
			}
			return true;
		}else{
			return false;
		}
	}
	private int getWinResource(LegionFight legionFight){
		String guardInfo;
		int guardNum = legionFight.getLevel() > 5 ? 9 : 5;
		int a = 0;
		int[] LegionGuardCoins = guardNum>5?LegionContext.LegionGuardCoins69:LegionContext.LegionGuardCoins05;
		for (int i = 1; i <= guardNum; i++) {
			guardInfo = (String) BeanUtils.getFieldValueByName("guard" + i,
					legionFight);
			int win = Integer.parseInt(guardInfo.split("_")[1]);
			if (win > 0) {
				a=a+LegionGuardCoins[i-1];
			}
		}
		return a;
	}
	@Override
	public int isWin(LegionFight legionFight) {
		String guardInfo;
		int guardNum = legionFight.getLevel() > 5 ? 9 : 5;
		int a = 0;
		for (int i = 1; i <= guardNum; i++) {
			guardInfo = (String) BeanUtils.getFieldValueByName("guard" + i,
					legionFight);
			int win = Integer.parseInt(guardInfo.split("_")[1]);
			if (win > 0) {
				a++;
			}
		}
		if(guardNum==5){
			if(a>2){
				return a;
			}
		}
		if(guardNum==9){
			if(a>6){
				return a;
			}
		}
		return 0;
	}
	@Override
	public LegionFight getLegionFightBydefend(Long defend) {
		// TODO Auto-generated method stub
		return MapToLegionFight(legionFightDao.getLegionFightByDefend(defend));
	}
	@Override
	public LegionFight getLegionFightByattack(Long attack) {
		// TODO Auto-generated method stub
		return MapToLegionFight(legionFightDao.getLegionFightByAttack(attack));
	}
	private LegionFight MapToLegionFight(Map map){
		if(CollectionUtils.isEmpty(map)){
			return null;
		}
		LegionFight legionFight = new LegionFight();
		legionFight.setAttack(new Long(map.get("ATTACK").toString()));
		legionFight.setDefend(new Long(map.get("DEFEND").toString()));
		legionFight.setStartId(new Long(map.get("STARTID").toString()));
		legionFight.setEnd(Integer.parseInt(map.get("END").toString()));
		legionFight.setGuard1((String)map.get("GUARD1"));
		legionFight.setGuard2((String)map.get("GUARD2"));
		legionFight.setGuard3((String)map.get("GUARD3"));
		legionFight.setGuard4((String)map.get("GUARD4"));
		legionFight.setGuard5((String)map.get("GUARD5"));
		legionFight.setGuard6((String)map.get("GUARD6"));
		legionFight.setGuard7((String)map.get("GUARD7"));
		legionFight.setGuard8((String)map.get("GUARD8"));
		legionFight.setGuard9((String)map.get("GUARD9"));
		legionFight.setId(new Long(map.get("ID").toString()));
		legionFight.setState(Integer.parseInt(map.get("STATE").toString()));
		legionFight.setLevel(Integer.parseInt(map.get("LEVEL").toString()));
		legionFight.setTime((Timestamp)map.get("TIME"));
		legionFight.setWin(Integer.parseInt(map.get("WIN").toString()));
		legionFight.setResource(new Long(map.get("RESOURCE").toString()));
		return legionFight;
	}
	private List<LegionFight> MapToListLegionFight(List<Map<String,Object>> list){
		if(CollectionUtils.isEmpty(list)){
			return null;
		}
		List<LegionFight> legionFights = new ArrayList<LegionFight>();
		for(int i=0;i<list.size();i++){
			legionFights.add(MapToLegionFight(list.get(i)));
		}
		return legionFights;
	}

	public PushNotificationManager getPushNotificationManager() {
		return pushNotificationManager;
	}

	public void setPushNotificationManager(
			PushNotificationManager pushNotificationManager) {
		this.pushNotificationManager = pushNotificationManager;
	}

	@Override
	public void scanAllLegionFight() {
		List<LegionFight> legionFights = this.getLegionFights();
		if(!CollectionUtils.isEmpty(legionFights)){
			for(LegionFight legionFight:legionFights){
				this.isEnd(legionFight);
			}
		}
	}

	@Override
	public List<LegionFight> getLegionFights() {
		// TODO Auto-generated method stub
		return MapToListLegionFight(legionFightDao.getLegionFights());
	}

	@Override
	public LegionFight getEndLegionFightById(Long id) {
		// TODO Auto-generated method stub
		return MapToLegionFight(legionFightDao.getLegionFightById(id));
	}

	@Override
	public int updateLegionFightWinResource(Long id,int win,Long resource) {
		// TODO Auto-generated method stub
		return legionFightDao.updateLegionFightWinResource(id, win, resource);
	}
	public static void main(String[] args) {
		System.out.println(9/2);
	}

	@Override
	public boolean end(LegionFight legionFight) {
		if(legionFight.getEnd()==0){
			legionFightDao.updateLegionState(legionFight.getId());
			Legion attack = legionManager.getLegionById(legionFight.getAttack());
			Legion defend = legionManager.getLegionById(legionFight.getDefend());
			if(this.isWin(legionFight)>0){
				int res = (defend.getResources().intValue()/5)*getWinResource(legionFight)/100;
				legionManager.delLegionResources(defend.getId(), res);
				legionManager.addLegionResources(attack.getId(), res);
				this.updateLegionFightWinResource(legionFight.getId(), 1,new Long(res));
				legionManager.saveLegionEvent(legionFight.getAttack(), legionFight.getStartId(), "我们向"+defend.getName()+"发起帮派战[胜利]，获得"+res+"资产[<a href=\"event:legionfight_"+legionFight.getId()+"\">查看战况</a>]", LegionEventEnum.STARTFIGHTATTACK.getCode());
				legionManager.saveLegionEvent(legionFight.getDefend(), legionFight.getStartId(), attack.getName()+"向我们发起公会战 [失守]，损失"+res+"资产[<a href=\"event:legionfight_"+legionFight.getId()+"\">查看战况</a>]", LegionEventEnum.STARTFIGHTDEFEND.getCode());
				//通知被入侵的帮派
				ChannelBuffer responseBuffer = new DynamicChannelBuffer(200);
				responseBuffer.writeInt(1003);
				MessageUtil.writeString(responseBuffer, defend.getName(), "UTF-8");
				MessageUtil.writeString(responseBuffer, "["+attack.getName()+"]入侵了我们，抢走了我们"+res+"资产，赶紧去抢回来吧！", "UTF-8");
				List<Legioner> legioners = legionManager.getLegioner(defend.getId(), 1, 80);
				Role toRole;
				for (int i = 0; i < legioners.size(); i++) {
					toRole = roleManager.getRoleById(legioners.get(i).getRoleId());
					this.getMessageManager().sendMessageToRole(toRole.getRoleName(),
							responseBuffer.copy());
				}
				//通知入侵者
				ChannelBuffer responseBuffer1 = new DynamicChannelBuffer(200);
				responseBuffer1.writeInt(1003);
				MessageUtil.writeString(responseBuffer1, attack.getName(), "UTF-8");
				MessageUtil.writeString(responseBuffer1, "我们入侵了帮派["+defend.getName()+"]，抢了他们"+res+"资产！", "UTF-8");
				legioners = legionManager.getLegioner(attack.getId(), 1, 80);
				for (int i = 0; i < legioners.size(); i++) {
					toRole = roleManager.getRoleById(legioners.get(i).getRoleId());
					this.getMessageManager().sendMessageToRole(toRole.getRoleName(),
							responseBuffer1.copy());
				}
			}else{
				legionManager.saveLegionEvent(legionFight.getAttack(), legionFight.getStartId(), "我们向"+defend.getName()+"发起帮派战[未攻破]，无任何收益[<a href=\"event:legionfight_"+legionFight.getId()+"\">查看战况</a>]", LegionEventEnum.STARTFIGHTATTACK.getCode());
				legionManager.saveLegionEvent(legionFight.getDefend(), legionFight.getStartId(), attack.getName()+"向我们发起公会战 [未攻破]，无任何损失[<a href=\"event:legionfight_"+legionFight.getId()+"\">查看战况</a>]", LegionEventEnum.STARTFIGHTDEFEND.getCode());
				//通知被入侵的帮派
				ChannelBuffer responseBuffer = new DynamicChannelBuffer(200);
				responseBuffer.writeInt(1003);
				MessageUtil.writeString(responseBuffer, defend.getName(), "UTF-8");
				MessageUtil.writeString(responseBuffer, "["+attack.getName()+"]入侵了我们，赶紧去复仇吧！", "UTF-8");
				List<Legioner> legioners = legionManager.getLegioner(defend.getId(), 1, 80);
				Role toRole;
				for (int i = 0; i < legioners.size(); i++) {
					toRole = roleManager.getRoleById(legioners.get(i).getRoleId());
					this.getMessageManager().sendMessageToRole(toRole.getRoleName(),
							responseBuffer.copy());
				}
				//通知入侵者
				ChannelBuffer responseBuffer1 = new DynamicChannelBuffer(200);
				responseBuffer1.writeInt(1003);
				MessageUtil.writeString(responseBuffer1, attack.getName(), "UTF-8");
				MessageUtil.writeString(responseBuffer1, "我们入侵了帮派["+defend.getName()+"]，帮派实力不够，小伙伴们加油哦，高手都去哪儿了！", "UTF-8");
				legioners = legionManager.getLegioner(attack.getId(), 1, 80);
				for (int i = 0; i < legioners.size(); i++) {
					toRole = roleManager.getRoleById(legioners.get(i).getRoleId());
					this.getMessageManager().sendMessageToRole(toRole.getRoleName(),
							responseBuffer1.copy());
				}
			}
			
		}
		return true;
	}
}

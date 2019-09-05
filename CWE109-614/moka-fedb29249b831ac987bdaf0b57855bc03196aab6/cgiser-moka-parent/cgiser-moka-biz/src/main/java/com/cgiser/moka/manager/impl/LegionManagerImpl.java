package com.cgiser.moka.manager.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.cgiser.moka.dao.LegionDao;
import com.cgiser.moka.manager.CardManager;
import com.cgiser.moka.manager.LegionManager;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.manager.UserCardGroupManager;
import com.cgiser.moka.model.Card;
import com.cgiser.moka.model.Legion;
import com.cgiser.moka.model.LegionApply;
import com.cgiser.moka.model.LegionContext;
import com.cgiser.moka.model.LegionEvent;
import com.cgiser.moka.model.LegionEventEnum;
import com.cgiser.moka.model.LegionGood;
import com.cgiser.moka.model.LegionLevel;
import com.cgiser.moka.model.LegionTech;
import com.cgiser.moka.model.Legioner;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.model.UserCard;
import com.cgiser.moka.model.UserCardGroup;

public class LegionManagerImpl implements LegionManager {
	Logger logger = LoggerFactory.getLogger("cashDao");
	private LegionDao legionDao;
	private RoleManager roleManager;
	private UserCardGroupManager userCardGroupManager;
	private CardManager cardManager;
	@Override
	public List<LegionApply> getLegionApply(long legionId, int index, int count) {		
		return mapListToLegionApplyList(legionDao.getLegionApply(legionId, index, count));
	}

	@Override
	public int getLegionCount() {
		return legionDao.getLegionCount();
	}

	@Override
	public List<Legioner> getLegioner(long legionId, int index, int count) {
		
		return mapListToLegionerList(legionDao.getLegioner(legionId, index, count));
	}

	@Override
	public List<Legion> getLegions(String legion, int index, int count) {
		
		return mapListToLegionList(legionDao.getLegions(legion, index, count));
	}

	@Override
	public Legioner getLegioner(long roleId) {
		return MapToLegioner(legionDao.getLegionerByRoleId(roleId));
	}
	
	@Override
	public Legion getLegionById(long legionId) {
		return MapToLegion(legionDao.getLegionById(legionId));
	}
	@Override
	public Legion getLegionByName(String name) {
		return MapToLegion(legionDao.getLegionByName(name));
	}
	@Override
	public Long saveLegion(Role role,String name, long createid, String slogan) {
		if(roleManager.updateCash(role.getRoleName(), 200)){
			Long legionId = legionDao.saveLegion(name, createid, slogan);
			if(legionId>0){
				if(!(legionDao.saveLegionApplyRel(role.getRoleId(), legionId, 1, 1, 1)>0)){
					logger.error("消费元宝成功,创建帮派成功,添加角色失败,roleName:"+role.getRoleName());
				}
				if(!(legionDao.saveLegionGuard(legionId, 5, 1, 1)>0)){
					logger.error("消费元宝成功,创建帮派成功,创建帮派守卫失败,roleName:"+role.getRoleName());
				}
				this.saveLegionEvent(legionId, role.getRoleId(), role.getRoleName()+"创建帮派", LegionEventEnum.CREATELEGION.getCode());
				return legionId;
			}else{
				logger.error("消费元宝成功,创建帮派失败,roleName:"+role.getRoleName());
				return 0L;
			}
		}else{
			return 0L;
		}
		
	}
	
	private Legion MapToLegion(Map<String,Object> map){
		if(CollectionUtils.isEmpty(map)){
			return null;
		}
		Legion legion = new Legion();
		legion.setContribute1(map.get("CONTRIBUTE1")==null?0L:new Long(map.get("CONTRIBUTE1").toString()));
		legion.setContribute2(map.get("CONTRIBUTE2")==null?0L:new Long(map.get("CONTRIBUTE2").toString()));
		legion.setContribute3(map.get("CONTRIBUTE3")==null?0L:new Long(map.get("CONTRIBUTE3").toString()));
		legion.setContribute4(map.get("CONTRIBUTE4")==null?0L:new Long(map.get("CONTRIBUTE4").toString()));
		legion.setContribute5(map.get("CONTRIBUTE5")==null?0L:new Long(map.get("CONTRIBUTE5").toString()));
		legion.setContribute6(map.get("CONTRIBUTE6")==null?0L:new Long(map.get("CONTRIBUTE6").toString()));
		legion.setContribute7(map.get("CONTRIBUTE7")==null?0L:new Long(map.get("CONTRIBUTE7").toString()));
		legion.setContribute8(map.get("CONTRIBUTE8")==null?0L:new Long(map.get("CONTRIBUTE8").toString()));
		legion.setContribute9(map.get("CONTRIBUTE9")==null?0L:new Long(map.get("CONTRIBUTE9").toString()));
		legion.setContribute10(map.get("CONTRIBUTE10")==null?0L:new Long(map.get("CONTRIBUTE10").toString()));
		legion.setContribute11(map.get("CONTRIBUTE11")==null?0L:new Long(map.get("CONTRIBUTE11").toString()));
		legion.setCreateId(map.get("CREATEID")==null?0L:new Long(map.get("CREATEID").toString()));
		legion.setLastAttack(Integer.parseInt(map.get("LASTATTACK").toString()));
		legion.setCreateTime(((Timestamp)map.get("CREATETIME")));
		legion.setEmblem(Integer.parseInt(map.get("EMBLEM").toString()));
		legion.setEmblemLevel(Integer.parseInt(map.get("EMBLEMLEVEL").toString()));
		legion.setHeaderId(new Long(map.get("HEADERID").toString()));
		legion.setId(new Long(map.get("ID").toString()));
		legion.setLegionLevel(Integer.parseInt(map.get("LEGIONLEVEL").toString()));
		legion.setName(map.get("NAME").toString());
		legion.setResources(map.get("RESOURCES")==null?0L:new Long(map.get("RESOURCES").toString()));
		legion.setSlogan(map.get("SLOGAN")==null?"":map.get("SLOGAN").toString());
		legion.setNotice(map.get("NOTICE")==null?"":map.get("NOTICE").toString());
		legion.setBuyAttack(Integer.parseInt(map.get("BUYATTACK").toString()));
		legion.setBuyAttackAll(Integer.parseInt(map.get("BUYATTACKALL").toString()));
		legion.setRobTimes(Integer.parseInt(map.get("ROBTIMES").toString()));
		legion.setRobTimesAll(Integer.parseInt(map.get("ROBTIMESALL").toString()));
		Map mapGuard = legionDao.getLegionGuardByLegionId(legion.getId());
		if(CollectionUtils.isEmpty(mapGuard)){
			if(legionDao.saveLegionGuard(legion.getId(), legion.getLegionLevel()>5?9:5, legion.getLegionLevel(), 1)>0){
				mapGuard = legionDao.getLegionGuardByLegionId(legion.getId());
			}
		}
		if(!CollectionUtils.isEmpty(mapGuard)){
			legion.setGuard1(new Long((String)mapGuard.get("GUARD1")));
			legion.setGuard2(new Long((String)mapGuard.get("GUARD2")));
			legion.setGuard3(new Long((String)mapGuard.get("GUARD3")));
			legion.setGuard4(new Long((String)mapGuard.get("GUARD4")));
			legion.setGuard5(new Long((String)mapGuard.get("GUARD5")));
			legion.setGuard6(new Long((String)mapGuard.get("GUARD6")));
			legion.setGuard7(new Long((String)mapGuard.get("GUARD7")));
			legion.setGuard8(new Long((String)mapGuard.get("GUARD8")));
			legion.setGuard9(new Long((String)mapGuard.get("GUARD9")));
		}
		return legion;
	}
	private LegionTech MapToLegionTech(Map<String,Object> map){
		if(CollectionUtils.isEmpty(map)){
			return null;
		}
		LegionTech legionTech = new LegionTech();
		legionTech.setId(new Long((String)map.get("ID")));
		String strResourceArray = (String)map.get("RESOURCEARRAY");
		String[] strResoures = strResourceArray.split("_");
		List<Integer> resources = new ArrayList<Integer>();
		for(String str:strResoures){
			resources.add(Integer.parseInt(str));
		}
		legionTech.setResourceArray(resources);
		legionTech.setTechEffect((String)map.get("TECHEFFECT"));
		legionTech.setTechId((String)map.get("TECHID"));
		legionTech.setTechName((String)map.get("TECHNAME"));
		legionTech.setLegionLevel(Integer.parseInt((String)map.get("LEGIONLEVEL")));
		legionTech.setRace(Integer.parseInt((String)map.get("RACE")));
		legionTech.setType(Integer.parseInt((String)map.get("TYPE")));
		legionTech.setAnd(Float.parseFloat((String)map.get("ADD")));
		return legionTech;
	}
	private List<LegionTech> mapListToLegionTechList(List<Map<String,Object>> list){
		if(CollectionUtils.isEmpty(list)){
			return null;
		}
		LegionTech legion;
		List<LegionTech> listLegion = new ArrayList<LegionTech>();
		for(int i = 0; i<list.size();i++){
			legion = MapToLegionTech(list.get(i));
			if(legion!=null){
				listLegion.add(legion);
			}
		}
		return listLegion;
	}
	private LegionLevel MapToLegionLevel(Map<String,Object> map){
		if(CollectionUtils.isEmpty(map)){
			return null;
		}
		LegionLevel legionLevel = new LegionLevel();
		legionLevel.setId(new Long((String)map.get("ID")));
		legionLevel.setLegionerNum(Integer.parseInt((String)map.get("LEGIONERNUM")));
		legionLevel.setLevel(Integer.parseInt((String)map.get("LEVEL")));
		legionLevel.setResource(Integer.parseInt((String)map.get("RESOURCE")));
		return legionLevel;
	}
	private List<Legion> mapListToLegionList(List<Map<String,Object>> list){
		if(CollectionUtils.isEmpty(list)){
			return null;
		}
		Legion legion;
		List<Legion> listLegion = new ArrayList<Legion>();
		for(int i = 0; i<list.size();i++){
			legion = MapToLegion(list.get(i));
			if(legion!=null){
				listLegion.add(legion);
			}
		}
		return listLegion;
	}
	private LegionApply MapToLegionApply(Map<String,Object> map){
		if(CollectionUtils.isEmpty(map)){
			return null;
		}
		LegionApply legionApply = new LegionApply();
		legionApply.setUid(new Long(map.get("ID").toString()));
		legionApply.setRoleId(new Long(map.get("ROLEID").toString()));
		Role role = roleManager.getRoleById(legionApply.getRoleId());
		legionApply.setAvatar(role.getAvatar());
		legionApply.setLastLoginDate(role.getLastLoginTime()==null?new Date():role.getLastLoginTime());
		legionApply.setLevel(role.getLevel());
		legionApply.setNickName(role.getRoleName());
		legionApply.setRank(role.getRank());
		legionApply.setSex(role.getSex());
		return legionApply;
	}
	private List<LegionApply> mapListToLegionApplyList(List<Map<String,Object>> list){
		if(CollectionUtils.isEmpty(list)){
			return null;
		}
		LegionApply legionApply;
		List<LegionApply> listLegionApply = new ArrayList<LegionApply>();
		for(int i = 0; i<list.size();i++){
			legionApply = MapToLegionApply(list.get(i));
			if(legionApply!=null){
				listLegionApply.add(legionApply);
			}
		}
		return listLegionApply;
	}
	private List<LegionEvent> mapListToLegionEventList(List<Map<String,Object>> list){
		if(CollectionUtils.isEmpty(list)){
			return null;
		}
		LegionEvent legionEvent;
		List<LegionEvent> listLegionEvent = new ArrayList<LegionEvent>();
		for(int i = 0; i<list.size();i++){
			legionEvent = MapToLegionEvent(list.get(i));
			if(legionEvent!=null){
				listLegionEvent.add(legionEvent);
			}
		}
		return listLegionEvent;
	}
	private LegionEvent MapToLegionEvent(Map<String, Object> map) {
		if(CollectionUtils.isEmpty(map)){
			return null;
		}
		LegionEvent legionEvent = new LegionEvent();
		legionEvent.setCreateTime(new Date(((Timestamp)map.get("CREATETIME")).getTime()));
		legionEvent.setEventContent((String)map.get("EVENTCONTENT"));
		legionEvent.setEventId(new Long(map.get("EVENTID").toString()));
		legionEvent.setLegionId(new Long(map.get("LEGIONID").toString()));
		legionEvent.setRoleId(new Long(map.get("ROLEID").toString()));
		legionEvent.setState(Integer.parseInt(map.get("STATE").toString()));
		legionEvent.setType(Integer.parseInt(map.get("TYPE").toString()));
		return legionEvent;
	}

	private Legioner MapToLegioner(Map<String,Object> map){
		if(CollectionUtils.isEmpty(map)){
			return null;
		}
		Legioner legioner = new Legioner();
		legioner.setContribute(new Long(map.get("CONTRIBUTE").toString()));
		legioner.setDuty(Integer.parseInt(map.get("DUTY").toString()));
		legioner.setHonor(new Long(map.get("HONOR").toString()));
		legioner.setId(new Long(map.get("ID").toString()));
		legioner.setRoleId(new Long(map.get("ROLEID").toString()));
		legioner.setLegionId(new Long(map.get("LEGIONID").toString()));
		legioner.setJoinTime(new Date(((Timestamp)map.get("JOINTIME")).getTime()));
		Role role = roleManager.getRoleById(legioner.getRoleId());
		legioner.setAvatar(role.getAvatar());
		legioner.setLastLoginDate(role.getLastLoginTime()==null?new Date():role.getLastLoginTime());
		legioner.setLevel(role.getLevel());
		legioner.setNickName(role.getRoleName());
		legioner.setRank(role.getRank());
		legioner.setSex(role.getSex());
		legioner.setLastAttack(Integer.parseInt(map.get("LASTATTACK").toString()));
		legioner.setLastContribute1(Integer.parseInt(map.get("LASTCONTRIBUTE1").toString()));
		legioner.setLastContribute2(Integer.parseInt(map.get("LASTCONTRIBUTE2").toString()));
		legioner.setLastContribute3(Integer.parseInt(map.get("LASTCONTRIBUTE3").toString()));
		legioner.setEffective(roleManager.getRoleEffective(role.getRoleId()));
		UserCardGroup userCardGroup = userCardGroupManager.getUserCardGroupById(new Long(role.getDefaultGroupId()));
		Card card = null;
		if(!CollectionUtils.isEmpty(userCardGroup.getUserCardInfo())){
			for(UserCard userCard:userCardGroup.getUserCardInfo()){
				if(userCard==null||userCard.getCardId()==0){
					logger.error(role.getRoleName()+"有一张不存在的卡牌");
					continue;
				}
				card = cardManager.getCardById(new Long(userCard.getCardId()));
				if(card==null){
					logger.error(role.getRoleName()+"有一张不存在的卡牌");
					continue;
				}
				if(card.getColor()==3){
					legioner.setStar3(legioner.getStar3()+1);
				}
				if(card.getColor()==4){
					legioner.setStar4(legioner.getStar4()+1);
				}
				if(card.getColor()==5){
					legioner.setStar5(legioner.getStar5()+1);
				}
			}
		}
		legioner.setVip(role.getVip());
		return legioner;
	}
	private List<Legioner> mapListToLegionerList(List<Map<String,Object>> list){
		if(CollectionUtils.isEmpty(list)){
			return null;
		}
		Legioner legioner;
		List<Legioner> listLegioner = new ArrayList<Legioner>();
		for(int i = 0; i<list.size();i++){
			legioner = MapToLegioner(list.get(i));
			if(legioner!=null){
				listLegioner.add(legioner);
			}
		}
		return listLegioner;
	}
	public LegionDao getLegionDao() {
		return legionDao;
	}

	public void setLegionDao(LegionDao legionDao) {
		this.legionDao = legionDao;
	}
	
	@Override
	public Long ApplyAddToLegion(Long legionId, Long roleId) {
		Role role = roleManager.getRoleById(roleId);
		if(role==null){
			return 0L;
		}
		this.saveLegionEvent(legionId,roleId, role.getRoleName()+"申请加入帮派", LegionEventEnum.APPLYLEGION.getCode());
		return legionDao.saveLegionApply(roleId, legionId, 0, 1);
	}

	@Override
	public int AgreeLegionApply(Long Id) {
		return legionDao.agreeLegionApply(Id);
	}

	@Override
	public int updateLegionSlogan(long legionId, String slogan) {
		// TODO Auto-generated method stub
		return legionDao.updateLegionSlogan(legionId, slogan);
	}

	@Override
	public int outLegion(Long legionerId) {
		Legioner legioner = this.getLegionerBylegionerId(legionerId);
		if(legioner==null){
			return 1;
		}
		if(this.isGuard(legioner.getRoleId())>0){
			this.removeGuard(this.isGuard(legioner.getRoleId()), legioner.getLegionId());
		}
		return legionDao.outLegion(legionerId);
	}
	@Override
	public int breakupLegion(Long legionId) {
		// TODO Auto-generated method stub
		return legionDao.breakupLegion(legionId);
	}
	@Override
	public Legioner getDeputyHeader(Long legionId) {
		// TODO Auto-generated method stub
		return MapToLegioner(legionDao.getDeputyHeader(legionId));
	}
	@Override
	public Legioner getHeader(Long legionId) {
		// TODO Auto-generated method stub
		return MapToLegioner(legionDao.getHeader(legionId));
	}
	@Override
	public Legioner getMaxContributeLegioner(Long legionId) {
		Legioner legioner=null;
		List<Legioner> list = mapListToLegionerList(legionDao.getMaxContributeLegioner(legionId));
		if(CollectionUtils.isEmpty(list)){
			return legioner;
		}
		legioner = list.get(0);
		for(int i=1;i<list.size();i++){
			if(legioner.getContribute()<list.get(i).getContribute()){
				legioner = list.get(i);
			}
		}
		return legioner;
	}
	public RoleManager getRoleManager() {
		return roleManager;
	}

	public void setRoleManager(RoleManager roleManager) {
		this.roleManager = roleManager;
	}

	@Override
	public int updateDeputyHeader(Long legionId,Long legionerId) {
		Legioner dLegioner = this.getDeputyHeader(legionId);
		if(dLegioner!=null){
			this.resignDuty(dLegioner.getId());
		}
		return legionDao.updateDuty(legionerId, 2);
	}

	@Override
	public int updateHeader(Long legionId,Long roleId) {
		Legioner legioner = this.getHeader(legionId);
		this.resignDuty(legioner.getId());
		legioner = this.getLegioner(roleId);
		legionDao.updateDuty(legioner.getId(), 1);
		return legionDao.updateHeader(legionId, roleId);
	}

	@Override
	public int resignDuty(Long legionerId) {
		// TODO Auto-generated method stub
		return legionDao.updateDuty(legionerId, 5);
	}

	@Override
	public int delLegionApply(Long Id) {
		// TODO Auto-generated method stub
		return legionDao.delLegionApply(Id);
	}

	@Override
	public int upLegionEmblem(Role role,Long legionId) {
		Legion legion = this.getLegionById(legionId);
		int level = legion.getEmblemLevel()-1;
		Legioner legioner = this.getLegioner(role.getRoleId());
		if(roleManager.updateCash(role.getRoleName(), LegionContext.LegionEmblemLevelCash[level])){
			if(this.addLegionContribute(legionId, LegionContext.LegionEmblemLevelContribute[level], 1)<1){
				logger.debug("升级军徽，增加军团贡献失败");
			}
			if(this.addRoleContributeHonor(legioner.getId(), LegionContext.LegionEmblemLevelContribute[level], 
					LegionContext.LegionEmblemLevelHonor[level])<1){
				logger.debug("升级军徽，增加用户军团贡献，军团荣誉失败");
			}
			return legionDao.upLegionEmblemLevel(legionId, 1);
		}else{
			logger.debug("升级军徽，扣除元宝失败");
			return 0;
		}
		
	}

	@Override
	public int addLegionContribute(Long legionId, int contribute,int contributeId) {
		// TODO Auto-generated method stub
		return legionDao.andLegionContribute(legionId, contribute, contributeId);
	}

	@Override
	public int addRoleContributeHonor(Long legionerId, int contribute, int honor) {
		// TODO Auto-generated method stub
		return legionDao.addRoleContributeHonor(legionerId, contribute, honor);
	}

	@Override
	public int updateLegionEmblem(int emblemId, Long legionId) {
		// TODO Auto-generated method stub
		return legionDao.updateLegionEmblem(emblemId, legionId);
	}

	@Override
	public List<Legioner> getLegionersByduty(Long legionId,int duty) {
		return  mapListToLegionerList(legionDao.getLegionersByDuty(legionId, duty));
	}

	@Override
	public int updateLegionerDuty(Long legionerId, int duty) {
		// TODO Auto-generated method stub
		return legionDao.updateDuty(legionerId, duty);
	}

	@Override
	public int addLegionResources(Long legionId, int resources) {
		// TODO Auto-generated method stub
		return legionDao.updateResources(legionId, resources, 1);
	}

	@Override
	public int delLegionResources(Long legionId, int resources) {
		// TODO Auto-generated method stub
		return legionDao.updateResources(legionId, resources, 0);
	}

	@Override
	public int updateLegionNotice(long legionId, String notice) {
		// TODO Auto-generated method stub
		return legionDao.updateLegionNotice(legionId, notice);
	}

	@Override
	public int setGuard(int id, Long roleId) {

		Legioner legioner = this.getLegioner(roleId);
		if(legioner==null){
			return 0;
		}
		Legion legion = this.getLegionById(legioner.getLegionId());
		
		if(legion==null){
			return 0;
		}
		if(legion.getLegionLevel()<6&&id>5){
			return 0;
		}
		if(this.isGuard(roleId)>0){
			this.removeGuard(this.isGuard(roleId), legion.getId());
		}
		Role role = roleManager.getRoleById(roleId);
		this.saveLegionEvent(legioner.getLegionId(),legioner.getRoleId(), role.getRoleName()+"被设置成了守卫", LegionEventEnum.GUARDLEGION.getCode());
		return legionDao.updateLegionGuard(legion.getId(), id, roleId);
	}
	private int removeGuard(int id,Long legionId){
		Legion legion = this.getLegionById(legionId);
		
		if(legion==null){
			return 0;
		}
		if(legion.getLegionLevel()<6&&id>5){
			return 0;
		}
		return legionDao.updateLegionGuard(legion.getId(), id, 0L);
	}
	@Override
	public int isGuard(Long roleId) {
		Legioner legioner = this.getLegioner(roleId);
		if(legioner==null){
			return 0;
		}
		Legion legion = this.getLegionById(legioner.getLegionId());
		
		if(legion==null){
			return 0;
		}
		if(legion.getGuard1().equals(roleId)){
			return 1;
		}
		if(legion.getGuard2().equals(roleId)){
			return 2;
		}
		if(legion.getGuard3().equals(roleId)){
			return 3;
		}
		if(legion.getGuard4().equals(roleId)){
			return 4;
		}
		if(legion.getGuard5().equals(roleId)){
			return 5;
		}
		if(legion.getLegionLevel()>5){
			if(legion.getGuard6().equals(roleId)){
				return 6;
			}
			if(legion.getGuard7().equals(roleId)){
				return 7;
			}
			if(legion.getGuard8().equals(roleId)){
				return 8;
			}
			if(legion.getGuard9().equals(roleId)){
				return 9;
			}
		}
		return 0;
	}

	@Override
	public int updateLegionFightAttackTimes(Long legionId) {
		// TODO Auto-generated method stub
		return legionDao.updateLegionAttack(legionId);
	}

	@Override
	public int resetLegionFightAttackTimes() {
		legionDao.resetLegionBuyAttack();
		return legionDao.resetLegionAttack();
	}

	@Override
	public int addLegionFightAttackTimes(Long legionId) {
		// TODO Auto-generated method stub
		return legionDao.addLegionAttack(legionId);
	}

	@Override
	public int addRoleLegionFightAttackTimes(Long legionerId) {
		// TODO Auto-generated method stub
		return legionDao.addLegionerAttack(legionerId);
	}

	@Override
	public int resetRoleLegionFightAttackTimes() {
		// TODO Auto-generated method stub
		return legionDao.resetLegionerAttack();
	}

	@Override
	public int updateRoleLegionFightAttackTimes(Long legionerId) {
		// TODO Auto-generated method stub
		return legionDao.updateLegionerAttack(legionerId);
	}

	@Override
	public LegionApply getLegionApplyByRoleId(long legionId, Long roleId) {
		// TODO Auto-generated method stub
		return MapToLegionApply(legionDao.getLegionApplyByRoleId(legionId,roleId));
	}

	@Override
	public Long saveLegionEvent(Long legionId, Long roleId, String content,
			int type) {
		// TODO Auto-generated method stub
		return legionDao.saveLegionEvent(legionId, roleId, content, type);
	}

	@Override
	public List<LegionEvent> getLegionEvent(Long legionId) {
		// TODO Auto-generated method stub
		return mapListToLegionEventList(legionDao.getLegionEvent(legionId));
	}

	@Override
	public List<LegionEvent> getLegionEventByType(Long legionId, int type) {
		// TODO Auto-generated method stub
		return mapListToLegionEventList(legionDao.getLegionEventByType(legionId, type));
	}

	@Override
	public LegionApply getLegionApply(long id) {
		// TODO Auto-generated method stub
		return MapToLegionApply(legionDao.getLegionApplyById(id));
	}

	@Override
	public Legioner getLegionerBylegionerId(long legionerId) {
		// TODO Auto-generated method stub
		return MapToLegioner(legionDao.getLegionerByLegionerId(legionerId));
	}

	@Override
	public int upLegion(Long legionId, Long roleId) {
		// TODO Auto-generated method stub
		Legion legion = this.getLegionById(legionId);
		if(legion==null){
			return 0;
		}
		int level = legion.getLegionLevel();
		LegionLevel legionLevel = this.getLegionLevelByLevel(level+1);
		if(legionLevel==null){
			return 2;
		}
		if(legion.getResources()<legionLevel.getResource()){
			return 3;
		}
		if(this.delLegionResources(legionId, legionLevel.getResource())>0){
			legionDao.upLegionLevel(legionId);
			legionDao.updateLegionLegionerNum(legionId, legionLevel.getLegionerNum());
			return 1;
		}
		return 0;
	}

	@Override
	public LegionLevel getLegionLevelByLevel(int level) {
		// TODO Auto-generated method stub
		return MapToLegionLevel(legionDao.getLegionLevelByLevel(level));
	}

	@Override
	public LegionTech getLegionTechByTechId(String techId) {
		// TODO Auto-generated method stub
		return MapToLegionTech(legionDao.getLegionTechByTechId(techId));
	}

	@Override
	public List<LegionTech> getLegionTechs() {
		// TODO Auto-generated method stub
		return mapListToLegionTechList(legionDao.getLegionTechs());
	}
	private LegionGood MapToLegionGood(Map<String,Object> map){
		if(CollectionUtils.isEmpty(map)){
			return null;
		}
		LegionGood legionGood = new LegionGood();
		legionGood.setGoodDesc((String)map.get("GOODDESC"));
		legionGood.setGoodName((String)map.get("GOODNAME"));
		legionGood.setGoodNum(Integer.parseInt((String)map.get("GOODNUM")));
		legionGood.setGoodPrice(Integer.parseInt((String)map.get("GOODPRICE")));
		legionGood.setGoodType(Integer.parseInt((String)map.get("GOODTYPE")));
		legionGood.setGoodValue(Integer.parseInt((String)map.get("GOODVALUE")));
		legionGood.setId(new Long((String)map.get("ID")));
		legionGood.setState(Integer.parseInt((String)map.get("STATE")));
		legionGood.setTechLevel(Integer.parseInt((String)map.get("TECHLEVEL")));
		return legionGood;
	}
	private List<LegionGood> MapListToLegionGoodList(List<Map<String,Object>> list){
		if(CollectionUtils.isEmpty(list)){
			return null;
		}
		LegionGood legion;
		List<LegionGood> listLegion = new ArrayList<LegionGood>();
		for(int i = 0; i<list.size();i++){
			legion = MapToLegionGood(list.get(i));
			if(legion!=null){
				listLegion.add(legion);
			}
		}
		return listLegion;
	}
	@Override
	public List<LegionGood> getLegionGoods() {
		// TODO Auto-generated method stub
		return MapListToLegionGoodList(legionDao.getLegionGoods());
	}

	@Override
	public LegionGood getLegionGoodById(Long goodId) {
		// TODO Auto-generated method stub
		return MapToLegionGood(legionDao.getLegionGoodById(goodId));
	}

	@Override
	public int addLegionerHonor(Long legionerId, int num) {
		// TODO Auto-generated method stub
		return legionDao.addRoleContributeHonor(legionerId, 0, num);
	}

	@Override
	public synchronized int updateLegionerHonor(Long legionerId, int num) {
		if(this.getLegionerBylegionerId(legionerId).getHonor()<num){
			return 0;
		}
		return legionDao.updateRoleHonor(legionerId, num);
	}

	@Override
	public int resetRoleLegionContribute() {
		// TODO Auto-generated method stub
		return legionDao.resetLegionerLastContribute();
	}

	@Override
	public int updateRoleLegionContribute(Long legionerId,int id) {
		// TODO Auto-generated method stub
		return legionDao.updateLegionerLastContribute(legionerId, id);
	}
	@Override
	public int addRoleLegionContribute(Long legionerId, int id) {
		// TODO Auto-generated method stub
		return legionDao.addLegionerLastContribute(legionerId, id);
	}
	public UserCardGroupManager getUserCardGroupManager() {
		return userCardGroupManager;
	}

	public void setUserCardGroupManager(UserCardGroupManager userCardGroupManager) {
		this.userCardGroupManager = userCardGroupManager;
	}

	public CardManager getCardManager() {
		return cardManager;
	}

	public void setCardManager(CardManager cardManager) {
		this.cardManager = cardManager;
	}

	@Override
	public int addLegionFightBuyAttackTimes(Long legionId) {
		legionDao.addLegionBuyAttackAll(legionId);
		// TODO Auto-generated method stub
		return legionDao.addLegionBuyAttack(legionId);
	}

	@Override
	public Date getOutLegionTime(Long roleId) {
		return legionDao.getOutLegionTime(roleId);
	}

	@Override
	public int delLegionApplyByRoleId(Long roleId) {
		// TODO Auto-generated method stub
		return legionDao.delLegionApplyByRoleId(roleId);
	}

	@Override
	public int addLegionRobTimes(Long legionId) {
		legionDao.addLegionRobTimesAll(legionId);
		return legionDao.addLegionRobTimes(legionId);
	}

	@Override
	public int resetLegionRobTimes() {
		// TODO Auto-generated method stub
		return legionDao.resetLegionRobTimes();
	}
}

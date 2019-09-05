package com.cgiser.moka.manager.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.util.CollectionUtils;

import com.cgiser.moka.dao.SoulDao;
import com.cgiser.moka.manager.SoulManager;
import com.cgiser.moka.model.FightCard;
import com.cgiser.moka.model.Opp;
import com.cgiser.moka.model.Soul;

public class SoulManagerImpl implements SoulManager {
	private SoulDao soulDao;
	private List<Integer> soulIdList;
	@Override
	public Soul getSoulById(int id) {
		// TODO Auto-generated method stub
		return MapToSoul(soulDao.getSoulById(id));
	}

	@Override
	public List<Soul> getSouls() {
		// TODO Auto-generated method stub
		return MapListToSoulList(soulDao.getSouls());
	}
	@Override
	public List<Opp> getOppsBySoul(Soul soul,FightCard fightCard) {
		List<Opp> opps = new ArrayList<Opp>();
		Opp opp;
		if(soul.getSoulType()==1){
			opp = new Opp();
			opp.setOpp(1091);
			opp.setUUID(fightCard.getUUID());
			opp.setTarget(fightCard.getUUID());
			opp.setValue(soul.getSoulId());
			opps.add(opp);
			
			opp = new Opp();
			opp.setOpp(1020);
			opp.setUUID(fightCard.getUUID());
			opp.setTarget(fightCard.getUUID());
			opp.setValue(soul.getAttack());
			opps.add(opp);
			fightCard.setAttack(fightCard.getAttack()+soul.getAttack());
		}
		if(soul.getSoulType()==4){
			Random rnd = new Random();
			if(rnd.nextInt(100)<soul.getAttack()){
				opp = new Opp();
				opp.setOpp(1091);
				opp.setUUID(fightCard.getUUID());
				opp.setTarget(fightCard.getUUID());
				opp.setValue(soul.getSoulId());
				opps.add(opp);
				
				opp = new Opp();
				opp.setOpp(1020);
				opp.setUUID(fightCard.getUUID());
				opp.setTarget(fightCard.getUUID());
				opp.setValue(fightCard.getAttack());
				opps.add(opp);
				fightCard.setAttack(fightCard.getAttack()+fightCard.getAttack());
				fightCard.getSoul().setIsFight(1);
			}
		}
		if(soul.getSoulType()==5){
			opp = new Opp();
			opp.setOpp(1091);
			opp.setUUID(fightCard.getUUID());
			opp.setTarget(fightCard.getUUID());
			opp.setValue(soul.getSoulId());
			opps.add(opp);
			opp = new Opp();
			opp.setOpp(1040);
			opp.setUUID(fightCard.getUUID());
			opp.setTarget(fightCard.getUUID());
			opp.setValue(soul.getHp());
			opps.add(opp);
			fightCard.setAttack(fightCard.getAttack()+soul.getHp());
		}
		if(soul.getSoulType()==9){
			opp = new Opp();
			opp.setOpp(1091);
			opp.setUUID(fightCard.getUUID());
			opp.setTarget(fightCard.getUUID());
			opp.setValue(soul.getSoulId());
			opps.add(opp);
			opp = new Opp();
			opp.setOpp(1020);
			opp.setUUID(fightCard.getUUID());
			opp.setTarget(fightCard.getUUID());
			opp.setValue(fightCard.getAttack()*soul.getAttack()/100);
			opps.add(opp);
			fightCard.setAttack(fightCard.getAttack()+fightCard.getAttack()*soul.getAttack()/100);
			fightCard.getSoul().setIsFight(1);
		}
		return opps;
	}
	private List<Soul> MapListToSoulList(List<Map<String,Object>> list){
		if(CollectionUtils.isEmpty(list)){
			return null;
		}
		List<Soul> souls = new ArrayList<Soul>();
		for(int i=0;i<list.size();i++){
			souls.add(MapToSoul(list.get(i)));
		}
		return souls;
	}
	private Soul MapToSoul(Map<String,Object> map){
		if(CollectionUtils.isEmpty(map)){
			return null;
		}
		Soul soul = new Soul();
		soul.setSoulId(Integer.parseInt(map.get("SOULID").toString()));
		soul.setSoulName(map.get("SOULNAME").toString());
		soul.setAttack(Integer.parseInt(map.get("ATTACK").toString()));
		soul.setHp(Integer.parseInt(map.get("HP").toString()));
		soul.setSoulType(Integer.parseInt(map.get("SOULTYPE").toString()));
		soul.setRound(Integer.parseInt(map.get("ROUND").toString()));
		soul.setDesc(map.get("DESC")==null?"":map.get("DESC").toString());
		soul.setColor(Integer.parseInt(map.get("COLOR").toString()));
		soul.setRandom(Integer.parseInt(map.get("RANDOM").toString()));
		soul.setHonor(Integer.parseInt(map.get("HONOR").toString()));
		soul.setPrice(Integer.parseInt(map.get("PRICE").toString()));
		return soul;
	}
	public SoulDao getSoulDao() {
		return soulDao;
	}

	public void setSoulDao(SoulDao soulDao) {
		this.soulDao = soulDao;
	}

	@Override
	public Soul randomSoul(int id1,int id2) {
		if(CollectionUtils.isEmpty(soulIdList)){
			soulIdList = new ArrayList<Integer>();
			List<Map<String, Object>> list = soulDao.getAllSoulId();
			for(Map map:list){
				soulIdList.add(Integer.parseInt(map.get("SOULID").toString()));
			}
		}
		Random rnd = new Random();
		int a = rnd.nextInt(soulIdList.size());
		if(a==id1){
			a++;
		}
		if(a==id2){
			a++;
		}
		if(a>=soulIdList.size()){
			a = soulIdList.size() - rnd.nextInt(soulIdList.size()-2);
		}
		
		return MapToSoul(soulDao.getSoulById(soulIdList.get(a)));
	}

}

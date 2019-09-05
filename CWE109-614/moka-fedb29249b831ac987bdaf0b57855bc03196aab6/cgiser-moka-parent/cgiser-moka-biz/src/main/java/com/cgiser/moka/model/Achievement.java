package com.cgiser.moka.model;

import com.cgiser.keel.http.utils.HttpSpringUtils;
import com.cgiser.moka.manager.CardManager;
import com.cgiser.moka.manager.RuneManager;
import com.cgiser.moka.manager.SoulManager;

public class Achievement {
	private int achievementId;
	private String desc;
	private int type;
	private int value;
	private int status;
	private int isFinish;
	private int finishNum;
	private int finishState;
	private String name;
	public int getAchievementId() {
		return achievementId;
	}
	public void setAchievementId(int achievementId) {
		this.achievementId = achievementId;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getIsFinish() {
		return isFinish;
	}
	public void setIsFinish(int isFinish) {
		this.isFinish = isFinish;
	}
	public int getFinishNum() {
		return finishNum;
	}
	public void setFinishNum(int finishNum) {
		this.finishNum = finishNum;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getFinishState() {
		return finishState;
	}
	public void setFinishState(int finishState) {
		this.finishState = finishState;
	}
	public String getDescByType(){
		if(type==1){
			//奖励类型为铜钱
			return value + "铜钱";
		}else if(type==2){
			//奖励类型为元宝
			return value + "元宝";
		}else if(type==3){
			//奖励类型为魔幻券
			return value + "张神龙券(可以购买一张三星以上卡牌)";
		}else if(type==4){
			//奖励类型为卡牌
			CardManager cardManager = (CardManager)HttpSpringUtils.getBean("cardManager");
			Card card = cardManager.getCardById(new Long(value));
			return card.getColor()+"星卡牌<a href=\"event:card_"+card.getCardId()+ "\"><u>[" + card.getCardName()+ "]</u></a>";
		}else if(type==5){
			//奖励类型为星辰
			RuneManager runeManager = (RuneManager)HttpSpringUtils.getBean("runeManager");
			Rune rune = runeManager.getRuneById(value);
			return rune.getColor()+"星星辰<a href=\"event:rune_"+rune.getRuneId()+ "\"><u>[" + rune.getRuneName()+ "]</u></a>";
		}else if(type==6){
			//神龙碎片
			return "一张神龙碎片(可以在商店中兑换神龙券)";
		}else if(type==7){
			//武器
			SoulManager soulManager = (SoulManager)HttpSpringUtils.getBean("soulManager");
			Soul soul = soulManager.getSoulById(value);
			return soul.getColor()+"星武器["+soul.getSoulName()+ "]";
		}else if(type==8){
			//荣誉点
			return value+"荣誉点(可在龙宫中兑换武器)";
		}else if(type==9){
			//卡牌碎片
			return value + "卡牌碎片(可以合成卡牌)";
		}else if(type==10){
			//星辰碎片
			return value + "张星辰碎片(可以合成星辰)";
		}else if(type==11){
			//进化碎片
			return value + "张进化碎片(可以使卡牌进化)";
		}else if(type==12){
			//经验
			
		}
		return "";
	}
}

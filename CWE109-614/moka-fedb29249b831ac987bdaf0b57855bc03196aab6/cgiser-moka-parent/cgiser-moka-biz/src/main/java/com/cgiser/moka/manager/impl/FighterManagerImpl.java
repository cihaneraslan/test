package com.cgiser.moka.manager.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.DynamicChannelBuffer;
import org.springframework.util.CollectionUtils;

import com.cgiser.core.common.cache.mem.MemCachedManager;
import com.cgiser.moka.manager.AchievementManager;
import com.cgiser.moka.manager.CardManager;
import com.cgiser.moka.manager.EmailManager;
import com.cgiser.moka.manager.FighterManager;
import com.cgiser.moka.manager.HeroLevelManager;
import com.cgiser.moka.manager.LegionManager;
import com.cgiser.moka.manager.MessageManager;
import com.cgiser.moka.manager.PushNotificationManager;
import com.cgiser.moka.manager.RobLogManager;
import com.cgiser.moka.manager.RobRoleCacheManager;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.manager.RuneManager;
import com.cgiser.moka.manager.SalaryManager;
import com.cgiser.moka.manager.SkillManager;
import com.cgiser.moka.manager.SoulManager;
import com.cgiser.moka.manager.StageLevelManager;
import com.cgiser.moka.manager.StageManager;
import com.cgiser.moka.manager.UserCardGroupManager;
import com.cgiser.moka.manager.UserSoulManager;
import com.cgiser.moka.manager.UserStageManager;
import com.cgiser.moka.manager.support.BeanUtils;
import com.cgiser.moka.message.netty.MessageUtil;
import com.cgiser.moka.model.Card;
import com.cgiser.moka.model.FightCard;
import com.cgiser.moka.model.FightResult;
import com.cgiser.moka.model.FightRune;
import com.cgiser.moka.model.HeroLevel;
import com.cgiser.moka.model.Legion;
import com.cgiser.moka.model.LegionTech;
import com.cgiser.moka.model.Legioner;
import com.cgiser.moka.model.Player;
import com.cgiser.moka.model.RankRoleCache;
import com.cgiser.moka.model.RobRoleCache;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.model.Rune;
import com.cgiser.moka.model.Salary;
import com.cgiser.moka.model.SalaryEnum;
import com.cgiser.moka.model.Skill;
import com.cgiser.moka.model.Soul;
import com.cgiser.moka.model.Stage;
import com.cgiser.moka.model.StageLevel;
import com.cgiser.moka.model.StageLevelColor;
import com.cgiser.moka.model.UserAchievement;
import com.cgiser.moka.model.UserCard;
import com.cgiser.moka.model.UserCardGroup;
import com.cgiser.moka.model.UserRune;
import com.cgiser.moka.model.UserSoul;
import com.cgiser.moka.model.UserStage;
import com.cgiser.moka.model.VirtualRole;

public class FighterManagerImpl implements FighterManager {
	private RobLogManager robLogManager;
	private UserCardGroupManager userCardGroupManager;
	private CardManager cardManager;
	private RuneManager runeManager;
	private SkillManager skillManager;
	private SoulManager soulManager;
	private UserSoulManager userSoulManager;
	private HeroLevelManager heroLevelManager;
	private UserStageManager userStageManager;
	private StageManager stageManager;
	private RoleManager roleManager;
	private StageLevelManager stageLevelManager;
	private RobRoleCacheManager robRoleCacheManager;
	private EmailManager emailManager;
	private AchievementManager achievementManager;
	private SalaryManager salaryManager;
	private PushNotificationManager pushNotificationManager;
	private MemCachedManager rankCachedManager;
	private MessageManager messageManager;
	private LegionManager legionManager;
	
	@Override
	public Player initVirtualPlayer(VirtualRole role, String name) {
		Player player = new Player();
		Card card = cardManager.getCardById(new Long(role.getAvatar()));
		player.setName(name);
		player.setAvatar(role.getAvatar());
		HeroLevel heroLevel = heroLevelManager.getHeroLevelByLevel(role.getLevel());
		player.setHP(heroLevel.getHp());
		player.setRemainHP(heroLevel.getHp());
		player.setLevel(role.getLevel());
		player.setNickName(card.getCardName());
		player.setRoleId(role.getRoleId());
		player.setSex(1);
		
		String strCardList = role.getCards();
		String strSoulList = role.getSouls();
		String[] strCards = strCardList.split(",");
		String[] strSouls = strSoulList.split(",");
		ArrayList<FightCard> dHandsCards = new ArrayList<FightCard>();
		int cardId;
		int level;
		int soulId;
		Soul soul;
		FightCard dFightCard;
		Card dCard;
		for(int i=0;i<strCards.length;i++){
			cardId = Integer.parseInt(strCards[i].split("_")[0]);
			level = Integer.parseInt(strCards[i].split("_")[1]);
			soulId = Integer.parseInt(strSouls[i].split("_")[1]);
			dCard = cardManager.getCardById(new Long(cardId));
			dFightCard = new FightCard();
			dFightCard.setCardId(cardId);
			dFightCard.setAttack(dCard.getAttackArray().get(level));
			dFightCard.sethP(dCard.getHpArray().get(level));
			dFightCard.setLevel(level);
			dFightCard.setUserCardId(new Long(i));
			dFightCard.setUUID("def_"+(i+1));
			dFightCard.setWait(dCard.getWait());
			if(soulId>0){
				soul = soulManager.getSoulById(soulId);
				UserSoul userSoul = new UserSoul();
				userSoul.setIsEquipment(1);
				userSoul.setLevel(0);
				userSoul.setRoleId(0L);
				userSoul.setState(0);
				userSoul.setUserSoulId(0L);
				userSoul.setSoulId(soulId);
				dFightCard.setSoul(userSoul);
				dFightCard.setSoulRound(soul.getRound());
			}
			dHandsCards.add(dFightCard);
			player.getCards().add(dFightCard.clone());
		}
		String strRuneList = role.getRunes();
		if(!StringUtils.isEmpty(strRuneList)){
			Rune rune;
			FightRune fightRune;
			Skill skill;
			
			String[] strRunes = strRuneList.split(",");
			int runeId;
			for(int i=0;i<strRunes.length;i++){
				fightRune = new FightRune();
				runeId = Integer.parseInt(strRunes[i].split("_")[0]);
				level = Integer.parseInt(strRunes[i].split("_")[1]);
				rune = runeManager.getRuneById(runeId);
				skill = skillManager.getSkillById(rune.getSkillIdByLevel(level));
				fightRune.setLevel(level);
				fightRune.setRemainTimes(rune.getSkillTimes());
				fightRune.setRuneId(runeId);
				fightRune.setSkill(skill);
				fightRune.setUserRuneId(new Long(i));
				fightRune.setuUID("defrune_"+(i+1));
				player.getRunes().add(fightRune);
			}
		}
		return player;
	}
	@Override
	public Player initPlayer(Role role,String name) {
		Player player = new Player();
		player.setName(name);
		player.setAvatar(role.getAvatar());
		player.setHP(role.getHP());
		player.setRemainHP(role.getHP());
		player.setLevel(role.getLevel());
		player.setNickName(role.getRoleName());
		player.setRoleId(role.getRoleId());
		player.setSex(role.getSex());
		
		UserCardGroup userCardGroup = userCardGroupManager.getUserCardGroupById(new Long(role.getDefaultGroupId()));
		UserCard userCard;
		FightCard fightCard;
		Card card;
		UserSoul userSoul;
		if(userCardGroup!=null&&!CollectionUtils.isEmpty(userCardGroup.getUserCardInfo())){
			for(int i=0;i<userCardGroup.getUserCardInfo().size();i++){
				userCard = userCardGroup.getUserCardInfo().get(i);
				if(userCard==null){
					continue;
				}
				card = cardManager.getCardById(new Long(userCard.getCardId()));
				fightCard = new FightCard();
				fightCard.setAttack(card.getAttackArray().get(userCard.getLevel()));
				fightCard.setCardId(userCard.getCardId());
//				fightCard.setEvolution(0);
				fightCard.sethP(card.getHpArray().get(userCard.getLevel()));
				fightCard.setLevel(userCard.getLevel());
				fightCard.setSkillNew(null);
				fightCard.setUserCardId(userCard.getUserCardId());
				fightCard.setUUID(name+"_"+(i+1));
				fightCard.setWait(card.getWait());
				//战魂武魂增效
				if(userCard.getUserSoul()!=null){
					userSoul = userSoulManager.getUserSoulById(userCard.getUserSoul().getUserSoulId());
					fightCard.setSoul(userSoul);
					fightCard.setSoulRound(soulManager.getSoulById(userSoul.getSoulId()).getRound());
				}
				
				player.getCards().add(fightCard);
			}
		}
		Legioner legioner = legionManager.getLegioner(role.getRoleId());
		if(legioner!=null){
			Legion legion = legionManager.getLegionById(legioner.getLegionId());
			if(legion!=null){
				List<LegionTech> techs =legionManager.getLegionTechs();
				LegionTech legionTech;
				for(int i=0;i<techs.size();i++){
					legionTech = techs.get(i);
					if(legion.getLegionLevel()>=legionTech.getLegionLevel()){
						legionTech.setLock(1);
						Long resource = (Long)BeanUtils.getFieldValueByName("contribute"+legionTech.getTechId().replace("tech", ""), legion);
						int level = (int)(legionTech.getLegionTechLevel(resource));
						if(level>0){
							for(FightCard fightCard2:player.getCards()){
								card = cardManager.getCardById(new Long(fightCard2.getCardId()));
								if(card.getRace()==legionTech.getRace()){
									if(legionTech.getType()==1){
										fightCard2.sethP(fightCard2.gethP()+fightCard2.gethP()*(int)(level*legionTech.getAnd())/100);
									}
									if(legionTech.getType()==2){
										fightCard2.setAttack(fightCard2.getAttack()+fightCard2.getAttack()*(int)(level*legionTech.getAnd())/100);
									}
								}
							}
						}
					}
				}
			}
		}

		if(userCardGroup!=null&&userCardGroup.getUserRuneInfo()!=null){
			UserRune userRune;
			Rune rune;
			FightRune fightRune;
			Skill skill;
			for(int i=0;i<userCardGroup.getUserRuneInfo().size();i++){
				fightRune = new FightRune();
				userRune = userCardGroup.getUserRuneInfo().get(i);
				rune = runeManager.getRuneById(userRune.getRuneId());
				skill = skillManager.getSkillById(rune.getSkillIdByLevel(userRune.getLevel()));
				fightRune.setLevel(userRune.getLevel());
				fightRune.setRemainTimes(rune.getSkillTimes());
				fightRune.setRuneId(userRune.getRuneId());
				fightRune.setSkill(skill);
				fightRune.setUserRuneId(userRune.getUserRuneId());
				fightRune.setuUID(name+"rune_"+(i+1));
				player.getRunes().add(fightRune);
			}
		}
		
		return player;
	}
	@Override
	public Player createHeroPlayer(int stageId,Role role) {
		
		Player defendPlayer = new Player();
		defendPlayer.setName("def");
		Stage stage = stageManager.getStageById(stageId);
		defendPlayer.setAvatar(stage.getFightImg());
		UserStage userStage = userStageManager.getUserStageByRoleIdStageId(role.getRoleId(), stageId);
		StageLevel stageLevel = stage.getLevels().get(userStage==null?0:userStage.getFinishedStage());
		String strCardList = stageLevel.getCardList();
		String strSoulList = stageLevel.getSoulList();
		String[] strCards = strCardList.split(",");
		String[] strSouls = strSoulList.split(",");
		ArrayList<FightCard> dHandsCards = new ArrayList<FightCard>();
		int cardId;
		int level;
		int soulId;
		Soul soul;
		FightCard dFightCard;
		Card dCard;
		for(int i=0;i<strCards.length;i++){
			cardId = Integer.parseInt(strCards[i].split("_")[0]);
			level = Integer.parseInt(strCards[i].split("_")[1]);
			soulId = Integer.parseInt(strSouls[i].split("_")[1]);
			dCard = cardManager.getCardById(new Long(cardId));
			dFightCard = new FightCard();
			dFightCard.setCardId(cardId);
			dFightCard.setAttack(dCard.getAttackArray().get(level));
			dFightCard.sethP(dCard.getHpArray().get(level));
			dFightCard.setLevel(level);
			dFightCard.setUserCardId(new Long(i));
			dFightCard.setUUID("def_"+(i+1));
			dFightCard.setWait(dCard.getWait());
			if(soulId>0){
				soul = soulManager.getSoulById(soulId);
				UserSoul userSoul = new UserSoul();
				userSoul.setIsEquipment(1);
				userSoul.setLevel(0);
				userSoul.setRoleId(0L);
				userSoul.setState(0);
				userSoul.setUserSoulId(0L);
				userSoul.setSoulId(soulId);
				dFightCard.setSoul(userSoul);
				dFightCard.setSoulRound(soul.getRound());
			}
			dHandsCards.add(dFightCard);
			defendPlayer.getCards().add(dFightCard.clone());
		}
		String strRuneList = stageLevel.getRuneList();
		if(!StringUtils.isEmpty(strRuneList)){
			Rune rune;
			FightRune fightRune;
			Skill skill;
			
			String[] strRunes = strRuneList.split(",");
			int runeId;
			for(int i=0;i<strRunes.length;i++){
				fightRune = new FightRune();
				runeId = Integer.parseInt(strRunes[i].split("_")[0]);
				level = Integer.parseInt(strRunes[i].split("_")[1]);
				rune = runeManager.getRuneById(runeId);
				skill = skillManager.getSkillById(rune.getSkillIdByLevel(level));
				fightRune.setLevel(level);
				fightRune.setRemainTimes(rune.getSkillTimes());
				fightRune.setRuneId(runeId);
				fightRune.setSkill(skill);
				fightRune.setUserRuneId(new Long(i));
				fightRune.setuUID("defrune_"+(i+1));
				defendPlayer.getRunes().add(fightRune);
			}
		}

		HeroLevel heroLevel = heroLevelManager.getHeroLevelByLevel(stageLevel.getHeroLevel());
		defendPlayer.setHP(heroLevel.getHp());
		defendPlayer.setLevel(stageLevel.getHeroLevel());
		defendPlayer.setNickName(stage.getFightName());
		defendPlayer.setRemainHP(heroLevel.getHp());
		defendPlayer.setRoleId(role.getRoleId());
		return defendPlayer;
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
	public RuneManager getRuneManager() {
		return runeManager;
	}
	public void setRuneManager(RuneManager runeManager) {
		this.runeManager = runeManager;
	}
	public SkillManager getSkillManager() {
		return skillManager;
	}
	public void setSkillManager(SkillManager skillManager) {
		this.skillManager = skillManager;
	}
	public SoulManager getSoulManager() {
		return soulManager;
	}
	public void setSoulManager(SoulManager soulManager) {
		this.soulManager = soulManager;
	}
	public UserSoulManager getUserSoulManager() {
		return userSoulManager;
	}
	public void setUserSoulManager(UserSoulManager userSoulManager) {
		this.userSoulManager = userSoulManager;
	}
	public HeroLevelManager getHeroLevelManager() {
		return heroLevelManager;
	}
	public void setHeroLevelManager(HeroLevelManager heroLevelManager) {
		this.heroLevelManager = heroLevelManager;
	}
	public UserStageManager getUserStageManager() {
		return userStageManager;
	}
	public void setUserStageManager(UserStageManager userStageManager) {
		this.userStageManager = userStageManager;
	}
	public StageManager getStageManager() {
		return stageManager;
	}
	public void setStageManager(StageManager stageManager) {
		this.stageManager = stageManager;
	}
	@Override
	public void handlerFightResult(Player aPlayer, Player dPlayer,
			int type,FightResult result) {
		int winExp = 0;
		int winCoins = 0;
		if(type==1){
			//匹配战
			Role aRole = roleManager.getRoleByName(aPlayer.getNickName());
//			MatchRoleCache matchRoleCache = (MatchRoleCache)matchCachedManager.get(String.valueOf(aRole.getRoleId()));
//			matchRoleCache.setMatchHasTimes(matchRoleCache.getMatchHasTimes()-1);
//			matchRoleCache.setMatchLastTime(new Date());
//			matchCachedManager.set(String.valueOf(aRole.getRoleId()), 0, matchRoleCache);
			Role dRole = roleManager.getRoleByName(dPlayer.getNickName());
//			matchRoleCache = (MatchRoleCache)matchCachedManager.get(String.valueOf(dRole.getRoleId()));
//			matchRoleCache.setMatchHasTimes(matchRoleCache.getMatchHasTimes()-1);
//			matchRoleCache.setMatchLastTime(new Date());
//			matchCachedManager.set(String.valueOf(dRole.getRoleId()), 0, matchRoleCache);
			int isWin = this.isWin(aPlayer, dPlayer);
			if(isWin==1){
				UserAchievement userAchievement = achievementManager.getUserAchievementById(dRole.getRoleId(), 41);
				if(userAchievement==null){
					achievementManager.saveUserAchievement(41,dRole.getRoleId(),1);
				}
			}else if(isWin==2){
				UserAchievement userAchievement = achievementManager.getUserAchievementById(aRole.getRoleId(), 41);
				if(userAchievement==null){
					achievementManager.saveUserAchievement(41,aRole.getRoleId(),1);
				}
			}
		}else if(type==2){
			//排名战
			Role role = roleManager.getRoleByName(aPlayer.getNickName());
			Role dRole = roleManager.getRoleByName(dPlayer.getNickName());
			int win = isWin(aPlayer, dPlayer);
			if(win==2){
				//排名战打败后，交换排名
				if(role.getRank()>dRole.getRank()){
					roleManager.swapRoleRank(role, dRole);
					if(dRole.getLevel()>=11){
						emailManager.sendEmail("排名战通知", "您在排名战中被"+role.getRoleName()+"打败，排名倒退了"+(role.getRank()-dRole.getRank())+"位，赶紧去打回来吧！", 0L, dRole.getRoleId(), 1);
						pushNotificationManager.pushNotification(dRole.getRoleId(), "", "亲，您在排名战中被"+role.getRoleName()+"打败，排名倒退了"+(role.getRank()-dRole.getRank())+"位，赶紧去打回来吧！");
					}
				}
//				winExp = this.getRankWinExp(role.getRank());
//				winCoins = this.getRankWinCoins(role.getRank());
				role = roleManager.getRoleByName(aPlayer.getNickName());
				//通知被挑战者被人打败
				RankRoleCache rankRoleCache = (RankRoleCache)rankCachedManager.get("rank_"+String.valueOf(role.getRoleId()));
				if(rankRoleCache==null){
					rankRoleCache = new RankRoleCache();
					rankRoleCache.setRank(role.getRank());
					rankRoleCache.setRankStreakTimes(0);
				}
				UserAchievement userAchievement = achievementManager.getUserAchievementById(role.getRoleId(), 38); 
				if(userAchievement==null){
					achievementManager.saveUserAchievement(38, role.getRoleId(),1);
				}
				rankRoleCache.setRankStreakTimes(rankRoleCache.getRankStreakTimes()+1);
				
				if(rankRoleCache.getRankStreakTimes()<=10){
					userAchievement = achievementManager.getUserAchievementById(role.getRoleId(), 39); 
					if(userAchievement==null){
						achievementManager.saveUserAchievement(39, role.getRoleId(),rankRoleCache.getRankStreakTimes());
					}
					if(userAchievement!=null&&userAchievement.getFinishState()<10){
						achievementManager.saveUserAchievement(39, role.getRoleId(),rankRoleCache.getRankStreakTimes());
					}
				}
				if(role.getRank()<=500){
					userAchievement = achievementManager.getUserAchievementById(role.getRoleId(), 40); 
					if(userAchievement==null){
						achievementManager.saveUserAchievement(40, role.getRoleId(),1);
					}
				}
				rankRoleCache.setRankFightTime(new Date());
				rankRoleCache.setRankHasTimes(rankRoleCache.getRankHasTimes()-1);
				rankCachedManager.set("rank_"+String.valueOf(role.getRoleId()), 0, rankRoleCache);

			}else{
				RankRoleCache rankRoleCache = (RankRoleCache)rankCachedManager.get("rank_"+String.valueOf(role.getRoleId()));
				if(rankRoleCache==null){
					rankRoleCache = new RankRoleCache();
					rankRoleCache.setRank(role.getRank());
					rankRoleCache.setRankFightTime(new Date());
					rankRoleCache.setRankStreakTimes(0);
				}
				rankRoleCache.setRankStreakTimes(0);
				rankRoleCache.setRankFightTime(new Date());
				rankRoleCache.setRankHasTimes(rankRoleCache.getRankHasTimes()-1);
				rankCachedManager.set("rank_"+String.valueOf(role.getRoleId()), 0, rankRoleCache);
				UserAchievement userAchievement = achievementManager.getUserAchievementById(role.getRoleId(), 39); 
				if(userAchievement!=null&&userAchievement.getFinishState()>0&&userAchievement.getFinishState()<10){
					achievementManager.saveUserAchievement(39, role.getRoleId(),rankRoleCache.getRankStreakTimes());
				}
//				emailManager.sendEmail("排名战通知", "您在排名战中被"+role.getRoleName()+"打败，排名保持原位了！", 0L, dRole.getRoleId(), 1);
			}
			
		}else if(type==3){
			//入侵玩家
			int isWin = this.isWin(aPlayer, dPlayer);
			Role role = roleManager.getRoleByName(aPlayer.getNickName());
			Role dRole = roleManager.getRoleByName(dPlayer.getNickName());
			RobRoleCache robRoleCache = robRoleCacheManager.getRobRoleCache(role.getRoleId());
			RobRoleCache robRoleCache1 = robRoleCacheManager.getRobRoleCache(dRole.getRoleId());
			if(robRoleCache1==null){
				robRoleCache1 = new RobRoleCache();
				robRoleCache1.setHasBeenRobCoins(0);
				robRoleCache1.setRobHasTimes(5);
				robRoleCache1.setRobRefreshTimes(2);
				robRoleCache1.setRobRoleName("");
				robRoleCache1.setRobStreakTimes(0);
				robLogManager.infoRobLog(role.getRoleId(), dRole.getRoleId(), 0);
			}
			if(isWin==2){
				robRoleCache.setRobStreakTimes(robRoleCache.getRobStreakTimes()+1);
				robRoleCache.setRobRoleName("");
				if(robRoleCache.getRobStreakTimes()<=5){
					UserAchievement userAchievement = achievementManager.getUserAchievementById(role.getRoleId(), 42);
					if(userAchievement==null){
						achievementManager.saveUserAchievement(42,role.getRoleId(),robRoleCache.getRobStreakTimes());
					}
					if(userAchievement!=null&&userAchievement.getFinishState()<5){
						achievementManager.saveUserAchievement(42,role.getRoleId(),robRoleCache.getRobStreakTimes());
					}
				}
				int coins = userStageManager.getRoleMapCoins(dRole.getRoleId());
				
				int lastCoins = 0;
				if(robRoleCache1!=null){
					lastCoins = robRoleCache1.getHasBeenRobCoins();
				}
				lastCoins = coins - lastCoins;
				if(coins<2000){
					//后面奖励已经发放给玩家
//					roleManager.addCoin(role.getRoleName(), 1000);
					winCoins = 1000;
				}else{
					if((lastCoins-coins/2)<1000){
//						roleManager.addCoin(role.getRoleName(), 1000);
						winCoins = 1000;
						robLogManager.infoRobLog(role.getRoleId(), dRole.getRoleId(), 0);
					}else{
//						roleManager.addCoin(role.getRoleName(), coins/10);
						winCoins = coins/10;
						robRoleCache1.setHasBeenRobCoins(robRoleCache1.getHasBeenRobCoins()+coins/10);
						robLogManager.infoRobLog(role.getRoleId(), dRole.getRoleId(), coins/10);
					}
					pushNotificationManager.pushNotification(dRole.getRoleId(), "", "亲，"+role.getRoleName()+"入侵了您，抢走您"+winCoins+"铜钱，赶紧去复仇吧！");
				} 
			}else{
				robRoleCache.setRobStreakTimes(0);
				robRoleCache.setRobRoleName("");
			}
			
			robRoleCache.setRobLastTime(new Date());
			robRoleCache.setRobHasTimes(robRoleCache.getRobHasTimes()-1);
			robRoleCacheManager.updateRobRoleCache(role.getRoleId(), robRoleCache);
			robRoleCacheManager.updateRobRoleCache(dRole.getRoleId(), robRoleCache1);
		}else if(type==4){
			//帮派战结束后处理逻辑
		}else{
			//周边玩家自由切磋
			Role aRole = roleManager.getRoleByName(aPlayer.getNickName());
			Role dRole = roleManager.getRoleByName(dPlayer.getNickName());
			UserAchievement userAchievement = achievementManager.getUserAchievementById(aRole.getRoleId(), 43);
			if(userAchievement==null){
				achievementManager.saveUserAchievement(43,aRole.getRoleId(),1);
			}
			userAchievement = achievementManager.getUserAchievementById(dRole.getRoleId(), 43);
			if(userAchievement==null){
				achievementManager.saveUserAchievement(43,dRole.getRoleId(),1);
			}
			String roleName1 = "";
			String roleName2 = "";
			if(this.isWin(aPlayer, dPlayer)==1){
				roleName1 = dPlayer.getNickName();
				roleName2 = aPlayer.getNickName();
			}else{
				roleName1 = aPlayer.getNickName();
				roleName2 = dPlayer.getNickName();
			}
	
			if (StringUtils.isEmpty(roleName1) || StringUtils.isEmpty(roleName2)) {
				return;
			}
			ChannelBuffer responseBuffer = new DynamicChannelBuffer(200);
			responseBuffer.writeInt(1002);
			MessageUtil.writeString(responseBuffer, roleName1, "UTF-8");
			MessageUtil.writeString(responseBuffer, roleName2, "UTF-8");
			responseBuffer.writeInt(1);
			MessageUtil.writeString(responseBuffer, "小样,再练练吧", "UTF-8");
			messageManager.sendMessageToRole(roleName2,responseBuffer);
		}
		this.setFightResult(aPlayer, dPlayer, type, result,winExp,winCoins);
	}
	private void setFightResult(Player aPlayer, Player dPlayer,
			int type,FightResult fightResult,int exp,int coins){
		//输的玩家的奖励
		List<Salary> salaries1 = new ArrayList<Salary>();
		//赢的玩家的奖励
		List<Salary> salaries2 = new ArrayList<Salary>();
		//当前玩家
		Role role = roleManager.getRoleByName(aPlayer.getNickName());
		//对手玩家
		Role dRole = roleManager.getRoleByName(dPlayer.getNickName());
		List<String> bonus = new ArrayList<String>();
		bonus.add("Exp_0");
		bonus.add("Coins_0");
		fightResult.getExtData().setBonus(bonus);
		fightResult.getExtData().setStarUp(0);
		fightResult.getExtData().setExp(role.getExp());
		fightResult.getExtData().setPrevExp(role.getPrevExp());
		fightResult.getExtData().setNextExp(role.getNextExp());
		fightResult.getExtData().setUserLevel(role.getLevel());
		//铜钱奖励
		Salary salary = new Salary();
		salary.setRoleId(role.getRoleId());
		salary.setTime(new Date());
		salary.setType(SalaryEnum.StageSalary.getValue());
		salary.setAwardType(1);
		salary.setAwardValue(0);
		salaries1.add(salary);
		salaries2.add(salary);
		//经验奖励
		salary = new Salary();
		salary.setRoleId(role.getRoleId());
		salary.setTime(new Date());
		salary.setType(SalaryEnum.StageSalary.getValue());
		salary.setAwardType(12);
		salary.setAwardValue(0);
		salaries1.add(salary);
		salaries2.add(salary);
		//战斗奖励在处理战斗结果时一起发放(不分输赢)
		if(type==1){
			salary = new Salary();
			salary.setRoleId(role.getRoleId());
			salary.setTime(new Date());
			salary.setAwardType(8);
			salary.setAwardValue(5);
			salary.setType(SalaryEnum.MatchGameSalary.getValue());
			salaries1.add(salary);
			
		}
		List<StageLevelColor> stageLevelColors = null;
		Random rnd = new Random();
		//匹配战奖励
		if(type==1){
			if(role.getLevel()<30){
				stageLevelColors = stageLevelManager.getStageLevelColor(1008);
			}else if(role.getLevel()>=30&&role.getLevel()<45){
				stageLevelColors = stageLevelManager.getStageLevelColor(1009);
			}else{
				stageLevelColors = stageLevelManager.getStageLevelColor(1010);
			}
			if(!CollectionUtils.isEmpty(stageLevelColors)){
				for(StageLevelColor stageLevelColor:stageLevelColors){
					if(rnd.nextInt(100)<stageLevelColor.getColor()){
						salary = new Salary();
						salary.setRoleId(role.getRoleId());
						salary.setTime(new Date());
						salary.setAwardType(stageLevelColor.getType());
						salary.setAwardValue(stageLevelColor.getValue());
						salary.setType(SalaryEnum.StageSalary.getValue());
						salaries2.add(salary);
						break;
					}
				}
				
			}
			int r = rnd.nextInt(100);
			stageLevelColors = stageLevelManager.getStageLevelColor(1011);
			if(!CollectionUtils.isEmpty(stageLevelColors)){
				for(StageLevelColor stageLevelColor:stageLevelColors){
					if(r<stageLevelColor.getColor()){
						salary = new Salary();
						salary.setRoleId(role.getRoleId());
						salary.setTime(new Date());
						salary.setAwardType(stageLevelColor.getType());
						salary.setAwardValue(stageLevelColor.getValue());
						salary.setType(SalaryEnum.StageSalary.getValue());
						salaries2.add(salary);
						break;
					}
				}
			}
		}
		
		//入侵战奖励
		if(type==3){
			salaries2.get(0).setAwardValue(coins);
			salaries2.get(1).setAwardValue(exp);
			int r = rnd.nextInt(100);
			stageLevelColors = stageLevelManager.getStageLevelColor(1012);
			if(!CollectionUtils.isEmpty(stageLevelColors)){
				for(StageLevelColor stageLevelColor:stageLevelColors){
					if(r<stageLevelColor.getColor()){
						salary = new Salary();
						salary.setRoleId(role.getRoleId());
						salary.setTime(new Date());
						salary.setAwardType(stageLevelColor.getType());
						salary.setAwardValue(stageLevelColor.getValue());
						salary.setType(SalaryEnum.StageSalary.getValue());
						salaries2.add(salary);
						break;
					}
				}
			}
		}
		//自由切磋
		if(type==0){
			int r = rnd.nextInt(100);
			stageLevelColors = stageLevelManager.getStageLevelColor(1013);
			if(!CollectionUtils.isEmpty(stageLevelColors)){
				for(StageLevelColor stageLevelColor:stageLevelColors){
					if(r<stageLevelColor.getColor()){
						salary = new Salary();
						salary.setRoleId(role.getRoleId());
						salary.setTime(new Date());
						salary.setAwardType(stageLevelColor.getType());
						salary.setAwardValue(stageLevelColor.getValue());
						salary.setType(SalaryEnum.StageSalary.getValue());
						salaries2.add(salary);
						break;
					}
				}
			}
		}
		if(aPlayer.getHP()<=0||(aPlayer.getFightCards().size()==0&&aPlayer.getPreCards().size()==0&&aPlayer.getHandsCards().size()==0)){
			fightResult.setWin(2);
			if(type==4){
				salary = new Salary();
				salary.setRoleId(role.getRoleId());
				salary.setTime(new Date());
				salary.setAwardType(14);
				salary.setAwardValue(15);
				salary.setType(SalaryEnum.StageSalary.getValue());
				salaries1.add(salary);
			}
			roleManager.addLostTimes(role.getRoleId());
			if(type==1){
				roleManager.addWinTimes(dRole.getRoleId());
			}else if(type==2){
				roleManager.addRankLostTimes(role.getRoleId());
			}else if(type==3){
				roleManager.addRankLostTimes(role.getRoleId());
			}else{
				roleManager.addWinTimes(dRole.getRoleId());
			}	
			for(Salary salary2:salaries1){
				salaryManager.extendSalaryAdd(salary2.getAwardType(), salary2.getAwardValue(), role.getRoleId());
			}
			if(type==1||type==0){
				for(Salary salary2:salaries2){
					salaryManager.extendSalaryAdd(salary2.getAwardType(), salary2.getAwardValue(), dRole.getRoleId());
				}
				fightResult.setdSalaries(salaries2);
			}

			fightResult.setSalaries(salaries1);
			
		}
		if(dPlayer.getHP()<=0||(dPlayer.getFightCards().size()==0&&dPlayer.getPreCards().size()==0&&dPlayer.getHandsCards().size()==0)){
			roleManager.addWinTimes(role.getRoleId());
			if(type==4){
				salary = new Salary();
				salary.setRoleId(role.getRoleId());
				salary.setTime(new Date());
				salary.setAwardType(14);
				salary.setAwardValue(40);
				salary.setType(SalaryEnum.StageSalary.getValue());
				salaries2.add(salary);
			}
			if(type==1){
				roleManager.addLostTimes(dRole.getRoleId());
			}else if(type==2){
				roleManager.addRankWinTimes(role.getRoleId());
			}else if(type==3){
				roleManager.addRankWinTimes(role.getRoleId());
			}else{
				roleManager.addLostTimes(dRole.getRoleId());
			}
			fightResult.setWin(1);
			if(type==1||type==0){
				for(Salary salary2:salaries1){
					salaryManager.extendSalaryAdd(salary2.getAwardType(), salary2.getAwardValue(), dRole.getRoleId());
				}
				fightResult.setdSalaries(salaries1);
			}
			for(Salary salary2:salaries2){
				salaryManager.extendSalaryAdd(salary2.getAwardType(), salary2.getAwardValue(), role.getRoleId());
			}
			fightResult.setSalaries(salaries2);
			
		}
		
	}
	/**
	 * aPlayer is win return 1 
	 * dPlayer is win return 2 
	 * @param aPlayer
	 * @param dPlayer
	 * @return
	 */
	private int isWin(Player aPlayer,Player dPlayer){
		if(aPlayer.getHP()<=0||(aPlayer.getFightCards().size()==0&&aPlayer.getPreCards().size()==0&&aPlayer.getHandsCards().size()==0)){
			return 1;
		}
		if(dPlayer.getHP()<=0||(dPlayer.getFightCards().size()==0&&dPlayer.getPreCards().size()==0&&dPlayer.getHandsCards().size()==0)){
			return 2;
		}
		return 0;
	}
	private int getRankWinExp(int rank){
		if(rank>2000){
			return 200+new Random().nextInt(100);
		}
		if(rank>1000){
			return 300+new Random().nextInt(100);
		}
		if(rank>500){
			return 400+new Random().nextInt(100);
		}
		if(rank>300){
			return 500+new Random().nextInt(600);
		}
		if(rank>100){
			return 600+new Random().nextInt(200);
		}
		if(rank>10){
			return 700+new Random().nextInt(400);
		}
		if(rank>0){
			return 1000+new Random().nextInt(300);
		}
		return 0;
	}
	private int getRankWinCoins(int rank){
		if(rank>2000){
			return 150+new Random().nextInt(200);
		}
		if(rank>1000){
			return 200+new Random().nextInt(300);
		}
		if(rank>500){
			return 300+new Random().nextInt(300);
		}
		if(rank>300){
			return 400+new Random().nextInt(300);
		}
		if(rank>100){
			return 500+new Random().nextInt(400);
		}
		if(rank>10){
			return 700+new Random().nextInt(500);
		}
		if(rank>0){
			return 1000+new Random().nextInt(500);
		}
		return 0;
	}
	public RoleManager getRoleManager() {
		return roleManager;
	}
	public void setRoleManager(RoleManager roleManager) {
		this.roleManager = roleManager;
	}
	public RobRoleCacheManager getRobRoleCacheManage() {
		return robRoleCacheManager;
	}
	public void setRobRoleCacheManage(RobRoleCacheManager robRoleCacheManager) {
		this.robRoleCacheManager = robRoleCacheManager;
	}
	public RobLogManager getRobLogManager() {
		return robLogManager;
	}
	public void setRobLogManager(RobLogManager robLogManager) {
		this.robLogManager = robLogManager;
	}
	public RobRoleCacheManager getRobRoleCacheManager() {
		return robRoleCacheManager;
	}
	public void setRobRoleCacheManager(RobRoleCacheManager robRoleCacheManager) {
		this.robRoleCacheManager = robRoleCacheManager;
	}
	public SalaryManager getSalaryManager() {
		return salaryManager;
	}
	public void setSalaryManager(SalaryManager salaryManager) {
		this.salaryManager = salaryManager;
	}
	public StageLevelManager getStageLevelManager() {
		return stageLevelManager;
	}
	public void setStageLevelManager(StageLevelManager stageLevelManager) {
		this.stageLevelManager = stageLevelManager;
	}
	public EmailManager getEmailManager() {
		return emailManager;
	}
	public void setEmailManager(EmailManager emailManager) {
		this.emailManager = emailManager;
	}
	public PushNotificationManager getPushNotificationManager() {
		return pushNotificationManager;
	}
	public void setPushNotificationManager(
			PushNotificationManager pushNotificationManager) {
		this.pushNotificationManager = pushNotificationManager;
	}
	public MemCachedManager getRankCachedManager() {
		return rankCachedManager;
	}
	public void setRankCachedManager(MemCachedManager rankCachedManager) {
		this.rankCachedManager = rankCachedManager;
	}
	public AchievementManager getAchievementManager() {
		return achievementManager;
	}
	public void setAchievementManager(AchievementManager achievementManager) {
		this.achievementManager = achievementManager;
	}
	public MessageManager getMessageManager() {
		return messageManager;
	}
	public void setMessageManager(MessageManager messageManager) {
		this.messageManager = messageManager;
	}
	public LegionManager getLegionManager() {
		return legionManager;
	}
	public void setLegionManager(LegionManager legionManager) {
		this.legionManager = legionManager;
	}


}

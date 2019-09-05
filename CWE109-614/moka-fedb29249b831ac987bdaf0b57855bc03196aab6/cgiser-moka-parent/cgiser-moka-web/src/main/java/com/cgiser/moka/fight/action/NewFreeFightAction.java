package com.cgiser.moka.fight.action;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cgiser.core.common.cache.mem.MemCachedManager;
import com.cgiser.keel.http.utils.HttpSpringUtils;
import com.cgiser.moka.action.AbstractAction;
import com.cgiser.moka.common.utils.ListUtil;
import com.cgiser.moka.common.utils.ServletUtil;
import com.cgiser.moka.dao.util.DigestUtils;
import com.cgiser.moka.manager.FightManager;
import com.cgiser.moka.manager.FighterManager;
import com.cgiser.moka.manager.HeroLevelManager;
import com.cgiser.moka.manager.LegionFightManager;
import com.cgiser.moka.manager.LegionManager;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.manager.SalaryManager;
import com.cgiser.moka.manager.support.BeanUtils;
import com.cgiser.moka.manager.support.RoleStatusScanThread;
import com.cgiser.moka.model.ExtData;
import com.cgiser.moka.model.FightCard;
import com.cgiser.moka.model.FightResult;
import com.cgiser.moka.model.HeroLevel;
import com.cgiser.moka.model.Legion;
import com.cgiser.moka.model.LegionFight;
import com.cgiser.moka.model.Legioner;
import com.cgiser.moka.model.Player;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.result.ReturnType;

public class NewFreeFightAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		RoleManager roleManager = (RoleManager)HttpSpringUtils.getBean("roleManager");
		ReturnType<FightResult> returnType = new ReturnType<FightResult>();
		Role role = super.getCurrentRole(request);
		if(null==role){
			returnType.setMsg("您还没登录哦,亲！");
			returnType.setStatus(0);
			super.printReturnType2Response(response, returnType);
			return null;
		}
		String strGuard = ServletUtil.getDefaultValue(request, "guard", "null");
		if(!StringUtils.isNumeric(strGuard)){
			returnType.setMsg("该守卫已经被攻破了！");
			returnType.setStatus(0);
			super.printReturnType2Response(response, returnType);
			return null;
		}
		
		LegionManager legionManager = (LegionManager)HttpSpringUtils.getBean("legionManager");
		Legioner legioner = legionManager.getLegioner(role.getRoleId());
		if(legioner==null){
			returnType.setMsg("您还没加入帮派哦");
			returnType.setStatus(0);
			super.printReturnType2Response(response, returnType);
			return null;
		}
		if(legioner.getLastAttack()<1){
			returnType.setMsg("您的帮令已经用完了");
			returnType.setStatus(0);
			super.printReturnType2Response(response, returnType);
			return null;
		}
		LegionFightManager legionFightManager = (LegionFightManager)HttpSpringUtils.getBean("legionFightManager");
		Legion attack = legionManager.getLegionById(legioner.getLegionId());
		if(attack==null){
			returnType.setMsg("您的帮派已解散哦");
			returnType.setStatus(0);
			super.printReturnType2Response(response, returnType);
			return null;
		}
		LegionFight legionFight = legionFightManager.getLegionFightByattack(attack.getId());
		if(legionFightManager.isEnd(legionFight)){
			returnType.setMsg("帮派战已经结束了,亲！");
			returnType.setStatus(0);
			super.printReturnType2Response(response, returnType);
			return null;
		}
		int guard = Integer.parseInt(strGuard);
		int level = legionFight.getLevel();
		if((level<5&&guard>5)||guard>9){
			returnType.setMsg("该守卫不存在,亲！");
			returnType.setStatus(0);
			super.printReturnType2Response(response, returnType);
			return null;
		}
		String guardInfo = (String)BeanUtils.getFieldValueByName("guard"+guard, legionFight);
		Long aRoleId = new Long(guardInfo.split("_")[2]);
		Long dRoleId = new Long(guardInfo.split("_")[0]);
		int win = Integer.parseInt(guardInfo.split("_")[1]);
		if(win==1){
			returnType.setMsg("您还没选择对手,亲！");
			returnType.setStatus(0); 
			super.printReturnType2Response(response, returnType);
			return null;
		}
		if(dRoleId<=0){
			if(aRoleId==0){
				legionManager.updateRoleLegionFightAttackTimes(legioner.getId());
				legionFightManager.defeatGuard(legionFight.getId(), guard, role.getRoleId());
				SalaryManager salaryManager = (SalaryManager)HttpSpringUtils.getBean("salaryManager");
				salaryManager.extendSalaryAdd(14, 20, role.getRoleId());
				if(legioner.getLastContribute1()==0){
					legionManager.addRoleLegionContribute(legioner.getId(), 1);
				}
				if(legioner.getLastContribute2()==0){
					legionManager.addRoleLegionContribute(legioner.getId(), 2);
				}
				if(legioner.getLastContribute3()==0){
					legionManager.addRoleLegionContribute(legioner.getId(), 3);
				}
			}
			returnType.setMsg("该守卫已经被攻破了！");
			returnType.setStatus(0);
			super.printReturnType2Response(response, returnType);
			return null;
		}
		MemCachedManager groupCachedManager = (MemCachedManager)HttpSpringUtils.getBean("groupCachedManager");
		try{
			if(!groupCachedManager.add("group_"+DigestUtils.digest(legionFight.getDefend()+"guard"+dRoleId), 10*60, role.getRoleName()+"_"+guard)){
				String roleName = (String)groupCachedManager.get("group_"+DigestUtils.digest(legionFight.getDefend()+"guard"+dRoleId));
				returnType.setMsg("该守卫正在被"+roleName.split("_")[0]+"攻打,亲！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			Role dRole = roleManager.getRoleById(dRoleId);
			if(dRole==null){
				returnType.setMsg("该守卫已经被攻破了！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			//减少帮派成员的帮战次数
			legionManager.updateRoleLegionFightAttackTimes(legioner.getId());
			//设置角色状态为忙碌
			role.setStatus(1);
			roleManager.updateRoleStatus(role.getRoleName(), 1);
			role = roleManager.getRoleByName(role.getRoleName());
			//构造攻击玩家
			FighterManager fighterManager = (FighterManager)HttpSpringUtils.getBean("fighterManager");
			Player attackPlayer = fighterManager.initPlayer(role,"atk");
			List<FightCard> aHandsCards = ListUtil.copyTo(attackPlayer.getCards(), FightCard.class);
			Collections.shuffle(aHandsCards);
			//构造对手玩家
			Player defendPlayer = fighterManager.initPlayer(dRole,"def");
			defendPlayer.setName("def");
			String strIsHand = ServletUtil.getDefaultValue(request, "ishand", null);
			boolean isHand = false;
			if(strIsHand.equals("1")){
				isHand = true;
			}
			//帮派战是手动战斗
			attackPlayer.setHand(isHand);
			defendPlayer.setHand(false);
			List<FightCard> dHandsCards = ListUtil.copyTo(defendPlayer.getCards(), FightCard.class);
			Collections.shuffle(dHandsCards);
			FightManager fightManager = (FightManager)HttpSpringUtils.getBean("fightManager");
			FightResult result = fightManager.runFight(attackPlayer, defendPlayer, 4);
			
			HeroLevelManager heroLevelManager = (HeroLevelManager)HttpSpringUtils.getBean("heroLevelManager");
			ExtData extData = new ExtData();
			extData.UserLevel = role.getLevel();
			HeroLevel roleLevel = heroLevelManager.getHeroLevelByLevel(role.getLevel());
			extData.Exp = roleLevel.getExp();
			if(roleLevel.getLevel()>2){
				extData.setPrevExp(heroLevelManager.getHeroLevelByLevel(role.getLevel()-1).getExp());
			}else{
				extData.setPrevExp(0);
			}
			if(roleLevel.getLevel()>=100){
				extData.setNextExp(extData.getExp());
			}else{
				extData.setNextExp(heroLevelManager.getHeroLevelByLevel(role.getLevel()+1).getExp());
			}
			result.setExtData(extData);
			returnType.setStatus(1);
			returnType.setValue(result);
			//初始化游戏创建房间
			if(isWin(attackPlayer, defendPlayer)){
				RoleStatusScanThread.freeFightRoom.remove(result.getBattleId());
				roleManager.updateRoleStatus(role.getRoleName(), 0);
				if(isWinner(attackPlayer, defendPlayer)){
					legionFightManager.defeatGuard(legionFight.getId(), guard, role.getRoleId());
					if(legioner.getLastContribute1()==0){
						legionManager.addRoleLegionContribute(legioner.getId(), 1);
					}
					if(legioner.getLastContribute2()==0){
						legionManager.addRoleLegionContribute(legioner.getId(), 2);
					}
					if(legioner.getLastContribute3()==0){
						legionManager.addRoleLegionContribute(legioner.getId(), 3);
					}
				}
				fighterManager.handlerFightResult(attackPlayer, defendPlayer, 4,result);
				this.setFightResult(attackPlayer, defendPlayer, result, role);
			}else{
				result.setWin(0);
			}
			//保存战斗到缓存
			fightManager.saveFight(result);
			super.printReturnType2Response(response, returnType);

		}catch (Exception e) {
			logger.error("fight error:",e);
		}finally{
			groupCachedManager.delete("group_"+DigestUtils.digest(legionFight.getDefend()+"guard"+dRoleId));
		}
		return null;
	}
	private void setFightResult(Player aPlayer,Player dPlayer,FightResult fightResult,Role role){
		if(aPlayer.getHP()<=0||(aPlayer.getFightCards().size()==0&&aPlayer.getPreCards().size()==0&&aPlayer.getHandsCards().size()==0)){
			fightResult.setWin(2);

			return;
		}
		if(dPlayer.getHP()<=0||(dPlayer.getFightCards().size()==0&&dPlayer.getPreCards().size()==0&&dPlayer.getHandsCards().size()==0)){
			fightResult.setWin(1);
			
		}
		
	}
	private boolean isWin(Player aPlayer,Player dPlayer){
		if(aPlayer.getHP()<=0||(aPlayer.getFightCards().size()==0&&aPlayer.getPreCards().size()==0&&aPlayer.getHandsCards().size()==0)){
			return true;
		}
		if(dPlayer.getHP()<=0||(dPlayer.getFightCards().size()==0&&dPlayer.getPreCards().size()==0&&dPlayer.getHandsCards().size()==0)){
			return true;
		}
		return false;
	}
	private boolean isWinner(Player aPlayer,Player dPlayer){
		if(aPlayer.getHP()<=0||(aPlayer.getFightCards().size()==0&&aPlayer.getPreCards().size()==0&&aPlayer.getHandsCards().size()==0)){
			return false;
		}
		if(dPlayer.getHP()<=0||(dPlayer.getFightCards().size()==0&&dPlayer.getPreCards().size()==0&&dPlayer.getHandsCards().size()==0)){
			return true;
		}
		return false;
	}
	
	
}

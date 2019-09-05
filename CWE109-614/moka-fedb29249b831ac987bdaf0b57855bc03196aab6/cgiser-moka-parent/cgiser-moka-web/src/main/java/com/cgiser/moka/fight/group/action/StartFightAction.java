package com.cgiser.moka.fight.group.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.DynamicChannelBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cgiser.keel.http.utils.HttpSpringUtils;
import com.cgiser.moka.action.AbstractAction;
import com.cgiser.moka.manager.AchievementManager;
import com.cgiser.moka.manager.FightManager;
import com.cgiser.moka.manager.FighterManager;
import com.cgiser.moka.manager.GroupFightManager;
import com.cgiser.moka.manager.MapManager;
import com.cgiser.moka.manager.MessageManager;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.manager.RoomManager;
import com.cgiser.moka.manager.SalaryManager;
import com.cgiser.moka.manager.support.BeanUtils;
import com.cgiser.moka.model.FightResult;
import com.cgiser.moka.model.GroupFight;
import com.cgiser.moka.model.MapRoom;
import com.cgiser.moka.model.MessageType;
import com.cgiser.moka.model.Player;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.model.RoomInfo;
import com.cgiser.moka.model.Salary;
import com.cgiser.moka.model.SalaryEnum;
import com.cgiser.moka.model.UserAchievement;
import com.cgiser.moka.model.VirtualRole;
import com.cgiser.moka.result.ReturnType;

public class StartFightAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		ReturnType<Integer> returnType = new ReturnType<Integer>();
		try{
			Role role = super.getCurrentRole(request);
			if(null==role){
				returnType.setMsg("您还没登录哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			RoomManager roomManager = (RoomManager)HttpSpringUtils.getBean("roomManager");
			MapManager mapManager = (MapManager)HttpSpringUtils.getBean("mapManager");
			RoleManager roleManager = (RoleManager)HttpSpringUtils.getBean("roleManager");
			FightManager fightManager = (FightManager)HttpSpringUtils.getBean("fightManager");
			FighterManager fighterManager = (FighterManager)HttpSpringUtils.getBean("fighterManager");
			MapRoom room = roomManager.getMapRoomByRoomOwner(role.getRoleId());
			if(room==null){
				returnType.setMsg("房间不存在！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			RoomInfo info = mapManager.getMapRoomInfo(room.getMapId());
			int count = 0;
			for(int i=1;i<7;i++){
				Long roleId = (Long)BeanUtils.getFieldValueByName("role"+i, room);
				if(roleId>0){
					count++;
				}	
			}
			if(count<info.getRoleNumMin()){
				returnType.setMsg("人数不够哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			//初始化队伍玩家
			List<Player> aPlayers = new ArrayList<Player>();
			Role role1;
			Player aPlayer;
			for(int i=1;i<7;i++){
				Long roleId = (Long)BeanUtils.getFieldValueByName("role"+i, room);
				if(roleId>0){
					role1 = roleManager.getRoleById(roleId);
					aPlayer = fighterManager.initPlayer(role1, "atk");
					aPlayer.setHand(false);
					aPlayers.add(aPlayer);
					roleManager.updateEnergy(aPlayer.getNickName(), info.getEnergy());
				}	
			}
			//初始化电脑玩家
			Player dPlayer;
			List<Player> dPlayers = new ArrayList<Player>();
			VirtualRole virtualRole;
			for(int i=1;i<=info.getRoleNumMax();i++){
				virtualRole = (VirtualRole)BeanUtils.getFieldValueByName("virtualRole"+i, info);
				if(virtualRole==null){
					continue;
				}
				dPlayer = fighterManager.initVirtualPlayer(virtualRole, "def");
				//设置虚拟角色的ID为编号，保证这次战斗中的被攻击者id唯一
				dPlayer.setRoleId(new Long(i));
				dPlayer.setHand(false);
				dPlayers.add(dPlayer);
			}
			//记录轮数
			int turn = 1;
			FightResult fightResult;
			String groupfightId = UUID.randomUUID().toString();
			GroupFight groupFight;
			String turnInfo;
			GroupFightManager groupFightManager = (GroupFightManager)HttpSpringUtils.getBean("groupFightManager");
			List<GroupFight> groupFights = new ArrayList<GroupFight>();
			roomManager.deleteRoom(room.getRoomId());
			SalaryManager salaryManager = (SalaryManager)HttpSpringUtils.getBean("salaryManager");
			List<Salary> salaries;
			Salary salary;
			Map<Long, Integer> playerTurnInfo =  new HashMap<Long, Integer>();
			while(turn>0){
				groupFight = new GroupFight();
				groupFight.setGroupFightId(groupfightId);
				groupFight.setTurn(turn);
				groupFight.setRoleId(room.getRoomOwner());
				groupFight.setRoomId(room.getRoomId());
				int a = aPlayers.size();
				int b = dPlayers.size();
				for(int i=a>b?b-1:a-1;i>=0;i--){
					aPlayer = aPlayers.get(i);
					if(aPlayer==null){
						continue;
					}else{
						dPlayer = dPlayers.get(i);
						fightResult = fightManager.runFight(aPlayer, dPlayer, 5);
						turnInfo = aPlayer.getRoleId()+"_"+dPlayer.getRoleId()+"_"+fightResult.getBattleId()+"_"+fightResult.getWin();
						BeanUtils.setFieldValueByName("turnInfo"+(i+1), groupFight, new Object[]{turnInfo});
						salaries = new ArrayList<Salary>();
						if(playerTurnInfo.get(aPlayer.getRoleId())==null){
							playerTurnInfo.put(aPlayer.getRoleId(), 1);
						}else{
							playerTurnInfo.put(aPlayer.getRoleId(), playerTurnInfo.get(aPlayer.getRoleId())+1);
						}
						if(fightResult.getWin()==1){
							dPlayers.remove(i);
							salary = new Salary();
							salary.setRoleId(role.getRoleId());
							salary.setTime(new Date());
							salary.setType(SalaryEnum.TeamSalary.getValue());
							salary.setAwardType(1);
							salary.setAwardValue(info.getWinCoins());
							salaries.add(salary);
							//经验奖励
							salary = new Salary();
							salary.setRoleId(role.getRoleId());
							salary.setTime(new Date());
							salary.setType(SalaryEnum.TeamSalary.getValue());
							salary.setAwardType(12);
							salary.setAwardValue(info.getWinExp());
							salaries.add(salary);
						}
						if(fightResult.getWin()==2){
							aPlayers.remove(i);
							salary = new Salary();
							salary.setRoleId(role.getRoleId());
							salary.setTime(new Date());
							salary.setType(SalaryEnum.TeamSalary.getValue());
							salary.setAwardType(1);
							salary.setAwardValue(info.getLoseCoins());
							salaries.add(salary);
							//经验奖励
							salary = new Salary();
							salary.setRoleId(role.getRoleId());
							salary.setTime(new Date());
							salary.setType(SalaryEnum.TeamSalary.getValue());
							salary.setAwardType(12);
							salary.setAwardValue(info.getLoseExp());
							salaries.add(salary);
						}
						for(Salary salary2:salaries){
							salaryManager.extendSalaryAdd(salary2.getAwardType(), salary2.getAwardValue(), aPlayer.getRoleId());
						}
						fightResult.setSalaries(salaries);
						fightManager.saveFight(fightResult);
					}
					
				}
				groupFights.add(groupFight);
				turn++;
				if(aPlayers.size()==0||dPlayers.size()==0){
					break;
				}
			}
			for(GroupFight groupFight2:groupFights){
				groupFight2.setTurns(turn-1);
				groupFightManager.save(groupFight2);
			}

			Map<Long, String> playerTurnInfo1 =  new HashMap<Long, String>();
			boolean flag = false;
			for(Map.Entry<Long, Integer> entry: playerTurnInfo.entrySet()) {
				flag = false;
				for(Player p:aPlayers){
					if(p.getRoleId().equals(entry.getKey())){
						flag = true;
					}
				}
				if(flag){
					playerTurnInfo1.put(entry.getKey(), entry.getValue()+"_"+1);
				}else{
					playerTurnInfo1.put(entry.getKey(), entry.getValue()+"_"+0);
				}
				
			}
			StringBuffer turnResult = new StringBuffer();
			for(Map.Entry<Long, String> entry: playerTurnInfo1.entrySet()) {
				turnResult.append(entry.getKey()).append("_").append(entry.getValue()).append(",");
			}
			StringBuffer salaryResult = new StringBuffer();
			int star4Day = info.getStar4Day();
			int star4Times = info.getStar4Times();
			int star5Day = info.getStar5Day();
			int star5Times = info.getStar5Times();
			int universalDay = info.getUniversialDay();
			int universalTimes = info.getUniversialTimes();
			int fightDayTimes = info.getFightDayTimes();
			mapManager.addDayFightTimes(info.getMapId());
			mapManager.addFightTimes(info.getMapId());
			if(dPlayers.size()==0){
				String[] star3s = info.getStar3().split(",");
				String[] star4s = info.getStar4().split(",");
				String[] star5s = info.getStar5().split(",");
				Random rnd = new Random();
				List<Salary> fSalaries = new ArrayList<Salary>();
				for(Map.Entry<Long, Integer> entry: playerTurnInfo.entrySet()) {
					int star = rnd.nextInt(10000);
					salary = new Salary();
					salary.setRoleId(entry.getKey());
					salary.setTime(new Date());
					salary.setType(SalaryEnum.TeamSalary.getValue());
					salary.setAwardType(9);
					if((info.getWinCard()*100*entry.getValue()>star||fightDayTimes%universalTimes==0)&&universalDay>0){
						salary.setAwardValue(2001);
						mapManager.updateUniversalDay(info.getMapId());
					}
					if(salary.getAwardValue()==0){
						for(String star5:star5s){
							if(!star5.equals("0")){
								int cardId = Integer.parseInt(star5.split("_")[0]);
								double cardRnd = new Double(star5.split("_")[1]);
								if((cardRnd*100*entry.getValue()>star||fightDayTimes%star5Times==0)&&star5Day>0){
									salary.setAwardValue(cardId);
									mapManager.updateStar5Day(info.getMapId());
									break;
								}
							}
						}
					}
					if(salary.getAwardValue()==0){
						for(String star4:star4s){
							if(!star4.equals("0")){
								int cardId = Integer.parseInt(star4.split("_")[0]);
								double cardRnd = new Double(star4.split("_")[1]);
								if((cardRnd*100*entry.getValue()>star||fightDayTimes%star4Times==0)&&star4Day>0){
									salary.setAwardValue(cardId);
									break;
								}
							}
						}
					}
					if(salary.getAwardValue()==0){
						star = rnd.nextInt(star3s.length);
						salary.setAwardValue(Integer.parseInt(star3s[star]));
					}
					fSalaries.add(salary);
				}
				
				for(Salary salary2:fSalaries){
					salaryManager.extendSalaryAdd(salary2.getAwardType(), salary2.getAwardValue(), salary2.getRoleId());
					salaryResult.append(salary2.getRoleId()).append("_").append(salary2.getAwardValue()).append(",");
				}
			}
			
			MessageManager messageManager = (MessageManager)HttpSpringUtils.getBean("messageManager");
			for(int i=1;i<7;i++){
				Long roleId = (Long)BeanUtils.getFieldValueByName("role"+i, room);
				if(roleId>0){
					role1 = roleManager.getRoleById(roleId);
					if(role1!=null){
						ChannelBuffer buffer = new DynamicChannelBuffer(200);
						buffer.writeInt(MessageType.GROUPFIGHT.getCode());
						buffer.writeInt(MessageType.STARTGROUPFIGHT.getCode());
						buffer.writeInt(room.getRoomId().intValue());
						messageManager.sendMessageToRole(role1.getRoleName(), buffer);
					}
					AchievementManager achievementManager = (AchievementManager)HttpSpringUtils.getBean("achievementManager");
					UserAchievement userAchievement = achievementManager.getUserAchievementById(roleId, 68);
					if(userAchievement==null){
						achievementManager.saveUserAchievement(68,roleId,1);
					}
				}	
			}

			returnType.setStatus(1);
			if(dPlayers.size()==0){
				returnType.setValue(1);
				roomManager.updateRoomFightResult(room.getRoomId(), salaryResult.substring(0,salaryResult.length()-1),turnResult.substring(0,turnResult.length()-1),1);
			}else{
				returnType.setValue(2);
				roomManager.updateRoomFightResult(room.getRoomId(),"",turnResult.substring(0,turnResult.length()-1), 2);
			}
			super.printReturnType2Response(response, returnType);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
	public static void main(String[] args) {
		String[] ss = "0".split(",");
		System.out.println(ss);
	}
}

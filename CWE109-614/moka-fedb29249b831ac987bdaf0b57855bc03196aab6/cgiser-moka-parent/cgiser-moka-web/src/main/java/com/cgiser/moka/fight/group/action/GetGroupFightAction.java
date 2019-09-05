package com.cgiser.moka.fight.group.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cgiser.keel.http.utils.HttpSpringUtils;
import com.cgiser.moka.action.AbstractAction;
import com.cgiser.moka.common.utils.ServletUtil;
import com.cgiser.moka.manager.FightManager;
import com.cgiser.moka.manager.GroupFightManager;
import com.cgiser.moka.manager.RoomManager;
import com.cgiser.moka.manager.support.BeanUtils;
import com.cgiser.moka.model.FightResult;
import com.cgiser.moka.model.GroupFight;
import com.cgiser.moka.model.MapRoom;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.result.GroupFightResult;
import com.cgiser.moka.result.ReturnType;

public class GetGroupFightAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		ReturnType<GroupFightResult> returnType = new ReturnType<GroupFightResult>();
		String roomId = ServletUtil.getDefaultValue(request, "roomId", null);
		String turn = ServletUtil.getDefaultValue(request, "turn", null);
		try{
			Role role = super.getCurrentRole(request);
			if(null==role){
				returnType.setMsg("您还没登录哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(!StringUtils.isNumeric(roomId)){
				returnType.setMsg("参数有误哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			RoomManager roomManager = (RoomManager)HttpSpringUtils.getBean("roomManager");
			MapRoom room = roomManager.getMapRoomByRoomIdState(new Long(roomId),0);
			if(room==null){
				returnType.setMsg("您获取的房间不存在！");
				returnType.setStatus(2);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			GroupFightManager groupFightManager = (GroupFightManager)HttpSpringUtils.getBean("groupFightManager");
			GroupFight groupFight = groupFightManager.getGroupFightByRoomIdTurn(new Long(roomId), Integer.parseInt(turn));
			if(groupFight==null){
				returnType.setMsg("战斗不存在！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			String battleId = "";
			for(int i=1;i<7;i++){
				String info = (String)BeanUtils.getFieldValueByName("turnInfo"+i, groupFight);
				if(!StringUtils.isEmpty(info)&&!info.equals("null")){
					Long roleId = new Long(info.split("_")[0]);
					if(role.getRoleId().equals(roleId)){
						battleId = info.split("_")[2];
						break;
					}
				}
			}
			if(StringUtils.isEmpty(battleId)){
				returnType.setMsg("战斗不存在！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			FightManager fightManager = (FightManager)HttpSpringUtils.getBean("fightManager");
			FightResult result = fightManager.getFight(battleId);
			
			GroupFightResult groupFightResult = new GroupFightResult();
			String turnInfo = room.getTurnInfo();
			String[] strTurns = turnInfo.split(",");
			for(String strTurn :strTurns){
				if(new Long(strTurn.split("_")[0]).equals(role.getRoleId())){
					groupFightResult.setTurns(Integer.parseInt(strTurn.split("_")[1]));
				}
			}
			groupFightResult.setGroupFight(groupFight);
			groupFightResult.setFightResult(result);
			groupFightResult.setWin(room.getWin());
			returnType.setStatus(1);
			returnType.setValue(groupFightResult);
			super.printReturnType2Response(response, returnType);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
}

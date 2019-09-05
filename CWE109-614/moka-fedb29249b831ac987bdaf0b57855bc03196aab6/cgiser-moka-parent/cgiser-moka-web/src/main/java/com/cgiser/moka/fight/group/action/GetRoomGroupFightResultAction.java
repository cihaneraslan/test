package com.cgiser.moka.fight.group.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.cgiser.keel.http.utils.HttpSpringUtils;
import com.cgiser.moka.action.AbstractAction;
import com.cgiser.moka.common.utils.ServletUtil;
import com.cgiser.moka.manager.GroupFightManager;
import com.cgiser.moka.manager.RoomManager;
import com.cgiser.moka.model.GroupFight;
import com.cgiser.moka.model.MapRoom;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.model.Salary;
import com.cgiser.moka.model.SalaryEnum;
import com.cgiser.moka.result.ReturnType;
import com.cgiser.moka.result.RoomGroupFightResult;

public class GetRoomGroupFightResultAction extends AbstractAction{
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		ReturnType<RoomGroupFightResult> returnType = new ReturnType<RoomGroupFightResult>();
		String roomId = ServletUtil.getDefaultValue(request, "roomId", null);
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
			List<GroupFight> groupFights = groupFightManager.getGroupFight(new Long(roomId));
			if(CollectionUtils.isEmpty(groupFights)){
				returnType.setMsg("您获取的战斗结果不存在！");
				returnType.setStatus(2);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			List<Salary> fSalaries = new ArrayList<Salary>();
			Salary salary;
			if(room.getWin()==1){
				String salaryInfo = room.getSalaryInfo();
				if(!StringUtils.isEmpty(salaryInfo)){
					for(String strSalary:salaryInfo.split(",")){
						salary = new Salary();
						salary.setRoleId(new Long(strSalary.split("_")[0]));
						salary.setTime(new Date());
						salary.setType(SalaryEnum.TeamSalary.getValue());
						salary.setAwardType(9);
						salary.setAwardValue(Integer.parseInt(strSalary.split("_")[1]));
						fSalaries.add(salary);
					}
				}
			}
			RoomGroupFightResult groupFightResult = new RoomGroupFightResult();
			groupFightResult.setTurnInfo(room.getTurnInfo());
			groupFightResult.setWin(room.getWin());
			groupFightResult.setTurns(groupFights.size());
			groupFightResult.setSalaries(fSalaries);
			groupFightResult.setGroupFight(groupFights.get(groupFights.size()-1));
			returnType.setStatus(1);
			returnType.setValue(groupFightResult);
			super.printReturnType2Response(response, returnType);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
}

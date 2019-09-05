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
import com.cgiser.moka.manager.MapManager;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.manager.RoomManager;
import com.cgiser.moka.manager.support.BeanUtils;
import com.cgiser.moka.model.MapRoom;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.model.RoomInfo;
import com.cgiser.moka.result.MapRoomResult;
import com.cgiser.moka.result.MapRoomRole;
import com.cgiser.moka.result.ReturnType;

public class JoinRoomAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		ReturnType<MapRoomResult> returnType = new ReturnType<MapRoomResult>();
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
			MapManager mapManager = (MapManager)HttpSpringUtils.getBean("mapManager");
			RoleManager roleManager = (RoleManager)HttpSpringUtils.getBean("roleManager");
			MapRoom room = roomManager.getMapRoomByRoomId(new Long(roomId));
			if(room==null){
				returnType.setMsg("您加入的房间不存在！");
				returnType.setStatus(2);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			RoomInfo info = mapManager.getMapRoomInfo(room.getMapId());
			if(role.getEnergy()<info.getEnergy()){
				returnType.setMsg("体力值不够哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			room = roomManager.joinInRoom(role.getRoleId(), new Long(roomId));
			if(room==null){
				returnType.setMsg("房间已满！");
				returnType.setStatus(3);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			Role role1;
			
			MapRoomRole mapRoomRole;
			MapRoomResult mapRoomResult = new MapRoomResult();
			mapRoomResult.setMapId(room.getMapId());
			mapRoomResult.setRoleNumMax(info.getRoleNumMax());
			mapRoomResult.setRoleNumMin(info.getRoleNumMin());
			mapRoomResult.setState(room.getState());
			mapRoomResult.setRoomId(room.getRoomId());
			Long roomOwnerId = room.getRoomOwner();
			if(roomOwnerId>0){
				role1 = roleManager.getRoleById(roomOwnerId);
				if(role1!=null){
					mapRoomRole = new MapRoomRole();
					mapRoomRole.setRoleId(role1.getRoleId());
					mapRoomRole.setRoleName(role1.getRoleName());
					mapRoomRole.setLevel(role1.getLevel());
					mapRoomResult.setRoomOwner(mapRoomRole);
				}
			}
			int count = 0;
			for(int i=1;i<7;i++){
				Long roleId = (Long)BeanUtils.getFieldValueByName("role"+i, room);
				if(roleId>0){
					role1 = roleManager.getRoleById(roleId);
					if(role1!=null){
						count++;
						mapRoomRole = new MapRoomRole();
						mapRoomRole.setRoleId(role1.getRoleId());
						mapRoomRole.setRoleName(role1.getRoleName());
						mapRoomRole.setLevel(role1.getLevel());
						mapRoomRole.setAvatar(role1.getAvatar());
						mapRoomRole.setSex(role1.getSex());
						BeanUtils.setFieldValueByName("role"+i, mapRoomResult, new Object[]{mapRoomRole});
					}
				}
			}
			mapRoomResult.setRoleNumNow(count);
			returnType.setStatus(1);
			returnType.setValue(mapRoomResult);
			super.printReturnType2Response(response, returnType);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
}

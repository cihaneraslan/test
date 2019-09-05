package com.cgiser.moka.fight.group.action;

import java.util.ArrayList;
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

public class GetRoomsAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		ReturnType<List<MapRoomResult>> returnType = new ReturnType<List<MapRoomResult>>();
		String mapId = ServletUtil.getDefaultValue(request, "mapId", null);
		try{
			Role role = super.getCurrentRole(request);
			if(null==role){
				returnType.setMsg("您还没登录哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(!StringUtils.isNumeric(mapId)){
				returnType.setMsg("参数有误哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			RoomManager roomManager = (RoomManager)HttpSpringUtils.getBean("roomManager");
			MapManager mapManager = (MapManager)HttpSpringUtils.getBean("mapManager");
			RoleManager roleManager = (RoleManager)HttpSpringUtils.getBean("roleManager");
			List<MapRoom> rooms = roomManager.getMapRoom(Integer.parseInt(mapId));
			if(CollectionUtils.isEmpty(rooms)){
				returnType.setMsg("当前没有房间！");
				returnType.setStatus(1);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			List<MapRoomResult> mapRoomResults = new ArrayList<MapRoomResult>();
			MapRoomResult mapRoomResult;
			RoomInfo info = mapManager.getMapRoomInfo(Integer.parseInt(mapId));
			Role role1;
			MapRoomRole mapRoomRole;
			for(MapRoom room:rooms){
				mapRoomResult = new MapRoomResult();
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
				mapRoomResults.add(mapRoomResult);
			}
			returnType.setStatus(1);
			returnType.setValue(mapRoomResults);
			super.printReturnType2Response(response, returnType);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
}

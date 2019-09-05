package com.cgiser.moka.manager.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import com.cgiser.moka.dao.ChipDao;
import com.cgiser.moka.manager.ChipManager;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.model.UserChip;

public class ChipManagerImpl implements ChipManager {
	private ChipDao chipDao;
	private RoleManager roleManager;
	public ChipDao getChipDao() {
		return chipDao;
	}

	public void setChipDao(ChipDao chipDao) {
		this.chipDao = chipDao;
	}

	@Override
	public int addChip(Long roleId, int type, int num) {
		// TODO Auto-generated method stub
		return chipDao.addUserChip(roleId, type, num);
	}

	@Override
	public int exChange(Long roleId) {
		// TODO Auto-generated method stub
		List<UserChip> list = this.getUserChip(roleId);
		int num =900000000;
		int[] type = {0,0,0,0,0,0,0,0,0};
		for(UserChip userChip :list){
			if(userChip.getNum()>0&&userChip.getNum()<num){
				num = userChip.getNum();
			}
			type[userChip.getType()-1] = 1;
		}
		for(int i=0;i<type.length;i++){
			if(type[i]==0){
				return 0;
			}
		}
		boolean flag = true;
		if(num>0&&num!=900000000){
			for(int i=0;i<type.length;i++){
				if(chipDao.exChange(roleId, i+1, num)<1){
					flag = false;
				}
			}
			if(flag){
				Role role = roleManager.getRoleById(roleId);
				roleManager.addTicket(role.getRoleName(), 1);
				return num;
			}else{
				return 0;
			}
		}else{
			return 0;
		}
		
	}

	@Override
	public List<UserChip> getUserChip(Long roleId) {
		// TODO Auto-generated method stub
		return MapListToChipList(chipDao.getUserChip(roleId));
	}
	private List<UserChip> MapListToChipList(List<Map<String, Object>> list){
		if(CollectionUtils.isEmpty(list)){
			return null;
		}
		List<UserChip> listChip = new ArrayList<UserChip>();
		for(int i=0;i<list.size();i++){
			listChip.add(MapToChip(list.get(i)));
		}
		return listChip;
	}
	private UserChip MapToChip(Map<String,Object> map){
		if(CollectionUtils.isEmpty(map)){
			return null;
		}
		UserChip userChip = new UserChip();
		userChip.setId(new Long(map.get("USERCHIPID").toString()));
		userChip.setNum(Integer.parseInt(map.get("NUM").toString()));
		userChip.setRoleId(new Long(map.get("ROLEID").toString()));
		userChip.setType(Integer.parseInt(map.get("TYPE").toString()));
		return userChip;
	}

	public RoleManager getRoleManager() {
		return roleManager;
	}

	public void setRoleManager(RoleManager roleManager) {
		this.roleManager = roleManager;
	}
}

package com.cgiser.moka.manager.impl;

import java.util.Map;

import org.springframework.util.CollectionUtils;

import com.cgiser.moka.dao.FragmentDao;
import com.cgiser.moka.manager.FragmentManager;
import com.cgiser.moka.model.Fragment;

public class FragmentManagerImpl implements FragmentManager {
	private FragmentDao fragmentDao;
	@Override
	public Long addFragment(Long roleId, int fragment1, int fragment2,
			int fragment3) {
		// TODO Auto-generated method stub
		return fragmentDao.addFragment(roleId, fragment1, fragment2, fragment3);
	}

	@Override
	public int delFragment(Long roleId, int fragment1, int fragment2,
			int fragment3) {
		if(this.getFragment(roleId)!=null){
			return fragmentDao.delFragment(roleId, fragment1, fragment2, fragment3);
		}else{
			return 0;
		}
		
	}
	@Override
	public Fragment getFragment(Long roleId) {
		// TODO Auto-generated method stub
		return MapToFragment(fragmentDao.getFragmentByRoleId(roleId));
	}
	private Fragment MapToFragment(Map<String,Object> map){
		if(CollectionUtils.isEmpty(map)){
			return null;
		}
		Fragment fragment = new Fragment();
		fragment.setFragment1(Integer.parseInt(map.get("FRAGMENT1").toString()));
		fragment.setFragment2(Integer.parseInt(map.get("FRAGMENT2").toString()));
		fragment.setFragment3(Integer.parseInt(map.get("FRAGMENT3").toString()));
		fragment.setId(new Long(map.get("ID").toString()));
		fragment.setRoleId(new Long(map.get("ROLEID").toString()));
		return fragment;
	}

	public FragmentDao getFragmentDao() {
		return fragmentDao;
	}

	public void setFragmentDao(FragmentDao fragmentDao) {
		this.fragmentDao = fragmentDao;
	}



}

package com.cgiser.moka.manager;

import com.cgiser.moka.model.Fragment;

public interface FragmentManager {
	public Long addFragment(Long roleId,int fragment1,int fragment2,int fragment3);
	public int delFragment(Long roleId,int fragment1,int fragment2,int fragment3);
	public Fragment getFragment(Long roleId);
}

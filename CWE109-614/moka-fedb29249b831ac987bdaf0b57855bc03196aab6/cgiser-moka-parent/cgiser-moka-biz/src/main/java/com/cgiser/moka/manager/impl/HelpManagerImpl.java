package com.cgiser.moka.manager.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import com.cgiser.moka.dao.HelpDao;
import com.cgiser.moka.manager.HelpManager;
import com.cgiser.moka.model.Help;

public class HelpManagerImpl implements HelpManager {
	private HelpDao helpDao;
	@Override
	public List<Help> getHelps() {
		// TODO Auto-generated method stub
		return MapListToHelpList(helpDao.getHelps());
	}
	
	private List<Help> MapListToHelpList(List<Map<String,Object>> list){
		if(CollectionUtils.isEmpty(list)){
			return null;
		}
		List<Help> helpList = new ArrayList<Help>();
		for(Map<String,Object> map:list){
			helpList.add(MapToHelp(map));
		}
		return helpList;
	}
	private Help MapToHelp(Map<String,Object> map){
		if(CollectionUtils.isEmpty(map)){
			return null;
		}
		Help help = new Help();
		help.setContent((String)map.get("CONTENT").toString());
		help.setHelpId(Integer.parseInt(map.get("HELPID").toString()));
		help.setStatus(Integer.parseInt(map.get("STATUS").toString()));
		help.setTitle((String)map.get("TITLE").toString());
		return help;
	}

	public HelpDao getHelpDao() {
		return helpDao;
	}

	public void setHelpDao(HelpDao helpDao) {
		this.helpDao = helpDao;
	}

}

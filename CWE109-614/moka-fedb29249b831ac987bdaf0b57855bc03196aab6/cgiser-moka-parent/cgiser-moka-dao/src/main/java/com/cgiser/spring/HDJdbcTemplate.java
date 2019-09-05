package com.cgiser.spring;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

@SuppressWarnings("unchecked")
public class HDJdbcTemplate extends JdbcTemplate{
	
	public Map queryForMap(String sql){
		
		try{
			List list = (List)super.query(sql, new HDColumnMapRowMapper());
			if(list == null || list.size() == 0)
				return new HashMap();
			
			return (Map)list.get(0);
		}catch(Exception e){
			return new HashMap();
		}
	}
	
	public Map queryForMap(String sql, Object[] args){		
		try{
			List list = (List)super.query(sql, args, new HDColumnMapRowMapper());
			if(list == null || list.size() == 0)
				return new HashMap();
			
			return (Map)list.get(0);
			
		}catch(Exception e){
			return new HashMap();
		}
	
	}
	
	public List queryForList(String sql){

		List list = super.query(sql, new HDColumnMapRowMapper());
		if(list == null || list.size() == 0)
			return new ArrayList();
		
		return list;
	}
	
	public List queryForList(String sql, Object[] args){
		
		List list = super.query(sql, args, new HDColumnMapRowMapper());
		if(list == null || list.size() == 0)
			return new ArrayList();
		
		return list;
	}

}

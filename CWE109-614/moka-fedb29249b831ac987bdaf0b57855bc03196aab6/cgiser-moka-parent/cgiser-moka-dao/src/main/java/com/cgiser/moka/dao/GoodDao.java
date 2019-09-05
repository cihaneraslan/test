package com.cgiser.moka.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

public class GoodDao {
	private static final Logger logger = LoggerFactory.getLogger(CardDao.class);
	private JdbcTemplate mokaJdbcTemplate;
	public List<Map<String,Object>> getGoods(){
		List list = new ArrayList();
        String sql = "select * From t_cgiser_goods";
        list = mokaJdbcTemplate.queryForList(sql);
        return list;
		
	}
	public Map<String,Object> getGoodById(int goodsId){
		Map<String,Object> map = new HashMap<String,Object>();
        String sql = "select * From t_cgiser_goods where goodsId = ?";
        String[] para = new String[1];
        para[0] = String.valueOf(goodsId);
        map = mokaJdbcTemplate.queryForMap(sql,para);
        return map;
		
	}
	public List<Map<String,Object>> getGoodColorByGoodId(int goodId){
		List list = new ArrayList();
        String sql = "select * From t_cgiser_goods_color where goodsId = ?";
        String[] para = new String[1];
        para[0] = String.valueOf(goodId);
        list = mokaJdbcTemplate.queryForList(sql,para);
        return list;
		
	}
	public Map<String,Object> getGoodColorByColorId(int colorId){
		Map<String,Object> map = new HashMap<String,Object>();
        String sql = "select * From t_cgiser_goods_color where id = ?";
        String[] para = new String[1];
        para[0] = String.valueOf(colorId);
        map = mokaJdbcTemplate.queryForMap(sql,para);
        return map;
		
	}
	public JdbcTemplate getMokaJdbcTemplate() {
		return mokaJdbcTemplate;
	}
	public void setMokaJdbcTemplate(JdbcTemplate mokaJdbcTemplate) {
		this.mokaJdbcTemplate = mokaJdbcTemplate;
	}
}

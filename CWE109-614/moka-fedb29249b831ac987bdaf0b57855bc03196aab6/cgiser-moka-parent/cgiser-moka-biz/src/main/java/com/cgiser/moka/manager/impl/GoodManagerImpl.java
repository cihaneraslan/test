package com.cgiser.moka.manager.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import com.cgiser.moka.dao.GoodDao;
import com.cgiser.moka.manager.GoodManager;
import com.cgiser.moka.model.Color;
import com.cgiser.moka.model.Good;

public class GoodManagerImpl implements GoodManager {
	private GoodDao goodDao;
	@Override
	public List<Good> getGoods() {
		List<Map<String,Object>> list = goodDao.getGoods();
		return MapListToGoodList(list);
	}
	@Override
	public Good getGoodById(int goodsId) {
		// TODO Auto-generated method stub
		return MapToGood(goodDao.getGoodById(goodsId));
	}
	private Good MapToGood(Map<String,Object> map){
		if(CollectionUtils.isEmpty(map)){
			return null;
		}
		Good good = new Good();
		good.setAdd(Integer.parseInt(map.get("ADD").toString()));
		good.setCash(Integer.parseInt(map.get("CASH").toString()));
		good.setCoins(Integer.parseInt(map.get("COINS").toString()));
		good.setColor(Integer.parseInt(map.get("COLOR").toString()));
		good.setGoodsId(Integer.parseInt(map.get("GOODSID").toString()));
		
		Map<String,Color> colors = new HashMap<String, Color>();
		List<Map<String,Object>> list = goodDao.getGoodColorByGoodId(good.getGoodsId());
		for(int i=0;i<list.size();i++){
			Color color = MapToColor(list.get(i));
			colors.put(String.valueOf(color.getId()), color);
			
		}
		good.setColors(colors);
		good.setCount(Integer.parseInt(map.get("COUNT").toString()));
		good.setContent(map.get("CONTENT").toString());
		good.setEndDate(map.get("ENDDATE").equals("")?null:((Timestamp)map.get("ENDDATE")).getTime());
		good.setEndTime(map.get("ENDTIME").equals("")?null:((Timestamp)map.get("ENDTIME")).getTime());
		good.setExtraContent(map.get("ExtraContent")==null?"":map.get("ENDTIME").toString());
		
		good.setMax(Integer.parseInt(map.get("ADD").toString()));
		good.setName(map.get("NAME").toString());
		good.setNum(Integer.parseInt(map.get("NUM").toString()));
		good.setStartDate(map.get("STARTDATE").equals("")?null:((Timestamp)map.get("STARTDATE")).getTime());
		good.setStartTime(map.get("STARTTIME").equals("")?null:((Timestamp)map.get("STARTTIME")).getTime());
		good.setTicket(Integer.parseInt(map.get("TICKET").toString()));
		return good;
		
	}
	private List<Good> MapListToGoodList(List<Map<String,Object>> list){
		if(CollectionUtils.isEmpty(list)){
			return null;
		}
		List<Good> listGood = new ArrayList<Good>();
		for(int i=0;i<list.size();i++){
			listGood.add(MapToGood(list.get(i)));
		}
		return listGood;
	}
	private Color MapToColor(Map<String,Object> map){
		if(CollectionUtils.isEmpty(map)){
			return null;
		}
		Color color = new Color();
		color.setGoodId(Integer.parseInt(map.get("GOODSID").toString()));
		color.setId(Integer.parseInt(map.get("ID").toString()));
		color.setStar1(Double.parseDouble(map.get("STAR1").toString()));
		color.setStar2(Double.parseDouble(map.get("STAR2").toString()));
		color.setStar3(Double.parseDouble(map.get("STAR3").toString()));
		color.setStar4(Double.parseDouble(map.get("STAR4").toString()));
		color.setStar5(Double.parseDouble(map.get("STAR5").toString()));
		color.setState(Integer.parseInt(map.get("STATE").toString()));
		return color;
	}

	public GoodDao getGoodDao() {
		return goodDao;
	}
	public void setGoodDao(GoodDao goodDao) {
		this.goodDao = goodDao;
	}
	@Override
	public Color getGoodColorById(int id) {
		// TODO Auto-generated method stub
		return MapToColor(goodDao.getGoodColorByColorId(id));
	}


}

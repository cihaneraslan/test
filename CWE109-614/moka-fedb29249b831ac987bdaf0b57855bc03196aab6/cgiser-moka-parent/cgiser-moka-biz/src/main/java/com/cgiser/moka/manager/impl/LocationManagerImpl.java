package com.cgiser.moka.manager.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import com.cgiser.moka.dao.LocationDao;
import com.cgiser.moka.manager.LocationManager;
import com.cgiser.moka.manager.support.HttpAccessClient;
import com.cgiser.moka.model.Mypolygon;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.model.RoleLocation;


public class LocationManagerImpl implements LocationManager {
	private LocationDao locationDao;
	private Map<String,Mypolygon> polygons = new HashMap<String, Mypolygon>();
	private HttpAccessClient httpAccessClient;
	@Override
	public int saveLocation(String city, String location) {
		return locationDao.saveLocation(city, location);
	}
	@Override
	public Long saveRoleLocation(Role role, double geoX, double geoY) {
//		String city = this.getCityByXY(String.valueOf(geoX), String.valueOf(geoY));
		String city = this.getCityByForBaiDu(String.valueOf(geoX), String.valueOf(geoY));
		net.sf.json.JSONObject obj = net.sf.json.JSONObject.fromObject(city);
		city = obj.getJSONObject("result").getJSONObject("addressComponent").getString("city");
		if(StringUtils.isEmpty(city)){
			city = "火星";
		}
		String addressComponent = obj.getJSONObject("result").getString("addressComponent");
		String address = obj.getJSONObject("result").getString("formatted_address");
		RoleLocation roleLocation = this.getRoleLocation(role.getRoleId());
		if(roleLocation==null){
			return locationDao.saveRoleLocationAddress(role.getRoleId(), role.getRoleName(), geoX, geoY, city,address,addressComponent);
		}else{
			if(!roleLocation.getCity().equals(city)||!roleLocation.getAddress().equals(address)){
				return new Long(locationDao.updateRoleLocationAddress(role.getRoleId(), geoX, geoY, city,address,addressComponent));
			}
			
		}
		return 0L;
	}
	public LocationDao getLocationDao() {
		return locationDao;
	}
	public void setLocationDao(LocationDao locationDao) {
		this.locationDao = locationDao;
	}
	@Override
	public String getCityByXY(String x, String y) {
		if(polygons.size()==0){
			List<Map<String,Object>> list = locationDao.getLocations();
			if(CollectionUtils.isEmpty(list)){
				return null;
			}
			Mypolygon polygon;
			Map<String,Object> map;
			for(int c=0;c<list.size();c++){
				map = list.get(c);
				if(!StringUtils.isEmpty((String)map.get("PNTS"))){
					polygon = new Mypolygon();
					String[] strPolygon = map.get("PNTS").toString().split("\\|");
					for(int i=0;i<strPolygon.length;i++){
						String[] strPnts = strPolygon[i].split(";");
						for(int j=0;j<strPnts.length;j++){
							polygon.addPoint(new Double(strPnts[j].split(",")[0].trim()), new Double(strPnts[j].split(",")[1].trim()));
						}
					}
					polygons.put((String)map.get("NAME"), polygon);
				}
			}

		}
		Set<String> set = polygons.keySet();
		Iterator<String>  iterator = set.iterator();
		while(iterator.hasNext()){
			String name = iterator.next();
			if(polygons.get(name).contains(new Double(x),new Double(y))){
				return name;
			}
		}
		return "火星";
	}
	@Override
	public List<RoleLocation> getRoleLocationByCity(String city) {
		
		return MapListToRoleLocationList(locationDao.getRoleLocationByCity(city));
	}
	private RoleLocation MapToRoleLocation(Map<String,Object> map){
		if(CollectionUtils.isEmpty(map)){
			return null;
		}
		RoleLocation roleLocation = new RoleLocation();
		roleLocation.setCity((String)map.get("CITY"));
		roleLocation.setGeoX(new Double((String)map.get("GEOX")));
		roleLocation.setGeoY(new Double((String)map.get("GEOY")));
		roleLocation.setId(Integer.parseInt((String)map.get("ID")));
		roleLocation.setRoleId(new Long((String)map.get("ROLEID")));
		roleLocation.setRoleName((String)map.get("ROLENAME"));
		roleLocation.setAddress((String)map.get("ADDRESS"));
		return roleLocation;
	}
	private List<RoleLocation> MapListToRoleLocationList(List<Map<String,Object>> list){
		if(CollectionUtils.isEmpty(list)){
			return null;
		}
		List<RoleLocation> listRoleLocation = new ArrayList<RoleLocation>();
		for(int i=0;i<list.size();i++){
			listRoleLocation.add(MapToRoleLocation(list.get(i)));
		}
		return listRoleLocation;
	}
	@Override
	public RoleLocation getRoleLocation(Long roleId) {
		// TODO Auto-generated method stub
		return MapToRoleLocation(locationDao.getRoleLocationByRoleId(roleId));
	}
	@Override
	public String getCityByForBaiDu(String x, String y) {
		Map<String, String> parmMap = new HashMap<String, String>();
		//location=30.2537,121.364702&output=json&key=37492c0ee6f924cb5e934fa08c6b1676
		parmMap.put("location", y+","+x);
		parmMap.put("output", "json");
		parmMap.put("key", "37492c0ee6f924cb5e934fa08c6b1676");
		String json = httpAccessClient.requestDataByUrl("http://api.map.baidu.com/geocoder", "GET", parmMap,"");
		return json;
	}
	public HttpAccessClient getHttpAccessClient() {
		return httpAccessClient;
	}
	public void setHttpAccessClient(HttpAccessClient httpAccessClient) {
		this.httpAccessClient = httpAccessClient;
	}
	public static void main(String[] args) {
		String s = "{s:2,d:{a:\"dasda\"}}";
		net.sf.json.JSONObject obj = net.sf.json.JSONObject.fromObject(s);
		obj = net.sf.json.JSONObject.fromObject(obj.get("d"));
		System.out.println(obj.get("a"));
	}

}

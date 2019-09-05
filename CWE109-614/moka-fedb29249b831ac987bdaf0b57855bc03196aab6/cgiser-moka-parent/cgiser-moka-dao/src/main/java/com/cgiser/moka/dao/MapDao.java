package com.cgiser.moka.dao;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

public class MapDao {
	private static final Logger logger = LoggerFactory.getLogger(MapDao.class);
	private JdbcTemplate mokaJdbcTemplate;
	public List<Map<String,Object>> getMaps(){
        String sql = "select * From t_cgiser_map";
        return mokaJdbcTemplate.queryForList(sql);
	}
	public Map<String,Object> getMapRoomInfo(int mapId){
        String sql = "select * From t_cgiser_map_role where mapid = ?";
        String[] para = new String[1];
        para[0] = String.valueOf(mapId);
        return mokaJdbcTemplate.queryForMap(sql,para);
	}
	public List<Map<String,Object>> getAllMapRoomInfo(){
        String sql = "select * From t_cgiser_map_role";
        return mokaJdbcTemplate.queryForList(sql);
	}
	public Map<String,Object> getVirtualRoleById(Long roleId){
        String sql = "select * From t_cgiser_virtualrole where roleid = ?";
        String[] para = new String[1];
        para[0] = String.valueOf(roleId);
        return mokaJdbcTemplate.queryForMap(sql,para);
	}
	/**
	 * 更新4星碎片次数
	 * @param mapId
	 * @return
	 */
	public int updateStar4Times(int mapId){
		String sql = "update t_cgiser_map_role set star4times = star4times -1 where mapid = ?";
        String[] para = new String[1];
        para[0] = String.valueOf(mapId);
        return mokaJdbcTemplate.update(sql, para);
	}
	/**
	 * 更新4星碎片得到的次数
	 * @param mapId
	 * @return
	 */
	public int updateStar4Day(int mapId){
		String sql = "update t_cgiser_map_role set star4day = star4day -1 where mapid = ?";
        String[] para = new String[1];
        para[0] = String.valueOf(mapId);
        return mokaJdbcTemplate.update(sql, para);
	}
	/**
	 * 更新5星碎片得到的次数
	 * @param mapId
	 * @return
	 */
	public int updateStar5Times(int mapId){
		String sql = "update t_cgiser_map_role set star5times = star5times -1 where mapid = ?";
        String[] para = new String[1];
        para[0] = String.valueOf(mapId);
        return mokaJdbcTemplate.update(sql, para);
	}
	/**
	 * 更新5星碎片的次数
	 * @param mapId
	 * @return
	 */
	public int updateStar5Day(int mapId){
		String sql = "update t_cgiser_map_role set star5day = star5day -1 where mapid = ?";
        String[] para = new String[1];
        para[0] = String.valueOf(mapId);
        return mokaJdbcTemplate.update(sql, para);
	}
	
	public int updateUniversalTimes(int mapId){
		String sql = "update t_cgiser_map_role set universaltimes = universaltimes -1 where mapid = ?";
        String[] para = new String[1];
        para[0] = String.valueOf(mapId);
        return mokaJdbcTemplate.update(sql, para);
	}
	public int updateUniversalDay(int mapId){
		String sql = "update t_cgiser_map_role set universalday = universalday -1 where mapid = ?";
        String[] para = new String[1];
        para[0] = String.valueOf(mapId);
        return mokaJdbcTemplate.update(sql, para);
	}
	public int addFightTimes(int mapId){
		String sql = "update t_cgiser_map_role set fighttimes = fighttimes +1 where mapid = ?";
        String[] para = new String[1];
        para[0] = String.valueOf(mapId);
        return mokaJdbcTemplate.update(sql, para);
	}
	public int addFightDayTimes(int mapId){
		String sql = "update t_cgiser_map_role set fightdaytimes = fightdaytimes +1 where mapid = ?";
        String[] para = new String[1];
        para[0] = String.valueOf(mapId);
        return mokaJdbcTemplate.update(sql, para);
	}
	public int resetStarDay(){
		String sql = "update t_cgiser_map_role set universalday = 10,star4day = 10,star5day = 20";
        return mokaJdbcTemplate.update(sql);
	}
	public JdbcTemplate getMokaJdbcTemplate() {
		return mokaJdbcTemplate;
	}
	public void setMokaJdbcTemplate(JdbcTemplate mokaJdbcTemplate) {
		this.mokaJdbcTemplate = mokaJdbcTemplate;
	}
}

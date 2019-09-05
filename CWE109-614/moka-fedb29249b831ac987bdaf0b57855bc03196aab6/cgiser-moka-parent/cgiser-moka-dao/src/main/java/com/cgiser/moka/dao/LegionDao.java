package com.cgiser.moka.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.CollectionUtils;

public class LegionDao {
	private static final Logger logger = LoggerFactory.getLogger(LegionDao.class);
	private JdbcTemplate mokaJdbcTemplate;
	public List<Map<String, Object>> getLegions(String legion,int index,int count){
		List list = new ArrayList();
		int start = (index-1)*count;
		int end = index*count;
        String sql = "select * FROM t_cgiser_legion where name like '%"+legion+"%' and state=1 ORDER BY LEGIONLEVEL DESC,RESOURCES limit "+start+","+end+"";
        list = mokaJdbcTemplate.queryForList(sql);
        return list;
	}
	public Map<String, Object> getLegionById(Long legionId){
        String sql = "select * FROM t_cgiser_legion where id = ?";
        String[] para = new String[1];
        para[0] = String.valueOf(legionId);
        return mokaJdbcTemplate.queryForMap(sql, para);
	}
	public Map<String, Object> getLegionByName(String name){
        String sql = "select * FROM t_cgiser_legion where name = ?";
        String[] para = new String[1];
        para[0] = String.valueOf(name);
        return mokaJdbcTemplate.queryForMap(sql, para);
	}
	public Long saveLegion(String name,long createid,String slogan){
		Long id = new Long(0);
        //String sql = "select USERID,USERIDEN,USERNAME,CREATETIME,UDID,ISACTIVE,STATE from t_user_user where STATE = 1 and username=?";
        final String insertSql ="insert into t_cgiser_legion(NAME,CREATEID,HEADERID,SLOGAN,CREATETIME)values('"+name+"',"+createid+","+createid+",'"+slogan+"','"+new Timestamp(System.currentTimeMillis())+"')";
        
        long legionid;

        KeyHolder keyHolder = new GeneratedKeyHolder();
        mokaJdbcTemplate.update(new PreparedStatementCreator() {

            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(insertSql.toString());
                return ps;
            }

        }, keyHolder);
        try {
        	legionid = keyHolder.getKey().longValue();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            legionid = 0;
        }
        if (legionid > 0) {
            id = new Long(legionid);
        } else {
            return 0l;
        }
		return id;
	}
	public Long saveLegionApply(Long roleId,Long legionId,int isActive,int state){
		return saveLegionApplyRel(roleId, legionId, 5, isActive, state);
	}
	public Long saveLegionApplyRel(Long roleId,Long legionId,int duty,int isActive,int state){
		Map map = this.getLegionApplyByRoleId(roleId,1,0);
		Long legion = new Long(0);
		final String insertSql;
		if(CollectionUtils.isEmpty(map)){
			insertSql = "insert into t_cgiser_legion_role(legionid,roleid,duty,jointime,isactive,state)values("+legionId+","+roleId+","+duty+",'"+new Timestamp(System.currentTimeMillis())+"',"+isActive+","+state+")";
			KeyHolder keyHolder = new GeneratedKeyHolder();
	        mokaJdbcTemplate.update(new PreparedStatementCreator() {

	            @Override
	            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
	                PreparedStatement ps = con.prepareStatement(insertSql.toString());
	                return ps;
	            }

	        }, keyHolder);
	        try {
	        	legion = keyHolder.getKey().longValue();
	        } catch (Exception e) {
	            legion = 0L;
	        }
	        if (legion > 0) {
	        	legion = new Long(legion);
	        } else {
	            return 0l;
	        }
			return legion;
		}else{
			insertSql = "update t_cgiser_legion_role set isactive="+isActive+",state="+state+",legionId="+legionId+",jointime="+new Timestamp(System.currentTimeMillis())+")";
			if(mokaJdbcTemplate.update(insertSql)>0){
				return legionId;
			}else{
				return 0L;
			}
		}
		
	}
	public Long saveLegionEvent(Long legionId, Long roleId, String content,
			int type) {
		Long eventId;
		final String  insertSql = "insert into t_cgiser_legion_event(legionid,roleid,eventcontent,type,createtime,state)values("
			+legionId+","+roleId+",'"+content+"',"+type+",'"+new Timestamp(System.currentTimeMillis())+"',"+1+")";
		KeyHolder keyHolder = new GeneratedKeyHolder();
        mokaJdbcTemplate.update(new PreparedStatementCreator() {

            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(insertSql.toString());
                return ps;
            }

        }, keyHolder);
        try {
        	eventId = keyHolder.getKey().longValue();
        } catch (Exception e) {
        	eventId = 0L;
        }
        if (eventId > 0) {
        	eventId = new Long(eventId);
        } else {
            return 0l;
        }
		return eventId;
		
	}
	public Long saveLegionGuard(Long legionId,int guardnum,int level,int state){
		Long legion =0L;
		final String insertSql = "insert into t_cgiser_legion_guard(legionid,guardnum,level,state)values("+legionId+","+guardnum+","+level+","+state+")";
		KeyHolder keyHolder = new GeneratedKeyHolder();
        mokaJdbcTemplate.update(new PreparedStatementCreator() {

            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(insertSql);
                return ps;
            }

        }, keyHolder);
        try {
        	legion = keyHolder.getKey().longValue();
        } catch (Exception e) {
            legion = 0L;
        }
        if (legion > 0) {
        	legion = new Long(legion);
        } else {
            return 0l;
        }
		return legion;
		
	}
	public int updateLegionLevelAndGuardNum(long id,int level,int guardNum) {
        String sql = "update t_cgiser_legion_guard set level= ? ,guardnum = ? where id = ?";
        String[] para = new String[3];
        para[0] = String.valueOf(level);
        para[1] = String.valueOf(guardNum);
        para[2] = String.valueOf(id);
        return mokaJdbcTemplate.update(sql,para);
	}
	public int updateLegionGuard(Long legionId,int id,Long roleId) {
        String sql = "update t_cgiser_legion_guard set guard"+id+" = ? where legionId = ?";
        String[] para = new String[2];
        para[0] = String.valueOf(roleId);
        para[1] = String.valueOf(legionId);
        return mokaJdbcTemplate.update(sql,para);
	}
	public Map<String, Object> getLegionGuardByLegionId(Long legionId){
		String sql = "select * FROM t_cgiser_legion_guard where legionid =? and  state=1";
		String[] para = new String[1];
        para[0] = String.valueOf(legionId);
        return mokaJdbcTemplate.queryForMap(sql,para);
	}
	public int getLegionCount(){
        String sql = "SELECT count(*) FROM t_cgiser_legion where state=1";
        return mokaJdbcTemplate.queryForInt(sql);
	}
	public List<Map<String, Object>> getLegioner(long legionId, int index, int count) {
		
		List list = new ArrayList();
		int start = (index-1)*count;
		int end = index*count;
        String sql = "select * FROM t_cgiser_legion_role where legionid =? and state=1 and isactive=1 limit ?,? ";
        Long[] para = new Long[3];
        para[0] = legionId;
        para[1] = new Long(start);
	    para[2] = new Long(end);
        list = mokaJdbcTemplate.queryForList(sql, para);
        return list;
	}
	public List<Map<String, Object>> getMaxContributeLegioner(long legionId) {
		
		List list = new ArrayList();
        String sql = "SELECT *  FROM t_cgiser_legion_role WHERE legionid =? and state = 1 and isactive=1 and  CONTRIBUTE=(SELECT MAX(CONTRIBUTE)  FROM `t_cgiser_legion_role`)";
        Long[] para = new Long[1];
        para[0] = legionId;
        list = mokaJdbcTemplate.queryForList(sql, para);
        return list;
	}
	public Map<String, Object> getDeputyHeader(long legionId){
		String sql = "select * FROM t_cgiser_legion_role where legionid =? and duty =2 and  state=1 and isactive=1";
		String[] para = new String[1];
        para[0] = String.valueOf(legionId);
        return mokaJdbcTemplate.queryForMap(sql,para);
	}
	public Map<String, Object> getHeader(long legionId){
		String sql = "select * FROM t_cgiser_legion_role where legionid =? and duty =1 and  state=1 and isactive=1";
		String[] para = new String[1];
        para[0] = String.valueOf(legionId);
        return mokaJdbcTemplate.queryForMap(sql,para);
	}
	public Map<String, Object> getLegionerByRoleId(long roleId){
		String sql = "select * FROM t_cgiser_legion_role where roleid =? and  state=1 and isactive=1";
		String[] para = new String[1];
        para[0] = String.valueOf(roleId);
        return mokaJdbcTemplate.queryForMap(sql,para);
	}
	public Map<String, Object> getLegionerByLegionerId(long legionerId){
		String sql = "select * FROM t_cgiser_legion_role where id =? and  state=1 and isactive=1";
		String[] para = new String[1];
        para[0] = String.valueOf(legionerId);
        return mokaJdbcTemplate.queryForMap(sql,para);
	}
	public Map<String, Object> getLegionApplyByRoleId(long roleId,int isActive,int state){
		String sql = "select * FROM t_cgiser_legion_role where roleid =? and state=? and isactive=?";
		String[] para = new String[3];
        para[0] = String.valueOf(roleId);
        para[0] = String.valueOf(state);
        para[0] = String.valueOf(isActive);
        return mokaJdbcTemplate.queryForMap(sql,para);
	}
	public List<Map<String, Object>> getLegionApply(long legionId, int index, int count) {
		List list = new ArrayList();
		int start = (index-1)*count;
		int end = index*count;
        String sql = "select * FROM t_cgiser_legion_role where legionid =? and state=1 and isactive=0 limit ?,?";
        Long[] para = new Long[3];
        para[0] = legionId;
        para[1] = new Long(start);
	    para[2] = new Long(end);
        list = mokaJdbcTemplate.queryForList(sql, para);
        return list;
	}
	public Map<String, Object> getLegionApplyByRoleId(long legionId, Long roleId) {
        String sql = "select * FROM t_cgiser_legion_role where legionid = ? and roleid = ? and state=1 and isactive=0";
        Long[] para = new Long[2];
        para[0] = legionId;
        para[1] = roleId;
        return mokaJdbcTemplate.queryForMap(sql,para);
	}
	public Map<String, Object> getLegionApplyById(long id) {
        String sql = "select * FROM t_cgiser_legion_role where id = ? and state=1 and isactive=0";
        Long[] para = new Long[1];
        para[0] = id;
        return mokaJdbcTemplate.queryForMap(sql,para);
	}
	public int agreeLegionApply(long id) {
		
        String sql = "update t_cgiser_legion_role set isactive=1 where id = ?";
        Long[] para = new Long[1];
        para[0] = id;
        return mokaJdbcTemplate.update(sql,para);
	}
	public int delLegionApply(long id) {
		
        String sql = "update t_cgiser_legion_role set state=0 where id = ?";
        Long[] para = new Long[1];
        para[0] = id;
        return mokaJdbcTemplate.update(sql,para);
	}
	public int delLegionApplyByRoleId(long roleId) {
        String sql = "update t_cgiser_legion_role set state=0 where roleId = ? and isactive = 0";
        Long[] para = new Long[1];
        para[0] = roleId;
        return mokaJdbcTemplate.update(sql,para);
	}
	public int updateDuty(long id,int duty) {
		
        String sql = "update t_cgiser_legion_role set duty=? where id = ?";
        String[] para = new String[2];
        para[0] = String.valueOf(duty);
        para[1] = String.valueOf(id);
        return mokaJdbcTemplate.update(sql,para);
	}
	public int updateHeader(long id,long roleid) {
		
        String sql = "update t_cgiser_legion set headerid=? where id = ?";
        String[] para = new String[2];
        para[0] = String.valueOf(roleid);
        para[1] = String.valueOf(id);
        return mokaJdbcTemplate.update(sql,para);
	}
	public int outLegion(Long id) {
        String sql = "update t_cgiser_legion_role set state=0,leavetime='"+new Timestamp(System.currentTimeMillis())+"' where id = ?";
        Long[] para = new Long[1];
        para[0] = id;
        return mokaJdbcTemplate.update(sql,para);
	}
	public Timestamp getOutLegionTime(Long roleId) {
        String sql = "select max(leavetime) FROM t_cgiser_legion_role where roleid =? and state = 0 and isactive = 1";
        String[] para = new String[1];
        para[0] = String.valueOf(roleId);
        return mokaJdbcTemplate.queryForObject(sql, para, Timestamp.class);
	}
	public int breakupLegion(Long id) {
        String sql = "update t_cgiser_legion set state=0 where id = ?";
        Long[] para = new Long[1];
        para[0] = id;
        return mokaJdbcTemplate.update(sql,para);
	}
	public int updateLegionSlogan(long legionId, String slogan){
        String sql = "update t_cgiser_legion set slogan=? where id = ?";
        String[] para = new String[2];
        para[0] = slogan;
        para[1] = String.valueOf(legionId);
        return mokaJdbcTemplate.update(sql,para);
	}
	public int updateLegionNotice(long legionId, String notice){
        String sql = "update t_cgiser_legion set notice=? where id = ?";
        String[] para = new String[2];
        para[0] = notice;
        para[1] = String.valueOf(legionId);
        return mokaJdbcTemplate.update(sql,para);
	}
	public int andLegionContribute(long legionId,int contribute,int contributeId) {
		String name = "contribute".concat(String.valueOf(contributeId));
        String sql = "update t_cgiser_legion set "+name+"="+name+"+"+contribute+" where id = ?";
        Long[] para = new Long[1];
        para[0] = legionId;
        return mokaJdbcTemplate.update(sql,para);
	}	
	public int addRoleContributeHonor(Long legionerId, int contribute, int honor) {
        String sql = "update t_cgiser_legion_role set contribute=contribute+ ?,honor=honor+ ? where id = ?";
        String[] para = new String[3];
        para[0] = String.valueOf(contribute);
        para[1] = String.valueOf(honor);
        para[2] = String.valueOf(legionerId);
        return mokaJdbcTemplate.update(sql,para);
	}	
	public int updateRoleHonor(Long legionerId, int honor) {
        String sql = "update t_cgiser_legion_role set honor=honor - ? where id = ?";
        String[] para = new String[2];
        para[0] = String.valueOf(honor);
        para[1] = String.valueOf(legionerId);
        return mokaJdbcTemplate.update(sql,para);
	}	
	public int upLegionEmblemLevel(Long legionId,int level) {
        String sql = "update t_cgiser_legion set emblemlevel=emblemlevel+ ? where id = ?";
        String[] para = new String[2];
        para[0] = String.valueOf(level);
        para[1] = String.valueOf(legionId);
        return mokaJdbcTemplate.update(sql,para);
	}
	public int updateLegionEmblem(int emblemId, Long legionId) {
        String sql = "update t_cgiser_legion set emblem=? where id = ?";
        String[] para = new String[2];
        para[0] = String.valueOf(emblemId);
        para[1] = String.valueOf(legionId);
        return mokaJdbcTemplate.update(sql,para);
	}
	public List<Map<String, Object>> getLegionersByDuty(Long legionId,int duty) {
		
		List list = new ArrayList();
        String sql = "SELECT *  FROM t_cgiser_legion_role WHERE legionid =? and state = 1 and isactive=1 and duty= ?";
        String[] para = new String[2];
        para[0] = String.valueOf(legionId);
        para[1] = String.valueOf(duty);
        list = mokaJdbcTemplate.queryForList(sql, para);
        return list;
	}
	public int updateResources(Long legionId,int resources,int type){
		if(type==0){
			int cashnow = Integer.parseInt(this.getLegionById(legionId).get("RESOURCES").toString());
	    	if(cashnow<resources){
	    		return 0;
	    	}
		}
    	String insertSql = "update t_cgiser_legion set resources=resources"+(type==0?"-":"+")+resources+" where id= "+legionId;
        
		return mokaJdbcTemplate.update(insertSql);
	}
	public int resetLegionRobTimes(){
		
    	String insertSql = "update t_cgiser_legion set robtimes=0";
        
		return mokaJdbcTemplate.update(insertSql);
	}
	public int addLegionRobTimes(Long legionId){
		
    	String insertSql = "update t_cgiser_legion set robtimes=robtimes+1 where id= "+legionId;
        
		return mokaJdbcTemplate.update(insertSql);
	}
	public int addLegionRobTimesAll(Long legionId){
		
    	String insertSql = "update t_cgiser_legion set robtimesall=robtimesall+1 where id= "+legionId;
        
		return mokaJdbcTemplate.update(insertSql);
	}
	public int updateLegionAttack(Long legionId){
		
    	String insertSql = "update t_cgiser_legion set lastattack=lastattack-1 where id= "+legionId;
        
		return mokaJdbcTemplate.update(insertSql);
	}
	public int addLegionAttack(Long legionId){
		
    	String insertSql = "update t_cgiser_legion set lastattack=lastattack+1 where id= "+legionId;
        
		return mokaJdbcTemplate.update(insertSql);
	}
	public int resetLegionAttack(){
		
    	String insertSql = "update t_cgiser_legion set lastattack=3 where lastattack<3";
        
		return mokaJdbcTemplate.update(insertSql);
	}
	public int resetLegionBuyAttack(){
		
    	String insertSql = "update t_cgiser_legion set buyattack=0";
        
		return mokaJdbcTemplate.update(insertSql);
	}
	public int addLegionBuyAttack(Long legionId){
		
    	String insertSql = "update t_cgiser_legion set buyattack=buyattack+1 where id= "+legionId;
        
		return mokaJdbcTemplate.update(insertSql);
	}
	public int addLegionBuyAttackAll(Long legionId){
		
    	String insertSql = "update t_cgiser_legion set buyattackall=buyattackall+1 where id= "+legionId;
        
		return mokaJdbcTemplate.update(insertSql);
	}
	public int updateLegionerAttack(Long legionerId){
		
    	String insertSql = "update t_cgiser_legion_role set lastattack=lastattack-1 where id= "+legionerId;
        
		return mokaJdbcTemplate.update(insertSql);
	}
	public int addLegionerAttack(Long legionerId){
		
    	String insertSql = "update t_cgiser_legion_role set lastattack=lastattack+1 where id= "+legionerId;
        
		return mokaJdbcTemplate.update(insertSql);
	}
	public int upLegionLevel(Long legionId){
		
    	String insertSql = "update t_cgiser_legion set legionlevel=legionlevel+1 where id= "+legionId;
        
		return mokaJdbcTemplate.update(insertSql);
	}
	public int updateLegionLegionerNum(Long legionId,int legionNum){
		
    	String insertSql = "update t_cgiser_legion set legionnum=legionnum+"+legionNum+" where id= "+legionId;
        
		return mokaJdbcTemplate.update(insertSql);
	}
	public int resetLegionerAttack(){
		
    	String insertSql = "update t_cgiser_legion_role set lastattack=10 where lastattack<10 and isactive=1 and state =1";
        
		return mokaJdbcTemplate.update(insertSql);
	}
	public int resetLegionerLastContribute(){
		
    	String insertSql = "update t_cgiser_legion_role set lastcontribute1=2,lastcontribute2=1,lastcontribute3=1 where isactive=1 and state =1";
        
		return mokaJdbcTemplate.update(insertSql);
	}
	public int updateLegionerLastContribute(Long legionerId,int id){
		
    	String insertSql = "update t_cgiser_legion_role set lastcontribute"+id+"=lastcontribute"+id+" -1 where id = "+legionerId+" and isactive=1 and state =1";
        
		return mokaJdbcTemplate.update(insertSql);
	}
	public int addLegionerLastContribute(Long legionerId,int id){
		
    	String insertSql = "update t_cgiser_legion_role set lastcontribute"+id+"=lastcontribute"+id+" +1 where id = "+legionerId+" and isactive=1 and state =1";
        
		return mokaJdbcTemplate.update(insertSql);
	}
	public List<Map<String, Object>> getLegionEventByType(long legionId, int type) {
        String sql = "select * FROM t_cgiser_legion_event where legionid =? and type = ? and state=1 and TIMESTAMPDIFF(DAY,NOW(),CREATETIME)>-3";
        Long[] para = new Long[2];
        para[0] = legionId;
        para[1] = new Long(type);
        return mokaJdbcTemplate.queryForList(sql, para);
	}
	public List<Map<String, Object>> getLegionEvent(long legionId) {
        String sql = "select * FROM t_cgiser_legion_event where legionid =? and state=1 and TIMESTAMPDIFF(DAY,NOW(),CREATETIME)>-3";
        Long[] para = new Long[1];
        para[0] = legionId;
        return mokaJdbcTemplate.queryForList(sql, para);
	}
	public Map<String, Object> getLegionTechByTechId(String techId) {
        String sql = "select * FROM t_cgiser_legion_tech where techid =?";
        String[] para = new String[1];
        para[0] = techId;
        return mokaJdbcTemplate.queryForMap(sql, para);
	}
	public List<Map<String, Object>> getLegionTechs() {
        String sql = "select * FROM t_cgiser_legion_tech order by id";
        return mokaJdbcTemplate.queryForList(sql);
	}
	public List<Map<String, Object>> getLegionGoods() {
        String sql = "select * FROM t_cgiser_legion_good where state=1";
        return mokaJdbcTemplate.queryForList(sql);
	}
	public Map<String, Object> getLegionGoodById(Long id) {
        String sql = "select * FROM t_cgiser_legion_good where id=? and state=1";
        String[] para = new String[1];
        para[0] = String.valueOf(id);
        return mokaJdbcTemplate.queryForMap(sql, para);
	}
	public Map<String, Object> getLegionLevelByLevel(int level) {
        String sql = "select * FROM t_cgiser_legion_level where level =?";
        String[] para = new String[1];
        para[0] = String.valueOf(level);
        return mokaJdbcTemplate.queryForMap(sql, para);
	}
	public JdbcTemplate getMokaJdbcTemplate() {
		return mokaJdbcTemplate;
	}
	public void setMokaJdbcTemplate(JdbcTemplate mokaJdbcTemplate) {
		this.mokaJdbcTemplate = mokaJdbcTemplate;
	}
}

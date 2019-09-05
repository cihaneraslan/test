package com.cgiser.moka.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.cgiser.moka.data.RoleDo;


public class RoleDao {
	private static final Logger logger = LoggerFactory.getLogger(RoleDao.class);
	private JdbcTemplate mokaJdbcTemplate;
	private DataSourceTransactionManager mokaDataSourceTransactionManager;

	public List<Map<String, Object>> getRolesByUserIden(String userIden,Long serverId){
        if (StringUtils.isEmpty(userIden))
            return null;
        List list = new ArrayList();
        String sql = "select * from t_cgiser_mokarole where useriden=? and serverid = ?";
        String[] para = new String[2];
        para[0] = StringUtils.trim(userIden);
        para[1] = StringUtils.trim(serverId.toString());
        list = mokaJdbcTemplate.queryForList(sql, para);
        return list;
		
	}
	public List<Map<String, Object>> getAllRoles(int state){
        String sql = "select * from t_cgiser_mokarole where status=?";
        String[] para = new String[1];
        para[0] = String.valueOf(state);
        return mokaJdbcTemplate.queryForList(sql, para);
		
	}
	public Map<String,Object> getRoleByRoleName(String roleName){
		if (StringUtils.isEmpty(roleName))
            return null;
        Map map = new HashMap();
        String sql = "select * from t_cgiser_mokarole where ROLENAME=?";
        String[] para = new String[1];
        para[0] = StringUtils.trim(roleName);
        return mokaJdbcTemplate.queryForMap(sql, para);
	}
	public Map<String,Object> getRoleByInvitCode(String invitCode){
		if (StringUtils.isEmpty(invitCode))
            return null;
        String sql = "select * from t_cgiser_mokarole where Invitecode=?";
        String[] para = new String[1];
        para[0] = StringUtils.trim(invitCode);
        return mokaJdbcTemplate.queryForMap(sql, para);
	}
	public Map<String,Object> getRoleById(Long roleId){
		if (roleId==null)
            return null;
        Map map = new HashMap();
        String sql = "select * from t_cgiser_mokarole where roleid = ?";
        String[] para = new String[1];
        para[0] = String.valueOf(roleId);
        map = mokaJdbcTemplate.queryForMap(sql, para);
        return map;
	}
	public Long saveRole(RoleDo role) {
		Calendar tommorowDate = Calendar.getInstance();
		tommorowDate.add(Calendar.MONTH, -1);
		Long id = new Long(0);
        //String sql = "select USERID,USERIDEN,USERNAME,CREATETIME,UDID,ISACTIVE,STATE from t_user_user where STATE = 1 and username=?";
        final String insertSql = "insert into t_cgiser_mokarole(ROLENAME,USERIDEN,SERVERID,AVATAR,WIN,LOSE,LEVEL,EXP,PREVEXP,NEXTEXP,COINS,CASH,TICKET,FRESHSTEP,ENERGY,ENERGYMAX," +
		"ENERGYLASTTIME,ENERGYBUYTIME,ENERGYBUYCOUNT,LEADERSHIP,FRIENDAPPLYNUM,FRIENDNUMMAX,DEFAULTGROUPID,SEX,FRAGMENT_3,FRAGMENT_4,FRAGMENT_5,RANK,THIEVESTIMES," +
		"HP,LOGINCONTINUETIMES,INVITECODE,LASTLOGIONTIME,INVITEROLECODE,MAPLASTINCOME,LASTRECEIVEENERGY,SOULREFRESHTIME,FREEFIGHTTIME,SENDENERGYTIMES)values('"
        	+role.getRoleName()+"','"+role.getUserIden()+"',"+role.getServerId()+","+role.getAvatar()+","+role.getWin()+","+role.getLose()+","+role.getLevel()+","+role.getExp()+","+role.getPrevExp()+
        	","+role.getNextExp()+","+role.getCoins()+","+role.getCash()+","+role.getTicket()+","+role.getFreshStep()+","+role.getEnergy()+","+role.getEnergyMax()+
        	",'"+new Timestamp(role.getEnergyLastTime())+"','"+new Timestamp(role.getEnergyBuyTime())+"',"+role.getEnergyBuyCount()+","+role.getLeaderShip()+","+role.getFriendApplyNum()+","+role.getFriendNumMax()+","+role.getDefaultGroupId()+
        	","+role.getSex()+","+role.getFragment_3()+","+role.getFragment_4()+","+role.getFragment_5()+","+role.getRank()+","+role.getThievesTimes()+","+role.getHP()+","+role.getLoginContinueTimes()+
        	",'"+role.getInviteCode()+"','"+new Timestamp(tommorowDate.getTimeInMillis())+"','"+role.getInviteRoleCode()+"','"+new Timestamp(tommorowDate.getTimeInMillis())+"','"+new Timestamp(tommorowDate.getTimeInMillis())
        	+"','"+new Timestamp(tommorowDate.getTimeInMillis())+"','"+new Timestamp(tommorowDate.getTimeInMillis())+"',0)";
        
        long roleid;

        KeyHolder keyHolder = new GeneratedKeyHolder();
        mokaJdbcTemplate.update(new PreparedStatementCreator() {

            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(insertSql.toString());
                return ps;
            }

        }, keyHolder);
        try {
        	roleid = keyHolder.getKey().longValue();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            roleid = 0;
        }
        if (roleid > 0) {
            id = new Long(roleid);
        } else {
            return 0l;
        }
		return id;
		
	}
	/**
	 * 更新角色体力值
	 * @param energy
	 * @param type 0:减少;1:增加
	 * @return
	 */
	public boolean updateRoleEnergyByRoleName(String roleName,int energy,int type){
		if(type==0){
			int energynow = Integer.parseInt(this.getRoleByRoleName(roleName).get("ENERGY").toString());
	    	if(energynow<energy){
	    		return false;
	    	}
		}
        //String sql = "select USERID,USERIDEN,USERNAME,CREATETIME,UDID,ISACTIVE,STATE from t_user_user where STATE = 1 and username=?";
		
    	String insertSql = "update t_cgiser_mokarole set energy=energy"+(type==0?"-":"+")+energy+" where roleName= '"+roleName+"'";
		if(mokaJdbcTemplate.update(insertSql)>0){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 更新角色体力值
	 * @param energy
	 * @param type 0:减少;1:增加
	 * @return
	 */
	public boolean updateRoleEnergyByRoleId(Long roleId,int energy,int type){
		if(type==0){
			int energynow = Integer.parseInt(this.getRoleById(roleId).get("ENERGY").toString());
	    	if(energynow<energy){
	    		return false;
	    	}
		}
        //String sql = "select USERID,USERIDEN,USERNAME,CREATETIME,UDID,ISACTIVE,STATE from t_user_user where STATE = 1 and username=?";
		
    	String insertSql = "update t_cgiser_mokarole set energy=energy"+(type==0?"-":"+")+energy+" where roleId= ?";
    	String[] para = new String[1];
    	para[0] = String.valueOf(roleId);
		if(mokaJdbcTemplate.update(insertSql,para)>0){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 用于用户升级后更新角色对应值
	 * @param level
	 * @param cost
	 * @param friendNum
	 * @param hp
	 * @return
	 */
	public boolean updateRole(String roleName,int prevExp,int nextExp,int level,int cost,int friendNum,int hp){
        //String sql = "select USERID,USERIDEN,USERNAME,CREATETIME,UDID,ISACTIVE,STATE from t_user_user where STATE = 1 and username=?";
		
    	String insertSql = "update t_cgiser_mokarole set level="+level+",LEADERSHIP="+cost+",FRIENDNUMMAX="+friendNum+",HP="+hp+",PREVEXP="+prevExp+",NEXTEXP="+nextExp+" where roleName= '"+roleName+"'";
        
		if(mokaJdbcTemplate.update(insertSql)>0){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 更新角色经验值
	 * @param exp
	 * @param type 0:减少;1:增加
	 * @return
	 */
	public boolean updateRoleExp(String roleName,int exp,int type){
		if(type==0){
			int expnow = Integer.parseInt(this.getRoleByRoleName(roleName).get("EXP").toString());
	    	if(expnow<exp){
	    		return false;
	    	}
		}
        //String sql = "select USERID,USERIDEN,USERNAME,CREATETIME,UDID,ISACTIVE,STATE from t_user_user where STATE = 1 and username=?";
		
    	String insertSql = "update t_cgiser_mokarole set exp=exp"+(type==0?"-":"+")+exp+" where roleName= '"+roleName+"'";
        
		if(mokaJdbcTemplate.update(insertSql)>0){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 更新角色元宝数
	 * @param cash
	 * @param type 0:减少;1:增加
	 * @return
	 */
	public boolean updateRoleCash(String roleName,int cash,int type){
        //String sql = "select USERID,USERIDEN,USERNAME,CREATETIME,UDID,ISACTIVE,STATE from t_user_user where STATE = 1 and username=?";
		if(type==0){
			int cashnow = Integer.parseInt(this.getRoleByRoleName(roleName).get("CASH").toString());
	    	if(cashnow<cash){
	    		return false;
	    	}
		}
    	String insertSql = "update t_cgiser_mokarole set cash=cash"+(type==0?"-":"+")+cash+" where roleName= '"+roleName+"'";
        
		if(mokaJdbcTemplate.update(insertSql)>0){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 更新角色铜钱数
	 * @param cash
	 * @param type 0:减少;1:增加
	 * @return
	 */
	public boolean updateRoleCoin(String roleName,int coin,int type){
		if(type==0){
	        //String sql = "select USERID,USERIDEN,USERNAME,CREATETIME,UDID,ISACTIVE,STATE from t_user_user where STATE = 1 and username=?";
			int coinnow = Integer.parseInt(this.getRoleByRoleName(roleName).get("COINS").toString());
	    	if(coinnow<coin){
	    		return false;
	    	}
		}

    	String insertSql = "update t_cgiser_mokarole set coins=coins"+(type==0?"-":"+")+coin+" where roleName= '"+roleName+"'";
        
		if(mokaJdbcTemplate.update(insertSql)>0){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 更新角色魔幻券数
	 * @param ticket
	 * @param type 0:减少;1:增加
	 * @return
	 */
	public boolean updateRoleTicket(String roleName,int ticket,int type){
		if(type==0){
	        //String sql = "select USERID,USERIDEN,USERNAME,CREATETIME,UDID,ISACTIVE,STATE from t_user_user where STATE = 1 and username=?";
			int ticketnow = Integer.parseInt(this.getRoleByRoleName(roleName).get("TICKET").toString());
	    	if(ticketnow<ticket){
	    		return false;
	    	}
		}

    	String insertSql = "update t_cgiser_mokarole set ticket=ticket"+(type==0?"-":"+")+ticket+" where roleName= '"+roleName+"'";
        
		if(mokaJdbcTemplate.update(insertSql)>0){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 更新角色荣誉点数
	 * @param honor
	 * @param type 0:减少;1:增加
	 * @return
	 */
	public boolean updateRoleHonor(Long roleId,int honor,int type){
		if(type==0){
	        //String sql = "select USERID,USERIDEN,USERNAME,CREATETIME,UDID,ISACTIVE,STATE from t_user_user where STATE = 1 and username=?";
			int honornow = Integer.parseInt(this.getRoleById(roleId).get("HONOR").toString());
	    	if(honornow<honor){
	    		return false;
	    	}
		}

    	String insertSql = "update t_cgiser_mokarole set honor=honor"+(type==0?"-":"+")+honor+" where roleId= "+roleId;
        
		if(mokaJdbcTemplate.update(insertSql)>0){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 得到该表的序列
	 * @return
	 */
    public long getSequence(String tableName){
    	String sql = "SELECT auto_increment FROM information_schema.`TABLES` WHERE TABLE_SCHEMA='MOKA' AND TABLE_NAME='"+tableName+"'";
        Long seq = (Long) mokaJdbcTemplate.queryForLong(sql);
        return seq;
    	
    }
	public int updateRoleDefaultGroup(Long roleId,Long groupId){
        //String sql = "select USERID,USERIDEN,USERNAME,CREATETIME,UDID,ISACTIVE,STATE from t_user_user where STATE = 1 and username=?";
    	String insertSql = "update t_cgiser_mokarole set DEFAULTGROUPID = ? where roleId=?";
        String[] para = new String[2];
        para[0] = String.valueOf(groupId);
        para[1] = String.valueOf(roleId);
		return mokaJdbcTemplate.update(insertSql,para);
	}
	public int updateRoleNewMail(Long roleId,int newEmail){
        //String sql = "select USERID,USERIDEN,USERNAME,CREATETIME,UDID,ISACTIVE,STATE from t_user_user where STATE = 1 and username=?";
    	String insertSql = "update t_cgiser_mokarole set NEWEMAIL = ? where roleId=?";
        String[] para = new String[2];
        para[0] = String.valueOf(newEmail);
        para[1] = String.valueOf(roleId);
		return mokaJdbcTemplate.update(insertSql,para);
	}
	public int updateRoleSalaryCount(Long roleId,int salaryCount){
        //String sql = "select USERID,USERIDEN,USERNAME,CREATETIME,UDID,ISACTIVE,STATE from t_user_user where STATE = 1 and username=?";
    	String insertSql = "update t_cgiser_mokarole set SALARYCOUNT = ? where roleId=?";
        String[] para = new String[2];
        para[0] = String.valueOf(salaryCount);
        para[1] = String.valueOf(roleId);
		return mokaJdbcTemplate.update(insertSql,para);
	}
	public int updateRoleFriendApplyNum(Long roleId,int friendApply){
        //String sql = "select USERID,USERIDEN,USERNAME,CREATETIME,UDID,ISACTIVE,STATE from t_user_user where STATE = 1 and username=?";
    	String insertSql = "update t_cgiser_mokarole set FRIENDAPPLYNUM = ? where roleId=?";
        String[] para = new String[2];
        para[0] = String.valueOf(friendApply);
        para[1] = String.valueOf(roleId);
		return mokaJdbcTemplate.update(insertSql,para);
	}
	public List<Map<String,Object>> getRolesByRoleName(String roleName) {
        String sql = "select * from t_cgiser_mokarole where rolename like '%"+roleName+"%' ORDER BY RAND() LIMIT 10";
        if(StringUtils.isEmpty(roleName)){
        	sql = "select * from t_cgiser_mokarole where level>5 ORDER BY RAND() LIMIT 10";
        }
        return mokaJdbcTemplate.queryForList(sql);
	}
	public List<Map> getRankCompetitors(int rank){
		StringBuffer sqlBuf = new StringBuffer();
		if(rank<=5){
			rank = 6;
		}
		sqlBuf.append("select * from t_cgiser_mokarole where rank<"+rank);
		if(rank<500){
			sqlBuf.append(" ORDER BY RANK DESC");
			sqlBuf.append(" limit 0 ,5");
		}else if(rank>=500&&rank<1000){
			sqlBuf.append(" and rank%2="+rank%2);
			sqlBuf.append(" ORDER BY RANK DESC");
			sqlBuf.append(" limit 0 ,5");
		}else if(rank>=1000&&rank<2500){
			sqlBuf.append(" and rank%20="+rank%20);
			sqlBuf.append(" ORDER BY RANK DESC");
			sqlBuf.append(" limit 0 ,5");
		}else if(rank>=2500){
			sqlBuf.append(" and rank%100="+rank%100);
			sqlBuf.append(" ORDER BY RANK DESC");
			sqlBuf.append(" limit 0 ,5");
		}
		
		List list = mokaJdbcTemplate.queryForList(sqlBuf.toString());
		return list;
	}
	public Map getFirstRank(){
		String sql = "select * from t_cgiser_mokarole where rank=1";
		return mokaJdbcTemplate.queryForMap(sql);
	}
	public List<Map> getRankRoles(int rank){
		StringBuffer sqlBuf = new StringBuffer();
		sqlBuf.append("select AVATAR,LEVEL,LOSE,ROLENAME,RANK,SEX,ROLEID,WIN from t_cgiser_mokarole where rank>="+rank);
		sqlBuf.append(" ORDER BY RANK ASC");
		sqlBuf.append(" limit 0 ,150");
		
		List list = mokaJdbcTemplate.queryForList(sqlBuf.toString());
		return list;
	}
	public Map getRoleByRank(int rank){
		StringBuffer sqlBuf = new StringBuffer();
		sqlBuf.append("select AVATAR,LEVEL,LOSE,ROLENAME,RANK,SEX,ROLEID,WIN from t_cgiser_mokarole where rank="+rank);
		return mokaJdbcTemplate.queryForMap(sqlBuf.toString());
	}
	public Map getRankByRoleId(Long roleId){
		StringBuffer sqlBuf = new StringBuffer();
		sqlBuf.append("select RANK from t_cgiser_mokarole where roleid="+roleId);
		return mokaJdbcTemplate.queryForMap(sqlBuf.toString());
	}
	public int getMaxRank(){
		String sql = "select max(rank) from t_cgiser_mokarole";
		return mokaJdbcTemplate.queryForInt(sql);
	}
	public int[] updateRoleRank(Long aRoleId,int aRank,Long dRoleId,int dRank){
		final String sql1 = "update t_cgiser_mokarole set RANK = "+aRank+" where roleId="+aRoleId;
		final String sql2 = "update t_cgiser_mokarole set RANK = "+dRank+" where roleId="+dRoleId;
        final String[] sqls = new String[2];
        sqls[0] = sql1;
        sqls[1]= sql2;
        TransactionTemplate tt = new TransactionTemplate(mokaDataSourceTransactionManager);
        return (int[])tt.execute(new TransactionCallback<Object>() {
        	public Object doInTransaction(TransactionStatus status) {
        		return mokaJdbcTemplate.batchUpdate(sqls);
        	}
		});
	}
	public int updateRoleLastLoginAndLonginTimes(Date date, int times,Long roleId){
		final String sql = "update t_cgiser_mokarole set LASTLOGIONTIME = '"+new Timestamp(date.getTime())+"' , LOGINCONTINUETIMES = "+times+" where roleId="+roleId;
		return mokaJdbcTemplate.update(sql);
	}
	public int upgradeVip(Long roleId,int vip){
		final String sql = "update t_cgiser_mokarole set vip = "+vip+" where roleId="+roleId;
		return mokaJdbcTemplate.update(sql);
	}
	public int addEnergyMax(Long roleId, int eneryMax) {
		final String sql = "update t_cgiser_mokarole set ENERGYMAX = ENERGYMAX+"+eneryMax+" where roleId="+roleId;
		return mokaJdbcTemplate.update(sql);
	}
	public int addFriendNumMax(Long roleId, int friendNum) {
		final String sql = "update t_cgiser_mokarole set FRIENDNUMMAX = FRIENDNUMMAX+"+friendNum+" where roleId="+roleId;
		return mokaJdbcTemplate.update(sql);
	}
	public int updateRoleMapLastInCome(Long roleId, Date date) {
		final String sql = "update t_cgiser_mokarole set MAPLASTINCOME = '"+new Timestamp(date.getTime())+"' where roleId="+roleId;
		return mokaJdbcTemplate.update(sql);
	}
	public int updateRoleSoulRefreshTime(Long roleId, Date date) {
		final String sql = "update t_cgiser_mokarole set SOULREFRESHTIME = '"+new Timestamp(date.getTime())+"' where roleId="+roleId;
		return mokaJdbcTemplate.update(sql);
	}
	public int updateRoleSoul(Long roleId, String souls) {
		final String sql = "update t_cgiser_mokarole set souls = '"+souls+"' where roleId="+roleId;
		return mokaJdbcTemplate.update(sql);
	}
	public int updateRoleBuyGoodTimes(Long roleId, int times) {
		final String sql = "update t_cgiser_mokarole set BUYGOODCOUNT = "+times+" where roleId="+roleId;
		return mokaJdbcTemplate.update(sql);
	}
	public  int getRoleBuyGoodTimes(Long roleId){
		final String sql = "select BUYGOODCOUNT from t_cgiser_mokarole where roleid = ?";
		String[] para = new String[1];
        para[0] = String.valueOf(roleId);
		return mokaJdbcTemplate.queryForInt(sql,para);
	}
	public int updateRoleLastReceiveEnergy(Long roleId, Date date) {
		final String sql = "update t_cgiser_mokarole set LASTRECEIVEENERGY = '"+new Timestamp(date.getTime())+"' where roleId="+roleId;
		return mokaJdbcTemplate.update(sql);
	}
	public int updateRoleFreeFightTime(Long roleId, Date date) {
		final String sql = "update t_cgiser_mokarole set FREEFIGHTTIME = '"+new Timestamp(date.getTime())+"' where roleId="+roleId;
		return mokaJdbcTemplate.update(sql);
	}
	public int updateRoleSendEnergyTimes(Long roleId,int num){
		final String sql = "update t_cgiser_mokarole set SENDENERGYTIMES = "+num+" where roleId="+roleId;
		return mokaJdbcTemplate.update(sql);
	}
	public int updateRoleAvatar(Long roleId,int id){
		final String sql = "update t_cgiser_mokarole set avatar = "+id+" where roleId="+roleId;
		return mokaJdbcTemplate.update(sql);
	}
	public  Map<String, Object> RandomRoleByLevel(Long roleId,Long roleId1){
//		final String sql = "SELECT * FROM t_cgiser_mokarole " +
//				"WHERE roleid >= (SELECT floor( RAND() * ((SELECT MAX(roleid) FROM t_cgiser_mokarole )- " +
//				"(SELECT MIN(roleid) FROM t_cgiser_mokarole )) + (SELECT MIN(roleid) FROM t_cgiser_mokarole ))) " +
//				"AND ROLEID <> ? ORDER BY roleid LIMIT 1;";
		final String sql = "select * from t_cgiser_mokarole where roleid > ? and roleid <> ? and level>=5 limit 1";
		String[] para = new String[2];
        para[0] = String.valueOf(roleId);
        para[1] = String.valueOf(roleId1);
		return mokaJdbcTemplate.queryForMap(sql,para);
	}
	public JdbcTemplate getMokaJdbcTemplate() {
		return mokaJdbcTemplate;
	}

	public void setMokaJdbcTemplate(JdbcTemplate mokaJdbcTemplate) {
		this.mokaJdbcTemplate = mokaJdbcTemplate;
	}
	public DataSourceTransactionManager getMokaDataSourceTransactionManager() {
		return mokaDataSourceTransactionManager;
	}
	public void setMokaDataSourceTransactionManager(
			DataSourceTransactionManager mokaDataSourceTransactionManager) {
		this.mokaDataSourceTransactionManager = mokaDataSourceTransactionManager;
	}
	public int addLostTimes(Long roleId){
		final String sql = "update t_cgiser_mokarole set lose = lose + 1 where roleId="+roleId;
		return mokaJdbcTemplate.update(sql);
	}
	public int addRankLostTimes(Long roleId){
		final String sql = "update t_cgiser_mokarole set ranklost = ranklost+1 where roleId="+roleId;
		return mokaJdbcTemplate.update(sql);
	}
	public int addRankWinTimes(Long roleId){
		final String sql = "update t_cgiser_mokarole set rankwin = rankwin + 1 where roleId="+roleId;
		return mokaJdbcTemplate.update(sql);
	}
	public int addWinTimes(Long roleId){
		final String sql = "update t_cgiser_mokarole set win = win + 1 where roleId="+roleId;
		return mokaJdbcTemplate.update(sql);
	}
	public int updateFreshStep(String value,Long roleId) {
		final String sql = "update t_cgiser_mokarole set freshstep = '"+value+"' where roleId="+roleId;
		return mokaJdbcTemplate.update(sql);
	}
	public int updateRoleCity(Long roleId,String city){
		final String sql = "update t_cgiser_mokarole set city = '"+city+"' where roleId="+roleId;
		return mokaJdbcTemplate.update(sql);
	}
	public int addRoleInvitNum(Long roleId,int num){
		final String sql = "update t_cgiser_mokarole set INVITENUM = INVITENUM +"+num+" where roleId="+roleId;
		return mokaJdbcTemplate.update(sql);
	}
	public int updateRoleInvitNum(Long roleId,int num){
		final String sql = "update t_cgiser_mokarole set INVITENUM = "+num+" where roleId="+roleId;
		return mokaJdbcTemplate.update(sql);
	}
	public List<Map<String,Object>> getRoleByCity(String city,int num){
		final String sql = "select * from t_cgiser_mokarole where city = '"+city+"' ORDER BY LASTLOGIONTIME desc LIMIT "+num;
		return mokaJdbcTemplate.queryForList(sql);
	}
}

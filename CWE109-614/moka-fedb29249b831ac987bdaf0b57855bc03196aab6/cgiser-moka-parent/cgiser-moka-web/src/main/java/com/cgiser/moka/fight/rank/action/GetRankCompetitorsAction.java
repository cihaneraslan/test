package com.cgiser.moka.fight.rank.action;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cgiser.core.common.cache.mem.MemCachedManager;
import com.cgiser.keel.http.utils.HttpSpringUtils;
import com.cgiser.moka.action.AbstractAction;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.manager.SalaryManager;
import com.cgiser.moka.model.RankRoleCache;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.result.RankCompetitorsResult;
import com.cgiser.moka.result.ReturnType;

public class GetRankCompetitorsAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		ReturnType<RankCompetitorsResult> returnType = new ReturnType<RankCompetitorsResult>();
		try{
			Role role = super.getCurrentRole(request);
			if(null==role){
				returnType.setMsg("您还没登录哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			
			RoleManager roleManager = (RoleManager)HttpSpringUtils.getBean("roleManager");
			//直接从数据库获取当前排名
			int rank = roleManager.getRankByRoleId(role.getRoleId());
			List<Role> roles = roleManager.getRankCompetitors(rank);
			RankCompetitorsResult result = new RankCompetitorsResult();
			result.setCash(10);
			result.setCompetitors(roles);
			MemCachedManager rankCachedManager = (MemCachedManager)HttpSpringUtils.getBean("rankCachedManager");
			RankRoleCache rankRoleCache = (RankRoleCache)rankCachedManager.get("rank_"+String.valueOf(role.getRoleId()));
			if(rankRoleCache==null){
				rankRoleCache = new RankRoleCache();
				rankRoleCache.setRankFightTime(new Date(new Date().getTime()-10*60*1000));
				rankRoleCache.setRankHasTimes(15);
				rankRoleCache.setRankStreakTimes(0);
				rankRoleCache.setRoleId(role.getRoleId());
			}
			Date date = new Date();
			if(rankRoleCache.getRankFightTime().before(date)&&date.getDate()!=rankRoleCache.getRankFightTime().getDate()){
				result.setCountdown(0L);
				if(rankRoleCache.getRankHasTimes()<15){
					result.setRankHasTimes(15);
					rankRoleCache.setRankHasTimes(15);
				}else{
					result.setRankHasTimes(rankRoleCache.getRankHasTimes());
				}
			}else if(rankRoleCache.getRankFightTime().before(date)&&date.getDate()==rankRoleCache.getRankFightTime().getDate()){
				if((new Date().getTime()-rankRoleCache.getRankFightTime().getTime())/1000>600){
					result.setCountdown(0L);
				}else{
					result.setCountdown(600-(new Date().getTime()-rankRoleCache.getRankFightTime().getTime())/1000);
				}	
				result.setRankHasTimes(rankRoleCache.getRankHasTimes());
			}else{
				result.setCountdown(0L);
			}
			rankRoleCache.setRank(rank);
			rankCachedManager.set("rank_"+String.valueOf(role.getRoleId()), 0, rankRoleCache);
			SalaryManager salaryManager = (SalaryManager)HttpSpringUtils.getBean("salaryManager");
			int coins = salaryManager.getRankSalaryByRank(role.getRank());
			result.setRankSalary(coins);
			Date rankDate = salaryManager.getLastRankSalaryTime();
			result.setRankSalaryTimes(3*24*60*60-(new Date().getTime()-rankDate.getTime())/1000);
			result.setRank(rank);
			returnType.setStatus(1);
			returnType.setValue(result);
			super.printReturnType2Response(response, returnType);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	} 
}

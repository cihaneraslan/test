package com.cgiser.moka.legion.action;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.cgiser.keel.http.utils.HttpSpringUtils;
import com.cgiser.moka.action.AbstractAction;
import com.cgiser.moka.manager.LegionManager;
import com.cgiser.moka.model.Legion;
import com.cgiser.moka.model.Legioner;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.result.ReturnType;

public class UpDutyAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		ReturnType<String> returnType = new ReturnType<String>();
		try{
			Role role = super.getCurrentRole(request);
			if(null==role){
				returnType.setMsg("您还没登录哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			LegionManager legionManager = (LegionManager)HttpSpringUtils.getBean("legionManager");
			Legioner legioner = legionManager.getLegioner(role.getRoleId());
			if(legioner==null){
				returnType.setMsg("您还没加入军团哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			Legion legion  = legionManager.getLegionById(legioner.getLegionId());
			if(legion==null){
				returnType.setMsg("军团不存在！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(legioner.getDuty()==1){				
				returnType.setMsg("您已经是团长了，不能再升了哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(legioner.getDuty()==2){
				Legioner header = legionManager.getHeader(legion.getId());
//				if(new Date().getDay()-header.getLastLoginDate().getDay())
				if(this.beforeXDays(header.getLastLoginDate(), 7)){
					if(legioner.getContribute()>header.getContribute()){
						legionManager.updateHeader(legion.getId(), legioner.getId());
					}else{
						returnType.setMsg("您的贡献值还不够晋升为团长哦");
						returnType.setStatus(0);
						super.printReturnType2Response(response, returnType);
						return null;
					}
				}else{
					returnType.setMsg("团长7天不再线，您才可以顶替其位置");
					returnType.setStatus(0);
					super.printReturnType2Response(response, returnType);
					return null;
				}
			}
			if(legioner.getDuty()==3){
				Legioner header = legionManager.getDeputyHeader(legion.getId());
				if(header!=null){
					if(legioner.getContribute()>header.getContribute()){
						legionManager.updateDeputyHeader(legion.getId(), legioner.getId());
					}else{
						returnType.setMsg("您的贡献值还不够晋升为副团长哦");
						returnType.setStatus(0);
						super.printReturnType2Response(response, returnType);
						return null;
					}
				}else{
					legionManager.updateDeputyHeader(legion.getId(), legioner.getId());
				}
			}
			if(legioner.getDuty()==4){
				List<Legioner> list = legionManager.getLegionersByduty(legion.getId(), 3);
				if(CollectionUtils.isEmpty(list)||list.size()<2){
					legionManager.updateLegionerDuty(legioner.getId(), 3);
				}else{
					Legioner legioner2 = list.get(0);
					Legioner legioner3 = list.get(1);
					if(legioner.getContribute()>legioner2.getContribute()){
						legionManager.updateLegionerDuty(legioner.getId(), 3);
						legionManager.resignDuty(legioner2.getId());
					}else if(legioner.getContribute()>legioner3.getContribute()){
						legionManager.updateLegionerDuty(legioner.getId(), 3);
						legionManager.resignDuty(legioner3.getId());
					}else{
						returnType.setMsg("您的贡献值还不够晋升为领主哦");
						returnType.setStatus(0);
						super.printReturnType2Response(response, returnType);
						return null;
					}
				}
			}
			if(legioner.getDuty()==5){
				List<Legioner> list = legionManager.getLegionersByduty(legion.getId(), 4);
				if(CollectionUtils.isEmpty(list)||list.size()<4){
					legionManager.updateLegionerDuty(legioner.getId(), 4);
				}else{
					Legioner legioner2 = list.get(0);
					Legioner legioner3 = list.get(1);
					Legioner legioner4 = list.get(2);
					Legioner legioner5 = list.get(3);
					if(legioner.getContribute()>legioner2.getContribute()){
						legionManager.updateLegionerDuty(legioner.getId(), 4);
						legionManager.resignDuty(legioner2.getId());
					}else if(legioner.getContribute()>legioner3.getContribute()){
						legionManager.updateLegionerDuty(legioner.getId(), 4);
						legionManager.resignDuty(legioner3.getId());
					}else if(legioner.getContribute()>legioner4.getContribute()){
						legionManager.updateLegionerDuty(legioner.getId(), 4);
						legionManager.resignDuty(legioner4.getId());
					}else if(legioner.getContribute()>legioner5.getContribute()){
						legionManager.updateLegionerDuty(legioner.getId(), 4);
						legionManager.resignDuty(legioner5.getId());
					}else{
						returnType.setMsg("您的贡献值还不够晋升为骑士哦");
						returnType.setStatus(0);
						super.printReturnType2Response(response, returnType);
						return null;
					}
				}
			}
			
			
			returnType.setStatus(1);
			super.printReturnType2Response(response, returnType);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
	private boolean beforeXDays(Date date,int day){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
//		System.out.println(calendar.get(Calendar.DAY_OF_MONTH));// 今天的日期
		calendar.set(Calendar.DAY_OF_MONTH,
				calendar.get(Calendar.DAY_OF_MONTH) - day);// 让日期加1
		
		return date.before(calendar.getTime());
	}
}

package com.cgiser.moka.legion.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.cgiser.keel.http.utils.HttpSpringUtils;
import com.cgiser.moka.action.AbstractAction;
import com.cgiser.moka.common.utils.ServletUtil;
import com.cgiser.moka.manager.LegionManager;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.manager.support.BeanUtils;
import com.cgiser.moka.model.Drum;
import com.cgiser.moka.model.Legion;
import com.cgiser.moka.model.LegionContext;
import com.cgiser.moka.model.LegionLevel;
import com.cgiser.moka.model.LegionTech;
import com.cgiser.moka.model.Legioner;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.result.LegionResult;
import com.cgiser.moka.result.ReturnType;

public class GetLegionsAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String index = ServletUtil.getDefaultValue(request,"index","");
		String count = ServletUtil.getDefaultValue(request,"count","");
		String name = ServletUtil.getDefaultValue(request, "name", "");
		ReturnType<LegionResult> returnType = new ReturnType<LegionResult>();
		try{
			Role role = super.getCurrentRole(request);
			if(null==role){
				returnType.setMsg("您还没登录哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(StringUtils.isBlank(index)||!StringUtils.isNumeric(index)){
				logger.debug("获取帮派参数错误");
				index = "1";
			}
			if(StringUtils.isBlank(count)||!StringUtils.isNumeric(count)){
				logger.debug("获取帮派参数错误");
				count = "10";
			}
			LegionManager legionManager = (LegionManager)HttpSpringUtils.getBean("legionManager");
			
			List<Legion> legionInfos =  legionManager.getLegions(name, Integer.parseInt(index), Integer.parseInt(count));
			int legionCount = legionManager.getLegionCount();
			if(!CollectionUtils.isEmpty(legionInfos)){				
				for(int i=0;i<legionInfos.size();i++){
					this.fomartLegion(legionInfos.get(i));
				}
			}
			
			LegionResult legionResult = new LegionResult();
			legionResult.setLegionInfos(legionInfos);
			legionResult.setCount(legionCount);
			legionResult.setCash(10);
			legionResult.setCoins(1000);
			if(role.getLevel()>0){
				Legioner myInfo = legionManager.getLegioner(role.getRoleId());
				if(myInfo!=null){
					legionResult.setMyInfo(myInfo);
					Legion myLegion = legionManager.getLegionById(myInfo.getLegionId());
					if(myLegion!=null){
						int a = myLegion.getBuyAttack()>3?3: myLegion.getBuyAttack();
						legionResult.setBuyLegionAttackCash(LegionContext.BuyAttackCash[a]);
						this.fomartLegion(myLegion);
						
						if(myLegion.getLegionLevel()==10){
							legionResult.setNextLegionResource(0);
						}else{
							LegionLevel legionLevel = legionManager.getLegionLevelByLevel(myLegion.getLegionLevel()+1);
							if(legionLevel!=null){
								legionResult.setNextLegionResource(legionLevel.getResource());
							}
						}
						List<LegionTech> techs =legionManager.getLegionTechs();
						LegionTech legionTech;
						for(int i=0;i<techs.size();i++){
							legionTech = techs.get(i);
							if(myLegion.getLegionLevel()>=legionTech.getLegionLevel()){
								legionTech.setLock(1);
							}else{
								legionTech.setLock(0);
							}
							Long resource = (Long)BeanUtils.getFieldValueByName("contribute"+legionTech.getTechId().replace("tech", ""), myLegion);
							int level = legionTech.getLegionTechLevel(resource);
							legionTech.setContribute(resource - legionTech.getContributeByLevel(level));
							legionTech.setNextContribute(new Long(legionTech.getContributeByLevel(level+1)-legionTech.getContributeByLevel(level)));
							legionTech.setTechLevel(level);
							legionTech.setTechEffect(legionTech.getTechEffect().replace("{?}", String.valueOf((int)(legionTech.getTechLevel()*legionTech.getAnd()))));
						}
						legionResult.setTeach(techs);
						legionResult.setMyLegion(myLegion);
					}
					
				}
			}
			List<Drum> drums = new ArrayList<Drum>();
			Drum drum = new Drum();
			drum.setId(1);
			drum.setEveryDayTimes(1);
			drum.setEveryDayFreeTimes(1);
			drum.setCurrencyType(1);
			drum.setCurrencyValue(40000);
			drum.setHonor(40);
			drums.add(drum);
			drum = new Drum();
			drum.setId(2);
			drum.setEveryDayTimes(1);
			drum.setEveryDayFreeTimes(0);
			drum.setCurrencyType(2);
			drum.setCurrencyValue(50);
			drum.setHonor(500);
			drums.add(drum);
			drum = new Drum();
			drum.setId(3);
			drum.setEveryDayTimes(1);
			drum.setEveryDayFreeTimes(0);
			drum.setCurrencyType(2);
			drum.setCurrencyValue(100);
			drum.setHonor(1200);
			drums.add(drum);
			legionResult.setDrums(drums);
			returnType.setStatus(1);
			returnType.setValue(legionResult);
			super.printReturnType2Response(response, returnType);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
	private void fomartLegion(Legion legion){
		LegionManager legionManager = (LegionManager)HttpSpringUtils.getBean("legionManager");
		RoleManager roleManager = (RoleManager)HttpSpringUtils.getBean("roleManager");
		Role header = roleManager.getRoleById(legion.getHeaderId());
		if(header!=null){
			legion.setHeaderLevel(header.getLevel());
			legion.setHeaderName(header.getRoleName());
			legion.setHeaderSex(header.getSex());
		}

		List<Legioner> legioners = legionManager.getLegioner(legion.getId(), 1, 80);
		legion.setMemberCount(legioners.size()+"/"+(LegionContext.LegionBaseMemberCount+LegionContext.LegionMemberEmblemLevel[legion.getLegionLevel()-1]));
		StringBuffer members = new StringBuffer();
		Legioner legioner = null;
		for(int j=0;j<legioners.size();j++){
			legioner = legioners.get(j);
			members.append(legioner.getRoleId());
			if(j<legioners.size()-1){
				members.append("_");
			}
		}
		legion.setMembers(members.toString());
	}
}

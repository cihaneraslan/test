package com.cgiser.moka.map.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

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
import com.cgiser.moka.common.utils.ServletUtil;
import com.cgiser.moka.manager.LegionManager;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.manager.SalaryManager;
import com.cgiser.moka.manager.StageLevelManager;
import com.cgiser.moka.manager.UserStageManager;
import com.cgiser.moka.manager.support.BeanUtils;
import com.cgiser.moka.model.Legion;
import com.cgiser.moka.model.LegionTech;
import com.cgiser.moka.model.Legioner;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.model.Salary;
import com.cgiser.moka.model.SalaryEnum;
import com.cgiser.moka.model.StageLevel;
import com.cgiser.moka.model.StageLevelColor;
import com.cgiser.moka.model.UserStage;
import com.cgiser.moka.result.ExploreResult;
import com.cgiser.moka.result.ReturnType;

public class ExploreAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		ReturnType<ExploreResult> returnType = new ReturnType<ExploreResult>();
		try{
			Role role = super.getCurrentRole(request);
			if(null==role){
				returnType.setMsg("您还没登录哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			String stageId = ServletUtil.getDefaultValue(request, "stageId", null);
			String level = ServletUtil.getDefaultValue(request, "level", "1");
			if(null==stageId){
				returnType.setMsg("参数有误！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			StageLevelManager stageLevelManager = (StageLevelManager)HttpSpringUtils.getBean("stageLevelManager");
			List<StageLevel> stageLevels = stageLevelManager.getSageLevelsByStageId(Integer.parseInt(stageId));
			UserStageManager userStageManager = (UserStageManager)HttpSpringUtils.getBean("userStageManager");
			UserStage userStage = userStageManager.getUserStageByRoleIdStageId(role.getRoleId(), Integer.parseInt(stageId));
			if(null==userStage){
				returnType.setMsg("您还没开启着一关哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(userStage!=null){
				if(userStage.getFinishedStage()<Integer.parseInt(level)){
					returnType.setMsg("您还没战胜这一关不能扫荡！");
					returnType.setStatus(0);
					super.printReturnType2Response(response, returnType);
					return null;
				}
				StageLevel stageLevel = stageLevels.get(Integer.parseInt(level)-1);
				int coins = stageLevel.getBonusWinGold();
				if(role.getEnergy()<stageLevel.getEnergyExplore()){
					returnType.setMsg("扫荡所需要的体力不够");
					returnType.setStatus(0);
					super.printReturnType2Response(response, returnType);
					return null;
				}
				
				Random rnd = new Random();
				int exp = stageLevel.getBonusWinExp();
				coins = coins*12/10;
				coins = coins*(100-rnd.nextInt(40))/100;
				exp = exp*(100-rnd.nextInt(20))/100;
				ExploreResult result = new ExploreResult();
				RoleManager roleManager = (RoleManager)HttpSpringUtils.getBean("roleManager");
				if(role.getVip()==2){
					result.setExtraExp(exp/20);
					result.setExtraCoins(coins/20);
					exp = exp+exp/20;
					coins = coins+coins/20;
				}
				if(role.getVip()==3){
					result.setExtraExp(exp/10);
					result.setExtraCoins(coins/10);
					exp = exp+exp/10;
					coins = coins+coins/10;
				}
				LegionManager legionManager = (LegionManager)HttpSpringUtils.getBean("legionManager");
				Legioner legioner = legionManager.getLegioner(role.getRoleId());
				if (legioner != null) {
					Legion legion = legionManager.getLegionById(legioner
							.getLegionId());
					if (legion != null) {
						List<LegionTech> techs = legionManager.getLegionTechs();
						LegionTech legionTech;
						for (int i = 0; i < techs.size(); i++) {
							legionTech = techs.get(i);
							if (legion.getLegionLevel() >= legionTech
									.getLegionLevel()) {
								legionTech.setLock(1);
								Long resource = (Long) BeanUtils
										.getFieldValueByName(
												"contribute" + (i + 1), legion);
								int level1 =(int)(legionTech.getLegionTechLevel(resource)*legionTech.getAnd());
								if (level1 > 0) {
									if (legionTech.getType() == 3) {
										exp = exp+exp*level1/100;
									}
									if (legionTech.getType() == 4) {
										coins = coins+coins*level1/100;
									}
								}
							}
						}

					}
				}
				if(!roleManager.addCoin(role.getRoleName(), coins)){
					logger.error("探索给用户加铜钱失败");
				}
				if(!roleManager.addExp(role.getRoleName(), exp)){
					logger.error("探索给用户加经验失败");
				}
				if(!roleManager.updateEnergy(role.getRoleName(), stageLevel.getEnergyExplore())){
					logger.error("探索给用户减体力失败");
				}
				SalaryManager salaryManager = (SalaryManager)HttpSpringUtils.getBean("salaryManager");
				List<StageLevelColor> stageLevelColors = stageLevelManager.getStageLevelColor(stageLevel.getId());
				Salary salary;
				List<Salary> salaries = new ArrayList<Salary>();
				//铜钱奖励
				salary = new Salary();
				salary.setRoleId(role.getRoleId());
				salary.setTime(new Date());
				salary.setType(SalaryEnum.StageSalary.getValue());
				salary.setAwardType(1);
				salary.setAwardValue(coins);
				salaries.add(salary);
				//经验奖励
				salary = new Salary();
				salary.setRoleId(role.getRoleId());
				salary.setTime(new Date());
				salary.setType(SalaryEnum.StageSalary.getValue());
				salary.setAwardType(12);
				salary.setAwardValue(exp);
				salaries.add(salary);

				if(!CollectionUtils.isEmpty(stageLevelColors)){
					for(StageLevelColor stageLevelColor:stageLevelColors){
						if(rnd.nextInt(10000)<stageLevelColor.getColor()*100){
							salary = new Salary();
							salary.setRoleId(role.getRoleId());
							salary.setTime(new Date());
							salary.setAwardType(stageLevelColor.getType());
							salary.setAwardValue(stageLevelColor.getValue());
							salary.setType(SalaryEnum.StageSalary.getValue());
							salaryManager.extendSalaryAdd(salary.getAwardType(), salary.getAwardValue(), role.getRoleId());
							salaries.add(salary);
							break;
						}
					}
					
				}
//				//掉落神龙碎片
//				if(rnd.nextInt(100)<5){
//					int s = rnd.nextInt(3);
//					result.setChipType(s+4);
//					ChipManager chipManager = (ChipManager)HttpSpringUtils.getBean("chipManager");
//					chipManager.addChip(role.getRoleId(), result.getChipType(), 1);
//				}
				result.setEnergyExplore(stageLevel.getEnergyExplore());
				result.setSalaryList(salaries);
				returnType.setValue(result);
				returnType.setStatus(1);
				returnType.setMsg("");
				super.printReturnType2Response(response, returnType);
				return null;
			}else{
				returnType.setMsg("您还没开启着一关哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
	public static void main(String[] args) {
		System.out.println((int)((float)0.5*2));
	}
}

package com.cgiser.moka.fane.action;

import java.util.Random;
import java.util.Set;

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
import com.cgiser.moka.common.utils.ServletUtil;
import com.cgiser.moka.dao.util.DigestUtils;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.manager.RuneManager;
import com.cgiser.moka.model.AwardItem;
import com.cgiser.moka.model.FaneContext;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.result.AwardResult;
import com.cgiser.moka.result.FaneResult;
import com.cgiser.moka.result.ReturnType;

public class DoNpcAction extends AbstractAction{
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String strId = ServletUtil.getDefaultValue(request, "id",
		"");
		ReturnType<AwardResult> returnType = new ReturnType<AwardResult>();
		try{
			Role role = super.getCurrentRole(request);
			if(role==null){
				returnType.setValue(null);
				returnType.setStatus(0);
				returnType.setMsg("您还没登录哦!");
				super.printReturnType2Response(response, returnType);
				return null;
			}

			int type = 0;
			int value = 0;
			int id = Integer.parseInt(strId);
			int coins = FaneContext.type[id][1];
			if(role.getCoins()<coins){
				returnType.setValue(null);
				returnType.setStatus(0);
				returnType.setMsg("冥想所需的铜钱不够!");
				super.printReturnType2Response(response, returnType);
				return null;
			}
			int s1 = FaneContext.award[id][0];
			int s2 = FaneContext.award[id][1];
			int s3 = FaneContext.award[id][2];
			int s4 = FaneContext.award[id][3];
			int s5 = FaneContext.award[id][4];
			int s6 = FaneContext.award[id][5];
//			int s7 = FaneContext.award[id][6];
			Random rnd = new Random();
			int s=rnd.nextInt(100)+1;
			String[] freshStep = role.getFreshStep();
			if(Integer.parseInt(freshStep[5])<10){
				if(id==0){
					s = s1;
				}
				if(id==1){
					s = s4;
					RoleManager roleManager = (RoleManager)HttpSpringUtils.getBean("roleManager");
					roleManager.updateFreshStep(5, 10, role.getRoleId());
				}
			}
			if(s<=s3){
				//星辰
				type=2;
				RuneManager runeManager = (RuneManager)HttpSpringUtils.getBean("runeManager");
				
				if(s<=s1){
					value = runeManager.RandomRune(1);
				}else if(s>s1&&s<=s2){
					value = runeManager.RandomRune(2);
				}else{
					value = runeManager.RandomRune(3);
				}
			}else if(s>s3&&s<=s6){
				//碎片
				type=1;
				if(s<=s4){
					//透明碎片
					value = 3;
				}else if(s>s4&&s<=s5){
					//银色碎片
					value = 4;
				}else{
					//金色碎片
					value = 5;
				}
				
			}else{
				type=3;
				value = 4;
			}
			MemCachedManager faneCachedManager = (MemCachedManager)HttpSpringUtils.getBean("faneCachedManager");
			FaneResult result = (FaneResult)faneCachedManager.get("fane_"+DigestUtils.digest(role.getRoleName()));
			if(result==null){
				result = new FaneResult();
			}
			Set<Integer> npcList = result.getNpcList();
			npcList.add(0);
//			Iterator<Integer> ite = npcList.iterator();
			int max = 0;
//			while(ite.hasNext()){
//				j=ite.next();
//				if(max<j){
//					max = j;
//				}
//			}
			s = rnd.nextInt(10);
			if(id>0){
				npcList.remove(id);
			}
			if(Integer.parseInt(freshStep[5])<10){
				if(id==0){
					s = 1;
				}
			}
			max = id+1;
			if(s<FaneContext.type[id][0]&&max<4){
				npcList.add(max);
			}else{
				npcList.add(0);
			}
			AwardItem item = new AwardItem();
			item.setType(type);
			item.setValue(value);
			result.getAwardList().add(item);
			result.setNpcList(npcList);
			result.setCoins(role.getCoins());
			faneCachedManager.set("fane_"+DigestUtils.digest(role.getRoleName()), 0, result);
			AwardResult result1 = new AwardResult();
			result1.setAwardItem(item);
			result1.setNpcList(npcList);
			result1.setCoins(role.getCoins()-coins);
			returnType.setValue(result1);
			returnType.setStatus(1);
			RoleManager roleManager = (RoleManager)HttpSpringUtils.getBean("roleManager");
			roleManager.updateCoin(role.getRoleName(), coins);
			super.printReturnType2Response(response, returnType);
			return null;
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
}

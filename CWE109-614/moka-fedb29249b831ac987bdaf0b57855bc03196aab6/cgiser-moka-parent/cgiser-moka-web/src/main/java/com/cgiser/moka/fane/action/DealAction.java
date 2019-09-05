package com.cgiser.moka.fane.action;

import java.util.HashSet;
import java.util.List;
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
import com.cgiser.moka.dao.util.DigestUtils;
import com.cgiser.moka.manager.FragmentManager;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.manager.UserRuneManager;
import com.cgiser.moka.model.AwardItem;
import com.cgiser.moka.model.RewardItem;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.result.DealResult;
import com.cgiser.moka.result.FaneResult;
import com.cgiser.moka.result.ReturnType;

public class DealAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		ReturnType<DealResult> returnType = new ReturnType<DealResult>();
		try{
			Role role = super.getCurrentRole(request);
			if(role==null){
				returnType.setValue(null);
				returnType.setStatus(0);
				returnType.setMsg("您还没登录哦!");
				super.printReturnType2Response(response, returnType);
				return null;
			}
			DealResult result2 = new DealResult();
			MemCachedManager faneCachedManager = (MemCachedManager)HttpSpringUtils.getBean("faneCachedManager");
			FaneResult result = (FaneResult)faneCachedManager.get("fane_"+DigestUtils.digest(role.getRoleName()));
			if(result==null){
				result = new FaneResult();
				Set<Integer> npcList = new HashSet<Integer>();
				npcList.add(1);
				result.setNpcList(npcList);
				faneCachedManager.set("fane_"+DigestUtils.digest(role.getRoleName()), 0, result);
			}
			UserRuneManager userRuneManager = (UserRuneManager)HttpSpringUtils.getBean("userRuneManager");
			RoleManager roleManager = (RoleManager)HttpSpringUtils.getBean("roleManager");
			int fragment1=0;
			int fragment2=0;
			int fragment3=0;
			int coins = 0;
			for(int i=result.getAwardList().size()-1;i>=0;i--){
				AwardItem item = result.getAwardList().get(i);
				if(item.getType()==1){
					if(item.getValue()==3){
						fragment1++;
					}else if(item.getValue()==4){
						fragment2++;
					}else{
						fragment3++;
					}
					this.addRewardItem(result2.getRewards(), item);
				}else if(item.getType()==2){
					userRuneManager.saveUserRune(item.getValue(), role.getRoleId());
					this.addRewardItem(result2.getRewards(), item);
				}else{
					coins = coins+300;
					this.addRewardItem(result2.getSells(), item);
				}
				result.getAwardList().remove(i);
			}
			faneCachedManager.set("fane_"+DigestUtils.digest(role.getRoleName()), 0, result);
			FragmentManager fragmentManager = (FragmentManager)HttpSpringUtils.getBean("fragmentManager");
			fragmentManager.addFragment(role.getRoleId(), fragment1, fragment2, fragment3);
			roleManager.addCoin(role.getRoleName(), coins);
			role = roleManager.getRoleById(role.getRoleId());
			result2.setCoins(role.getCoins());
			returnType.setValue(result2);
			returnType.setStatus(1);
			super.printReturnType2Response(response, returnType);
			return null;
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
	private void addRewardItem(List<RewardItem> list,AwardItem item){
		RewardItem rItem;
		for(int i=0;i<list.size();i++){
			rItem = list.get(i);
			if(rItem.getType()==item.getType()&&rItem.getValue()==item.getValue()){
				rItem.setNum(rItem.getNum()+1);
				item=null;
				break;
			}
		}
		if(item!=null){
			rItem = new RewardItem();
			rItem.setNum(1);
			rItem.setType(item.getType());
			rItem.setValue(item.getValue());
			list.add(rItem);
		}
	}
}

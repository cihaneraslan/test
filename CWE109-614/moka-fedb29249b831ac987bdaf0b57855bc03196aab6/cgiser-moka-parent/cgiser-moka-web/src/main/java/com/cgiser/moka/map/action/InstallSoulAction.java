package com.cgiser.moka.map.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cgiser.keel.http.utils.HttpSpringUtils;
import com.cgiser.moka.action.AbstractAction;
import com.cgiser.moka.common.utils.ServletUtil;
import com.cgiser.moka.manager.StageLevelManager;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.model.StageLevel;
import com.cgiser.moka.result.ReturnType;

public class InstallSoulAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		ReturnType<String> returnType = new ReturnType<String>();
		try{
			String stageLevelId = ServletUtil.getDefaultValue(request, "stageLevelId", "");
			String strCardId = ServletUtil.getDefaultValue(request, "cardId", "");
			String strSoulId = ServletUtil.getDefaultValue(request, "soulId", "");
			String index = ServletUtil.getDefaultValue(request, "index", "");
			Role role = super.getCurrentRole(request);
			if(null==role){
				returnType.setMsg("您还没登录哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			if(StringUtils.isEmpty(index)||StringUtils.isEmpty(stageLevelId)||StringUtils.isEmpty(strCardId)||StringUtils.isEmpty(strSoulId)){
				returnType.setMsg("参数有误！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			StageLevelManager stageLevelManager = (StageLevelManager)HttpSpringUtils.getBean("stageLevelManager");
			StageLevel stageLevel = stageLevelManager.getSageLevelByStageLevelId(Integer.parseInt(stageLevelId));
			String strCardList = stageLevel.getSoulList();
			String[] cardList = strCardList.split(",");
			int cardId1 = Integer.parseInt(strCardId);
			StringBuffer strSoulList = new StringBuffer();
			for(int i=0;i<cardList.length;i++){
				String cardInfo = cardList[i];
				int cardId = Integer.parseInt(cardInfo.split("_")[0]);
				String soulId = cardInfo.split("_")[1];
				if(cardId==cardId1&&i==Integer.parseInt(index)){
					strSoulList.append(cardId).append("_").append(strSoulId).append(",");
				}else{
					strSoulList.append(cardId).append("_").append(soulId).append(",");
				}
			}
			int isInstall = stageLevelManager.installSoul(Integer.parseInt(stageLevelId), strSoulList.substring(0, strSoulList.length()-1).toString());
			if(isInstall<1){
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			returnType.setStatus(1);
			super.printReturnType2Response(response, returnType);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
}

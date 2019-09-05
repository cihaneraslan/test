package com.cgiser.moka.ticket.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import com.cgiser.moka.manager.ThirdManager;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.model.ThirdRecord;
import com.cgiser.moka.result.ReturnType;
import com.cgiser.moka.result.ThirdRecordResult;

public class GetThirdRecordsAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		ReturnType<ThirdRecordResult> returnType = new ReturnType<ThirdRecordResult>();
		try{
			Role role = super.getCurrentRole(request);
			if(null==role){
				returnType.setMsg("您还没登录哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			ThirdManager thirdManager =(ThirdManager)HttpSpringUtils.getBean("thirdManager");
			String str = thirdManager.getPreThirdNumber();
			String issue = thirdManager.getPreThirdIssue();
			List<ThirdRecord> records = new ArrayList<ThirdRecord>();
			if(!StringUtils.isBlank(str)&&!StringUtils.isEmpty(issue)){
				List<ThirdRecord> records1 = thirdManager.getThirdRecordsByRoleIdIssue(role.getRoleId(), issue);
				int num = 0;
				Map<Integer,Integer> mapCash = thirdManager.getCash(Integer.parseInt(str));
				if(!CollectionUtils.isEmpty(records1)){
					for(ThirdRecord thirdRecord:records1){
						//是否上一期的投注记录
						if(thirdRecord.getDate()==Integer.parseInt(issue)){
							num = thirdRecord.getFirst()*100+thirdRecord.getSecond()*10+thirdRecord.getThird();
							//判断是否中奖
							if(mapCash.containsKey(num)){
								thirdRecord.setIsWin(1);
							}
						}
					}
					records.addAll(records1);
				}
			}	
			issue = thirdManager.getNowThirdIssue();
			List<ThirdRecord> records2 = thirdManager.getThirdRecordsByRoleIdIssue(role.getRoleId(),issue);
			if(!CollectionUtils.isEmpty(records2)){
				for(ThirdRecord thirdRecord:records2){
					//是否上一期的投注记录
					if(thirdRecord.getDate()==Integer.parseInt(issue)){
						//当前期的状态设置为未开奖
						thirdRecord.setIsWin(-1);
					}	
					
				}
				records.addAll(records2);
			}
			ThirdRecordResult result = new ThirdRecordResult();
			result.setRecords(records);
			result.setNumber(str);
			returnType.setStatus(1);
			returnType.setValue(result);
			super.printReturnType2Response(response, returnType);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
}

package com.cgiser.moka.ticket.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cgiser.keel.http.utils.HttpSpringUtils;
import com.cgiser.moka.action.AbstractAction;
import com.cgiser.moka.manager.ThirdManager;
import com.cgiser.moka.manager.support.DateUtils;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.result.ReturnType;
import com.cgiser.moka.result.ThirdDDateResult;

public class GetThirdDDateAction extends AbstractAction {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		ReturnType<ThirdDDateResult> returnType = new ReturnType<ThirdDDateResult>();
		try{
			Role role = super.getCurrentRole(request);
			if(null==role){
				returnType.setMsg("您还没登录哦！");
				returnType.setStatus(0);
				super.printReturnType2Response(response, returnType);
				return null;
			}
			ThirdDDateResult result = new ThirdDDateResult();
			ThirdManager thirdManager = (ThirdManager)HttpSpringUtils.getBean("thirdManager");
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			String str = thirdManager.getNowThirdIssue();
			Long secends = 0L;
			try {
				Date date = dateFormat.parse(str);
				Date date2 = DateUtils.getNextThreeDay(date);
				secends = (date2.getTime() - new Date().getTime())/1000;
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			result.setSeconds(secends);
			result.setDate(str);
			returnType.setStatus(1);
			returnType.setValue(result);
			super.printReturnType2Response(response, returnType);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
	public static void main(String[] args) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		try {
			Date date = dateFormat.parse("20140102");
			Date date2 = DateUtils.getNextThreeDay(date);
			Long secends = (date2.getTime() - date.getTime())/1000/24/60/60;
			System.out.println(secends/24/3600);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Calendar curDate = Calendar.getInstance();
		Calendar tommorowDate = Calendar.getInstance();
		tommorowDate.add(Calendar.DATE, 1);
		System.out.println((tommorowDate.getTimeInMillis() - curDate
				.getTimeInMillis()) / 1000/24/3600);
	}
}

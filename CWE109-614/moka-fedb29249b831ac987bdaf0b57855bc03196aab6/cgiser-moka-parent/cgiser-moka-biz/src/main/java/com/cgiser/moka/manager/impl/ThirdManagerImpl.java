package com.cgiser.moka.manager.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.DynamicChannelBuffer;
import org.springframework.util.CollectionUtils;

import com.cgiser.moka.dao.ThirdDao;
import com.cgiser.moka.manager.EmailManager;
import com.cgiser.moka.manager.MessageManager;
import com.cgiser.moka.manager.PushNotificationManager;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.manager.ThirdManager;
import com.cgiser.moka.manager.support.DateUtils;
import com.cgiser.moka.message.netty.MessageUtil;
import com.cgiser.moka.model.MessageType;
import com.cgiser.moka.model.Role;
import com.cgiser.moka.model.ThirdRecord;

public class ThirdManagerImpl implements ThirdManager {
	private ThirdDao thirdDao;
	private EmailManager emailManager;
	private PushNotificationManager pushNotificationManager;
	private RoleManager roleManager;
	private MessageManager messageManager;
	@Override
	public Long addRecord(Long roleId,int first,int second,int third,int count,int date) {
		// TODO Auto-generated method stub
		return thirdDao.addRecord(roleId, first, second, third, count, date);
	}

	@Override
	public List<ThirdRecord> getThirdRecords(Long roleId) {
		return MapListToThirdRecords(thirdDao.getThirdRecords(roleId));
	}
	private List<ThirdRecord> MapListToThirdRecords(List<Map<String,Object>> list){
    	if(CollectionUtils.isEmpty(list)){
    		return null;
    	}
    	List<ThirdRecord> records = new ArrayList<ThirdRecord>();
    	for(int i=0;i<list.size();i++){
    		records.add(MapToThirdRecord(list.get(i)));
    	}
    	return records;
	}
    private ThirdRecord MapToThirdRecord(Map<String,Object> map){
    	if(CollectionUtils.isEmpty(map)){
    		return null;
    	}
    	ThirdRecord record = new ThirdRecord();
    	record.setCount(Integer.parseInt(map.get("COUNT").toString()));
    	record.setDate(Integer.parseInt(map.get("DATE").toString()));
    	record.setFirst(Integer.parseInt(map.get("FIRST").toString()));
    	record.setId(new Long(map.get("ID").toString()));
    	record.setRoleId(new Long(map.get("ROLEID").toString()));
    	record.setSecond(Integer.parseInt(map.get("SECOND").toString()));
    	record.setThird(Integer.parseInt(map.get("THIRD").toString()));
    	return record;
    } 
	public ThirdDao getThirdDao() {
		return thirdDao;
	}

	public void setThirdDao(ThirdDao thirdDao) {
		this.thirdDao = thirdDao;
	}

	@Override
	public void runLottery() {
		Map map = thirdDao.getNowThirdNumber();
		if(CollectionUtils.isEmpty(map)){
			Date date = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			String str = dateFormat.format(date);
			thirdDao.addThirdDate(str);
			return;
		}
		Date date = ((Timestamp) map.get("DATE"));
		if(DateUtils.getDayDiff(date, new Date())>=3){
			date = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			String str = dateFormat.format(date);
			//修改前一期状态为0
			thirdDao.updateThirdateState();
			//取消当前期
			thirdDao.updateThirdate();
			//生成当前期
			thirdDao.addThirdDate(str);
			int num = new Random().nextInt(1000);
			//生成当前期中奖号码
			thirdDao.updateThirdateNum(String.valueOf(num));
			return;
		}
	}

	@Override
	public String getNowThirdIssue() {
		// TODO Auto-generated method stub
		Map map = thirdDao.getNowThirdNumber();
		if(!CollectionUtils.isEmpty(map)){
			return map.get("ISSUE").toString();
		}else{
			Date date = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			String str = dateFormat.format(date);
			thirdDao.addThirdDate(str);
			return str;
		}
	}

	@Override
	public void reciveSalary() {
		Map map = thirdDao.getThirdDateIssueByStateIsNow();
		if(CollectionUtils.isEmpty(map)){
			return ;
		}
		String issue = map.get("ISSUE").toString();
		Map<Integer,Integer> mapCash = this.getCash(Integer.parseInt(map.get("NUM").toString()));
		List<ThirdRecord> thirdRecords = MapListToThirdRecords(thirdDao.getThirdRecordsByIssue(issue));
		int num = 0;
		if(!CollectionUtils.isEmpty(thirdRecords)){
			for(ThirdRecord thirdRecord:thirdRecords){
				num = thirdRecord.getFirst()*100+thirdRecord.getSecond()*10+thirdRecord.getThird();
				if(mapCash.containsKey(num)){
					emailManager.sendEmail("中奖通知", "恭喜您在长乐坊第"+issue+"期中用"+thirdRecord.getCount()*50+"元宝博得了" +
							""+mapCash.get(num)*thirdRecord.getCount()+"元宝,奖励已发放到您的账户中", 0L, thirdRecord.getRoleId(), 1);
					
					pushNotificationManager.pushNotification(thirdRecord.getRoleId(), "", "亲，您中奖了，赶紧去看看吧！");
					Role role = roleManager.getRoleById(thirdRecord.getRoleId());
					roleManager.addCash(role.getRoleName(), mapCash.get(num)*thirdRecord.getCount());
					ChannelBuffer buffer = new DynamicChannelBuffer(200);
					buffer.writeInt(MessageType.SYSTEM.getCode());
					buffer.writeInt(MessageType.THIRD.getCode());
					MessageUtil.writeString(buffer, role.getRoleName(), "UTF-8");
					buffer.writeInt(mapCash.get(num)*thirdRecord.getCount());
					messageManager.sendMessge(MessageType.SYSTEM, buffer);
					thirdDao.updateThirdRecordIsWin(thirdRecord.getId());
				}
				thirdDao.updateThirdRecordIsRecive(thirdRecord.getId());
			}
		}
		
	}
	/**
	 * 根据中奖号码得出中奖的所有号码单注中奖金额
	 * @return
	 */
	@Override
	public Map<Integer,Integer> getCash(int s){
		Map<Integer,Integer> map = new HashMap<Integer, Integer>();
		int u = 50;
		int a = s/100;
		int b = s%100/10;
		int c = s%100%10;
		int x = 0;
		int y = 0;
		int z = 0;
		List<Integer> s1 = new ArrayList<Integer>();
		s1.add(a);
		s1.add(b);
		s1.add(c);
		List<Integer> s2 = new ArrayList<Integer>();
		int f = 0;
		for(int i=0;i<1000;i++){
			s1.clear();
			s1.add(a);
			s1.add(b);
			s1.add(c);
			x = i/100;
			y = i%100/10;
			z = i%100%10;
			s2.clear();
			s2.add(x);
			s2.add(y);
			s2.add(z);
			for(int j=2;j>=0;j--){
				if(s1.contains(s2.get(j))){	
					s1.remove(s2.get(j));
					s2.remove(j);
				}
			}
			
			if(3-s2.size()>1){
				f++;
			}
			f = 3-s2.size();
			s1.clear();
			s1.add(a);
			s1.add(b);
			s1.add(c);
			s2.clear();
			s2.add(x);
			s2.add(y);
			s2.add(z);
			if(f==3){
				int d = 0;
				for(int j=2;j>=0;j--){
					if(s1.get(j)==s2.get(j)){
						d++;
					}
				}
				map.put(i, 50*u+d*20*50);
			}
			if(f==2){
				int d = 0;
				for(int j=2;j>=0;j--){
					if(s1.get(j)==s2.get(j)){
						d++;
					}
				}
				if(d>=1){
					map.put(i, 2*u+d*u);
				}
			}
//			if(f==1){
//				int d = 0;
//				for(int j=2;j>=0;j--){
//					if(s1.get(j)==s2.get(j)){
//						d++;
//					}
//				}
//				if(d>0){
//					map.put(i, Integer.parseInt(String.valueOf(d*1.4*u),0));
//				}
//				
//			}
			
		}
		return map;
	}

	public EmailManager getEmailManager() {
		return emailManager;
	}

	public void setEmailManager(EmailManager emailManager) {
		this.emailManager = emailManager;
	}

	public PushNotificationManager getPushNotificationManager() {
		return pushNotificationManager;
	}

	public void setPushNotificationManager(
			PushNotificationManager pushNotificationManager) {
		this.pushNotificationManager = pushNotificationManager;
	}

	public RoleManager getRoleManager() {
		return roleManager;
	}

	public void setRoleManager(RoleManager roleManager) {
		this.roleManager = roleManager;
	}

	@Override
	public String getPreThirdNumber() {
		Map map = thirdDao.getThirdDateIssueByStateIsNow();
		if(CollectionUtils.isEmpty(map)){
			return "";
		}
		return map.get("NUM").toString();
	}
	@Override
	public String getPreThirdIssue() {
		Map map = thirdDao.getThirdDateIssueByStateIsNow();
		if(CollectionUtils.isEmpty(map)){
			return "";
		}
		return map.get("ISSUE").toString();
	}

	public MessageManager getMessageManager() {
		return messageManager;
	}

	public void setMessageManager(MessageManager messageManager) {
		this.messageManager = messageManager;
	}
	public static void main(String[] args) {
		Random rnd = new Random();
		while(true){
			System.out.println(rnd.nextInt(1000));
		}
	}

	@Override
	public List<ThirdRecord> getThirdRecordsByRoleIdIssue(Long roleId,String issue) {
		return MapListToThirdRecords(thirdDao.getThirdRecordsByRoleIdIssue(roleId, issue));
	}

	@Override
	public List<ThirdRecord> getWinner() {
		
		return MapListToThirdRecords(thirdDao.getThirdRecordWinner());
	}

	@Override
	public int getNumByIssue(String issue) {
		return Integer.parseInt(thirdDao.getNumByIssue(issue).get("NUM").toString());
	}
}

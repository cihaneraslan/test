package com.cgiser.moka.manager.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import com.cgiser.moka.dao.MailDao;
import com.cgiser.moka.manager.EmailManager;
import com.cgiser.moka.manager.RoleManager;
import com.cgiser.moka.model.Email;

public class EmailManagerImpl implements EmailManager {
	private MailDao mailDao;
	private RoleManager roleManager;
	@Override
	public List<Email> getEmailsByTo(Long roleId) {
		List<Email> emails = new ArrayList<Email>();
		List<Email> sysEmails = MapSysListToEmailList(mailDao.getSysEmails());
		if(!CollectionUtils.isEmpty(sysEmails)){
			emails.addAll(sysEmails);
		}
		int count = this.getEmailsCountByToRoleStatus(roleId, 1);
		count = count+ emails.size();
		roleManager.updateRoleNewEmail(roleId, count);
		List<Email> emails1 = MapListToEmailList(mailDao.getEmailsByToRoleId(roleId, 0));
		if(!CollectionUtils.isEmpty(emails1)){
			emails.addAll(emails1);
		}
		return emails;
	}

	@Override
	public Long sendEmail(String title, String content, Long from, Long to,
			int type) {
		Long emailId = mailDao.saveEmail(title, content, from, to, type);
		if(emailId>0&&to>100000000){
			int count = this.getEmailsCountByToRoleStatus(to, 1);
			roleManager.updateRoleNewEmail(to, count);
		}
		return emailId;
	}

	@Override
	public int updateEmailStatue(Long emailId, int status) {
		// TODO Auto-generated method stub
		return mailDao.updateEmailStatus(emailId, status);
	}
	private List<Email> MapListToEmailList(List<Map<String, Object>> list){
		if(CollectionUtils.isEmpty(list)){
			return null;
		}
		List<Email> emailList = new ArrayList<Email>();
		for(Map<String, Object> map:list){
			emailList.add(MapToEmail(map));
		}
		return emailList;	
	}
	private List<Email> MapSysListToEmailList(List<Map<String, Object>> list){
		if(CollectionUtils.isEmpty(list)){
			return null;
		}
		List<Email> emailList = new ArrayList<Email>();
		for(Map<String, Object> map:list){
			emailList.add(MapSysToEmail(map));
		}
		return emailList;	
	}
	private Email MapSysToEmail(Map<String, Object> map){
		if(CollectionUtils.isEmpty(map)){
			return null;
		}
		Email email = new Email();
		email.setId(new Long(map.get("EMAILID").toString()));
		email.setTitle(map.get("TITLE").toString());
		email.setContent(map.get("CONTENT").toString());
		email.setFrom("系统管理员");
		email.setTo("玩家");
		email.setAddTime(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		email.setAddDate(dateFormat.format(new Date(email.getAddTime())));
		email.setType(1);
		email.setStatus(1);
		return email;
		
	}
	private Email MapToEmail(Map<String, Object> map){
		if(CollectionUtils.isEmpty(map)){
			return null;
		}
		Email email = new Email();
		email.setId(new Long(map.get("EMAILID").toString()));
		email.setTitle(map.get("TITLE").toString());
		email.setContent(map.get("CONTENT").toString());
		Long from = new Long(map.get("FROMROLE").toString());
		if(from>0){
			email.setFrom(roleManager.getRoleById(from).getRoleName());
		}else{
			email.setFrom("系统管理员");
		}
		
		Long to = new Long(map.get("TOROLE").toString());
		if(to>0){
			email.setTo(roleManager.getRoleById(to).getRoleName());
		}else{
			email.setTo("系统管理员");
		}
		email.setAddTime(((Timestamp)map.get("ADDTIME")).getTime());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		email.setAddDate(dateFormat.format(new Date(email.getAddTime())));
		email.setType(Integer.parseInt(map.get("TYPE").toString()));
		email.setStatus(Integer.parseInt(map.get("STATUS").toString()));
		return email;
		
	}

	public MailDao getMailDao() {
		return mailDao;
	}

	public void setMailDao(MailDao mailDao) {
		this.mailDao = mailDao;
	}

	public RoleManager getRoleManager() {
		return roleManager;
	}

	public void setRoleManager(RoleManager roleManager) {
		this.roleManager = roleManager;
	}

	@Override
	public int getEmailsCountByToRoleStatus(Long roleId, int status) {
		// TODO Auto-generated method stub
		return mailDao.getEmailCountByToRoleIdStatus(roleId, status);
	}

}

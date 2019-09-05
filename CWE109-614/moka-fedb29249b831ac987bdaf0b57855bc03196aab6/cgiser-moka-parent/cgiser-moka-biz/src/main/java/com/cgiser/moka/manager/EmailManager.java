package com.cgiser.moka.manager;

import java.util.List;

import com.cgiser.moka.model.Email;

public interface EmailManager {
	/**
	 * 发送邮件
	 * @param title
	 * @param content
	 * @param from
	 * @param to
	 * @param type
	 * @return
	 */
	public Long sendEmail(String title, String content, Long from, Long to,
			int type);
	/**
	 * 获取邮件
	 * @param roleId
	 * @return
	 */
	public List<Email> getEmailsByTo(Long roleId);
	/**
	 * 获取未读邮件数量
	 * @param roleId
	 * @param status
	 * @return
	 */
	public int getEmailsCountByToRoleStatus(Long roleId,int status);
	/**
	 * 更新邮件状态
	 * @param emailId
	 * @param status
	 * @return
	 */
	public int updateEmailStatue(Long emailId,int status);
}

package com.cgiser.moka.manager;
/**
 * 推送消息管理接口
 * @author Administrator
 *
 */
public interface PushNotificationManager {
	/**
	 * 修改用户的tokenId
	 * @param roleId
	 * @param tokenId
	 * @return
	 */
	public Long updateRoleTokenId(Long roleId,String tokenId);
	/**
	 * 向用户推送消息
	 * @param roleId
	 * @param title
	 * @param content
	 * @return
	 */
	public int pushNotification(Long roleId,String title,String content);
	/**
	 * 向用户推送匹配战开启的消息
	 * @return
	 */
	public void pushMatchGameMessageToAll();
	/**
	 * 向用户推送领取体力的消息
	 * @return
	 */
	public void pushEnertyMessageToAll();
	/**
	 * 每隔10分钟给用户发放体力
	 */
	public void pushEnergyToAll();
}

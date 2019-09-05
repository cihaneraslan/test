package com.cgiser.moka.manager;

import java.util.List;

import com.cgiser.moka.model.Friend;

public interface FriendsManager {
	/**
	 * 获取好友列表
	 * @param roleName
	 * @return
	 */
	public List<Friend> getFriendsByRoleName(String roleName);
	/**
	 * 获取邀请列表
	 * @param roleName
	 * @return
	 */
	public List<Friend>  getFriendsInviteByRoleName(String roleName);
	/**
	 * 邀请好友
	 * @param roleName1
	 * @param roleName2
	 * @return
	 */
	public Long inviteFriend(String roleName1,String roleName2);
	/**
	 * 接受好友邀请
	 * @param roleName1
	 * @param roleName2
	 * @return
	 */
	public Long applyFriendInvite(String roleName1,String roleName2);
	/**
	 * 删除好友邀请
	 * @param roleName1
	 * @param roleName2
	 * @return
	 */
	public Long delFriendInvite(String roleName1,String roleName2);
	/**
	 * 查看角色是否为好友
	 * @param roleName1
	 * @param roleName2
	 * @return
	 */
	public boolean isFriend(String roleName1,String roleName2);
	
	/**
	 * 查看角色是否已经邀请过好友
	 * @param roleName1
	 * @param roleName2
	 * @return
	 */
	public boolean isInvitFriend(String roleName1,String roleName2);
	
}

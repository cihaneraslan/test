package com.cgiser.moka.manager;

import com.cgiser.moka.model.Gift;
import com.cgiser.moka.model.GiftCode;
import com.cgiser.moka.model.GiftRole;

public interface GiftManager {
	public Gift getGiftById(int giftId);
	public GiftCode getGiftCodeByCodeGiftId(int giftId,String code);
	public GiftRole getGiftRoleByRoleId(Long roleId,int giftId);
	public GiftRole getGiftRoleByRoleIdCode(Long roleId,int giftId,String code);
	public int receiveGift(int giftId,Long roleId,String code);
	public Long saveGiftRole(Long roleId,int giftId,String code);
	public int updateGiftCode(int giftId,String code);
}

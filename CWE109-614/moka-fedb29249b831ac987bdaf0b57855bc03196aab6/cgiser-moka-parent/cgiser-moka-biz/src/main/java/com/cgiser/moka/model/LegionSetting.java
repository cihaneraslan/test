package com.cgiser.moka.model;

import java.util.Map;

public class LegionSetting {
	private long conis_contribute_limit;
	private long cash_contribute_limit;
	private int create_level_limit;
	private int create_legion_cash;
	private int create_legion_coins;
	private int join_legion_level;
	private int buy_legionFightAttack_cash;
	private int buy_legionerFightAttack_cash;
	private int rob_legion_res;
	private Map<String, String> duty;
	public long getConis_contribute_limit() {
		return conis_contribute_limit;
	}
	public void setConis_contribute_limit(long conisContributeLimit) {
		conis_contribute_limit = conisContributeLimit;
	}
	public long getCash_contribute_limit() {
		return cash_contribute_limit;
	}
	public void setCash_contribute_limit(long cashContributeLimit) {
		cash_contribute_limit = cashContributeLimit;
	}
	public int getCreate_level_limit() {
		return create_level_limit;
	}
	public void setCreate_level_limit(int createLevelLimit) {
		create_level_limit = createLevelLimit;
	}
	public int getCreate_legion_cash() {
		return create_legion_cash;
	}
	public void setCreate_legion_cash(int createLegionCash) {
		create_legion_cash = createLegionCash;
	}
	public int getCreate_legion_coins() {
		return create_legion_coins;
	}
	public void setCreate_legion_coins(int createLegionCoins) {
		create_legion_coins = createLegionCoins;
	}
	public int getJoin_legion_level() {
		return join_legion_level;
	}
	public void setJoin_legion_level(int joinLegionLevel) {
		join_legion_level = joinLegionLevel;
	}
	public Map<String, String> getDuty() {
		return duty;
	}
	public void setDuty(Map<String, String> duty) {
		this.duty = duty;
	}
	public int getBuy_legionFightAttack_cash() {
		return buy_legionFightAttack_cash;
	}
	public void setBuy_legionFightAttack_cash(int buyLegionFightAttackCash) {
		buy_legionFightAttack_cash = buyLegionFightAttackCash;
	}
	public int getBuy_legionerFightAttack_cash() {
		return buy_legionerFightAttack_cash;
	}
	public void setBuy_legionerFightAttack_cash(int buyLegionerFightAttackCash) {
		buy_legionerFightAttack_cash = buyLegionerFightAttackCash;
	}
	public int getRob_legion_res() {
		return rob_legion_res;
	}
	public void setRob_legion_res(int robLegionRes) {
		rob_legion_res = robLegionRes;
	}
}
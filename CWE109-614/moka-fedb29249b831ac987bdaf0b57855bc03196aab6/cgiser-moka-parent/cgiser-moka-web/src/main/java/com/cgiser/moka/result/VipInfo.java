package com.cgiser.moka.result;

public class VipInfo {
	public static Integer[] vipcash = {0,60,8000,25000,50000,100000,500000,1000000};
	private int vip;
	private int preCash;
	private int nextCash;
	private String vipSalary;
	private int cash;
	public int getVip() {
		return vip;
	}
	public void setVip(int vip) {
		this.vip = vip;
	}
	public int getPreCash() {
		return preCash;
	}
	public void setPreCash(int preCash) {
		this.preCash = preCash;
	}
	public int getNextCash() {
		return nextCash;
	}
	public void setNextCash(int nextCash) {
		this.nextCash = nextCash;
	}
	public String getVipSalary() {
		return vipSalary;
	}
	public void setVipSalary(String vipSalary) {
		this.vipSalary = vipSalary;
	}
	public int getCash() {
		return cash;
	}
	public void setCash(int cash) {
		this.cash = cash;
	}
	public static int getVipInfo(int cash){
		int vip=0;
		if(cash==0){
			return 0;
		}
		for(int i=0;i<vipcash.length;i++){
			if(cash<vipcash[i]){
				vip = i-1;
				break;
			}
			if(cash==vipcash[i]){
				vip = i;
				break;
			}
		}
		if(vip>3){;
			vip = 3;
		}
		return vip;
	}
}

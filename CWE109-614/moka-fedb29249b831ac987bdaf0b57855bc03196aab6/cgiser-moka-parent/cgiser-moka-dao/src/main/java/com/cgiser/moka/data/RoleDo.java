package com.cgiser.moka.data;

import java.sql.Timestamp;

public class RoleDo {
	private Long RoleId;
	private String RoleName;
	private String UserIden;
	private int Avatar;
	private int Win;
	private int Lose;
	private int Level;
	private int Exp;
	private int PrevExp;
	private int NextExp;
	private int Coins;
	private int Cash;
	private int Ticket;
	private int FreshMan;
	private int FreshStep;
	private int Energy;
    private int EnergyMax;
    private Long EnergyLastTime;
    private Long EnergyBuyTime;
    private int EnergyBuyCount;
    private int LeaderShip;
    private int FriendApplyNum;
    private int FriendNumMax;
    private int DefaultGroupId;
    private int Sex;
    private int Fragment_5;
    private int Fragment_4;
    private int Fragment_3;
    private int RankTimes;
    private int ThievesTimes;
    private int RankWin;
    private int Rank;
    private int RankLost;
    private int HP;
    private int LoginContinueTimes = 0;
    private int SalaryCount = 0;
    private Long ServerId;
    private int InviteNum;
    private String InviteCode;
    private String Udid;
    private int NewEmail;
    private int honor;
    private String bossFightRank;
    private int LoginContinueTimesXX;
    private int Boss;
    private String InviteRoleCode;
	
	public Long getRoleId() {
		return RoleId;
	}
	public void setRoleId(Long roleId) {
		RoleId = roleId;
	}
	public String getRoleName() {
		return RoleName;
	}
	public void setRoleName(String roleName) {
		RoleName = roleName;
	}
	public String getUserIden() {
		return UserIden;
	}
	public void setUserIden(String userIden) {
		UserIden = userIden;
	}
	public int getAvatar() {
		return Avatar;
	}
	public void setAvatar(int avatar) {
		Avatar = avatar;
	}
	public int getWin() {
		return Win;
	}
	public void setWin(int win) {
		Win = win;
	}
	public int getLose() {
		return Lose;
	}
	public void setLose(int lose) {
		Lose = lose;
	}
	public int getLevel() {
		return Level;
	}
	public void setLevel(int level) {
		Level = level;
	}
	public int getExp() {
		return Exp;
	}
	public void setExp(int exp) {
		Exp = exp;
	}
	public int getRank() {
		return Rank;
	}
	public void setRank(int rank) {
		Rank = rank;
	}
	public int getPrevExp() {
		return PrevExp;
	}
	public void setPrevExp(int prevExp) {
		PrevExp = prevExp;
	}
	public int getNextExp() {
		return NextExp;
	}
	public void setNextExp(int nextExp) {
		NextExp = nextExp;
	}
	public int getCoins() {
		return Coins;
	}
	public void setCoins(int coins) {
		Coins = coins;
	}
	public int getCash() {
		return Cash;
	}
	public void setCash(int cash) {
		Cash = cash;
	}
	public int getTicket() {
		return Ticket;
	}
	public void setTicket(int ticket) {
		Ticket = ticket;
	}
	public int getFreshMan() {
		return FreshMan;
	}
	public void setFreshMan(int freshMan) {
		FreshMan = freshMan;
	}
	public int getFreshStep() {
		return FreshStep;
	}
	public void setFreshStep(int freshStep) {
		FreshStep = freshStep;
	}
	public int getEnergy() {
		return Energy;
	}
	public void setEnergy(int energy) {
		Energy = energy;
	}
	public int getEnergyMax() {
		return EnergyMax;
	}
	public void setEnergyMax(int energyMax) {
		EnergyMax = energyMax;
	}
	public Long getEnergyLastTime() {
		return EnergyLastTime;
	}
	public void setEnergyLastTime(Long energyLastTime) {
		EnergyLastTime = energyLastTime;
	}
	public Long getEnergyBuyTime() {
		return EnergyBuyTime;
	}
	public void setEnergyBuyTime(Long energyBuyTime) {
		EnergyBuyTime = energyBuyTime;
	}
	public int getEnergyBuyCount() {
		return EnergyBuyCount;
	}
	public void setEnergyBuyCount(int energyBuyCount) {
		EnergyBuyCount = energyBuyCount;
	}
	public int getLeaderShip() {
		return LeaderShip;
	}
	public void setLeaderShip(int leaderShip) {
		LeaderShip = leaderShip;
	}
	public int getFriendApplyNum() {
		return FriendApplyNum;
	}
	public void setFriendApplyNum(int friendApplyNum) {
		FriendApplyNum = friendApplyNum;
	}
	public int getFriendNumMax() {
		return FriendNumMax;
	}
	public void setFriendNumMax(int friendNumMax) {
		FriendNumMax = friendNumMax;
	}
	public int getDefaultGroupId() {
		return DefaultGroupId;
	}
	public void setDefaultGroupId(int defaultGroupId) {
		DefaultGroupId = defaultGroupId;
	}
	public int getSex() {
		return Sex;
	}
	public void setSex(int sex) {
		Sex = sex;
	}
	public int getFragment_5() {
		return Fragment_5;
	}
	public void setFragment_5(int fragment_5) {
		Fragment_5 = fragment_5;
	}
	public int getFragment_4() {
		return Fragment_4;
	}
	public void setFragment_4(int fragment_4) {
		Fragment_4 = fragment_4;
	}
	public int getFragment_3() {
		return Fragment_3;
	}
	public void setFragment_3(int fragment_3) {
		Fragment_3 = fragment_3;
	}
	public int getRankTimes() {
		return RankTimes;
	}
	public void setRankTimes(int rankTimes) {
		RankTimes = rankTimes;
	}
	public int getThievesTimes() {
		return ThievesTimes;
	}
	public void setThievesTimes(int thievesTimes) {
		ThievesTimes = thievesTimes;
	}
	public int getRankWin() {
		return RankWin;
	}
	public void setRankWin(int rankWin) {
		RankWin = rankWin;
	}
	public int getRankLost() {
		return RankLost;
	}
	public void setRankLost(int rankLost) {
		RankLost = rankLost;
	}
	public int getHP() {
		return HP;
	}
	public void setHP(int hP) {
		HP = hP;
	}
	public int getLoginContinueTimes() {
		return LoginContinueTimes;
	}
	public void setLoginContinueTimes(int loginContinueTimes) {
		LoginContinueTimes = loginContinueTimes;
	}
	public int getSalaryCount() {
		return SalaryCount;
	}
	public void setSalaryCount(int salaryCount) {
		SalaryCount = salaryCount;
	}
	public Long getServerId() {
		return ServerId;
	}
	public void setServerId(Long serverId) {
		ServerId = serverId;
	}
	public int getInviteNum() {
		return InviteNum;
	}
	public void setInviteNum(int inviteNum) {
		InviteNum = inviteNum;
	}
	public String getInviteCode() {
		return InviteCode;
	}
	public void setInviteCode(String inviteCode) {
		InviteCode = inviteCode;
	}
	public String getUdid() {
		return Udid;
	}
	public void setUdid(String udid) {
		Udid = udid;
	}
	public int getNewEmail() {
		return NewEmail;
	}
	public void setNewEmail(int newEmail) {
		NewEmail = newEmail;
	}
	public int getHonor() {
		return honor;
	}
	public void setHonor(int honor) {
		this.honor = honor;
	}
	public String getBossFightRank() {
		return bossFightRank;
	}
	public void setBossFightRank(String bossFightRank) {
		this.bossFightRank = bossFightRank;
	}
	public int getLoginContinueTimesXX() {
		return LoginContinueTimesXX;
	}
	public void setLoginContinueTimesXX(int loginContinueTimesXX) {
		LoginContinueTimesXX = loginContinueTimesXX;
	}
	public int getBoss() {
		return Boss;
	}
	public void setBoss(int boss) {
		Boss = boss;
	}
	public void init(){
		this.Coins = 10000;
		this.Ticket = 1;
		this.Energy = 60;
		this.EnergyMax = 60;
		this.FriendNumMax = 20;
		this.EnergyBuyTime = System.currentTimeMillis();
		this.EnergyLastTime = this.EnergyBuyTime;
	}
	public String getInviteRoleCode() {
		return InviteRoleCode;
	}
	public void setInviteRoleCode(String inviteRoleCode) {
		InviteRoleCode = inviteRoleCode;
	}
}

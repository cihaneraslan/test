package com.cgiser.moka.model;

public enum SalaryEnum {
	LoginSalary(1,"登录奖励"),
	MapSalary(2,"地图收益"),
	AchieveSalary(3,"成就奖励"),
	MatchGameSalary(4,"匹配赛奖励"),
	RankSalary(5,"排名战奖励"),
	RobSalary(6,"入侵战奖励"),
	FreeGameSalary(7,"自由切磋奖励"),
	TeamSalary(8,"组队战奖励"),
	GroupSalary(9,"军团战奖励"),
	MumboSalary(10,"魔神奖励"),
	ExploitSalary(11,"功勋奖励"),
	StageSalary(12,"关卡奖励"),
	OtherSalary(100,"其他奖励");
	private int code;

	private String description;

	private SalaryEnum(int code, String description) {
		this.code = code;
		this.description = description;
	}

	public int getValue() {
		return code;
	}

	public String getDescription() {
		return description;
	}
	
	public static SalaryEnum getSalaryEnum(int value){
		if(value==1){
			return LoginSalary;
		}else if(value==2){
			return MapSalary;
		}else if(value==3){
			return AchieveSalary;
		}else if(value==4){
			return SalaryEnum.MatchGameSalary;
		}else if(value==5){
			return SalaryEnum.RankSalary;
		}else if(value==6){
			return SalaryEnum.RobSalary;
		}else if(value==7){
			return SalaryEnum.FreeGameSalary;
		}else if(value==8){
			return SalaryEnum.TeamSalary;
		}else if(value==9){
			return SalaryEnum.GroupSalary;
		}else if(value==10){
			return MumboSalary;
		}else if(value==11){
			return SalaryEnum.ExploitSalary;
		}else if(value==12){
			return SalaryEnum.StageSalary;
		}else{
			return OtherSalary;
		}
	}
}

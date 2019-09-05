package com.cgiser.moka.model;

import java.io.Serializable;
import java.util.Date;

public class Legion implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2440108936202438808L;
	public static Integer[] legionduty = {0,1,1,3,5,0};
	private Long id;
	private String name;
	private Long headerId;
	private int headerLevel;
	private String headerName;
	private int headerSex;
	private int emblem;
	private String slogan;
	private String notice;
	private int emblemLevel;
	private Date createTime;
	private Long createId;
	private String memberCount;
	private String members;
	private int legionLevel;
	private Long resources;
	private Long contribute1;
	private Long contribute2;
	private Long contribute3;
	private Long contribute4;
	private Long contribute5;
	private Long contribute6;
	private Long contribute7;
	private Long contribute8;
	private Long contribute9;
	private Long contribute10;
	private Long contribute11;
	private int buyAttack;
	private int buyAttackAll;
	private int robTimes;
	private int robTimesAll;
	public Long getContribute10() {
		return contribute10;
	}
	public void setContribute10(Long contribute10) {
		this.contribute10 = contribute10;
	}
	public Long getContribute11() {
		return contribute11;
	}
	public void setContribute11(Long contribute11) {
		this.contribute11 = contribute11;
	}
	private Long guard1;
	private int lastAttack;
	public Long getGuard1() {
		return guard1;
	}
	public void setGuard1(Long guard1) {
		this.guard1 = guard1;
	}
	public Long getGuard2() {
		return guard2;
	}
	public void setGuard2(Long guard2) {
		this.guard2 = guard2;
	}
	public Long getGuard3() {
		return guard3;
	}
	public void setGuard3(Long guard3) {
		this.guard3 = guard3;
	}
	public Long getGuard4() {
		return guard4;
	}
	public void setGuard4(Long guard4) {
		this.guard4 = guard4;
	}
	public Long getGuard5() {
		return guard5;
	}
	public void setGuard5(Long guard5) {
		this.guard5 = guard5;
	}
	public Long getGuard6() {
		return guard6;
	}
	public void setGuard6(Long guard6) {
		this.guard6 = guard6;
	}
	public Long getGuard7() {
		return guard7;
	}
	public void setGuard7(Long guard7) {
		this.guard7 = guard7;
	}
	public Long getGuard8() {
		return guard8;
	}
	public void setGuard8(Long guard8) {
		this.guard8 = guard8;
	}
	public Long getGuard9() {
		return guard9;
	}
	public void setGuard9(Long guard9) {
		this.guard9 = guard9;
	}
	private Long guard2;
	private Long guard3;
	private Long guard4;
	private Long guard5;
	private Long guard6;
	private Long guard7;
	private Long guard8;
	private Long guard9;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getHeaderId() {
		return headerId;
	}
	public void setHeaderId(Long headerId) {
		this.headerId = headerId;
	}
	public int getEmblem() {
		return emblem;
	}
	public void setEmblem(int emblem) {
		this.emblem = emblem;
	}
	public String getSlogan() {
		return slogan;
	}
	public void setSlogan(String slogan) {
		this.slogan = slogan;
	}
	public int getEmblemLevel() {
		return emblemLevel;
	}
	public void setEmblemLevel(int emblemLevel) {
		this.emblemLevel = emblemLevel;
	}
	public int getHeaderLevel() {
		return headerLevel;
	}
	public void setHeaderLevel(int headerLevel) {
		this.headerLevel = headerLevel;
	}
	public String getHeaderName() {
		return headerName;
	}
	public void setHeaderName(String headerName) {
		this.headerName = headerName;
	}
	public int getHeaderSex() {
		return headerSex;
	}
	public void setHeaderSex(int headerSex) {
		this.headerSex = headerSex;
	}
	public Long getCreateId() {
		return createId;
	}
	public void setCreateId(Long createId) {
		this.createId = createId;
	}
	public String getMemberCount() {
		return memberCount;
	}
	public void setMemberCount(String memberCount) {
		this.memberCount = memberCount;
	}
	public int getLegionLevel() {
		return legionLevel;
	}
	public void setLegionLevel(int legionLevel) {
		this.legionLevel = legionLevel;
	}
	public Long getResources() {
		return resources;
	}
	public void setResources(Long resources) {
		this.resources = resources;
	}
	public Long getContribute1() {
		return contribute1;
	}
	public void setContribute1(Long contribute1) {
		this.contribute1 = contribute1;
	}
	public Long getContribute2() {
		return contribute2;
	}
	public void setContribute2(Long contribute2) {
		this.contribute2 = contribute2;
	}
	public Long getContribute3() {
		return contribute3;
	}
	public void setContribute3(Long contribute3) {
		this.contribute3 = contribute3;
	}
	public Long getContribute4() {
		return contribute4;
	}
	public void setContribute4(Long contribute4) {
		this.contribute4 = contribute4;
	}
	public Long getContribute5() {
		return contribute5;
	}
	public void setContribute5(Long contribute5) {
		this.contribute5 = contribute5;
	}
	public Long getContribute6() {
		return contribute6;
	}
	public void setContribute6(Long contribute6) {
		this.contribute6 = contribute6;
	}
	public Long getContribute7() {
		return contribute7;
	}
	public void setContribute7(Long contribute7) {
		this.contribute7 = contribute7;
	}
	public Long getContribute8() {
		return contribute8;
	}
	public void setContribute8(Long contribute8) {
		this.contribute8 = contribute8;
	}
	public Long getContribute9() {
		return contribute9;
	}
	public void setContribute9(Long contribute9) {
		this.contribute9 = contribute9;
	}
	public String getMembers() {
		return members;
	}
	public void setMembers(String members) {
		this.members = members;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public int getLastAttack() {
		return lastAttack;
	}
	public void setLastAttack(int lastAttack) {
		this.lastAttack = lastAttack;
	}
	public String getNotice() {
		return notice;
	}
	public void setNotice(String notice) {
		this.notice = notice;
	}
	public int getBuyAttack() {
		return buyAttack;
	}
	public void setBuyAttack(int buyAttack) {
		this.buyAttack = buyAttack;
	}
	public int getBuyAttackAll() {
		return buyAttackAll;
	}
	public void setBuyAttackAll(int buyAttackAll) {
		this.buyAttackAll = buyAttackAll;
	}
	public int getRobTimes() {
		return robTimes;
	}
	public void setRobTimes(int robTimes) {
		this.robTimes = robTimes;
	}
	public int getRobTimesAll() {
		return robTimesAll;
	}
	public void setRobTimesAll(int robTimesAll) {
		this.robTimesAll = robTimesAll;
	}
}

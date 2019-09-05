package com.cgiser.moka.model;

public class GroupFight {
	private Long id;
	private String groupFightId;
	private int turn;
	private int turns;
	private String turnInfo1;
	private String turnInfo2;
	private String turnInfo3;
	private String turnInfo4;
	private String turnInfo5;
	private String turnInfo6;
	private Long roomId;
	private Long roleId;
	private int state;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getGroupFightId() {
		return groupFightId;
	}
	public void setGroupFightId(String groupFightId) {
		this.groupFightId = groupFightId;
	}
	public int getTurns() {
		return turns;
	}
	public void setTurns(int turns) {
		this.turns = turns;
	}
	public String getTurnInfo1() {
		return turnInfo1;
	}
	public void setTurnInfo1(String turnInfo1) {
		this.turnInfo1 = turnInfo1;
	}
	public String getTurnInfo2() {
		return turnInfo2;
	}
	public void setTurnInfo2(String turnInfo2) {
		this.turnInfo2 = turnInfo2;
	}
	public String getTurnInfo3() {
		return turnInfo3;
	}
	public void setTurnInfo3(String turnInfo3) {
		this.turnInfo3 = turnInfo3;
	}
	public String getTurnInfo4() {
		return turnInfo4;
	}
	public void setTurnInfo4(String turnInfo4) {
		this.turnInfo4 = turnInfo4;
	}
	public String getTurnInfo5() {
		return turnInfo5;
	}
	public void setTurnInfo5(String turnInfo5) {
		this.turnInfo5 = turnInfo5;
	}
	public String getTurnInfo6() {
		return turnInfo6;
	}
	public void setTurnInfo6(String turnInfo6) {
		this.turnInfo6 = turnInfo6;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public Long getRoomId() {
		return roomId;
	}
	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	public int getTurn() {
		return turn;
	}
	public void setTurn(int turn) {
		this.turn = turn;
	}
}

package com.cgiser.moka.result;

import java.util.List;

import com.cgiser.moka.model.RankRole;

public class RankRoleResult {
	private List<RankRole> competitors;
	private int rank;
	public List<RankRole> getCompetitors() {
		return competitors;
	}
	public void setCompetitors(List<RankRole> competitors) {
		this.competitors = competitors;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
}

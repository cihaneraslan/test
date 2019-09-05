package com.cgiser.moka.result;

import java.util.List;

import com.cgiser.moka.model.ThirdRecord;

public class ThirdRecordResult {
	private List<ThirdRecord> records;
	private String Number;
	public List<ThirdRecord> getRecords() {
		return records;
	}
	public void setRecords(List<ThirdRecord> records) {
		this.records = records;
	}
	public String getNumber() {
		return Number;
	}
	public void setNumber(String number) {
		Number = number;
	}
}

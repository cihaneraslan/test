package com.cgiser.moka.result;

import com.cgiser.moka.model.FightResult;

public class FightPartResult {
	private FightResult result;
	private String messageKey;
	public FightResult getResult() {
		return result;
	}
	public void setResult(FightResult result) {
		this.result = result;
	}
	public String getMessageKey() {
		return messageKey;
	}
	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}
}

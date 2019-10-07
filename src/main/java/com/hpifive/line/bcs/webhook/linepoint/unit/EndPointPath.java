package com.hpifive.line.bcs.webhook.linepoint.unit;

public enum EndPointPath {

	BALANCE("balance"),
	ISSUE("issue"),
	TRANSACTIONS("transactions"),
	TRANSACTION("transaction/%s"),
	TRANSACTION_CANCEL("/cancel");
	
	private String path;

	EndPointPath(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}
	
}

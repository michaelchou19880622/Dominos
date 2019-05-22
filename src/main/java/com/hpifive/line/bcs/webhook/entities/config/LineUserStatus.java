package com.hpifive.line.bcs.webhook.entities.config;

public enum LineUserStatus {
	NORMALLY(1),
	BLOCK(9);
	
	private Integer value;

	private LineUserStatus(Integer value) {
		this.value = value;
	}

	public Integer getValue() {
		return value;
	}
	
	
}

package com.hpifive.line.bcs.webhook.entities.config;

public enum SendMessageStatus {
	CREATE(0),
	CANCEL(1),
	SUCCESS(8),
	FAILURE(9);
	
	private Integer value;

	private SendMessageStatus(Integer value) {
		this.value = value;
	}

	public Integer getValue() {
		return value;
	}
	
	
}

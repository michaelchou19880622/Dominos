package com.hpifive.line.bcs.webhook.entities.config;

public enum BelongingStatus {
	
	USEFUL(0),
	USED(1);

	private Integer value;
	
	private BelongingStatus(Integer value) {
		this.value = value;
	}

	public Integer getValue() {
		return this.value;
	}
	
	@Override
	public String toString() {
		return String.valueOf(this.value);
	}
	
}

	

package com.hpifive.line.bcs.webhook.config;

public enum DefaultConfig {
	
	INVOICE_ERROR_COUNT(3),
	CACHESECOND(60),
	LINEMSGSIZE(5),
	PAGESIZE(10);

	private Integer value;
	
	private DefaultConfig(Integer value) {
		this.value = value;
	}

	public Integer getValue() {
		return this.value;
	}
	
}

	

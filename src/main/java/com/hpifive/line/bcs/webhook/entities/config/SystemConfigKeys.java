package com.hpifive.line.bcs.webhook.entities.config;

public enum SystemConfigKeys {
	MAXNUMBEROFCALL("MAX_NUMBER_OF_CALL"),
	NUMBEROFCALL("NUMBER_OF_CALL"),
	TOKENEXPIRETIMES("CHANNEL_TOKEN_BEFORE_EXPIRE_TIMES"),
	TOKENSECOND("CHANNEL_TOKEN_SECONDS");
	
	private String value;

	private SystemConfigKeys(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
}

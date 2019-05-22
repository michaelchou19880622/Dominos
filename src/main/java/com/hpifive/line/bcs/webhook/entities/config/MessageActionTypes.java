package com.hpifive.line.bcs.webhook.entities.config;

public enum MessageActionTypes {
	
	LOCATION("LOCATION"),
	CAMERA("CAMERA"),
	CAMERAROLL("CAMERAROLL"),
	POSTBACK("postback"),
	MESSAGE("message"),
	URI("uri");

	private String value;
	
	private MessageActionTypes(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
	
}

	

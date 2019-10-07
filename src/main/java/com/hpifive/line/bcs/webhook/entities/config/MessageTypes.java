package com.hpifive.line.bcs.webhook.entities.config;

public enum MessageTypes {
	FLEX("FLEX"),
	TEXT("TEXT"),
	STICKER("STICKER"),
	IMAGE("IMAGE"),
	VIDEO("VIDEO"),
	AUDIO("AUDIO"),
	LOCATION("LOCATION"),
	TEMPLATE("TEMPLATE"),
	CAROUSEL("CAROUSEL"),
	IMAGEMAP("IMAGEMAP");

	private String value;
	
	private MessageTypes(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
	
}

	

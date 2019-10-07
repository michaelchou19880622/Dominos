package com.hpifive.line.bcs.webhook.entities.config;

public enum FlexMessageTypes {
	BUBBLE("BUBBLE"),
	CAROUSEL("CAROUSEL"),
	BOX("BOX"),
	TEXT("TEXT"),
	IMAGE("IMAGE"),
	ICON("ICON"),
	BUTTON("BUTTON"),
	SEPARATOR("SEPARATOR"),
	SPACER("SPACER");

	private String value;
	
	private FlexMessageTypes(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
	
}

	

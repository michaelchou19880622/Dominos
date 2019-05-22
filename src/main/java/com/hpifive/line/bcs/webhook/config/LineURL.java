package com.hpifive.line.bcs.webhook.config;

public enum LineURL {
	ACCESSTOKEN("line/webhook/msg/token?path=oauth/accessToken"),
	CONTENT("line/webhook/msg/call/content?path=bot/message/%s/content"),
	PUSH("line/webhook/msg/call?path=bot/message/push"),
	REPLY("line/webhook/msg/call?path=bot/message/reply"),
	MULTICAST("line/webhook/msg/call?path=bot/message/multicast");
	
	private String value;
	
	private LineURL(String url) {
		value = url;
	}
	
	@Override
	public String toString() {
		return value;
	}
	
}

package com.hpifive.line.bcs.webhook.linepoint.unit;

public enum EndPoint {

	ENDPOINT("https://api.line.me/pointConnect/v1/");
	
	private String url;

	private EndPoint(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}
	
}

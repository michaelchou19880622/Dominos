package com.hpifive.line.bcs.webhook.controller.body;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public class ChannelTokenResponseBody {

	@JsonProperty(value="access_token")
	private String accessToken;
	
	@JsonProperty(value="expires_in")
	private Integer expiresIn;
	
	@JsonProperty(value="token_type")
	private String tokenType;

	public ChannelTokenResponseBody(String accessToken, Integer expiresIn, String tokenType) {
		super();
		this.accessToken = accessToken;
		this.expiresIn = expiresIn;
		this.tokenType = tokenType;
	}
	
	public ChannelTokenResponseBody() {
	}
	public String getAccessToken() {
		return accessToken;
	}
	
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public Integer getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(Integer expiresIn) {
		this.expiresIn = expiresIn;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}
	
	
}
package com.hpifive.line.bcs.webhook.akka.body;

public class CallbackUserSource {

	private String type;
	private String userId;
	
	public CallbackUserSource(String type, String userId) {
		super();
		this.type = type;
		this.userId = userId;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getUserId() {
		return userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Override
	public String toString() {
		return "CallbackUserSource [type=" + type + ", userId=" + userId + "]";
	}
	
}

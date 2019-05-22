package com.hpifive.line.bcs.webhook.common;

public class ErrorResponseObject {

	private String message;

	public static ErrorResponseObject message(String message) {
		return new ErrorResponseObject(message);
	}
	
	public ErrorResponseObject(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}

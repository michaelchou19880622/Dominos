package com.hpifive.line.bcs.webhook.common;

public class ResponseDTO {

	Integer statusCode;
	String responseBody;
	
	public ResponseDTO() {
		super();
	}
	
	public ResponseDTO(Integer status, String responseBody) {
		super();
		this.statusCode = status;
		this.responseBody = responseBody;
	}

	public Integer getStatus() {
		return statusCode;
	}
	public void setStatus(Integer status) {
		this.statusCode = status;
	}
	public String getResponseBody() {
		return responseBody;
	}
	public void setResponseBody(String responseBody) {
		this.responseBody = responseBody;
	}

	@Override
	public String toString() {
		return "ResponseDTO [status=" + statusCode + ", responseBody=" + responseBody + "]";
	}
	
	
}

package com.hpifive.line.bcs.webhook.controller.body;

import java.util.Date;

public class UserTrackingResponseBody {

	private String source;
	private Integer count;
	private Date date;

	public UserTrackingResponseBody() {
	}

	public UserTrackingResponseBody(String source, Integer count, Date date) {
		super();
		this.source = source;
		this.count = count;
		this.date = date;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	
	
}

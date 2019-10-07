package com.hpifive.line.bcs.webhook.common;

import java.util.Date;
import java.util.List;

public class UserBindObject {
	private String uid;
	private String event;
	private Date timestamp;
	private List<UserBindDetailDataObject> data;
	
	public UserBindObject() {
	}
	
	public UserBindObject(String uid, String event, Date timestamp, List<UserBindDetailDataObject> data) {
		super();
		this.uid = uid;
		this.event = event;
		this.timestamp = timestamp;
		this.data = data;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public List<UserBindDetailDataObject> getData() {
		return data;
	}

	public void setData(List<UserBindDetailDataObject> data) {
		this.data = data;
	}
	
	
	
}

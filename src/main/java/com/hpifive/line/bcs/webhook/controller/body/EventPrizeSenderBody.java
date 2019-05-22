package com.hpifive.line.bcs.webhook.controller.body;

import com.hpifive.line.bcs.webhook.service.status.EventPrizeSenderStatus;

public class EventPrizeSenderBody {
	
	private Integer count;
	private EventPrizeSenderStatus status;
	
	public EventPrizeSenderBody(Integer count, EventPrizeSenderStatus status) {
		super();
		this.count = count;
		this.status = status;
	}
	
	public EventPrizeSenderBody() {
		super();
	}
	
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public EventPrizeSenderStatus getStatus() {
		return status;
	}
	public void setStatus(EventPrizeSenderStatus status) {
		this.status = status;
	}
	
}

package com.hpifive.line.bcs.webhook.akka.body;

import java.util.List;

public class CallbackBody {

	private List<CallbackMessageContent> events;

	public CallbackBody(List<CallbackMessageContent> events) {
		super();
		this.events = events;
	}

	public CallbackBody() {
		super();
		// TODO Auto-generated constructor stub
	}

	public List<CallbackMessageContent> getEvents() {
		return events;
	}

	public void setEvents(List<CallbackMessageContent> events) {
		this.events = events;
	}
	
	
}

package com.hpifive.line.bcs.webhook.model.scheduler;

public class AsyncSendBodySuccess implements AsyncSender {

	private Long sendId;
	
	public AsyncSendBodySuccess() {
		super();
	}

	public AsyncSendBodySuccess(Long sendId) {
		super();
		this.sendId = sendId;
	}

	public Long getSendId() {
		return sendId;
	}

	public void setSendId(Long sendId) {
		this.sendId = sendId;
	}
	
	
}

package com.hpifive.line.bcs.webhook.model.scheduler;

public class AsyncSendBodyPause implements AsyncSender {

	private Long sendId;
	
	public AsyncSendBodyPause() {
		super();
	}

	public AsyncSendBodyPause(Long sendId) {
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

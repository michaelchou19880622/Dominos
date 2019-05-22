package com.hpifive.line.bcs.webhook.model.scheduler;

public class AsyncSendBodyFail implements AsyncSender {

	private Long sendId;
	
	public AsyncSendBodyFail() {
		super();
	}

	public AsyncSendBodyFail(Long sendId) {
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

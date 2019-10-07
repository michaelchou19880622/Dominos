package com.hpifive.line.bcs.webhook.model.scheduler;

import java.time.ZonedDateTime;
import java.util.List;

import com.linecorp.bot.model.message.Message;

public class AsyncSendBody implements AsyncSender {

	private Long sendId;
	private List<Message> messages;
	private ZonedDateTime pauseTime;
	private Integer count;
	
	public AsyncSendBody() {
		super();
	}
	
	public AsyncSendBody(Long sendId, List<Message> messages, ZonedDateTime pauseTime) {
		super();
		this.sendId = sendId;
		this.messages = messages;
		this.pauseTime = pauseTime;
		this.count = 0;
	}
	
	public Long getSendId() {
		return sendId;
	}
	public void setSendId(Long sendId) {
		this.sendId = sendId;
	}
	public List<Message> getMessages() {
		return messages;
	}
	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}
	public ZonedDateTime getPauseTime() {
		return pauseTime;
	}

	public void setPauseTime(ZonedDateTime pauseTime) {
		this.pauseTime = pauseTime;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
	
	
}

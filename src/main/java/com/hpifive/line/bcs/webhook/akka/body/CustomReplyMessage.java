package com.hpifive.line.bcs.webhook.akka.body;

import java.time.ZonedDateTime;

import com.linecorp.bot.model.ReplyMessage;

public class CustomReplyMessage {

	private ReplyMessage replyMessage;
	private ZonedDateTime createTime;
	
	public CustomReplyMessage(ReplyMessage replyMessage, ZonedDateTime createTime) {
		super();
		this.replyMessage = replyMessage;
		this.createTime = createTime;
	}
	
	public CustomReplyMessage() {
		super();
	}

	public ReplyMessage getReplyMessage() {
		return replyMessage;
	}
	public void setReplyMessage(ReplyMessage replyMessage) {
		this.replyMessage = replyMessage;
	}
	public ZonedDateTime getCreateTime() {
		return createTime;
	}
	public void setCreateTime(ZonedDateTime createTime) {
		this.createTime = createTime;
	}
	
	
}

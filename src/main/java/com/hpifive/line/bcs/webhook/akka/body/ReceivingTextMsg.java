package com.hpifive.line.bcs.webhook.akka.body;

import java.time.ZonedDateTime;

public class ReceivingTextMsg {
	private String id;
	private String uid;
	private String replyToken;
	private String text;
	private ZonedDateTime createTime;
	
	public ReceivingTextMsg(String replyToken, String text, String uid) {
		this.uid = uid;
		this.replyToken = replyToken;
		this.text = text;
	}

	public String getReplyToken() {
		return replyToken;
	}

	public String getText() {
		return text;
	}

	public String getUid() {
		return uid;
	}

	public ZonedDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(ZonedDateTime createTime) {
		this.createTime = createTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}

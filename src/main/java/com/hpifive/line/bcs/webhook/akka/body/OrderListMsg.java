package com.hpifive.line.bcs.webhook.akka.body;

public class OrderListMsg {
	private String id;
	private String uid;
	private String replyToken;
	private String text;
	
	public OrderListMsg(String replyToken, String text, String uid) {
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return String.format("OrderListMsg[id=%s, uid=%s, replyToken=%s, text=%s]", id, uid, replyToken, text);
	}
}

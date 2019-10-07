package com.hpifive.line.bcs.webhook.akka.body;

import java.time.ZonedDateTime;

public class ReceivingImageMsg {
	private String uid;
	private String replyToken;
	private String contentId;
	private ZonedDateTime createTime;
	
	public ReceivingImageMsg(String uid, String replyToken, String contentId, ZonedDateTime createTime) {
		super();
		this.uid = uid;
		this.replyToken = replyToken;
		this.contentId = contentId;
		this.createTime = createTime;
	}
	
	public ReceivingImageMsg() {
		super();
	}
	
	public String getUid() {
		return uid;
	}
	
	public void setUid(String uid) {
		this.uid = uid;
	}
	
	public String getReplyToken() {
		return replyToken;
	}
	
	public void setReplyToken(String replyToken) {
		this.replyToken = replyToken;
	}
	
	public String getContentId() {
		return contentId;
	}
	
	public void setContentId(String contentId) {
		this.contentId = contentId;
	}
	
	public ZonedDateTime getCreateTime() {
		return createTime;
	}
	
	public void setCreateTime(ZonedDateTime createTime) {
		this.createTime = createTime;
	}
	
	
}

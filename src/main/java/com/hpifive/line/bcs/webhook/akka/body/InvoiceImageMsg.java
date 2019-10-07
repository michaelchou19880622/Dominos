package com.hpifive.line.bcs.webhook.akka.body;

import java.awt.image.BufferedImage;
import java.time.ZonedDateTime;

import com.hpifive.line.bcs.webhook.entities.config.KeywordEventTypes;

public class InvoiceImageMsg {

	private String replyToken;
	private String uuid;
	private String invNum;
	private String invTerm;
	private String invRand;
	private BufferedImage buffered;
	private ZonedDateTime receivedTime;
	private KeywordEventTypes type;
	
	public InvoiceImageMsg() {
		super();
	}
	
	public InvoiceImageMsg(String replyToken, String uuid, String invNum, String invTerm, String invRand,
			BufferedImage buffered, ZonedDateTime receivedTime) {
		super();
		this.replyToken = replyToken;
		this.uuid = uuid;
		this.invNum = invNum;
		this.invTerm = invTerm;
		this.invRand = invRand;
		this.buffered = buffered;
		this.receivedTime = receivedTime;
	}
	
	public String getReplyToken() {
		return replyToken;
	}
	public void setReplyToken(String replyToken) {
		this.replyToken = replyToken;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getInvNum() {
		return invNum;
	}
	public void setInvNum(String invNum) {
		this.invNum = invNum;
	}
	public String getInvTerm() {
		return invTerm;
	}
	public void setInvTerm(String invTerm) {
		this.invTerm = invTerm;
	}
	public String getInvRand() {
		return invRand;
	}
	public void setInvRand(String invRand) {
		this.invRand = invRand;
	}
	public BufferedImage getBuffered() {
		return buffered;
	}
	public void setBuffered(BufferedImage buffered) {
		this.buffered = buffered;
	}
	public ZonedDateTime getReceivedTime() {
		return receivedTime;
	}
	public void setReceivedTime(ZonedDateTime receivedTime) {
		this.receivedTime = receivedTime;
	}

	public KeywordEventTypes getType() {
		return type;
	}

	public void setType(KeywordEventTypes type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "InvoiceImageMsg [replyToken=" + replyToken + ", uuid=" + uuid + ", invNum=" + invNum + ", invTerm="
				+ invTerm + ", invRand=" + invRand + ", buffered=" + buffered + ", receivedTime=" + receivedTime
				+ ", type=" + type + "]";
	}
	
}

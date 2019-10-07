package com.hpifive.line.bcs.webhook.akka.body;

import com.hpifive.line.bcs.webhook.entities.InvoiceEntity;
import com.hpifive.line.bcs.webhook.entities.config.KeywordEventTypes;

public class InvoiceMsg {
	
	private Long userId;
	private Long eventId;
	private String uuid;
	private String replyToken;
	private String text;
	private InvoiceEntity invoice;
	private KeywordEventTypes type;
	
	public InvoiceMsg(Long userId, Long eventId, String uuid, String replyToken, String text, InvoiceEntity invoice) {
		super();
		this.userId = userId;
		this.eventId = eventId;
		this.uuid = uuid;
		this.replyToken = replyToken;
		this.text = text;
		this.invoice = invoice;
	}
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getEventId() {
		return eventId;
	}
	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getReplyToken() {
		return replyToken;
	}
	public void setReplyToken(String replyToken) {
		this.replyToken = replyToken;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public InvoiceEntity getInvoice() {
		return invoice;
	}
	public void setInvoice(InvoiceEntity invoice) {
		this.invoice = invoice;
	}

	public KeywordEventTypes getType() {
		return type;
	}

	public void setType(KeywordEventTypes type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "InvoiceMsg [userId=" + userId + ", eventId=" + eventId + ", uuid=" + uuid + ", replyToken=" + replyToken
				+ ", text=" + text + ", invoice=" + invoice + ", type=" + type + "]";
	}
	
	
	
}

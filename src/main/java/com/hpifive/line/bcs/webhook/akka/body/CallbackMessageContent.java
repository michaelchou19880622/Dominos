package com.hpifive.line.bcs.webhook.akka.body;

import com.linecorp.bot.model.event.message.MessageContent;

public class CallbackMessageContent {
	
	private String type;
	private String replyToken;
	private Long timestamp;
	private CallbackUserSource source;
	private MessageContent message;
	
	public CallbackMessageContent(String type, String replyToken, Long timestamp, CallbackUserSource source, MessageContent message) {
		super();
		this.type = type;
		this.replyToken = replyToken;
		this.timestamp = timestamp;
		this.source = source;
		this.message = message;
	}
	public String getReplyToken() {
		return replyToken;
	}
	public void setReplyToken(String replyToken) {
		this.replyToken = replyToken;
	}
	public Long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	public CallbackUserSource getSource() {
		return source;
	}
	public void setSource(CallbackUserSource source) {
		this.source = source;
	}
	public MessageContent getMessage() {
		return message;
	}
	public void setMessage(MessageContent message) {
		this.message = message;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "CallbackBody [type=" + type + ", replyToken=" + replyToken + ", timestamp=" + timestamp + ", source="
				+ source + ", message=" + message + "]";
	}
	
	
}

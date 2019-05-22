package com.hpifive.line.bcs.webhook.model.scheduler;

import java.util.List;

import com.linecorp.bot.model.message.Message;

public class AsyncSendingModel implements AsyncSender {

	private long sendId;
	private List<Long> sendUserIds;
	private List<String> uids;
	private List<Message> messages;
	
	public AsyncSendingModel(long sendId, List<Long> sendUserIds, List<String> uids, List<Message> messages) {
		super();
		this.sendId = sendId;
		this.sendUserIds = sendUserIds;
		this.uids = uids;
		this.messages = messages;
	}

	public List<Long> getSendUserIds() {
		return sendUserIds;
	}

	public void setSendUserIds(List<Long> sendUserIds) {
		this.sendUserIds = sendUserIds;
	}

	public long getSendId() {
		return sendId;
	}

	public void setSendId(long sendId) {
		this.sendId = sendId;
	}

	public List<String> getUids() {
		return uids;
	}

	public void setUids(List<String> uids) {
		this.uids = uids;
	}

	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}
	
	
	
}

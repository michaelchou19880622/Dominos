package com.hpifive.line.bcs.webhook.akka.body;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.linecorp.bot.model.message.Message;

public class CustomPushBody {
	
	private final Set<String> to;
	
	private final List<Message> messages;

	public CustomPushBody(List<String> to, List<Message> messages) {
		super();
		this.to = new HashSet<>(to);
		this.messages = messages;
	}
	
	public CustomPushBody(Set<String> to, List<Message> messages) {
		super();
		this.to = to;
		this.messages = messages;
	}

	public Set<String> getTo() {
		return to;
	}

	public List<Message> getMessages() {
		return messages;
	}
	
}

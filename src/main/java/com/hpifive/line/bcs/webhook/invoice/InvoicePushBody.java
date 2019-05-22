package com.hpifive.line.bcs.webhook.invoice;

import com.hpifive.line.bcs.webhook.entities.config.InvoiceStatus;
import com.hpifive.line.bcs.webhook.entities.config.KeywordEventTypes;

public class InvoicePushBody {

	private InvoiceStatus target;
	private KeywordEventTypes replyType;
	private InvoiceStatus then;
	
	public InvoicePushBody(InvoiceStatus target, KeywordEventTypes replyType, InvoiceStatus then) {
		super();
		this.target = target;
		this.replyType = replyType;
		this.then = then;
	}
	public InvoiceStatus getTarget() {
		return target;
	}
	public void setTarget(InvoiceStatus target) {
		this.target = target;
	}
	public KeywordEventTypes getReplyType() {
		return replyType;
	}
	public void setReplyType(KeywordEventTypes replyType) {
		this.replyType = replyType;
	}
	public InvoiceStatus getThen() {
		return then;
	}
	public void setThen(InvoiceStatus then) {
		this.then = then;
	}
	@Override
	public String toString() {
		return "InvoicePushBody [target=" + target + ", replyType=" + replyType + ", then=" + then + "]";
	}
	
}

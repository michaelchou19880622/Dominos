package com.hpifive.line.bcs.webhook.executors.invoice;

import com.hpifive.line.bcs.webhook.akka.body.InvoiceMsg;
import com.hpifive.line.bcs.webhook.config.ApplicationContextProvider;
import com.hpifive.line.bcs.webhook.entities.config.InvoiceStatus;
import com.hpifive.line.bcs.webhook.entities.config.KeywordEventTypes;
import com.hpifive.line.bcs.webhook.executors.EventExecutor;
import com.hpifive.line.bcs.webhook.executors.EventKeywordMessageExecutor;
import com.hpifive.line.bcs.webhook.service.InvoiceEventService;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class InvoiceFinishExecutor implements EventExecutor {

	private InvoiceMsg msg;
	
	@Override
	public EventExecutor run() {
		InvoiceEventService model = ApplicationContextProvider.getApplicationContext().getBean(InvoiceEventService.class);
		InvoiceStatus status = model.getInvoiceFromGov(msg.getInvoice());
		if (InvoiceStatus.NOT_FOUND == status) {
			msg.setType(KeywordEventTypes.INVOICE_UNVERIFIED);
		}
		return EventKeywordMessageExecutor.builder()
				.eventId(msg.getEventId()).userId(msg.getUserId())
				.type(msg.getType()).replyToken(msg.getReplyToken()).build();
	}

}

package com.hpifive.line.bcs.webhook.executors.invoice;

import com.hpifive.line.bcs.webhook.akka.body.InvoiceMsg;
import com.hpifive.line.bcs.webhook.config.ApplicationContextProvider;
import com.hpifive.line.bcs.webhook.executors.EventExecutor;
import com.hpifive.line.bcs.webhook.executors.EventKeywordMessageExecutor;
import com.hpifive.line.bcs.webhook.service.EventService;
import com.hpifive.line.bcs.webhook.service.InvoiceEventService;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class InvoiceCleanExecutor implements EventExecutor {

	private InvoiceMsg msg;
	
	@Override
	public EventExecutor run() {
		InvoiceEventService model = ApplicationContextProvider.getApplicationContext().getBean(InvoiceEventService.class);
		EventService eventService = ApplicationContextProvider.getApplicationContext().getBean(EventService.class);
		eventService.resetUserInputErrorCount(msg.getEventId(), msg.getUserId());
		model.clean(msg.getInvoice());
		return EventKeywordMessageExecutor.builder()
				.eventId(msg.getEventId()).userId(msg.getUserId())
				.type(msg.getType()).replyToken(msg.getReplyToken()).build();
	}

}

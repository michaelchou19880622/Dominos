package com.hpifive.line.bcs.webhook.executors.invoice;


import com.hpifive.line.bcs.webhook.akka.body.InvoiceMsg;
import com.hpifive.line.bcs.webhook.config.ApplicationContextProvider;
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
public class InvoiceMessageExecutor implements EventExecutor {

	private InvoiceMsg msg;
	
	@Override
	public EventExecutor run() {
		InvoiceEventService model = ApplicationContextProvider.getApplicationContext().getBean(InvoiceEventService.class);
		KeywordEventTypes type = msg.getType();
		if (type == null) {
			msg.setInvoice(model.getUnfinishInvoiceBy(msg));
			KeywordEventTypes eventType = model.getEventTypeByFillInvoice(msg.getInvoice(), msg.getText());
			KeywordEventTypes tempType = model.incrementErrorCountByType(msg.getEventId(), msg.getUserId(), type);
			msg.setType(tempType == null ? eventType : tempType);
			return InvoiceMessageExecutor.builder().msg(msg).build();
		} else if (KeywordEventTypes.INVOICE_START == type) {
			return InvoiceStartExecutor.builder().msg(msg).build();
		} else if (KeywordEventTypes.INVOICE_FINISH == type) {
			return InvoiceFinishExecutor.builder().msg(msg).build();
		} else if (KeywordEventTypes.ERROR_N_TIME == type ||
				KeywordEventTypes.INVOICE_NOT_IN_PERIOD == type ||
				KeywordEventTypes.INVOICE_DUPLICATE == type || 
				KeywordEventTypes.TIME_OUT == type) {
			return InvoiceCleanExecutor.builder().msg(msg).build();
		}
		KeywordEventTypes userErrorOverCountType = model.incrementErrorCountByType(msg.getEventId(), msg.getUserId(), msg.getType());
		if (userErrorOverCountType != null) {
			msg.setType(userErrorOverCountType);
			return InvoiceMessageExecutor.builder().msg(msg).build();
		}
		return EventKeywordMessageExecutor.builder()
				.eventId(msg.getEventId()).userId(msg.getUserId())
				.type(msg.getType()).replyToken(msg.getReplyToken()).build();
	}

}

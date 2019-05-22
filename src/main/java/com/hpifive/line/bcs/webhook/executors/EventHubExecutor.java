package com.hpifive.line.bcs.webhook.executors;

import com.hpifive.line.bcs.webhook.akka.body.EventRegisterMsg;
import com.hpifive.line.bcs.webhook.akka.body.InvoiceMsg;
import com.hpifive.line.bcs.webhook.entities.config.EventTypes;
import com.hpifive.line.bcs.webhook.entities.config.KeywordEventTypes;
import com.hpifive.line.bcs.webhook.executors.invoice.InvoiceMessageExecutor;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class EventHubExecutor implements EventExecutor {

	private EventRegisterMsg msg;
	
	@Override
	public EventExecutor run() {
		EventTypes type = msg.getEventTypes();
		if (EventTypes.INVOICE == type) {
			InvoiceMsg invMsg = new InvoiceMsg(msg.getUserId(), msg.getEventId(), null, msg.getReplyToken(), msg.getKeyword(), null);
			invMsg.setType(KeywordEventTypes.INVOICE_START);
			return InvoiceMessageExecutor.builder().msg(invMsg).build();
		} else if (EventTypes.ITERATE == type) {
			
		}
		return EventAttendanceMessageExecutor.builder()
				.eventId(msg.getEventId())
				.attendanceId(msg.getAttendanceId())
				.applyStatus(msg.getApplyStatus())
				.type(msg.getKeywordEventTypes())
				.replyToken(msg.getReplyToken())
				.build();
	}

}

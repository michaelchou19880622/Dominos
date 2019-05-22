package com.hpifive.line.bcs.webhook.executors.event;

import com.hpifive.line.bcs.webhook.akka.body.EventRegisterMsg;
import com.hpifive.line.bcs.webhook.config.ApplicationContextProvider;
import com.hpifive.line.bcs.webhook.dao.EventAttendanceDao;
import com.hpifive.line.bcs.webhook.executors.DefaultEventExecutor;
import com.hpifive.line.bcs.webhook.executors.EventAttendanceMessageExecutor;
import com.hpifive.line.bcs.webhook.executors.EventExecutor;

public class EventApplyAgreeExecutor extends DefaultEventExecutor {
	
	public EventApplyAgreeExecutor(EventRegisterMsg msg) {
		super(msg);
	}
	
	@Override
	public EventExecutor run() {
		EventAttendanceDao model = ApplicationContextProvider.getApplicationContext().getBean(EventAttendanceDao.class);
		model.addByEventIdAndUserId(msg.getUserId(), msg.getEventId());
		return EventAttendanceMessageExecutor.builder()
				.eventId(msg.getEventId())
				.attendanceId(msg.getAttendanceId())
				.applyStatus(0)
				.type(msg.getKeywordEventTypes())
				.replyToken(msg.getReplyToken())
				.build();
	}

}

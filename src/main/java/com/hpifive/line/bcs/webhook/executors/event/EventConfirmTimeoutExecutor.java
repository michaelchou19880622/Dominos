package com.hpifive.line.bcs.webhook.executors.event;

import com.hpifive.line.bcs.webhook.akka.body.EventRegisterMsg;
import com.hpifive.line.bcs.webhook.common.DateTimeModel;
import com.hpifive.line.bcs.webhook.config.ApplicationContextProvider;
import com.hpifive.line.bcs.webhook.dao.EventAttendanceDao;
import com.hpifive.line.bcs.webhook.entities.config.EventApplyStatus;
import com.hpifive.line.bcs.webhook.executors.DefaultEventExecutor;
import com.hpifive.line.bcs.webhook.executors.EventAttendanceMessageExecutor;
import com.hpifive.line.bcs.webhook.executors.EventExecutor;

public class EventConfirmTimeoutExecutor extends DefaultEventExecutor {

	@Override
	public EventExecutor run() {
		EventAttendanceDao model = ApplicationContextProvider.getApplicationContext().getBean(EventAttendanceDao.class);
		model.setApplyStatusAndDateById(msg.getAttendanceId(), EventApplyStatus.REJECT, DateTimeModel.getTime());
		return EventAttendanceMessageExecutor.builder()
				.eventId(msg.getEventId())
				.attendanceId(msg.getAttendanceId())
				.applyStatus(msg.getApplyStatus())
				.type(msg.getKeywordEventTypes())
				.replyToken(msg.getReplyToken())
				.build();
	}

	public EventConfirmTimeoutExecutor(EventRegisterMsg msg) {
		super(msg);
	}

}

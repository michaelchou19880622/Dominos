package com.hpifive.line.bcs.webhook.executors.event;

import com.hpifive.line.bcs.webhook.akka.body.EventRegisterMsg;
import com.hpifive.line.bcs.webhook.config.ApplicationContextProvider;
import com.hpifive.line.bcs.webhook.dao.EventAttendanceAttributeDao;
import com.hpifive.line.bcs.webhook.dao.EventAttendanceDao;
import com.hpifive.line.bcs.webhook.entities.config.EventApplyStatus;
import com.hpifive.line.bcs.webhook.executors.DefaultEventExecutor;
import com.hpifive.line.bcs.webhook.executors.EventAttendanceMessageExecutor;
import com.hpifive.line.bcs.webhook.executors.EventExecutor;

public class EventApplyCompletedNoExecutor extends DefaultEventExecutor {

	@Override
	public EventExecutor run() {
		EventAttendanceDao attendanceDao = ApplicationContextProvider.getApplicationContext().getBean(EventAttendanceDao.class);
		EventAttendanceAttributeDao attributeDao = ApplicationContextProvider.getApplicationContext().getBean(EventAttendanceAttributeDao.class);
		attendanceDao.setApplyStatusAndDateById(msg.getAttendanceId(), EventApplyStatus.INIT, null);
		attributeDao.deleteByAttendId(msg.getAttendanceId());
		return EventAttendanceMessageExecutor.builder()
				.eventId(msg.getEventId())
				.attendanceId(msg.getAttendanceId())
				.applyStatus(0)
				.type(msg.getKeywordEventTypes())
				.replyToken(msg.getReplyToken())
				.build();
	}

	public EventApplyCompletedNoExecutor(EventRegisterMsg msg) {
		super(msg);
	}

}

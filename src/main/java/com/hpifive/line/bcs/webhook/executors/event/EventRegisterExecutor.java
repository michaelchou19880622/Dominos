package com.hpifive.line.bcs.webhook.executors.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hpifive.line.bcs.webhook.akka.body.EventRegisterMsg;
import com.hpifive.line.bcs.webhook.config.ApplicationContextProvider;
import com.hpifive.line.bcs.webhook.dao.EventAttendanceDao;
import com.hpifive.line.bcs.webhook.entities.config.KeywordEventTypes;
import com.hpifive.line.bcs.webhook.executors.DefaultEventExecutor;
import com.hpifive.line.bcs.webhook.executors.EventAttendanceMessageExecutor;
import com.hpifive.line.bcs.webhook.executors.EventExecutor;
import com.hpifive.line.bcs.webhook.executors.UnknownErrorExecutor;
import com.hpifive.line.bcs.webhook.service.EventRegisterHandlerModelService;


public class EventRegisterExecutor extends DefaultEventExecutor {

	private static final Logger logger = LoggerFactory.getLogger(EventRegisterExecutor.class);

	public EventRegisterExecutor(EventRegisterMsg msg) {
		super(msg);
	}

	@Override
	public EventExecutor run() {
		EventRegisterHandlerModelService model = ApplicationContextProvider.getApplicationContext().getBean(EventRegisterHandlerModelService.class);
		EventAttendanceDao attendanceDao = ApplicationContextProvider.getApplicationContext().getBean(EventAttendanceDao.class);
		try {
			msg = model.register(msg);
			if (KeywordEventTypes.REGISTER_FLOW_RUNNING == msg.getKeywordEventTypes()) {
				Integer next = msg.getApplyStatus()+1;
				attendanceDao.setErrorCountAndApplyStatusById(msg.getAttendanceId(), 0, next);
				return EventAttendanceMessageExecutor.builder()
						.eventId(msg.getEventId())
						.attendanceId(msg.getAttendanceId())
						.applyStatus(msg.getApplyStatus())
						.type(msg.getKeywordEventTypes())
						.replyToken(msg.getReplyToken())
						.build();
			}
		} catch (Exception e) {
			logger.error("EventRegisterExecutor Error {}", e);
			return UnknownErrorExecutor.builder().replyToken(msg.getReplyToken()).build();
		}
		return new EventMessageExecutor(msg);
	}

}

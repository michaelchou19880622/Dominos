package com.hpifive.line.bcs.webhook.executors.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hpifive.line.bcs.webhook.akka.body.EventRegisterMsg;
import com.hpifive.line.bcs.webhook.entities.config.KeywordEventTypes;
import com.hpifive.line.bcs.webhook.executors.DefaultEventExecutor;
import com.hpifive.line.bcs.webhook.executors.EventAttendanceMessageExecutor;
import com.hpifive.line.bcs.webhook.executors.EventExecutor;


public class EventMessageExecutor extends DefaultEventExecutor {

	private static final Logger logger = LoggerFactory.getLogger(EventMessageExecutor.class);

	public EventMessageExecutor(EventRegisterMsg msg) {
		super(msg);
	}

	@Override
	public EventExecutor run() {
		KeywordEventTypes type = msg.getKeywordEventTypes();
		logger.info("TYPE {}", type);
		switch (type) {
			case REGISTER_FLOW_RUNNING:
				return new EventRegisterExecutor(msg);
			case APPLY_AGREE:
				return new EventApplyAgreeExecutor(msg);
			case APPLY_COMPLETED:
				return new EventApplyCompletedExecutor(msg);
			case APPLY_COMPLETED_YES:
				return new EventApplyCompletedYesExecutor(msg);
			case APPLY_COMPLETED_NO:
				return new EventApplyCompletedNoExecutor(msg);
			case CONFIRM_CHECK: 
				return new EventConfirmCheckExecutor(msg);
			case CONFIRM_TIMEOUT:
			case REJECT_CHECK:
				return new EventConfirmTimeoutExecutor(msg);
			case CUSTOMIZE_INPUT_ERROR:
				return new EventInputErrorExecutor(msg);
			case ERROR_N_TIME:
			case TIME_OUT:
				return new EventErrorOverCountExecutor(msg);
			case APPLIED:
				return new EventAppliedExecutor(msg);
			default:
				return EventAttendanceMessageExecutor.builder()
						.eventId(msg.getEventId())
						.attendanceId(msg.getAttendanceId())
						.applyStatus(msg.getApplyStatus())
						.type(msg.getKeywordEventTypes())
						.replyToken(msg.getReplyToken())
						.build();
		}
	}

}

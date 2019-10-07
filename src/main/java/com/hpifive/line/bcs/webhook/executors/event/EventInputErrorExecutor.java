package com.hpifive.line.bcs.webhook.executors.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hpifive.line.bcs.webhook.akka.body.EventRegisterMsg;
import com.hpifive.line.bcs.webhook.config.ApplicationContextProvider;
import com.hpifive.line.bcs.webhook.dao.EventApplyDataDao;
import com.hpifive.line.bcs.webhook.dao.EventApplyDataKeywordDao;
import com.hpifive.line.bcs.webhook.dao.EventAttendanceDao;
import com.hpifive.line.bcs.webhook.entities.EventApplydataEntity;
import com.hpifive.line.bcs.webhook.entities.config.KeywordEventTypes;
import com.hpifive.line.bcs.webhook.executors.DefaultEventExecutor;
import com.hpifive.line.bcs.webhook.executors.EventAttendanceMessageExecutor;
import com.hpifive.line.bcs.webhook.executors.EventExecutor;

public class EventInputErrorExecutor extends DefaultEventExecutor {

	private static final Logger logger = LoggerFactory.getLogger(EventInputErrorExecutor.class);

	@Override
	public EventExecutor run() {
		EventAttendanceDao model = ApplicationContextProvider.getApplicationContext().getBean(EventAttendanceDao.class);
		model.incrementErrorCountById(msg.getAttendanceId());
		if (! this.isErrorKeywordExist(msg.getEventId(), msg.getApplyStatus()-1, msg.getKeywordEventTypes())) {
			msg.setKeywordEventTypes(KeywordEventTypes.USER_INPUT_ERROR);
		}
		return EventAttendanceMessageExecutor.builder()
				.eventId(msg.getEventId())
				.attendanceId(msg.getAttendanceId())
				.applyStatus(msg.getApplyStatus())
				.type(msg.getKeywordEventTypes())
				.replyToken(msg.getReplyToken())
				.build();
	}
	
	private boolean isErrorKeywordExist(Long eventId, Integer page, KeywordEventTypes type) {
		EventApplyDataDao applyDataDao = ApplicationContextProvider.getApplicationContext().getBean(EventApplyDataDao.class);
		EventApplyDataKeywordDao applyDataKeywordDao = ApplicationContextProvider.getApplicationContext().getBean(EventApplyDataKeywordDao.class);
		EventApplydataEntity applyDataEntity = applyDataDao.getByEventIdAndPage(eventId, page);
		if (applyDataEntity == null) {
			logger.warn("Can't find applyData by eventId {} and page {}", eventId, page);
			return false;
		}
		return applyDataKeywordDao.isExistByApplyDataIdAndKeywordEventAndApplyStatus(applyDataEntity.getId(), type.toString(), 0);
	}

	public EventInputErrorExecutor(EventRegisterMsg msg) {
		super(msg);
	}
	
}

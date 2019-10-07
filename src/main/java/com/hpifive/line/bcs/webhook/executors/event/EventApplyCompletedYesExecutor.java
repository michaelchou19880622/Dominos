package com.hpifive.line.bcs.webhook.executors.event;

import com.hpifive.line.bcs.webhook.akka.body.EventRegisterMsg;
import com.hpifive.line.bcs.webhook.common.DateTimeModel;
import com.hpifive.line.bcs.webhook.config.ApplicationContextProvider;
import com.hpifive.line.bcs.webhook.dao.EventAttendanceDao;
import com.hpifive.line.bcs.webhook.entities.config.EventApplyStatus;
import com.hpifive.line.bcs.webhook.executors.DefaultEventExecutor;
import com.hpifive.line.bcs.webhook.executors.EventExecutor;
import com.hpifive.line.bcs.webhook.executors.EventHubExecutor;

public class EventApplyCompletedYesExecutor extends DefaultEventExecutor {
	
	public EventApplyCompletedYesExecutor(EventRegisterMsg msg) {
		super(msg);
	}
	
	@Override
	public EventExecutor run() {
		EventAttendanceDao model = ApplicationContextProvider.getApplicationContext().getBean(EventAttendanceDao.class);
		model.setApplyStatusAndDateById(msg.getAttendanceId(), EventApplyStatus.APPLY_COMPLETED_YES, DateTimeModel.getTime());
		return EventHubExecutor.builder().msg(msg).build();
	}
	
}

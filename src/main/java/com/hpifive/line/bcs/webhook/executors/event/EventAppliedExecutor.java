package com.hpifive.line.bcs.webhook.executors.event;

import com.hpifive.line.bcs.webhook.akka.body.EventRegisterMsg;
import com.hpifive.line.bcs.webhook.executors.DefaultEventExecutor;
import com.hpifive.line.bcs.webhook.executors.EventExecutor;
import com.hpifive.line.bcs.webhook.executors.EventHubExecutor;

public class EventAppliedExecutor extends DefaultEventExecutor {
	
	public EventAppliedExecutor(EventRegisterMsg msg) {
		super(msg);
	}
	
	@Override
	public EventExecutor run() {
		msg.setApplyStatus(null);
		return EventHubExecutor.builder().msg(msg).build();
	}

}

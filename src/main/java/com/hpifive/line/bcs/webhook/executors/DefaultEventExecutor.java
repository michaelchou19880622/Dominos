package com.hpifive.line.bcs.webhook.executors;

import com.hpifive.line.bcs.webhook.akka.body.EventRegisterMsg;


public class DefaultEventExecutor implements EventExecutor{
	
		protected EventRegisterMsg msg;

		public DefaultEventExecutor() {
			super();
		}

		public DefaultEventExecutor(EventRegisterMsg msg) {
			super();
			this.msg = msg;
		}

		public EventRegisterMsg getMsg() {
			return msg;
		}

		public void setMsg(EventRegisterMsg msg) {
			this.msg = msg;
		}

		@Override
		public EventExecutor run() {
			// TODO Auto-generated method stub
			return null;
		}
		
		
}

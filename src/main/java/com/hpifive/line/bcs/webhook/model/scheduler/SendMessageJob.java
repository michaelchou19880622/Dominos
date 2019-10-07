package com.hpifive.line.bcs.webhook.model.scheduler;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class SendMessageJob extends QuartzJobBean {

	private static final Logger logger = LoggerFactory.getLogger(SendMessageJob.class);
	
	private Long sendId;
	private SendMsgTaskExecuter sendMsgTaskExecuter;
	
	public void setSendId(Long sendId) {
		this.sendId = sendId;
	}

	public void setSendMsgTaskExecuter(SendMsgTaskExecuter sendMsgTaskExecuter) {
		this.sendMsgTaskExecuter = sendMsgTaskExecuter;
	}

	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		try {
			this.sendMsgTaskExecuter.execute(this.sendId);
		} catch (Exception e) {
			logger.error("send Msg task execute internal", e);
		}
	}

}

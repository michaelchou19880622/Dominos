package com.hpifive.line.bcs.webhook.controller;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.quartz.JobDetail;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hpifive.line.bcs.webhook.entities.config.SystemConfigKeys;
import com.hpifive.line.bcs.webhook.model.scheduler.ChannelTokenUseabilityJob;
import com.hpifive.line.bcs.webhook.model.scheduler.invoice.InvoicePushJob;
import com.hpifive.line.bcs.webhook.model.scheduler.invoice.InvoiceRefreshJob;
import com.hpifive.line.bcs.webhook.service.ScheduleService;
import com.hpifive.line.bcs.webhook.service.SystemConfigService;

@Controller
@RequestMapping("/init")
public class InitController {

	@Autowired
	private SystemConfigService configService;
	@Autowired
	private ScheduleService scheduleService;
	
	/** Logger */
	private static final Logger logger = LoggerFactory.getLogger(InitController.class);

	public InitController(){
		logger.info("Constructor InitController");
	}
	
	@PostConstruct
	public void init(){
		try {
			logger.info("init loadScheduleFromDB");
			this.scheduleService.loadFromDatabase();
			this.schedulerChannelTokenJob();
			this.schedulerInvoiceRefreshJob();
			this.schedulerInvoiceMulticastJob();
		} catch (Exception e) {
			logger.error("InitController PostConstruct ",e);
		}
	}

	/**
	 * cleanUp
	 */
	@PreDestroy
	public void cleanUp() {
		logger.info("[DESTROY] InitController destroyed...");
	}
	
	private void schedulerInvoiceMulticastJob() {
		try {
			this.scheduleService.createCronJob(InvoicePushJob.class, "0 0 10 * * ? ");
		} catch (Exception e) {
			logger.info("發票推播排程失敗");
		}
	}
	
	private void schedulerInvoiceRefreshJob() {
		try {
			this.scheduleService.createCronJob(InvoiceRefreshJob.class, "0 0 8 * * ? ");
		} catch (Exception e) {
			logger.info("發票更新排程失敗");
		}
	}
	
	private void schedulerChannelTokenJob() {
		Integer second = 3600;
		Integer value = this.configService.getIntByKey(SystemConfigKeys.TOKENSECOND);
		if (value != null && value > 0) {
			second = value;
		}
		JobDetail jobDetail = this.scheduleService.getJobDetailByClass(ChannelTokenUseabilityJob.class);
		Trigger trigger = TriggerBuilder.newTrigger()
				.withIdentity("ChannelTokenUseabilityQuartz")
				.startNow()
				.withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(second))
				.build();
		this.scheduleService.schedulerJob(jobDetail, trigger);
	}

}


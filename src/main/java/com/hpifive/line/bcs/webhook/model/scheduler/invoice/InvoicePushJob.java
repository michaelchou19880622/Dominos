package com.hpifive.line.bcs.webhook.model.scheduler.invoice;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.hpifive.line.bcs.webhook.config.ApplicationContextProvider;
import com.hpifive.line.bcs.webhook.service.InvoiceService;

public class InvoicePushJob extends QuartzJobBean {
	
	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		InvoiceService supplier = ApplicationContextProvider.getApplicationContext().getBean(InvoiceService.class);
		supplier.sendToMatchAndNot();
	}

}

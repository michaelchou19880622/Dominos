package com.hpifive.line.bcs.webhook.model.scheduler;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.hpifive.line.bcs.webhook.config.ApplicationContextProvider;
import com.hpifive.line.bcs.webhook.config.CustomChannelTokenSupplier;

public class ChannelTokenUseabilityJob extends QuartzJobBean {
	
	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		CustomChannelTokenSupplier supplier = ApplicationContextProvider.getApplicationContext().getBean(CustomChannelTokenSupplier.class);
		supplier.refreshChannelTokenViaAPIIfExpiresSoon();
	}

}

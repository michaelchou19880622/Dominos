package com.hpifive.line.bcs.webhook.akka;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hpifive.line.bcs.webhook.common.DateTimeModel;
import com.hpifive.line.bcs.webhook.config.ApplicationContextProvider;
import com.hpifive.line.bcs.webhook.entities.config.SystemConfigKeys;
import com.hpifive.line.bcs.webhook.model.scheduler.AsyncSendBody;
import com.hpifive.line.bcs.webhook.model.scheduler.AsyncSendBodyFail;
import com.hpifive.line.bcs.webhook.model.scheduler.AsyncSendBodyPause;
import com.hpifive.line.bcs.webhook.model.scheduler.AsyncSender;
import com.hpifive.line.bcs.webhook.service.AsyncSendService;
import com.hpifive.line.bcs.webhook.service.SystemConfigService;
import com.linecorp.bot.model.message.Message;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;

public class AsyncSenderMaster extends AbstractActor {

	private static final Logger logger = LoggerFactory.getLogger(AsyncSenderMaster.class);
	
	private static Integer max;
	private static SystemConfigService configService;
	
	private static SystemConfigService getConfigService() {
		if (configService == null) {
			configService =  ApplicationContextProvider.getApplicationContext().getBean(SystemConfigService.class);
		}
		return configService;
	}
	
	private static Integer getMaxNumberOfCall() {
		if (max == null) {
			String result = getConfigService().getCacheOrSqlByKey(SystemConfigKeys.MAXNUMBEROFCALL);
			try {
				max =  Integer.valueOf(result);
			} catch (Exception e) {
				max = 7000;
			}
		}
		return max;
	}
	
	public void onSendBodyReceived(AsyncSendBody body) {
		Long sendId = body.getSendId();
		List<Message> messages = body.getMessages();
		ZonedDateTime pauseTime = body.getPauseTime();
		ZonedDateTime currentTime = DateTimeModel.getTime();
		Integer numberOfCall = this.getNumberOfCall();
		logger.info("目前次數 {}", numberOfCall);
		
		if (numberOfCall >= getMaxNumberOfCall() && currentTime.isBefore(pauseTime)) {
			logger.info("呼叫次數已超過上限，自動暫停推播直到一分鐘後");
			this.setNumberOfCall(0);
			getSender().tell(new AsyncSendBodyPause(sendId), ActorRef.noSender());
			return;
		} else if (numberOfCall >= getMaxNumberOfCall() && currentTime.isAfter(pauseTime) ) {
			this.setNumberOfCall(0);
			pauseTime = DateTimeModel.addMin(new Date(), 1);
		}
		try {
			AkkaService akkaService = ApplicationContextProvider.getApplicationContext().getBean(AkkaService.class);
			AsyncSendService sendService = ApplicationContextProvider.getApplicationContext().getBean(AsyncSendService.class);
			akkaService.mulitcastSender(new AsyncSendBody(sendId, messages, pauseTime));
			AsyncSender sender = sendService.send(body);
			if (sender != null) {
				getSender().tell(sender, getSelf());
			}
		} catch (Exception e) {
			AsyncSendBody tempBody = new AsyncSendBody(sendId, messages, pauseTime);
			tempBody.setCount(body.getCount()+1);
			if (tempBody.getCount() < 5) {
				getSender().tell(tempBody, ActorRef.noSender());
				return;
			}
			getSender().tell(new AsyncSendBodyFail(sendId), ActorRef.noSender());
		}
	}
	
	private void setNumberOfCall(Integer count) {
		getConfigService().setCacheByKey(SystemConfigKeys.NUMBEROFCALL, String.valueOf(count));
	}
	
	private Integer getNumberOfCall() {
		String value = getConfigService().getCacheByKey(SystemConfigKeys.NUMBEROFCALL);
		try {
			if (value == null) {
				return 0;
			}
			return Integer.valueOf(value);
		} catch (Exception e) {
			return 0;
		}
	}
	
	@Override
	public Receive createReceive() {
		return receiveBuilder()
				.match(AsyncSendBody.class, this::onSendBodyReceived)
				.build();
	}

}

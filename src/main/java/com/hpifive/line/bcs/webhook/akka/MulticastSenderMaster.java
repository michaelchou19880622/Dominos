package com.hpifive.line.bcs.webhook.akka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hpifive.line.bcs.webhook.config.ApplicationContextProvider;
import com.hpifive.line.bcs.webhook.dao.SendMsgDao;
import com.hpifive.line.bcs.webhook.entities.SendMessageEntity;
import com.hpifive.line.bcs.webhook.entities.config.SendMessageMode;
import com.hpifive.line.bcs.webhook.entities.config.SendMessageStatus;
import com.hpifive.line.bcs.webhook.model.scheduler.AsyncSendBody;
import com.hpifive.line.bcs.webhook.model.scheduler.AsyncSendBodyFail;
import com.hpifive.line.bcs.webhook.model.scheduler.AsyncSendBodyPause;
import com.hpifive.line.bcs.webhook.model.scheduler.AsyncSendBodySuccess;
import com.hpifive.line.bcs.webhook.service.ScheduleService;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;

public class MulticastSenderMaster extends AbstractActor {
	
	private static final Logger logger = LoggerFactory.getLogger(MulticastSenderMaster.class);
	
	private static ScheduleService scheduleService;
	private final ActorRef routerSendActor;
	
	public MulticastSenderMaster() {
		routerSendActor = new AkkaRouterFactory<AsyncSenderMaster>(getContext(), AsyncSenderMaster.class, true).getRouterActor();
	}
	
	private static ScheduleService getScheduleService() {
		if (scheduleService == null) {
			scheduleService = ApplicationContextProvider.getApplicationContext().getBean(ScheduleService.class);
		}
		return scheduleService;
	}
	
	public void onSendBodyReceived(AsyncSendBody body) {
		SendMsgDao sendMsgDao = ApplicationContextProvider.getApplicationContext().getBean(SendMsgDao.class);
		SendMessageEntity entity = sendMsgDao.getSendMessageById(body.getSendId(), SendMessageStatus.CREATE);
		if (entity != null) {
			routerSendActor.tell(body, getSelf());
			return;
		}
		logger.info("沒有訊息拉 沒有訊息拉");
	}
	
	public void onSendSuccess(AsyncSendBodySuccess body) {
		this.setSendMsgStatusById(body.getSendId(), SendMessageStatus.SUCCESS);
	}
	
	public void onSendFailure(AsyncSendBodyFail body) {
		this.setSendMsgStatusById(body.getSendId(), SendMessageStatus.FAILURE);
	}
	
	public void onPause(AsyncSendBodyPause body) {
		try {
			getScheduleService().addJobBySendId(body.getSendId());
		} catch (Exception e) {
			logger.error("Add job error", e);
		}
	}
	
	private void setSendMsgStatusById(Long id, SendMessageStatus status) {
		SendMsgDao sendMsgDao = ApplicationContextProvider.getApplicationContext().getBean(SendMsgDao.class);
		try {
			SendMessageEntity entity = sendMsgDao.getSendMessageById(id);
			if (entity == null) {
				logger.error("SendMessage Entity null with id {}", id);
				return;
			}
			if (SendMessageMode.ONCE.toString().equals(entity.getMode()) ||
					SendMessageMode.NOW.toString().equals(entity.getMode())){
	//		rewrite status cause is one-time job
				if (status.getValue() == entity.getStatus()) {
					return;
				}
				sendMsgDao.rewriteSendMessageStatusById(id, status);
				ScheduleService scheduleService = ApplicationContextProvider.getApplicationContext().getBean(ScheduleService.class);
				scheduleService.deletePushJobBy(id);
			}
		} catch (Exception e) {
			String ex = String.format("id: %s Catch set SendMessage status error %s", id.toString(), e.getMessage());
			logger.error(ex);
		}
	}
	
	@Override
	public Receive createReceive() {
		return receiveBuilder()
				.match(AsyncSendBody.class, this::onSendBodyReceived)
				.match(AsyncSendBodySuccess.class, this::onSendSuccess)
				.match(AsyncSendBodyFail.class, this::onSendFailure)
				.match(AsyncSendBodyPause.class, this::onPause)
				.matchAny(any -> logger.warn("handle uncheck type {}", any))
				.build();
	}

}

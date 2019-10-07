package com.hpifive.line.bcs.webhook.model.scheduler;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hpifive.line.bcs.webhook.akka.AkkaService;
import com.hpifive.line.bcs.webhook.common.DateTimeModel;
import com.hpifive.line.bcs.webhook.config.ApplicationContextProvider;
import com.hpifive.line.bcs.webhook.dao.SendMsgDao;
import com.linecorp.bot.model.message.Message;

public class SendMsgTaskExecuter {
	private static final Logger logger = LoggerFactory.getLogger(SendMsgTaskExecuter.class);
	
	private AkkaService akkaService = ApplicationContextProvider.getApplicationContext().getBean(AkkaService.class);
	private SendMsgDao sendMsgDao = ApplicationContextProvider.getApplicationContext().getBean(SendMsgDao.class);
	
	public void execute(Long sendId) {
		try {
			logger.info("send id: {}", sendId);
			List<Message> sendMsgs = sendMsgDao.getMsgBySendId(sendId);
			AsyncSendBody body = new AsyncSendBody(sendId, sendMsgs, DateTimeModel.addMin(new Date(), 1));
			this.akkaService.mulitcastSender(body);
		} catch (Exception e) {
			logger.error("send msg task execute", e);
		}
	}
	
}

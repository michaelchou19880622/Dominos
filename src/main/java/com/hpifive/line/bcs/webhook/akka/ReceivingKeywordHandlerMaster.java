package com.hpifive.line.bcs.webhook.akka;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hpifive.line.bcs.webhook.akka.body.ReceivingTextMsg;
import com.hpifive.line.bcs.webhook.common.DateTimeModel;
import com.hpifive.line.bcs.webhook.config.ApplicationContextProvider;
import com.hpifive.line.bcs.webhook.executors.EventExecutor;
import com.hpifive.line.bcs.webhook.executors.ReplyMessageExecutor;
import com.hpifive.line.bcs.webhook.service.ReceivingMsgHandlerMasterModelService;
import com.linecorp.bot.model.message.Message;

import akka.actor.AbstractActor;

public class ReceivingKeywordHandlerMaster extends AbstractActor{
	
	private static final Logger logger = LoggerFactory.getLogger(ReceivingKeywordHandlerMaster.class);
	private static ReceivingMsgHandlerMasterModelService model;
	
	public static ReceivingMsgHandlerMasterModelService getModel() {
		if (model == null) {
			model = ApplicationContextProvider.getApplicationContext().getBean(ReceivingMsgHandlerMasterModelService.class);
		}
		return model;
	}
	
	public void onTextMsgReceived(ReceivingTextMsg msg) {
		logger.info("到進入Keyword階段費時: {} 秒", (DateTimeModel.getTime().toEpochSecond()-msg.getCreateTime().toEpochSecond()));
		List<Message> messages = getModel().onTextMsgReceived(msg.getUid(), msg.getText());
		EventExecutor executor = ReplyMessageExecutor.builder()
				.replyToken(msg.getReplyToken()).messages(messages).build();
		executor.run();
	}
	
	@Override
	public Receive createReceive() {
		return receiveBuilder()
						.match(ReceivingTextMsg.class, this::onTextMsgReceived)
						.matchAny( a -> logger.warn("handler unchecked type"))
						.build();
	}

}

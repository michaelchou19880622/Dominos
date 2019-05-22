package com.hpifive.line.bcs.webhook.executors;

import java.util.List;

import com.hpifive.line.bcs.webhook.akka.body.ReceivingTextMsg;
import com.hpifive.line.bcs.webhook.config.ApplicationContextProvider;
import com.hpifive.line.bcs.webhook.service.ReceivingMsgHandlerMasterModelService;
import com.linecorp.bot.model.message.Message;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class KeywordMessageExecutor implements EventExecutor {

	private ReceivingTextMsg msg;
	
	@Override
	public EventExecutor run() {
		ReceivingMsgHandlerMasterModelService model = ApplicationContextProvider.getApplicationContext().getBean(ReceivingMsgHandlerMasterModelService.class);
		List<Message> messages = model.onTextMsgReceived(msg.getUid(), msg.getText());
		return ReplyMessageExecutor.builder()
				.replyToken(msg.getReplyToken())
				.messages(messages)
				.build();
	}

}

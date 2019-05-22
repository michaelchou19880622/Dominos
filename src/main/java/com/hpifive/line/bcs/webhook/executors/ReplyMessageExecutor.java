package com.hpifive.line.bcs.webhook.executors;

import java.util.List;

import com.hpifive.line.bcs.webhook.akka.AkkaService;
import com.hpifive.line.bcs.webhook.config.ApplicationContextProvider;
import com.hpifive.line.bcs.webhook.entities.config.AutoreplyDefaultKeywords;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.message.Message;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ReplyMessageExecutor implements EventExecutor {

	private String replyToken;
	private List<Message> messages;
	
	@Override
	public EventExecutor run() {
		AkkaService akkaService = ApplicationContextProvider.getApplicationContext().getBean(AkkaService.class);
		if (replyToken != null && replyToken != "" && messages != null && ! messages.isEmpty()) {
			akkaService.reply(new ReplyMessage(replyToken, messages));
			return null;
		}
		return DefaultReplyMessageExecutor.builder().replyToken(replyToken).type(AutoreplyDefaultKeywords.DEFAULT).build();
	}
}

package com.hpifive.line.bcs.webhook.executors;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hpifive.line.bcs.webhook.config.ApplicationContextProvider;
import com.hpifive.line.bcs.webhook.entities.config.AutoreplyDefaultKeywords;
import com.hpifive.line.bcs.webhook.entities.config.AutoreplyType;
import com.hpifive.line.bcs.webhook.service.AutoreplyService;
import com.linecorp.bot.model.message.Message;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class DefaultReplyMessageExecutor implements EventExecutor {

	private static final Logger logger = LoggerFactory.getLogger(DefaultReplyMessageExecutor.class);

	private String replyToken;
	private AutoreplyDefaultKeywords type;
	
	@Override
	public EventExecutor run() {
		AutoreplyService service = ApplicationContextProvider.getApplicationContext().getBean(AutoreplyService.class);
		List<Message> messages = service.getByKeywordAndType(type, AutoreplyType.DEFAULT);
		if (messages == null || messages.isEmpty()) {
			logger.warn("Default Keyword Messages not found");
			return null;
		}
		return ReplyMessageExecutor.builder().replyToken(replyToken).messages(messages).build();
	}
}

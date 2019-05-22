package com.hpifive.line.bcs.webhook.executors;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hpifive.line.bcs.webhook.config.ApplicationContextProvider;
import com.hpifive.line.bcs.webhook.entities.config.KeywordEventTypes;
import com.hpifive.line.bcs.webhook.service.EventKeywordMessageService;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class EventKeywordMessageExecutor implements EventExecutor {

	private static final Logger logger = LoggerFactory.getLogger(EventKeywordMessageExecutor.class);

	private Long eventId;
	private Long userId;
	private String replyToken;
	private KeywordEventTypes type;
	
	@Override
	public EventExecutor run() {
		List<Message> messages;
		try {
			EventKeywordMessageService model = ApplicationContextProvider.getApplicationContext().getBean(EventKeywordMessageService.class);
			messages = model.getMessageBy(this.eventId, this.userId, this.type);
		} catch (Exception e) {
			messages =  Collections.singletonList(new TextMessage("未知錯誤發生, 請稍後再試"));
			logger.error("payload: eventId {} userId {} type {}", eventId, userId, type);
		}
		return ReplyMessageExecutor.builder().replyToken(replyToken).messages(messages).build();
	}

}

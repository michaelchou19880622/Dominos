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
public class EventAttendanceMessageExecutor implements EventExecutor {

	private static final Logger logger = LoggerFactory.getLogger(EventAttendanceMessageExecutor.class);

	private Long eventId;
	private Long attendanceId;
	private Integer applyStatus;
	private String replyToken;
	private KeywordEventTypes type;
	
	@Override
	public EventExecutor run() {
		List<Message> messages = null;
		try {
			EventKeywordMessageService model = ApplicationContextProvider.getApplicationContext().getBean(EventKeywordMessageService.class);
			if (applyStatus != null) {
				messages = model.getMessagesBy(eventId, attendanceId, applyStatus, type);
			}
			if (messages == null || messages.isEmpty()) {
				messages = model.getMessagesBy(eventId, attendanceId, type);
			}
		} catch (Exception e) {
			messages =  Collections.singletonList(new TextMessage("未知錯誤發生, 請稍後再試"));
			logger.error("payload: eventId {} attendanceId {} type {}", eventId, attendanceId, type);
		}
		return ReplyMessageExecutor.builder().replyToken(replyToken).messages(messages).build();
	}

}

package com.hpifive.line.bcs.webhook.executors;

import java.util.ArrayList;
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
public class CheckOrderSatusMessageExecutor implements EventExecutor {

	private static final Logger logger = LoggerFactory.getLogger(CheckOrderSatusMessageExecutor.class);

	private String replyToken;
	private String orderContent;
	
	@Override
	public EventExecutor run() {
		List<Message> messages = new ArrayList<Message>();
		
		try {
			messages.add(new TextMessage(orderContent));
		} catch (Exception e) {
			messages = Collections.singletonList(new TextMessage("未知錯誤發生, 請稍後再試"));
			logger.error("Exception : replyToken = {}, orderContent = {}", replyToken, orderContent);
		}
		
		return ReplyMessageExecutor.builder().replyToken(replyToken).messages(messages).build();
	}
}

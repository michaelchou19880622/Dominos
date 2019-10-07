package com.hpifive.line.bcs.webhook.executors;

import java.util.Collections;

import com.linecorp.bot.model.message.TextMessage;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class UnknownErrorExecutor implements EventExecutor {

	private String replyToken;
	
	@Override
	public EventExecutor run() {
		return ReplyMessageExecutor.builder()
				.replyToken(replyToken)
				.messages(Collections.singletonList(new TextMessage("未知錯誤發生")))
				.build();
	}

}

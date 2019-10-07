package com.hpifive.line.bcs.webhook.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class LineBotProperties {
	
	@Value("${line.bot.web.endpoint}")
	private String endpoint;
	
	@Value("${line.bot.tokenkey}")
	private String channelTokenKey;
	
	@Value("${line.bot.channelId}")
    private String channelId;

    @Value("${line.bot.channelSecret}")
    private String channelSecret;
	
}

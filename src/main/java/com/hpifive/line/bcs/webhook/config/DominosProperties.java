package com.hpifive.line.bcs.webhook.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class DominosProperties {
	
	@Value("${dominos.orderlist.url}")
	private String orderlist_url;
	
}

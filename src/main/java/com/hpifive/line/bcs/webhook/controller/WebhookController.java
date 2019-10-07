package com.hpifive.line.bcs.webhook.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hpifive.line.bcs.webhook.akka.AkkaService;
import com.hpifive.line.bcs.webhook.akka.body.ReceivingImageMsg;
import com.hpifive.line.bcs.webhook.akka.body.ReceivingTextMsg;
import com.hpifive.line.bcs.webhook.common.DateTimeModel;
import com.linecorp.bot.model.event.BeaconEvent;
import com.linecorp.bot.model.event.FollowEvent;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.PostbackEvent;
import com.linecorp.bot.model.event.UnfollowEvent;
import com.linecorp.bot.model.event.message.ImageMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;


@LineMessageHandler
@RestController
public class WebhookController {
	
	private static final Logger logger = LoggerFactory.getLogger(WebhookController.class);
	
	@Autowired
	private AkkaService akkaService;
	
	@RequestMapping(path="/callback", method=RequestMethod.GET)
	public String index(HttpServletRequest request, HttpServletResponse response) {
		return "Request method 'GET' not supported";
	}
	
	@EventMapping
	public void handlerBeaconMessageEvent(BeaconEvent event) {
		try {
			TextMessageContent textMessageContent = new TextMessageContent("beacon", "Beacon");
			MessageEvent<TextMessageContent> messageEvent = new MessageEvent<>(
							event.getReplyToken(), event.getSource(), textMessageContent, event.getTimestamp());
			handlerTextMessageEvent(messageEvent);
		} catch (Exception e) {
			logger.error("Handler Beacon Message Error", e);
		}
	}
	
	@EventMapping
	public void handlerFollowMessageEvent(FollowEvent event) {
		logger.info("『 FollowEvent 』=> {}", event);
		this.akkaService.receiveMessage(event);
	}
	
	@EventMapping
	public void handlerUnFollowMessageEvent(UnfollowEvent event) {
		logger.info("『 UnFollowEvent 』=> {}", event);
		this.akkaService.receiveMessage(event);
	}
	
	@EventMapping
	public void handlerPostbackMessageEvent(PostbackEvent event) {
		logger.info("handler PostbackEvent");
		TextMessageContent textMessageContent = new TextMessageContent("postback", event.getPostbackContent().getData());
		MessageEvent<TextMessageContent> messageEvent = new MessageEvent<>(
						event.getReplyToken(), event.getSource(), textMessageContent, event.getTimestamp());
		handlerTextMessageEvent(messageEvent);
	}
	
	@EventMapping
	public void handlerTextMessageEvent(MessageEvent<TextMessageContent> event) {
		ReceivingTextMsg msg = new ReceivingTextMsg(event.getReplyToken(), event.getMessage().getText(), event.getSource().getUserId());
		msg.setCreateTime(DateTimeModel.getTime());
		msg.setId(event.getMessage().getId());
		this.akkaService.receiveMessage(msg);													
	}
	
	@EventMapping
	public void handlerImageMessageEvent(MessageEvent<ImageMessageContent> event) {
		ReceivingImageMsg imgMsg = new ReceivingImageMsg(event.getSource().getUserId(), event.getReplyToken(), event.getMessage().getId(), DateTimeModel.getTime());
		this.akkaService.receiveMessage(imgMsg);
	}
	
}

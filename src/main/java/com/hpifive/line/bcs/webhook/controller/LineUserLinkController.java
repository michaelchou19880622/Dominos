package com.hpifive.line.bcs.webhook.controller;

import java.util.EnumMap;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hpifive.line.bcs.webhook.akka.AkkaService;
import com.hpifive.line.bcs.webhook.common.CryptGenerator;
import com.hpifive.line.bcs.webhook.config.LineBotProperties;
import com.hpifive.line.bcs.webhook.entities.config.AutoreplyDefaultKeywords;
import com.hpifive.line.bcs.webhook.entities.config.AutoreplyType;
import com.hpifive.line.bcs.webhook.entities.config.LineUserTrackSource;
import com.hpifive.line.bcs.webhook.service.AutoreplyService;
import com.hpifive.line.bcs.webhook.service.UserTrackingService;
import com.linecorp.bot.model.message.Message;

@RestController
@RequestMapping(path="/bind")
public class LineUserLinkController {
	
	private static final Logger logger = LoggerFactory.getLogger(LineUserLinkController.class);

	@Autowired
	private AkkaService akkaService;
	@Autowired
	private AutoreplyService autoreplyService;
	@Autowired
	private UserTrackingService trackingService;
	@Autowired
	private LineBotProperties property;
	
	private AutoreplyDefaultKeywords getKeyword(String method) {
		EnumMap<RequestMethod, AutoreplyDefaultKeywords> methods = new EnumMap<>(RequestMethod.class);
		methods.put(RequestMethod.POST, AutoreplyDefaultKeywords.BINDED);
		methods.put(RequestMethod.DELETE, AutoreplyDefaultKeywords.UNBINDED);
		for(Entry<RequestMethod, AutoreplyDefaultKeywords> map : methods.entrySet()) {
			if (map.getKey().toString().equals(method)) {
				return map.getValue();
			}
		}
		return null;
	}
	
	private void messageSendConsumer(String uuid, AutoreplyDefaultKeywords defaultKeyword) {
		if (uuid == null || defaultKeyword == null) {
			return;
		}
		try {
			LineUserTrackSource trackSource = LineUserTrackSource.fromString(defaultKeyword.toString());
			List<Message> messages = this.autoreplyService.getByKeywordAndType(defaultKeyword.toString(), AutoreplyType.DEFAULT.toString());
			this.akkaService.push(uuid, messages);
			this.trackingService.trackByUidAndStatus(uuid, trackSource);
		} catch (Exception e) {
			logger.error("LineUserLinkController messageSendConsumer", e);
		}
	}
	
	@RequestMapping(path="/{user}", method= {RequestMethod.POST, RequestMethod.DELETE})
	public ResponseEntity<Object> bindUser(@PathVariable("user") String uuid, HttpServletRequest request) {
		AutoreplyDefaultKeywords keyword = this.getKeyword(request.getMethod());
		String auth = request.getHeader("Authorization");
		String authKey = CryptGenerator.sha256(property.getChannelSecret(), uuid);
		if (auth != null && authKey != null && authKey.equals(auth)) {
			this.messageSendConsumer(uuid, keyword);
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.badRequest().build();
	}

}

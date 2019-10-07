package com.hpifive.line.bcs.webhook.akka;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import com.hpifive.line.bcs.webhook.akka.body.CustomPushBody;
import com.hpifive.line.bcs.webhook.akka.body.EventKeywordPushBody;
import com.hpifive.line.bcs.webhook.config.ApplicationContextProvider;
import com.hpifive.line.bcs.webhook.config.LineURL;
import com.hpifive.line.bcs.webhook.dao.SendMsgDao;
import com.hpifive.line.bcs.webhook.entities.config.SendMessageStatus;
import com.hpifive.line.bcs.webhook.model.scheduler.AsyncSendingModel;
import com.hpifive.line.bcs.webhook.service.EventKeywordMessageService;
import com.hpifive.line.bcs.webhook.service.LineHttpService;
import com.linecorp.bot.model.Multicast;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.message.Message;

import akka.actor.AbstractActor;
import akka.actor.Props;

public class ReplyMsgHandlerMaster extends AbstractActor {
	
	private static final Logger logger = LoggerFactory.getLogger(ReplyMsgHandlerMaster.class);
	private static String enconding = "utf-8";
	
	public static Props props() {
		return Props.create(ReplyMsgHandlerMaster.class);
	}
	
	private void send(String url, Object body) {
		LineHttpService httpService = ApplicationContextProvider.getApplicationContext().getBean(LineHttpService.class);
		CompletableFuture<HttpResponse> cf = httpService.replyMsgToLine(url, body);
		cf.whenComplete( (response, exec)-> {
			if (exec != null) {
				logger.error("Error: send to"+url, exec);
				return;
			}
			InputStream inputStream = null;
			StringWriter writer = new StringWriter();
				try {
					inputStream = response.getEntity().getContent();
					IOUtils.copy(inputStream, writer, enconding);
				} catch (Exception e) {
					logger.warn("Unable to get response body");
				}
				Integer code = response.getStatusLine().getStatusCode();
				String theString = writer == null ? "" : writer.toString();
				logger.info("Response Code {}", code);
		        logger.info("ResponseBody {}", theString);
		});
	}
	
	private void push(Object message) {
		this.send(LineURL.PUSH.toString(), message);
	}
	
	private void onPushMsgReceivedHandler(PushMessage message) {
		this.push(message);
	}
	
	private void onEventKeywordPushReceivedHandler(EventKeywordPushBody body) {
		EventKeywordMessageService msgDao = ApplicationContextProvider.getApplicationContext().getBean(EventKeywordMessageService.class);
		try {
			List<Message> messages;
			Map<String, Object> map = body.getUserMap();
			if (map == null) {
				return;
			}
			for ( Entry<String, Object> entry : body.getUserMap().entrySet()) {
				messages = msgDao.getMessageBy(body.getEventId(), Long.parseLong(entry.getKey()), body.getKeywordType());
				this.onPushMsgReceivedHandler(new PushMessage(entry.getValue().toString(), messages));
			}
			
		} catch (Exception e) {
			logger.error("Fail to Send msg with EventKeywordPushBody {}", body, e);
		}
		
	}
	
	private void onPushBodyReceivedHandler(CustomPushBody message) {
		Set<String> ids = message.getTo();
		List<Message> messages = message.getMessages();
		for (String uid : ids) {
			this.push(new PushMessage(uid, messages));
		}
		
	}
	
	
	private void onMulticastMsgReceivedHandler(AsyncSendingModel model) {
		List<Long> ids = model.getSendUserIds();
		Set<String> to = new HashSet<>(model.getUids());
		Multicast multicast = new Multicast(to, model.getMessages());
		LineHttpService httpService = ApplicationContextProvider.getApplicationContext().getBean(LineHttpService.class);
		CompletableFuture<HttpResponse> cf = httpService.replyMsgToLine(LineURL.MULTICAST.toString(), multicast);
		SendMsgDao sendMsgDao = ApplicationContextProvider.getApplicationContext().getBean(SendMsgDao.class);
		cf.whenComplete( (response, exec)-> {
			StringWriter writer = new StringWriter();
			Integer code = response.getStatusLine().getStatusCode();
			if (code != HttpStatus.OK.value() || exec != null) {
				sendMsgDao.setSendUsersStatusAndResponseCodeByIds(ids, SendMessageStatus.FAILURE, code);
			}
			try (InputStream inputStream = response.getEntity().getContent()) {
				IOUtils.copy(inputStream, writer, enconding);
			} catch (Exception e) {
				logger.warn(e.getMessage());
			} finally {
				String theString = writer == null ? "" : writer.toString();
				logger.info("MulticastMsgToLINE Code {}",code);
		        logger.info("MulticastMsgToLINE Body {}", theString);
			}
		});
	}
	
	private void onMulticastMsgReceivedHandler(Multicast multicast) {
		this.send(LineURL.MULTICAST.toString(), multicast);
	}
	
	private void onReplyMsgReceivedHandler(ReplyMessage msg) {
		this.send(LineURL.REPLY.toString(), msg);
	}
	
	@Override
	public Receive createReceive() {
		return receiveBuilder()
				.match(ReplyMessage.class, this::onReplyMsgReceivedHandler)
				.match(Multicast.class, this::onMulticastMsgReceivedHandler)
				.match(AsyncSendingModel.class, this::onMulticastMsgReceivedHandler)
				.match(CustomPushBody.class, this::onPushBodyReceivedHandler)
				.match(PushMessage.class, this::onPushMsgReceivedHandler)
				.match(EventKeywordPushBody.class, this::onEventKeywordPushReceivedHandler)
				.matchAny(any -> logger.info("handler unchecked type {}", any))
				.build();
	}

}

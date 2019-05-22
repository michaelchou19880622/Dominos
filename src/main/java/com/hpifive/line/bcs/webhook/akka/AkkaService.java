package com.hpifive.line.bcs.webhook.akka;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.hpifive.line.bcs.webhook.akka.body.CustomPushBody;
import com.hpifive.line.bcs.webhook.model.scheduler.AsyncSender;
import com.hpifive.line.bcs.webhook.model.scheduler.AsyncSendingModel;
import com.linecorp.bot.model.Multicast;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.message.Message;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

@Component
@Service
public class AkkaService {
	
	private static final Logger logger = LoggerFactory.getLogger(AkkaService.class);
	
	private List<ActorSystem> systemReceiving = new ArrayList<>();
	private List<ActorRef> receivingMasters = new ArrayList<>();
	private List<ActorSystem> systemReply = new ArrayList<>();
	private List<ActorRef> replyMasters = new ArrayList<>();
	private List<ActorSystem> systemMulitcast = new ArrayList<>();
	private List<ActorRef> mulitcastMasters = new ArrayList<>();
	
	private AkkaService() {
		logger.info("[Initialize] Akka Service");
		new AkkaSystemFactory<ReceivingMsgHandlerMaster>(systemReceiving, receivingMasters, ReceivingMsgHandlerMaster.class, this.getClass().getSimpleName(), ReceivingMsgHandlerMaster.class.getSimpleName());
		new AkkaSystemFactory<ReplyMsgHandlerMaster>(systemReply, replyMasters, ReplyMsgHandlerMaster.class, "systemReply", "ReplyMsgHandlerMaster");
		new AkkaSystemFactory<MulticastSenderMaster>(systemMulitcast, mulitcastMasters, MulticastSenderMaster.class, "systemMulitcast", "mulitcastMasters");
	}
	
	@PreDestroy
	public void shutdownNow(){
		logger.info("AkkaService Shutdown Now !");
		closeSystem(systemReceiving, receivingMasters);
		closeSystem(systemReply, replyMasters);
		closeSystem(systemMulitcast, mulitcastMasters);
	}
	
	private void closeSystem(List<ActorSystem> systems, List<ActorRef> masters) {
		try {
			for(Integer i=0; i<systems.size();i++) {
				ActorSystem system = systems.get(i);
				ActorRef master = masters.get(i);
				system.stop(master);
				system.terminate();
			}
		} catch (Exception e) {
			logger.error("close system error", e);
		}
	}
	
	private ActorRef randomMaster(List<ActorRef> masters){
		logger.debug("randomMaster Size: {}", masters.size());

        int index = new Random().nextInt(masters.size());
        return masters.get(index);
	}
	
	public void receiveMessage(Object event) {
		ActorRef taskRouter = this.randomMaster(receivingMasters);
		taskRouter.tell(event, ActorRef.noSender());
	}
	
	public void reply(Object arg0) {
		ActorRef taskRouter = this.randomMaster(replyMasters);
		taskRouter.tell(arg0, ActorRef.noSender());
	}
	
	public void push(String uuid, List<Message> messages) {
		try {
			if (messages == null || messages.isEmpty()) {
				return;
			}
			PushMessage message = new PushMessage(uuid, messages);
			this.reply(message);
		} catch (Exception e) {
			logger.error("push master: ", e);
		}
	}
	
	public void push(List<String> uuids, List<Message> message) {
		try {
			CustomPushBody pushBody = new CustomPushBody(uuids, message);
			ActorRef taskRouter = this.randomMaster(replyMasters);
			taskRouter.tell(pushBody, ActorRef.noSender());
		} catch (Exception e) {
			logger.error("push master: ", e);
		}
	}
	
	public void multicast(AsyncSendingModel model) {
		this.reply(model);
	}
	
	public void multicast(List<String> uuids, List<Message> messages) {
		try {
			Set<String> to = new HashSet<>(uuids);
			Multicast multicast = new Multicast(to, messages);
			this.reply(multicast);
		} catch (Exception e) {
			logger.error("multicast master: {}", e.getMessage());
		}
	}
	
	public void mulitcastSender(AsyncSender body) {
		ActorRef taskRouter = this.randomMaster(mulitcastMasters);
		taskRouter.tell(body, ActorRef.noSender());
	}
	
}

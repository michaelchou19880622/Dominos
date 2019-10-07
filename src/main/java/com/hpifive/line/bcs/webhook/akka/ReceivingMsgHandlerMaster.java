package com.hpifive.line.bcs.webhook.akka;

import java.awt.image.BufferedImage;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hpifive.line.bcs.webhook.akka.body.InvoiceImageMsg;
import com.hpifive.line.bcs.webhook.akka.body.InvoiceMsg;
import com.hpifive.line.bcs.webhook.akka.body.ReceivingImageMsg;
import com.hpifive.line.bcs.webhook.akka.body.ReceivingTextMsg;
import com.hpifive.line.bcs.webhook.common.DateTimeModel;
import com.hpifive.line.bcs.webhook.config.ApplicationContextProvider;
import com.hpifive.line.bcs.webhook.entities.InvoiceEntity;
import com.hpifive.line.bcs.webhook.entities.config.KeywordEventTypes;
import com.hpifive.line.bcs.webhook.executors.EventExecutor;
import com.hpifive.line.bcs.webhook.executors.ReceivingTextExecutor;
import com.hpifive.line.bcs.webhook.executors.invoice.InvoiceMessageExecutor;
import com.hpifive.line.bcs.webhook.invoice.InvoiceDecodeService;
import com.hpifive.line.bcs.webhook.service.EventReceivingMsgHandlerMasterModelService;
import com.hpifive.line.bcs.webhook.service.InvoiceEventService;
import com.hpifive.line.bcs.webhook.service.LineHttpService;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.FollowEvent;
import com.linecorp.bot.model.event.UnfollowEvent;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

public class ReceivingMsgHandlerMaster extends AbstractActor{
	
	private static final Logger logger = LoggerFactory.getLogger(ReceivingMsgHandlerMaster.class);
	
	private final ActorRef routerFriendActor;
	
	private static EventReceivingMsgHandlerMasterModelService model;
	private static InvoiceEventService invoiceModel;
	
	public ReceivingMsgHandlerMaster(){
		routerFriendActor = new AkkaRouterFactory<FriendEventHandlerMaster>(getContext(), FriendEventHandlerMaster.class, true).getRouterActor();
	}
	
	public static EventReceivingMsgHandlerMasterModelService getModel() {
		if (model == null) {
			model = ApplicationContextProvider.getApplicationContext().getBean(EventReceivingMsgHandlerMasterModelService.class);
		}
		return model;
	}
	
	public static InvoiceEventService getInvoiceModel() {
		if (invoiceModel == null) {
			invoiceModel = ApplicationContextProvider.getApplicationContext().getBean(InvoiceEventService.class);
		}
		return invoiceModel;
	}
	
	/**
	 * Akka Props Constructor
	 * @return Props
	 */
	public static Props props() {
		return Props.create(ReceivingMsgHandlerMaster.class);
	}
	private void onInvoiceMsgReceived(InvoiceMsg msg) {
		EventExecutor executor = InvoiceMessageExecutor.builder().msg(msg).build();
		this.fsm(executor);
	}
	
	private void fsm(EventExecutor exec) {
		EventExecutor executor = exec;
		while(executor != null) {
			executor = executor.run();
		}
	}
	
	private void onInvoiceImageMsgReceived(InvoiceImageMsg msg) {
		if (msg != null) {
			InvoiceMsg msgs = getInvoiceModel().onInvoiceImageMsgReceived(msg);
			if (msgs != null) {
				logger.info("接收到發票圖片訊息 {}", msgs);
				this.onInvoiceMsgReceived(msgs);
			}
		}
	}
	
	private void onImageMsgReceived(ReceivingImageMsg event) {
		try {
			if (getInvoiceModel().isAttendInvoiceEvent(event.getUid())) {
				LineHttpService httpService = ApplicationContextProvider.getApplicationContext().getBean(LineHttpService.class);
				InvoiceDecodeService decodeService = ApplicationContextProvider.getApplicationContext().getBean(InvoiceDecodeService.class);
				CompletableFuture<BufferedImage> result = httpService.readImageFromLine(event.getContentId());
				result.whenComplete((buffer, exec)->{
					if (exec != null) {
						logger.error("Invoice read content錯誤", exec);
					}
					try {
						logger.info("Buffer {}", buffer);
						InvoiceEntity decode = decodeService.decode(buffer);
						InvoiceImageMsg msg = new InvoiceImageMsg();
						msg.setReplyToken(event.getReplyToken());
						msg.setUuid(event.getUid());
						msg.setReceivedTime(DateTimeModel.getTime());
						if (decode != null) {
							msg.setInvNum(decode.getInvNum());
							msg.setInvRand(decode.getInvRandom());
							msg.setInvTerm(decode.getInvTerm());
							msg.setBuffered(buffer);
						} else {
							msg.setType(KeywordEventTypes.INVOICE_DECODE_FAIL);
						}
						this.onInvoiceImageMsgReceived(msg);
					} catch (Exception e) {
						logger.error("decode content error", exec);
					}
				});
			}
		} catch (Exception e) {
			logger.error("onImageMsgReceived Error: ", e);
		}
	}
	
	private void onTextMsgReceived(ReceivingTextMsg event) {
		EventExecutor executor = ReceivingTextExecutor.builder().msg(event).build();
		this.fsm(executor);
	}

	private void onFriendEventReceived(Event event) {
		this.routerFriendActor.tell(event, ActorRef.noSender());
	}
	
	@Override
	public Receive createReceive() {
		return receiveBuilder()
						.match(ReceivingTextMsg.class, this::onTextMsgReceived)
						.match(FollowEvent.class, this::onFriendEventReceived)
						.match(UnfollowEvent.class, this::onFriendEventReceived)
						.match(ReceivingImageMsg.class, this::onImageMsgReceived)
						.match(InvoiceImageMsg.class, this::onInvoiceImageMsgReceived)
						.matchAny(any -> logger.warn("handler unchecked type"))
						.build();
	}

}

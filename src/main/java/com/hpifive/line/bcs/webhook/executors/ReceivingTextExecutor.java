package com.hpifive.line.bcs.webhook.executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hpifive.line.bcs.webhook.akka.body.OrderListMsg;
import com.hpifive.line.bcs.webhook.akka.body.ReceivingTextMsg;
import com.hpifive.line.bcs.webhook.config.ApplicationContextProvider;
import com.hpifive.line.bcs.webhook.dao.UserDao;
import com.hpifive.line.bcs.webhook.service.CheckOrderStatusService;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ReceivingTextExecutor implements EventExecutor {

	private ReceivingTextMsg msg;

	private static final Logger logger = LoggerFactory.getLogger(ReceivingTextExecutor.class);
	
	@Override
	public EventExecutor run() {
		UserDao userDao = ApplicationContextProvider.getApplicationContext().getBean(UserDao.class);
		userDao.addUserByUidIfNotExist(this.msg.getUid());
		try {
			
			/* Invoice checking procedure - check is the message match to invoice keyword? */
//			InvoiceEventService invoiceModel = ApplicationContextProvider.getApplicationContext().getBean(InvoiceEventService.class);
//			InvoiceMsg msgs = invoiceModel.onTextMsgReceived(this.msg);
//			if (msgs != null) {
//				return InvoiceMessageExecutor.builder().msg(msgs).build();
//			}

			/* Event checking procedure - check is the message match to event keyword? */
//			EventReceivingMsgHandlerMasterModelService model = ApplicationContextProvider.getApplicationContext().getBean(EventReceivingMsgHandlerMasterModelService.class);
//			EventRegisterMsg msg = model.onTextMsgReceived(this.msg.getUid(), this.msg.getText());
//			if (msg != null) {
//				msg.setReplyToken(this.msg.getReplyToken());
//				return new EventMessageExecutor(msg);
//			}

			/* Dominos order checking procedure - check is the message match to order status checking keyword? */
			CheckOrderStatusService checkOrderStatusModel = ApplicationContextProvider.getApplicationContext().getBean(CheckOrderStatusService.class);
			OrderListMsg orderListMsg = checkOrderStatusModel.onTextMsgReceived(this.msg);
			if (orderListMsg != null) {
				logger.info("orderListMsg = " + orderListMsg.toString());
				
				return CheckOrderSatusMessageExecutor.builder()
						.replyToken(orderListMsg.getReplyToken())
						.orderContent(orderListMsg.getText())
						.build();
			}

			/* Not the above procedure, continue to go to the BC keyword checking process */
			return KeywordMessageExecutor.builder().msg(this.msg).build();
		} catch (Exception e) {
			return UnknownErrorExecutor.builder().replyToken(this.msg.getReplyToken()).build();
		}
	}

}

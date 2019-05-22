package com.hpifive.line.bcs.webhook.executors;

import com.hpifive.line.bcs.webhook.akka.body.EventRegisterMsg;
import com.hpifive.line.bcs.webhook.akka.body.InvoiceMsg;
import com.hpifive.line.bcs.webhook.akka.body.ReceivingTextMsg;
import com.hpifive.line.bcs.webhook.config.ApplicationContextProvider;
import com.hpifive.line.bcs.webhook.dao.UserDao;
import com.hpifive.line.bcs.webhook.executors.event.EventMessageExecutor;
import com.hpifive.line.bcs.webhook.executors.invoice.InvoiceMessageExecutor;
import com.hpifive.line.bcs.webhook.service.EventReceivingMsgHandlerMasterModelService;
import com.hpifive.line.bcs.webhook.service.InvoiceEventService;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ReceivingTextExecutor implements EventExecutor {

	private ReceivingTextMsg msg;
	
	@Override
	public EventExecutor run() {
		UserDao userDao = ApplicationContextProvider.getApplicationContext().getBean(UserDao.class);
		userDao.addUserByUidIfNotExist(msg.getUid());
		try {
			InvoiceEventService invoiceModel = ApplicationContextProvider.getApplicationContext().getBean(InvoiceEventService.class);
			InvoiceMsg msgs = invoiceModel.onTextMsgReceived(msg);
			if (msgs != null) {
				return InvoiceMessageExecutor.builder().msg(msgs).build();
			}
			EventReceivingMsgHandlerMasterModelService model = ApplicationContextProvider.getApplicationContext().getBean(EventReceivingMsgHandlerMasterModelService.class);
			EventRegisterMsg msg = model.onTextMsgReceived(this.msg.getUid(), this.msg.getText());
			if (msg != null) {
				msg.setReplyToken(this.msg.getReplyToken());
				return new EventMessageExecutor(msg);
			}
			return KeywordMessageExecutor.builder().msg(this.msg).build();
		} catch (Exception e) {
			return UnknownErrorExecutor.builder().replyToken(this.msg.getReplyToken()).build();
		}
	}

}

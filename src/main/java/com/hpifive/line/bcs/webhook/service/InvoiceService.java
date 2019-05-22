package com.hpifive.line.bcs.webhook.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.hpifive.line.bcs.webhook.akka.AkkaService;
import com.hpifive.line.bcs.webhook.config.DefaultConfig;
import com.hpifive.line.bcs.webhook.dao.EventKeywordMessageListDao;
import com.hpifive.line.bcs.webhook.dao.UserDao;
import com.hpifive.line.bcs.webhook.entities.InvoiceEntity;
import com.hpifive.line.bcs.webhook.entities.LineUserEntity;
import com.hpifive.line.bcs.webhook.entities.config.InvoiceStatus;
import com.hpifive.line.bcs.webhook.entities.config.KeywordEventTypes;
import com.hpifive.line.bcs.webhook.invoice.InvoicePushBody;
import com.hpifive.line.bcs.webhook.repository.InvoiceEntityRepository;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;

@Service
@Component
public class InvoiceService {
	
	private static final Logger logger = LoggerFactory.getLogger(InvoiceService.class);
	
	@Autowired
	private UserDao userDao;
	@Autowired
	private InvoiceEventService eventService;
	@Autowired
	private AkkaService akkaService;
	@Autowired
	private InvoiceEntityRepository repository;
	@Autowired
	private EventKeywordMessageListDao messageListDao;
	
	public void refreshAllInvoiceByStatus(InvoiceStatus invoiceStatus) {
		List<InvoiceEntity> invs = this.repository.findByStatus(invoiceStatus.toString());
		for (InvoiceEntity inv : invs) {
			InvoiceStatus status = this.eventService.getInvoiceFromGov(inv);
			if (InvoiceStatus.NOT_FOUND == status) {
				Integer errorCount = inv.getErrorCount() == null ? 1 : inv.getErrorCount()+1;
				if (errorCount >= DefaultConfig.INVOICE_ERROR_COUNT.getValue()) {
					inv.setStatus(InvoiceStatus.INVOICE_FAKE);
				}
				inv.setErrorCount(errorCount);
			}
			this.repository.save(inv);
		}
	}
	
	public void sendBy(InvoicePushBody body) {
		String target = body.getTarget().toString();
		List<Long> eventIds = this.repository.findEventIdByStatus(target);
		if (eventIds == null || eventIds.isEmpty()) {
			return;
		}
		for (Long eventId : eventIds) {
			List<Long> ids = this.repository.findUserIdByStatusAndEventId(eventId, target);
			List<LineUserEntity> entitys = this.userDao.getAllByIds(ids);
			if (entitys == null || entitys.isEmpty()) {
				continue;
			}
			for (LineUserEntity user : entitys) {
				this.sendToId(eventId, user.getId(), user.getUid(), target, body.getReplyType());
			}
			this.repository.setStatusByEventIdAndStatus(eventId, target, body.getThen().toString());
		}
	}
	
	public void sendToMatchAndNot() {
		List<InvoicePushBody> list = Arrays.asList(
				new InvoicePushBody(InvoiceStatus.MATCH, KeywordEventTypes.INVOICE_MATCH, InvoiceStatus.MATCH_FINISH),
				new InvoicePushBody(InvoiceStatus.UNQUALIFIED, KeywordEventTypes.INVOICE_UNQUALIFIED, InvoiceStatus.UNQUALIFIED_FINISH),
				new InvoicePushBody(InvoiceStatus.NOT_MATCH, KeywordEventTypes.INVOICE_NOT_MATCH, InvoiceStatus.NOT_MATCH_FINISH),
				new InvoicePushBody(InvoiceStatus.INVOICE_FAKE, KeywordEventTypes.INVOICE_FAKE, InvoiceStatus.FAKE_FINISH)
				);
		for (InvoicePushBody invoicePushBody : list) {
			logger.info("發票名單推播 {}", invoicePushBody);
			this.sendBy(invoicePushBody);
		}
	}
	
	private void sendToId(Long eventId, Long userId, String uuid, String invoiceStatus, KeywordEventTypes types) {
		try {
			List<Message> messages = this.getInvResultMessageBy(eventId, userId, invoiceStatus, types);
			this.send(uuid, messages);
		} catch (Exception e) {
			logger.error(String.format("Send To Invoice Ids eventId %d KeywordEventType %s", eventId, types.toString()), e);
		}
	}
	
	private TextMessage getUserInvoiceMsg(Long eventId, Long userId, String invoiceStatus) {
		StringBuilder builder = new StringBuilder("電子發票驗證通知");
		try {
			List<String> invNums = this.repository.findInvNumByLineUserIdAndStatusAndEventId(eventId, userId, invoiceStatus);
			for (String string : invNums) {
				builder.append("\n");
				builder.append("發票號碼: ");
				builder.append(string);
			}
		} catch (Exception e) {
			logger.error("getUserInvoiceMsg {}", e);
		}
		return new TextMessage(builder.toString());
	}
	
	private List<Message> getInvResultMessageBy(Long eventId, Long userId, String invoiceStatus, KeywordEventTypes types) {
		List<Message> result = new ArrayList<>();
		try {
			TextMessage invMessage = this.getUserInvoiceMsg(eventId, userId, invoiceStatus);
			List<Message> messages = this.messageListDao.getMessageByEventIdAndKeywordTypes(eventId, types);
			result.add(invMessage);
			result.addAll(messages);
			return result;
		} catch (Exception e) {
			return null;
		}
	}
	
	private void send(String uid, List<Message> messages) {
		PushMessage pushMessage = new PushMessage(uid, messages);
		this.akkaService.reply(pushMessage);
	}
	
}

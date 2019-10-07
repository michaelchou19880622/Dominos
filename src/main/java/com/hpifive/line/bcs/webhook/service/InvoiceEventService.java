package com.hpifive.line.bcs.webhook.service;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.hpifive.line.bcs.webhook.akka.body.InvoiceImageMsg;
import com.hpifive.line.bcs.webhook.akka.body.InvoiceMsg;
import com.hpifive.line.bcs.webhook.akka.body.ReceivingTextMsg;
import com.hpifive.line.bcs.webhook.common.DateTimeModel;
import com.hpifive.line.bcs.webhook.config.ApplicationContextProvider;
import com.hpifive.line.bcs.webhook.dao.EventDao;
import com.hpifive.line.bcs.webhook.dao.UserDao;
import com.hpifive.line.bcs.webhook.entities.EventEntity;
import com.hpifive.line.bcs.webhook.entities.InvoiceEntity;
import com.hpifive.line.bcs.webhook.entities.LineUserEntity;
import com.hpifive.line.bcs.webhook.entities.config.InvoiceStatus;
import com.hpifive.line.bcs.webhook.entities.config.KeywordEventTypes;
import com.hpifive.line.bcs.webhook.repository.InvoiceEntityRepository;

@Service
@Component
public class InvoiceEventService {

	private static final Logger logger = LoggerFactory.getLogger(InvoiceEventService.class);

	@Autowired
	private InvoiceRewardMatcherService invoiceMatcher;
	@Autowired
	private InvoiceHttpService invoiceHttpService;
	@Autowired
	private EventService eventService;
	@Autowired
	private UserDao userDao;
	@Autowired
	private EventDao eventDao;
	@Autowired
	private InvoiceValidService invoiceValidService;
	@Autowired
	private InvoiceEntityRepository repository;
	
	public InvoiceStatus getInvoiceFromGov(InvoiceEntity inv) {
		InvoiceEntity entity = this.invoiceHttpService.getInvoiceFromGov(inv);
		if (entity == null || InvoiceStatus.NOT_FOUND.toString().equals(entity.getStatus())) {
			return InvoiceStatus.NOT_FOUND;
		}
		if (inv.getInvoiceDetail() == null || inv.getInvoiceDetail().isEmpty()) {
			inv.setInvoiceDetail(entity.getInvoiceDetail());
		}
		inv.setInvPeriod(entity.getInvPeriod());
		inv.setCode(entity.getCode());
		inv.setMsg(entity.getMsg());
		inv.setSellerName(entity.getSellerName());
		inv.setSellerBan(entity.getSellerBan());
		inv.setSellerAddress(entity.getSellerAddress());
		inv.setInvDate(entity.getInvDate());
		inv.setInvStatus(entity.getInvStatus());
		inv.setStatus(InvoiceStatus.VALID.toString());
		this.invoiceMatcher.matchInvoiceGoods(inv);
		return InvoiceStatus.VALID;
	}
	
	public InvoiceMsg onInvoiceImageMsgReceived(InvoiceImageMsg msg) {
		if (msg == null) {
			return null;
		}
		Long userId = this.userDao.getIdByUid(msg.getUuid());
		if (userId == null) {
			return null;
		}
		List<InvoiceEntity> tempEntity = repository.findUnFilledInvoiceByUserIdAndDate(userId, new Date(), PageRequest.of(0, 1));
		if (tempEntity == null || tempEntity.isEmpty()) {
			return null;
		}
		Long eventId = tempEntity.get(0).getEventId();
		InvoiceMsg result = new InvoiceMsg(userId, eventId, msg.getUuid(), msg.getReplyToken(), null, null);
		result.setType(KeywordEventTypes.INVOICE_FINISH);
		result.setInvoice(tempEntity.get(0));
		if (msg.getType() == KeywordEventTypes.INVOICE_DECODE_FAIL) {
			result.setType(msg.getType());
			return result;
		}
		if (msg.getInvNum() == null || msg.getInvRand() == null || msg.getInvTerm() == null) {
			return null;
		}
		KeywordEventTypes t = this.invoiceValidService.validInvoice(eventId, msg.getInvNum(), msg.getInvRand(), msg.getInvTerm());
		if (t != null) {
			result.setType(t);
		}
		if (result.getType() == KeywordEventTypes.INVOICE_FINISH) {
			FileSaverService fileService = ApplicationContextProvider.getApplicationContext().getBean(FileSaverService.class);
			fileService.save(msg.getBuffered(), msg.getInvNum()+msg.getInvTerm()+msg.getInvRand(), "jpg");
			InvoiceEntity entity = tempEntity.get(0);
			entity.setInvNum(msg.getInvNum());
			entity.setInvTerm(msg.getInvTerm());
			entity.setInvRandom(msg.getInvRand());
			entity.setUploadTime(DateTimeModel.getTime());
			entity.setStatus(InvoiceStatus.NOT_FOUND.toString());
			this.repository.save(entity);
		}
		return result;
	}
	
	public boolean isAttendInvoiceEvent(String uuid) {
		LineUserEntity userEntity = this.userDao.getByUid(uuid);
		if (userEntity == null) {
			return false;
		}
		Long userId = userEntity.getId();
		List<InvoiceEntity> entities = this.repository.findUnFinishInvoiceByUserIdAndDate(userId, new Date(), PageRequest.of(0, 1));
		return (entities != null && ! entities.isEmpty());
	}
	
	public InvoiceMsg onTextMsgReceived(ReceivingTextMsg msg) {
		LineUserEntity userEntity = this.userDao.getByUid(msg.getUid());
		if (userEntity == null) {
			return null;
		}
		Long userId = userEntity.getId();
		List<InvoiceEntity> entities = this.repository.findUnFinishInvoiceByUserIdAndDate(userId, new Date(), PageRequest.of(0, 1));
		if (entities == null || entities.isEmpty()) {
			return null;
		}
		return new InvoiceMsg(userId, entities.get(0).getEventId(), msg.getUid(), msg.getReplyToken(), msg.getText(), entities.get(0));
	}
	
	public boolean validInvoiceTermByEventId(Long eventId, String invTerm) {
		try {
			Integer term = Integer.valueOf(invTerm);
			EventEntity entity = this.eventDao.getById(eventId);
			if (entity == null) {
				return false;
			}
			term = (term/10000 > 1) ? term/100 : term;
			String formatRule = "%d%02d";
			ZonedDateTime startTime = DateTimeModel.getTime(entity.getBeginDatetime());
			ZonedDateTime endTime = DateTimeModel.getTime(entity.getEndDatetime());
			ZonedDateTime currentTime = DateTimeModel.getTime();
			Integer currentTerm = Integer.valueOf(String.format(formatRule, currentTime.getYear()-1911, currentTime.getMonthValue()));
			Integer startTerm = Integer.valueOf(String.format(formatRule, startTime.getYear()-1911, startTime.getMonthValue()));
			Integer endTerm = Integer.valueOf(String.format(formatRule, endTime.getYear()-1911, endTime.getMonthValue()));
			return (term >= startTerm && term <= currentTerm && currentTerm <= endTerm);
		} catch (Exception e) {
			return false;
		}
	}
	
	public InvoiceEntity getUnfinishInvoiceBy(InvoiceMsg msg) {
		try {
			Long eventId = msg.getEventId();
			Long userId = msg.getUserId();
			return repository.findUnFinishInvoiceByEventIdAndUserId(eventId, userId);
		} catch (Exception e) {
			logger.error("getUnfinishInvoiceBy InvoiceMsg {}", msg);
			logger.error("ErrorMessage: ", e);
			return null;
		}
	}

	public boolean isInputTimeOut(Long eventId, ZonedDateTime time) {
		return this.eventService.isUserInputTimeout(eventId, time);
	}
	
	public KeywordEventTypes getEventTypeByFillInvoice(InvoiceEntity entity, String text) {
		if (entity == null) {
			return null;
		}
		if (text == null) {
			return KeywordEventTypes.USER_INPUT_ERROR;
		}
		if (this.isInputTimeOut(entity.getEventId(), entity.getUploadTime())) {
			return KeywordEventTypes.TIME_OUT;
		} else if (entity.getInvNum() == null) {
			KeywordEventTypes t = this.invoiceValidService.validInvoiceNumber(entity.getEventId(), text);
			if (t != null) {
				return t;
			}
			entity.setInvNum(text);
		} else if (entity.getInvRandom() == null) {
			KeywordEventTypes t = this.invoiceValidService.validInvoiceRandomNumber(text);
			if (t != null) {
				return t;
			}
			entity.setInvRandom(text);
		} else if (entity.getInvTerm() == null) {
			KeywordEventTypes t = this.invoiceValidService.validInvoiceTermNumber(entity.getEventId(), text);
			if (t != null) {
				return t;
			}
			entity.setInvTerm(text);
			entity.setStatus(InvoiceStatus.NOT_FOUND.toString());
		}
		ZonedDateTime currentTime = DateTimeModel.getTime();
		entity.setUploadTime(currentTime);
		this.repository.save(entity);
		return this.getTypeByInvoiceFillStatus(entity);
	}
	
	public KeywordEventTypes getTypeByInvoiceFillStatus(InvoiceEntity entity) {
		if (entity.getInvNum() == null) {
			return KeywordEventTypes.INVOICE_NUM;
		} else if (entity.getInvRandom() == null) {
			return KeywordEventTypes.INVOICE_RAND;
		} else if (entity.getInvTerm() == null) {
			return KeywordEventTypes.INVOICE_TERM;
		}
		return KeywordEventTypes.INVOICE_FINISH;
	}
	
	public void addBy(Long eventId, Long userId) {
		InvoiceEntity entity = new InvoiceEntity();
		entity.setLineUserId(userId);
		entity.setEventId(eventId);
		this.repository.save(entity);
	}
	
	public void clean(InvoiceEntity entity) {
		this.repository.delete(entity);
	}
	
	public KeywordEventTypes incrementErrorCountByType(Long eventId, Long userId, KeywordEventTypes type) {
		if (type != null && 
				KeywordEventTypes.USER_INPUT_ERROR == type ||
				KeywordEventTypes.INVOICE_DUPLICATE == type ||
				KeywordEventTypes.INVOICE_NOT_IN_PERIOD == type) {
					if (eventService.isUserInputErrorOverCount(eventId, userId)) {
						return KeywordEventTypes.ERROR_N_TIME;
					} else {
						eventService.incrementErrorCount(eventId, userId);
					}
		}
		return null;
	}
	
}

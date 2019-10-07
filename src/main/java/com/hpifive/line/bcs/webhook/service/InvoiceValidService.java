package com.hpifive.line.bcs.webhook.service;

import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.hpifive.line.bcs.webhook.common.DateTimeModel;
import com.hpifive.line.bcs.webhook.common.ValidPatternModel;
import com.hpifive.line.bcs.webhook.dao.EventDao;
import com.hpifive.line.bcs.webhook.entities.EventEntity;
import com.hpifive.line.bcs.webhook.entities.config.KeywordEventTypes;
import com.hpifive.line.bcs.webhook.repository.InvoiceEntityRepository;

@Service
@Component
public class InvoiceValidService {
	
	@Autowired
	private EventDao eventDao;
	@Autowired
	private InvoiceEntityRepository repository;
	
	public KeywordEventTypes validInvoice(Long eventId, String invNum, String invRand, String invTerm) {
		KeywordEventTypes t = this.validInvoiceNumber(eventId, invNum);
		if (t != null) {
			return t;
		}
		if (! this.validInvoiceTermByEventId(eventId, invTerm)) {
			return KeywordEventTypes.INVOICE_NOT_IN_PERIOD;
		}
		return this.validInvoiceRandomNumber(invRand);
	}
	
	public KeywordEventTypes validInvoiceTermNumber(Long eventId, String text) {
		if (! ValidPatternModel.isInvoiceTerm(text)) {
			return KeywordEventTypes.USER_INPUT_ERROR;
		}
		if (! this.validInvoiceTermByEventId(eventId, text)) {
			return KeywordEventTypes.INVOICE_NOT_IN_PERIOD;
		}
		return null;
	}
	
	public KeywordEventTypes validInvoiceRandomNumber(String text) {
		if (! ValidPatternModel.isInvoiceRandNumber(text)) {
			return KeywordEventTypes.USER_INPUT_ERROR;
		}
		return null;
	}
	
	public KeywordEventTypes validInvoiceNumber(Long eventId, String text) {
		if (! ValidPatternModel.isInvoiceNumber(text)) {
			return KeywordEventTypes.USER_INPUT_ERROR;
		}
		if (this.checkInvoiceNumDuplicateByEventId(eventId, text)) {
			return KeywordEventTypes.INVOICE_DUPLICATE;
		}
		return null;
	}
	
	private boolean checkInvoiceNumDuplicateByEventId(Long eventId, String invNum) {
		return (this.repository.countByEventIdAndInvNum(eventId, invNum) > 0);
	}
	
	private boolean validInvoiceTermByEventId(Long eventId, String invTerm) {
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
	
}

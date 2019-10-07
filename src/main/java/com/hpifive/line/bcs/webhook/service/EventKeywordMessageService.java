package com.hpifive.line.bcs.webhook.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.springframework.stereotype.Service;

import com.hpifive.line.bcs.webhook.dao.ApplyKeywordMessageListDao;
import com.hpifive.line.bcs.webhook.dao.EventAttendanceAttributeDao;
import com.hpifive.line.bcs.webhook.dao.EventAttendanceDao;
import com.hpifive.line.bcs.webhook.dao.EventKeywordMessageListDao;
import com.hpifive.line.bcs.webhook.entities.EventAttendanceEntity;
import com.hpifive.line.bcs.webhook.entities.config.KeywordEventTypes;
import com.hpifive.line.bcs.webhook.exception.DaoException;
import com.linecorp.bot.model.message.Message;

@Service
@Component
public class EventKeywordMessageService {

private static final Logger logger = LoggerFactory.getLogger(EventKeywordMessageService.class);

	@Autowired
	private EventKeywordMessageListDao msgDao;
	@Autowired
	private EventAttendanceDao attendanceDao;
	@Autowired
	private EventAttendanceAttributeDao attributeDao;
	@Autowired
	private ApplyKeywordMessageListDao applyMsgDao;
	
	public List<Message> getMessageBy(Long eventId, Long userId, KeywordEventTypes type) throws DaoException {
		EventAttendanceEntity entity = attendanceDao.getByUserIdAndEventId(userId, eventId);
		if (entity == null) {
			throw DaoException.message(String.format("Can't find EventAttendanceEntity By eventId %d And userId %d", eventId, userId));
		}
		return this.getMessagesBy(eventId, entity.getId(), type);
	}
	public List<Message> getMessagesBy(Long eventId, Long attendId, Integer applyStatus, KeywordEventTypes type) {
		List<Message> messages = new ArrayList<>();
		try {
			if (KeywordEventTypes.REGISTER_FLOW_RUNNING == type) {
	//			代表註冊流程進行中 回覆下一個page樣板
				messages = this.applyMsgDao.getMessageByEventIdAndPage(eventId, applyStatus, true);
			} else if (KeywordEventTypes.APPLY_AGREE == type){
				messages = this.applyMsgDao.getMessageByEventIdAndPage(eventId, 0, true);
			} else if (KeywordEventTypes.APPLY_COMPLETED_NO == type) {
	//			回覆第一個需要輸入的樣板
				messages = this.applyMsgDao.getMessageByEventIdAndPage(eventId, 0, true);
			} else if (KeywordEventTypes.CUSTOMIZE_INPUT_ERROR == type) {
	//			回覆客製化錯誤樣板
				Integer page = applyStatus-1;
				messages = this.applyMsgDao.getMessageByEventIdAndPage(eventId, page, false);
			}
		} catch (Exception e) {
			logger.error("getMessagesBy EventId {} AttendanceId {} ApplyStatus {}", eventId, attendId, applyStatus, e);
			return null;
		}
		return messages;
	}
	public List<Message> getMessagesBy(Long eventId, Long attendId, KeywordEventTypes type) throws DaoException {
		List<Message> result = new ArrayList<>();
		if (KeywordEventTypes.APPLY == type) {
			result = this.msgDao.getMessageByEventIdAndKeywordTypes(eventId, KeywordEventTypes.APPLY_EXTRA_AGREEMENT);
			result.addAll(this.msgDao.getMessageByEventIdAndKeywordTypes(eventId, KeywordEventTypes.APPLY_AGREEMENT));
		} else if (KeywordEventTypes.APPLY_COMPLETED == type) {
			result.add(this.attributeDao.getTextMessageByEventIdAndUserId(attendId));
			result.addAll(this.msgDao.getMessageByEventIdAndKeywordTypes(eventId, type));
		} else if (KeywordEventTypes.WAIT_CONFIRM == type) {
			result.addAll(this.msgDao.getMessageByEventIdAndKeywordTypes(eventId, KeywordEventTypes.WAIT_CONFIRM_MESSAGE));
			result.add(this.attributeDao.getTextMessageByEventIdAndUserId(attendId));
			result.addAll(this.msgDao.getMessageByEventIdAndKeywordTypes(eventId, type));
		} else if (KeywordEventTypes.CONFIRM_CHECK == type) {
			result = this.msgDao.getMessageByEventIdAndKeywordTypes(eventId, KeywordEventTypes.CONFIRM);
		} else if (KeywordEventTypes.REJECT_CHECK == type) {
			result = this.msgDao.getMessageByEventIdAndKeywordTypes(eventId, KeywordEventTypes.REJECT);
		} else if (KeywordEventTypes.CONFIRM_TIMEOUT == type) {
			result = this.msgDao.getMessageByEventIdAndKeywordTypes(eventId, KeywordEventTypes.TIME_OUT);
		} else {
			result = this.msgDao.getMessageByEventIdAndKeywordTypes(eventId, type);
		}
		return result;
	}
}

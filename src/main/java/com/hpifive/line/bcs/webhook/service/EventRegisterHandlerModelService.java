package com.hpifive.line.bcs.webhook.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.hpifive.line.bcs.webhook.akka.body.EventRegisterMsg;
import com.hpifive.line.bcs.webhook.common.ValidPatternModel;
import com.hpifive.line.bcs.webhook.dao.EventApplyDataDao;
import com.hpifive.line.bcs.webhook.dao.EventApplyDataKeywordDao;
import com.hpifive.line.bcs.webhook.dao.EventAttendanceAttributeDao;
import com.hpifive.line.bcs.webhook.entities.EventApplydataEntity;
import com.hpifive.line.bcs.webhook.entities.EventAttendanceAttributeEntity;
import com.hpifive.line.bcs.webhook.entities.config.ApplyDataFormatType;
import com.hpifive.line.bcs.webhook.entities.config.ApplyDataInputStatus;
import com.hpifive.line.bcs.webhook.entities.config.KeywordEventTypes;
import com.hpifive.line.bcs.webhook.exception.CustomException;

@Service
@Component
public class EventRegisterHandlerModelService {

	private static final Logger logger = LoggerFactory.getLogger(EventRegisterHandlerModelService.class);
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	private EventAttendanceAttributeDao attributeDao;
	
	@Autowired
	private EventApplyDataDao applyDataDao;
	
	@Autowired
	private EventApplyDataKeywordDao applyDataKeywordDao;
	
	protected void addAttributeByEventIdAndUserIdAndApplyStatus(Long eventId, Long userId, Long attendId, String keyword, Integer page) throws CustomException{
		EventApplydataEntity applyDataEntity = this.applyDataDao.getByEventIdAndPage(eventId, page);
		if (applyDataEntity == null) {
			String error = String.format("Can't find ApplyDataEntity with eventId %s and page %d", eventId.toString(), page);
			throw CustomException.message(error);
		}
		EventAttendanceAttributeEntity attributeEntity = new EventAttendanceAttributeEntity();
		attributeEntity.setDescription(applyDataEntity.getColumnName());
		attributeEntity.setEventId(eventId);
		attributeEntity.setFormatType(applyDataEntity.getColumnFormat());
		attributeEntity.setKey(applyDataEntity.getColumnKey());
		attributeEntity.setLineUserId(userId);
		attributeEntity.setValue(keyword);
		attributeEntity.setAttendanceId(attendId);
		this.attributeDao.save(attributeEntity);
	}
	
	protected boolean verifyUserInput(EventRegisterMsg body) throws CustomException {
		Long eventId = body.getEventId();
		Integer page = body.getApplyStatus()-1;
		String keyword = body.getKeyword();
		EventApplydataEntity applyDataEntity = this.applyDataDao.getByEventIdAndPage(eventId, page);
		if (applyDataEntity == null) {
			String error = String.format("Can't find ApplyDataEntity with eventId %s and applyStatus %d", eventId.toString(), page);
			throw CustomException.message(error);
		}
		if (ApplyDataInputStatus.YES.getValue().equals(applyDataEntity.getStatus())) {
			return (this.applyDataKeywordDao.isExistInKeywordListByApplyDataIdAndKeyword(applyDataEntity.getId(), body.getKeyword()));
		} else {
			return (this.verifyKeywordByColumnFormatAndLength(keyword, applyDataEntity.getColumnFormat(), applyDataEntity.getColumnLength()));
		}
	}

	protected boolean compareIsFinishedByEventIdAndCurrentStatus(Long eventId, Integer currentApplyStatus) {
		Integer allPage = applyDataDao.countByEventId(eventId);
		return (currentApplyStatus >= allPage);
	}
	
	public EventRegisterMsg onEventRegisterMsgReceived(EventRegisterMsg msg) throws CustomException  {
		KeywordEventTypes type = msg.getKeywordEventTypes();
		if (KeywordEventTypes.REGISTER_FLOW_RUNNING == type) {
			return this.register(msg);
		}
		msg.setKeywordEventTypes(type);
		return msg;
	}
	
	public EventRegisterMsg register(EventRegisterMsg msg) throws CustomException  {
		Long eventId= msg.getEventId();
		Long userId = msg.getUserId();
		String keyword = msg.getKeyword();
		Integer applyStatus = msg.getApplyStatus();
		if (this.eventService.isUserAttendanceInputTimeout(eventId, msg.getAttendanceId())) {
//			TIME OUT
			logger.info("註冊輸入TIMEOUT eventId:  {} , userId: {}", eventId, userId);
			msg.setKeywordEventTypes(KeywordEventTypes.TIME_OUT);
			return msg;
		}
		if (this.verifyUserInput(msg)) {
//			儲存USER 輸入的屬性到Attribute
			this.addAttributeByEventIdAndUserIdAndApplyStatus(eventId, userId, msg.getAttendanceId(),keyword, applyStatus-1);
			boolean finished = this.compareIsFinishedByEventIdAndCurrentStatus(eventId, applyStatus);
			msg.setKeywordEventTypes(KeywordEventTypes.REGISTER_FLOW_RUNNING);
			if (finished) {
				msg.setKeywordEventTypes(KeywordEventTypes.APPLY_COMPLETED);
			}
			return msg;
		}
//		驗證是否超過輸入錯誤限制
		if (this.eventService.isUserInputErrorOverCount(eventId, userId)) {
			msg.setKeywordEventTypes(KeywordEventTypes.ERROR_N_TIME);
		} else {
			msg.setKeywordEventTypes(KeywordEventTypes.CUSTOMIZE_INPUT_ERROR);
		}
		return msg;
	}
	
	public boolean verifyKeywordByColumnFormatAndLength(String keyword, String columnFormat, Integer length) throws CustomException  {
		ApplyDataFormatType formatType = ApplyDataFormatType.fromString(columnFormat);
		return ValidPatternModel.verifyStringByFormatTypeAndLength(keyword, formatType, length);
	}
}

package com.hpifive.line.bcs.webhook.service;

import java.time.ZonedDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.hpifive.line.bcs.webhook.common.DateTimeModel;
import com.hpifive.line.bcs.webhook.dao.EventAttendanceAttributeDao;
import com.hpifive.line.bcs.webhook.dao.EventAttendanceDao;
import com.hpifive.line.bcs.webhook.dao.EventDao;
import com.hpifive.line.bcs.webhook.entities.EventAttendanceAttributeEntity;
import com.hpifive.line.bcs.webhook.entities.EventAttendanceEntity;
import com.hpifive.line.bcs.webhook.entities.EventEntity;
import com.hpifive.line.bcs.webhook.entities.config.LineUserBelongingTypes;

@Service
@Component
public class EventService {

	private static final Logger logger = LoggerFactory.getLogger(EventService.class);

	@Autowired
	private EventDao eventDao;
	@Autowired
	private EventAttendanceDao attendanceDao;
	@Autowired
	private EventAttendanceAttributeDao attributeDao;
	@Autowired
	private EventPrizeSenderService prizeSenderService;

	public void sendEventPrizeByEventIdAndUserId(Long eventId, Long userId, Long attendanceId) {
		try {
			EventEntity entity = this.eventDao.getById(eventId);
			if (entity == null) {
				return;
			}
			this.prizeSenderService.pushTicketToUsers(userId, entity.getEventPrizeId(), attendanceId, LineUserBelongingTypes.ATTEND);
		} catch (Exception e) {
			logger.error("Send Prize By EventId {} And UserId {} Fail {}", eventId, userId, e);
		}
	}
	
	public void incrementErrorCount(Long eventId, Long userId) {
		this.attendanceDao.incrementErrorCountByUserIdAndEventId(userId, eventId);
	}
	
	public Integer getMaxErrorCount(Long eventId) {
		return this.eventDao.getErrorCountById(eventId);
	}
	
	public Integer getUserInputErrorCount(Long eventId, Long userId) {
		EventAttendanceEntity entity = this.attendanceDao.getByUserIdAndEventId(userId, eventId);
		if (entity == null) {
			return 0;
		}
		return entity.getInputErrorCount();
	}
	
	public boolean isUserInputErrorOverCount(Long eventId, Long userId) {
		Integer max = this.getMaxErrorCount(eventId);
		Integer userCount = this.getUserInputErrorCount(eventId, userId)+1;
		return (userCount >= max);
	}
	
	public Integer getEventInputTimeoutSecond(Long eventId) {
		try {
			EventEntity eventEntity = this.eventDao.getById(eventId);
			return eventEntity.getInputTimeout();
		} catch (Exception e) {
			return 0;
		}
	}
	
	public boolean isUserInputTimeout(Long eventId, ZonedDateTime time) {
		Integer timeOutSecond = this.getEventInputTimeoutSecond(eventId);
		if (timeOutSecond == 0 || timeOutSecond == null || time == null) {
			return false;
		}
		time = time.plusSeconds(timeOutSecond);
		return time.isBefore(DateTimeModel.getTime());
	}
	
	public boolean isUserAttendanceInputTimeout(Long eventId, Long attendId) {
		EventAttendanceAttributeEntity attributeEntity = this.attributeDao.getOneByAttendIdOrderByCreationTimeDesc(attendId);
		if (attributeEntity == null) {
			return false;
		}
		ZonedDateTime timeOutDate = DateTimeModel.getTime(attributeEntity.getCreateTime());
		return this.isUserInputTimeout(eventId, timeOutDate);
	}
	
	public void resetUserInputErrorCount(Long eventId, Long userId) {
		this.attendanceDao.setInputErrorCountBy(eventId, userId, 0);
	}
	
	public Integer getMinimizeSpend(Long eventId) {
		try {
			EventEntity entity = this.eventDao.getById(eventId);
			Integer amount = entity.getMinimumSpendAmount();
			return (amount == null) ? 0 : amount;
		} catch (Exception e) {
			return 0;
		}
	}
	
}

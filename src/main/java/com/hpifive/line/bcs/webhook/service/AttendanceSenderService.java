package com.hpifive.line.bcs.webhook.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpifive.line.bcs.webhook.akka.AkkaService;
import com.hpifive.line.bcs.webhook.akka.body.EventKeywordPushBody;
import com.hpifive.line.bcs.webhook.controller.body.ExpiredKeyValueBody;
import com.hpifive.line.bcs.webhook.dao.EventAttendanceDao;
import com.hpifive.line.bcs.webhook.dao.UserDao;
import com.hpifive.line.bcs.webhook.entities.config.EventApplyStatus;
import com.hpifive.line.bcs.webhook.entities.config.KeywordEventTypes;

@Service
public class AttendanceSenderService {

	@Autowired
	private UserDao userDao;
	@Autowired
	private EventAttendanceDao attendanceDao;
	@Autowired
	private AkkaService akkaService;
	
	public <T> List<Long> pushStatusByfilterUsers(ExpiredKeyValueBody<T> body, Long eventId, EventApplyStatus applyStatus, KeywordEventTypes thEventTypes,Integer userStatus, Integer volume) {
		List<Long> userIds = this.attendanceDao.getNonBlockUserIdByAttr(body.getFilters(), eventId, applyStatus.getValue(), userStatus, volume);
		this.send(eventId, userIds, thEventTypes, EventApplyStatus.valueOf(thEventTypes.toString()));
		if (userIds == null) {
			return new ArrayList<>();
		}
		return userIds;
	}
	
	private void send(Long eventId, List<Long> userIds, KeywordEventTypes type, EventApplyStatus targetStatus) {
		Map<String, Object> userMap = this.userDao.getIdAndUidMapBy(userIds);
		if (userMap != null) {
			this.akkaService.reply(new EventKeywordPushBody(eventId, userMap, type));
		}
		if (userIds != null && ! userIds.isEmpty()) {
			this.attendanceDao.setApplyStatusByEventIdAndUserIds(eventId, userIds, targetStatus.getValue());
		}
	}
}

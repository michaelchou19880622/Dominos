package com.hpifive.line.bcs.webhook.dao;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpifive.line.bcs.webhook.entities.EventAttendanceConfirmTimeEntity;
import com.hpifive.line.bcs.webhook.repository.EventAttendanceConfirmTimeRepository;

@Service
public class EventConfirmDao {

	@Autowired
	private EventAttendanceConfirmTimeRepository repository;
	
	public void addExpireTime(Long eventId, Long userId, ZonedDateTime expireTime) {
		if (userId == null) {
			return;
		}
		this.addExpireTime(eventId, Collections.singletonList(userId), expireTime);
	}
	
	public void addExpireTime(Long eventId, List<Long> userIds, ZonedDateTime expireTime) {
		if (userIds == null || userIds.isEmpty() || expireTime == null) {
			return;
		}
		List<EventAttendanceConfirmTimeEntity> entities = new ArrayList<>();
		for (Long userId : userIds) {
			entities.add(new EventAttendanceConfirmTimeEntity(null, eventId, userId, expireTime));
		}
		this.repository.saveAll(entities);
	}
}

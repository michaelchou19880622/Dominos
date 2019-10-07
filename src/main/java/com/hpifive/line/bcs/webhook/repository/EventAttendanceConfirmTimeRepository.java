package com.hpifive.line.bcs.webhook.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.hpifive.line.bcs.webhook.entities.EventAttendanceConfirmTimeEntity;

public interface EventAttendanceConfirmTimeRepository extends CrudRepository<EventAttendanceConfirmTimeEntity, Long> {

	public List<EventAttendanceConfirmTimeEntity> findByEventIdAndUserId(Long eventId, Long userId);
}

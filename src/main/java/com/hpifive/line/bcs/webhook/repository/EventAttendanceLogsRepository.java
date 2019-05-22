package com.hpifive.line.bcs.webhook.repository;

import org.springframework.data.repository.CrudRepository;

import com.hpifive.line.bcs.webhook.entities.EventAttendanceLogsEntity;

public interface EventAttendanceLogsRepository extends CrudRepository<EventAttendanceLogsEntity, Long> {

}

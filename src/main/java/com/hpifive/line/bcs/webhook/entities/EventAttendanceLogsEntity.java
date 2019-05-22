package com.hpifive.line.bcs.webhook.entities;

import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table
@Entity(name="event_attendance_logs")
public class EventAttendanceLogsEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="event_id")
	private Long eventId;
	
	@Column(name="lineuser_id")
	private Long userId;
	
	@Column(name="applyStatus")
	private Integer applyStatus;
	
	@Column(name="create_time")
	private ZonedDateTime craeteTime;
	
	public EventAttendanceLogsEntity(Long id, Long eventId, Long userId, Integer applyStatus, ZonedDateTime craeteTime) {
		super();
		this.id = id;
		this.eventId = eventId;
		this.userId = userId;
		this.applyStatus = applyStatus;
		this.craeteTime = craeteTime;
	}

	public EventAttendanceLogsEntity() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getApplyStatus() {
		return applyStatus;
	}

	public void setApplyStatus(Integer applyStatus) {
		this.applyStatus = applyStatus;
	}

	public ZonedDateTime getCraeteTime() {
		return craeteTime;
	}

	public void setCraeteTime(ZonedDateTime craeteTime) {
		this.craeteTime = craeteTime;
	}
	
	
}

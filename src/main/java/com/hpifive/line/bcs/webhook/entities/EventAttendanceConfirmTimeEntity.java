package com.hpifive.line.bcs.webhook.entities;

import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="event_attendance_confirm_time")
public class EventAttendanceConfirmTimeEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="event_id")
	private Long eventId;
	
	@Column(name="lineuser_id")
	private Long userId;
	
	@Column(name="expired_time")
	private ZonedDateTime expireTime;

	public EventAttendanceConfirmTimeEntity() {
		super();
	}

	public EventAttendanceConfirmTimeEntity(Long id, Long eventId, Long userId, ZonedDateTime expireTime) {
		super();
		this.id = id;
		this.eventId = eventId;
		this.userId = userId;
		this.expireTime = expireTime;
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

	public ZonedDateTime getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(ZonedDateTime expireTime) {
		this.expireTime = expireTime;
	}
	
}

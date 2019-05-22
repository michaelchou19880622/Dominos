package com.hpifive.line.bcs.webhook.entities;

import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="lineuser_reward")
public class LineUserRewardEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="reward_card_id", nullable=false)
	private Long cardId;
	
	@Column(name="lineuser_id", nullable=false)
	private Long userId;
	
	@Column(name="event_id", nullable=false)
	private Long eventId;
	
	@Column(name="reward_card_point")
	private Integer point;
	
	@Column(name="latest_get_point_time")
	private ZonedDateTime lastTime;
	
	@Column(name="status")
	private Integer status;
	
	@Column(name="valid_begin_time")
	private ZonedDateTime beginTime;
	
	@Column(name="valie_end_Time")
	private ZonedDateTime endTime;

	public LineUserRewardEntity() {
		super();
	}

	public LineUserRewardEntity(Long id, Long cardId, Long userId, Long eventId, Integer point, ZonedDateTime lastTime,
			Integer status, ZonedDateTime beginTime, ZonedDateTime endTime) {
		super();
		this.id = id;
		this.cardId = cardId;
		this.userId = userId;
		this.eventId = eventId;
		this.point = point;
		this.lastTime = lastTime;
		this.status = status;
		this.beginTime = beginTime;
		this.endTime = endTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCardId() {
		return cardId;
	}

	public void setCardId(Long cardId) {
		this.cardId = cardId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public Integer getPoint() {
		return point;
	}

	public void setPoint(Integer point) {
		this.point = point;
	}

	public ZonedDateTime getLastTime() {
		return lastTime;
	}

	public void setLastTime(ZonedDateTime lastTime) {
		this.lastTime = lastTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public ZonedDateTime getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(ZonedDateTime beginTime) {
		this.beginTime = beginTime;
	}

	public ZonedDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(ZonedDateTime endTime) {
		this.endTime = endTime;
	}
	
	
}

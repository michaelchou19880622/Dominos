package com.hpifive.line.bcs.webhook.entities;

import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="event_goods")
public class EventGoodsEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="event_id")
	private Long eventId;
	
	@Column(name="goods_id")
	private Long goodId;
	
	@Column(name="reward_point")
	private Integer rewardPoint;
	
	@Column(name="modify_user")
	private String modifyUser;
	
	@Column(name="modify_datetime")
	private ZonedDateTime modifyTime;

	public EventGoodsEntity() {
		super();
	}

	public EventGoodsEntity(Long id, Long eventId, Long goodId, Integer rewardPoint, String modifyUser,
			ZonedDateTime modifyTime) {
		super();
		this.id = id;
		this.eventId = eventId;
		this.goodId = goodId;
		this.rewardPoint = rewardPoint;
		this.modifyUser = modifyUser;
		this.modifyTime = modifyTime;
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

	public Long getGoodId() {
		return goodId;
	}

	public void setGoodId(Long goodId) {
		this.goodId = goodId;
	}

	public Integer getRewardPoint() {
		return rewardPoint;
	}

	public void setRewardPoint(Integer rewardPoint) {
		this.rewardPoint = rewardPoint;
	}

	public String getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}

	public ZonedDateTime getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(ZonedDateTime modifyTime) {
		this.modifyTime = modifyTime;
	}
	
	
}

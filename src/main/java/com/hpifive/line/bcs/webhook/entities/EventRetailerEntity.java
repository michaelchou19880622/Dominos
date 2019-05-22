package com.hpifive.line.bcs.webhook.entities;

import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="event_retailer")
public class EventRetailerEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="event_id", nullable=false)
	private Long eventId;
	
	@Column(name="retailer_id", nullable=false)
	private Long retailerId;
	
	@Column(name="modify_user")
	private String modifyUser;
	
	@Column(name="modify_datetime")
	private ZonedDateTime modifyTime;

	public EventRetailerEntity() {
		super();
	}

	public EventRetailerEntity(Long id, Long eventId, Long retailerId, String modifyUser, ZonedDateTime modifyTime) {
		super();
		this.id = id;
		this.eventId = eventId;
		this.retailerId = retailerId;
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

	public Long getRetailerId() {
		return retailerId;
	}

	public void setRetailerId(Long retailerId) {
		this.retailerId = retailerId;
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

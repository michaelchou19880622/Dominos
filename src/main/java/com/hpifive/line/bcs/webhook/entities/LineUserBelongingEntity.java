package com.hpifive.line.bcs.webhook.entities;

import java.time.ZonedDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="lineuser_belonging")
public class LineUserBelongingEntity {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Column(name="lineuser_id")
	private Long userId;
	
	@Column(name="belonging_id")
	private Long belongId;
	
	@Column(name="belonging_type")
	private String belongType;
	
	@Column(name="belonging_source")
	private Long belongSource;
	
	@Column(name="status", columnDefinition="CHAR(1)")
	private String status;
	
	@Column(name="received_time")
	private ZonedDateTime receivedTime;
	
	@Column(name="used_time")
	private Date usedTime;
	
	public LineUserBelongingEntity() {
	}

	public LineUserBelongingEntity(Long id, Long userId, Long belongId, String belongType, Long belongSource, String status,
			ZonedDateTime receivedTime, Date usedTime) {
		super();
		this.id = id;
		this.userId = userId;
		this.belongId = belongId;
		this.belongType = belongType;
		this.belongSource = belongSource;
		this.status = status;
		this.receivedTime = receivedTime;
		this.usedTime = usedTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getBelongId() {
		return belongId;
	}

	public void setBelongId(Long belongId) {
		this.belongId = belongId;
	}

	public String getBelongType() {
		return belongType;
	}

	public void setBelongType(String belongType) {
		this.belongType = belongType;
	}

	public Long getBelongSource() {
		return belongSource;
	}

	public void setBelongSource(Long belongSource) {
		this.belongSource = belongSource;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public ZonedDateTime getReceivedTime() {
		return receivedTime;
	}

	public void setReceivedTime(ZonedDateTime receivedTime) {
		this.receivedTime = receivedTime;
	}

	public Date getUsedTime() {
		return usedTime;
	}

	public void setUsedTime(Date usedTime) {
		this.usedTime = usedTime;
	}
	
	
}

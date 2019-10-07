package com.hpifive.line.bcs.webhook.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="event_prize_detail")
public class EventPrizeDetailEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="event_prize_id", insertable=false, updatable=false)
	private Long eventPrizeId;
	
	@Column(name="prize_sn")
	private String prizeSN;
	
	@Column(name="used_lineuser_id")
	private Long userId;
	
	@Column(name="status", columnDefinition="char")
	private String status;
	
	@ManyToOne
    @JoinColumn(name="event_prize_id")
    private EventPrizeEntity prizeEntity;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getEventPrizeId() {
		return eventPrizeId;
	}

	public void setEventPrizeId(Long eventPrizeId) {
		this.eventPrizeId = eventPrizeId;
	}

	public String getPrizeSN() {
		return prizeSN;
	}

	public void setPrizeSN(String prizeSN) {
		this.prizeSN = prizeSN;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public EventPrizeEntity getPrizeEntity() {
		return prizeEntity;
	}

	public void setPrizeEntity(EventPrizeEntity prizeEntity) {
		this.prizeEntity = prizeEntity;
	}
	
	
}

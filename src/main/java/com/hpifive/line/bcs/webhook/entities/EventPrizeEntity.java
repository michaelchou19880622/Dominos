package com.hpifive.line.bcs.webhook.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="event_prize")
public class EventPrizeEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="event_id", insertable=false, updatable=false)
	private Long eventId;
	
	@Column(name="prize_id", insertable=false, updatable=false)
	private Long prizeId;
	
	@Column(name="prize_type")
	private String prizeType;
	
	@Column(name="announce_datetime")
	private Date announceDateTime;
	
	@Column(name="expired_datetime")
	private Date expiredDateTime;
	
	@Column(name="volume")
	private Integer volume;
	
	@Column(name="next_event_prize_id")
	private Long nextEventPrizeId;
	
	@ManyToOne
    @JoinColumn(name="event_id")
    private EventEntity eventEntity;
	
	@OneToMany(cascade=CascadeType.ALL)
    @JoinColumn(name="event_prize_id")
    private List<EventPrizeDetailEntity> prizeDetailEntities = new ArrayList<>();

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

	public Long getPrizeId() {
		return prizeId;
	}

	public void setPrizeId(Long prizeId) {
		this.prizeId = prizeId;
	}

	public String getPrizeType() {
		return prizeType;
	}

	public void setPrizeType(String prizeType) {
		this.prizeType = prizeType;
	}

	public Date getAnnounceDateTime() {
		return announceDateTime;
	}

	public void setAnnounceDateTime(Date announceDateTime) {
		this.announceDateTime = announceDateTime;
	}

	public Date getExpiredDateTime() {
		return expiredDateTime;
	}

	public void setExpiredDateTime(Date expiredDateTime) {
		this.expiredDateTime = expiredDateTime;
	}

	public Integer getVolume() {
		return volume;
	}

	public void setVolume(Integer volume) {
		this.volume = volume;
	}

	public Long getNextEventPrizeId() {
		return nextEventPrizeId;
	}

	public void setNextEventPrizeId(Long nextEventPrizeId) {
		this.nextEventPrizeId = nextEventPrizeId;
	}

	public EventEntity getEventEntity() {
		return eventEntity;
	}

	public void setEventEntity(EventEntity eventEntity) {
		this.eventEntity = eventEntity;
	}

	public List<EventPrizeDetailEntity> getPrizeDetailEntities() {
		return prizeDetailEntities;
	}

	public void setPrizeDetailEntities(List<EventPrizeDetailEntity> prizeDetailEntities) {
		this.prizeDetailEntities = prizeDetailEntities;
	}

	
}

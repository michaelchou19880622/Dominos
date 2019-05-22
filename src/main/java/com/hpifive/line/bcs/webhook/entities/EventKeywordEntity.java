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
@Table(name = "event_keyword")
public class EventKeywordEntity  {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //id自動遞增
    private Long id;
	
	@Column(name = "event_id", insertable=false, updatable=false)
	private Long eventId;
    
	@Column(name = "keyword")
    private String keyword;

    @Column(name = "keyword_event")
    private String keywordEvent;

    @ManyToOne
    @JoinColumn(name="event_id")
    private EventEntity eventEntity;
    
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

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getKeywordEvent() {
		return keywordEvent;
	}

	public void setKeywordEvent(String keywordEvent) {
		this.keywordEvent = keywordEvent;
	}

	public EventEntity getEventEntity() {
		return eventEntity;
	}

	public void setEventEntity(EventEntity eventEntity) {
		this.eventEntity = eventEntity;
	}
	
	
}

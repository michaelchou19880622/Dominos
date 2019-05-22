package com.hpifive.line.bcs.webhook.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@Table(name = "event_applydata_keyword")
public class EventApplydataKeywordEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //id自動遞增
    private Long id;
	
	@Column(name = "event_applydata_id", insertable=false, updatable=false)
	private Long eventApplydataId;
    
    private String keyword;

    @Column(name = "keyword_event")
    private String keywordEvent;

    @ManyToOne
    @JoinColumn(name="event_applydata_id")
    private EventApplydataEntity eventApplydataEntity;
    
    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    @JoinColumn(name="event_applydata_keyword_id")
    @OrderBy("id")
    private List<EventApplydataKeywordMessageListEntity> eventtApplydataKeywordMessageListEntities = new ArrayList<>();
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getEventApplydataId() {
		return eventApplydataId;
	}

	public void setEventApplydataId(Long eventApplydataId) {
		this.eventApplydataId = eventApplydataId;
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

	public EventApplydataEntity getEventApplydataEntity() {
		return eventApplydataEntity;
	}

	public void setEventApplydataEntity(EventApplydataEntity eventApplydataEntity) {
		this.eventApplydataEntity = eventApplydataEntity;
	}

	public List<EventApplydataKeywordMessageListEntity> getEventtApplydataKeywordMessageListEntities() {
		return eventtApplydataKeywordMessageListEntities;
	}

	public void setEventtApplydataKeywordMessageListEntities(
			List<EventApplydataKeywordMessageListEntity> eventtApplydataKeywordMessageListEntities) {
		this.eventtApplydataKeywordMessageListEntities = eventtApplydataKeywordMessageListEntities;
	}
	
	
}

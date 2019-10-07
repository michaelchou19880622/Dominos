package com.hpifive.line.bcs.webhook.entities;

import javax.persistence.*;

@Entity
@Table(name = "event_applydata_keyword_message_list")
public class EventApplydataKeywordMessageListEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //id自動遞增
    private Long id;
	
	@Column(name = "event_applydata_keyword_id", insertable=false, updatable=false)
	private Long eventApplydataKeywordId;
	
	@Column(name = "message_type")
    private String messageType;

	@Column(name = "message_id")
    private int messageId;

	@ManyToOne
    @JoinColumn(name="event_applydata_keyword_id")
    private EventApplydataKeywordEntity eventApplydataKeywordEntity;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getEventApplydataKeywordId() {
		return eventApplydataKeywordId;
	}

	public void setEventApplydataKeywordId(Long eventApplydataKeywordId) {
		this.eventApplydataKeywordId = eventApplydataKeywordId;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public int getMessageId() {
		return messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	public EventApplydataKeywordEntity getEventApplydataKeywordEntity() {
		return eventApplydataKeywordEntity;
	}

	public void setEventApplydataKeywordEntity(EventApplydataKeywordEntity eventApplydataKeywordEntity) {
		this.eventApplydataKeywordEntity = eventApplydataKeywordEntity;
	}

	
	
}

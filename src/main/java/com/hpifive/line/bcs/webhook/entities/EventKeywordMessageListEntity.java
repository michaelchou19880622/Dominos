package com.hpifive.line.bcs.webhook.entities;

import javax.persistence.*;

@Entity
@Table(name = "event_keyword_message_list")
public class EventKeywordMessageListEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //id自動遞增
    private Long id;
	
	@Column(name = "event_keyword_id")
	private Long eventKeywordId;
    
	@Column(name = "message_type")
    private String messageType;

	@Column(name = "message_id")
    private int messageId;

	@Column(name = "order_index")
	private Integer orderIndex;
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getEventKeywordId() {
		return eventKeywordId;
	}

	public void setEventKeywordId(Long eventKeywordId) {
		this.eventKeywordId = eventKeywordId;
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

	public Integer getOrderIndex() {
		return orderIndex;
	}

	public void setOrderIndex(Integer orderIndex) {
		this.orderIndex = orderIndex;
	}
	
}

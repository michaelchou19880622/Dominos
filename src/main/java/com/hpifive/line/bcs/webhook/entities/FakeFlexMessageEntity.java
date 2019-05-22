package com.hpifive.line.bcs.webhook.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="fake_flex_message")
public class FakeFlexMessageEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="event_prize_id")
	private Long eventPrizeId;
	
	@Column(name="content")
	private String content;

	public FakeFlexMessageEntity() {
	}

	public FakeFlexMessageEntity(Long id, Long eventPrizeId, String content) {
		super();
		this.id = id;
		this.eventPrizeId = eventPrizeId;
		this.content = content;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getEventPrizeId() {
		return eventPrizeId;
	}

	public void setEventPrizeId(Long eventPrizeId) {
		this.eventPrizeId = eventPrizeId;
	}
	
}

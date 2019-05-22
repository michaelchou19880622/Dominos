package com.hpifive.line.bcs.webhook.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="flex_message_carousel")
public class FlexMessageCarouselEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="flex_id", nullable=false)
	private Long flexId;
	
	@Column(name="bubble_id", nullable=false)
	private Long bubbleId;
	
	@Column(name="order_index", nullable=false)
	private Integer orderIndex;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getFlexId() {
		return flexId;
	}

	public void setFlexId(Long flexId) {
		this.flexId = flexId;
	}

	public Long getBubbleId() {
		return bubbleId;
	}

	public void setBubbleId(Long bubbleId) {
		this.bubbleId = bubbleId;
	}

	public Integer getOrderIndex() {
		return orderIndex;
	}

	public void setOrderIndex(Integer orderIndex) {
		this.orderIndex = orderIndex;
	}
	
	
}

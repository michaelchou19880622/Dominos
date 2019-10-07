package com.hpifive.line.bcs.webhook.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="flex_message_bubble_component_list")
public class FlexMessageBubbleComponentList {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="bubble_id")
	private Long bubbleId;
	
	@Column(name="bubble_type")
	private String bubbleType;
	
	@Column(name="component_id")
	private Long componentId;
	
	@Column(name="component_type")
	private String componentType;
	
	@Column(name="order_index")
	private Integer orderIndex;
	
	public FlexMessageBubbleComponentList() {
		super();
	}

	public FlexMessageBubbleComponentList(Long id, Long bubbleId, String bubbleType, Long componentId,
			String componentType, Integer orderIndex) {
		super();
		this.id = id;
		this.bubbleId = bubbleId;
		this.bubbleType = bubbleType;
		this.componentId = componentId;
		this.componentType = componentType;
		this.orderIndex = orderIndex;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getBubbleId() {
		return bubbleId;
	}

	public void setBubbleId(Long bubbleId) {
		this.bubbleId = bubbleId;
	}

	public String getBubbleType() {
		return bubbleType;
	}

	public void setBubbleType(String bubbleType) {
		this.bubbleType = bubbleType;
	}

	public Long getComponentId() {
		return componentId;
	}

	public void setComponentId(Long componentId) {
		this.componentId = componentId;
	}

	public String getComponentType() {
		return componentType;
	}

	public void setComponentType(String componentType) {
		this.componentType = componentType;
	}

	public Integer getOrderIndex() {
		return orderIndex;
	}

	public void setOrderIndex(Integer orderIndex) {
		this.orderIndex = orderIndex;
	}

	
}

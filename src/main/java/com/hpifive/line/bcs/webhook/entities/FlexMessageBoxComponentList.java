package com.hpifive.line.bcs.webhook.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="flex_message_box_component_list")
public class FlexMessageBoxComponentList {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="box_id", nullable=false)
	private Long boxId;
	
	@Column(name="component_id", nullable=false)
	private Long componentId;
	
	@Column(name="component_type", nullable=false)
	private String componentType;
	
	@Column(name="order_index")
	private Integer orderIndex;
	
	public FlexMessageBoxComponentList() {
		super();
	}

	public FlexMessageBoxComponentList(Long id, Long bubbleId, Long componentId,
			String componentType, Integer orderIndex) {
		super();
		this.id = id;
		this.boxId = bubbleId;
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

	public Long getBoxId() {
		return boxId;
	}

	public void setBoxId(Long boxId) {
		this.boxId = boxId;
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

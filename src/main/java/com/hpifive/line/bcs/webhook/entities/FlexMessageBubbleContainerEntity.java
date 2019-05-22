package com.hpifive.line.bcs.webhook.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="flex_message_bubble_container")
public class FlexMessageBubbleContainerEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="flex_id")
	private Long flexId;
	
	@Column(name="direction")
	private String direction;
	
	public FlexMessageBubbleContainerEntity(Long id, String direction) {
		super();
		this.id = id;
		this.direction = direction;
	}

	public FlexMessageBubbleContainerEntity() {
		super();
	}

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

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	@Override
	public String toString() {
		return "FlexMessageBubbleContainerEntity [id=" + id + ", direction=" + direction + "]";
	}
	
	
	
}

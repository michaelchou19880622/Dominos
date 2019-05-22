package com.hpifive.line.bcs.webhook.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="flex_message_box_container")
public class FlexMessageBoxContainerEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="layout")
	private String layout;
	
	@Column(name="flex")
	private Integer flex;
	
	@Column(name="spacing")
	private String spacing;
	
	@Column(name="margin")
	private String margin;

	public FlexMessageBoxContainerEntity() {
		super();
	}

	public FlexMessageBoxContainerEntity(Long id, String layout, Integer flex, String spacing, String margin) {
		super();
		this.id = id;
		this.layout = layout;
		this.flex = flex;
		this.spacing = spacing;
		this.margin = margin;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLayout() {
		return layout;
	}

	public void setLayout(String layout) {
		this.layout = layout;
	}

	public Integer getFlex() {
		return flex;
	}

	public void setFlex(Integer flex) {
		this.flex = flex;
	}

	public String getSpacing() {
		return spacing;
	}

	public void setSpacing(String spacing) {
		this.spacing = spacing;
	}

	public String getMargin() {
		return margin;
	}

	public void setMargin(String margin) {
		this.margin = margin;
	}
	
}

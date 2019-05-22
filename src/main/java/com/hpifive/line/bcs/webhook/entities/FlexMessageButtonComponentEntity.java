package com.hpifive.line.bcs.webhook.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="flex_message_button_component")
public class FlexMessageButtonComponentEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="flex")
	private Integer flex;
	
	@Column(name="margin")
	private String margin;
	
	@Column(name="height")
	private String height;
	
	@Column(name="style")
	private String style;
	
	@Column(name="color")
	private String color;
	
	@Column(name="gravity")
	private String gravity;

	public FlexMessageButtonComponentEntity() {
		super();
	}

	public FlexMessageButtonComponentEntity(Long id, Integer flex, String margin, String height, String style, String color,
			String gravity) {
		super();
		this.id = id;
		this.flex = flex;
		this.margin = margin;
		this.height = height;
		this.style = style;
		this.color = color;
		this.gravity = gravity;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getFlex() {
		return flex;
	}

	public void setFlex(Integer flex) {
		this.flex = flex;
	}

	public String getMargin() {
		return margin;
	}

	public void setMargin(String margin) {
		this.margin = margin;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getGravity() {
		return gravity;
	}

	public void setGravity(String gravity) {
		this.gravity = gravity;
	}
	
	
}

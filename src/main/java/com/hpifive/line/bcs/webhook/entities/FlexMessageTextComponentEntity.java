package com.hpifive.line.bcs.webhook.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="flex_message_text_component")
public class FlexMessageTextComponentEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="text", length=300, nullable=false)
	private String text;
	
	@Column(name="flex")
	private Integer flex;
	
	@Column(name="margin")
	private String margin;
	
	@Column(name="size")
	private String size;
	
	@Column(name="align")
	private String align;
	
	@Column(name="gravity")
	private String gravity;
	
	@Column(name="wrap", columnDefinition = "BOOLEAN")
	private Boolean wrap;
	
	@Column(name="weight")
	private String weight;
	
	@Column(name="color")
	private String color;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
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

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getAlign() {
		return align;
	}

	public void setAlign(String align) {
		this.align = align;
	}

	public String getGravity() {
		return gravity;
	}

	public void setGravity(String gravity) {
		this.gravity = gravity;
	}

	public Boolean getWrap() {
		return wrap;
	}

	public void setWrap(Boolean wrap) {
		this.wrap = wrap;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
	
	
	
}

package com.hpifive.line.bcs.webhook.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="flex_message_image_component")
public class FlexMessageImageComponentEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="url", nullable=false)
	private String url;
	
	@Column(name="flex")
	private Integer flex;
	
	@Column(name="margin")
	private String margin;
	
	@Column(name="align")
	private String align;
	
	@Column(name="gravity")
	private String gravity;
	
	@Column(name="size")
	private String size;
	
	@Column(name="aspect_ratio")
	private String aspectRatio;
	
	@Column(name="aspect_mode")
	private String aspectMode;
	
	@Column(name="background_color")
	private String backgroundColor;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getAspectRatio() {
		return aspectRatio;
	}

	public void setAspectRatio(String aspectRatio) {
		this.aspectRatio = aspectRatio;
	}

	public String getAspectMode() {
		return aspectMode;
	}

	public void setAspectMode(String aspectMode) {
		this.aspectMode = aspectMode;
	}

	public String getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
	
	
}

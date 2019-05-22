package com.hpifive.line.bcs.webhook.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="message_carousel_template")
public class MessageCarouselTemplateEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="type")
	private String type;
	
	@Column(name="alt_text")
	private String altText;
	
	@Column(name="image_aspect_ratio")
	private String imageAspectRatio;
	
	@Column(name="image_size")
	private String imageSize;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAltText() {
		return altText;
	}

	public void setAltText(String altText) {
		this.altText = altText;
	}

	public String getImageAspectRatio() {
		return imageAspectRatio;
	}

	public void setImageAspectRatio(String imageAspectRatio) {
		this.imageAspectRatio = imageAspectRatio;
	}

	public String getImageSize() {
		return imageSize;
	}

	public void setImageSize(String imageSize) {
		this.imageSize = imageSize;
	}

	public Integer getId() {
		return id;
	}
	
	
}

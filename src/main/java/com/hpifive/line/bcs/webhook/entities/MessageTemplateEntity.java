package com.hpifive.line.bcs.webhook.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="message_template")
public class MessageTemplateEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="type")
	private String type;
	
	@Column(name="alt_text")
	private String altText;
	
	@Column(name="title")
	private String title;
	
	@Column(name="text")
	private String text;
	
	@Column(name="thumbnail_image_url")
	private String thumbnailImageUrl;
	
	@Column(name="image_aspect_ratio")
	private String imageAspectRatio;
	
	@Column(name="image_size")
	private String imageSize;
	
	@Column(name="image_background_color")
	private String imageBackgroundColor;

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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getThumbnailImageUrl() {
		return thumbnailImageUrl;
	}

	public void setThumbnailImageUrl(String thumbnailImageUrl) {
		this.thumbnailImageUrl = thumbnailImageUrl;
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

	public String getImageBackgroundColor() {
		return imageBackgroundColor;
	}

	public void setImageBackgroundColor(String imageBackgroundColor) {
		this.imageBackgroundColor = imageBackgroundColor;
	}

	public Integer getId() {
		return id;
	}
	
	
}

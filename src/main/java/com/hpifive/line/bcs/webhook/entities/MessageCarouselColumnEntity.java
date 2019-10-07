package com.hpifive.line.bcs.webhook.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="message_carousel_column")
public class MessageCarouselColumnEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="carousel_id")
	private Integer carouselId;
	
	@Column(name="type")
	private String type;
	
	@Column(name="title")
	private String title;
	
	@Column(name="text")
	private String text;
	
	@Column(name="thumbnail_image_url")
	private String thumbnailImageUrl;
	
	@Column(name="image_background_color")
	private String imageBackgroundColor;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

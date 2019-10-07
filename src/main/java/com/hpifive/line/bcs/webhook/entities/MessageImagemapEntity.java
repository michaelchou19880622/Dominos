package com.hpifive.line.bcs.webhook.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="message_imagemap")
public class MessageImagemapEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="base_url")
	private String baseUrl;
	
	@Column(name="alt_text")
	private String altText;
	
	@Column(name="base_size_width")
	private Integer baseSizeWidth;
	
	@Column(name="base_size_height")
	private Integer baseSizeHeight;

	
	public String getBaseUrl() {
		return baseUrl;
	}


	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}


	public String getAltText() {
		return altText;
	}


	public void setAltText(String altText) {
		this.altText = altText;
	}


	public Integer getBaseSizeWidth() {
		return baseSizeWidth;
	}


	public void setBaseSizeWidth(Integer baseSizeWidth) {
		this.baseSizeWidth = baseSizeWidth;
	}


	public Integer getBaseSizeHeight() {
		return baseSizeHeight;
	}


	public void setBaseSizeHeight(Integer baseSizeHeight) {
		this.baseSizeHeight = baseSizeHeight;
	}


	public Integer getId() {
		return id;
	}
	
	
}

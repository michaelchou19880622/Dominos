package com.hpifive.line.bcs.webhook.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="message_imagemap_action")
public class MessageImagemapActionEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="message_id")
	private Integer messageId;
	
	@Column(name="type")
	private String type;
	
	@Column(name="label")
	private String label;
	
	@Column(name="link_uri")
	private String linkUri;
	
	@Column(name="text")
	private String text;
	
	@Column(name="area_x")
	private Integer areaX;
	
	@Column(name="area_y")
	private Integer areaY;
	
	@Column(name="area_width")
	private Integer areaWidth;
	
	@Column(name="area_height")
	private Integer areaHeight;


	public Integer getMessageId() {
		return messageId;
	}


	public void setMessageId(Integer messageId) {
		this.messageId = messageId;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getLabel() {
		return label;
	}


	public void setLabel(String label) {
		this.label = label;
	}


	public String getLinkUri() {
		return linkUri;
	}


	public void setLinkUri(String linkUri) {
		this.linkUri = linkUri;
	}


	public String getText() {
		return text;
	}


	public void setText(String text) {
		this.text = text;
	}


	public Integer getAreaX() {
		return areaX;
	}


	public void setAreaX(Integer areaX) {
		this.areaX = areaX;
	}


	public Integer getAreaY() {
		return areaY;
	}


	public void setAreaY(Integer areaY) {
		this.areaY = areaY;
	}


	public Integer getAreaWidth() {
		return areaWidth;
	}


	public void setAreaWidth(Integer areaWidth) {
		this.areaWidth = areaWidth;
	}


	public Integer getAreaHeight() {
		return areaHeight;
	}


	public void setAreaHeight(Integer areaHeight) {
		this.areaHeight = areaHeight;
	}


	public Integer getId() {
		return id;
	}
	
	
}

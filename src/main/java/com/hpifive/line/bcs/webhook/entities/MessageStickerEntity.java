package com.hpifive.line.bcs.webhook.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.linecorp.bot.model.message.Message;

@Entity
@Table(name="message_sticker")
@JsonTypeName("sticker")
public class MessageStickerEntity implements Message {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@JsonIgnore
	private Integer id;
	
	@Column(name="sticker_id", nullable=false)
	private String stickerId;

	@Column(name="package_id", nullable=false)
	private String packageId;

	public Integer getId() {
		return id;
	}

	public String getStickerId() {
		return stickerId;
	}

	public void setStickerId(String stickerId) {
		this.stickerId = stickerId;
	}

	public String getPackageId() {
		return packageId;
	}

	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}
	
	
}

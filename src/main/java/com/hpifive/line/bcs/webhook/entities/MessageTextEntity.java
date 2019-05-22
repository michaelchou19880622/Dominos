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
@Table(name="message_text")
@JsonTypeName("text")
public class MessageTextEntity implements Message {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@JsonIgnore
	private Integer id;

	@Column(name="text", nullable=true)
	private String text;

	public MessageTextEntity() {
		//		default Constructor
	}
	
	public MessageTextEntity(String text) {
		this.text = text;
	}

	public Integer getId() {
		return id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
}

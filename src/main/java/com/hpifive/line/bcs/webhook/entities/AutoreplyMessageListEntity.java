package com.hpifive.line.bcs.webhook.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="autoreply_message_list")
public class AutoreplyMessageListEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@Column(name="autoreply_id", nullable=false)
	private long autoreplyID;
	
	@Column(name="message_id", nullable=false)
	private Integer messageID;
	
	@Column(name="message_type", nullable=false)
	private String messageType;
	
	@Column(name="order_num", nullable=false)
	private Integer orderNum;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getAutoreplyID() {
		return autoreplyID;
	}

	public void setAutoreplyID(long autoreplyID) {
		this.autoreplyID = autoreplyID;
	}

	public Integer getMessageID() {
		return messageID;
	}

	public void setMessageID(Integer messageID) {
		this.messageID = messageID;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}
	
	
}

package com.hpifive.line.bcs.webhook.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="send_message_users_push_job")
public class SendMessageUsersPushJobEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="send_id")
	private Long sendId;
	
	@Column(name="index_value")
	private Long index;

	public SendMessageUsersPushJobEntity() {
		super();
	}

	public SendMessageUsersPushJobEntity(Long id, Long sendId, Long index) {
		super();
		this.id = id;
		this.sendId = sendId;
		this.index = index;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSendId() {
		return sendId;
	}

	public void setSendId(Long sendId) {
		this.sendId = sendId;
	}

	public Long getIndex() {
		return index;
	}

	public void setIndex(Long index) {
		this.index = index;
	}

	@Override
	public String toString() {
		return "SendMessageUsersPushJobEntity [id=" + id + ", sendId=" + sendId + ", index=" + index + "]";
	}
	
	
}

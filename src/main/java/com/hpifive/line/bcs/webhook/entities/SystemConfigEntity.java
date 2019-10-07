package com.hpifive.line.bcs.webhook.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="system_config")
public class SystemConfigEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="config_key", nullable=false)
	private String key;
	
	@Column(name="config_value", nullable=false)
	private String value;
	
	@Column(name="modify_time", nullable=false)
	private Date modifyTime;

	public SystemConfigEntity(Integer id, String key, String value, Date modifyTime) {
		super();
		this.id = id;
		this.key = key;
		this.value = value;
		this.modifyTime = modifyTime;
	}

	public SystemConfigEntity() {
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	
}

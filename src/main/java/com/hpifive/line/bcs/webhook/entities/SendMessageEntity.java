package com.hpifive.line.bcs.webhook.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="send_message")
public class SendMessageEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="mode")
	private String mode;
	
	@Column(name="mode_week")
	private String modeWeek;
	
	@Column(name="mode_day")
	private Integer modeDay;
	
	@Column(name="mode_hour")
	private Integer modeHour;
	
	@Column(name="mode_min")
	private Integer modeMin;
	
	@Column(name="group_id")
	private Long groupId;
	
	@Column(name="schedule")
	@Temporal(TemporalType.TIMESTAMP)
	private Date schedule;
	
	@Column(name="category")
	private String category;
	
	@Column(name="lineuser_count")
	private Integer count;
	
	@Column(name="lineuser_ok_count")
	private Integer successfulCount;
	
	@Column(name="status")
	private Integer status;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="creation_time", nullable=false, insertable=false, updatable=false, columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Date creationTime;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="modify_time", nullable=false, insertable=false, updatable=false, columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
	private Date modificationTime;
	
	@Column(name="modify_account")
	private String modifyAccount;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getModeWeek() {
		return modeWeek;
	}

	public void setModeWeek(String modeWeek) {
		this.modeWeek = modeWeek;
	}

	public Integer getModeDay() {
		return modeDay;
	}

	public void setModeDay(Integer modeDay) {
		this.modeDay = modeDay;
	}

	public Integer getModeHour() {
		return modeHour;
	}

	public void setModeHour(Integer modeHour) {
		this.modeHour = modeHour;
	}

	public Integer getModeMin() {
		return modeMin;
	}

	public void setModeMin(Integer modeMin) {
		this.modeMin = modeMin;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public Date getSchedule() {
		return schedule;
	}

	public void setSchedule(Date schedule) {
		this.schedule = schedule;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getSuccessfulCount() {
		return successfulCount;
	}

	public void setSuccessfulCount(Integer successfulCount) {
		this.successfulCount = successfulCount;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date getModificationTime() {
		return modificationTime;
	}

	public void setModificationTime(Date modificationTime) {
		this.modificationTime = modificationTime;
	}

	public String getModifyAccount() {
		return modifyAccount;
	}

	public void setModifyAccount(String modifyAccount) {
		this.modifyAccount = modifyAccount;
	}
	
	
}

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
@Table(name="autoreply")
public class AutoreplyEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="keyword")
	private String data;
	
	@Column(name="type")
	private String type;
	
	@Column(name="status")
	private String status;
	
	@Column(name="user_status")
	private String userStatus;
	
	@Column(name="reply_index")
	private Integer replyIndex;
	
	@Column(name="period")
	private String period;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="datetime_begin")
	private Date beginTime;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="datetime_end")
	private Date endTime;
	
	@Column(name="response_count")
	private Integer responseCount;
	
	@Column(name="memo")
	private String memo;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="creation_time", nullable=false, insertable=false, updatable=false, columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Date creationTime;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="modification_time", nullable=false, insertable=false, updatable=false, columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
	private Date modificationTime;
	
	@Column(name="modify_user")
	private String modifyUser;

	public AutoreplyEntity() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}

	public Integer getReplyIndex() {
		return replyIndex;
	}

	public void setReplyIndex(Integer replyIndex) {
		this.replyIndex = replyIndex;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Integer getResponseCount() {
		return responseCount;
	}

	public void setResponseCount(Integer responseCount) {
		this.responseCount = responseCount;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
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

	public String getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}
	
	
}

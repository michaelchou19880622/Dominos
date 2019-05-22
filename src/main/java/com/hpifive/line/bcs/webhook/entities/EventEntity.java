package com.hpifive.line.bcs.webhook.entities;

import org.springframework.data.annotation.LastModifiedDate;

import com.hpifive.line.bcs.webhook.entities.config.EventTypes;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "event")
public class EventEntity {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(name="event_prize_id")
	private Long eventPrizeId;
	
	@Enumerated(EnumType.STRING)
	@Column(name="type")
	private EventTypes type;
	
	@Column(name="name")
	private String name;
    
	@Column(name="description")
    private String description;
    
    @Column(name = "user_status")
    private String userStatus;
    
    @Column(name = "attendance_count")
    private Integer attendanceCount;
    
    @Column(name = "begin_datetime")
    private Date beginDatetime;
    
    @Column(name = "end_datetime")
    private Date endDatetime;
    
    @Column(name = "input_error_time")
    private Integer inputErrorTime;
    
    @Column(name = "input_timeout")
    private Integer inputTimeout;
    
    @Column(name = "modify_user")
    private String modifyUser;
    
    @Column(name = "modify_time")
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date modifyTime;

    @Column(name="minimum_spend_amount")
    private Integer minimumSpendAmount;
    
    @OneToMany(cascade=CascadeType.ALL)
    @JoinColumn(name="event_id")
    private List<EventKeywordEntity> eventKeywordEntities = new ArrayList<>();
    
    @OneToMany(cascade=CascadeType.ALL)
    @JoinColumn(name="event_id")
    private List<EventApplydataEntity> eventApplydataEntities = new ArrayList<>();
    
    @OneToMany(cascade=CascadeType.ALL)
    @JoinColumn(name="event_id")
    private List<EventPrizeEntity> prizeEntities = new ArrayList<>();
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getEventPrizeId() {
		return eventPrizeId;
	}

	public void setEventPrizeId(Long eventPrizeId) {
		this.eventPrizeId = eventPrizeId;
	}

	public EventTypes getType() {
		return type;
	}

	public void setType(EventTypes type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}

	public Integer getAttendanceCount() {
		return attendanceCount;
	}

	public void setAttendanceCount(Integer attendanceCount) {
		this.attendanceCount = attendanceCount;
	}

	public Date getBeginDatetime() {
		return beginDatetime;
	}

	public void setBeginDatetime(Date beginDatetime) {
		this.beginDatetime = beginDatetime;
	}

	public Date getEndDatetime() {
		return endDatetime;
	}

	public void setEndDatetime(Date endDatetime) {
		this.endDatetime = endDatetime;
	}

	public Integer getInputErrorTime() {
		return inputErrorTime;
	}

	public void setInputErrorTime(Integer inputErrorTime) {
		this.inputErrorTime = inputErrorTime;
	}

	public Integer getInputTimeout() {
		return inputTimeout;
	}

	public void setInputTimeout(Integer inputTimeout) {
		this.inputTimeout = inputTimeout;
	}

	public String getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public Integer getMinimumSpendAmount() {
		return minimumSpendAmount;
	}

	public void setMinimumSpendAmount(Integer minimumSpendAmount) {
		this.minimumSpendAmount = minimumSpendAmount;
	}

	public List<EventKeywordEntity> getEventKeywordEntities() {
		return eventKeywordEntities;
	}

	public void setEventKeywordEntities(List<EventKeywordEntity> eventKeywordEntities) {
		this.eventKeywordEntities = eventKeywordEntities;
	}

	public List<EventApplydataEntity> getEventApplydataEntities() {
		return eventApplydataEntities;
	}

	public void setEventApplydataEntities(List<EventApplydataEntity> eventApplydataEntities) {
		this.eventApplydataEntities = eventApplydataEntities;
	}

	public List<EventPrizeEntity> getPrizeEntities() {
		return prizeEntities;
	}

	public void setPrizeEntities(List<EventPrizeEntity> prizeEntities) {
		this.prizeEntities = prizeEntities;
	}

	@Override
	public String toString() {
		return "EventEntity [id=" + id + ", type=" + type + ", name=" + name + ", description=" + description
				+ ", userStatus=" + userStatus + ", attendanceCount=" + attendanceCount + ", beginDatetime="
				+ beginDatetime + ", endDatetime=" + endDatetime + ", inputErrorTime=" + inputErrorTime
				+ ", inputTimeout=" + inputTimeout + ", modifyUser=" + modifyUser + ", modifyTime=" + modifyTime
				+ ", eventKeywordEntities=" + eventKeywordEntities + ", eventApplydataEntities="
				+ eventApplydataEntities + ", prizeEntities=" + prizeEntities + "]";
	}
	
	
	
}

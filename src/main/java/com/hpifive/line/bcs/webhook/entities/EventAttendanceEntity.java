package com.hpifive.line.bcs.webhook.entities;

import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity
@Table(name="event_attendance")
public class EventAttendanceEntity {


	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="event_id")
	private Long eventId;
	
	@Column(name="lineuser_id")
	private Long userId;
	
	@Column(name="apply_date")
	private ZonedDateTime applyDate;
	
	@Column(name="apply_status")
	private Integer applyStatus;
	
	@Column(name="input_error_count")
	private Integer inputErrorCount;
	
	@Column(name="get_ticket")
	private String getTicker;
	
	@Column(name="get_ticket_datetime")
	private Date getTicketDate;
	
	@Column(name="checkin_date")
	private Date checkinDate;
	
	@Column(name="checkin_employee")
	private String checkinEmployee;
	
	@Column(name="getprize_date")
	private Date getPrizeDate;
	
	@Column(name="getprize_employee")
	private String getprizeEmployee;

	@OneToMany(orphanRemoval=true, fetch=FetchType.LAZY)
	@OnDelete(action=OnDeleteAction.CASCADE)
	@JoinColumn(name="event_attendance_id")
	private List<EventAttendanceAttributeEntity> attributeEntities = new ArrayList<>();
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public ZonedDateTime getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(ZonedDateTime applyDate) {
		this.applyDate = applyDate;
	}

	public Integer getApplyStatus() {
		return applyStatus;
	}

	public void setApplyStatus(Integer applyStatus) {
		this.applyStatus = applyStatus;
	}
	
	public Integer getInputErrorCount() {
		return inputErrorCount;
	}

	public void setInputErrorCount(Integer inputErrorCount) {
		this.inputErrorCount = inputErrorCount;
	}

	public String getGetTicker() {
		return getTicker;
	}

	public void setGetTicker(String getTicker) {
		this.getTicker = getTicker;
	}

	public Date getGetTicketDate() {
		return getTicketDate;
	}

	public void setGetTicketDate(Date getTicketDate) {
		this.getTicketDate = getTicketDate;
	}

	public Date getCheckinDate() {
		return checkinDate;
	}

	public void setCheckinDate(Date checkinDate) {
		this.checkinDate = checkinDate;
	}

	public String getCheckinEmployee() {
		return checkinEmployee;
	}

	public void setCheckinEmployee(String checkinEmployee) {
		this.checkinEmployee = checkinEmployee;
	}

	public Date getGetPrizeDate() {
		return getPrizeDate;
	}

	public void setGetPrizeDate(Date getPrizeDate) {
		this.getPrizeDate = getPrizeDate;
	}

	public String getGetprizeEmployee() {
		return getprizeEmployee;
	}

	public void setGetprizeEmployee(String getprizeEmployee) {
		this.getprizeEmployee = getprizeEmployee;
	}
	
	
}

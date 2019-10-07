package com.hpifive.line.bcs.webhook.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;


@Entity
@Table(name = "event_applydata")
public class EventApplydataEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //id自動遞增
    private Long id;
	
	@Column(name = "event_id", insertable=false, updatable=false)
	private Long eventId;
    
	@Column(name = "column_name")
    private String columnName;

	@Column(name = "column_key")
    private String columnKey;
    
	@Column(name = "column_format")
    private String columnFormat;
    
	@Column(name = "column_length")
    private Integer columnLength;
    
	@Column(name = "order_index")
    private Integer orderIndex;
    
    private String status;
    
    private String keyword;

    @ManyToOne
    @JoinColumn(name="event_id")
    private EventEntity eventEntity;
    
    @OneToMany(cascade=CascadeType.ALL)
    @JoinColumn(name="event_applydata_id")
    private List<EventApplydataKeywordEntity> eventtApplydataKeywordEntities = new ArrayList<>();
    
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

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getColumnKey() {
		return columnKey;
	}

	public void setColumnKey(String columnKey) {
		this.columnKey = columnKey;
	}

	public String getColumnFormat() {
		return columnFormat;
	}

	public void setColumnFormat(String columnFormat) {
		this.columnFormat = columnFormat;
	}

	public Integer getColumnLength() {
		return columnLength;
	}

	public void setColumnLength(Integer columnLength) {
		this.columnLength = columnLength;
	}

	public Integer getOrderIndex() {
		return orderIndex;
	}

	public void setOrderIndex(Integer orderIndex) {
		this.orderIndex = orderIndex;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public EventEntity getEventEntity() {
		return eventEntity;
	}

	public void setEventEntity(EventEntity eventEntity) {
		this.eventEntity = eventEntity;
	}

	public List<EventApplydataKeywordEntity> getEventtApplydataKeywordEntities() {
		return eventtApplydataKeywordEntities;
	}

	public void setEventtApplydataKeywordEntities(List<EventApplydataKeywordEntity> eventtApplydataKeywordEntities) {
		this.eventtApplydataKeywordEntities = eventtApplydataKeywordEntities;
	}

	
	
}

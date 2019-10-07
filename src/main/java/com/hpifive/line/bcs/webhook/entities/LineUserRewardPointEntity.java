package com.hpifive.line.bcs.webhook.entities;

import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="lineuser_reward_point")
public class LineUserRewardPointEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="lineuser_reward_id")
	private Long rewardId;
	
	@Column(name="reward_card_point")
	private Integer point;
	
	@Column(name="point_source")
	private String source;
	
	@Column(name="point_source_id")
	private Long sourceId;
	
	@Column(name="point_operation")
	private String operation;
	
	@Column(name="create_Time")
	private ZonedDateTime createTime;

	public LineUserRewardPointEntity() {
		super();
	}

	public LineUserRewardPointEntity(Long id, Long rewardId, Integer point, String source, Long sourceId,
			String operation, ZonedDateTime createTime) {
		super();
		this.id = id;
		this.rewardId = rewardId;
		this.point = point;
		this.source = source;
		this.sourceId = sourceId;
		this.operation = operation;
		this.createTime = createTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRewardId() {
		return rewardId;
	}

	public void setRewardId(Long rewardId) {
		this.rewardId = rewardId;
	}

	public Integer getPoint() {
		return point;
	}

	public void setPoint(Integer point) {
		this.point = point;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Long getSourceId() {
		return sourceId;
	}

	public void setSourceId(Long sourceId) {
		this.sourceId = sourceId;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public ZonedDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(ZonedDateTime createTime) {
		this.createTime = createTime;
	}
	
	
}

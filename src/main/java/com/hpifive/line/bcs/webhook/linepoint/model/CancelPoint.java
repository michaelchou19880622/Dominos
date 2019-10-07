package com.hpifive.line.bcs.webhook.linepoint.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class CancelPoint implements PointMessage {
	
	private final String clientId;
	private final String memberId;
	private final String orderKey;
	private final Integer amount;
	private final String note;
	
	@JsonCreator
	public CancelPoint(
			@JsonProperty("clientId") String clientId, 
			@JsonProperty("memberId") String memberId, 
			@JsonProperty("orderKey") String orderKey, 
			@JsonProperty("amount") Integer amount, 
			@JsonProperty("note") String note) {
		super();
		this.clientId = clientId;
		this.memberId = memberId;
		this.orderKey = orderKey;
		this.amount = amount;
		this.note = note;
	}
	
}
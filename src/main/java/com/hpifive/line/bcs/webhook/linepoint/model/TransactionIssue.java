package com.hpifive.line.bcs.webhook.linepoint.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hpifive.line.bcs.webhook.linepoint.unit.TransactionType;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class TransactionIssue {

	private String transactionId;
	private Long transactionTime;
	private TransactionType transactionType;
	private Integer transactionAmount;
	private Integer balance;
	
	@JsonCreator
	public TransactionIssue(
			@JsonProperty("transactionId") String transactionId, 
			@JsonProperty("transactionTime") Long transactionTime,
			@JsonProperty("transactionType") TransactionType transactionType,
			@JsonProperty("transactionAmount") Integer transactionAmount,
			@JsonProperty("balance") Integer balance) {
		super();
		this.transactionId = transactionId;
		this.transactionTime = transactionTime;
		this.transactionType = transactionType;
		this.transactionAmount = transactionAmount;
		this.balance = balance;
	}
}

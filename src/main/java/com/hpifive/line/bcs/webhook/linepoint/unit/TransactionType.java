package com.hpifive.line.bcs.webhook.linepoint.unit;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum TransactionType {
	
	@JsonProperty("ISSUE_ALL")
	ISSUE_ALL,
	@JsonProperty("ISSUE")
	ISSUE,
	@JsonProperty("ISSUE_CANCEL")
	ISSUE_CANCEL;
}

package com.hpifive.line.bcs.webhook.linepoint.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;

@JsonSubTypes({
	@JsonSubTypes.Type(IssuePoint.class),
	@JsonSubTypes.Type(CancelPoint.class)
})
public interface PointMessage {

}

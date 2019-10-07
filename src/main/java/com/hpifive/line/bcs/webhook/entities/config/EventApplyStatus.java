package com.hpifive.line.bcs.webhook.entities.config;

public enum EventApplyStatus {
	INIT(1),
	APPLY_COMPLETED(98),
	APPLY_COMPLETED_YES(99),
	WAIT_CONFIRM(100),
	CONFIRM(101),
	REJECT(102);
	
	private Integer value;

	private EventApplyStatus(Integer value) {
		this.value = value;
	}

	public Integer getValue() {
		return value;
	}
	
	public static EventApplyStatus fromValue(Integer v) {
	    for (EventApplyStatus b : EventApplyStatus.values()) {
	      if (b.value == v) {
	        return b;
	      }
	    }
	    throw new IllegalArgumentException("No constant with value " + v + " found");
	}
	
}

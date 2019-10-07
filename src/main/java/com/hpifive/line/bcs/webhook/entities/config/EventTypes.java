package com.hpifive.line.bcs.webhook.entities.config;

public enum EventTypes {

	DEFAULT,
	TICKET,
	ITERATE,
	CONFIRM,
	INVOICE;

	public static EventTypes fromValue(String v) {
	    for (EventTypes b : EventTypes.values()) {
	    	if (b.toString().equals(v)) {
	    		return b;
	    	}
	    }
	    return null;
	}
	
}

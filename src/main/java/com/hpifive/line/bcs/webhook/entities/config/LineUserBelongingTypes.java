package com.hpifive.line.bcs.webhook.entities.config;

public enum LineUserBelongingTypes {

	PRIZE,
	ATTEND;

	public static LineUserBelongingTypes fromValue(String v) {
	    for (LineUserBelongingTypes b : LineUserBelongingTypes.values()) {
	    	if (b.toString().equals(v)) {
	    		return b;
	    	}
	    }
	    return null;
	}
	
}

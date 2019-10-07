package com.hpifive.line.bcs.webhook.akka.body;

import java.util.Map;

import com.hpifive.line.bcs.webhook.entities.config.KeywordEventTypes;

public class EventKeywordPushBody {

	private Long eventId;
	private Map<String, Object> userMap;
	private KeywordEventTypes keywordType;
	
	public EventKeywordPushBody() {
		super();
	}
	
	public EventKeywordPushBody(Long eventId, Map<String, Object> userMap, KeywordEventTypes keywordType) {
		super();
		this.eventId = eventId;
		this.userMap = userMap;
		this.keywordType = keywordType;
	}


	public Long getEventId() {
		return eventId;
	}
	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public Map<String, Object> getUserMap() {
		return userMap;
	}

	public void setUserMap(Map<String, Object> userMap) {
		this.userMap = userMap;
	}

	public KeywordEventTypes getKeywordType() {
		return keywordType;
	}
	public void setKeywordType(KeywordEventTypes keywordType) {
		this.keywordType = keywordType;
	}

	@Override
	public String toString() {
		return "EventKeywordPushBody [eventId=" + eventId + ", userMap=" + userMap + ", keywordType=" + keywordType
				+ "]";
	}
	
}

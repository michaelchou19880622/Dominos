package com.hpifive.line.bcs.webhook.akka.body;

import com.hpifive.line.bcs.webhook.entities.config.EventTypes;
import com.hpifive.line.bcs.webhook.entities.config.KeywordEventTypes;

public class EventRegisterMsg {

	private String replyToken;
	private String keyword;
	private Long eventId;
	private Long userId;
	private Long attendanceId;
	private Integer applyStatus;
	private String userUid;
	private EventTypes eventTypes;
	private KeywordEventTypes keywordEventTypes;

	public EventRegisterMsg() {
		super();
	}
	
	public EventRegisterMsg(Long eventId, Long userId, Integer applyStatus,
			String userUid, String keyword, KeywordEventTypes keywordEventTypes) {
		super();
		this.eventId = eventId;
		this.userId = userId;
		this.applyStatus = applyStatus;
		this.userUid = userUid;
		this.keyword = keyword;
		this.keywordEventTypes = keywordEventTypes;
	}
	
	public EventRegisterMsg(String replyToken, Long eventId, Long userId, Integer applyStatus,
			String userUid, String keyword, KeywordEventTypes keywordEventTypes) {
		super();
		this.replyToken = replyToken;
		this.eventId = eventId;
		this.userId = userId;
		this.applyStatus = applyStatus;
		this.userUid = userUid;
		this.keyword = keyword;
		this.keywordEventTypes = keywordEventTypes;
	}

	public String getReplyToken() {
		return replyToken;
	}

	public void setReplyToken(String replyToken) {
		this.replyToken = replyToken;
	}

	public Integer getApplyStatus() {
		return applyStatus;
	}

	public void setApplyStatus(Integer applyStatus) {
		this.applyStatus = applyStatus;
	}

	public KeywordEventTypes getKeywordEventTypes() {
		return keywordEventTypes;
	}

	public void setKeywordEventTypes(KeywordEventTypes keywordEventTypes) {
		this.keywordEventTypes = keywordEventTypes;
	}

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserUid() {
		return userUid;
	}

	public void setUserUid(String userUid) {
		this.userUid = userUid;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public EventTypes getEventTypes() {
		return eventTypes;
	}

	public void setEventTypes(EventTypes eventTypes) {
		this.eventTypes = eventTypes;
	}

	public Long getAttendanceId() {
		return attendanceId;
	}

	public void setAttendanceId(Long attendanceId) {
		this.attendanceId = attendanceId;
	}
	
}

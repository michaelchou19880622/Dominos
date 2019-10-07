package com.hpifive.line.bcs.webhook.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hpifive.line.bcs.webhook.akka.AkkaService;
import com.hpifive.line.bcs.webhook.akka.body.EventKeywordPushBody;
import com.hpifive.line.bcs.webhook.dao.EventAttendanceDao;
import com.hpifive.line.bcs.webhook.dao.UserDao;
import com.hpifive.line.bcs.webhook.entities.EventAttendanceEntity;
import com.hpifive.line.bcs.webhook.entities.config.EventApplyStatus;
import com.hpifive.line.bcs.webhook.entities.config.KeywordEventTypes;

@RestController
@RequestMapping(path="/event/push")
public class EventKeywordSendController {
	
	
	private static final Logger logger = LoggerFactory.getLogger(EventKeywordSendController.class);

	@Autowired
	private AkkaService akkaService;
	@Autowired
	private UserDao userDao;
	@Autowired
	private EventAttendanceDao attendanceDao;
	
	@RequestMapping(value="/{eventId}/{uuid}", method=RequestMethod.GET)
	public ResponseEntity<Object> sendByKeyword(
			@PathVariable("eventId") Long eventId,
			@PathVariable("uuid") String uuid
			) {
		KeywordEventTypes type = null;
		try {
			Map<String, Object> userMap = new HashMap<>();
			Long userId = this.userDao.getIdByUid(uuid);
			EventAttendanceEntity entity = attendanceDao.getByUserIdAndEventId(userId, eventId);
			type = KeywordEventTypes.fromString(EventApplyStatus.fromValue(entity.getApplyStatus()).toString());
			userMap.put(userId.toString(), uuid);
			this.akkaService.reply(new EventKeywordPushBody(eventId, userMap, type));
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			logger.error("SendMsgByKeyword with eventId {} KeywordEvent {} UUID {}", eventId, type, uuid, e);
		}
		return ResponseEntity.badRequest().build();
	}

}

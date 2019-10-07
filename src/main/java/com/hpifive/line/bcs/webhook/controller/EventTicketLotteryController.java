package com.hpifive.line.bcs.webhook.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hpifive.line.bcs.webhook.common.ErrorResponseObject;
import com.hpifive.line.bcs.webhook.controller.body.KeyValueBody;
import com.hpifive.line.bcs.webhook.dao.EventAttendanceDao;
import com.hpifive.line.bcs.webhook.entities.config.EventApplyStatus;
import com.hpifive.line.bcs.webhook.entities.config.LineUserStatus;

@RestController
@RequestMapping(path="/event/prize/lottery")
public class EventTicketLotteryController {
	
	private static final Logger logger = LoggerFactory.getLogger(EventTicketLotteryController.class);
	
	@Autowired
	private EventAttendanceDao attendanceDao;
	
	@GetMapping
	public ResponseEntity<Object> lottery(
			@RequestParam(value="id", required=false) Long eventId,
			@RequestParam(value="prizeId", required=false) Long eventPrizeId, 
			@RequestParam(value="status", required=false) Integer applyStatus, 
			@RequestParam(value="volume", required=false) Integer volume,
			@RequestBody List<KeyValueBody<String>> filters
			) {
		try {
			applyStatus = applyStatus==null ? EventApplyStatus.WAIT_CONFIRM.getValue() : applyStatus;
			List<Long> expiredUsers = this.attendanceDao.getExpiredUserIdByEventIdAndApplyStatus(eventId, applyStatus);
			this.attendanceDao.setApplyStatusByEventIdAndUserIds(eventId, expiredUsers, EventApplyStatus.REJECT);
			List<Long> nonPrizeUserIds = this.attendanceDao.getNonPrizeAndNonBlockUserIdByAttr(filters, eventId, eventPrizeId, EventApplyStatus.CONFIRM.getValue(), LineUserStatus.NORMALLY.getValue(), volume);
			return ResponseEntity.ok(nonPrizeUserIds);
		} catch (Exception e) {
			logger.error("[GET] lottery", e);
			return ResponseEntity.badRequest().body(ErrorResponseObject.message(e.getMessage()));
		}
	}

}

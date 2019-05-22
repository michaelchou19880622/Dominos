package com.hpifive.line.bcs.webhook.controller;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hpifive.line.bcs.webhook.common.DateTimeModel;
import com.hpifive.line.bcs.webhook.controller.body.ExpiredKeyValueBody;
import com.hpifive.line.bcs.webhook.dao.EventConfirmDao;
import com.hpifive.line.bcs.webhook.entities.config.EventApplyStatus;
import com.hpifive.line.bcs.webhook.entities.config.KeywordEventTypes;
import com.hpifive.line.bcs.webhook.entities.config.LineUserStatus;
import com.hpifive.line.bcs.webhook.exception.ControllerException;
import com.hpifive.line.bcs.webhook.service.AttendanceSenderService;

@RestController
@RequestMapping(path="/event/check/lottery")
public class EventDoubleCheckLotteryController {
	
	private static final Logger logger = LoggerFactory.getLogger(EventDoubleCheckLotteryController.class);
	@Autowired
	private EventConfirmDao confirmDao;
	@Autowired
	private AttendanceSenderService attendanceSenderService;

	@PostMapping
	public ResponseEntity<Object> lotterys(@RequestParam(value="id", required=false) Long eventId, 
			@RequestParam(value="volume", required=false) Integer volume,
			@RequestParam(value="status", required=false) Integer status,
			@RequestParam(value="targetStatus", required=false) Integer ts,
			@RequestBody ExpiredKeyValueBody<String> body
			) {
		volume = volume == null ? 1 : volume;
		if (body == null) {
			body = new ExpiredKeyValueBody<>();
		}
		try {
			ZonedDateTime expireTime = DateTimeModel.format(body.getExpire());
			if (expireTime == null) {
				throw ControllerException.message("parameter expire format error");
			}
			EventApplyStatus applyStatus = status == null ? EventApplyStatus.APPLY_COMPLETED_YES : EventApplyStatus.fromValue(status);
			EventApplyStatus targetStatus = ts == null ? EventApplyStatus.WAIT_CONFIRM : EventApplyStatus.fromValue(ts);
			KeywordEventTypes targetEventTypes = KeywordEventTypes.fromString(targetStatus.toString());
			List<Long> affectedUserIds = this.attendanceSenderService.pushStatusByfilterUsers(body, eventId, applyStatus, targetEventTypes, LineUserStatus.NORMALLY.getValue(), volume);
			this.confirmDao.addExpireTime(eventId, affectedUserIds, expireTime);
			Map<String, Integer> result = new HashMap<>();
			result.put("AFFECTED", affectedUserIds.size());
			logger.info("ApplyStatus {}", applyStatus);
			logger.info("targetEventTypes {}", targetEventTypes);
			logger.info("body {}", body);
			return ResponseEntity.ok(result);
		} catch (Exception e) {
			logger.error("錯誤拉 ", e);
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

}

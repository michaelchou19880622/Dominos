package com.hpifive.line.bcs.webhook.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hpifive.line.bcs.webhook.common.ErrorResponseObject;
import com.hpifive.line.bcs.webhook.service.ScheduleService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path="/service/scheduler")
public class SchedulerServiceController {
	
	private static final Logger logger = LoggerFactory.getLogger(SchedulerServiceController.class);
	
	@Autowired
	private ScheduleService scheduleService;
	
	@RequestMapping(path="/add", method=RequestMethod.GET)
	public ResponseEntity<Object> addSchedulerTask(@RequestParam("sendId") Long id) {
		if (id == null) {
			return ResponseEntity.badRequest().body(ErrorResponseObject.message("errors body"));
		}
		try {
			this.scheduleService.addJobBySendId(id);
			logger.info("get Scheduler Job ID {}", id);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			logger.error("Error add Secheduler By sendId {}",id, e);
			return ResponseEntity.badRequest().body(ErrorResponseObject.message(e.getMessage()));
		}
	}
	
}

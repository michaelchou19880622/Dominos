package com.hpifive.line.bcs.webhook.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.hpifive.line.bcs.webhook.dao.EventAttendanceAttributeDao;
import com.hpifive.line.bcs.webhook.dao.EventPrizeDao;
import com.hpifive.line.bcs.webhook.entities.EventPrizeEntity;
import com.hpifive.line.bcs.webhook.exception.ControllerException;

@Service
@Component
public class LotteryService {
	
	@Autowired
	private EventPrizeDao prizeDao;
	
	@Autowired
	private EventAttendanceAttributeDao attributeDao;
	
	public List<Long> getRandomUserIdsBy(String keyOne, String keyTwo, String valueOne, String valueTwo, Long eventId, Integer applyStatus, Integer limit) throws ControllerException {
		List<BigInteger> userIds = this.attributeDao.getRandomUserIdByTwoKeyValueAndEventIdAndApplyStatus(keyOne, keyTwo, valueOne, valueTwo, eventId, applyStatus, limit);
		return this.convertUserIds(userIds);
	}
	
	public List<Long> getUnSendRandomUserIds(String keyOne, String keyTwo, String valueOne, String valueTwo, Long prizeId, Integer limit) throws ControllerException {
		try {
			EventPrizeEntity prizeEntity = this.prizeDao.getById(prizeId);
			return this.getUnSendRandomUserIds(keyOne, keyTwo, valueOne, valueTwo, prizeEntity.getEventId(), prizeId, limit);
		} catch (Exception e) {
			return Collections.emptyList();
		}
	}
	
	public List<Long> getUnSendRandomUserIds(String keyOne, String keyTwo, String valueOne, String valueTwo, Long eventId, Long prizeId, Integer limit) throws ControllerException {
		List<BigInteger> userIds = this.attributeDao.getRandomByTwoKeyValueAndEventIdAndPrizeId(keyOne, keyTwo, valueOne, valueTwo, eventId, prizeId, limit);
		return this.convertUserIds(userIds);
	}
	
	private List<Long> convertUserIds(List<BigInteger> list) throws ControllerException {
		if (list == null || list.isEmpty()) {
			throw new ControllerException("當前條件下無可供抽獎名單");
		}
		return this.convert(list);
	}
	
	private List<Long> convert(List<BigInteger> list) {
		List<Long> result = new ArrayList<>(list.size());
		list.forEach( i -> result.add(i.longValue()));
		return result;
	}
	
}

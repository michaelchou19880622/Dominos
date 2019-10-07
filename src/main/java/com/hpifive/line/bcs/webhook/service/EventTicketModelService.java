package com.hpifive.line.bcs.webhook.service;

import java.util.EnumMap;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.hpifive.line.bcs.webhook.controller.body.EventPrizeSenderBody;
import com.hpifive.line.bcs.webhook.dao.UserDao;
import com.hpifive.line.bcs.webhook.exception.ControllerException;
import com.hpifive.line.bcs.webhook.service.status.EventPrizeSenderStatus;

@Service
@Component
public class EventTicketModelService {
	
	private static final EnumMap<EventPrizeSenderStatus, String> statusMessage = new EnumMap<>(EventPrizeSenderStatus.class);
	
	@Autowired
	private LotteryService lotteryService;
	
	@Autowired
	private UserDao userDao;
	@Autowired
	private EventPrizeSenderService prizeSenderService;
	
	@PostConstruct
	private void init() {
		if (statusMessage.isEmpty()) {
			statusMessage.put(EventPrizeSenderStatus.NOT_EXIST, "票卷編號 %d ，票卷不存在");
			statusMessage.put(EventPrizeSenderStatus.NOT_ENOUGH_TICKERT, "票卷編號 %d ，沒有足夠的票，數量 %d");
			statusMessage.put(EventPrizeSenderStatus.OVER_UPPER_LIMIT, "票卷編號 %d ，沒有足夠的票，數量 %d");
		}
	}
	
	private String errorMessage(EventPrizeSenderStatus status) {
		return statusMessage.get(status);
	}
	
	public List<String> getLotteryListBy(Long eventPrizeId, Integer volume, String keyOne, String valueOne, String keyTwo, String valueTwo) throws ControllerException {
		List<Long> userIds = this.lotteryService.getUnSendRandomUserIds(keyOne, keyTwo, valueOne, valueTwo, eventPrizeId, volume);
		return this.getLotteryListBy(eventPrizeId, userIds);
	}
	
	public List<String> getLotteryListBy(Long eventPrizeId, List<Long> userIds) throws ControllerException {
		EventPrizeSenderBody body = this.prizeSenderService.pushTicketToUsers(userIds, eventPrizeId);
		EventPrizeSenderStatus status = body.getStatus();
		if (EventPrizeSenderStatus.SUCCESS != status) {
			throw ControllerException.message(String.format(errorMessage(status), eventPrizeId, body.getCount()));
		}
		return this.userDao.getByIds(userIds);
	}
	
}

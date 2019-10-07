package com.hpifive.line.bcs.webhook.service;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.hpifive.line.bcs.webhook.akka.AkkaService;
import com.hpifive.line.bcs.webhook.common.DateTimeModel;
import com.hpifive.line.bcs.webhook.controller.body.EventPrizeSenderBody;
import com.hpifive.line.bcs.webhook.dao.EventKeywordMessageListDao;
import com.hpifive.line.bcs.webhook.dao.EventPrizeDao;
import com.hpifive.line.bcs.webhook.dao.EventPrizeDetailDao;
import com.hpifive.line.bcs.webhook.dao.UserBelongingDao;
import com.hpifive.line.bcs.webhook.dao.UserDao;
import com.hpifive.line.bcs.webhook.entities.EventPrizeDetailEntity;
import com.hpifive.line.bcs.webhook.entities.EventPrizeEntity;
import com.hpifive.line.bcs.webhook.entities.LineUserBelongingEntity;
import com.hpifive.line.bcs.webhook.entities.config.BelongingStatus;
import com.hpifive.line.bcs.webhook.entities.config.KeywordEventTypes;
import com.hpifive.line.bcs.webhook.entities.config.LineUserBelongingTypes;
import com.hpifive.line.bcs.webhook.service.status.EventPrizeSenderStatus;
import com.linecorp.bot.model.message.Message;

@Service
@Component
public class EventPrizeSenderService {
	
	private static final Logger logger = LoggerFactory.getLogger(EventPrizeSenderService.class);

	@Autowired
	private AkkaService akkaService;
	@Autowired
	private UserDao userDao;
	@Autowired
	private UserBelongingDao belongingDao;
	@Autowired
	private EventPrizeDao prizeDao;
	@Autowired
	private EventPrizeDetailDao prizeDetailDao;
	@Autowired
	private EventKeywordMessageListDao messageListDao;
	
	public EventPrizeSenderBody pushTicketToUsers(Long userId, Long eventPrizeId, Long belongSource, LineUserBelongingTypes belongType) {
		return this.pushTicketToUsers(Collections.singletonList(userId), eventPrizeId, belongSource, belongType);
	}
	
	public EventPrizeSenderBody pushTicketToUsers(List<Long> ids, Long eventPrizeId, Long belongSource, LineUserBelongingTypes belongType) {
		try {
			EventPrizeSenderBody result = this.sendTicketToUsers(ids, eventPrizeId, belongSource, belongType);
			if (EventPrizeSenderStatus.SUCCESS == result.getStatus()) {
//				push !!
				EventPrizeEntity prizeEntity = this.prizeDao.getById(eventPrizeId);
				List<String> uuids = this.userDao.getByIds(ids);
				if (uuids == null || uuids.isEmpty()) {
					logger.error("Can't Push Ticket Msg to User with event_prize_id {} and ids {}", eventPrizeId, ids);
					return result;
				}
				List<Message> messages = messageListDao.getMessageByEventIdAndKeywordTypes(prizeEntity.getEventId(), KeywordEventTypes.TICKET);
				this.akkaService.push(uuids, messages);
			}
			return result;
		} catch (Exception e) {
			logger.error("PushTicket to user fail with event_prize_id {} and ids {}", eventPrizeId, ids, e);
		}
		return null;
	}
	public EventPrizeSenderBody pushTicketToUsers(List<Long> ids, Long eventPrizeId) {
		return this.pushTicketToUsers(ids, eventPrizeId, eventPrizeId, LineUserBelongingTypes.PRIZE);
	}
	
	private EventPrizeSenderBody sendTicketToUsers(List<Long> ids, Long eventPrizeId, Long belongSource, LineUserBelongingTypes type) {
		return this.sendTicketToUsers(ids, eventPrizeId, belongSource, type.toString());
	}
	private EventPrizeSenderBody sendTicketToUsers(List<Long> ids, Long eventPrizeId, Long belongSource, String type) {
		Integer volume = ids.size();
		EventPrizeEntity prizeEntity = this.prizeDao.getById(eventPrizeId);
		if (prizeEntity == null) {
			logger.error("Can't find Ticket By EventPrizeId {}", eventPrizeId);
			return new EventPrizeSenderBody(null, EventPrizeSenderStatus.NOT_EXIST);
		}
		if (volume > prizeEntity.getVolume()) {
			return new EventPrizeSenderBody(prizeEntity.getVolume(), EventPrizeSenderStatus.OVER_UPPER_LIMIT);
		}
		Integer unsendTicketVolume = this.prizeDetailDao.countUnSendTicketByPrizeId(eventPrizeId);
		if (volume > unsendTicketVolume) {
			return new EventPrizeSenderBody(unsendTicketVolume, EventPrizeSenderStatus.NOT_ENOUGH_TICKERT);
		}
		List<EventPrizeDetailEntity> unSendPrizes = this.prizeDetailDao.getUnSendTicketBy(eventPrizeId, volume);
		this.sendTicketToUser(ids, belongSource, type, unSendPrizes);
		return new EventPrizeSenderBody(volume, EventPrizeSenderStatus.SUCCESS);
	}
	
	private void sendTicketToUser(List<Long> ids, Long belongSource, String belongType, List<EventPrizeDetailEntity> unSendPrizes) {
		for(Integer index=0;index<ids.size(); index+=1) {
			Long userId = ids.get(index);
			EventPrizeDetailEntity prizeDetailEntity = unSendPrizes.get(index);
			this.sendTicketToUser(userId, belongSource, belongType, prizeDetailEntity);
		}
	}
	
	private void sendTicketToUser(Long userId, Long belongSource, String belongType, EventPrizeDetailEntity unSendPrize) {
		if (unSendPrize == null) {
			return;
		}
		LineUserBelongingEntity belongingEntity = new LineUserBelongingEntity(null, userId, unSendPrize.getId(), belongType, belongSource, BelongingStatus.USEFUL.toString(), DateTimeModel.getTime(), null);
		unSendPrize.setUserId(userId);
		this.belongingDao.save(belongingEntity);
		this.prizeDetailDao.save(unSendPrize);
	}
}

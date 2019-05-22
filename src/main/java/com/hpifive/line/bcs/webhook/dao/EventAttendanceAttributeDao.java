package com.hpifive.line.bcs.webhook.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.hpifive.line.bcs.webhook.common.ListExtension;
import com.hpifive.line.bcs.webhook.common.PageExtension;
import com.hpifive.line.bcs.webhook.entities.EventAttendanceAttributeEntity;
import com.hpifive.line.bcs.webhook.repository.EventAttendanceAttributeRepository;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;

@Component
@Repository
public class EventAttendanceAttributeDao {
	
	@Autowired
	private EventAttendanceAttributeRepository repository;
	
	public List<Long> getAttendanceIdByTwoKeyValueAndEventIdAndApplyStatus(String keyOne, String keyTwo, String valueOne, String valueTwo, Long eventId, Integer applyStatus) {
		return this.repository.findAttendanceIdByTwoColumnsAndApplyStatus(eventId, keyOne, valueOne, keyTwo, valueTwo, applyStatus);
	}
	
	public List<Long> getByTwoKeyValueAndEventIdAndPrizeId(String keyOne, String keyTwo, String valueOne, String valueTwo, Long eventId, Long prizeId, Integer limit) {
		Pageable pageable = PageRequest.of(0, limit);
		Page<Long> pages = this.repository.findUnSendAttendanceIdsByTwoColumns(eventId, prizeId, keyOne, valueOne, keyTwo, valueTwo, pageable);
		return PageExtension.getListFromPage(pages);
	}
	
	public List<BigInteger> getRandomByTwoKeyValueAndEventIdAndPrizeId(String keyOne, String keyTwo, String valueOne, String valueTwo, Long eventId, Long prizeId, Integer limit) {
		return this.repository.findRandomUnSendAttendanceIdsByTwoColumns(eventId, prizeId, keyOne, valueOne, keyTwo, valueTwo, limit);
	}
	
	public List<BigInteger> getRandomUserIdByTwoKeyValueAndEventIdAndApplyStatus(String keyOne, String keyTwo, String valueOne, String valueTwo, Long eventId, Integer applyStatus, Integer limit) {
		return this.repository.findRandomUserIdByTwoColumnsAndApplyStatus(eventId, keyOne, valueOne, keyTwo, valueTwo, applyStatus, limit);
	}
	
	public void deleteByAttendId(Long attendId) {
		this.repository.deleteByAttendanceId(attendId);
	}

	public void save(EventAttendanceAttributeEntity entity) {
		this.repository.save(entity);
	}
	
	public EventAttendanceAttributeEntity getNewAttributeByUserId(Long userId) {
		EventAttendanceAttributeEntity entity = new EventAttendanceAttributeEntity();
		entity.setLineUserId(userId);
		return entity;
	}
	
	public EventAttendanceAttributeEntity getOneByAttendIdOrderByCreationTimeDesc(Long attendId) {
		Pageable pageable = PageRequest.of(0, 1);
		List<EventAttendanceAttributeEntity> pages = this.repository.findByAttendanceIdOrderByCreateTimeDesc(attendId, pageable);
		return ListExtension.first(pages);
	}
	
	public List<EventAttendanceAttributeEntity> getAllByAttendanceId(Long attendId) {
		List<EventAttendanceAttributeEntity> attributeList = this.repository.findByAttendanceIdOrderByCreateTimeAsc(attendId);
		if (attributeList == null || attributeList.isEmpty()) {
			return new ArrayList<>();
		}
		return attributeList;
	}
	
	public Message getTextMessageByEventIdAndUserId(Long attendId) {
		StringBuilder builder = new StringBuilder("");
		List<EventAttendanceAttributeEntity> attributeList = this.getAllByAttendanceId(attendId);
		if (!(attributeList == null || attributeList.isEmpty())) {
			for (EventAttendanceAttributeEntity entity : attributeList) {
				String attributeString = String.format("【%s】%s", entity.getDescription(), entity.getValue());
				builder.append(attributeString);
				builder.append("\n");
			}
		}
		return new TextMessage(builder.toString());
	}
	
}

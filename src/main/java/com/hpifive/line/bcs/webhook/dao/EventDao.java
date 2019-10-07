package com.hpifive.line.bcs.webhook.dao;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.hpifive.line.bcs.webhook.common.DateTimeModel;
import com.hpifive.line.bcs.webhook.entities.EventEntity;
import com.hpifive.line.bcs.webhook.entities.config.LineUserBindStatus;
import com.hpifive.line.bcs.webhook.exception.DaoException;
import com.hpifive.line.bcs.webhook.repository.EventRepository;

@Service
@Component
public class EventDao {

	@Autowired
	private EventRepository eventRepository;
	
	public EventEntity getById(Long eventId) throws DaoException {
		Optional<EventEntity> optionalEntity = this.eventRepository.findById(eventId);
		if (optionalEntity.isPresent()) {
			return optionalEntity.get();
		}
		throw DaoException.message(String.format("No EventEntity found with id %d", eventId));
	}
	
	public Integer getErrorCountById(Long id) {
		Integer errorCount = this.eventRepository.findErrorCountById(id);
		if (errorCount == null || errorCount == 0) {
			return Integer.MAX_VALUE;
		}
		return  errorCount;
	}
	
	public EventEntity getByKeywordAndUserStatus(String keyword, LineUserBindStatus status) {
		List<String> listStatus = Arrays.asList(status.toString(), LineUserBindStatus.ALL.toString());
		ZonedDateTime date = DateTimeModel.getTime();
		return this.eventRepository.findByKeywordAndUserStatusAndDate(keyword, listStatus, date);
	}
	
}

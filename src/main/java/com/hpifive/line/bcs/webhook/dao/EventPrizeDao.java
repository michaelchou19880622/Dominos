package com.hpifive.line.bcs.webhook.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.hpifive.line.bcs.webhook.common.ListExtension;
import com.hpifive.line.bcs.webhook.entities.EventPrizeEntity;
import com.hpifive.line.bcs.webhook.repository.EventPrizeRepository;

@Service
@Component
public class EventPrizeDao {
	
	@Autowired
	private EventPrizeRepository repository;
	
	public EventPrizeEntity getByIdAndEventId(Long id, Long eventId) {
		Pageable pageable = PageRequest.of(0, 1);
		List<EventPrizeEntity> content = this.repository.findByIdAndEventId(id, eventId, pageable);
		return ListExtension.first(content);
	}
	
	public EventPrizeEntity getById(Long eventPrizeId) {
		if (eventPrizeId == null) {
			return null;
		}
		Optional<EventPrizeEntity> optional = this.repository.findById(eventPrizeId);
		if (optional.isPresent() ) {
			return optional.get();
		}
		return null;
	}
	
}

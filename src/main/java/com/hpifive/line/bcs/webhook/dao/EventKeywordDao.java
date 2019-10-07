package com.hpifive.line.bcs.webhook.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.hpifive.line.bcs.webhook.common.PageExtension;
import com.hpifive.line.bcs.webhook.entities.EventEntity;
import com.hpifive.line.bcs.webhook.entities.EventKeywordEntity;
import com.hpifive.line.bcs.webhook.entities.config.KeywordEventTypes;
import com.hpifive.line.bcs.webhook.repository.EventKeywordRepository;

@Service
@Component
public class EventKeywordDao {

	@Autowired
	private EventKeywordRepository eventKeywordRepository;
	
	public EventEntity getEventByKeyword(String keyword) {
		EventKeywordEntity entity = this.getByKeyword(keyword);
		if (entity==null) {return null;}
		return entity.getEventEntity();
	}
	
	public EventKeywordEntity getByIdAndKeyword(Long eventId, String keyword) {
		Pageable pageable = PageRequest.of(0, 1);
		Page<EventKeywordEntity> pages = this.eventKeywordRepository.findByKeywordAndEventId(keyword, eventId, pageable);
		return PageExtension.getFirstFromPage(pages);
	}
	
	public EventKeywordEntity getByIdAndTypes(Long eventId, KeywordEventTypes types) {
		Pageable pageable = PageRequest.of(0, 1);
		Page<EventKeywordEntity> pages = this.eventKeywordRepository.findByEventIdAndKeywordEvent(eventId, types.toString(), pageable);
		return PageExtension.getFirstFromPage(pages);
	}
	
	public EventKeywordEntity getByKeyword(String keyword) {
		Pageable pageable = PageRequest.of(0, 1);
		Page<EventKeywordEntity> pages = this.eventKeywordRepository.findByKeyword(keyword, pageable);
		return PageExtension.getFirstFromPage(pages);
	}
	
}

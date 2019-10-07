package com.hpifive.line.bcs.webhook.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.hpifive.line.bcs.webhook.entities.EventKeywordEntity;

public interface EventKeywordRepository extends CrudRepository<EventKeywordEntity, Long> {
	
	public Page<EventKeywordEntity> findByKeyword(String keyword, Pageable pageable);
	
	public Page<EventKeywordEntity> findByKeywordAndEventId(String keyword, Long eventId, Pageable pageable);

	public Page<EventKeywordEntity> findByEventIdAndKeywordEvent(Long eventId, String keywordEvent, Pageable pageable);
}

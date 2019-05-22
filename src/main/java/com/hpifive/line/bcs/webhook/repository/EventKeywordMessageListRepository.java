package com.hpifive.line.bcs.webhook.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.hpifive.line.bcs.webhook.entities.EventKeywordMessageListEntity;

public interface EventKeywordMessageListRepository extends CrudRepository<EventKeywordMessageListEntity, Long> {
	
	public Page<EventKeywordMessageListEntity> findByEventKeywordIdOrderByOrderIndexAsc(Long eventKeywordId, Pageable pageable);
	
	@Query(value="SELECT L FROM EventKeywordMessageListEntity L INNER JOIN EventKeywordEntity K ON K.id = L.eventKeywordId WHERE  K.eventId = :eventId AND K.keywordEvent = :keywordEvent GROUP BY L ORDER BY L.orderIndex ASC")
	public Page<EventKeywordMessageListEntity> findByEventIdAndKeywordEvent(@Param("eventId") Long eventId, @Param("keywordEvent") String keywordEvent, Pageable pageable); 
}

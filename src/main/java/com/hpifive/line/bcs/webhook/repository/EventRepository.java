package com.hpifive.line.bcs.webhook.repository;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.hpifive.line.bcs.webhook.entities.EventEntity;

public interface EventRepository extends CrudRepository<EventEntity, Long> {
	
	@Query(value="select E from EventEntity E inner join EventKeywordEntity K on K.eventId = E.id and K.keyword = :keyword where K.keyword = :keyword and E.beginDatetime <= :date and E.endDatetime >= :date")
	public EventEntity findByKeyword(@Param("keyword") String keyword, @Param("date") Date date);
	
	@Query(value="select E from EventEntity E inner join EventKeywordEntity K on K.eventId = E.id and K.keyword = :keyword where K.keyword = :keyword and E.userStatus in :status and E.beginDatetime <= :date and E.endDatetime >= :date")
	public EventEntity findByKeywordAndUserStatusAndDate(@Param("keyword") String keyword, @Param("status") List<String> status, @Param("date") ZonedDateTime date);

	@Query(value="select E from EventEntity E inner join EventKeywordEntity K on K.eventId = E.id and K.keyword = :keyword where K.keyword = :keyword and E.userStatus in :status")
	public Page<EventEntity> findByKeywordAndUserStatus(@Param("keyword") String keyword, @Param("status") List<String> status, Pageable pageable);

	@Query(value="select E.inputErrorTime from EventEntity E where E.id = :eventId")
	public Integer findErrorCountById(@Param("eventId") Long id);
}

package com.hpifive.line.bcs.webhook.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.hpifive.line.bcs.webhook.entities.EventApplydataEntity;

public interface EventApplyDataRepository extends CrudRepository<EventApplydataEntity, Long> {

	public Page<EventApplydataEntity> findByEventIdOrderByOrderIndexDesc(Long eventId, Pageable pageable);

	@Query(value="SELECT count(E.id) FROM EventApplydataEntity E WHERE E.eventId = :eventId GROUP BY E.eventId")
	public Integer countByEventId(@Param("eventId") Long eventId);

	public Page<EventApplydataEntity> findByEventId(@Param("eventId") Long eventId, Pageable pageable);
}

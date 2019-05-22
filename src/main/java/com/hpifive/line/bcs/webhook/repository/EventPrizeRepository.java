package com.hpifive.line.bcs.webhook.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.hpifive.line.bcs.webhook.entities.EventPrizeEntity;

public interface EventPrizeRepository extends CrudRepository<EventPrizeEntity, Long> {

	public List<EventPrizeEntity> findByIdAndEventId(Long id, Long eventId, Pageable pageable);
}

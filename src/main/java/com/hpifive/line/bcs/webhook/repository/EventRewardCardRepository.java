package com.hpifive.line.bcs.webhook.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.hpifive.line.bcs.webhook.entities.EventRewardCardEntity;

public interface EventRewardCardRepository extends CrudRepository<EventRewardCardEntity, Long> {

	public List<EventRewardCardEntity> findByEventId(Long eventId, Pageable pageable);
}

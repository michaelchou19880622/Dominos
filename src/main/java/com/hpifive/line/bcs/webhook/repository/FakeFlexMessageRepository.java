package com.hpifive.line.bcs.webhook.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.hpifive.line.bcs.webhook.entities.FakeFlexMessageEntity;

public interface FakeFlexMessageRepository extends CrudRepository<FakeFlexMessageEntity, Long> {

	public Page<FakeFlexMessageEntity> findByEventPrizeId(Long eventPrizeId, Pageable pageable);
}

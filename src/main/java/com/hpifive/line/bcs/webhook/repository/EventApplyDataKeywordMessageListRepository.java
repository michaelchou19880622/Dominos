package com.hpifive.line.bcs.webhook.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.hpifive.line.bcs.webhook.entities.EventApplydataKeywordMessageListEntity;

public interface EventApplyDataKeywordMessageListRepository extends CrudRepository<EventApplydataKeywordMessageListEntity, Long> {

	public Page<EventApplydataKeywordMessageListEntity> findByEventApplydataKeywordId(Long eventApplydataKeywordId, Pageable pageable);
}

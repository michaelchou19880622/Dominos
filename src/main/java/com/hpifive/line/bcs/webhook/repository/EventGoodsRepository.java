package com.hpifive.line.bcs.webhook.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.hpifive.line.bcs.webhook.entities.EventGoodsEntity;

public interface EventGoodsRepository extends CrudRepository<EventGoodsEntity, Long> {
	public EventGoodsEntity findByEventIdAndGoodId(Long eventId, Long goodId);
	public List<EventGoodsEntity> findByEventId(Long eventId);
}

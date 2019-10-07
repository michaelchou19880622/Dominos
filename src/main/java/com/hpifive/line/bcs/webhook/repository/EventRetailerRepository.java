package com.hpifive.line.bcs.webhook.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.hpifive.line.bcs.webhook.entities.EventRetailerEntity;

public interface EventRetailerRepository extends CrudRepository<EventRetailerEntity, Long> {
	
	public List<EventRetailerEntity> findByEventIdAndRetailerId(Long eventId, Long retailerId, Pageable pageable);
}

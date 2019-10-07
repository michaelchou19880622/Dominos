package com.hpifive.line.bcs.webhook.repository;

import org.springframework.data.repository.CrudRepository;

import com.hpifive.line.bcs.webhook.entities.FlexMessageBubbleContainerEntity;

public interface FlexMessageBubbleContainerRepository extends CrudRepository<FlexMessageBubbleContainerEntity, Long> {

	public FlexMessageBubbleContainerEntity findByFlexId(Long flexId);
}

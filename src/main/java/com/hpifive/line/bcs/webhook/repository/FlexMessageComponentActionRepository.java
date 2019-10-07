package com.hpifive.line.bcs.webhook.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.hpifive.line.bcs.webhook.entities.FlexMessageComponentActionEntity;

public interface FlexMessageComponentActionRepository extends CrudRepository<FlexMessageComponentActionEntity, Long> {

	public List<FlexMessageComponentActionEntity> findByComponentIdAndComponentType(Long componentId, String componentType);
	
	
}

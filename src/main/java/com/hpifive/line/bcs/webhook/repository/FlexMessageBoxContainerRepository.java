package com.hpifive.line.bcs.webhook.repository;

import org.springframework.data.repository.CrudRepository;

import com.hpifive.line.bcs.webhook.entities.FlexMessageBoxContainerEntity;

public interface FlexMessageBoxContainerRepository extends CrudRepository<FlexMessageBoxContainerEntity, Long> {
}

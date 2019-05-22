package com.hpifive.line.bcs.webhook.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.hpifive.line.bcs.webhook.entities.FlexMessageCarouselEntity;

public interface FlexMessageCarouselRepository extends CrudRepository<FlexMessageCarouselEntity, Long> {

	public List<FlexMessageCarouselEntity> findByFlexIdOrderByOrderIndexAsc(Long flexId);
}

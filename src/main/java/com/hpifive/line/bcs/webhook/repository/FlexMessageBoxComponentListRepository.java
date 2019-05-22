package com.hpifive.line.bcs.webhook.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.hpifive.line.bcs.webhook.entities.FlexMessageBoxComponentList;

public interface FlexMessageBoxComponentListRepository extends CrudRepository<FlexMessageBoxComponentList, Long> {

	public List<FlexMessageBoxComponentList> findAllByBoxIdOrderByOrderIndexAsc(Long boxId);
}

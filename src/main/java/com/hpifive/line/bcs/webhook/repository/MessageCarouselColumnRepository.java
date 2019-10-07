package com.hpifive.line.bcs.webhook.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.hpifive.line.bcs.webhook.entities.MessageCarouselColumnEntity;

public interface MessageCarouselColumnRepository extends CrudRepository<MessageCarouselColumnEntity, Integer>{
	
	public List<MessageCarouselColumnEntity> findAllByCarouselId(Integer carouselId);
}

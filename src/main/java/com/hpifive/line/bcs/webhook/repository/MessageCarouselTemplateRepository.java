package com.hpifive.line.bcs.webhook.repository;

import org.springframework.data.repository.CrudRepository;

import com.hpifive.line.bcs.webhook.entities.MessageCarouselTemplateEntity;

public interface MessageCarouselTemplateRepository extends CrudRepository<MessageCarouselTemplateEntity, Integer>{
	
}

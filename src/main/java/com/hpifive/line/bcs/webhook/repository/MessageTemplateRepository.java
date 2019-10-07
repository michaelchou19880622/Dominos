package com.hpifive.line.bcs.webhook.repository;

import org.springframework.data.repository.CrudRepository;

import com.hpifive.line.bcs.webhook.entities.MessageTemplateEntity;

public interface MessageTemplateRepository extends CrudRepository<MessageTemplateEntity, Integer>{
	
}

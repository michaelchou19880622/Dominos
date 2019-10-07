package com.hpifive.line.bcs.webhook.repository;

import org.springframework.data.repository.CrudRepository;

import com.hpifive.line.bcs.webhook.entities.MessageTextEntity;

public interface MessageTextRepository extends CrudRepository<MessageTextEntity, Integer>{

}

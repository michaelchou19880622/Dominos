package com.hpifive.line.bcs.webhook.repository;

import org.springframework.data.repository.CrudRepository;

import com.hpifive.line.bcs.webhook.entities.MessageLocationEntity;

public interface MessageLocationRepository extends CrudRepository<MessageLocationEntity, Integer>{

}

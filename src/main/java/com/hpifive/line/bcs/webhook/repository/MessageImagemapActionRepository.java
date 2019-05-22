package com.hpifive.line.bcs.webhook.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.hpifive.line.bcs.webhook.entities.MessageImagemapActionEntity;

public interface MessageImagemapActionRepository extends CrudRepository<MessageImagemapActionEntity, Integer>{

	public List<MessageImagemapActionEntity> findAllByMessageId(Integer messageId);
}

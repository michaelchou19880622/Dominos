package com.hpifive.line.bcs.webhook.repository;

import org.springframework.data.repository.CrudRepository;

import com.hpifive.line.bcs.webhook.entities.AutoreplyEntity;

public interface AutoreplyRepository extends CrudRepository<AutoreplyEntity, Long> {
	
	public AutoreplyEntity findByDataAndType(String keyword, String type);
}

package com.hpifive.line.bcs.webhook.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.hpifive.line.bcs.webhook.entities.RetailerDetailEntity;

public interface RetailerDetailRepository extends CrudRepository<RetailerDetailEntity, Long> {
	
	public List<RetailerDetailEntity> findByCompanyId(String companyId, Pageable pageable);
}

package com.hpifive.line.bcs.webhook.repository;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.hpifive.line.bcs.webhook.entities.SystemConfigEntity;

public interface SystemConfigRepository extends CrudRepository<SystemConfigEntity, Integer> {

	public SystemConfigEntity findOneByKeyAndModifyTime(String key, Date modifyTime);
	
	public SystemConfigEntity findOneByKey(String key);
	
	@Query(value="SELECT S.value FROM SystemConfigEntity S WHERE S.key = :key")
	public Page<String> findValueByKey(@Param("key") String key, Pageable pageable);
	
}

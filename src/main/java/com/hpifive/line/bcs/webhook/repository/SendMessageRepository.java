package com.hpifive.line.bcs.webhook.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.hpifive.line.bcs.webhook.entities.SendMessageEntity;

public interface SendMessageRepository extends CrudRepository<SendMessageEntity, Long> {

	public List<SendMessageEntity> findAllByStatus(Integer status);
	
	public SendMessageEntity findOneByIdAndStatus(Long id, Integer status);
	
	public SendMessageEntity findOneById(Long id);
	
	@Transactional
	@Modifying
	@Query(value="update SendMessageEntity s set s.status = :status where s.id = :id")
	public void setStatusById(@Param("id") Long id, @Param("status") Integer status);
}

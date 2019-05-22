package com.hpifive.line.bcs.webhook.repository;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.hpifive.line.bcs.webhook.entities.SendMessageUsersPushJobEntity;

public interface SendMessageUsersPushJobRepository extends CrudRepository<SendMessageUsersPushJobEntity, Long> {

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query(value="SELECT P FROM SendMessageUsersPushJobEntity P WHERE P.sendId = :sendId")
	public SendMessageUsersPushJobEntity getBySendId(@Param("sendId") Long sendId);
	
	public SendMessageUsersPushJobEntity findBySendId(Long sendId);
}

package com.hpifive.line.bcs.webhook.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.hpifive.line.bcs.webhook.entities.SendMessageListEntity;

public interface SendMessageListRepository extends CrudRepository<SendMessageListEntity, Long> {
	
	@Query(value="select distinct L FROM SendMessageListEntity L inner join SendMessageEntity a on a.id = L.sendId and L.sendId = :sendId order by L.orderIndex")
	public Page<SendMessageListEntity> getListBySendId(@Param("sendId") Long sendId, Pageable pageable);

}

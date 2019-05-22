package com.hpifive.line.bcs.webhook.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.hpifive.line.bcs.webhook.entities.EventPrizeDetailEntity;

public interface EventPrizeDetailRepository extends CrudRepository<EventPrizeDetailEntity, Long> {

	@Query(value="SELECT count(d.id) FROM EventPrizeDetailEntity d WHERE d.userId IS NULL and d.eventPrizeId = :prizeId")
	public Integer countUnSendPrizeByPrizeId(@Param("prizeId") Long prizeId);
	
	@Query(value="SELECT d FROM EventPrizeDetailEntity d WHERE d.userId IS NULL and d.eventPrizeId = :prizeId")
	public List<EventPrizeDetailEntity> findUnSendTicketByPrizeId(@Param("prizeId")  Long prizeId, Pageable pageable);
}

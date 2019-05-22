package com.hpifive.line.bcs.webhook.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.hpifive.line.bcs.webhook.entities.GoodsDetailEntity;

public interface GoodsDetailRepository extends CrudRepository<GoodsDetailEntity, Long> {
	@Query(value="SELECT D FROM GoodsDetailEntity D inner join EventGoodsEntity E on E.goodId = D.goodId WHERE E.eventId = :eventId")
	public List<GoodsDetailEntity> findByEventId(@Param("eventId") Long eventId);
}

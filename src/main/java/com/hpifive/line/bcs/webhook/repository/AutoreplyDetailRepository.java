package com.hpifive.line.bcs.webhook.repository;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.hpifive.line.bcs.webhook.entities.AutoreplyDetailEntity;

public interface AutoreplyDetailRepository extends CrudRepository<AutoreplyDetailEntity, Long> {

	@Query("select distinct D.autoreplyID from AutoreplyDetailEntity D inner join AutoreplyEntity A on A.id = D.autoreplyID and D.keyword = :keyword and A.status = 'ACTIVE' and A.type = 'KEYWORD' and (A.period = 'FOREVER' or (A.period = 'DAY' and A.beginTime <= :date and A.endTime >= :date ))")
	public Page<Long> findOneAutoreplyIDByKeyword(@Param("keyword")String keyword, @Param("date") Date date, Pageable pageable);
}

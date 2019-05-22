package com.hpifive.line.bcs.webhook.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.hpifive.line.bcs.webhook.entities.AutoreplyMessageListEntity;

public interface AutoreplyMessageListRepository extends CrudRepository<AutoreplyMessageListEntity, Long> {

	@Query(value="SELECT DISTINCT L FROM AutoreplyMessageListEntity L "
			+ "INNER JOIN AutoreplyEntity a ON a.id = L.autoreplyID "
			+ "WHERE a.status = 'ACTIVE' AND a.userStatus = 'ALL' AND (a.period = 'FOREVER' or (a.period = 'DAY' and a.beginTime <= :date and a.endTime >= :date )) AND a.data = :keyword AND a.type = :type  order by L.orderNum")
	public List<AutoreplyMessageListEntity> findListByKeyWordAndType(@Param("keyword") String keyword, @Param("type") String type, @Param("date") Date date, Pageable pageable);
	
	@Query(value="select distinct L FROM AutoreplyMessageListEntity L "
			+ "inner join AutoreplyEntity a on a.id = L.autoreplyID and a.status = 'ACTIVE' and a.userStatus = 'ALL' and (a.period = 'FOREVER' or (a.period = 'DAY' and a.beginTime <= :date and a.endTime >= :date )) "
			+ "inner join AutoreplyDetailEntity d on d.autoreplyID = L.autoreplyID and  d.keyword = :keyword  order by L.orderNum")
	public Page<AutoreplyMessageListEntity> getListByKeyWord(@Param("keyword") String keyword, @Param("date") Date date, Pageable pageable);

	@Query(value="select distinct L FROM AutoreplyMessageListEntity L "
			+ "inner join AutoreplyEntity a on a.id = L.autoreplyID "
			+ "inner join AutoreplyDetailEntity d on d.autoreplyID = L.autoreplyID WHERE  d.keyword = :keyword  and a.status = 'ACTIVE' and a.userStatus in :status and (a.period = 'FOREVER' or (a.period = 'DAY' and a.beginTime <= :date and a.endTime >= :date ))  order by L.orderNum")
	public Page<AutoreplyMessageListEntity> getListByKeyWordAndDateAndStatus(@Param("keyword") String keyword, @Param("date") Date date, @Param("status") List<String> status, Pageable pageable);
}

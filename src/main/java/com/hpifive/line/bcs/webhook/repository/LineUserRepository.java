package com.hpifive.line.bcs.webhook.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.hpifive.line.bcs.webhook.entities.LineUserEntity;

public interface LineUserRepository extends CrudRepository<LineUserEntity, Long> {

	@Query(value="SELECT U.id AS tarKey, U.uid AS tarValue FROM LineUserEntity U WHERE U.id IN :ids")
	public List<Map<String, Object>> findIdAndUidByIds(@Param("ids") List<Long> ids);
	
	public List<LineUserEntity> findByIdIn(List<Long> ids);
	
	@Query(value="select distinct U.uid from LineUserEntity U where U.id in :ids")
	public List<String> findUidByIdIn(@Param("ids") List<Long> ids);
	
	@Query(value="select distinct U.uid from LineUserEntity U where U.id = :id")
	public String findUidById(@Param("id") Long id);
	
	public LineUserEntity findByUid(String uid);
	
	@Query(value="SELECT U.id FROM LineUserEntity U WHERE U.uid = :uid")
	public List<Long> findIdByUid(@Param("uid") String uid, Pageable pageable);
	
	@Query(value="SELECT U FROM LineUserEntity U WHERE U.id NOT IN (SELECT distinct P.userId FROM EventPrizeDetailEntity P WHERE P.eventPrizeId = :prizeId)")
	public Page<LineUserEntity> findUnUseUserBy(@Param("prizeId")Long prizeId, Pageable pageable);
	
	@Query(value="SELECT U.status FROM LineUserEntity U where U.uid = :uid")
	public String findStatusByUid(@Param("uid") String uid);
}

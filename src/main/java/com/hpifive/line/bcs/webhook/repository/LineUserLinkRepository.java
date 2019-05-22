package com.hpifive.line.bcs.webhook.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.hpifive.line.bcs.webhook.entities.LineUserLinkEntity;

public interface LineUserLinkRepository extends CrudRepository<LineUserLinkEntity, Long> {

	public LineUserLinkEntity findByUserId(Long userId);
	
	@Query(value="select L.linked from LineUserLinkEntity L where L.userId = :userId ")
	public String findLinkedByUserId(@Param("userId") Long userId);
	
	@Query(value="select L.linked from LineUserLinkEntity L inner join LineUserEntity U on U.uid = :uid and L.userId = U.id")
	public String findLinkedByUid(@Param("uid") String uid);
	
	@Query(value="SELECT L FROM LineUserLinkEntity L inner join LineUserEntity U on U.uid = :uid and L.userId = U.id")
	public LineUserLinkEntity findByUid(@Param("uid") String uid);
	
}

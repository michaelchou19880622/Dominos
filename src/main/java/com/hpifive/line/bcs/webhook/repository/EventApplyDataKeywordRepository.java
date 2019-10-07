package com.hpifive.line.bcs.webhook.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.hpifive.line.bcs.webhook.entities.EventApplydataKeywordEntity;

public interface EventApplyDataKeywordRepository extends CrudRepository<EventApplydataKeywordEntity, Long> {

	@Query(value="SELECT COUNT(E.keyword) FROM EventApplydataKeywordEntity E WHERE E.eventApplydataId = :applyId AND E.keyword = :keyword GROUP BY E.eventApplydataId, E.keyword")
	public Integer countByApplydataIdAndKeyword(@Param("applyId") Long applyDataId, @Param("keyword") String keyword);
	
	public Page<EventApplydataKeywordEntity> findByEventApplydataIdAndKeywordEvent(Long eventApplydataId, String keywordEvent, Pageable pageable);
	
	@Query(value="SELECT K FROM EventApplydataKeywordEntity K WHERE K.eventApplydataId = :applyDataId")
	public Page<EventApplydataKeywordEntity> findByApplyDataId(@Param("applyDataId") Long applyDataId, Pageable pageable);

	@Query(value="SELECT K FROM EventApplydataKeywordEntity K WHERE K.eventApplydataId = :applyDataId and K.keywordEvent != :keywordEvent")
	public Page<EventApplydataKeywordEntity> findByEventApplydataIdAndNotKeywordEvent(@Param("applyDataId") Long applyDataId, @Param("keywordEvent") String keywordEvent, Pageable pageable);
}

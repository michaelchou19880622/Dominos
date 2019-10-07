package com.hpifive.line.bcs.webhook.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.hpifive.line.bcs.webhook.entities.LineUserAttributeEntity;

public interface LineUserAttributeRepository extends CrudRepository<LineUserAttributeEntity, Long> {

	public LineUserAttributeEntity findByLineUserIdAndKey(Long lineUserId, String key);
	
	@Query(value="select A.value as key, count(A.value) as value from LineUserAttributeEntity A where A.key = :key group by A.value")
	public Page<Map<String, Object>> findByKeyAndPage(@Param("key") String key, Pageable pageable);
	
	@Query(value="select A.value as key, count(A.value) as count  from LineUserAttributeEntity A, LineUserAttributeEntity B "
			+ "where A.lineUserId=B.lineUserId and A.key = :firstKey and B.key = :secondKey and B.value between :firstKeyValue and :secondKeyValue group by A.value")
	public List<Map<String, Object>> findByTwoKeyAndValueBetweenRange(@Param("firstKey") String firstKey, @Param("secondKey") String secondKey, @Param("firstKeyValue") String firstKeyValue, @Param("secondKeyValue") String secondKeyValue);
}

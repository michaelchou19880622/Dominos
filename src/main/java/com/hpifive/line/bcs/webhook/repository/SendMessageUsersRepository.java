package com.hpifive.line.bcs.webhook.repository;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.hpifive.line.bcs.webhook.entities.SendMessageUsersEntity;

public interface SendMessageUsersRepository extends CrudRepository<SendMessageUsersEntity, Long> {

	@Query(value="SELECT U.id FROM SendMessageUsersEntity U WHERE U.sendId = :sendId ORDER BY U.id DESC")
	public List<Long> findLastIndexBySendIdOrderByIdDesc(@Param("sendId") Long sendId, Pageable pageable);
	
	@Query(value="select U from SendMessageUsersEntity U WHERE U.id > :id AND U.sendId = :sendId")
	public List<SendMessageUsersEntity> findUserIdBySendIdAndStatus(@Param("id") Long id, @Param("sendId") Long sendId, Pageable pageable);

	@Query(value="select distinct U from SendMessageUsersEntity U where U.sendId = :sendId")
	public Page<SendMessageUsersEntity> findBySendId(@Param("sendId") Long sendId, Pageable pageable);
	
	@Query(value="select U.userId from SendMessageUsersEntity U where U.sendId = :sendId")
	public List<Long> findUserIdBySendId(@Param("sendId") Long sendId, Pageable pageable);
	
	@Transactional
	@Modifying
	@Query(value="update SendMessageUsersEntity U set U.status = :status, U.responseCode = :responseCode, U.sendTime = :time where U.id in :ids")
	public void setStatusAndResponseCodeByIds(@Param("ids") List<Long> ids, @Param("status") Integer status, @Param("responseCode") Integer responseCode, @Param("time") ZonedDateTime time);

	@Modifying
	@Query(value="update SendMessageUsersEntity U set U.status = :status, U.responseCode = :responseCode, U.sendTime = :time where U IN (:entities)")
	public void setStatusAndResponseCodeBy(@Param("entities") List<SendMessageUsersEntity> entities,@Param("status") Integer status, @Param("responseCode") Integer responseCode, @Param("time") ZonedDateTime time);
	
	@Modifying
	@Query(value="update SendMessageUsersEntity U set U.status = :status, U.responseCode = :responseCode, U.sendTime = :time where U.id >= :min AND U.id <= :max")
	public void setStatusAndResponseCodeById(@Param("min") Long min, @Param("max") Long max, @Param("status") Integer status, @Param("responseCode") Integer responseCode, @Param("time") ZonedDateTime time);

}

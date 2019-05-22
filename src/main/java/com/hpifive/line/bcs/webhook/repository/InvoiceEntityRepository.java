package com.hpifive.line.bcs.webhook.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.hpifive.line.bcs.webhook.entities.InvoiceEntity;

public interface InvoiceEntityRepository extends CrudRepository<InvoiceEntity, Long> {

	@Query(value="SELECT I FROM InvoiceEntity I  inner join EventEntity E on E.id = I.eventId WHERE E.beginDatetime <= :date AND E.endDatetime >= :date AND I.lineUserId = :userId AND (I.invNum IS NULL OR I.invRandom IS NULL OR I.invTerm IS NULL) ORDER BY I.id ASC")
	public List<InvoiceEntity> findUnFinishInvoiceByUserIdAndDate(@Param("userId") Long userId, @Param("date") Date date, Pageable pageable);
	
	@Query(value="SELECT I FROM InvoiceEntity I  inner join EventEntity E on E.id = I.eventId WHERE E.beginDatetime <= :date AND E.endDatetime >= :date AND I.lineUserId = :userId AND I.invNum IS NULL AND I.invRandom IS NULL AND I.invTerm IS NULL ORDER BY I.id ASC")
	public List<InvoiceEntity> findUnFilledInvoiceByUserIdAndDate(@Param("userId") Long userId, @Param("date") Date date, Pageable pageable);
	
	@Query(value="SELECT I FROM InvoiceEntity I WHERE I.eventId = :eventId AND I.lineUserId = :userId AND I.invNum IS NULL AND I.invRandom IS NULL AND I.invTerm IS NULL")
	public InvoiceEntity findUnFilledInvoiceByEventIdAndUserId(@Param("eventId") Long eventId, @Param("userId") Long userId);
	
	@Query(value="SELECT I FROM InvoiceEntity I WHERE I.eventId = :eventId AND I.lineUserId = :userId AND (I.invNum IS NULL OR I.invRandom IS NULL OR I.invTerm IS NULL)")
	public InvoiceEntity findUnFinishInvoiceByEventIdAndUserId(@Param("eventId") Long eventId, @Param("userId") Long userId);

	public List<InvoiceEntity> findByStatus(String status);
	
	public List<InvoiceEntity> findByLineUserIdAndStatus(Long userId, String status);
	
	@Query(value="SELECT DISTINCT I.eventId FROM InvoiceEntity I WHERE I.status = :status")
	public List<Long> findEventIdByStatus(@Param("status") String status);
	
	@Query(value="SELECT DISTINCT I.lineUserId FROM InvoiceEntity I WHERE I.status = :status AND I.eventId = :eventId")
	public List<Long> findUserIdByStatusAndEventId(@Param("eventId") Long eventId, @Param("status") String status);
	
	@Query(value="SELECT DISTINCT I.invNum FROM InvoiceEntity I WHERE I.lineUserId = :userId AND I.status = :status AND I.eventId = :eventId")
	public List<String> findInvNumByLineUserIdAndStatusAndEventId(@Param("eventId") Long eventId, @Param("userId") Long userId, @Param("status") String status);
	
	public Integer countByEventIdAndInvNum(Long eventId, String invNum);

	@Transactional
	@Modifying
	@Query(value="UPDATE InvoiceEntity I SET I.status = :afterStatus WHERE I.eventId = :eventId AND I.status = :beforeStatus")
	public void setStatusByEventIdAndStatus(@Param("eventId") Long eventId, @Param("beforeStatus") String beforeStatus, @Param("afterStatus") String afterStatus);
}

package com.hpifive.line.bcs.webhook.repository;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.hpifive.line.bcs.webhook.entities.EventAttendanceEntity;

public interface EventAttendanceRepository extends CrudRepository<EventAttendanceEntity, Long> {
	
	@Query(value="SELECT A.userId FROM EventAttendanceEntity A left join EventAttendanceConfirmTimeEntity C on A.eventId = C.eventId and A.userId = C.userId WHERE A.eventId = :eventId AND A.userId = :userId AND C.expireTime <= :time")
	public List<Long> findExpiredUserIdByEventIdAndUserIdAndTime(@Param("eventId") Long eventId, @Param("userId") Long userId, @Param("time") ZonedDateTime time, Pageable pageable);
	
	@Query(value="SELECT A.userId FROM EventAttendanceEntity A left join EventAttendanceConfirmTimeEntity C on A.eventId = C.eventId and A.userId = C.userId WHERE A.eventId = :eventId AND A.applyStatus = :applyStatus AND C.expireTime <= :time")
	public List<Long> findExpiredUserIdByApplyStatusAndTime(@Param("eventId") Long eventId, @Param("applyStatus") Integer applyStatus, @Param("time") ZonedDateTime time);
	
	@Query(value="SELECT DISTINCT A.userId FROM EventAttendanceEntity A WHERE A.eventId = :eventId AND A.applyStatus = :applyStatus AND A.userId NOT IN (SELECT D.userId FROM EventPrizeDetailEntity D WHERE D.eventPrizeId = :prizeId AND D.userId IS NOT NULL)")
	public List<Long> findUnSendUserByEventIdAndPrizeIdAndApplyStatus(@Param("eventId") Long eventId, @Param("prizeId") Long prizeId, @Param("applyStatus") Integer applyStatus);
	
	@Transactional
	public void deleteById(Long id);

	@Query(value="SELECT DISTINCT A.userId FROM EventAttendanceEntity A WHERE A.id IN :ids")
	public List<Long> findUserIdByIds(@Param("ids") List<Long> ids);
	
	@Query(value="SELECT DISTINCT A.userId FROM EventAttendanceEntity A WHERE A.eventId = :eventId AND A.applyStatus = :applyStatus ")
	public List<Long> findUserIdsByEventIdAndApplyStatus(Long eventId, Integer applyStatus);
	
	public List<EventAttendanceEntity> findByUserIdAndEventId(Long userId, Long eventId, Pageable pageable);
	
	@Query(value="SELECT A FROM EventAttendanceEntity A inner join LineUserEntity L on L.id = A.userId and L.uid = :uid inner join EventEntity E on E.id = A.eventId and E.userStatus in :status and E.beginDatetime <= :date and E.endDatetime >= :date where A.userId = L.id ORDER BY A.id DESC")
	public List<EventAttendanceEntity> findByUidAndUserStatusAndDate(@Param("uid") String uid, @Param("status") List<String> status, @Param("date") Date date, Pageable pageable);

	@Query(value="SELECT A FROM EventAttendanceEntity A inner join EventEntity E on E.id = A.eventId and E.userStatus in :status and E.beginDatetime <= :date and E.endDatetime >= :date WHERE A.userId = :userId AND A.eventId = :eventId ORDER BY A.id DESC")
	public List<EventAttendanceEntity> findByUserIdAndEventIdAndUserStatusAndDate(@Param("userId") Long userId, @Param("eventId") Long eventId, @Param("status") List<String> status, @Param("date") Date date, Pageable pageable);
	
	@Query(value="SELECT A FROM EventAttendanceEntity A inner join EventEntity E on E.id = A.eventId and E.userStatus in :status and E.beginDatetime <= :date and E.endDatetime >= :date WHERE A.userId = :userId AND A.applyStatus NOT IN :applyStatus ORDER BY A.id DESC")
	public List<EventAttendanceEntity> findByUserIdAndUserStatusAndDateAndNotInApplyStatus(@Param("userId") Long userId, @Param("status") List<String> status, @Param("date") Date date, @Param("applyStatus") List<Integer> applyStatus, Pageable pageable);

	@Transactional
	@Modifying
	@Query(value="UPDATE EventAttendanceEntity E SET E.inputErrorCount = E.inputErrorCount+1 WHERE E.id = :id")
	public void incrementInputErrorCountById(@Param("id") Long id);

	
	//wait for delete
	@Transactional
	@Modifying
	@Query(value="UPDATE EventAttendanceEntity E SET E.inputErrorCount = E.inputErrorCount+1 WHERE E.userId = :userId AND E.eventId = :eventId")
	public void incrementInputErrorCountByUserIdAndEventId(@Param("userId") Long userId, @Param("eventId") Long eventId);

	@Transactional
	@Modifying
	@Query(value="UPDATE EventAttendanceEntity E SET E.applyStatus = :applyStatus, E.applyDate = :applyDate WHERE E.id = :id")
	public void updateApplyStatusAndApplyDateById(@Param("id") Long id, @Param("applyStatus") Integer applyStatus, @Param("applyDate") ZonedDateTime applyDate);
	
	@Transactional
	@Modifying
	@Query(value="UPDATE EventAttendanceEntity E SET E.applyStatus = :applyStatus, E.applyDate = :applyDate WHERE E.id IN :ids")
	public void updateApplyStatusAndApplyDateByIds(@Param("ids") List<Long> ids, @Param("applyStatus") Integer applyStatus, @Param("applyDate") ZonedDateTime applyDate);

	@Transactional
	@Modifying
	@Query(value="UPDATE EventAttendanceEntity E SET E.inputErrorCount = :count WHERE E.userId = :userId AND E.eventId = :eventId")
	public void updateInputErrorCountByEventIdAndUserId(@Param("eventId") Long eventId, @Param("userId") Long userId, @Param("count") Integer count);
	
	@Transactional
	@Modifying
	@Query(value="UPDATE EventAttendanceEntity E SET E.applyStatus = :applyStatus WHERE E.eventId = :eventId AND E.userId IN :userIds")
	public void updateApplyStatusByEventIdAndUserIds(@Param("eventId") Long eventId, @Param("userIds") List<Long> userIds, @Param("applyStatus") Integer applyStatus);

	
}

package com.hpifive.line.bcs.webhook.repository;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.hpifive.line.bcs.webhook.entities.EventAttendanceAttributeEntity;

public interface EventAttendanceAttributeRepository extends CrudRepository<EventAttendanceAttributeEntity, Long>, JpaSpecificationExecutor<EventAttendanceAttributeEntity> {
	
	public List<EventAttendanceAttributeEntity> findByAttendanceIdOrderByCreateTimeDesc(Long attendanceId, Pageable pageable);
	
	public List<EventAttendanceAttributeEntity> findByAttendanceIdOrderByCreateTimeAsc(Long attendanceId);
	
	@Query(nativeQuery=true, value="SELECT distinct A.lineuser_id FROM event_attendance A WHERE A.event_id = :eventId AND A.apply_status = :applyStatus AND A.lineuser_id IN (SELECT id FROM bcs.lineuser where status = :userStatus) AND A.lineuser_id in (select b.lineuser_id from event_attendance_attribute b where b.event_attendance_id = A.id :filters group by b.lineuser_id having count(b.lineuser_id) > :filterLength) order by A.lineuser_id \n#pageable\n")
	public List<Long> getUserIdsByfilterUserAttribute(@Param("eventId") Long eventId, @Param("applyStatus") Integer applyStatus, @Param("userStatus") Integer userStatus, @Param("filters") String filters, @Param("filterLength") Integer filterLength, Pageable pageable);

	
	@Query(value="select A.lineUserId from EventAttendanceAttributeEntity A left join EventAttendanceAttributeEntity B on A.lineUserId = B.lineUserId and B.eventId = :eventId and A.eventId = B.eventId and (A.key = :oneKey and A.value = :oneValue) and (B.key = :twoKey and B.value = :twoValue) where A.eventId = :eventId group by A.lineUserId having A.lineUserId not in (SELECT D.userId FROM EventPrizeDetailEntity D WHERE D.eventPrizeId = :eventPrizeId and D.userId IS NOT NULL)")
	public Page<Long> findUnSendAttendanceIdsByTwoColumns(@Param("eventId") Long eventId, @Param("eventPrizeId") Long eventPrizeId, @Param("oneKey") String oneKey, @Param("oneValue") String oneValue, @Param("twoKey") String twoKey, @Param("twoValue") String twoValue, Pageable pageable);
	
	@Query(value="select A.lineuser_id from event_attendance_attribute A left join event_attendance_attribute B on A.lineuser_id = B.lineuser_id and B.event_id = :eventId and A.event_id = B.event_id where A.event_id = :eventId and (A.attr_key = :oneKey and A.attr_value = :oneValue) and (B.attr_key = :twoKey and B.attr_value = :twoValue) and A.lineuser_id not in (SELECT D.used_lineuser_id FROM event_prize_detail D WHERE D.event_prize_id = :eventPrizeId and D.used_lineuser_id IS NOT NULL) AND A.lineuser_id NOT IN (SELECT T.id FROM lineuser T WHERE T.status = 9) GROUP BY A.lineuser_id ORDER BY rand() LIMIT :limit", nativeQuery=true)
	public List<BigInteger> findRandomUnSendAttendanceIdsByTwoColumns(@Param("eventId") Long eventId, @Param("eventPrizeId") Long eventPrizeId, @Param("oneKey") String oneKey, @Param("oneValue") String oneValue, @Param("twoKey") String twoKey, @Param("twoValue") String twoValue, @Param("limit") Integer limit);
	
	@Query(value="select A.lineuser_id from event_attendance_attribute A left join event_attendance_attribute B on A.lineuser_id = B.lineuser_id where A.event_id = :eventId and (A.attr_key = :oneKey and A.attr_value = :oneValue) and (B.attr_key = :twoKey and B.attr_value = :twoValue) and A.lineuser_id in (SELECT D.lineuser_id FROM event_attendance D WHERE D.event_id = :eventId AND D.apply_status = :applyStatus and D.lineuser_id IS NOT NULL) AND A.lineuser_id NOT IN (SELECT T.id FROM lineuser T WHERE T.status = 9) GROUP BY A.lineuser_id ORDER BY rand() LIMIT :limit", nativeQuery=true)
	public List<BigInteger> findRandomUserIdByTwoColumnsAndApplyStatus(@Param("eventId") Long eventId, @Param("oneKey") String oneKey, @Param("oneValue") String oneValue, @Param("twoKey") String twoKey, @Param("twoValue") String twoValue, @Param("applyStatus") Integer applyStatus, @Param("limit") Integer limit);
	
	@Query(value="SELECT A.attendanceId FROM EventAttendanceAttributeEntity A LEFT JOIN EventAttendanceAttributeEntity B ON A.lineUserId = B.lineUserId AND B.eventId = :eventId AND A.eventId = B.eventId WHERE A.eventId = :eventId AND (A.key = :oneKey AND A.value = :oneValue) AND (B.key = :twoKey AND B.value = :twoValue) AND A.lineUserId in (SELECT D.userId FROM EventAttendanceEntity D WHERE D.applyStatus = :applyStatus and D.userId IS NOT NULL) AND A.lineUserId NOT IN (SELECT T.id FROM LineUserEntity T WHERE T.status = 9) GROUP BY A.attendanceId")
	public List<Long> findAttendanceIdByTwoColumnsAndApplyStatus(@Param("eventId") Long eventId, @Param("oneKey") String oneKey, @Param("oneValue") String oneValue, @Param("twoKey") String twoKey, @Param("twoValue") String twoValue, @Param("applyStatus") Integer applyStatus);
	
	
	@Transactional
	public void deleteByAttendanceId(Long attendanceId);

}

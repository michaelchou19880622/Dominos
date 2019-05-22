package com.hpifive.line.bcs.webhook.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.hpifive.line.bcs.webhook.entities.UserClickEntity;

public interface UserClickRepository extends CrudRepository<UserClickEntity, Long> {
	@Query(value="select A.id as id, A.data as keyword, A.userStatus as status, A.period as period, A.creationTime as createtime, A.modifyUser as user,count(distinct U) as count from AutoreplyEntity A "
			+ "inner join AutoreplyDetailEntity D on D.autoreplyID = A.id and D.keyword like %:keyword% "
			+ "inner join UserClickEntity U on A.id = U.mappingId and U.type = :type where (A.period = 'FOREVER' or (A.period ='DAY' and A.beginTime >= :period)) group by A.id, A.data, A.userStatus, A.period, A.creationTime, A.modifyUser")
	public Page<Map<String, Object>> getByKeywordAndPeriod(@Param("keyword") String keyword, @Param("period") Date period, @Param("type") String type, Pageable pageable);

	@Query(value="select A.id as id, A.data as keyword, A.userStatus as status, A.period as period, A.creationTime as createtime, A.modifyUser as user, count(distinct U) as count from AutoreplyEntity A "
			+ "inner join UserClickEntity U on A.id = U.mappingId and U.type = :type where (A.period = 'FOREVER' or (A.period ='DAY' and A.beginTime >= :period)) group by A.id, A.data, A.userStatus, A.period, A.creationTime, A.modifyUser")
	public Page<Map<String, Object>> getByPeriod(@Param("period") Date period, @Param("type") String type, Pageable pageable);

	@Query(value="select U.id from UserClickEntity U where U.mappingId = :id and U.type = :type and U.createTime between :since and :untils group by date_format(U.createTime, '%Y/%m/%d'), U.id")
	public List<Long> getRowCountByMappingIdAndTypeAndBetweenDate(@Param("id") Long mappingId, @Param("type") String type, @Param("since") Date since, @Param("untils") Date untils);
	
	@Query(value="select date_format(c.create_time, '%Y/%m/%d') as date, count(c.lineuser_uid) as count from user_click c where c.mapping_id = :id and c.type = :type and c.create_time between :since and :untils group by date order by date asc limit :page,:pageSize", nativeQuery=true)
	public List<Map<String, String>> getDateAndCountByMappingIdAndType(@Param("id") Long mappingId, @Param("type") String type, @Param("since") Date since, @Param("untils") Date untils, @Param("page") Integer page, @Param("pageSize") Integer pageSize);

	@Query(value="select date_format(c.create_time, '%Y/%m/%d') as date, count(distinct c.lineuser_uid) as count from user_click c where c.mapping_id = :id and c.type = :type and c.create_time between :since and :untils group by date order by date asc limit :page,:pageSize", nativeQuery=true)
	public List<Map<String, String>> getDistinctCountAndDateByMappingIdAndType(@Param("id") Long mappingId, @Param("type") String type, @Param("since") Date since, @Param("untils") Date untils, @Param("page") Integer page, @Param("pageSize") Integer pageSize);

	@Query(value="SELECT DISTINCT U.userUid FROM UserClickEntity U WHERE U.type = :type AND U.mappingId = :mappingId AND U.createTime BETWEEN :since AND :untils")
	public List<String> getUidByTypeAndMappingIdAndCreateTimeBetweenSinceAndUntils(@Param("type") String type, @Param("mappingId") Long mappingId, @Param("since") Date since, @Param("untils") Date untils);
	
}

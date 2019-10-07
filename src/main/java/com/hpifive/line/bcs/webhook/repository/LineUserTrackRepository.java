package com.hpifive.line.bcs.webhook.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.hpifive.line.bcs.webhook.entities.LineUserTrackEntity;

public interface LineUserTrackRepository extends CrudRepository<LineUserTrackEntity, Long> {

	public Integer countBySourceAndCreateTimeBetween(String source, Date since, Date until);
	
	@Query(value="SELECT T.source as source, count(T.id) as count , date_format(T.creation_time, '%Y/%m/%d') as date "
			+ "FROM lineuser_track T WHERE T.source in :state and  date_format(T.creation_time, '%Y/%m/%d') between date_format(:since, '%Y/%m/%d')  and date_format(:untils, '%Y/%m/%d')"
			+ "group by T.source, date_format(T.creation_time, '%Y/%m/%d')", nativeQuery=true)
	public List<Map<String, Object>> countBySourceAndCreateTimeBetween(@Param("since") Date since, @Param("untils") Date until, @Param("state") List<String> state);
}

package com.hpifive.line.bcs.webhook.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.hpifive.line.bcs.webhook.config.DefaultConfig;
import com.hpifive.line.bcs.webhook.entities.UserClickEntity;
import com.hpifive.line.bcs.webhook.entities.config.UserClickType;
import com.hpifive.line.bcs.webhook.repository.UserClickRepository;

@Component
@Repository
public class UserClickDao {

	@Autowired
	private UserClickRepository userClickRepository;
	
	public List<String> getUidByTypeAndMappingBetweenDate(String type, Long mappingId, Date since, Date untils) {
		return this.userClickRepository.getUidByTypeAndMappingIdAndCreateTimeBetweenSinceAndUntils(type, mappingId, since, untils);
	}
	
	public Page<Map<String, Object>> getByPeriod(Date period, Pageable pageable) {
		return this.userClickRepository.getByPeriod(period, UserClickType.AUTOREPLY.toString(), pageable);
	}
	public Page<Map<String, Object>> getByKeywordAndPeriod(String keyword, Date period, Pageable pageable) {
		return this.userClickRepository.getByKeywordAndPeriod(keyword, period, UserClickType.AUTOREPLY.toString(), pageable);
	}
	
	public void save(UserClickEntity entity) {
		this.userClickRepository.save(entity);
	}
	
	public Integer getDateCountPageBy(Long mappingId, String type, Date since, Date untils) {
		List<Long> counts = this.userClickRepository.getRowCountByMappingIdAndTypeAndBetweenDate(mappingId, type, since, untils);
		if (counts == null || counts.isEmpty()) {
			return 0;
		}
		Integer sum = counts.size();
		Double result = (double)sum/(double)DefaultConfig.PAGESIZE.getValue();
		Double temp = Math.ceil(result);
		return temp.intValue();
	}
	
	public List<Map<String, String>> getDateAndCountBy(Long mappingId, String type, Date since, Date untils, Integer page) {
		Integer pageSize = DefaultConfig.PAGESIZE.getValue();	
		Integer pageIndex = page*pageSize;
		return this.userClickRepository.getDateAndCountByMappingIdAndType(mappingId, type, since, untils, pageIndex, pageSize);
	}
	
	public List<Map<String, String>> getDistinctCountCountAndDateBy(Long mappingId, String type, Date since, Date untils, Integer page) {
		Integer pageSize = DefaultConfig.PAGESIZE.getValue();	
		Integer pageIndex = page*pageSize;
		return this.userClickRepository.getDistinctCountAndDateByMappingIdAndType(mappingId, type, since, untils, pageIndex, pageSize);
	}
	
}

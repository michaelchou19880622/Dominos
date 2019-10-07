package com.hpifive.line.bcs.webhook.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.hpifive.line.bcs.webhook.config.DefaultConfig;
import com.hpifive.line.bcs.webhook.entities.LineUserAttributeEntity;
import com.hpifive.line.bcs.webhook.repository.LineUserAttributeRepository;

@Component
@Repository
public class UserAttributeDao {

	private static final Logger logger = LoggerFactory.getLogger(UserAttributeDao.class);
			
	@Autowired
	private LineUserAttributeRepository lineUserAttributeRepository;
	
	public Boolean isAttributePresentByUserIdAndKey(Long userId, String key) {
		LineUserAttributeEntity entity = this.lineUserAttributeRepository.findByLineUserIdAndKey(userId, key);
		return (entity != null);
	}
	
	public void save(LineUserAttributeEntity entity) {
		this.lineUserAttributeRepository.save(entity);
	}
	
	public LineUserAttributeEntity getNewAttributeByUserId(Long userId) {
		LineUserAttributeEntity entity = new LineUserAttributeEntity();
		entity.setLineUserId(userId);
		return entity;
	}
	
	public LineUserAttributeEntity getAttributeByUserIdAndKey(Long userId, String key) {
		LineUserAttributeEntity entity = this.lineUserAttributeRepository.findByLineUserIdAndKey(userId, key);
		if (entity == null) {
			return this.getNewAttributeByUserId(userId);
		}
		return entity;
	}
	
	public LineUserAttributeEntity getAttributeByLineUserIdAndKeyOrNew(Long userId, String key) {
		LineUserAttributeEntity entity = this.lineUserAttributeRepository.findByLineUserIdAndKey(userId, key);
		if (entity == null) {
			logger.debug("Can't find attribute by UserId '{}' and Key '{}'", userId, key);
			return new LineUserAttributeEntity();
		}
		return entity;
	}
	
	public Page<Map<String, Object>> getByKeyAndPage(String key, Integer page) {
		Pageable pageable = PageRequest.of(page, DefaultConfig.PAGESIZE.getValue());
		return this.lineUserAttributeRepository.findByKeyAndPage(key, pageable);
	}
	
	public Map<String, Object> getFirstKeyBySecondKeyRange(String firstKey, String secondKey, String firstKValue, String secondKValue) {
		Map<String, Object> content = new HashMap<>();
		List<Map<String, Object>> results = this.lineUserAttributeRepository.findByTwoKeyAndValueBetweenRange(firstKey, secondKey, firstKValue, secondKValue);
		for (Map<String, Object> map : results) {
			String key = map.get("key").toString();
			Object value = map.get("count");
			content.put(key, value);
		}
		return content;
	}
}

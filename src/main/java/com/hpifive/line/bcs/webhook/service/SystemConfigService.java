package com.hpifive.line.bcs.webhook.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.hpifive.line.bcs.webhook.dao.SystemConfigDao;
import com.hpifive.line.bcs.webhook.entities.config.SystemConfigKeys;

@Service
@Component
public class SystemConfigService {
	
	private static final Logger logger = LoggerFactory.getLogger(SystemConfigService.class);
	
	@Autowired
	private RedisCacheService cacheService;
	@Autowired
	private SystemConfigDao configDao;
	
	public Long getLongByKey(SystemConfigKeys key) {
		String result = this.getByKey(key);
		try {
			return Long.valueOf(result);
		} catch (Exception e) {
			logger.debug(String.format("Error %s", key.getValue()));
			return null;
		}
	}
	
	public Integer getIntByKey(SystemConfigKeys key) {
		String result = this.getByKey(key);
		try {
			return Integer.valueOf(result);
		} catch (Exception e) {
			logger.debug(String.format("Error %s", key.getValue()));
			return null;
		}
	}
	
	public String getByKey(SystemConfigKeys key) {
		return this.configDao.getValueByKey(key.getValue());
	}
	
	public String getCacheOrSqlByKey(SystemConfigKeys key) {
		String result = this.getCacheByKey(key);
		if (result != null) {
			return result;
		}
		result = this.getByKey(key);
		if (result != null) {
			this.cacheService.set(key.getValue(), result);
		}
		return result;
	}
	
	public String getCacheByKey(SystemConfigKeys key) {
		return this.cacheService.get(key.getValue());
	}
	
	public void setCacheByKey(SystemConfigKeys key, String value) {
		this.cacheService.set(key.getValue(), value);
	}
	
	
}

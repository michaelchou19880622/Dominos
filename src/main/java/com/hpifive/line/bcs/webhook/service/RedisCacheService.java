package com.hpifive.line.bcs.webhook.service;

import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hpifive.line.bcs.webhook.config.DefaultConfig;
import com.hpifive.line.bcs.webhook.exception.CustomException;
import com.linecorp.bot.model.message.Message;

import redis.clients.jedis.Jedis;

@Service
public class RedisCacheService {
	
	private static final Logger logger = LoggerFactory.getLogger(RedisCacheService.class);
	
	@Autowired
	private JSONMsgConverterService converterService;
	
	
	@PostConstruct
	private void init() {
		logger.info("RedisCacheService start...");
	}

	@PreDestroy
	private void close() {
		logger.info("RedisCacheService shutdown...");
	}
	
	public void set(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = RedisService.getInstance();
			jedis.setex(key, DefaultConfig.CACHESECOND.getValue(), value);
		} catch (CustomException e) {
			logger.error("Get Redis instance error", e);
		} finally {
			releaseConnection(jedis);
		}
			
	}
	
	public String get(String key) {
		Jedis jedis = null;
		try {
			jedis = RedisService.getInstance();
			return jedis.get(key);
		} catch (Exception e) {
			return null;
		} finally {
			releaseConnection(jedis);
		}
	}
	
	public void setCache(String key, Message msg) {
		try {
			String json = this.converterService.convert(msg);
			this.set(key, json);
		} catch (JsonProcessingException e) {
			logger.error("JSON Processing Exception {}", e.getMessage());
		} catch (Exception e) {
			logger.error("Set cache", e);
		}
	}
	
	public Optional<Message> getCache(String key) {
		try {
			String json = this.get(key);
			if (json != null) {
				logger.info("取得快取內容成功 {}", json);
				return this.converterService.convert(json);
			}
		} catch (Exception e) {
			logger.error("get Cache", e);
		}
		return Optional.empty();
	}
	
	private void releaseConnection(Jedis jedis) {
		if (jedis != null) {
			jedis.close();
		}
	}
}

package com.hpifive.line.bcs.webhook.service;

import java.time.Duration;

import javax.annotation.PreDestroy;

import org.springframework.stereotype.Service;

import com.hpifive.line.bcs.webhook.exception.CustomException;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Service
public class RedisService {
	
	private static JedisPool jedisPool;
	
	private RedisService() {
		super();
		buildPool();
	}

	@PreDestroy
	private void close() {
		if (jedisPool != null) {
			jedisPool.destroy();
		}
	}
	
	private static void buildPool() {
		if (jedisPool == null) {
			final JedisPoolConfig poolConfig = buildPoolConfig();
			jedisPool = new JedisPool(poolConfig, "localhost");
		}
	}
	
	public static Jedis getInstance() throws CustomException {
		try {
			buildPool();
			return jedisPool.getResource();
		} catch (Exception e) {
			throw CustomException.message(String.format("Jedis initialize Error: %s", e.getMessage()));
		}
	}
	
	private static JedisPoolConfig buildPoolConfig() {
		final JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(1000);
		poolConfig.setMaxIdle(100); 
		poolConfig.setMinIdle(16);
		poolConfig.setTestOnBorrow(false);
		poolConfig.setTestOnReturn(false);
		poolConfig.setTestWhileIdle(true);
		poolConfig.setMinEvictableIdleTimeMillis(Duration.ofSeconds(60).toMillis());
		poolConfig.setTimeBetweenEvictionRunsMillis(Duration.ofSeconds(30).toMillis());
		poolConfig.setNumTestsPerEvictionRun(3);
		poolConfig.setBlockWhenExhausted(false);
		return poolConfig;
	}
}

package com.hpifive.line.bcs.webhook.dao;

import java.math.BigInteger;
import java.util.ArrayList;
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

import com.hpifive.line.bcs.webhook.common.MapExtension;
import com.hpifive.line.bcs.webhook.common.OptionalExtension;
import com.hpifive.line.bcs.webhook.common.PageExtension;
import com.hpifive.line.bcs.webhook.entities.LineUserEntity;
import com.hpifive.line.bcs.webhook.entities.config.LineUserStatus;
import com.hpifive.line.bcs.webhook.exception.DaoException;
import com.hpifive.line.bcs.webhook.repository.LineUserRepository;

@Component
@Repository
public class UserDao {
	
	private static final Logger logger = LoggerFactory.getLogger(UserDao.class);
	
	@Autowired
	private LineUserRepository lineUserRepository;
	
	public Map<String, Object> getIdAndUidMapBy(List<Long> ids) {
		if (ids == null || ids.isEmpty()) {
			return null;
		}
		List<Map<String, Object>> listmap = this.lineUserRepository.findIdAndUidByIds(ids);
		return MapExtension.keyValueProcess(listmap);
	}
	
	public String getUidById(Long id) {
		return this.lineUserRepository.findUidById(id);
	}
	
	public List<String> getUidByIds(List<BigInteger> ids) {
		List<Long> userIds = new ArrayList<>(ids.size());
		for (BigInteger i : ids) {
			userIds.add(i.longValue());
		}
		return this.lineUserRepository.findUidByIdIn(userIds);
	}
	
	public List<String> getByIds(List<Long> ids) {
		if (ids == null || ids.isEmpty()) {
			return new ArrayList<>();
		}
		return this.lineUserRepository.findUidByIdIn(ids);
	}
	
	public List<LineUserEntity> getAllByIds(List<Long> ids) {
		if (ids == null || ids.isEmpty()) {
			return null;
		}
		return this.lineUserRepository.findByIdIn(ids);
	}
	public Long getIdByUid(String uuid) {
		Pageable pageable = PageRequest.of(0, 1);
		List<Long> result = this.lineUserRepository.findIdByUid(uuid, pageable);
		if (result == null || result.isEmpty()) {
			return null;
		}
		return result.get(0);
	}
	
	public LineUserEntity addUserByUidIfNotExist(String uid) {
		LineUserEntity user = this.getByUid(uid);
		if (user == null) {
			user = new LineUserEntity();
			user.setUid(uid);
			user.setStatus(LineUserStatus.NORMALLY.getValue());
			this.lineUserRepository.save(user);
		}
		return user;
	}
	
	public void addUserByUid(String uid) {
		try {
			this.setLineUserByUid(uid, LineUserStatus.NORMALLY);
		} catch (Exception e) {
			logger.warn("Add user error {}", e.getMessage());
		}
	}
	
	public void setLineUserByUid(String uid, LineUserStatus status) {
		LineUserEntity user = this.getByUid(uid);
		if (user == null) {
			user = new LineUserEntity();
			user.setUid(uid);
			user.setStatus(status.getValue());
			this.lineUserRepository.save(user);
			return;
		}
		user.setStatus(status.getValue());
		this.lineUserRepository.save(user);
	}
	
	public LineUserEntity getById(Long id) throws DaoException {
		LineUserEntity entity= OptionalExtension.get(this.lineUserRepository.findById(id));
		if (entity == null) {
			throw DaoException.message(String.format("Can't find User by id [ %d ]", id));
		}
		return entity;
	}
	
	public LineUserEntity getByUid(String uid) {
		return this.lineUserRepository.findByUid(uid);
	}
	
	public void setUserStatusByUid(String uid, LineUserStatus status) {
		this.setLineUserByUid(uid, status);
	}
	
	public List<LineUserEntity> getUnUseUserByEventPrizeIdAndLimit(Long prizeId, Integer limit) {
		Pageable pageable = PageRequest.of(0, limit);
		Page<LineUserEntity> pages = this.lineUserRepository.findUnUseUserBy(prizeId, pageable);
		return PageExtension.getListFromPage(pages);
	}
	
}

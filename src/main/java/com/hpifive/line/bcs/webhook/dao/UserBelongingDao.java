package com.hpifive.line.bcs.webhook.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.hpifive.line.bcs.webhook.entities.LineUserBelongingEntity;
import com.hpifive.line.bcs.webhook.repository.LineUserBelongingRepository;

@Service
@Component
public class UserBelongingDao {

	@Autowired
	private LineUserBelongingRepository repository;
	
	public void save(LineUserBelongingEntity entity) {
		this.repository.save(entity);
	}
	
	public void save(List<LineUserBelongingEntity> entities) {
		this.repository.saveAll(entities);
	}
}

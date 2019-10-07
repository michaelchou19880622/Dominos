package com.hpifive.line.bcs.webhook.repository;

import org.springframework.data.repository.CrudRepository;

import com.hpifive.line.bcs.webhook.entities.LineUserBelongingEntity;

public interface LineUserBelongingRepository extends CrudRepository<LineUserBelongingEntity, Long> {

}

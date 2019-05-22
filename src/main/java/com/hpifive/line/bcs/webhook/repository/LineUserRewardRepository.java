package com.hpifive.line.bcs.webhook.repository;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.CrudRepository;

import com.hpifive.line.bcs.webhook.entities.LineUserRewardEntity;

public interface LineUserRewardRepository extends CrudRepository<LineUserRewardEntity, Long> {

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	public LineUserRewardEntity findByEventIdAndUserIdAndCardId(Long eventId, Long userId, Long cardId);

}

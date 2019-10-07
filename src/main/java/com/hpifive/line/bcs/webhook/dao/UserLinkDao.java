package com.hpifive.line.bcs.webhook.dao;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.hpifive.line.bcs.webhook.entities.LineUserLinkEntity;
import com.hpifive.line.bcs.webhook.entities.config.LineUserBindStatus;
import com.hpifive.line.bcs.webhook.repository.LineUserLinkRepository;

@Component
@Repository
public class UserLinkDao {

	@Autowired
	private LineUserLinkRepository lineUserLinkRepository;
	
	public String getUserLinkStatusById(Long userId) {
		return this.lineUserLinkRepository.findLinkedByUserId(userId);
	}
	
	public String getUserLinkStatusByUid(String uid) {
		return this.lineUserLinkRepository.findLinkedByUid(uid);
	}
	
	public LineUserBindStatus getStatusByUid(String uid) {
		String status = this.getUserLinkStatusByUid(uid);
		return LineUserBindStatus.BINDED.getValues().equals(status) ? LineUserBindStatus.BINDED : LineUserBindStatus.UNBINDED;
	}
	
	public List<LineUserBindStatus> getListStatusByUserId(Long userId) {
		LineUserBindStatus status = this.getStatusByUserId(userId);
		return Arrays.asList(LineUserBindStatus.ALL, status);
	}
	
	
	public LineUserBindStatus getStatusByUserId(Long userId) {
		String status = this.getUserLinkStatusById(userId);
		return LineUserBindStatus.BINDED.getValues().equals(status) ? LineUserBindStatus.BINDED : LineUserBindStatus.UNBINDED;
	}
	
	public void setUserLinkByUserId(Long userId, String status) {
		LineUserLinkEntity entity = this.getByUserIdOrNew(userId);
		entity.setLinked(status);
		this.lineUserLinkRepository.save(entity);
	}
	
	public LineUserLinkEntity getByUserIdOrNew(Long userId) {
		LineUserLinkEntity entity = this.lineUserLinkRepository.findByUserId(userId);
		if (entity == null) {
			entity = new LineUserLinkEntity();
			entity.setUserId(userId);
		}
		return entity;
	}
}

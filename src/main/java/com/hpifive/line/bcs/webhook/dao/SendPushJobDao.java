package com.hpifive.line.bcs.webhook.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.hpifive.line.bcs.webhook.entities.SendMessageUsersPushJobEntity;
import com.hpifive.line.bcs.webhook.repository.SendMessageUsersPushJobRepository;
import com.hpifive.line.bcs.webhook.repository.SendMessageUsersRepository;

@Service
@Component
public class SendPushJobDao {
	
	@Autowired
	private SendMessageUsersPushJobRepository repository;
	@Autowired
	private SendMessageUsersRepository usersRepository;
	
	public void setJobBy(Long sendId) {
		SendMessageUsersPushJobEntity entity = this.repository.findBySendId(sendId);
		if (entity == null) {
			entity = new SendMessageUsersPushJobEntity(null, sendId, 0L);
		} else {
			List<Long> indexs = this.usersRepository.findLastIndexBySendIdOrderByIdDesc(sendId, PageRequest.of(0, 1));
			if (indexs != null && ! indexs.isEmpty()) {
				Long index = entity.getIndex() >= indexs.get(0) ? 0L : entity.getIndex();
				entity.setIndex(index);
			}
		}
		this.repository.save(entity);
	}
	
}
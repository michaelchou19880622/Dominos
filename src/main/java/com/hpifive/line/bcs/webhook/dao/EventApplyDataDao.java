package com.hpifive.line.bcs.webhook.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.hpifive.line.bcs.webhook.common.PageExtension;
import com.hpifive.line.bcs.webhook.entities.EventApplydataEntity;
import com.hpifive.line.bcs.webhook.repository.EventApplyDataRepository;

@Service
@Component
public class EventApplyDataDao {
	
	@Autowired
	private EventApplyDataRepository repository;
	
	public Integer countByEventId(Long eventId) {
		return this.repository.countByEventId(eventId);
	}
	
	public EventApplydataEntity getByEventIdAndPage(Long eventId, Integer page) {
		if (page == null) {
			return null;
		}
		Integer pageContentCount = 1;
		Pageable pageable = PageRequest.of(page, pageContentCount);
		Page<EventApplydataEntity> pages = this.repository.findByEventIdOrderByOrderIndexDesc(eventId, pageable);
		return PageExtension.getFirstFromPage(pages);
	}
	
}

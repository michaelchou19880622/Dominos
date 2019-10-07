package com.hpifive.line.bcs.webhook.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.hpifive.line.bcs.webhook.entities.EventPrizeDetailEntity;
import com.hpifive.line.bcs.webhook.repository.EventPrizeDetailRepository;

@Service
@Component
public class EventPrizeDetailDao {
	
	@Autowired
	private EventPrizeDetailRepository repository;
	
	public void save(EventPrizeDetailEntity s) {
		this.repository.save(s);
	}
	
	public void save(List<EventPrizeDetailEntity> s) {
		this.repository.saveAll(s);
	}
	
	public Integer countUnSendTicketByPrizeId(Long eventPrizeId) {
		Integer count = this.repository.countUnSendPrizeByPrizeId(eventPrizeId);
		if (count == null) {
			return 0;
		}
		return count;
	}
	
	public List<EventPrizeDetailEntity> getUnSendTicketBy(Long eventPrizeId, Integer limit) {
		Pageable pageable = PageRequest.of(0, limit);
		return this.repository.findUnSendTicketByPrizeId(eventPrizeId, pageable);
	}
}

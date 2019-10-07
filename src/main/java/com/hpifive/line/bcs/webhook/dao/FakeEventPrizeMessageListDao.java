package com.hpifive.line.bcs.webhook.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.hpifive.line.bcs.webhook.common.PageExtension;
import com.hpifive.line.bcs.webhook.config.DefaultConfig;
import com.hpifive.line.bcs.webhook.entities.FakeFlexMessageEntity;
import com.hpifive.line.bcs.webhook.repository.FakeFlexMessageRepository;

@Service
@Component
public class FakeEventPrizeMessageListDao {

	@Autowired
	private FakeFlexMessageRepository repository;
	
	public String getByEventPrizeId(Long eventPrizeId) {
		Pageable pageable = PageRequest.of(0, DefaultConfig.LINEMSGSIZE.getValue());
		Page<FakeFlexMessageEntity> pages = this.repository.findByEventPrizeId(eventPrizeId, pageable);
		FakeFlexMessageEntity flexMessageEntity = PageExtension.getFirstFromPage(pages);
		if (flexMessageEntity == null) {
			return null;
		}
		return flexMessageEntity.getContent();
	}
	
}

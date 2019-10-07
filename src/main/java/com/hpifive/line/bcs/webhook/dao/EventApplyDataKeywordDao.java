package com.hpifive.line.bcs.webhook.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.hpifive.line.bcs.webhook.common.PageExtension;
import com.hpifive.line.bcs.webhook.entities.EventApplydataKeywordEntity;
import com.hpifive.line.bcs.webhook.entities.config.KeywordEventTypes;
import com.hpifive.line.bcs.webhook.repository.EventApplyDataKeywordRepository;

@Service
@Component
public class EventApplyDataKeywordDao {
	
	@Autowired
	private EventApplyDataKeywordRepository repository;
	
	public EventApplydataKeywordEntity getByEventApplyDataIdAndWithoutTypeError(Long eventApplyDataId) {
		Pageable pageable = PageRequest.of(0, 1);
		Page<EventApplydataKeywordEntity> pages = this.repository.findByEventApplydataIdAndNotKeywordEvent(eventApplyDataId, KeywordEventTypes.CUSTOMIZE_INPUT_ERROR.toString(), pageable);
		return PageExtension.getFirstFromPage(pages);
	}
	
	public boolean isExistInKeywordListByApplyDataIdAndKeyword(Long id, String keyword) {
		Integer count = this.repository.countByApplydataIdAndKeyword(id, keyword);
		if (count == null) {
			return false;
		}
		return (count > 0);
	}
	
	public boolean isExistByApplyDataIdAndApplyStatus(Long applyDataId, Integer page) {
		EventApplydataKeywordEntity entity = this.getByApplyDataIdAndPage(applyDataId, page);
		return (entity != null);
	}
	
	public EventApplydataKeywordEntity getByApplyDataIdAndPage(Long applyDataId, Integer page) {
		Pageable pageable = PageRequest.of(page, 1);
		Page<EventApplydataKeywordEntity> pages = this.repository.findByApplyDataId(applyDataId, pageable);
		return PageExtension.getFirstFromPage(pages);
	}
	
	public boolean isExistByApplyDataIdAndKeywordEventAndApplyStatus(Long applyDataId, String keywordEvent, Integer page) {
		EventApplydataKeywordEntity entity = this.getByApplyDataIdAndKeywordEventAndPage(applyDataId, keywordEvent, page);
		return (entity != null);
	}
	
	public EventApplydataKeywordEntity getByApplyDataIdAndKeywordEventAndPage(Long applyDataId, String keywordEvent, Integer page) {
		Pageable pageable = PageRequest.of(page, 1);
		Page<EventApplydataKeywordEntity> pages = this.repository.findByEventApplydataIdAndKeywordEvent(applyDataId, keywordEvent, pageable);
		return PageExtension.getFirstFromPage(pages);
	}
	
}

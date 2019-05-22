package com.hpifive.line.bcs.webhook.dao;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.hpifive.line.bcs.webhook.common.PageExtension;
import com.hpifive.line.bcs.webhook.config.DefaultConfig;
import com.hpifive.line.bcs.webhook.entities.EventKeywordEntity;
import com.hpifive.line.bcs.webhook.entities.EventKeywordMessageListEntity;
import com.hpifive.line.bcs.webhook.entities.config.KeywordEventTypes;
import com.hpifive.line.bcs.webhook.exception.DaoException;
import com.hpifive.line.bcs.webhook.repository.EventKeywordMessageListRepository;
import com.linecorp.bot.model.message.Message;


@Service
@Component
public class EventKeywordMessageListDao {

private static final Logger logger = LoggerFactory.getLogger(EventKeywordMessageListDao.class);

	@Autowired
	private EventKeywordMessageListRepository repository;
	@Autowired
	private EventKeywordDao keywordDao;
	@Autowired
	private MsgDao msgDao;
	
	public List<Message> getMessageByEventIdAndKeywordTypes(Long eventId, KeywordEventTypes eventTypes) throws DaoException {
		try {
			Pageable pageable = PageRequest.of(0, DefaultConfig.LINEMSGSIZE.getValue());
			EventKeywordEntity eventKeywordEntity = keywordDao.getByIdAndTypes(eventId, eventTypes);
			if (eventKeywordEntity == null) {
				logger.error("no msg found with eventId {} type {}", eventId, eventTypes);
				return new ArrayList<>();
			}
			Page<EventKeywordMessageListEntity> pages = this.repository.findByEventKeywordIdOrderByOrderIndexAsc(eventKeywordEntity.getId(), pageable);
			List<Message> resultContent = this.getMessagesByPages(pages);
			if (resultContent == null || resultContent.isEmpty()) {
				logger.error("no msg found with eventId {} type {}", eventId, eventTypes);
				return new ArrayList<>();
			}
			return resultContent;
		} catch (Exception e) {
			logger.error("eventId {} KeywordEventTypes {}", eventId, eventTypes);
			throw new DaoException(e);
		}
	}
	
	protected List<Message> getMessagesByPages(Page<EventKeywordMessageListEntity> pages) throws DaoException  {
		List<Message> resultContent = new ArrayList<>();
		List<EventKeywordMessageListEntity> pageContent = PageExtension.getListFromPage(pages);
		if (pageContent == null || pageContent.isEmpty()) {
			return resultContent;
		}
		for (EventKeywordMessageListEntity entity : pageContent) {
			Message message = this.msgDao.get(entity.getMessageType(), entity.getMessageId());
			if (message != null) {
				resultContent.add(message);
			}
		}
		return resultContent;
	}
	
}

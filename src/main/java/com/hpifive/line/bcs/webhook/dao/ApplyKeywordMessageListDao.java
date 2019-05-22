package com.hpifive.line.bcs.webhook.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.hpifive.line.bcs.webhook.config.DefaultConfig;
import com.hpifive.line.bcs.webhook.entities.EventApplydataEntity;
import com.hpifive.line.bcs.webhook.entities.EventApplydataKeywordEntity;
import com.hpifive.line.bcs.webhook.entities.EventApplydataKeywordMessageListEntity;
import com.hpifive.line.bcs.webhook.entities.config.KeywordEventTypes;
import com.hpifive.line.bcs.webhook.exception.DaoException;
import com.linecorp.bot.model.message.Message;

@Service
@Component
public class ApplyKeywordMessageListDao {
	
	@Autowired
	private EventApplyDataDao applyDao;
	@Autowired
	private EventApplyDataKeywordDao keywordDao;
	@Autowired
	private MsgDao msgDao;
	
	public List<Message> getMessageByEventIdAndPage(Long eventId, Integer page, boolean withoutTypeError) throws DaoException {
		EventApplydataEntity entity = this.applyDao.getByEventIdAndPage(eventId, page);
		if (entity == null) {
			throw DaoException.message(String.format("no msg found with eventId %d And Page %d", eventId, page));
		}
		if (withoutTypeError) {
			return this.getMessageByEventApplydataIdWithOutError(entity.getId());
		}
		return this.getMessageByEventApplydataId(entity.getId());
	}
	
	public List<Message> getMessageByEventApplydataId(Long eventApplyDataId) throws DaoException {
		EventApplydataKeywordEntity keywordEntity = this.keywordDao.getByApplyDataIdAndKeywordEventAndPage(eventApplyDataId, KeywordEventTypes.CUSTOMIZE_INPUT_ERROR.toString(), 0);
		if (keywordEntity == null) {
			throw DaoException.message(String.format("no msg found with eventApplyDataId %d", eventApplyDataId));
		}
		return this.getMessageByEventApplydataKeywordId(keywordEntity);
	}
	
	public List<Message> getMessageByEventApplydataIdWithOutError(Long eventApplyDataId) throws DaoException {
		EventApplydataKeywordEntity keywordEntity = this.keywordDao.getByEventApplyDataIdAndWithoutTypeError(eventApplyDataId);
		if (keywordEntity == null) {
			throw DaoException.message(String.format("no msg found with eventApplyDataId %d", eventApplyDataId));
		}
		return this.getMessageByEventApplydataKeywordId(keywordEntity);
	}
	
	public List<Message> getMessageByEventApplydataKeywordId(EventApplydataKeywordEntity entity) throws DaoException {
		List<EventApplydataKeywordMessageListEntity> entities = entity.getEventtApplydataKeywordMessageListEntities();
		if (entities.size() > 5) {
			entities = entities.subList(0, DefaultConfig.LINEMSGSIZE.getValue());
		}
		List<Message> resultContent = this.getMessagesBy(entities);
		if (resultContent == null || resultContent.isEmpty()) {
			throw DaoException.message(String.format("no msg found with eventApplydataKeywordId %d", entity.getId()));
		}
		return resultContent;
	}
	
	protected List<Message> getMessagesBy(List<EventApplydataKeywordMessageListEntity> entities) throws DaoException  {
		List<Message> resultContent = new ArrayList<>();
		if (entities == null) {
			return new ArrayList<>();
		}
		for (EventApplydataKeywordMessageListEntity entity : entities) {
			Message message = this.msgDao.get(entity.getMessageType(), entity.getMessageId());
			if (message != null) {
				resultContent.add(message);
			}
		}
		return resultContent;
	}
	
}

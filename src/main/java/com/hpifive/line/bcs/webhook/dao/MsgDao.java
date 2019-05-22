package com.hpifive.line.bcs.webhook.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hpifive.line.bcs.webhook.common.OptionalExtension;
import com.hpifive.line.bcs.webhook.common.TemplateProducer;
import com.hpifive.line.bcs.webhook.entities.MessageCarouselActionEntity;
import com.hpifive.line.bcs.webhook.entities.MessageCarouselColumnEntity;
import com.hpifive.line.bcs.webhook.entities.MessageCarouselTemplateEntity;
import com.hpifive.line.bcs.webhook.entities.MessageImagemapActionEntity;
import com.hpifive.line.bcs.webhook.entities.MessageImagemapEntity;
import com.hpifive.line.bcs.webhook.entities.MessageTemplateActionEntity;
import com.hpifive.line.bcs.webhook.entities.MessageTemplateEntity;
import com.hpifive.line.bcs.webhook.entities.config.MessageTypes;
import com.hpifive.line.bcs.webhook.exception.DaoException;
import com.hpifive.line.bcs.webhook.repository.MessageAudioRepository;
import com.hpifive.line.bcs.webhook.repository.MessageCarouselActionRepository;
import com.hpifive.line.bcs.webhook.repository.MessageCarouselColumnRepository;
import com.hpifive.line.bcs.webhook.repository.MessageCarouselTemplateRepository;
import com.hpifive.line.bcs.webhook.repository.MessageImageRepository;
import com.hpifive.line.bcs.webhook.repository.MessageImagemapActionRepository;
import com.hpifive.line.bcs.webhook.repository.MessageImagemapRepository;
import com.hpifive.line.bcs.webhook.repository.MessageLocationRepository;
import com.hpifive.line.bcs.webhook.repository.MessageStickerRepository;
import com.hpifive.line.bcs.webhook.repository.MessageTemplateActionRepository;
import com.hpifive.line.bcs.webhook.repository.MessageTemplateRepository;
import com.hpifive.line.bcs.webhook.repository.MessageTextRepository;
import com.hpifive.line.bcs.webhook.repository.MessageVideoRepository;
import com.hpifive.line.bcs.webhook.service.JSONMsgConverterService;
import com.hpifive.line.bcs.webhook.service.RedisCacheService;
import com.linecorp.bot.model.action.Action;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.imagemap.ImagemapAction;
import com.linecorp.bot.model.message.template.CarouselColumn;

@Repository
public class MsgDao {

	private static final Logger logger = LoggerFactory.getLogger(MsgDao.class);
	
	@Autowired
	private RedisCacheService cacheService;
	@Autowired
	private JSONMsgConverterService jsonMsgConverterService;
	@Autowired
	private FlexMessageDao flexDao;
	@Autowired
	private MessageTextRepository messageTextRepository;
	@Autowired
	private MessageStickerRepository messageStickerRepository;
	@Autowired
	private MessageImageRepository messageImageRepository;
	@Autowired
	private MessageVideoRepository messageVideoRepository;
	@Autowired
	private MessageAudioRepository messageAudioRepository;
	@Autowired
	private MessageLocationRepository messageLocationRepository;
	@Autowired
	private MessageTemplateRepository messageTemplateRepository;
	@Autowired
	private MessageTemplateActionRepository messageTemplateActionRepository;
	@Autowired
	private MessageCarouselTemplateRepository messageCarouselTemplateRepository;
	@Autowired
	private MessageCarouselColumnRepository messageCarouselColumnRepository;
	@Autowired
	private MessageCarouselActionRepository messageCarouselActionRepository;
	@Autowired
	private MessageImagemapRepository messageImagemapRepository;
	@Autowired
	private MessageImagemapActionRepository messageImagemapActionRepository;
	
	
	public Message get(String type, Integer id) throws DaoException {
		try {
			logger.debug("快取層1");
			Message msg = null;
			Optional<Message> cache = this.cacheService.getCache(String.format("%s%d", type, id));
			if (cache.isPresent()) {
				logger.debug("快取層2有快取");
				return cache.get();
			} else {
				logger.debug("快取層2沒有快取");
				msg = this.getMessageByTypeAndID(type, id);
				logger.debug("快取層3");
				if (msg != null) {
					logger.debug("快取層4");
					this.cacheService.setCache(String.format("%s%d", type, id), msg);
					return msg;
				}
			}
		} catch (Exception e) {
			throw new DaoException(e);
		}
		throw DaoException.message(String.format("result is not found with type %s and id %d", type,id));
	}

	private Message getFlexMessageById(Long id) {
		return this.flexDao.getFlexMessageById(id);
	}
	
	private Message getMessageByTypeAndID(String type, Integer id) throws DaoException {
		try {
			Message msg = null;
			if ( type.equals( MessageTypes.FLEX.toString() ) ) {
				msg = this.getFlexMessageById(id.longValue());
			} else if ( type.equals( MessageTypes.TEXT.toString() ) ) {
				msg = OptionalExtension.get(this.messageTextRepository.findById(id));
			} else if (type.equals(MessageTypes.IMAGE.toString())) {
				msg = OptionalExtension.get(this.messageImageRepository.findById(id));
			} else if (type.equals(MessageTypes.AUDIO.toString())) {
				msg = OptionalExtension.get(this.messageAudioRepository.findById(id));
			} else if (type.equals(MessageTypes.VIDEO.toString())) {
				msg = OptionalExtension.get(this.messageVideoRepository.findById(id));
			} else if (type.equals(MessageTypes.STICKER.toString())) {
				msg = OptionalExtension.get(this.messageStickerRepository.findById(id));
			} else if (type.equals(MessageTypes.LOCATION.toString())) {
				msg = OptionalExtension.get(this.messageLocationRepository.findById(id));
			} else if (type.equals(MessageTypes.TEMPLATE.toString())) {
				MessageTemplateEntity content = OptionalExtension.get(this.messageTemplateRepository.findById(id));
				msg = this.getTemplateByMsg(content);
			} else if (type.equals(MessageTypes.CAROUSEL.toString())) {
				MessageCarouselTemplateEntity content =  OptionalExtension.get(this.messageCarouselTemplateRepository.findById(id));
				msg = this.getCarouselByMsg(content);
			} else if (type.equals(MessageTypes.IMAGEMAP.toString())) {
				MessageImagemapEntity content = OptionalExtension.get(this.messageImagemapRepository.findById(id));
				msg = this.getImagemapMsg(content);
			}
			return OptionalExtension.get(this.jsonMsgConverterService.convertBy(msg));
		} catch (NoSuchElementException elementException) {
			return null;
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	private Message getImagemapMsg(MessageImagemapEntity m) {
		if (m == null) {
			return null;
		}
		List<MessageImagemapActionEntity> actions = this.messageImagemapActionRepository.findAllByMessageId(m.getId());
		List<ImagemapAction> resAction = TemplateProducer.imagemapAction(actions);
		return TemplateProducer.imagemap(m, resAction);
	}
	
	private Message getCarouselByMsg(MessageCarouselTemplateEntity t) {
		if (t== null) {
			return null;
		}
		List<CarouselColumn> resultColumn = new ArrayList<>();
		List<MessageCarouselColumnEntity> columns = this.messageCarouselColumnRepository.findAllByCarouselId(t.getId());
		for (MessageCarouselColumnEntity col : columns) {
			List<MessageCarouselActionEntity> messageCarouselActions = this.messageCarouselActionRepository.findByTemplateIdAndTemplateType(col.getId(), col.getType());
			List<Action> actions = TemplateProducer.carouselAction(messageCarouselActions);
			CarouselColumn tempC = TemplateProducer.carouselColumn(col, actions);
			if (tempC != null) {
				resultColumn.add(tempC);
			}
		}
		return TemplateProducer.carouselTemplate(t, resultColumn);
	}
	
	private Message getTemplateByMsg(MessageTemplateEntity t) {
		if (t == null) {
			return null;
		}
		List<MessageTemplateActionEntity> msgActions = this.messageTemplateActionRepository.findByTemplateIdAndTemplateType(t.getId(), t.getType());
		List<Action> actions = TemplateProducer.action(msgActions);
		return TemplateProducer.template(t, actions);
	}
}

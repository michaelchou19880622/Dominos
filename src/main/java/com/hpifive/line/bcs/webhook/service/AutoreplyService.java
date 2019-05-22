package com.hpifive.line.bcs.webhook.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.hpifive.line.bcs.webhook.dao.ReplyMsgDao;
import com.hpifive.line.bcs.webhook.entities.AutoreplyMessageListEntity;
import com.hpifive.line.bcs.webhook.entities.config.AutoreplyDefaultKeywords;
import com.hpifive.line.bcs.webhook.entities.config.AutoreplyType;
import com.hpifive.line.bcs.webhook.repository.AutoreplyMessageListRepository;
import com.linecorp.bot.model.message.Message;

@Service
@Component
public class AutoreplyService {

	private static final Logger logger = LoggerFactory.getLogger(AutoreplyService.class);

	@Autowired
	private AutoreplyMessageListRepository listRepository;

	@Autowired
	private ReplyMsgDao replyMsgDao;

	public List<Message> getByKeywordAndType(AutoreplyDefaultKeywords keyword, AutoreplyType type) {
		return this.getByKeywordAndType(keyword.toString(), type.toString());
	}

	public List<Message> getByKeywordAndType(String keyword, AutoreplyType type) {
		return this.getByKeywordAndType(keyword, type.toString());
	}

	public List<Message> getByKeywordAndType(String keyword, String type) {
		try {
			Pageable pageable = PageRequest.of(0, 5);
			List<AutoreplyMessageListEntity> list = this.listRepository.findListByKeyWordAndType(keyword, type,
					new Date(), pageable);
			return this.replyMsgDao.getResultsByMsgList(list);
		} catch (Exception e) {
			logger.error("Can't find Message with Autoreply keyword {} and type {}. {}", keyword, type, e);
			return null;
		}
	}
}

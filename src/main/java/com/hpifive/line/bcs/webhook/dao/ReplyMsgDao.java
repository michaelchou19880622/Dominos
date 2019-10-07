package com.hpifive.line.bcs.webhook.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.hpifive.line.bcs.webhook.entities.AutoreplyMessageListEntity;
import com.hpifive.line.bcs.webhook.entities.config.LineUserBindStatus;
import com.hpifive.line.bcs.webhook.exception.DaoException;
import com.hpifive.line.bcs.webhook.repository.AutoreplyDetailRepository;
import com.hpifive.line.bcs.webhook.repository.AutoreplyMessageListRepository;
import com.linecorp.bot.model.message.Message;

@Repository
public class ReplyMsgDao {
	
	@Autowired
	private MsgDao msgDao;
	
	@Autowired
	private AutoreplyDetailRepository autoreplyDetailRepository;
	
	@Autowired
	private AutoreplyMessageListRepository autoreplyMessageListRepository;
	
	public Long getIdByKeyWord(String keyword) {
		Date date = new Date();
		Pageable pageable = PageRequest.of(0, 1);
		Page<Long> page = this.autoreplyDetailRepository.findOneAutoreplyIDByKeyword(keyword, date,  pageable);
		List<Long> content = page.getContent();
		if (content == null || content.isEmpty()) {
			return null;
		}
		return content.get(content.size()-1);
	}
	
	public List<Message> getMsgsByTextAndType(String text) throws DaoException {
		Pageable pageable = PageRequest.of(0, 5);
		Page<AutoreplyMessageListEntity> msgPage = this.autoreplyMessageListRepository.getListByKeyWord(text, new Date(), pageable);
		List<AutoreplyMessageListEntity> msgList = msgPage.getContent();
		return this.getResultsByMsgList(msgList);
	}
	
	public List<Message> getMsgsByTextAndTypeAndUserStatus(String text, LineUserBindStatus status) throws DaoException {
		List<String> allowStatus = Arrays.asList(LineUserBindStatus.ALL.toString(), status.toString());
		Pageable pageable = PageRequest.of(0, 5);
		Page<AutoreplyMessageListEntity> msgPage = this.autoreplyMessageListRepository.getListByKeyWordAndDateAndStatus(text, new Date(), allowStatus, pageable);
		List<AutoreplyMessageListEntity> msgList = msgPage.getContent();
		return this.getResultsByMsgList(msgList);
	}
	
	public List<Message> getResultsByMsgList(List<AutoreplyMessageListEntity> msgList) throws DaoException {
		List<Message> results = new ArrayList<>();
		for (AutoreplyMessageListEntity m : msgList) {
			Integer messageID = m.getMessageID();
			String messageType = m.getMessageType();
			Message message = this.msgDao.get(messageType, messageID);
			if (message != null) {
				results.add(message);
			}
 		}
		return results;
	}

	
}

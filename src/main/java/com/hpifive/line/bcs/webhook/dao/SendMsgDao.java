package com.hpifive.line.bcs.webhook.dao;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hpifive.line.bcs.webhook.common.DateTimeModel;
import com.hpifive.line.bcs.webhook.entities.LineUserEntity;
import com.hpifive.line.bcs.webhook.entities.SendMessageEntity;
import com.hpifive.line.bcs.webhook.entities.SendMessageListEntity;
import com.hpifive.line.bcs.webhook.entities.SendMessageUsersEntity;
import com.hpifive.line.bcs.webhook.entities.SendMessageUsersPushJobEntity;
import com.hpifive.line.bcs.webhook.entities.config.SendMessageStatus;
import com.hpifive.line.bcs.webhook.exception.DaoException;
import com.hpifive.line.bcs.webhook.repository.LineUserRepository;
import com.hpifive.line.bcs.webhook.repository.SendMessageListRepository;
import com.hpifive.line.bcs.webhook.repository.SendMessageRepository;
import com.hpifive.line.bcs.webhook.repository.SendMessageUsersPushJobRepository;
import com.hpifive.line.bcs.webhook.repository.SendMessageUsersRepository;
import com.linecorp.bot.model.message.Message;

@Component
@Repository
public class SendMsgDao {
	
	private static final Logger logger = LoggerFactory.getLogger(SendMsgDao.class);
	
	@Autowired
	private SendMessageUsersPushJobRepository pushJobRepository;
	@Autowired
	private SendMessageUsersRepository sendMessageUsersRepository;
	@Autowired
	private SendMessageListRepository sendMessageListRepository;
	@Autowired
	private SendMessageRepository sendMessageRepository;
	@Autowired
	private LineUserRepository lineUserRepository;
	
	@Autowired
	private MsgDao msgDao;
	
	public <S extends SendMessageEntity> S save(S object) {
		return this.sendMessageRepository.save(object);
	}

	public void rewriteSendMessageStatusById(long id, SendMessageStatus status) {
		this.sendMessageRepository.setStatusById(id, status.getValue());
	
	}
	
	public SendMessageEntity getSendMessageById(Long id, SendMessageStatus status) {
		return this.sendMessageRepository.findOneByIdAndStatus(id, status.getValue());
	}
	
	public List<SendMessageEntity> getSendListByStatus(SendMessageStatus status) {
		return this.sendMessageRepository.findAllByStatus(status.getValue());
	}
	
	public SendMessageEntity getSendMessageById(Long id) {
		return this.sendMessageRepository.findOneById(id);
	}
	
	@Transactional(rollbackFor=Exception.class)
	public void setSendUsersStatusAndResponseCodeByIds(List<Long> ids, SendMessageStatus status, Integer responseCode) {
		ZonedDateTime time = DateTimeModel.getTime();
		this.sendMessageUsersRepository.setStatusAndResponseCodeByIds(ids, status.getValue(), responseCode, time);
	}
	public List<SendMessageUsersEntity> getSendUsersBySendId(long id, Pageable pageable) {
		Page<SendMessageUsersEntity> pages = this.sendMessageUsersRepository.findBySendId(id, pageable);
		return pages.getContent();
	}
	
	public List<Long> getSendUserIdBySendId(Long id, Pageable pageable) {
		return this.sendMessageUsersRepository.findUserIdBySendId(id, pageable);
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW, rollbackFor=Exception.class, timeout=10, isolation=Isolation.READ_COMMITTED)
	public List<SendMessageUsersEntity> getUnSendUserId(Long id, Integer size) {
		SendMessageUsersPushJobEntity job = this.pushJobRepository.getBySendId(id);
		if (job == null) {
			job = new SendMessageUsersPushJobEntity(null, id, 0L);
		}
		logger.info("Job get index {}", job.getIndex());
		Pageable pageable = PageRequest.of(0, size);
		List<SendMessageUsersEntity> entities = this.sendMessageUsersRepository.findUserIdBySendIdAndStatus(job.getIndex(), id, pageable);
		if (entities != null && ! entities.isEmpty()) {
			this.sendMessageUsersRepository.setStatusAndResponseCodeById(entities.get(0).getId(), entities.get(entities.size()-1).getId(), SendMessageStatus.SUCCESS.getValue(), 200, DateTimeModel.getTime());
			job.setIndex(entities.get(entities.size()-1).getId());
			this.pushJobRepository.save(job);
			logger.info("Min Id {} Max Id {}", entities.get(0).getId(), entities.get(entities.size()-1).getId());
			logger.info("Job index {}", job.getIndex());
		}
		return entities;
	}
	
	public void saveAllUsers(List<SendMessageUsersEntity> users, SendMessageStatus status) {
		List<Long> ids = new ArrayList<>();
		if (users == null || users.isEmpty()) {
			return;
		}
		for (SendMessageUsersEntity entity : users) {
			ids.add(entity.getId());
		}
		this.setSendUsersStatusAndResponseCodeByIds(ids, status, 200);
	}
	
	
	public List<LineUserEntity> getLineUsersByIds(List<Long> ids) {
		return this.lineUserRepository.findByIdIn(ids);
	}
	public List<String> getUidByUsers(List<LineUserEntity> users) {
		List<String> result = new ArrayList<>();
		for (LineUserEntity lineUser : users) {
			result.add(lineUser.getUid());
		}
		return result;
	}
	public List<Long> getIdBySendUserList(List<SendMessageUsersEntity> sendUserList) {
		List<Long> result = new ArrayList<>();
		for (SendMessageUsersEntity user : sendUserList) {
			result.add(user.getId());
		}
		return result;
	}
	public List<String> getUidsBySendUserList(List<SendMessageUsersEntity> sendUserList) {
		List<Long> ids = new ArrayList<>();
		for (SendMessageUsersEntity sendUser : sendUserList) {
			ids.add(sendUser.getUserId());
		}
		List<LineUserEntity> users = this.getLineUsersByIds(ids);
		return this.getUidByUsers(users);
	}

	public List<Message> getMsgBySendId(long id) throws DaoException {
		Pageable pageable = PageRequest.of(0, 5);
		Page<SendMessageListEntity> pages = this.sendMessageListRepository.getListBySendId(id, pageable);
		List<SendMessageListEntity> list = pages.getContent();
		return this.getMsgBySendList(list);
	}
	
	private List<Message> getMsgBySendList(List<SendMessageListEntity> list) throws DaoException {
		List<Message> messages = new ArrayList<>();
		for (SendMessageListEntity entity : list) {
			Message message = this.msgDao.get(entity.getMessageType(), entity.getMessageId());
			if (message != null) {
				messages.add(message);
			}
		}
		return messages;
	}
	
}

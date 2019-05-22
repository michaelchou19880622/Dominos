package com.hpifive.line.bcs.webhook.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.hpifive.line.bcs.webhook.akka.AkkaService;
import com.hpifive.line.bcs.webhook.common.ListExtension;
import com.hpifive.line.bcs.webhook.dao.SendMsgDao;
import com.hpifive.line.bcs.webhook.dao.UserDao;
import com.hpifive.line.bcs.webhook.entities.SendMessageUsersEntity;
import com.hpifive.line.bcs.webhook.entities.config.SystemConfigKeys;
import com.hpifive.line.bcs.webhook.model.scheduler.AsyncSendBody;
import com.hpifive.line.bcs.webhook.model.scheduler.AsyncSendBodySuccess;
import com.hpifive.line.bcs.webhook.model.scheduler.AsyncSender;
import com.hpifive.line.bcs.webhook.model.scheduler.AsyncSendingModel;
import com.linecorp.bot.model.message.Message;

@Service
@Component
public class AsyncSendService {

	private static final Logger logger = LoggerFactory.getLogger(AsyncSendService.class);
	@Autowired
	private AkkaService akkaService;
	
	@Autowired
	private SystemConfigService configService;
	
	@Autowired
	private SendMsgDao sendMsgDao;
	
	@Autowired
	private UserDao userDao;
	
	public AsyncSender send(AsyncSendBody body) {
		Long sendId = body.getSendId();
		List<Message> messages = body.getMessages();
		try {
			List<Long> sendIds = new ArrayList<>();
			List<Long> userIds = new ArrayList<>();
			List<SendMessageUsersEntity> sendUsers = this.sendMsgDao.getUnSendUserId(sendId, 750);
			for (SendMessageUsersEntity entity : sendUsers) {
				sendIds.add(entity.getId());
				userIds.add(entity.getUserId());
			}
			if (sendIds.isEmpty() || userIds.isEmpty()) {
				return new AsyncSendBodySuccess(sendId);
			}
			List<String> sendUids = userDao.getByIds(userIds);
			List<List<Long>> idChopped = ListExtension.chopped(sendIds, 150);
			List<List<String>> userChopped = ListExtension.chopped(sendUids, 150);
			for (int i=0;i<userChopped.size();i+=1) {
				List<Long> ids = idChopped.get(i);
				List<String> uuids = userChopped.get(i);
				logger.info("chopped size {}", userChopped.size());
				logger.info("user size {}", uuids.size());
				if (sendUids != null && ! sendUids.isEmpty()) {
					AsyncSendingModel sendingModel = new AsyncSendingModel(sendId, ids, uuids, messages);
					this.akkaService.reply(sendingModel);
					this.increNumberOfCall();
				}
			}
		} catch (Exception e) {
			logger.error("AsyncSendService on send fail {}", e);
		}
		return null;
	}
	
	private void setNumberOfCall(Integer count) {
		this.configService.setCacheByKey(SystemConfigKeys.NUMBEROFCALL, String.valueOf(count));
	}
	
	private Integer getNumberOfCall() {
		String value = this.configService.getCacheByKey(SystemConfigKeys.NUMBEROFCALL);
		try {
			if (value == null) {
				return 0;
			}
			return Integer.valueOf(value);
		} catch (Exception e) {
			return 0;
		}
	}
	
	private void increNumberOfCall() {
		Integer value = this.getNumberOfCall()+1;
		this.setNumberOfCall(value);
	}
	
}

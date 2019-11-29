package com.hpifive.line.bcs.webhook.service;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.hpifive.line.bcs.webhook.dao.ReplyMsgDao;
import com.hpifive.line.bcs.webhook.dao.UserClickDao;
import com.hpifive.line.bcs.webhook.entities.UserClickEntity;
import com.hpifive.line.bcs.webhook.entities.config.LineUserBindStatus;
import com.hpifive.line.bcs.webhook.entities.config.UserClickType;

@Service
@Component
public class AutoReplyTrackingService {
	
	private static final Logger logger = LoggerFactory.getLogger(AutoReplyTrackingService.class);
	
	@Autowired
	private ReplyMsgDao replyMsgDao;
	
	@Autowired
	private UserClickDao userClickDao;
	
	public void addMsgByUidAndStatusAndKeyWord(String uid, LineUserBindStatus status, String keyword) {
		try {
			Long replyId = this.replyMsgDao.getIdByKeyWord(keyword);
			if (replyId == null) {
				logger.info("無對應之關鍵字可追蹤: {}", keyword);
				return;
			}
			this.addMsgByUidAndStatusAndReplyId(uid, status, replyId);
		} catch (Exception e) {
			logger.warn(e.getMessage());
		}
	}
	
	public void addMsgByUidAndStatusAndReplyId(String uid, LineUserBindStatus status, Long replyId) {
		try {
			UserClickEntity entity = new UserClickEntity();
			entity.setCreateTime(new Date());
			entity.setUserUid(uid);
			entity.setStatus(status.toString());
			entity.setType(UserClickType.AUTOREPLY.getValues());
			entity.setMappingId(replyId);
			this.userClickDao.save(entity);
		} catch (Exception e) {
			logger.warn(e.getMessage());
		}
	}
}

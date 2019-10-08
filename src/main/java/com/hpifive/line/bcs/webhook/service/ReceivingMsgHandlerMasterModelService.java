package com.hpifive.line.bcs.webhook.service;

import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLEngineResult.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.hpifive.line.bcs.webhook.dao.ReplyMsgDao;
import com.hpifive.line.bcs.webhook.dao.UserDao;
import com.hpifive.line.bcs.webhook.dao.UserLinkDao;
import com.hpifive.line.bcs.webhook.entities.config.LineUserBindStatus;
import com.hpifive.line.bcs.webhook.exception.DaoException;
import com.linecorp.bot.model.message.Message;

@Service
@Component
public class ReceivingMsgHandlerMasterModelService {

	private static final Logger logger = LoggerFactory.getLogger(ReceivingMsgHandlerMasterModelService.class);
	
//	@Autowired
//	private UserLinkDao userLinkDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private ReplyMsgDao replyMsgDao;
	
	@Autowired
	private AutoReplyTrackingService autoReplyTrackingService;
	
	public LineUserBindStatus getStatusByUid(String uid) {
//		String status = this.userLinkDao.getUserLinkStatusByUid(uid);
		String status = this.userDao.getStatusByUid(uid);
		logger.info("使用者 {}, 狀態 {}", uid, status);
		return LineUserBindStatus.BINDED.getValues().equals(status) ? LineUserBindStatus.BINDED : LineUserBindStatus.UNBINDED;
	}
	
	public List<Message> onTextMsgReceived(String uid, String text) {
		try {
			LineUserBindStatus status = this.getStatusByUid(uid);
			this.autoReplyTrackingService.addMsgByUidAndStatusAndKeyWord(uid, status, text);
			return this.getMsgsByUidAndTextAndTypeAndUserStatus(uid, text, status);
		} catch (Exception e) {
			logger.error("onTextMsgReceived {}", e.getMessage());
		}
		return new ArrayList<>();
	}
	
	public List<Message> getMsgsByUidAndTextAndTypeAndUserStatus(String uid, String text, LineUserBindStatus status) throws DaoException {
		List<Message> messages = this.replyMsgDao.getMsgsByTextAndTypeAndUserStatus(text, status);
		if (messages == null || messages.isEmpty()) {
			String exString = String.format("使用者 %s 關鍵字 %s 無任何對應的訊息", uid, text);
			throw DaoException.message(exString);
		}
		logger.debug("使用者 {} 觸發關鍵字 {} 回覆訊息", uid, text);
		return messages;
	}
	
	
	
}

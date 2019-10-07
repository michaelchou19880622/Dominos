package com.hpifive.line.bcs.webhook.service;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.hpifive.line.bcs.webhook.akka.body.EventRegisterMsg;
import com.hpifive.line.bcs.webhook.common.DateTimeModel;
import com.hpifive.line.bcs.webhook.dao.EventAttendanceDao;
import com.hpifive.line.bcs.webhook.dao.EventDao;
import com.hpifive.line.bcs.webhook.dao.EventKeywordDao;
import com.hpifive.line.bcs.webhook.dao.UserDao;
import com.hpifive.line.bcs.webhook.dao.UserLinkDao;
import com.hpifive.line.bcs.webhook.entities.EventAttendanceEntity;
import com.hpifive.line.bcs.webhook.entities.EventEntity;
import com.hpifive.line.bcs.webhook.entities.EventKeywordEntity;
import com.hpifive.line.bcs.webhook.entities.LineUserEntity;
import com.hpifive.line.bcs.webhook.entities.config.EventApplyStatus;
import com.hpifive.line.bcs.webhook.entities.config.EventInfo;
import com.hpifive.line.bcs.webhook.entities.config.EventTypes;
import com.hpifive.line.bcs.webhook.entities.config.KeywordEventTypes;
import com.hpifive.line.bcs.webhook.entities.config.LineUserBindStatus;
import com.hpifive.line.bcs.webhook.exception.DaoException;

@Service
@Component
public class EventReceivingMsgHandlerMasterModelService {
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private UserLinkDao userLinkDao;
	
//	event
	@Autowired
	private EventAttendanceDao eventAttendanceDao;
	
	@Autowired
	private EventDao eventDao;
	
	@Autowired
	private EventKeywordDao eventKeywordDao;
// end event
	private static final List<Integer> nonAcceptApplyStatus = Arrays.asList(
			EventApplyStatus.APPLY_COMPLETED_YES.getValue(),
			EventApplyStatus.WAIT_CONFIRM.getValue(),
			EventApplyStatus.CONFIRM.getValue(),
			EventApplyStatus.REJECT.getValue()
		);
	
	public KeywordEventTypes getKeywordTypeByEventIdAndKeyword(Long eventId, String keyword) {
		EventKeywordEntity eventKeywordEntity = this.eventKeywordDao.getByIdAndKeyword(eventId, keyword);
		if (eventKeywordEntity == null) {
			return null;
		}
//		從keyword 取得 EventType來傳遞到下一步
		return KeywordEventTypes.fromString(eventKeywordEntity.getKeywordEvent());	
	}
	
	protected boolean isEventAvailableBetweenDate(Date beginDate, Date endDate) {
		ZonedDateTime currentDate = DateTimeModel.getTime();
		ZonedDateTime begin = DateTimeModel.getTime(beginDate);
		ZonedDateTime end = DateTimeModel.getTime(endDate);
		return ( (begin.isBefore(currentDate) || begin.equals(currentDate)) && (end.isAfter(currentDate) || end.equals(currentDate)));
	}
	
	protected boolean isEventUserStatusAvailableByAllowStatus(List<String> allowStatus, String eventUserStatus) {
		return allowStatus.contains(eventUserStatus);
	}
	
	protected EventInfo isEventAvailableBetweenDateAndEventUserStatus(Date beginDate, Date endDate, LineUserBindStatus userBindStatus, String eventUserStatus)  {
		if (this.isEventAvailableBetweenDate(beginDate, endDate)) {
			List<String> allowStatus = Arrays.asList(LineUserBindStatus.ALL.toString(), userBindStatus.toString());
			if (this.isEventUserStatusAvailableByAllowStatus(allowStatus, eventUserStatus)) {
				return EventInfo.NORMALLY;
			}
			return EventInfo.INVALIDSTATUS;
		} else {
			return EventInfo.INVALIDPERIOD;
		}
	}
/*
活動事件 
檢查活動有沒有過期以及是否符合使用者身分
如果 applyStatus == null 代表使用者先前沒有註冊過這檔活動
代表關鍵字可能是要用做觸發申請活動使用
如果 applyStatus != null 代表使用者先前註冊過活動
代表使用者可能是要完成註冊流程或者是重複申請
*/
	public KeywordEventTypes onEventKeywordReceived(Long eventId, String keyword, LineUserBindStatus userBindStatus, String eventUserStatus, Integer applyStatus, Date beginTime, Date endTime) {
		KeywordEventTypes replyKeywordTypes = this.isEventAllowd(beginTime, endTime, userBindStatus, eventUserStatus);
		if (KeywordEventTypes.REGISTER_FLOW_RUNNING != replyKeywordTypes) {
			return replyKeywordTypes;
		}
		KeywordEventTypes types = this.getKeywordTypeByEventIdAndKeyword(eventId, keyword); 
		if (types == null) {
			return null;
		}
		 if (EventApplyStatus.APPLY_COMPLETED.getValue() == applyStatus) {
//			確認資料狀態僅接受以下兩個指令事件
				if (KeywordEventTypes.APPLY_COMPLETED_YES == types || KeywordEventTypes.APPLY_COMPLETED_NO == types) {
					return types;
				}
		} else if (EventApplyStatus.WAIT_CONFIRM.getValue() == applyStatus) {
//			等待確認狀態僅接受以下兩個事件
			if (KeywordEventTypes.CONFIRM_CHECK == types || KeywordEventTypes.REJECT_CHECK == types) {
				return types;
			}
		} else if (nonAcceptApplyStatus.contains(applyStatus) &&
				(KeywordEventTypes.APPLY_AGREE == types || KeywordEventTypes.APPLY == types)) {
			return KeywordEventTypes.APPLIED;
		}
		 if (KeywordEventTypes.APPLY_COMPLETED_YES == types ||
				KeywordEventTypes.APPLY_COMPLETED_NO == types || 
				KeywordEventTypes.WAIT_CONFIRM == types ||
				KeywordEventTypes.CONFIRM_CHECK == types || 
				KeywordEventTypes.REJECT_CHECK == types) {
				return null;
		}
		return types;
	}
	
	public KeywordEventTypes isEventAllowd(Date beginTime, Date endTime, LineUserBindStatus userBindStatus, String eventUserStatus) {
		EventInfo info = this.isEventAvailableBetweenDateAndEventUserStatus(beginTime, endTime, userBindStatus, eventUserStatus);
		if (EventInfo.INVALIDPERIOD == info) {
			return KeywordEventTypes.INVALID_PERIOD;
		} else if (EventInfo.INVALIDSTATUS == info) {
			return KeywordEventTypes.APPLY_INVALID;
		}
		return KeywordEventTypes.REGISTER_FLOW_RUNNING;
	}
	
	public EventRegisterMsg onTextMsgReceived(String uid, String text) throws DaoException {
		LineUserBindStatus status = this.userLinkDao.getStatusByUid(uid);
//		start here
		LineUserEntity userEntity = this.userDao.getByUid(uid);
		Long userId = userEntity.getId();
		EventAttendanceEntity unFinishAttendEntity = this.eventAttendanceDao.getUnFinishRegisterFlowAttendance(userId, status);
		if (unFinishAttendEntity == null) {
//			沒有待填寫資料的活動
//			確認關鍵字是不是活動的
			EventEntity eventEntity = this.eventKeywordDao.getEventByKeyword(text);
			if (eventEntity == null) {
				return null;
			}
//				如果是活動的關鍵字
			Long eventId = eventEntity.getId();
			EventAttendanceEntity attendEntity = this.eventAttendanceDao.getByUserIdAndEventIdAndUserStatus(eventId, userId, status);
			Integer applyStatus = attendEntity==null ? null : attendEntity.getApplyStatus();
			KeywordEventTypes replyKeywordTypes = this.onEventKeywordReceived(eventId, text, status, eventEntity.getUserStatus(), applyStatus, eventEntity.getBeginDatetime(), eventEntity.getEndDatetime());
			if (replyKeywordTypes == null) {
				return null;
			}
			EventTypes type = eventEntity.getType();
			if (EventTypes.ITERATE == type && KeywordEventTypes.APPLIED == replyKeywordTypes) {
				replyKeywordTypes = KeywordEventTypes.APPLY_AGREE;	
			}
			EventRegisterMsg msg = new EventRegisterMsg(eventId, userId, applyStatus, uid, text, replyKeywordTypes);
			msg.setEventTypes(type);
			if (attendEntity != null) {
				msg.setAttendanceId(attendEntity.getId());
			}
			return msg;
		}
//		目前正在未完成的關鍵字流程中
		EventEntity eventEntity = this.eventDao.getById(unFinishAttendEntity.getEventId());
		EventTypes type = eventEntity.getType();
		KeywordEventTypes types = this.isEventAllowd(eventEntity.getBeginDatetime(), eventEntity.getEndDatetime(), status, eventEntity.getUserStatus());
		EventRegisterMsg msg = new EventRegisterMsg(unFinishAttendEntity.getEventId(), unFinishAttendEntity.getUserId(), unFinishAttendEntity.getApplyStatus(),uid, text, types);
		msg.setEventTypes(type);
		msg.setAttendanceId(unFinishAttendEntity.getId());
		return msg;
	}
}

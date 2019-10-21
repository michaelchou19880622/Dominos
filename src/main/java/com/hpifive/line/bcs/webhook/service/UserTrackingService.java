package com.hpifive.line.bcs.webhook.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.hpifive.line.bcs.webhook.dao.UserDao;
import com.hpifive.line.bcs.webhook.dao.UserTrackDao;
import com.hpifive.line.bcs.webhook.entities.LineUserEntity;
import com.hpifive.line.bcs.webhook.entities.config.LineUserBindStatus;
import com.hpifive.line.bcs.webhook.entities.config.LineUserStatus;
import com.hpifive.line.bcs.webhook.entities.config.LineUserTrackSource;

@Component
@Service
public class UserTrackingService {
	
	private static final Logger logger = LoggerFactory.getLogger(UserTrackingService.class);
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private UserTrackDao userTrackDao;
	
	public void trackByUidAndStatus(String uid, LineUserStatus status) {
		try {
			LineUserTrackSource source = LineUserTrackSource.convert(status);
			if (source != null) {
				this.trackByUidAndStatus(uid, source);
			}
		} catch (Exception e) {
			logger.error("track error by uid "+uid, e);
		}
	}
	
	public void trackByUidAndStatus(String uid, LineUserTrackSource status) {
		LineUserEntity entity = this.userDao.addUserByUidIfNotExist(uid);
		if (entity != null) {
			this.trackByUserIdAndStatus(entity.getId(), status);
		} else {
			logger.warn("無法追蹤 UserUUid {} ", uid);
		}
	}
	
	public void trackByUserIdAndStatus(Long userId, LineUserTrackSource status) {
		this.userTrackDao.add(userId, status);
	}
	
	public void trackByUserIdAndStatus(Long userId, LineUserBindStatus status) {
		try {
			LineUserTrackSource source = LineUserTrackSource.convert(status);
			if (source != null) {
				this.userTrackDao.add(userId, source);
			}
		} catch (Exception e) {
			logger.error("track error", e);
		}
	}
	
	public void trackByUserIdAndStatus(Long userId, LineUserStatus status) {
		try {
			LineUserTrackSource source = LineUserTrackSource.convert(status);
			if (source != null) {
				this.userTrackDao.add(userId, source);
			}
		} catch (Exception e) {
			logger.error("track error", e);
		}
	}
	
}

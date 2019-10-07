package com.hpifive.line.bcs.webhook.akka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hpifive.line.bcs.webhook.config.ApplicationContextProvider;
import com.hpifive.line.bcs.webhook.dao.UserDao;
import com.hpifive.line.bcs.webhook.entities.config.LineUserStatus;
import com.hpifive.line.bcs.webhook.service.UserTrackingService;
import com.linecorp.bot.model.event.FollowEvent;
import com.linecorp.bot.model.event.UnfollowEvent;

import akka.actor.AbstractActor;
import akka.actor.Props;

public class FriendEventHandlerMaster extends AbstractActor {
	
	private static final Logger logger = LoggerFactory.getLogger(FriendEventHandlerMaster.class);
	
	public static Props props() {
		return Props.create(FriendEventHandlerMaster.class);
	}
	
	private void onFollowEventReceived(FollowEvent event) {
		try {
			String uid = event.getSource().getUserId();
			this.setUserByUidAndStatus(uid, LineUserStatus.NORMALLY);
		} catch (Exception e) {
			logger.warn(e.getMessage());
		}
		
	}
	
	private void setUserByUidAndStatus(String uid, LineUserStatus status) {
		UserDao userDao =  ApplicationContextProvider.getApplicationContext().getBean(UserDao.class);
		UserTrackingService track = ApplicationContextProvider.getApplicationContext().getBean(UserTrackingService.class);
		track.trackByUidAndStatus(uid, status);
		userDao.setLineUserByUid(uid, status);
	}
	
	private void onBlockEventReceived(UnfollowEvent event) {
		try {
			String uid = event.getSource().getUserId();
			this.setUserByUidAndStatus(uid, LineUserStatus.BLOCK);
		} catch (Exception e) {
			logger.warn(e.getMessage());
		}

	}
	
	@Override
	public Receive createReceive() {
		return receiveBuilder()
				.match(FollowEvent.class, this::onFollowEventReceived)
				.match(UnfollowEvent.class, this::onBlockEventReceived)
				.matchAny(any -> logger.warn("handler unchecked type {}", any))
				.build();
	}

}

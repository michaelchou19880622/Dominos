package com.hpifive.line.bcs.webhook.akka.body;

import java.util.List;

public class UnSendUsers {

	private List<Long> ids;
	private List<Long> userIds;
	
	public UnSendUsers() {
		super();
		// TODO Auto-generated constructor stub
	}
	public UnSendUsers(List<Long> ids, List<Long> userIds) {
		super();
		this.ids = ids;
		this.userIds = userIds;
	}
	public List<Long> getIds() {
		return ids;
	}
	public void setIds(List<Long> ids) {
		this.ids = ids;
	}
	public List<Long> getUserIds() {
		return userIds;
	}
	public void setUserIds(List<Long> userIds) {
		this.userIds = userIds;
	}
	
	
}

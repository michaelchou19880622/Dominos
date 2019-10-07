package com.hpifive.line.bcs.webhook.controller.body;

import java.util.List;

public class ExpiredKeyValueBody<T> {

	private String expire;
	private List<KeyValueBody<T>> filters;
	
	public ExpiredKeyValueBody() {
		super();
	}

	public ExpiredKeyValueBody(String expire, List<KeyValueBody<T>> filters) {
		super();
		this.expire = expire;
		this.filters = filters;
	}

	public String getExpire() {
		return expire;
	}

	public void setExpire(String expire) {
		this.expire = expire;
	}

	public List<KeyValueBody<T>> getFilters() {
		return filters;
	}

	public void setFilters(List<KeyValueBody<T>> filters) {
		this.filters = filters;
	}

	@Override
	public String toString() {
		return "ExpiredKeyValueBody [expire=" + expire + ", filters=" + filters + "]";
	}
	
}

package com.hpifive.line.bcs.webhook.common;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;

public class CriteriaQueryDto<T> {

	private CriteriaQuery<T> query;
	private Predicate predicate;
	
	public CriteriaQueryDto() {
		super();
	}
	public CriteriaQueryDto(CriteriaQuery<T> query, Predicate predicate) {
		super();
		this.query = query;
		this.predicate = predicate;
	}
	public CriteriaQuery<T> getQuery() {
		return query;
	}
	public void setQuery(CriteriaQuery<T> query) {
		this.query = query;
	}
	public Predicate getPredicate() {
		return predicate;
	}
	public void setPredicate(Predicate predicate) {
		this.predicate = predicate;
	}
	
	
}

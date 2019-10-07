package com.hpifive.line.bcs.webhook.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="retailer_detail")
public class RetailerDetailEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="retailer_id", nullable=false)
	private Long retailerId;
	
	@Column(name="company_id", nullable=false, length=10)
	private String companyId;

	public RetailerDetailEntity() {
		super();
	}

	public RetailerDetailEntity(Long id, Long retailerId, String companyId) {
		super();
		this.id = id;
		this.retailerId = retailerId;
		this.companyId = companyId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRetailerId() {
		return retailerId;
	}

	public void setRetailerId(Long retailerId) {
		this.retailerId = retailerId;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	
}

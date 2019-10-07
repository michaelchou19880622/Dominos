package com.hpifive.line.bcs.webhook.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "invoice_detail")
@EntityListeners(AuditingEntityListener.class)
public class InvoiceDetailEntity implements Serializable {

	private static final long serialVersionUID = 6807180767593661005L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "invoice_id")
	private Long invoiceId;
	
	@Column(name = "amount")
	private Double amount;

	@Column(name = "description")
	private String description;

	@Column(name = "unit_price")
	private Double unitPrice;

	@Column(name = "quantity")
	private Integer quantity;

	@Column(name = "is_match")
	private Boolean isMatch;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Long getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(Long invoiceId) {
		this.invoiceId = invoiceId;
	}

	public Boolean getIsMatch() {
		return isMatch;
	}

	public void setIsMatch(Boolean isMatch) {
		this.isMatch = isMatch;
	}

	@Override
	public String toString() {
		return "InvoiceDetail [id=" + id + ", amount=" + amount + ", description=" + description + ", unitPrice="
				+ unitPrice + ", quantity=" + quantity + ", invoiceId=" + invoiceId + ", isMatch=" + isMatch + "]";
	}
	
}

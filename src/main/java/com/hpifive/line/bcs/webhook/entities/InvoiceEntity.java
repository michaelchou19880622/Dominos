package com.hpifive.line.bcs.webhook.entities;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.hpifive.line.bcs.webhook.entities.config.InvoiceStatus;

@Entity
@Table(name = "invoice")
public class InvoiceEntity implements Serializable {

	private static final long serialVersionUID = -4597019701878994696L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "lineuser_id")
	private Long lineUserId;

	@Column(name = "event_id")
	private Long eventId; 
	
	@Column(name = "code")
	private String code;
	
	@Column(name = "msg")
	private String msg;
	
	@Column(name = "seller_name")
	private String sellerName;

	@Column(name = "seller_ban")
	private String sellerBan;

	@Column(name = "seller_address")
	private String sellerAddress;
	
	@Column(name = "inv_num")
	private String invNum;
	
	@Column(name = "inv_period")
	private String invPeriod;
	
	@Column(name = "inv_random")
	private String invRandom;

	@Column(name = "inv_term")
	private String invTerm;

	@Column(name = "inv_date")
	private ZonedDateTime invDate;
	
	@Column(name = "inv_status")
	private String invStatus;
	
	@Column(name = "v")
	private String v;
	
	@Column(name = "inv_image_url")
	private String invImageUrl;

	@Column(name = "upload_time")
	private ZonedDateTime uploadTime;
	
	@Column(name = "status")
	private String status;
	
	@Column(name="error_count")
	private Integer errorCount;
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    @JoinColumn(name="invoice_id")
	private List<InvoiceDetailEntity> invoiceDetail;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getSellerName() {
		return sellerName;
	}

	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}

	public String getSellerBan() {
		return sellerBan;
	}

	public void setSellerBan(String sellerBan) {
		this.sellerBan = sellerBan;
	}

	public String getSellerAddress() {
		return sellerAddress;
	}

	public void setSellerAddress(String sellerAddress) {
		this.sellerAddress = sellerAddress;
	}

	public Long getLineUserId() {
		return lineUserId;
	}

	public void setLineUserId(Long lineUserId) {
		this.lineUserId = lineUserId;
	}

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public String getInvNum() {
		return invNum;
	}

	public void setInvNum(String invNum) {
		this.invNum = invNum;
	}

	public String getInvPeriod() {
		return invPeriod;
	}

	public void setInvPeriod(String invPeriod) {
		this.invPeriod = invPeriod;
	}

	public String getInvRandom() {
		return invRandom;
	}

	public void setInvRandom(String invRandom) {
		this.invRandom = invRandom;
	}

	public String getInvTerm() {
		return invTerm;
	}

	public void setInvTerm(String invTerm) {
		this.invTerm = invTerm;
	}

	public ZonedDateTime getInvDate() {
		return invDate;
	}

	public void setInvDate(ZonedDateTime invDate) {
		this.invDate = invDate;
	}

	public String getInvStatus() {
		return invStatus;
	}

	public void setInvStatus(String invStatus) {
		this.invStatus = invStatus;
	}

	public String getV() {
		return v;
	}

	public void setV(String v) {
		this.v = v;
	}

	public String getInvImageUrl() {
		return invImageUrl;
	}

	public void setInvImageUrl(String invImageUrl) {
		this.invImageUrl = invImageUrl;
	}

	public ZonedDateTime getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(ZonedDateTime uploadTime) {
		this.uploadTime = uploadTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public void setStatus(InvoiceStatus status) {
		this.status = status.toString();
	}
	
	public List<InvoiceDetailEntity> getInvoiceDetail() {
		return invoiceDetail;
	}

	public void setInvoiceDetail(List<InvoiceDetailEntity> invoiceDetail) {
		this.invoiceDetail = invoiceDetail;
	}

	public Integer getErrorCount() {
		return errorCount;
	}

	public void setErrorCount(Integer errorCount) {
		this.errorCount = errorCount;
	}

	@Override
	public String toString() {
		return "Invoice [id=" + id + ", code=" + code + ", msg=" + msg + ", sellerName=" + sellerName + ", sellerBan="
				+ sellerBan + ", sellerAddress=" + sellerAddress + ", lineUserId=" + lineUserId + ", eventId=" + eventId
				+ ", invNum=" + invNum + ", invPeriod=" + invPeriod + ", invRandom=" + invRandom + ", invTerm="
				+ invTerm + ", invDate=" + invDate + ", invStatus=" + invStatus + ", v=" + v + ", invImageUrl="
				+ invImageUrl + ", uploadTime=" + uploadTime + ", status=" + status + ", invoiceDetail=" + invoiceDetail
				+ "]";
	}
	
}

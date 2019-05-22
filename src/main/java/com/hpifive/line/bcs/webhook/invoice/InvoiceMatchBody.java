package com.hpifive.line.bcs.webhook.invoice;

public class InvoiceMatchBody {

	private Integer point;
	private Integer totalSpend;
	
	public InvoiceMatchBody() {
		super();
	}
	
	public InvoiceMatchBody(Integer point, Integer totalSpend) {
		super();
		this.point = point;
		this.totalSpend = totalSpend;
	}

	public Integer getPoint() {
		return point;
	}
	public void setPoint(Integer point) {
		this.point = point;
	}
	public Integer getTotalSpend() {
		return totalSpend;
	}
	public void setTotalSpend(Integer totalSpend) {
		this.totalSpend = totalSpend;
	}
	
	
}

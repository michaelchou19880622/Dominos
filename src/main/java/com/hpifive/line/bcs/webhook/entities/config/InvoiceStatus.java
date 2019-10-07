package com.hpifive.line.bcs.webhook.entities.config;

public enum InvoiceStatus {

	MATCH,
	NOT_MATCH,
	UNQUALIFIED,
	VALID,
	DECODE_FAIL,
	NOT_FOUND,
	INVOICE_FAKE,
	MATCH_FINISH,
	NOT_MATCH_FINISH,
	UNQUALIFIED_FINISH,
	FAKE_FINISH;
	
	public static InvoiceStatus fromChinese(String field) {
        if (field != null && field.equals("已確認")) {
    		return InvoiceStatus.VALID;
        }
        return InvoiceStatus.NOT_FOUND;
    }
}

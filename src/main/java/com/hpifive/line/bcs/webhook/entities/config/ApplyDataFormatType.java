package com.hpifive.line.bcs.webhook.entities.config;

public enum ApplyDataFormatType {
	INVOICE_NUM("INV01"),
	INVOICE_RAND("INV02"),
	INVOICE_TERM("INV03"),
	ENGLISH("F0001"),
	NUMBER("F0002"),
	ENGNUM("F0003"),
	NONCHINESE("F0004"),
	CHINESE("F0011"),
	CHIENGNUM("F0012"),
	EMAIL("F0021"),
	CELLPHONE("F0022"),
	DATE("F0023"),
	GENDER("F0024");
	
	private String value;

	private ApplyDataFormatType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
	public static ApplyDataFormatType fromString(String text) {
	    for (ApplyDataFormatType b : ApplyDataFormatType.values()) {
	      if (b.value.equalsIgnoreCase(text)) {
	        return b;
	      }
	    }
	    throw new IllegalArgumentException("No constant with text " + text + " found");
	  }
	
}

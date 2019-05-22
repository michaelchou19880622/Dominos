package com.hpifive.line.bcs.webhook.common;

public class UserBindDetailDataObject {

	private String name;
	private String column;
	private String value;
	private String format;
	
	
	
	public UserBindDetailDataObject() {
	}

	public UserBindDetailDataObject(String name, String column, String value, String format) {
		super();
		this.name = name;
		this.column = column;
		this.value = value;
		this.format = format;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}
	
	
	
}

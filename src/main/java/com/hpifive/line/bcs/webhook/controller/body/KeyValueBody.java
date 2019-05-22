package com.hpifive.line.bcs.webhook.controller.body;

public class KeyValueBody<T> {

	private T key;
	private T value;
	
	public KeyValueBody() {
	}

	public KeyValueBody(T key, T value) {
		super();
		this.key = key;
		this.value = value;
	}

	public T getKey() {
		return key;
	}

	public void setKey(T key) {
		this.key = key;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "KeyValueBody [key=" + key + ", value=" + value + "]";
	}

}

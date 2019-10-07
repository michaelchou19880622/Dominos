package com.hpifive.line.bcs.webhook.exception;

public class CustomException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1692934933809733819L;

	public static CustomException message(String message) {
		return new CustomException(message);
	}
	
	public CustomException() {
		super();
	}

	public CustomException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public CustomException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public CustomException(String arg0) {
		super(arg0);
	}

	public CustomException(Throwable arg0) {
		super(arg0);
	}

}

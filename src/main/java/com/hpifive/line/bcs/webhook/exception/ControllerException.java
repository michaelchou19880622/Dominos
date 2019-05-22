package com.hpifive.line.bcs.webhook.exception;

public class ControllerException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ControllerException() {
		super();
	}

	public ControllerException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public ControllerException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public ControllerException(String arg0) {
		super(arg0);
	}

	public ControllerException(Throwable arg0) {
		super(arg0);
	}

	public static ControllerException message(String message) {
		return new ControllerException(message);
	}
}

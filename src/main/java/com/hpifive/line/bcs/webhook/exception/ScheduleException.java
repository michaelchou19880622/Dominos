package com.hpifive.line.bcs.webhook.exception;

public class ScheduleException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ScheduleException() {
		super();
	}

	public ScheduleException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public ScheduleException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public ScheduleException(String arg0) {
		super(arg0);
	}

	public ScheduleException(Throwable arg0) {
		super(arg0);
	}

}

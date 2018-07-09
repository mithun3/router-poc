package com.router.poc.exception;

public class RouterException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3805275484224488672L;

	public RouterException(String message) {
		super(message);
	}

	public RouterException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
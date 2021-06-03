package com.itc.dev.devchallange.exception;

public class ClientNotFoundException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ClientNotFoundException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public ClientNotFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
}

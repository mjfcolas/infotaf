package com.infotaf.common.exceptions;

public class BusinessException extends Exception{ 

	private static final long serialVersionUID = 1L;
	
	String message;
	
	public BusinessException(String message) {
		this.setMessage(message);
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
package com.dnstest.exceptions;

@SuppressWarnings("serial")
public class InvalidJsonFilePathException extends RuntimeException {

	public InvalidJsonFilePathException(String strMessage) {
		super(strMessage);
	}

	public InvalidJsonFilePathException(String strMessage, Throwable cause) {
		super(strMessage, cause);
	}

}

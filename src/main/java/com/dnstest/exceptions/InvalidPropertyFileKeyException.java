package com.dnstest.exceptions;

@SuppressWarnings("serial")
public class InvalidPropertyFileKeyException extends RuntimeException {
	public InvalidPropertyFileKeyException(String strMessage) {
		super(strMessage);
	}

	public InvalidPropertyFileKeyException(String strMessage, Throwable cause) {
		super(strMessage, cause);
	}
}

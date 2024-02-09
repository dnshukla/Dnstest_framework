package com.dnstest.exceptions;

@SuppressWarnings("serial")
public class InvalidNodeParsingJsonException extends RuntimeException {

	public InvalidNodeParsingJsonException(String strMessage) {
		super(strMessage);
	}

	public InvalidNodeParsingJsonException(String strMessage, Throwable cause) {
		super(strMessage, cause);
	}
}

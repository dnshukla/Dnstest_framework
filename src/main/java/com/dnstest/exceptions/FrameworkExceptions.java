package com.dnstest.exceptions;

@SuppressWarnings("serial")
public class FrameworkExceptions extends RuntimeException {

	public FrameworkExceptions(String strMessage) {
		super(strMessage);
	}

	public FrameworkExceptions(String strMessage, Throwable cause) {
		super(strMessage, cause);
	}

}

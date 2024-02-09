package com.dnstest.listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import com.dnstest.utils.PropertyUtils;

public class RetryFailedTests implements IRetryAnalyzer {

	private int iCount = 0;
	private int iRetries = 1;

	/**
	 * Return true when needs to be retried and false otherwise. Maximum will retry
	 * for one time. Retry will happen if user desires to and set the value in the
	 * property file
	 */
	@Override
	public boolean retry(ITestResult result) {
		boolean blnValue = false;

		if (PropertyUtils.getProperty("retryFailedTestCases").equalsIgnoreCase("yes")) {
			blnValue = iCount < iRetries;
			iCount++;
		}
		return blnValue;
	}

}

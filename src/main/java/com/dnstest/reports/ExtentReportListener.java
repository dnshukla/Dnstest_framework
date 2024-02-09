package com.dnstest.reports;

import static com.dnstest.init.DriverManager.getDriver;
import static com.dnstest.reports.ExtentReport.createTest;
import static com.dnstest.reports.ExtentReport.flushReports;
import static com.dnstest.reports.ExtentReport.getExtentTest;
import static com.dnstest.reports.ExtentReport.initReport;

import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.dnstest.init.Common;

/**
 * The listener interface for receiving extentReport events. The class that is
 * interested in processing a extentReport event implements this interface, and
 * the object created with that class is registered with a component using the
 * component's <code>addExtentReportListener<code> method. When the extentReport
 * event occurs, that object's appropriate method is invoked.
 *
 * @see ExtentReportEvent
 */
public class ExtentReportListener implements ITestListener, ISuiteListener {

	/**
	 * On start.
	 *
	 * @param suite the suite
	 */
	@Override
	public void onStart(ISuite suite) {
		initReport();
	}

	/**
	 * On finish.
	 *
	 * @param suite the suite
	 */
	@Override
	public void onFinish(ISuite suite) {
		flushReports();
	}

	/**
	 * On test start.
	 *
	 * @param result the result
	 */
	@Override
	public void onTestStart(ITestResult result) {
		createTest(result.getTestContext().getName());
	}

	/**
	 * On test success.
	 *
	 * @param result the result
	 */
	@Override
	public void onTestSuccess(ITestResult result) {
		getExtentTest().log(Status.PASS,
				MarkupHelper.createLabel(result.getTestContext().getName() + " is passed.", ExtentColor.GREEN));
	}

	/**
	 * On test failure.
	 *
	 * @param result the result
	 */
	@Override
	public void onTestFailure(ITestResult result) {
		getExtentTest().fail(
				MediaEntityBuilder.createScreenCaptureFromBase64String(Common.getBase64Image(getDriver())).build());
		getExtentTest().log(Status.FAIL,
				MarkupHelper.createLabel(result.getTestContext().getName() + " is failed.", ExtentColor.RED));
		Throwable exception = result.getThrowable();
		boolean hasThrowable = exception != null;
		if (hasThrowable) {
			getExtentTest().log(Status.FAIL, exception);
		}
	}

	/**
	 * On test skipped.
	 *
	 * @param result the result
	 */
	@Override
	public void onTestSkipped(ITestResult result) {
		getExtentTest().log(Status.SKIP,
				MarkupHelper.createLabel(result.getTestContext().getName() + " is skipped.", ExtentColor.YELLOW));
		Throwable exception = result.getThrowable();
		boolean hasThrowable = exception != null;
		if (hasThrowable) {
			getExtentTest().log(Status.SKIP, exception);
		}
	}
}

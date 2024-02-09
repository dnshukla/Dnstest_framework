package com.dnstest.listeners;

import java.util.Objects;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;

import com.dnstest.init.DriverManager;

import io.qameta.allure.Attachment;

/**
 * The listener interface for receiving framework events. The class that is
 * interested in processing a framework event implements this interface, and the
 * object created with that class is registered with a component using the
 * component's <code>addFrameworkListener<code> method. When the framework event
 * occurs, that object's appropriate method is invoked.
 *
 * @see FrameworkEvent
 */
public class FrameworkListener implements ITestListener {

	/** The env. */
	private static String env = null;

	/**
	 * Gets the test method name.
	 *
	 * @param result the result
	 * @return the test method name
	 */
	private static String getTestMethodName(ITestResult result) {
		return result.getMethod().getMethodName();
	}

	/**
	 * Gets the env.
	 *
	 * @return the env
	 */
	public static String getEnv() {
		return env;
	}

	/**
	 * Sets the env.
	 *
	 * @param strEnv the new env
	 */
	public static void setEnv(String strEnv) {
		env = strEnv;
	}

	/**
	 * Save screenshot PNG.
	 *
	 * @param driver the driver
	 * @return the byte[]
	 */
	// Text attachments for Allure
	@Attachment(value = "Page screenshot", type = "image/png")
	public static byte[] saveScreenshotPNG(WebDriver driver) {
		return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
	}

	/**
	 * Save text log.
	 *
	 * @param message the message
	 * @return the string
	 */
	// Text attachments for Allure
	@Attachment(value = "{0}", type = "text/plain")
	public static String saveTextLog(String message) {
		return message;
	}

	/**
	 * Attach html.
	 *
	 * @param html the html
	 * @return the string
	 */
	// HTML attachments for Allure
	@Attachment(value = "{0}", type = "text/html")
	public static String attachHtml(String html) {
		return html;
	}

	/**
	 * On start.
	 *
	 * @param context the context
	 */
	@Override
	public void onStart(ITestContext context) {
		env = context.getSuite().getParameter("env");
	}

	/**
	 * On test start.
	 *
	 * @param result the result
	 */
	@Override
	public void onTestStart(ITestResult result) {
		Reporter.log("I am in onTestStart method. Test case " + getTestMethodName(result) + " started", true);
	}

	/**
	 * On test failure.
	 *
	 * @param result the result
	 */
	@Override
	public void onTestFailure(ITestResult result) {
		Reporter.log("I am in onTestFailure method. Test case " + getTestMethodName(result) + " failed", true);

		WebDriver driver = DriverManager.getDriver();
		if (Objects.nonNull(driver)) {
			Reporter.log("Screenshot captured for test case:" + getTestMethodName(result), true);
			saveScreenshotPNG(driver);
		}
		// Save a log on allure.
		saveTextLog(getTestMethodName(result) + " failed and screenshot taken!");
	}

	/**
	 * On test success.
	 *
	 * @param result the result
	 */
	@Override
	public void onTestSuccess(ITestResult result) {
		Reporter.log("I am in onTestSuccess method. Test case " + getTestMethodName(result) + " suceeded", true);
	}

	/**
	 * On test skipped.
	 *
	 * @param result the result
	 */
	@Override
	public void onTestSkipped(ITestResult result) {
		Reporter.log("I am in onTestSkipped method. Test case " + getTestMethodName(result) + " skipped", true);
		WebDriver driver = DriverManager.getDriver();
		if (Objects.nonNull(driver)) {
			Reporter.log("Screenshot captured for test case:" + getTestMethodName(result), true);
			saveScreenshotPNG(driver);
		}
		// Save a log on allure.
		saveTextLog(getTestMethodName(result) + " failed and screenshot taken!");
	}

	/**
	 * On finish.
	 *
	 * @param context the context
	 */
	@Override
	public void onFinish(ITestContext context) {
		Reporter.log("I am in onFinish method", true);
		Reporter.log("Surefire report path : " + context.getOutputDirectory(), true);
		Reporter.log("End time of execution : " + context.getEndDate().toString(), true);
		Reporter.log("Generating allure report...", true);
//		ProcessBuilder processBuilder = new ProcessBuilder();
//		processBuilder.command("cmd", "/c", "allure serve allure-results")
//				.directory(new File(System.getProperty("user.dir")));
//		try {
//			processBuilder.start();
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
	}

	/**
	 * On test failed but within success percentage.
	 *
	 * @param result the result
	 */
	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		ITestListener.super.onTestFailedButWithinSuccessPercentage(result);
	}

	/**
	 * On test failed with timeout.
	 *
	 * @param result the result
	 */
	@Override
	public void onTestFailedWithTimeout(ITestResult result) {
		ITestListener.super.onTestFailedWithTimeout(result);
	}
}

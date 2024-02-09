package com.dnstest.reports;

import java.util.Objects;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

/**
 * The Class ExtentReport.
 */
public class ExtentReport {

	/**
	 * Instantiates a new extent report.
	 */
	private ExtentReport() {
	}

	/** The ext test. */
	private static ThreadLocal<ExtentTest> extTest = new ThreadLocal<>();

	/** The extent. */
	private static ExtentReports extent;

	/**
	 * Inits the report.
	 */
	static void initReport() {
		if (Objects.isNull(extent)) {
			extent = new ExtentReports();
			ExtentSparkReporter spark = new ExtentSparkReporter("extReport.html");
			extent.attachReporter(spark);
			spark.config().setTheme(Theme.DARK);
			spark.config().setDocumentTitle("K-FAST Report");
			spark.config().setReportName("Functional");
			spark.config().thumbnailForBase64(true);
			spark.config().setTimelineEnabled(true);
		}
	}

	/**
	 * Flush reports.
	 */
	static void flushReports() {
		if (Objects.nonNull(extent)) {
			extent.flush();
			unload();
		}
	}

	/**
	 * Creates the test.
	 *
	 * @param testcaseName the testcase name
	 */
	static void createTest(String testcaseName) {
		setExtentTest(extent.createTest(testcaseName));
	}

	/**
	 * Sets the extent test.
	 *
	 * @param test the new extent test
	 */
	private static void setExtentTest(ExtentTest test) {
		extTest.set(test);
	}

	/**
	 * Gets the extent test.
	 *
	 * @return the extent test
	 */
	public static ExtentTest getExtentTest() {
		return extTest.get();
	}

	/**
	 * Unload.
	 */
	private static void unload() {
		extTest.remove();
	}

	/**
	 * Log.
	 *
	 * @param message the message
	 */
	public static void info(String message) {
		getExtentTest().info(message);
	}

	/**
	 * Pass.
	 *
	 * @param message the message
	 */
	public static void pass(String message) {
		getExtentTest().pass(message);
	}

	/**
	 * Fail.
	 *
	 * @param message the message
	 */
	public static void fail(String message) {
		getExtentTest().fail(message);
	}

	/**
	 * Skip.
	 *
	 * @param message the message
	 */
	public static void skip(String message) {
		getExtentTest().skip(message);
	}
}

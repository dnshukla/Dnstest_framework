package com.dnstest.init;

import static com.dnstest.constants.FrameworkConstants.BROWSER;
import static com.dnstest.constants.FrameworkConstants.CONFIG_PROPERTIES;
import static com.dnstest.constants.FrameworkConstants.URL;
import static com.dnstest.constants.FrameworkConstants.VERSION;
import static com.dnstest.init.Common.makeScreenshot;
import static com.dnstest.init.DriverManager.getDriver;
import static com.dnstest.init.DriverManager.initDriver;
import static com.dnstest.init.DriverManager.quitDriver;
import static com.dnstest.reports.ExtentReport.fail;
import static com.dnstest.reports.ExtentReport.getExtentTest;
import static com.dnstest.reports.ExtentReport.info;
import static com.dnstest.reports.ExtentReport.pass;
import static com.dnstest.reports.ExtentReport.skip;
import static com.dnstest.utils.DateUtils.getCurrentTimeStampString;
import static com.dnstest.utils.PropertyUtils.getProperty;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;

import com.aventstack.extentreports.MediaEntityBuilder;

/**
 * The Class SeleniumInit.
 */
public class SeleniumInit {

	/** The test url. */
	protected String testUrl;

	/** The browser name. */
	protected String browserName;

	/** The browser version. */
	protected String browserVersion = "";

	/** The driver. */
	protected WebDriver driver;

	/**
	 * Fetch suite configuration.
	 */
	@BeforeTest(alwaysRun = true)
	public void fetchSuiteConfiguration() {
		testUrl = getProperty(CONFIG_PROPERTIES, URL);
		browserName = getProperty(CONFIG_PROPERTIES, BROWSER);
		browserVersion = getProperty(CONFIG_PROPERTIES, VERSION);
	}

	/**
	 * SetUp.
	 */
	@BeforeMethod(alwaysRun = true)
	public void setUp() {
		initDriver();
		driver = getDriver();
	}

	/**
	 * Tear down.
	 *
	 * @param testResult  the test result
	 * @param testContext the test context
	 */
	@AfterMethod(alwaysRun = true)
	public void tearDown(ITestResult testResult, ITestContext testContext) {
		try {
			String testName = testContext.getName();
			Reporter.setCurrentTestResult(testResult);
			if (!testResult.isSuccess()) {
				System.out.println("\nTEST FAILED - " + testName);
				System.out.println("\nERROR MESSAGE: " + testResult.getThrowable());
				if (testResult.getStatus() == ITestResult.FAILURE) {
					System.out.println("1 message from tear down");
					String screenshotName = "finalScreenshot_" + getCurrentTimeStampString();
					makeScreenshot(driver, screenshotName);
					slog("<Strong><b>" + testName + " is failed.</b></font></strong>");
				}
			} else if (testResult.getStatus() == ITestResult.SUCCESS) {
				System.out.println("1 message from tear down");
				slog("<Strong><b>" + testName + " is passed.</b></font></strong>");
			}
			quitDriver();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	@AfterSuite
//	public void sendReportToEmail() throws MessagingException, IOException, GeneralSecurityException {
//		new MailUtils().sendMail("badal.shah@kiwiqa.com", "badal.shah@kiwiqa.com");
//	}

	/**
	 * Log.
	 *
	 * @param msg the msg
	 */
	protected void log(String msg) {
		info(msg);
		slog(msg);
	}

	/**
	 * Slog.
	 *
	 * @param msg the msg
	 */
	private void slog(String msg) {
		Reporter.log(msg + "</br>");
		System.out.println(msg);
	}

	/**
	 * Log status.
	 *
	 * @param testStatus the test status
	 * @param msg        the msg
	 */
	protected void logStatus(ITestStatus testStatus, String msg) {
		switch (testStatus) {
		case PASSED:
			pass(msg);
			slog(msg + " <Strong><font color=#32cd32><b> &#10004 Pass</b></font></strong>");
			break;
		case FAILED:
			String screenshotName = getCurrentTimeStampString();
			fail(msg);
			getExtentTest().fail(
					MediaEntityBuilder.createScreenCaptureFromBase64String(Common.getBase64Image(driver)).build());
			slog(msg + " <Strong><font color=#dc3545><b> &#10008 Fail</b></font></strong>");
			Common.makeScreenshot(driver, screenshotName);
			break;
		case SKIPPED:
			slog(msg);
			skip(msg);
			break;
		default:
			break;
		}
	}
}
package com.dnstest.tests;

import static com.dnstest.init.DriverManager.getDriver;
import static com.dnstest.init.ITestStatus.FAILED;
import static com.dnstest.init.ITestStatus.PASSED;
import static com.dnstest.utils.DataUtils.getValueOf;
import static com.dnstest.utils.DataUtils.readSheetData;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.dnstest.init.SeleniumInit;
import com.login.LoginPage;

/**
 * The Class EndToEndTest.
 */
public class LoginTest extends SeleniumInit {

	/**
	 * Test TC 01.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public void testTC01() throws IOException {
		int numOfFailure = 0;
		WebDriver driver = getDriver();
		LoginPage loginIndexPage = new LoginPage(driver);

		loginIndexPage.startTest();

		List<Map<String, String>> loginDetails = readSheetData("ETE_Details.xlsx", "Login");
		String tcID = "TC01";
		String username = getValueOf(loginDetails, tcID, "Username");
		String password = getValueOf(loginDetails, tcID, "Password");

		loginIndexPage.enterLoginDetails(username, password);
		log("Verifying user has been logged in.");
		if (loginIndexPage.verifyNameOnHeader()) {
			logStatus(PASSED, "Login Successful");
		} else {
			logStatus(FAILED, "Login Unsuccessful.");
			numOfFailure++;
		}

		if (numOfFailure > 0) {
			Assert.assertTrue(false);
		}
	}

	/**
	 * Test TC 02.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public void testTC02() throws IOException {
		int numOfFailure = 0;
		WebDriver driver = getDriver();
		LoginPage loginIndexPage = new LoginPage(driver);

		loginIndexPage.startTest();

		List<Map<String, String>> loginDetails = readSheetData("ETE_Details.xlsx", "Login");
		String tcID = "TC02";
		String username = getValueOf(loginDetails, tcID, "Username");
		String password = getValueOf(loginDetails, tcID, "Password");

		loginIndexPage.enterLoginDetails(username, password);
		log("Verifying user has been logged in.");
		if (loginIndexPage.verifyNameOnHeader()) {
			logStatus(PASSED, "Login Successful");
		} else {
			logStatus(FAILED, "Login Unsuccessful.");
			numOfFailure++;
		}

		if (numOfFailure > 0) {
			Assert.assertTrue(false);
		}
	}
}
package com.login;

import static com.dnstest.constants.FrameworkConstants.CONFIG_PROPERTIES;
import static com.dnstest.constants.FrameworkConstants.LOW_TIMEOUT;
import static com.dnstest.constants.FrameworkConstants.URL;
import static com.dnstest.init.Common.checkIfElementIsDisplayedwithWait;
import static com.dnstest.init.Common.waitAndClick;
import static com.dnstest.init.Common.waitAndSendKeys;
import static com.dnstest.utils.PropertyUtils.getProperty;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.dnstest.init.AbstractPage;

import io.qameta.allure.Step;

/**
 * The Class LoginIndexPage.
 */
public class LoginPage extends AbstractPage {

	/**
	 * Instantiates a new LoginIndexPage.
	 *
	 * @param driver The WebDriver
	 */
	public LoginPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
	}

	// ************************** locators and variables **************************

	/** The username text field. */
	@FindBy(id = "user-name")
	private WebElement usernameTxtField;

	/** The password field. */
	@FindBy(id = "password")
	private WebElement passwordField;

	/** The do login button. */
	@FindBy(id = "login-button")
	private WebElement doLoginBtn;

	/** The inventory page header. */
	@FindBy(xpath = "//span[text()='Products']")
	private WebElement inventoryPageHeader;

	// ********************************* methods **********************************

	/**
	 * Start test.
	 */
	@Step
	public void startTest() {
		String testUrl = getProperty(CONFIG_PROPERTIES, URL);
		driver.get(testUrl);
		log("Open URL : " + testUrl);
	}

	/**
	 * Enter username.
	 *
	 * @param username the username
	 * @return the login index page
	 */
	@Step
	private LoginPage enterUsername(String username) {
		waitAndSendKeys(driver, usernameTxtField, username);
		log("Entered Username : " + username);
		return this;
	}

	/**
	 * Enter password.
	 *
	 * @param password the password
	 * @return the login index page
	 */
	@Step
	private LoginPage enterPassword(String password) {
		waitAndSendKeys(driver, passwordField, password);
		log("Entered Password : " + password);
		return this;
	}

	/**
	 * Click to login.
	 *
	 * @return the login index page
	 */
	@Step
	private LoginPage clickToLogin() {
		waitAndClick(driver, doLoginBtn);
		log("Clicked Login button.");
		return this;
	}

	/**
	 * Enter login details.
	 *
	 * @param username the username
	 * @param password the password
	 * @return the login index page
	 */
	@Step
	public LoginPage enterLoginDetails(String username, String password) {
		return enterUsername(username).enterPassword(password).clickToLogin();
	}

	/**
	 * Verify name on header.
	 *
	 * @return true, if successful
	 */
	@Step
	public boolean verifyNameOnHeader() {
		return checkIfElementIsDisplayedwithWait(driver, inventoryPageHeader, LOW_TIMEOUT);
	}
}
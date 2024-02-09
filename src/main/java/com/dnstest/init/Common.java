package com.dnstest.init;

import static com.dnstest.constants.FrameworkConstants.EMAIL_VERIFICATION_PATH;
import static com.dnstest.constants.FrameworkConstants.LOWEST_TIMEOUT;
import static com.dnstest.constants.FrameworkConstants.LOW_TIMEOUT;
import static com.dnstest.utils.DateUtils.getCurrentTimeStampString;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;

import com.google.common.util.concurrent.Uninterruptibles;

/**
 * Define Common Webdriver.
 */
public class Common {

	/**
	 * Instantiates a new common.
	 */
	private Common() {
	}

	/** The random. */
	private static Random random = new Random();

	/** The os name. */
	private static String osName = "os.name";

	/**
	 * Move to object element.
	 *
	 * @param driver the driver
	 * @param by     the by
	 */
	public static void moveToObjectElement(WebDriver driver, By by) {
		driver.switchTo().frame(driver.findElement(by));
	}

	/**
	 * Scroll up to bottom.
	 *
	 * @param driver the driver
	 */
	public static void scrollUpToBottom(WebDriver driver) {
		((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
	}

	/**
	 * Scroll up to element.
	 *
	 * @param driver  the driver
	 * @param element the element
	 */
	public static void scrollUpToElement(WebDriver driver, WebElement element) {
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
	}

	/**
	 * Scroll up top.
	 *
	 * @param driver the driver
	 */
	public static void scrollUpTop(WebDriver driver) {
		((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0)");
	}

	/**
	 * Scroll to middle.
	 *
	 * @param driver  the driver
	 * @param element the element
	 */
	public static void scrollToMiddle(WebDriver driver, WebElement element) {
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'})",
				element);
	}

	/**
	 * Scroll to horizontal.
	 *
	 * @param driver  the driver
	 * @param element the element
	 */
	public static void scrollToHorizontal(WebDriver driver, WebElement element) {
		Actions dragger = new Actions(driver);
		WebElement draggablePartOfScrollbar = element;
		// drag downwards
		int numberOfPixelsToDragTheScrollbarDown = 50;
		for (int i = 10; i < 500; i = i + numberOfPixelsToDragTheScrollbarDown) {
			try {
				// this causes a gradual drag of the scroll bar, 10 units at a time
				dragger.moveToElement(draggablePartOfScrollbar).clickAndHold()
						.moveByOffset(numberOfPixelsToDragTheScrollbarDown, 0).release().perform();
				Thread.sleep(1000L);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Scroll to vertical.
	 *
	 * @param driver  the driver
	 * @param element the element
	 */
	public static void scrollToVertical(WebDriver driver, WebElement element) {
		Actions dragger = new Actions(driver);
		WebElement draggablePartOfScrollbar = element;
		// drag downwards
		int numberOfPixelsToDragTheScrollbarDown = 50;
		for (int i = 10; i < 500; i = i + numberOfPixelsToDragTheScrollbarDown) {
			try {
				// this causes a gradual drag of the scroll bar, 10 units at a
				// time
				dragger.moveToElement(draggablePartOfScrollbar).clickAndHold()
						.moveByOffset(0, numberOfPixelsToDragTheScrollbarDown).release().perform();
				Thread.sleep(1000L);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Check check box.
	 *
	 * @param element the element
	 */
	public static void checkCheckBox(WebElement element) {
		boolean isCheckBoxSet;
		isCheckBoxSet = element.isSelected();
		if (!isCheckBoxSet) {
			element.click();
		}
	}

	/**
	 * Gets the alert text.
	 *
	 * @param driver the driver
	 * @return the alert text
	 */
	public static String getAlertText(WebDriver driver) {
		return driver.switchTo().alert().getText();
	}

	/**
	 * Wait for alert.
	 *
	 * @param driver the driver
	 */
	private static void waitForAlert(WebDriver driver) {
		new WebDriverWait(driver, Duration.ofSeconds(2)).until(ExpectedConditions.alertIsPresent());
	}

	/**
	 * Accept alert.
	 *
	 * @param driver the driver
	 */
	public static void acceptAlert(WebDriver driver) {
		waitForAlert(driver);
		Alert alert = driver.switchTo().alert();
		log("Alert Message: " + alert.getText());
		alert.accept();
	}

	/**
	 * Open mailinator.
	 *
	 * @param driver       the driver
	 * @param emailAddress the email address
	 */
	public static void openMailinator(WebDriver driver, String emailAddress) {
		sleep(3);
		String[] emailParsed = emailAddress.split("@");
		String url = "http://" + emailParsed[0] + ".mailinator.com";
		goToUrl(driver, url);
	}

	/**
	 * Make screenshot.
	 *
	 * @param driver         the driver
	 * @param screenshotName the screenshot name
	 */
	static void makeScreenshot(WebDriver driver, String screenshotName) {
		WebDriver augmentedDriver = new Augmenter().augment(driver);
		/* Take a screenshot */
		File screenshot = ((TakesScreenshot) augmentedDriver).getScreenshotAs(OutputType.FILE);
		String nameWithExtention = screenshotName + ".png";
		/* Copy screenshot to specific folder */
		try {
			String reportFolder = "test-output" + File.separator;
			String screenshotsFolder = "screenshots";
			File screenshotFolder = new File(reportFolder + screenshotsFolder);
			if (!screenshotFolder.getAbsoluteFile().exists()) {
				screenshotFolder.mkdir();
			}
			FileUtils.copyFile(screenshot,
					new File(screenshotFolder + File.separator + nameWithExtention).getAbsoluteFile());
		} catch (IOException e) {
			log("Failed to capture screenshot: " + e.getMessage());
		}
		log("<a href='../test-output/screenshots/" + nameWithExtention
				+ "' target='new'><img src='../test-output/screenshots/" + nameWithExtention
				+ "' width='200' height='100'/></a>");
	}

	/**
	 * Capture screenshot.
	 *
	 * @param driver the driver
	 */
	public static void captureScreenshot(WebDriver driver) {
		/* Take a screenshot */
		File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		String screenshotName = driver.getTitle().replaceAll("[^a-zA-Z0-9]", "_") + getCurrentTimeStampString();
		String nameWithExtention = screenshotName + ".png";
		String screenshotsFolder = System.getProperty("scr.folder");
		/* Copy screenshot to specific folder */
		try {
			File screenshotFolder = new File(screenshotsFolder);
			if (!screenshotFolder.getAbsoluteFile().exists()) {
				screenshotFolder.mkdir();
			}
			FileUtils.copyFile(screenshot,
					new File(screenshotFolder + File.separator + nameWithExtention).getAbsoluteFile());
		} catch (IOException e) {
			log("Failed to capture screenshot: " + e.getMessage());
		}
	}

	/**
	 * Gets the base 64 image.
	 *
	 * @param driver the driver
	 * @return the base 64 image
	 */
	public static String getBase64Image(WebDriver driver) {
		return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
	}

	/**
	 * Make screenshot 2.
	 *
	 * @param driver         the driver
	 * @param screenshotName the screenshot name
	 */
	public static void makeScreenshot2(WebDriver driver, String screenshotName) {
		WebDriver augmentedDriver = new Augmenter().augment(driver);
		/* Take a screenshot */
		File screenshot = ((TakesScreenshot) augmentedDriver).getScreenshotAs(OutputType.FILE);
		String nameWithExtention = screenshotName + ".png";
		/* Copy screenshot to specific folder */
		try {
			String reportFolder = "test-output" + File.separator;
			String screenshotsFolder = "screenshots";
			File screenshotFolder = new File(reportFolder + screenshotsFolder);
			if (!screenshotFolder.getAbsoluteFile().exists()) {
				screenshotFolder.mkdir();
			}
			FileUtils.copyFile(screenshot,
					new File(screenshotFolder + File.separator + nameWithExtention).getAbsoluteFile());
		} catch (IOException e) {
			new SeleniumInit();
			Reporter.log("Failed to capture screenshot: " + e.getMessage());
		}
		log("<b>Please look to the screenshot - </b>" + getScreenshotLink2(nameWithExtention, nameWithExtention)
				+ "<br>");
	}

	/**
	 * Log.
	 *
	 * @param msg the msg
	 */
	private static void log(String msg) {
		System.out.println(msg);
		Reporter.log(msg + "</br>");
	}

	/**
	 * Gets the screenshot link.
	 *
	 * @param screenshotName the screenshot name
	 * @param linkText       the link text
	 * @return the screenshot link
	 */
	public static String getScreenshotLink(String screenshotName, String linkText) {
		return "<a href='../test-output/screenshots/" + screenshotName + "' target='_new'>" + linkText + "</a>";
	}

	/**
	 * Gets the screenshot link 2.
	 *
	 * @param screenshotName the screenshot name
	 * @param linkText       the link text
	 * @return the screenshot link 2
	 */
	private static String getScreenshotLink2(String screenshotName, String linkText) {
		String dataFilePath = "test-output/screenshots";
		File datafile = new File(dataFilePath);
		String fullpath = datafile.getAbsolutePath();
		return "<a href='" + fullpath + "/" + screenshotName + "' target='_new'>" + linkText + "</a>";
	}

	/**
	 * Wait for iframe.
	 *
	 * @param driver the driver
	 * @param str    the str
	 * @return
	 */
	public static WebElement waitForIframe(WebDriver driver, String str) {
		return new WebDriverWait(driver, Duration.ofSeconds(LOW_TIMEOUT))
				.until(ExpectedConditions.visibilityOfElementLocated((By.cssSelector(str))));
	}

	/**
	 * Switch to iframe by css.
	 *
	 * @param driver the driver
	 * @param str    the str
	 */
	public static void switchToIframeByCss(WebDriver driver, String str) {
		(new WebDriverWait(driver, Duration.ofSeconds(LOWEST_TIMEOUT)))
				.until(ExpectedConditions.visibilityOfElementLocated((By.cssSelector(str))));
		driver.switchTo().frame(driver.findElement(By.cssSelector(str)));
	}

	/**
	 * Find and switch to second window.
	 *
	 * @param driver              the driver
	 * @param handleCurrentWindow the handle current window
	 * @return the string
	 */
	public static String findAndSwitchToSecondWindow(WebDriver driver, String handleCurrentWindow) {
		sleep(3);
		Set<String> windows = driver.getWindowHandles();
		String handleSecondWindow = null;
		for (String window : windows) {
			if (!window.contains(handleCurrentWindow)) {
				handleSecondWindow = window;
			}
		}
		// Switch to the second window.
		try {
			sleep(4);
			driver.switchTo().window(handleSecondWindow);
		} catch (Exception e) {
			// If there is problem in switching window, then re-try.
			sleep(3);
			driver.switchTo().window(handleSecondWindow);
		}
		return handleSecondWindow;
	}

	/**
	 * Find and switch to second window.
	 *
	 * @param driver               the driver
	 * @param handleCurrentWindow  the handle current window
	 * @param handleCurrentWindow1 the handle current window 1
	 * @return the string
	 */
	public static String findAndSwitchToSecondWindow(WebDriver driver, String handleCurrentWindow,
			String handleCurrentWindow1) {
		sleep(3);
		Set<String> windows = driver.getWindowHandles();
		String handleSecondWindow = null;
		for (String window : windows) {
			if (!window.contains(handleCurrentWindow) && !window.contains(handleCurrentWindow1)) {
				handleSecondWindow = window;
			}
		}
		// Switch to the second window.
		try {
			sleep(4);
			driver.switchTo().window(handleSecondWindow);
		} catch (Exception e) {
			// If there is problem in switching window, then re-try.
			sleep(3);
			driver.switchTo().window(handleSecondWindow);
		}
		return handleSecondWindow;
	}

	/**
	 * Close other window.
	 *
	 * @param driver              the driver
	 * @param handleCurrentWindow the handle current window
	 */
	public static void closeOtherWindow(WebDriver driver, String handleCurrentWindow) {
		Set<String> windows = driver.getWindowHandles();
		for (String window : windows) {
			if (!window.contains(handleCurrentWindow)) {
				driver.close();
			}
		}
		driver.switchTo().window(handleCurrentWindow);
	}

	/**
	 * Find and switch to second windows.
	 *
	 * @param driver              the driver
	 * @param handleCurrentWindow the handle current window
	 * @param anotherWindow       the another window
	 * @return the string
	 */
	public static String findAndSwitchToSecondWindows(WebDriver driver, String handleCurrentWindow,
			String anotherWindow) {
		sleep(3);
		Set<String> windows = driver.getWindowHandles();
		String handleSecondWindow = null;
		for (String window : windows) {
			if (!window.contains(handleCurrentWindow) && !window.contains(anotherWindow)) {
				handleSecondWindow = window;
			}
		}
		try {
			sleep(4);
			driver.switchTo().window(handleSecondWindow);
		} catch (Exception e) {
			// If there is problem in switching
			sleep(3);
			driver.switchTo().window(handleSecondWindow);
		}
		return handleSecondWindow;
	}

	/**
	 * Select data from drop down or combo box by Value.
	 *
	 * @param element the element
	 * @param value   the value
	 */
	public static void selectFromCombo(WebElement element, String value) {
		Select select = new Select(element);
		select.selectByValue(value);
	}

	/**
	 * Select data form drop down or combo box by visible element.
	 *
	 * @param element the element
	 * @param value   the value
	 */
	public static void selectFromComboByVisibleElement(WebElement element, String value) {
		Select select = new Select(element);
		select.selectByVisibleText(value);
	}

	/**
	 * Select from combo by index.
	 *
	 * @param element the element
	 * @param index   the index
	 */
	public static void selectFromComboByIndex(WebElement element, int index) {
		Select select = new Select(element);
		select.selectByIndex(index);
	}

	/**
	 * Select from combo by index return value.
	 *
	 * @param element the element
	 * @param index   the index
	 * @return the string
	 */
	public static String selectFromComboByIndexReturnValue(WebElement element, int index) {
		Select select = new Select(element);
		select.selectByIndex(index);
		sleep(1);
		return select.getFirstSelectedOption().getText();
	}

	/**
	 * Select from combo by index return value.
	 *
	 * @param element the element
	 * @return the string
	 */
	public static String selectFromComboByIndexReturnValue(WebElement element) {
		Select select = new Select(element);
		return select.getFirstSelectedOption().getText();
	}

	/**
	 * Java script click.
	 *
	 * @param driver  the driver
	 * @param element the element
	 */
	public static void jsClick(WebDriver driver, WebElement element) {
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
	}

	/**
	 * Java script click.
	 *
	 * @param driver the driver
	 * @param by     the by
	 */
	public static void jsClick(WebDriver driver, By by) {
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", findElement(driver, by));
	}

	/**
	 * Hover.
	 *
	 * @param driver  the driver
	 * @param element the element
	 */
	public static void hover(WebDriver driver, WebElement element) {
		Actions builder = new Actions(driver);
		builder.moveToElement(element).perform();
	}

	/**
	 * Gets the text JS.
	 *
	 * @param driver  the driver
	 * @param element the element
	 * @return the text JS
	 */
	public static String getTextJS(WebDriver driver, WebElement element) {
		return (String) ((JavascriptExecutor) driver).executeScript("return jQuery(arguments[0]).text();", element);
	}

	/**
	 * Select random option from combo.
	 *
	 * @param driver the driver
	 * @param by     the by
	 * @return the string
	 */
	public static String selectRandomOptionFromCombo(WebDriver driver, By by) {
		String selectedOption = "";
		WebElement selectCombo = driver.findElement(by);
		sleep(2);
		List<WebElement> getAllOption = selectCombo.findElements(By.tagName("option"));
		ArrayList<String> arrayOfAllOption = new ArrayList<>();
		for (WebElement ele : getAllOption) {
			if (!ele.getText().startsWith("Select")) {
				arrayOfAllOption.add(ele.getText());
			}
		}
		int index = random.nextInt(arrayOfAllOption.size());
		if (Integer.signum(index) == -1) {
			index = -index;
		}
		selectedOption = arrayOfAllOption.get(index);
		return selectedOption;
	}

	/**
	 * Gets the num of elements.
	 *
	 * @param driver the driver
	 * @param by     the by
	 * @return the num of elements
	 */
	public static int getNumOfElements(WebDriver driver, By by) {
		return driver.findElements(by).size();
	}

	/**
	 * Refresh.
	 *
	 * @param driver the driver
	 */
	public static void refresh(WebDriver driver) {
		driver.navigate().refresh();
	}

	/**
	 * Open url in new tab.
	 *
	 * @param driver the driver
	 * @param url    the url
	 */
	public static void openUrlInNewTab(WebDriver driver, String url) {
		if (System.getProperty(osName).equalsIgnoreCase("Mac OS X")) {
			driver.findElement(By.tagName("body")).sendKeys(Keys.COMMAND + "t");
		} else {
			driver.findElement(By.tagName("body")).sendKeys(Keys.CONTROL + "t");
		}
		driver.get(url);
	}

	/**
	 * Close current tab.
	 *
	 * @param driver the driver
	 */
	public static void closeCurrentTab(WebDriver driver) {
		if (System.getProperty(osName).equalsIgnoreCase("Mac OS X")) {
			driver.findElement(By.tagName("body")).sendKeys(Keys.COMMAND + "w");
		} else {
			driver.findElement(By.tagName("body")).sendKeys(Keys.CONTROL + "w");
		}
	}

	/**
	 * Focus on element.
	 *
	 * @param driver  the driver
	 * @param element the element
	 */
	public static void focusOnElement(WebDriver driver, WebElement element) {
		Actions action = new Actions(driver);
		action.moveToElement(element).perform();
	}

	/**
	 * Mouse hover using Java Script.
	 *
	 * @param driver  the driver
	 * @param element the element
	 */
	public static void mouseHoverUsingJS(WebDriver driver, WebElement element) {
		((JavascriptExecutor) driver).executeScript(
				"if(document.createEvent){var evObj = document.createEvent('MouseEvents');evObj.initEvent('mouseover',true, false); arguments[0].dispatchEvent(evObj);} else if(document.createEventObject) { arguments[0].fireEvent('onmouseover');}",
				element);
	}

	/**
	 * Java Script type.
	 *
	 * @param driver  the driver
	 * @param element the element
	 * @param value   the value
	 */
	public static void jsType(WebDriver driver, WebElement element, String value) {
		((JavascriptExecutor) driver).executeScript("return arguments[0].value='" + value + "';", element);
	}

	/**
	 * Go to url.
	 *
	 * @param driver the driver
	 * @param url    the url
	 */
	private static void goToUrl(WebDriver driver, String url) {
		driver.get(url);
	}

	/**
	 * Go to previous page.
	 *
	 * @param driver the driver
	 */
	public static void goToPreviousPage(WebDriver driver) {
		driver.navigate().back();
	}

	/**
	 * Stop page loading.
	 *
	 * @param driver the driver
	 */
	public static void stopPageLoading(WebDriver driver) {
		driver.findElement(By.tagName("body")).sendKeys(Keys.ESCAPE);
	}

	/**
	 * Checks if is element present.
	 *
	 * @param driver the driver
	 * @param by     the by
	 * @return true, if is element present
	 */
	public static boolean isElementPresent(WebDriver driver, By by) {
		return !driver.findElements(by).isEmpty();
	}

	/**
	 * Switchto tab.
	 *
	 * @param driver    the driver
	 * @param tabNumber the tab number
	 */
	public static void switchtoTab(WebDriver driver, int tabNumber) {
		ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
		driver.switchTo().window(tabs.get(tabNumber));
	}

	/**
	 * Forced clear.
	 *
	 * @param element the element
	 */
	public static void forcedClear(WebElement element) {
		element.sendKeys(Keys.CONTROL + "a");
		element.sendKeys(Keys.DELETE);
	}

	/**
	 * Gets the all cookies.
	 *
	 * @param driver the driver
	 * @return the all cookies
	 */
	public static Set<Cookie> getAllCookies(WebDriver driver) {
		return driver.manage().getCookies();
	}

	/**
	 * Adds the all cookies.
	 *
	 * @param driver  the driver
	 * @param cookies the cookies
	 */
	public static void addAllCookies(WebDriver driver, Set<Cookie> cookies) {
		for (Cookie ck : cookies) {
			driver.manage().addCookie(ck);
		}
	}

	/**
	 * Close active window and switch to new.
	 *
	 * @param driver the driver
	 * @param url    the url
	 */
	public static void closeActiveWindowAndSwitchToNew(WebDriver driver, String url) {
		String oldWindowHandleID = driver.getWindowHandle();
		driver.switchTo().newWindow(WindowType.WINDOW);
		driver.manage().window().maximize();
		String newWindowHandleID = driver.getWindowHandle();
		driver.get(url);
		driver.switchTo().window(oldWindowHandleID);
		driver.close();
		driver.switchTo().window(newWindowHandleID);
	}

	/**
	 * Find element.
	 *
	 * @param driver the driver
	 * @param by     the by
	 * @return the web element
	 */
	public static WebElement findElement(WebDriver driver, By by) {
		return driver.findElement(by);
	}

	/**
	 * Find elements.
	 *
	 * @param driver the driver
	 * @param by     the by
	 * @return the list
	 */
	public static List<WebElement> findElements(WebDriver driver, By by) {
		return driver.findElements(by);
	}

	/**
	 * Wait for presence of an element in DOM.
	 *
	 * @param driver the driver
	 * @param by     the by
	 * @return the WebElement once it is located and visible
	 */
	public static WebElement waitForPresenceOfAnElement(WebDriver driver, By by) {
		return new WebDriverWait(driver, Duration.ofSeconds(LOW_TIMEOUT))
				.until(ExpectedConditions.presenceOfElementLocated(by));
	}

	/**
	 * Wait for presence of an element in DOM.
	 *
	 * @param driver  the driver
	 * @param by      the by
	 * @param timeout the timeout
	 * @return the WebElement once it is located and visible
	 */
	public static WebElement waitForPresenceOfAnElement(WebDriver driver, By by, long timeout) {
		return new WebDriverWait(driver, Duration.ofSeconds(timeout))
				.until(ExpectedConditions.presenceOfElementLocated(by));
	}

	/**
	 * Wait for element to be visible.
	 *
	 * @param driver  the driver
	 * @param by      the by
	 * @param timeout the timeout
	 * @return the WebElement once it is located and visible
	 */
	public static WebElement waitForElementToBeVisible(WebDriver driver, By by, long timeout) {
		return new WebDriverWait(driver, Duration.ofSeconds(timeout))
				.until(ExpectedConditions.visibilityOfElementLocated(by));
	}

	/**
	 * Wait for element to be visible with default wait of 30 seconds.
	 *
	 * @param driver the driver
	 * @param by     the by
	 * @return the web element
	 */
	public static WebElement waitForElementToBeVisible(WebDriver driver, By by) {
		return new WebDriverWait(driver, Duration.ofSeconds(LOW_TIMEOUT))
				.until(ExpectedConditions.visibilityOfElementLocated(by));
	}

	/**
	 * Wait for element to be visible.
	 *
	 * @param driver  the driver
	 * @param element the element
	 * @param timeout the timeout
	 * @return the WebElement once it is located and visible
	 */
	public static WebElement waitForElementToBeVisible(WebDriver driver, WebElement element, long timeout) {
		return new WebDriverWait(driver, Duration.ofSeconds(timeout)).until(ExpectedConditions.visibilityOf(element));
	}

	/**
	 * Wait for element to be visible with default wait of 30 seconds.
	 *
	 * @param driver the driver
	 * @param by     the by
	 * @return the web element
	 */
	public static WebElement waitForElementToBeVisible(WebDriver driver, WebElement element) {
		return new WebDriverWait(driver, Duration.ofSeconds(LOW_TIMEOUT))
				.until(ExpectedConditions.visibilityOf(element));
	}

	/**
	 * Wait for text to be present in element.
	 *
	 * @param driver          the driver
	 * @param by              the by
	 * @param strContainsText the str text to be present
	 * @return the boolean
	 */
	public static Boolean waitForTextToBePresentInElement(WebDriver driver, By by, String strContainsText) {
		return new WebDriverWait(driver, Duration.ofSeconds(LOW_TIMEOUT))
				.until(ExpectedConditions.textToBePresentInElementLocated(by, strContainsText));
	}

	/**
	 * Wait for text to be present in element.
	 *
	 * @param driver          the driver
	 * @param by              the by
	 * @param strContainsText the str text to be present
	 * @param timeout         the timeout
	 * @return the boolean
	 */
	public static Boolean waitForTextToBePresentInElement(WebDriver driver, By by, String strContainsText,
			long timeout) {
		return new WebDriverWait(driver, Duration.ofSeconds(timeout))
				.until(ExpectedConditions.textToBePresentInElementLocated(by, strContainsText));
	}

	/**
	 * Wait for text to be present in element.
	 *
	 * @param driver          the driver
	 * @param element         the element
	 * @param strContainsText the str text to be present
	 * @return the boolean
	 */
	public static Boolean waitForTextToBePresentInElement(WebDriver driver, WebElement element,
			String strContainsText) {
		return new WebDriverWait(driver, Duration.ofSeconds(LOW_TIMEOUT))
				.until(ExpectedConditions.textToBePresentInElement(element, strContainsText));
	}

	/**
	 * Wait for text to be present in element.
	 *
	 * @param driver          the driver
	 * @param element         the element
	 * @param strContainsText the str text to be present
	 * @param timeout         the timeout
	 * @return the boolean
	 */
	public static Boolean waitForTextToBePresentInElement(WebDriver driver, WebElement element, String strContainsText,
			long timeout) {
		return new WebDriverWait(driver, Duration.ofSeconds(timeout))
				.until(ExpectedConditions.textToBePresentInElement(element, strContainsText));
	}

	/**
	 * Check if element is displayed with wait.
	 *
	 * @param driver  the driver
	 * @param by      the by
	 * @param timeout the timeout
	 * @return true, if successful otherwise false
	 */
	public static boolean checkIfElementIsDisplayedwithWait(WebDriver driver, By by, long timeout) {
		FluentWait<WebDriver> fluentWait = new FluentWait<>(driver);
		fluentWait.withTimeout(Duration.ofSeconds(timeout)).pollingEvery(Duration.ofMillis(500))
				.ignoring(NoSuchElementException.class);

		try {
			return fluentWait.until(localDriver -> driver.findElement(by).isDisplayed());
		} catch (NoSuchElementException | TimeoutException e) {
			return false;
		}
	}

	/**
	 * Check if element is displayed with wait.
	 *
	 * @param driver  the driver
	 * @param by      the by
	 * @param timeout the timeout
	 * @return true, if successful otherwise false
	 */
	public static boolean checkIfElementIsDisplayedwithWait(WebDriver driver, WebElement element, long timeout) {
		FluentWait<WebDriver> fluentWait = new FluentWait<>(driver);
		fluentWait.withTimeout(Duration.ofSeconds(timeout)).pollingEvery(Duration.ofMillis(500))
				.ignoring(NoSuchElementException.class);

		try {
			return fluentWait.until(localDriver -> element.isDisplayed());
		} catch (NoSuchElementException | TimeoutException e) {
			return false;
		}
	}

	/**
	 * Check if element is displayed with wait ignoring stale element reference
	 * exception.
	 *
	 * @param driver  the driver
	 * @param by      the by
	 * @param timeout the timeout
	 * @return true, if successful otherwise false
	 */
	public static boolean checkIfElementIsDisplayedwithWaitIgnoringStaleElement(WebDriver driver, By by, long timeout) {
		FluentWait<WebDriver> fluentWait = new FluentWait<>(driver);
		fluentWait.withTimeout(Duration.ofSeconds(timeout)).pollingEvery(Duration.ofMillis(500))
				.ignoring(NoSuchElementException.class).ignoring(StaleElementReferenceException.class);

		try {
			return fluentWait.until(localDriver -> driver.findElement(by).isDisplayed());
		} catch (NoSuchElementException | TimeoutException e) {
			return false;
		}
	}

	/**
	 * Wait for all elements to be visible.
	 *
	 * @param driver the driver
	 * @param by     the by
	 * @return the list
	 */
	public static List<WebElement> waitForAllElementsToBeVisible(WebDriver driver, By by) {
		return new WebDriverWait(driver, Duration.ofSeconds(LOW_TIMEOUT))
				.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(by));
	}

	/**
	 * Wait for all elements to be visible.
	 *
	 * @param driver   the driver
	 * @param by       the by
	 * @param lTimeOut the l time out
	 * @return the list
	 */
	public static List<WebElement> waitForAllElementsToBeVisible(WebDriver driver, By by, long lTimeOut) {
		return new WebDriverWait(driver, Duration.ofSeconds(lTimeOut))
				.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(by));
	}

	/**
	 * Wait for all elements to be visible.
	 *
	 * @param driver the driver
	 * @param lstWb  the lst wb
	 * @return the list
	 */
	public static List<WebElement> waitForAllElementsToBeVisible(WebDriver driver, List<WebElement> lstWb) {
		return new WebDriverWait(driver, Duration.ofSeconds(LOW_TIMEOUT))
				.until(ExpectedConditions.visibilityOfAllElements(lstWb));
	}

	/**
	 * Wait for all elements to be visible.
	 *
	 * @param driver   the driver
	 * @param lstWb    the lst wb
	 * @param lTimeOut the l time out
	 * @return the list
	 */
	public static List<WebElement> waitForAllElementsToBeVisible(WebDriver driver, List<WebElement> lstWb,
			long lTimeOut) {
		return new WebDriverWait(driver, Duration.ofSeconds(lTimeOut))
				.until(ExpectedConditions.visibilityOfAllElements(lstWb));
	}

	/**
	 * Wait for number of elements to be.
	 *
	 * @param driver            the driver
	 * @param by                the by
	 * @param iNumberOfElements the i number of elements
	 * @return the list
	 */
	public static List<WebElement> waitForNumberOfElementsToBe(WebDriver driver, By by, int iNumberOfElements) {
		return new WebDriverWait(driver, Duration.ofSeconds(LOW_TIMEOUT))
				.until(ExpectedConditions.numberOfElementsToBe(by, iNumberOfElements));
	}

	/**
	 * Wait for number of elements to be.
	 *
	 * @param driver            the driver
	 * @param by                the by
	 * @param iNumberOfElements the i number of elements
	 * @param lTimeout          the l timeout
	 * @return the list
	 */
	public static List<WebElement> waitForNumberOfElementsToBe(WebDriver driver, By by, int iNumberOfElements,
			long lTimeout) {
		return new WebDriverWait(driver, Duration.ofSeconds(lTimeout))
				.until(ExpectedConditions.numberOfElementsToBe(by, iNumberOfElements));
	}

	/**
	 * Wait for number of elements to be more than.
	 *
	 * @param driver                   the driver
	 * @param by                       the by
	 * @param iMinimumNumberOfElements the i minimum number of elements
	 * @param lTimeout                 the l timeout
	 * @return the list
	 */
	public static List<WebElement> waitForNumberOfElementsToBeMoreThan(WebDriver driver, By by,
			int iMinimumNumberOfElements, long lTimeout) {
		return new WebDriverWait(driver, Duration.ofSeconds(lTimeout))
				.until(ExpectedConditions.numberOfElementsToBeMoreThan(by, iMinimumNumberOfElements));
	}

	/**
	 * Wait for element to be invisible.
	 *
	 * @param driver the driver
	 * @param by     the by
	 * @return the boolean -> Returns true if element is invisible
	 */
	public static Boolean waitForElementToBeInvisible(WebDriver driver, By by) {
		return new WebDriverWait(driver, Duration.ofSeconds(LOW_TIMEOUT))
				.until(ExpectedConditions.invisibilityOfElementLocated(by));
	}

	/**
	 * Wait for element to be invisible.
	 *
	 * @param driver  the driver
	 * @param by      the by
	 * @param timeout the timeout
	 * @return the boolean -> Returns true if element is invisible
	 */
	public static Boolean waitForElementToBeInvisible(WebDriver driver, By by, long timeout) {
		return new WebDriverWait(driver, Duration.ofSeconds(timeout))
				.until(ExpectedConditions.invisibilityOfElementLocated(by));
	}

	/**
	 * Wait for element to be invisible.
	 *
	 * @param driver  the driver
	 * @param element the element
	 * @return the boolean -> Returns true if element is invisible
	 */
	public static Boolean waitForElementToBeInvisible(WebDriver driver, WebElement element) {
		return new WebDriverWait(driver, Duration.ofSeconds(LOW_TIMEOUT))
				.until(ExpectedConditions.invisibilityOf(element));
	}

	/**
	 * Wait for element to be invisible.
	 *
	 * @param driver  the driver
	 * @param element the element
	 * @param timeout the timeout
	 * @return the boolean -> Returns true if element is invisible
	 */
	public static Boolean waitForElementToBeInvisible(WebDriver driver, WebElement element, long timeout) {
		return new WebDriverWait(driver, Duration.ofSeconds(timeout)).until(ExpectedConditions.invisibilityOf(element));
	}

	/**
	 * Wait for element to be clickable.
	 *
	 * @param driver the driver
	 * @param by     the by
	 * @return the WebElement once it is located and clickable (visible and enabled)
	 */
	public static WebElement waitForElementToBeClickable(WebDriver driver, By by) {
		return new WebDriverWait(driver, Duration.ofSeconds(LOW_TIMEOUT))
				.until(ExpectedConditions.elementToBeClickable(by));
	}

	/**
	 * Wait for element to be clickable.
	 *
	 * @param driver  the driver
	 * @param by      the by
	 * @param timeout the timeout
	 * @return the WebElement once it is located and clickable (visible and enabled)
	 */
	public static WebElement waitForElementToBeClickable(WebDriver driver, By by, long timeout) {
		return new WebDriverWait(driver, Duration.ofSeconds(timeout))
				.until(ExpectedConditions.elementToBeClickable(by));
	}

	/**
	 * Wait for element to be clickable.
	 *
	 * @param driver  the driver
	 * @param element the element
	 * @return the WebElement once it is located and clickable (visible and enabled)
	 */
	public static WebElement waitForElementToBeClickable(WebDriver driver, WebElement element) {
		return new WebDriverWait(driver, Duration.ofSeconds(LOW_TIMEOUT))
				.until(ExpectedConditions.elementToBeClickable(element));
	}

	/**
	 * Wait for element to be clickable.
	 *
	 * @param driver  the driver
	 * @param element the element
	 * @param timeout the timeout
	 * @return the WebElement once it is located and clickable (visible and enabled)
	 */
	public static WebElement waitForElementToBeClickable(WebDriver driver, WebElement element, long timeout) {
		return new WebDriverWait(driver, Duration.ofSeconds(timeout))
				.until(ExpectedConditions.elementToBeClickable(element));
	}

	/**
	 * Click.
	 *
	 * @param driver the driver
	 * @param by     the by
	 */
	public static void click(WebDriver driver, By by) {
		driver.findElement(by).click();
	}

	/**
	 * Click.
	 *
	 * @param element the element
	 */
	public static void click(WebElement element) {
		element.click();
	}

	/**
	 * Actions click.
	 *
	 * @param driver the driver
	 * @param by     the by
	 */
	public static void actionsClick(WebDriver driver, By by) {
		WebElement element = driver.findElement(by);
		Actions actions = new Actions(driver);
		actions.click(element).perform();
	}

	/**
	 * Actions click.
	 *
	 * @param driver  the driver
	 * @param element the element
	 */
	public static void actionsClick(WebDriver driver, WebElement element) {
		Actions actions = new Actions(driver);
		actions.click(element).perform();
	}

	/**
	 * Send keys.
	 *
	 * @param driver     the driver
	 * @param by         the by
	 * @param keysToSend the keys to send
	 */
	public static void sendKeys(WebDriver driver, By by, String keysToSend) {
		driver.findElement(by).sendKeys(keysToSend);
	}

	/**
	 * Send keys.
	 *
	 * @param element    the element
	 * @param keysToSend the keys to send
	 */
	public static void sendKeys(WebElement element, String keysToSend) {
		element.sendKeys(keysToSend);
	}

	/**
	 * Clear and send keys.
	 *
	 * @param driver     the driver
	 * @param by         the by
	 * @param keysToSend the keys to send
	 */
	public static void clearAndSendKeys(WebDriver driver, By by, String keysToSend) {
		WebElement element = findElement(driver, by);
		element.clear();
		element.sendKeys(keysToSend);
	}

	/**
	 * Clear and send keys.
	 *
	 * @param element the element
	 * @param value   the value
	 */
	public static void clearAndSendKeys(WebElement element, String value) {
		element.clear();
		element.sendKeys(value);
	}

	/**
	 * Wait and send keys.
	 *
	 * @param driver     the driver
	 * @param by         the by
	 * @param keysToSend the keys to send
	 */
	public static void waitAndSendKeys(WebDriver driver, By by, String keysToSend) {
		waitForElementToBeVisible(driver, by, LOW_TIMEOUT).sendKeys(keysToSend);
	}

	/**
	 * Wait and send keys.
	 *
	 * @param driver     the driver
	 * @param element    the element
	 * @param keysToSend the keys to send
	 */
	public static void waitAndSendKeys(WebDriver driver, WebElement element, String keysToSend) {
		waitForElementToBeVisible(driver, element, LOW_TIMEOUT).sendKeys(keysToSend);
	}

	/**
	 * Clear the text field.
	 *
	 * @param driver the driver
	 * @param by     the by
	 */
	public static void clear(WebDriver driver, By by) {
		WebElement element = findElement(driver, by);
		element.clear();
	}

	/**
	 * Clear the text field.
	 *
	 * @param element the element
	 */
	public static void clear(WebElement element) {
		element.clear();
	}

	/**
	 * Press enter.
	 *
	 * @param driver the driver
	 * @param by     the by
	 */
	public static void pressEnter(WebDriver driver, By by) {
		driver.findElement(by).sendKeys(Keys.ENTER);
	}

	/**
	 * Press enter.
	 *
	 * @param element the element
	 */
	public static void pressEnter(WebElement element) {
		element.sendKeys(Keys.ENTER);
	}

	/**
	 * Actions send keys.
	 *
	 * @param driver     the driver
	 * @param keysToSend the keys to send
	 */
	public static void actionsSendKeys(WebDriver driver, CharSequence... keysToSend) {
		Actions actions = new Actions(driver);
		actions.sendKeys(keysToSend).perform();
	}

	/**
	 * Click with stale element ignore.
	 *
	 * @param driver  the driver
	 * @param by      the by
	 * @param timeout the timeout
	 */
	public static void clickWithStaleElementIgnore(WebDriver driver, By by, long timeout) {
		FluentWait<WebDriver> fluentWait = new FluentWait<>(driver);
		fluentWait.withTimeout(Duration.ofSeconds(timeout)).pollingEvery(Duration.ofMillis(500))
				.ignoring(NoSuchElementException.class).ignoring(StaleElementReferenceException.class);

		WebElement element = fluentWait.until(localDriver -> driver.findElement(by));
		element.click();
	}

	/**
	 * Wait and click.
	 *
	 * @param driver the driver
	 * @param by     the by
	 */
	public static void waitAndClick(WebDriver driver, By by) {
		waitForElementToBeClickable(driver, by, LOW_TIMEOUT).click();
	}

	/**
	 * Wait and click.
	 *
	 * @param driver  the driver
	 * @param element the element
	 */
	public static void waitAndClick(WebDriver driver, WebElement element) {
		waitForElementToBeClickable(driver, element, LOW_TIMEOUT).click();
	}

	/**
	 * Sleep.
	 *
	 * @param timeInSeconds the time in seconds
	 */
	public static void sleep(long timeInSeconds) {
		Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(timeInSeconds));
	}

	/**
	 * Java script fetch property.
	 *
	 * @param driver      the driver
	 * @param element     the element
	 * @param strProperty the property to fetch for
	 *                    eg.(offsetParent.firstChild.checked)
	 * @return the object - which can then be type casted as needed
	 */
	public static Object javaScriptFetchProperty(WebDriver driver, WebElement element, String strProperty) {
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		return executor.executeScript("return arguments[0]." + strProperty + ";", element);
	}

	/**
	 * Fetch property value.
	 *
	 * @param driver             the driver
	 * @param by                 the by
	 * @param strPsuedoParameter the str psuedo parameter
	 * @param strProperty        the str property
	 * @return the string
	 */
	public static String fetchPropertyValue(WebDriver driver, By by, String strPsuedoParameter, String strProperty) {
		WebElement e = findElement(driver, by);
		return ((JavascriptExecutor) driver).executeScript("return window.getComputedStyle(arguments[0]," + " '"
				+ strPsuedoParameter + "').getPropertyValue('" + strProperty + "');", e).toString();
	}

	/**
	 * Gets the text.
	 *
	 * @param driver the driver
	 * @param by     the by
	 * @return the text
	 */
	public static String getText(WebDriver driver, By by) {
		return driver.findElement(by).getText();
	}

	/**
	 * Gets the text.
	 *
	 * @param element the element
	 * @return the text
	 */
	public static String getText(WebElement element) {
		return element.getText();
	}

	/**
	 * Gets the text with stale element ignore.
	 *
	 * @param driver  the driver
	 * @param by      the by
	 * @param timeout the timeout
	 * @return the text
	 */
	public static String getTextWithStaleElementIgnore(WebDriver driver, By by, long timeout) {
		FluentWait<WebDriver> fluentWait = new FluentWait<>(driver);
		fluentWait.withTimeout(Duration.ofSeconds(timeout)).pollingEvery(Duration.ofMillis(500))
				.ignoring(StaleElementReferenceException.class);

		return fluentWait.until(localDriver -> driver.findElement(by).getText());
	}

	/**
	 * Gets the attribute.
	 *
	 * @param driver       the driver
	 * @param by           the by
	 * @param strAttribute the str attribute
	 * @return the attribute
	 */
	public static String getAttribute(WebDriver driver, By by, String strAttribute) {
		return driver.findElement(by).getAttribute(strAttribute);
	}

	/**
	 * Gets the attribute.
	 *
	 * @param element      the element
	 * @param strAttribute the str attribute
	 * @return the attribute
	 */
	public static String getAttribute(WebElement element, String strAttribute) {
		return element.getAttribute(strAttribute);
	}

	/**
	 * Wait for attribute.
	 *
	 * @param driver       the driver
	 * @param by           the by
	 * @param strAttribute the str attribute
	 * @param strValue     the str value
	 * @param lTimeout     the l timeout
	 */
	public static void waitForAttribute(WebDriver driver, By by, String strAttribute, String strValue, long lTimeout) {
		new WebDriverWait(driver, Duration.ofSeconds(lTimeout))
				.until(ExpectedConditions.attributeContains(by, strAttribute, strValue));
	}

	/**
	 * Wait for attribute.
	 *
	 * @param driver       the driver
	 * @param element      the element
	 * @param strAttribute the str attribute
	 * @param strValue     the str value
	 * @param lTimeout     the l timeout
	 */
	public static void waitForAttribute(WebDriver driver, WebElement element, String strAttribute, String strValue,
			long lTimeout) {
		new WebDriverWait(driver, Duration.ofSeconds(lTimeout))
				.until(ExpectedConditions.attributeContains(element, strAttribute, strValue));
	}

	/**
	 * Right click.
	 *
	 * @param driver  the driver
	 * @param element the element
	 */
	public static void rightClick(WebDriver driver, WebElement element) {
		Actions actionProvider = new Actions(driver);
		// Perform context-click action on the element
		actionProvider.contextClick(element).perform();
	}

	/**
	 * Switch to default content.
	 *
	 * @param driver the driver
	 */
	public static void switchToDefaultContent(WebDriver driver) {
		driver.switchTo().defaultContent();
	}

	/**
	 * Gets the browser URL.
	 *
	 * @param driver the driver
	 * @return the browser URL
	 */
	public static String getBrowserURL(WebDriver driver) {
		return driver.getCurrentUrl();
	}

	/**
	 * Navigate to URL.
	 *
	 * @param driver the driver
	 * @param strURL the str URL
	 */
	public static void navigateToURL(WebDriver driver, String strURL) {
		driver.navigate().to(strURL);
	}

	/**
	 * Get shadow root element.
	 *
	 * @param shadowHost the Shadow Host
	 * @return the SearchContext of WebElement
	 */
	public static SearchContext getShadowRootElement(WebElement shadowHost) {
		// Inside Shadow Root , for Chrome , xpath is not found :
		// https://bugs.chromium.org/p/chromedriver/issues/detail?id=3984
		return shadowHost.getShadowRoot();
	}

	/**
	 * Double click.
	 *
	 * @param driver the driver
	 * @param by     the by
	 */
	public static void doubleClick(WebDriver driver, By by) {
		Actions actionProvider = new Actions(driver);

		// Perform double-click action on the element
		actionProvider.doubleClick(driver.findElement(by)).perform();
	}

	/**
	 * Switch to another window and return the parent window handle.
	 *
	 * @param driver  the driver
	 * @param timeout the timeout
	 * @return the string
	 */
	public static String switchToAnotherWindow(WebDriver driver, long timeout) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));

		// Wait for the new window or tab
		wait.until(ExpectedConditions.numberOfWindowsToBe(2));

		// Store the ID of the original window
		String originalWindow = driver.getWindowHandle();

		// Loop through until we find a new window handle
		for (String windowHandle : driver.getWindowHandles()) {
			if (!originalWindow.contentEquals(windowHandle)) {
				switchToWindow(driver, windowHandle);
				break;
			}
		}

		return originalWindow;
	}

	/**
	 * Switch to window.
	 *
	 * @param driver       the driver
	 * @param windowHandle the window handle
	 */
	public static void switchToWindow(WebDriver driver, String windowHandle) {
		driver.switchTo().window(windowHandle);
	}

	/**
	 * Open blank new tab.
	 *
	 * @param driver the driver
	 * @return originalWindowHandle the original window handle
	 */
	public static String openBlankNewTab(WebDriver driver) {
		String originalWindowHandle = driver.getWindowHandle();

		// Open new Tab
		driver.switchTo().newWindow(WindowType.TAB);

		return originalWindowHandle;
	}

	/**
	 * Wait for frame and switch to it.
	 *
	 * @param driver  the driver
	 * @param element the element
	 * @param timeout the timeout
	 */
	public static void waitForFrameAndSwitchToIt(WebDriver driver, WebElement element, long timeout) {
		new WebDriverWait(driver, Duration.ofSeconds(timeout))
				.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(element));
	}

	/**
	 * Wait for frame and switch to it.
	 *
	 * @param driver  the driver
	 * @param by      the by
	 * @param timeout the timeout
	 */
	public static void waitForFrameAndSwitchToIt(WebDriver driver, By by, long timeout) {
		new WebDriverWait(driver, Duration.ofSeconds(timeout))
				.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(by));
	}

	/**
	 * Close all windows and switch to original.
	 *
	 * @param driver  the driver
	 * @param timeout the timeout
	 */
	public void closeAllWindowsAndSwitchToOriginal(WebDriver driver, long timeout) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));

		// Wait for the new window or tab
		wait.until(ExpectedConditions.numberOfWindowsToBe(3));

		// Store the ID of the original window
		String strOriginalWindow = driver.getWindowHandle();

		// Loop through until original window is left
		for (String windowHandle : driver.getWindowHandles()) {
			if (!windowHandle.equals(strOriginalWindow)) {
				driver.switchTo().window(windowHandle);
				driver.close();
			}
		}
		switchToWindow(driver, strOriginalWindow);
	}

	/**
	 * Select whole line.
	 *
	 * @param wbElement the wb element
	 */
	public void selectWholeLine(WebDriver driver, WebElement wbElement) {
		Actions actions = new Actions(driver);
		actions.moveToElement(wbElement).doubleClick().click().build().perform();
	}

	/**
	 * Open gmail HTML page.
	 *
	 * @param driver                   the driver
	 * @param strGmailHTMLMailFileName the str gmail HTML mail file name
	 * 
	 * @return the string
	 */
	public String openGmailHTMLPage(WebDriver driver, String strGmailHTMLMailFileName) {

		String originalWindow = openBlankNewTab(driver);

		driver.get(EMAIL_VERIFICATION_PATH + File.separator + strGmailHTMLMailFileName);

		return originalWindow;
	}

}
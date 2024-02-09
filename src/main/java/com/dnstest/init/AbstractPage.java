package com.dnstest.init;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;

/**
 * The Class AbstractPage.
 */
public abstract class AbstractPage extends SeleniumInit {

	/**
	 * Instantiates a new abstract page.
	 *
	 * @param driver the driver
	 */
	protected AbstractPage(WebDriver driver) {
		PageFactory.initElements(new AjaxElementLocatorFactory(driver, 10), this);
	}
}
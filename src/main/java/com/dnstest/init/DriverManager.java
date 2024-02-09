package com.dnstest.init;

import static com.dnstest.constants.FrameworkConstants.BROWSER;
import static com.dnstest.constants.FrameworkConstants.CHROME;
import static com.dnstest.constants.FrameworkConstants.CONFIG_PROPERTIES;
import static com.dnstest.constants.FrameworkConstants.EDGE;
import static com.dnstest.constants.FrameworkConstants.FILE_DOWNLOAD_PATH;
import static com.dnstest.constants.FrameworkConstants.FIREFOX;
import static com.dnstest.constants.FrameworkConstants.HEADLESS;
import static com.dnstest.constants.FrameworkConstants.MOBILE;
import static com.dnstest.utils.PropertyUtils.getProperty;

import java.io.File;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public final class DriverManager {

	private DriverManager() {
	}

	private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

	public static WebDriver getDriver() {
		return driver.get();
	}

	private static void setDriver(WebDriver driverRef) {
		driver.set(driverRef);
	}

	private static void unload() {
		driver.remove();
	}

	static void initDriver() {
		if (Objects.isNull(getDriver())) {
			String browserName = getProperty(CONFIG_PROPERTIES, BROWSER);

			// set options to download files to a certain path
			Map<String, Object> prefs = new HashMap<>();
			prefs.put("download.default_directory", FILE_DOWNLOAD_PATH);

			if (StringUtils.containsIgnoreCase(browserName, FIREFOX)) {
				FirefoxOptions options = new FirefoxOptions();
				options.addArguments("-private");
				options.setAcceptInsecureCerts(true);
				options.addPreference("browser.download.folderList", 2);
				options.addPreference("browser.helperApps.alwaysAsk.force", false);
				options.addPreference("browser.download.dir", new File(FILE_DOWNLOAD_PATH).getAbsolutePath());
				options.addPreference("browser.download.defaultFolder", new File(FILE_DOWNLOAD_PATH).getAbsolutePath());
				options.addPreference("browser.download.manager.showWhenStarting", false);
				options.addPreference("browser.helperApps.neverAsk.saveToDisk",
						"multipart/x-zip,application/zip,application/x-zip-compressed,application/x-compressed,application/msword,application/csv,text/csv,image/png ,image/jpeg, application/pdf, text/html,text/plain,  application/excel, application/vnd.ms-excel, application/x-excel, application/x-msexcel, application/octet-stream");
				if (StringUtils.containsIgnoreCase(browserName, HEADLESS)) {
					options.addArguments("-headless");
				}
				setDriver(new FirefoxDriver(options));
				if (StringUtils.containsIgnoreCase(browserName, HEADLESS)) {
					getDriver().manage().window().setPosition(new Point(0, 0));
					getDriver().manage().window().setSize(new Dimension(1920, 1080));
				}
			} else if (StringUtils.containsIgnoreCase(browserName, EDGE)) {
				prefs.put("download.prompt_for_download", false);
				prefs.put("credentials_enable_service", false);
				prefs.put("profile.password_manager_enabled", false);
				EdgeOptions options = new EdgeOptions();
				options.setExperimentalOption("prefs", prefs);
				options.addArguments("inPrivate");
				options.addArguments("start-maximized");
				if (StringUtils.containsIgnoreCase(browserName, HEADLESS)) {
					options.addArguments("--headless=new");
					options.addArguments("window-size=1920,1080");
				}
				if (StringUtils.containsIgnoreCase(browserName, MOBILE)) {
					Map<String, Object> deviceMetrics = new HashMap<>();
					deviceMetrics.put("width", 767);
					deviceMetrics.put("height", 640);
					deviceMetrics.put("pixelRatio", 3.0);
					Map<String, Object> mobileEmulation = new HashMap<>();
					mobileEmulation.put("deviceMetrics", deviceMetrics);
					mobileEmulation.put("userAgent",
							"Mozilla/5.0 (Linux; Android 4.2.1; en-us; Nexus 5 Build/JOP40D) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.166 Mobile Safari/535.19");
					options.setExperimentalOption("mobileEmulation", mobileEmulation);
				}
				options.setExperimentalOption("excludeSwitches", new String[] { "enable-automation" });
				setDriver(new EdgeDriver(options));
			} else if (StringUtils.containsIgnoreCase(browserName, CHROME)) {
				final ChromeOptions options = new ChromeOptions();
				options.addArguments("disable-infobars"); // disabling infobars
				options.addArguments("--disable-extensions"); // disabling extensions
				options.addArguments("--disable-gpu"); // applicable to windows os only
				options.addArguments("--disable-dev-shm-usage"); // overcome limited resource problems
				options.addArguments("--no-sandbox"); // Bypass OS security model
				options.addArguments("incognito");
				options.addArguments("--start-maximized");
				options.addArguments("--disable-popup-blocking");
				if (StringUtils.containsIgnoreCase(browserName, HEADLESS)) {
					options.addArguments("--headless=new");
					options.addArguments("window-size=1920,1080");
				}
				if (StringUtils.containsIgnoreCase(browserName, MOBILE)) {
					Map<String, Object> deviceMetrics = new HashMap<>();
					deviceMetrics.put("width", 767);
					deviceMetrics.put("height", 640);
					deviceMetrics.put("pixelRatio", 3.0);
					Map<String, Object> mobileEmulation = new HashMap<>();
					mobileEmulation.put("deviceMetrics", deviceMetrics);
					mobileEmulation.put("userAgent",
							"Mozilla/5.0 (Linux; Android 4.2.1; en-us; Nexus 5 Build/JOP40D) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.166 Mobile Safari/535.19");
					options.setExperimentalOption("mobileEmulation", mobileEmulation);
				}
				options.setExperimentalOption("excludeSwitches", new String[] { "enable-automation" });
				options.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.IGNORE);
				prefs.put("credentials_enable_service", false);
				prefs.put("profile.password_manager_enabled", false);
				options.setExperimentalOption("prefs", prefs);
				setDriver(new ChromeDriver(options));
			}
			getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		}
	}

	static void quitDriver() {
		if (Objects.nonNull(getDriver())) {
			getDriver().manage().deleteAllCookies();
			getDriver().close();
			getDriver().quit();
			unload();
		}
	}
}

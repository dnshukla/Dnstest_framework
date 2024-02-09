package com.dnstest.constants;

import java.io.File;

public final class FrameworkConstants {
	private FrameworkConstants() {
	}

	// Timeouts
	public static final long HIGH_TIMEOUT = 60;
	public static final long MID_TIMEOUT = 45;
	public static final long LOW_TIMEOUT = 30;
	public static final long LOWEST_TIMEOUT = 10;
	public static final String CONFIG_PROPERTIES = "config.properties";
	public static final String URL = "url";
	public static final String BROWSER = "browser";
	public static final String VERSION = "version";
	public static final String TEST_RESOURCES_PATH = System.getProperty("user.dir") + File.separator + "src"
			+ File.separator + "test" + File.separator + "resources";
	public static final String TEST_DATA_PATH = TEST_RESOURCES_PATH + File.separator + "data";
	public static final String FILE_DOWNLOAD_PATH = TEST_RESOURCES_PATH + File.separator + "downloads";
	public static final String HEADLESS = "headless";
	public static final String CHROME = "chrome";
	public static final String EDGE = "edge";
	public static final String FIREFOX = "firefox";
	public static final String MOBILE = "mobile";
	public static final String EMAIL_VERIFICATION_PATH = TEST_RESOURCES_PATH + File.separator + "email HTML files";

}

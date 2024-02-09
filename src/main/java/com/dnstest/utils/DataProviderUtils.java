package com.dnstest.utils;

import static com.dnstest.constants.FrameworkConstants.TEST_RESOURCES_PATH;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Map;

import org.testng.annotations.DataProvider;

public final class DataProviderUtils {

	@DataProvider
	public Object[] getData(Method m) {
		Map<String, String> map = null;
		map = new ParseJsonFile().readJsonFile(TEST_RESOURCES_PATH + File.separator + "data" + File.separator
				+ m.getDeclaringClass().getSimpleName() + ".json", m.getName());
		return new Object[] { map };

	}
}

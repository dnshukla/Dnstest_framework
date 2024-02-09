package com.dnstest.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

import com.dnstest.exceptions.InvalidPropertyFileKeyException;

/**
 * The Class PropertyUtils.
 */
public final class PropertyUtils {

	/**
	 * Instantiates a new property utils.
	 */
	private PropertyUtils() {
	}

	/** The properties. */
	private static Properties properties = new Properties();

	/** The property. */
	private static Properties property;

	static {
		try {
			property = new Properties();
			FileInputStream fis = new FileInputStream("config.properties");
			property.load(fis);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the property.
	 *
	 * @param key the key
	 * @return the property
	 */
	public static String getProperty(String key) {
		String value = null;
		value = property.getProperty(key);
		if (Objects.isNull(property.getProperty(key))) {
			throw new InvalidPropertyFileKeyException(
					"Property name '" + key + "' is not found. Please check config.properties");
		}

		return value;
	}

	/**
	 * Gets the property.
	 *
	 * @param dataFile the data file
	 * @param value    the value
	 * @return the property
	 */
	public static String getProperty(String dataFile, String value) {
		try (FileInputStream fis = new FileInputStream(new File(dataFile))) {
			properties.load(fis);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties.getProperty(value);
	}

	/**
	 * Sets the property.
	 *
	 * @param dataFile the data file
	 * @param key      the key
	 * @param value    the value
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void setProperty(String dataFile, String key, String value) throws IOException {
		try (FileInputStream fileInput = new FileInputStream(new File(dataFile))) {
			properties.load(fileInput);
			properties.setProperty(key, value);
			try (FileOutputStream fos = new FileOutputStream(new File(dataFile))) {
				properties.store(fos, null);
			}
		}
	}

	/**
	 * Clear properties.
	 *
	 * @param dataFile the data file
	 * @param key      the key
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void clearProperties(String dataFile, String key) throws IOException {
		try (FileInputStream fileInput = new FileInputStream(new File(dataFile))) {
			properties.load(fileInput);
			properties.remove(key);
			try (FileOutputStream fos = new FileOutputStream(new File(dataFile))) {
				properties.store(fos, null);
			}
		}
	}
}

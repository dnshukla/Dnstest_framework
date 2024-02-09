package com.dnstest.utils;

import static com.dnstest.constants.FrameworkConstants.EMAIL_VERIFICATION_PATH;
import static com.dnstest.utils.DateUtils.getCurrentDateTimeStamp;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * The Class GenericUtils.
 */
public final class GenericUtils {

	/** The random object. */
	private static Random random = new Random();

	/**
	 * Instantiates a new generic utils.
	 */
	private GenericUtils() {
	}

	/**
	 * Gives the long random number.
	 *
	 * @return the random number
	 */
	public static long getRandomNumber() {
		long num = random.nextLong();
		if (num < 0) {
			num = num * (-1);
		}
		return num;
	}

	/**
	 * Generate random chars.
	 *
	 * @param length the length
	 * @return the string
	 */
	public static String generateRandomChars(int length) {
		return RandomStringUtils.randomAlphabetic(length);
	}

	/**
	 * Generate random number.
	 *
	 * @param length the length
	 * @return the string
	 */
	public static String generateRandomNumber(int length) {
		String numNoRange = "";
		do {
			numNoRange = RandomStringUtils.randomNumeric(length);
		} while (numNoRange.length() != length);
		return numNoRange;
	}

	/**
	 * Gives the random number within the range given by user e.g. input=1000
	 * output= any number less than 1000
	 *
	 * @param iMaxNumber the max number
	 * @return the random number
	 */
	public static int getRandomNumber(int iMaxNumber) {
		int num = random.nextInt(iMaxNumber);
		if (num < 0) {
			num = num * (-1);
		}
		return num;
	}

	/**
	 * Random between.
	 *
	 * @param minimum the minimum
	 * @param maximum the maximum
	 * @return the random number
	 */
	public static int randomBetween(int minimum, int maximum) {
		return random.nextInt(maximum - minimum + 1) + minimum;
	}

	/**
	 * Random numeric value.
	 *
	 * @param length the length
	 * @return the int
	 */
	public static int randomNumericValue(int length) {
		return random.nextInt(length);
	}

	/**
	 * Gets the text in UTF 8 format.
	 *
	 * @param strToConvert the str to convert
	 * @return the text in UTF 8
	 */
	public static String getTextInUTF8(String strToConvert) {
		String strConverted = null;
		try {
			strConverted = new String(strToConvert.getBytes("windows-1252"), StandardCharsets.UTF_8);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return strConverted;
	}

	/**
	 * Gets the cvv.
	 *
	 * @return the cvv
	 */
	public static String getCVV() {
		return Integer.toString(randomBetween(100, 899));
	}

	/**
	 * File path.
	 *
	 * @param filePath the file path
	 * @return the string
	 */
	public static String filePath(String filePath) {
		String finalPath;
		finalPath = System.getProperty("user.dir") + File.separator + filePath;
		return finalPath;
	}

	/**
	 * Gets the absolute path for upload.
	 *
	 * @param document the document
	 * @return the absolute path for upload
	 */
	public static String getAbsolutePathForUpload(String document) {
		return new File(document).getAbsolutePath();
	}

	/**
	 * Read password protected PDF.
	 *
	 * @param pdfFile  the pdf file
	 * @param password the password
	 * @return the string
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	/*
	 * public static String readPasswordProtectedPDF(String pdfFile, String
	 * password) throws IOException { PDDocument pdfDocument = PDDocument.load(new
	 * File(pdfFile), password); PDFTextStripper pdfStripper = new
	 * PDFTextStripper(); pdfDocument.close();
	 * 
	 * return pdfStripper.getText(pdfDocument); }
	 */

	/**
	 * Sets the clipboard data.
	 *
	 * @param string the new clipboard data
	 */
	public static void setClipboardData(String string) {
		StringSelection stringSelection = new StringSelection(string);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
	}

	/**
	 * Creates the HTML page from gmail mail body.
	 *
	 * @param strHTMLBody the str HTML body
	 * @return the string
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public String createHTMLPageFromGmailMailBody(String strHTMLBody) throws IOException {
		String fileName = "GmailAPI_" + getCurrentDateTimeStamp() + ".html";
		File gmailAPIHtml = new File(EMAIL_VERIFICATION_PATH + File.separator + fileName);
		gmailAPIHtml.createNewFile();
		try (FileWriter fileWriter = new FileWriter(gmailAPIHtml)) {
			fileWriter.write(strHTMLBody);
			fileWriter.flush();
		}
		return fileName;
	}

	/**
	 * Delete gmail HTML page.
	 *
	 * @param strGmailHTMLMailFileName the str gmail HTML mail file name
	 * @return true, if successful
	 */
	public boolean deleteGmailHTMLPage(String strGmailHTMLMailFileName) {
		File gmailFile = new File(EMAIL_VERIFICATION_PATH + File.separator + strGmailHTMLMailFileName);
		return gmailFile.delete();
	}
	/**
	 * Generate KiwiQA extent report.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void generateKiwiQAExtentReport() throws IOException {
		File input = new File("extReport.html");
		Document doc = Jsoup.parse(input, "UTF-8");

		Elements links = doc.getElementsByTag("link");
		links.get(0).attr("href",
				"https://cdn.jsdelivr.net/gh/icloudshahkiwiqa/ExtentKiwiQA@872b1f84d21acd4356aa047d3bf352722e5531e3/logo.png");
		links.get(1).attr("href",
				"https://cdn.jsdelivr.net/gh/icloudshahkiwiqa/ExtentKiwiQA@872b1f84d21acd4356aa047d3bf352722e5531e3/logo.png");
		links.get(2).attr("href",
				"https://cdn.jsdelivr.net/gh/icloudshahkiwiqa/ExtentKiwiQA@872b1f84d21acd4356aa047d3bf352722e5531e3/kq-spark-style.css");

		Elements logo = doc.getElementsByClass("logo");
		logo.get(0).attr("style",
				"background-image: url('https://cdn.jsdelivr.net/gh/icloudshahkiwiqa/ExtentKiwiQA@872b1f84d21acd4356aa047d3bf352722e5531e3/logo.png')");

		try (FileWriter writer = new FileWriter(input)) {
			writer.write(doc.toString());
			writer.flush();
		}
	}
}

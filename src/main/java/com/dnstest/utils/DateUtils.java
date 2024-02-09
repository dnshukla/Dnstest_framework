package com.dnstest.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The Class DateUtils.
 */
public final class DateUtils {

	/**
	 * Instantiates a new date utils.
	 */
	private DateUtils() {
	}

	/**
	 * Gets the current date time stamp.
	 *
	 * @return the current date time stamp
	 */
	public static String getCurrentDateTimeStamp() {
		return new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_z").format(new Date());
	}

	/**
	 * Gets the current time stamp string.
	 *
	 * @return the current time stamp string
	 */
	public static String getCurrentTimeStampString() {
		java.util.Date date = new java.util.Date();
		SimpleDateFormat sd = new SimpleDateFormat("yyyyMMddHHmmssSS");
		TimeZone timeZone = TimeZone.getDefault();
		Calendar cal = Calendar.getInstance(new SimpleTimeZone(timeZone.getOffset(date.getTime()), "IST"));
		sd.setCalendar(cal);
		return sd.format(date);
	}

	/**
	 * Gets the current time stamp string 2.
	 *
	 * @return the current time stamp string 2
	 */
	public static String getCurrentTimeStampString2() {
		java.util.Date date = new java.util.Date();
		SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
		TimeZone timeZone = TimeZone.getDefault();
		Calendar cal = Calendar.getInstance(new SimpleTimeZone(timeZone.getOffset(date.getTime()), "IST"));
		sd.setCalendar(cal);
		return sd.format(date);
	}

	/**
	 * Gets the current date and time.
	 *
	 * @param pattern the pattern needed
	 * @return the current date and time
	 */
	public static String getCurrentDateAndTime(String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		sdf.setTimeZone(TimeZone.getTimeZone("IST"));
		return sdf.format(new Date());
	}

	/**
	 * Gets the next date.
	 *
	 * @param format            the format
	 * @param currentToNextDate the current to next date
	 * @return the next date
	 */
	public static String getNextDate(String format, int currentToNextDate) {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, currentToNextDate);
		date = c.getTime();
		return formatter.format(date);
	}

	/**
	 * Gets the before and after date month wise for single digit.
	 *
	 * @param i the i
	 * @return the before and after date month wise for single digit
	 */
	public static String getBeforeAndAfterDateMonthWiseForSingleDigit(int i) {
		Calendar cur = Calendar.getInstance();
		NumberFormat f = new DecimalFormat("0");
		cur.add(Calendar.MONTH, i);
		return f.format((cur.get(Calendar.MONTH)) + 1) + "/" + f.format(cur.get(Calendar.DATE)) + "/"
				+ cur.get(Calendar.YEAR);
	}

	/**
	 * Gets the friday date of particular month.
	 *
	 * @param year  the year
	 * @param month the month
	 * @return the friday date of particular month
	 */
	public static String getFridayDateOfParticularMonth(String year, String month) {
		Calendar cal = Calendar.getInstance();
		cal.set(Integer.parseInt(year), Integer.parseInt(month), 1);
		cal.add(Calendar.DAY_OF_MONTH, -(cal.get(Calendar.DAY_OF_WEEK) % 7 + 1));
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		Date d1 = cal.getTime();
		return format.format(d1);
	}

	/**
	 * Date calculation.
	 *
	 * @param addStart the add start
	 * @param addEnd   the add end
	 * @return the long
	 */
	public static long dateCalculation(String addStart, String addEnd) {
		String dateStart = addStart;
		String dateStop = addEnd;
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		Date d1 = null;
		Date d2 = null;
		long diffDays = 0;
		try {
			d1 = format.parse(dateStart);
			d2 = format.parse(dateStop);
			long diff = d2.getTime() - d1.getTime();
			diffDays = diff / (24 * 60 * 60 * 1000) + 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return diffDays;
	}

	/**
	 * Number of week day.
	 *
	 * @param addStart the add start
	 * @param addEnd   the add end
	 * @param dayName  the day name
	 * @return the int
	 */
	public static int numberOfWeekDay(String addStart, String addEnd, String dayName) {
		int add = 0;
		try {
			long diffDays = dateCalculation(addStart, addEnd);

			Calendar cur = Calendar.getInstance();
			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");

			cur.setTime(format.parse(addStart));

			LocalDate date1 = LocalDate.of(2018, 1, 1);
			for (int i = 0; i < diffDays; i++) {
				if (dayName.equalsIgnoreCase(date1.getDayOfWeek().toString())) {
					add++;
				}
				date1 = date1.plusDays(1);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return add;
	}

	/**
	 * Gets the date.
	 *
	 * @param fromYear  the from year
	 * @param fromMonth the from month
	 * @param fromDate  the from date
	 * @param toYear    the to year
	 * @param toMonth   the to month
	 * @param toDate    the to date
	 * @return the date
	 */
	public static String getDate(int fromYear, int fromMonth, int fromDate, int toYear, int toMonth, int toDate) {
		LocalDate from = LocalDate.of(fromYear, fromMonth, fromDate);
		LocalDate to = LocalDate.of(toYear, toMonth, toDate);
		long days = from.until(to, ChronoUnit.DAYS);
		long randomDays = ThreadLocalRandom.current().nextLong(days + 1);
		LocalDate randomDate = from.plusDays(randomDays);

		return randomDate.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy"));
	}

	/**
	 * Gets the before and after date month wise.
	 *
	 * @param i the i
	 * @return the before and after date month wise
	 */
	public static String getBeforeAndAfterDateMonthWise(int i) {
		Calendar cur = Calendar.getInstance();
		NumberFormat f = new DecimalFormat("00");
		cur.add(Calendar.MONTH, i);
		return f.format((cur.get(Calendar.MONTH)) + 1) + "/" + f.format(cur.get(Calendar.DATE)) + "/"
				+ cur.get(Calendar.YEAR);
	}

	/**
	 * Gets the before and after date month wise.
	 *
	 * @param i the i
	 * @param n the n
	 * @return the before and after date month wise
	 */
	public static String getBeforeAndAfterDateMonthWise(int i, int n) {
		Calendar cur = Calendar.getInstance();
		NumberFormat f = new DecimalFormat("00");
		cur.add(Calendar.MONTH, i);
		return f.format((cur.get(Calendar.MONTH)) + 1) + "/" + f.format(n) + "/" + cur.get(Calendar.YEAR);
	}

	/**
	 * Gets the before and after month year.
	 *
	 * @param i     the i
	 * @param motnh the motnh
	 * @param year  the year
	 * @return the before and after month year
	 */
	public static String[] getBeforeAndAfterMonthYear(int i, int motnh, int year) {
		Calendar cur = Calendar.getInstance();
		cur.set(year, motnh, 1);
		cur.add(Calendar.MONTH, i);
		return new String[] { cur.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()),
				String.valueOf(cur.get(Calendar.YEAR)) };
	}

	/**
	 * Gets the month number.
	 *
	 * @param str the str
	 * @return the month number
	 */
	public static int getMonthNumber(String str) {
		return Month.valueOf(str.toUpperCase()).getValue();
	}

	/**
	 * Gets the before and after date year wise.
	 *
	 * @param i the i
	 * @return the before and after date year wise
	 */
	public static String getBeforeAndAfterDateYearWise(int i) {
		Calendar cur = Calendar.getInstance();
		NumberFormat f = new DecimalFormat("00");
		cur.add(Calendar.YEAR, i);
		return f.format(cur.get(Calendar.MONTH)) + "/" + f.format(cur.get(Calendar.DATE)) + "/"
				+ cur.get(Calendar.YEAR);
	}
}

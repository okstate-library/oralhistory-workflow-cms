package com.okstatelibrary.oralhistoryworkflowcms.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	private static DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

//	public static Date getTodayDate() {
//		long millis = System.currentTimeMillis();
//
//		return new java.sql.Date(millis);
//	}

	public static Date convertDate(String date) {

		try {
			// return java.sql.Date.valueOf(date);
			Date myDate = dateFormat.parse(date);
			long millis = myDate.getTime();

			return new java.sql.Date(millis);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public static java.sql.Date getTodaySqlDate() {
		long millis = System.currentTimeMillis();

		return new java.sql.Date(millis);
	}
//
//	public static int getCurrentHour() {
//
//		Calendar rightNow = Calendar.getInstance();
//		return rightNow.get(Calendar.HOUR_OF_DAY);
//
//	}
//
//	public static DateFormat getDateFormat() {
//		return dateFormat;
//	}
//
//	public static java.util.Date getPreviousDate(java.util.Date date) {
//
//		try {
//			String dateString = date.toString();
//
//			java.util.Date myDate = (java.util.Date) dateFormat.parse(dateString);
//
//			Calendar calendar = Calendar.getInstance();
//			calendar.setTime(myDate);
//			calendar.add(Calendar.DAY_OF_YEAR, -1);
//
//			return (java.util.Date) calendar.getTime();
//		} catch (Error er) {
//			return date;
//		} catch (ParseException e) {
//			e.printStackTrace();
//			return date;
//		}
//
//	}
//
//	public static int compareDates(Date date1) {
//		return dateFormat.format(date1).compareTo(dateFormat.format(getTodayDate()));
//	}

}

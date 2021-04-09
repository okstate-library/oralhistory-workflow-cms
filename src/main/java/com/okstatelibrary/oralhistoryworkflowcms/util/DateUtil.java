package com.okstatelibrary.oralhistoryworkflowcms.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	private static DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

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

}

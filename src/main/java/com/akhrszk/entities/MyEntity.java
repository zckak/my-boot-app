package com.akhrszk.entities;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class MyEntity {
	protected String formatDate(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"));
		return dateFormat.format(date);
	}
}

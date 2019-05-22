package com.hpifive.line.bcs.webhook.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateTimeModel {
	
	private static final String ZONE = "Asia/Taipei";
	private static final DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(getZone());
	
	
	private DateTimeModel() {
	}
	
	private static ZoneId getZone() {
		return ZoneId.of(ZONE);
	}
	
	public static ZonedDateTime getTime(Date date) {
		ZoneId zoneId = ZoneId.of(ZONE);
		return ZonedDateTime.ofInstant(date.toInstant(), zoneId);
	}
	
	public static ZonedDateTime getTime() {
		return ZonedDateTime.now(getZone());
	}
	
	public static Calendar getCalendar() {
		return Calendar.getInstance(getTimeZone());
	}
	
	private static TimeZone getTimeZone() {
		return TimeZone.getTimeZone(ZONE);
	}
	
	public static ZonedDateTime addMin(Date date, Integer min) {
		ZonedDateTime before = getTime(date);
		return before.plusMinutes(min.longValue());
	}
	
	public static ZonedDateTime addHours(Date date, Integer hour)  {
		ZonedDateTime before = getTime(date);
		return before.plusHours(hour.longValue());
	}
	
	public static Date timeZoneTaiwan(Date date) throws ParseException {
		SimpleDateFormat isoFormat = new SimpleDateFormat();
		isoFormat.setTimeZone(getTimeZone());
		String format = isoFormat.format(date);
		return isoFormat.parse(format);
	}
	
	public static ZonedDateTime initialDate() {
		return ZonedDateTime.ofInstant(Instant.ofEpochSecond(0), getZone());
	}
	
	public static ZonedDateTime maximumTime(Date date) {
		LocalDateTime localDateTime = getTime(date).toLocalDate().atTime(LocalTime.MAX);
		return ZonedDateTime.of(localDateTime, getZone());
	}
	
	public static ZonedDateTime minimizeTime(Date date) {
		return getTime(date).toLocalDate().atStartOfDay(getZone());
	}
	
	public static Date plusMillisecond(Date date, long millis) {
		Calendar timeout = Calendar.getInstance();
		timeout.setTimeInMillis(date.getTime() + millis);
		return timeout.getTime();
	}
	
	public static ZonedDateTime format(String date) {
		try {
			return ZonedDateTime.parse(date, pattern);
		} catch (Exception e) {
			return null;
		}
	}
}

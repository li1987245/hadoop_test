
package util;

import org.springframework.util.StringUtils;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@SuppressWarnings("all")
public final class CalendarUtil {

	public static final String CREDIT_CARD_DATE_FORMAT = "MM/yyyy";
	public static final String SHORT_DATE_FORMAT = "yyyy-MM-dd";
	public static final String SHORT_DATE_FORMAT_YYYY_MM = "yyyy-MM";
	public static final String SHORT_DATE_DOT_FORMAT = "yyyy.MM.dd";
	public static final String SHORT_DATE_FORMAT_NO_DASH = "yyyyMMdd";
	public static final String SIMPLE_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String SIMPLE_DATE_FORMAT_NO_DASH = "yyyyMMddHHmmss";
	public static final String LOG_DATE_FORMAT = "yyyyMMdd_HH00";
	public static final String ZONE_DATE_FORMAT = "EEE yyyy-MM-dd HH:mm:ss zzz";
	public static final String DATE_FORMAT = "yyyy/MM/dd EEE";
	public static final String TIME_FORMAT = "HH:mm";

	public static int daysBetween(Calendar startTime, Calendar endTime) {
		if (startTime == null) {
			throw new IllegalArgumentException("startTime is null");
		}
		if (endTime == null) {
			throw new IllegalArgumentException("endTime is null");
		}
		if (startTime.compareTo(endTime) > 0) {
			throw new IllegalArgumentException("endTime is before the startTime");
		}
		return (int) ((endTime.getTimeInMillis() - startTime.getTimeInMillis()) / (1000 * 60 * 60 * 24));
	}

	public static Calendar startOfDayTomorrow() {
		Calendar calendar = Calendar.getInstance();
		truncateDay(calendar);
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		return calendar;
	}

	public static Calendar startOfDayYesterday() {
		Calendar yesterday = Calendar.getInstance();
		truncateDay(yesterday);
		yesterday.add(Calendar.DAY_OF_MONTH, -1);
		return yesterday;
	}

	public static Calendar truncateDay(Calendar calendar) {
		if (calendar == null) {
			throw new IllegalArgumentException("input is null");
		}
		calendar.set(Calendar.AM_PM, Calendar.AM);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar;
	}

	public static String format(Calendar calendar) {
		String formatted = "";
		if (calendar != null) {
			formatted = new SimpleDateFormat().format(calendar.getTime());
		}
		return formatted;
	}

	public static String format(Time time) {
		String formatted = "";
		if (time != null) {
			formatted = new SimpleDateFormat(TIME_FORMAT).format(time.getTime());
		}
		return formatted;
	}

	public static String getDateString(Calendar calendar, String format) {
		if (calendar == null) {
			return null;
		}
		return getDateString(calendar.getTime(), format);
	}

	public static String getDefaultDateString(Date date) {
		if (null == date) {
			return "";
		}
		return getDateString(date, SIMPLE_DATE_FORMAT);
	}

	public static String getShortDateString(Date date) {
		if (null == date) {
			return "";
		}
		return getDateString(date, SHORT_DATE_FORMAT);
	}

	public static String getDateString(Date date, String format) {
		if (date == null) {
			return null;
		}

		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	public static String getDateString(Date date, String format, Locale locale) {
		if (date == null) {
			return null;
		}

		SimpleDateFormat sdf = new SimpleDateFormat(format, locale);
		return sdf.format(date);
	}

	public static Date parseDate(String dateString, String pattern) {
		Date date = null;
		try {
			DateFormat format = new SimpleDateFormat(pattern);
			date = format.parse(dateString);
		} catch (ParseException ex) {
			throw new IllegalArgumentException("Invalid date string: " + dateString, ex);
		}

		return date;
	}

	public static Date parseDefaultDate(String dateString) {
		if (!StringUtils.hasText(dateString)) {
			return null;
		}
		Date date = null;
		try {
			DateFormat format = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
			date = format.parse(dateString);
		} catch (ParseException ex) {
			throw new IllegalArgumentException("Invalid date string: " + dateString, ex);
		}

		return date;
	}
	
	public static Date parse(String str, String pattern, Locale locale) {
      if(str == null || pattern == null) {
          return null;
      }
      try {
          return new SimpleDateFormat(pattern, locale).parse(str);
      } catch (ParseException e) {
          e.printStackTrace();
      }
      return null;
  }
	

	public static Calendar parseCalendarShort(String dateString) {
		if (!StringUtils.hasText(dateString)) {
			return null;
		}
		return parseCalendar(dateString, SHORT_DATE_FORMAT);
	}

	public static Calendar parseCalendar(String dateString) {
		if (!StringUtils.hasText(dateString)) {
			return null;
		}
		return parseCalendar(dateString, SIMPLE_DATE_FORMAT);
	}

	public static String parseIntegerToDate(Integer time) {
		if(time ==null){
			return "00:00";
		}
		StringBuilder sb = new StringBuilder("");
		int xs = time / 3600;
		if(xs>0){
		if (xs < 10) {
			sb.append("0");
		}
		
		sb.append(xs);
		sb.append(":");
		}
		int fen = (time - 3600 * xs) / 60;
		if (fen < 10) {
			sb.append("0");
		}
		sb.append(fen);
		sb.append(":");
		int second = time - xs * 3600 - fen * 60;
		if (second < 10) {
			sb.append("0");
		}
		sb.append(second);
		return sb.toString();
	}

	public static Calendar parseCalendar(String dateString, String pattern) {
		Date date = parseDate(dateString, pattern);

		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c;
	}

	public static Date parseShortDate(String dateString) {
		if (!StringUtils.hasText(dateString)) {
			return null;
		}
		Date date = null;
		try {
			DateFormat format = new SimpleDateFormat(SHORT_DATE_FORMAT);
			date = format.parse(dateString);
		} catch (ParseException ex) {
			throw new IllegalArgumentException("Invalid date string: " + dateString, ex);
		}

		return date;
	}

	public static Calendar backOneDay(Calendar date) {
		Calendar cal = (Calendar) date.clone();
		cal.add(Calendar.DATE, -1);
		return cal;
	}

	public static int daysForCurrentMonth() {
		Calendar c = Calendar.getInstance();
		int days = c.getActualMaximum(Calendar.DAY_OF_MONTH);

		return days;
	}
	public static Calendar fromDate(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);

		return c;
	}



	public static String getSimpleDate(long timeMillis) {
		Date date = new Date(timeMillis);
		String dateStr = getDateString(date, SIMPLE_DATE_FORMAT);
		return dateStr;
	}

	public static Calendar getDateFromDate(Date from, long days) {
		long froml = from.getTime();
		// 时间间隔。
		long interval = days * 24l * 60l * 60l * 1000l;
		long millis = froml + interval;
		Calendar now = Calendar.getInstance();
		now.setTimeInMillis(millis);
		return now;
	}

	public static String getDateFromDate(Date from, long days, String format) {
		Calendar c = CalendarUtil.getDateFromDate(from, days);
		return CalendarUtil.getDateString(c, format);
	}

	public static String getDateFromDate(String from, long days, String format) {
		Date d = CalendarUtil.parseDate(from, format);
		Calendar c = CalendarUtil.getDateFromDate(d, days);
		return CalendarUtil.getDateString(c, format);
	}


	
	public static void main(String args[]) {
 //System.out.println(CalendarUtil.parseIntegerToDate(60));
 //parseDefaultDate("Tue Dec 02 14:24:31 CST 2014");
	}
}

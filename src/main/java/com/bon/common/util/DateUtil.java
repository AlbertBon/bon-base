package com.bon.common.util;

import com.bon.common.exception.BusinessException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {

	public static final String DATE_CHAR_PATTERN = "yyyyMMdd";
	public static final String DATE_PATTERN = "yyyy-MM-dd";
	public static final String DATE_CHINESE_PATTERN = "yyyy年MM月dd日";
	public static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	public static final String TIMESTAMP_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";

	private static MyLog log = MyLog.getLog(AttachmentUtil.class);

	public static String format(Date date) {
		return format(date, null);
	}

	public static String format(Date date, String pattern) {
		if (pattern == null)
			pattern = DATETIME_PATTERN;

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		return simpleDateFormat.format(date);
	}

	public static String getToday(){
		Date date=new Date();
		return dateToString(date,DATE_CHAR_PATTERN);
	}

	public static Date addMin(Date date, int min) {
		if (date == null) {
			return null;
		}
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(Calendar.MINUTE, min);
		return cal.getTime();
	}

	public static String dateToString(Date date, String formatParttern) {
		if(null==date){
			return null;
		}
		DateFormat format = new SimpleDateFormat(formatParttern);
		return format.format(date);
	}

	public static Date stringToDate(String str) {
		if (StringUtils.isBlank(str)) {
			return null;
		}
		DateFormat format = null;
		if (str.length() > 11) {
			if (str.length() == 24) {
				str = str.replace("Z", " UTC");
				format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
			} else {
				format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			}

			Date date = null;
			try {
				date = format.parse(str);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return date;
		} else if (str.length() == 8) {
			format = new SimpleDateFormat("yyyyMMdd");
		} else {
			format = new SimpleDateFormat("yyyy-MM-dd");
		}
		

		Date date = null;
		try {
			date = format.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static Date getLastMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, -1);
		return calendar.getTime();
	}

	public static Date getBeginDayToNumDays(Date date, int num) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		calendar.setTime(getDayBegin(calendar));
		calendar.add(Calendar.DAY_OF_MONTH, num);
		return calendar.getTime();
	}

	// 获取当天的开始时间
	public static Date getDayBegin(Calendar cal) {
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	/**
	 * 
	 * @Title: contrastDifferDays
	 * @Description: 时间 date2减date1
	 * @date 2017年7月21日 下午4:39:15
	 *
	 * @param smdate
	 * @param bdate
	 * @return
	 * @throws BusinessException
	 */
	public static int contrastDifferDays(Date smdate, Date bdate) throws BusinessException {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			smdate = sdf.parse(sdf.format(smdate));
			bdate = sdf.parse(sdf.format(bdate));
			Calendar cal = Calendar.getInstance();
			cal.setTime(smdate);
			long time1 = cal.getTimeInMillis();
			cal.setTime(bdate);
			long time2 = cal.getTimeInMillis();
			long between_days = (time2 - time1) / (1000 * 3600 * 24);

			return Integer.parseInt(String.valueOf(between_days));
		} catch (ParseException e) {
			log.error("格式化时间错误", e);
			throw new BusinessException(e.getMessage());
		}

	}

	/**
	 *
	 * @Title: contrastDifferYears
	 * @Description: 时间 date2减date1
	 * @date 2017年7月21日 下午4:39:15
	 *
	 * @param smdate
	 * @param bdate
	 * @return
	 * @throws BusinessException
	 */
	public static int contrastDifferYears(String smdate, String bdate) throws BusinessException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date smDate = paraseDate(smdate,"yyyyMMdd");
		Date bDate = paraseDate(bdate,"yyyyMMdd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(smDate);
		long time1 = cal.get(Calendar.YEAR);
		cal.setTime(bDate);
		long time2 = cal.get(Calendar.YEAR);
		long between_year = time2 - time1;

		return Integer.parseInt(String.valueOf(between_year));

	}

	/**
	 * 
	 * @Title: getBeginDayToFixedDay
	 * @Description: TODO
	 * @date 2017年7月27日 上午11:04:00
	 *
	 * @param date
	 * @return
	 */
	public static Date getBeginDayToFixedDay(Date date, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		int dateDay = calendar.get(Calendar.DAY_OF_MONTH);
		if (dateDay > day) {
			calendar.add(Calendar.MONTH, 1);
		}

		calendar.set(Calendar.DAY_OF_MONTH, day);

		calendar.setTime(getDayBegin(calendar));
		calendar.add(Calendar.MONTH, 1);
		return calendar.getTime();
	}

	/**
	 * 凌晨
	 * 
	 * @param date
	 * @flag 0 返回yyyy-MM-dd 00:00:00日期<br>
	 *       1 返回yyyy-MM-dd 23:59:59日期
	 * @return
	 */
	public static Date getWeeHours(Date date, int flag) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		int second = cal.get(Calendar.SECOND);
		// 时分秒（毫秒数）
		long millisecond = hour * 60 * 60 * 1000 + minute * 60 * 1000 + second * 1000;
		// 凌晨00:00:00
		cal.setTimeInMillis(cal.getTimeInMillis() - millisecond);

		if (flag == 0) {
			return cal.getTime();
		} else if (flag == 1) {
			// 凌晨23:59:59
			cal.setTimeInMillis(cal.getTimeInMillis() + 23 * 60 * 60 * 1000 + 59 * 60 * 1000 + 59 * 1000);
		}
		return cal.getTime();
	}

	/**
	 * 转换对应格式的时间
	 * @param dateStr
	 * @param pattern
	 * @return
	 * @throws BusinessException
	 */
	public static Date paraseDate(String dateStr,String pattern) throws BusinessException{
		try {
			SimpleDateFormat sf = new SimpleDateFormat(pattern);
			if(!StringUtils.isBlank(dateStr)) {
				Date d = sf.parse(dateStr);
				return d;
			}else {
				return null;
			}
		} catch (ParseException e) {
			log.error("格式化时间错误", e);
			throw new BusinessException(e.getMessage());
		}
	}

	// public static void main(String[] args) throws ParseException {
	// SimpleDateFormat simFormat = new SimpleDateFormat("yyyy.MM.dd");
	// Date date = simFormat.parse("2017.01.31");
	// System.out.println(getBeginDayToFixedDay(date, 31));
	// }
	public static void main(String[] args) throws ParseException, BusinessException {
		Date date1 = new Date();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		Date date2 = sf.parse("2017-08-01");
		int differDays = contrastDifferDays(date1, date2);
		System.out.println(differDays);
	}
}

package org.shenlei.task.manager.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.shenlei.task.manager.constant.CommonConstant;

/**
 * @author: 谌磊
 * @date: 2018年5月4日 上午11:59:14
 * 
 */
public class DateUtils {

	/** 日期格式：yyyy-MM-dd */
	public static final String FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
	/** 日期格式：HH:mm:ss */
	public static final String HH_MM_SS = "HH:mm:ss";
	/** 日期格式：yyyy-MM-dd HH:mm:ss */
	public static final String STANDARDFORMAT = "yyyy-MM-dd HH:mm:ss";
	/** 日期格式：yyyy-MM-dd HH:mm */
	public static final String STANDARDFORMATTOMINI = "yyyy-MM-dd HH:mm";
	/** 日期格式：yyyyMMddHHmmss */
	public static final String NONEFORMAT = "yyyyMMddHHmmss";
	/** 日期格式：yyyyMMdd */
	public static final String YYYYMMDD = "yyyyMMdd";
	/** 日期格式：yyyyMMddHH */
	public static final String YYYYMMDDHH = "yyyyMMddHH";
	/** 日期格式：yyyyMM */
	public static final String YYYYMM = "yyyyMM";
	/** 日期格式：yyyy-MM */
	public static final String YYYY_MM = "yyyy-MM";
	/** 日期格式：yyyy */
	public static final String YYYY = "yyyy";
	/** 日期格式：yyyyMMddHHmmssSSS */
	public static final String MILLISECONDS_NONEFORMAT = "yyyyMMddHHmmssSSS";
	/** 最小时间：00:00:00 */
	public static final String HHMMSS_MIN = "00:00:00";
	/** 最大时间：23:59:59 */
	public static final String HHMMSS_MAX = "23:59:59";
	

	public static String dateStr5(Date date) {
		// 招商贷需求变更 短信时间由12小时改为24小时制 高才 2012/08/12 update start
		// SimpleDateFormat format = new
		// SimpleDateFormat("yyyy年MM月dd日hh时mm分ss秒");
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
		// 高才 2012/08/12 update end
		String str = format.format(date);
		return str;
	}

	/**
	 * 将字符串转为时间戳 
	 * 
	 * @param userTime
	 * @return
	 */
	public static String getTime(String userTime) {

		String reTime = null;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");

		Date d;

		try {

			d = sdf.parse(userTime);

			long l = d.getTime();

			String str = String.valueOf(l);

			reTime = str.substring(0, 10);

		} catch (ParseException e) {

			// TODO Auto-generated catch block

			e.printStackTrace();

		}

		return reTime;

	}

	/**
	 * 将秒转换成时间
	 * 
	 * @param times
	 * @return
	 */
	public static Date getDate(String times) {
		long time = Long.parseLong(times);
		return new Date(time * 1000);
	}

	public static long getTime(Date date) {
		return date.getTime() / 1000;
	}

	public static int getDay(Date d) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * s - 表示 "yyyy-mm-dd" 形式的日期的 String 对象
	 * 
	 * @param s
	 * @return
	 */
	public static Date valueOf(String s) {
		final int yearLength = 4;
		final int monthLength = 2;
		final int dayLength = 2;
		final int maxMonth = 12;
		final int maxDay = 31;
		int firstDash;
		int secondDash;
		int threeDash = 0;
		int fourDash = 0;
		Date d = null;

		if (s == null) {
			throw new IllegalArgumentException();
		}
		firstDash = s.indexOf('-');
		secondDash = s.indexOf('-', firstDash + 1);
		if (s.contains(CommonConstant.COLON)) {
			threeDash = s.indexOf(':');
			fourDash = s.indexOf(':', threeDash + 1);
		}
		if ((firstDash > 0) && (secondDash > 0) && (secondDash < s.length() - 1)) {
			String yyyy = s.substring(0, firstDash);
			String mm = s.substring(firstDash + 1, secondDash);
			String dd = "";
			String hh = "";
			String mnt = "";
			String ss = "";
			if (s.contains(CommonConstant.COLON)) {
				dd = s.substring(secondDash + 1, threeDash - 3);
				hh = s.substring(threeDash - 2, threeDash);
				mnt = s.substring(threeDash + 1, fourDash);
				ss = s.substring(fourDash + 1);
			} else {
				dd = s.substring(secondDash + 1);
			}
			if (yyyy.length() == yearLength && mm.length() == monthLength && dd.length() == dayLength) {
				int year = Integer.parseInt(yyyy);
				int month = Integer.parseInt(mm);
				int day = Integer.parseInt(dd);
				int hour = 0;
				int minute = 0;
				int second = 0;
				if (s.contains(CommonConstant.COLON)) {
					hour = Integer.parseInt(hh);
					minute = Integer.parseInt(mnt);
					second = Integer.parseInt(ss);
				}
				int oneHundred = 100;
				int fourHundred = 400;
				int four = 400;
				if (month >= 1 && month <= maxMonth) {
					int maxDays = maxDay;
					switch (month) {
					// February determine if a leap year or not
					case 2:
						boolean isLeapYear = (year % four == 0 && !(year % oneHundred == 0)) || (year % fourHundred == 0);
						if (isLeapYear) {
							// leap year so 29 days in February
							maxDays = maxDay - 2;
						} else {
							// not a leap year so 28 days in February
							maxDays = maxDay - 3;
						}
						break;
					// April, June, Sept, Nov 30 day months
					case 4:
					case 6:
					case 9:
					case 11:
						maxDays = maxDay - 1;
						break;
					default:
					}
					if (day >= 1 && day <= maxDays) {
						Calendar cal = Calendar.getInstance();
						cal.set(year, month - 1, day, hour, minute, second);
						cal.set(Calendar.MILLISECOND, 0);
						d = cal.getTime();
					}
				}
			}
		}
		if (d == null) {
			throw new IllegalArgumentException();
		}
		return d;
	}

	private static int getInt(String str) {
		if (str == null || str.equals("")) {
			return 0;
		}
		int ret = 0;
		try {
			ret = Integer.parseInt(str);
		} catch (NumberFormatException e) {
			ret = 0;
		}
		return ret;
	}

	/**
	 * @author lijie
	 * @param begin
	 * @param end
	 *            传入开始时间 和 结束时间 格式如：2012-09-07
	 * @return 返回Map 获取相隔多少年 get("YEAR")及为俩个时间年只差，月 天，类推 Key ： YEAR MONTH DAY
	 *         如果开始时间 晚于 结束时间 return null；
	 */

	public static Map<String, Integer> getApartTime(String begin, String end) {
		String[] temp = begin.split("-");
		String[] temp2 = end.split("-");
		if (temp.length > 1 && temp2.length > 1) {
			Calendar ends = Calendar.getInstance();
			Calendar begins = Calendar.getInstance();

			begins.set(getInt(temp[0]), getInt(temp[1]), getInt(temp[2]));
			ends.set(getInt(temp2[0]), getInt(temp2[1]), getInt(temp2[2]));
			if (begins.compareTo(ends) < 0) {
				Map<String, Integer> map = new HashMap<>(3);
				ends.add(Calendar.YEAR, -getInt(temp[0]));
				ends.add(Calendar.MONTH, -getInt(temp[1]));
				ends.add(Calendar.DATE, -getInt(temp[2]));
				map.put("YEAR", ends.get(Calendar.YEAR));
				map.put("MONTH", ends.get(Calendar.MONTH) + 1);
				map.put("DAY", ends.get(Calendar.DATE));
				return map;
			}
		}
		return null;
	}

	public static Date rollYear(Date d, int year) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.add(Calendar.YEAR, year);
		return cal.getTime();
	}

	public static Date rollDate(Date d, int year, int mon, int day) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.add(Calendar.YEAR, year);
		cal.add(Calendar.MONTH, mon);
		cal.add(Calendar.DAY_OF_MONTH, day);
		return cal.getTime();
	}

	// v1.6.6.2 RDPROJECT-277 xx 2013-10-22 start
	/**
	 * 获取当前时间-时间戳
	 * 
	 * @return
	 */
	public static int getNowTime() {
		return Integer.parseInt((System.currentTimeMillis() / 1000) + "");
	}
	// v1.6.6.2 RDPROJECT-277 xx 2013-10-22 end

	public static String getTimeStr(Date time) {
		long date = time.getTime();
		String str = Long.toString(date / 1000);
		return str;
	}

	public static String getTimeStr(String dateStr, String format) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date date;
		date = sdf.parse(dateStr);
		String str = DateUtils.getTimeStr(date);
		return str;
	}

	public static String rollMonth(String addtime, String timeLimit) {
		Date t = DateUtils.rollDate(DateUtils.getDate(addtime), 0, getInt(timeLimit), 0);
		return t.getTime() / 1000 + "";
	}

	public static String rollDay(String addtime, String timeLimitDay) {
		Date t = DateUtils.rollDate(DateUtils.getDate(addtime), 0, 0, getInt(timeLimitDay));
		return t.getTime() / 1000 + "";
	}

	/**
	 * 获取本周日的日期
	 * 
	 * @return
	 */
	public static String getCurrentWeekday() {
		int weeks = 0;
		int mondayPlus = DateUtils.getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, mondayPlus + 6);
		Date monday = currentDate.getTime();

		DateFormat df = DateFormat.getDateInstance();
		String preMonday = df.format(monday);
		return preMonday;
	}

	/**
	 * 获得当前日期与本周日相差的天数
	 * 
	 * @return
	 */
	private static int getMondayPlus() {
		Calendar cd = Calendar.getInstance();
		// 获得今天是一周的第几天，星期日是第一天，星期二是第二天......因为按中国礼拜一作为第一天所以这里减1
		int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1; 
		if (dayOfWeek == 1) {
			return 0;
		} else {
			return 1 - dayOfWeek;
		}
	}

	/**
	 * 获得本周一的日期
	 * 
	 * @return
	 */
	public static String getMondayOFWeek() {
		int weeks = 0;
		int mondayPlus = DateUtils.getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, mondayPlus);
		Date monday = currentDate.getTime();

		DateFormat df = DateFormat.getDateInstance();
		String preMonday = df.format(monday);
		return preMonday;
	}

	/**
	 * 获取当前月第一天
	 * 
	 * @param first
	 * @return
	 */
	public static String getFirstDayOfMonth(String first) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, 0);
		// 设置为1号,当前日期既为本月第一天
		c.set(Calendar.DAY_OF_MONTH, 1);
		return format.format(c.getTime());
	}

	/**
	 * 获取当月最后一天
	 * 
	 * @param last
	 * @return
	 */
	public static String getLastDayOfMonth(String last) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar ca = Calendar.getInstance();
		ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
		return format.format(ca.getTime());
	}

	/**
	 * v1.6.6.1 融华财富-首页显示统计 ljd 2013-10-15 start
	 * 获取上个月第一天 00:00:00
	 * 
	 * @return
	 */
	public static String getFirstDayOfLastMonth() {
		long time = 0;
		String monthBegin = getFirstDayOfMonth(null);
		try {
			Date lastMonthBegin = getDate(getTimeStr(monthBegin, "yyyy-MM-dd"));
			Date dateBegin = addMonth(lastMonthBegin, -1);
			time = dateBegin.getTime() / 1000;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return String.valueOf(time);
	}

	/**
	 * 获取上个月最后一天 23:59:59
	 * 
	 * @return
	 */
	public static String getLastDayOfLastMonth() {
		long time = 0;
		String monthEnd = getLastDayOfMonth(null);
		try {
			Date lastMonthEnd = getDate(getTimeStr(monthEnd, "yyyy-MM-dd"));
			Date dateEnd = addMonth(lastMonthEnd, -1);
			time = getLastSecIntegralTime(dateEnd).getTime() / 1000;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return String.valueOf(time);
	}
	// v1.6.6.1 融华财富-首页显示统计 ljd 2013-10-15 end

	/**
	 * 获取 date 时间 00:00:00 000
	 * 
	 * @param date
	 * @return
	 */
	public static Date getFirstSecIntegralTime(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(date.getTime());
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	/**
	 * 获取当前时间 00:00:00 000
	 * 
	 * @return
	 */
	public static Date getFirstSecIntegralTime() {

		return getFirstSecIntegralTime(new Date());
	}

	/**
	 * 获取 date 时间 23:59:59 000
	 * 
	 * @param date
	 * @return
	 */
	public static Date getLastSecIntegralTime(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(date.getTime());
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.SECOND, 51);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	/**
	 * 获取当前时间 23:59:59 000
	 * 
	 * @return
	 */
	public static Date getLastSecIntegralTime() {

		return getLastSecIntegralTime(new Date());
	}

	/**
	 * 获取当前时间的秒数字符串
	 * 
	 * @return
	 */
	public static String getCurrentSecStr() {
		return String.valueOf(getCurrentSec());
	}

	/**
	 * 获取当前时间的秒数
	 * 
	 * @return
	 */
	public static long getCurrentSec() {
		return System.currentTimeMillis() / 1000;
	}

	/**
	 * 返回 date 增加 day 天数后的结果
	 * 
	 * @param date
	 * @param day
	 * @return
	 */
	public static Date addDay(Date date, int day) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, day);
		return cal.getTime();
	}

	/**
	 * 得到某天加上天数的时间戳
	 * 
	 * @param date
	 *            日期
	 * @param dayNum
	 *            天数
	 * @return
	 */
	public static final Long getThisDateDayLongTime(Date date, Integer dayNum) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, dayNum);
		Date date1 = calendar.getTime();
		return date1.getTime();
	}

	/**
	 * 返回 date 增加 month 月数后的结果
	 * 
	 * @param date
	 * @param month
	 * @return
	 */
	public static Date addMonth(Date date, int month) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, month);
		return cal.getTime();
	}

	/**
	 * 获取指定时间对应的毫秒数
	 * 
	 * @param time
	 *            HH:mm:ss
	 * @return
	 */
	public static long getTimeMillis(String time) {
		try {
			DateFormat dateFormat = new SimpleDateFormat(FORMAT_YYYY_MM_DD + CommonConstant.STR_SPACE + HH_MM_SS);
			DateFormat dayFormat = new SimpleDateFormat(FORMAT_YYYY_MM_DD);
			String strTime = dayFormat.format(new Date()) + CommonConstant.STR_SPACE + time;
			Date curDate = dateFormat.parse(strTime);
			return curDate.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static String getDateFormatLong(Long date) {
		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		return format1.format(date);
	}

	/**
	 * 将时间戳转换为时间
	 */
	public static String strLongTimeTransTo(String strLongTime, String timeFormat) {
		int ten = 10;
		int thirteen = 13;
		if (StringUtils.isEmpty(strLongTime)) {
			return null;
		} else if (strLongTime.length() == ten) {
			strLongTime += "000";
		}
		if (strLongTime.length() != thirteen) {
			return null;
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(timeFormat);
		return simpleDateFormat.format(Long.parseLong(strLongTime));
	}

	/**
	 * 获得当前日期字符串
	 *
	 * @return 返回当前时间字符串
	 */
	public static String getCurrentTime() {
		Date date = new Date();
		String nowTime = getDateUseDateParameter(date);
		return nowTime;
	}

	public static String getDateUseDateParameter(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(STANDARDFORMAT);
		String nowTime = dateFormat.format(date);
		return nowTime;
	}

	/**
	 *
	 * <p>
	 * 
	 * @param startTime
	 *            : 带格式的起始时间
	 *            <p>
	 * @param endTime
	 *            ： 带格式的结束时间
	 *            <p>
	 * @param toCompareTime
	 *            ： 带格式的要比较的时间
	 *            </p>
	 * 
	 * @return
	 */
	public static boolean judgmentTimeTeriod(String startTime, String endTime, String toCompareTime) {
		// 判断null
		if (StringUtils.isEmpty(startTime) || StringUtils.isEmpty(endTime) || StringUtils.isEmpty(toCompareTime)) {
			return false;
		}
		Long dateStart = getCurrentDate(startTime, STANDARDFORMAT).getTime();
		Long dateEnd = getCurrentDate(endTime, STANDARDFORMAT).getTime();
		Long datetoCompare = getCurrentDate(toCompareTime, STANDARDFORMAT).getTime();
		Long dataStartTmp = dateStart > dateEnd ? dateEnd : dateStart;
		Long dataEndTmp = dateEnd > dateStart ? dateEnd : dateStart;
		return (datetoCompare >= dataStartTmp) && (datetoCompare <= dataEndTmp) ? true : false;
	}

	/**
	 * 获得当前时间，带时间格式
	 *
	 * @return 返回Date类型当前时间
	 */
	public static Date getCurrentDate(String orgiTime, String format) {
		Date date = null;
		if (orgiTime == null || orgiTime.equalsIgnoreCase("")) {
			orgiTime = getCurrentTime();
		}
		try {
			date = new SimpleDateFormat(format).parse(orgiTime);
		} catch (ParseException e) {
			e.printStackTrace();
			date = new Date();
		}
		return date;
	}

	/**
	 * 指定时间格式
	 *
	 * @param format
	 * @return
	 */
	public static String getSpecifiedTimeformat(String format) {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		String nowTime = dateFormat.format(date);
		return nowTime;
	}

	/**
	 * 获得指定日期的前几天
	 *
	 * @param specifiedDay
	 * @return
	 * @throws Exception
	 */
	public static String getSpecifiedDayAfter(String specifiedDay, int d) {
		SimpleDateFormat sf = new SimpleDateFormat(YYYYMMDD);
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, d);
		String dayBefore = sf.format(c.getTime());
		return dayBefore;
	}

	public static LocalDate timestampToLocalDate(long timestamp) {
		return Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDate();
	}

}

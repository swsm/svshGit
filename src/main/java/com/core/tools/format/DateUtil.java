package com.core.tools.format;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * ClassName: DateUtil
 * </p>
 * <p>
 * Description: 时间操作Util类
 * </p>
 */
public class DateUtil {
    /**
     * 日志
     */
    private static Logger logger = LoggerFactory.getLogger(DateUtil.class);

    /**
     * 日期格式化对象，格式为yyyy-MM-dd
     */
    private final static SimpleDateFormat dateFormat = new SimpleDateFormat(
            "yyyy-MM-dd");

    /**
     * 日期格式化对象，格式为yyyy-MM
     */
    private final static SimpleDateFormat monthFormat = new SimpleDateFormat(
            "yyyy-MM");

    /**
     * 时间格式化字符串
     */
    private static final String DEFAULT_DATETIME_MASK = "yyyy-MM-dd HH:mm:ss";

    /**
     * 日期格式化字符串
     */
    private static final String DEFAULT_DATE_MASK = "yyyy-MM-dd";

    /**
     * 一周天数
     */
    public static final int MAX_DAYS_IN_WEEK = 7;

    /**
     * 一个月设定为跨服最多6周
     */
    public static final int MAX_WEEKS_IN_MONTH = 6;

    /**
     * 一年366天
     */
    public static final int DAYS_IN_YEAR_366 = 366;

    /**
     * 一年365天
     */
    public static final int DAYS_IN_YEAR_365 = 365;

    /**
     * 一个月最多31天
     */
    public static final int MAX_DAYS_IN_MONTH = 31;


    /**
     * 根据格式化工具格式化dateStr
     *
     * @param dateStr
     * @param dateFormat
     * @return Date
     */
    private static Date parsorToDate(String dateStr, SimpleDateFormat dateFormat) {
        Date date = null;
        if (logger.isDebugEnabled()) {
            logger.debug("converting '" + dateStr);
        }
        try {
            date = dateFormat.parse(dateStr);
        } catch (ParseException e) {
            logger.error(e.getMessage(), e);
        }
        return date;
    }


    /**
     * <p>
     * Description: 解析指定格式的日期
     * </p>
     *
     * @param dateStr  日期字符串
     * @param dateMask 日期格式
     * @return 日期
     */
    public static Date getDate(String dateStr, String dateMask) {
        if (StringUtils.isEmpty(dateStr)) {
            return null;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateMask);
        return DateUtil.parsorToDate(dateStr, dateFormat);
    }

    /**
     * 根据给定的日期以及格式，格式化成相应的字符串；
     * 若date=null，则返回null；
     * 若dateMask为null，返回默认的日期格式
     * 若dateMask无效，则返回null
     *
     * @param date
     * @param dateMask
     * @return
     */
    public static String getDateStr(Date date, String dateMask) {
        String dateStr = null;
        if (date != null) {
            if (dateMask == null) {
                dateMask = DateUtil.DEFAULT_DATE_MASK;
            }
            SimpleDateFormat sdf = null;
            try {
                sdf = new SimpleDateFormat(dateMask);
                dateStr = sdf.format(date);
            } catch (IllegalArgumentException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return dateStr;
    }

    /**
     * 根据日期字符串解析成日期
     *
     * @param dateStr 默认的日期格式yyyy-MM-dd
     * @return Date 精度到日期
     */
    public static Date getDefaultDate(String dateStr) {
        if (StringUtils.isEmpty(dateStr)) {
            return null;
        }
        return DateUtil.parsorToDate(dateStr, DateUtil.dateFormat);
    }

    /**
     * 根据时间字符串获取默认的格式的时间数据
     *
     * @param dateTimeStr yyyy-MM-dd HH:mm:ss
     * @return Date 精度到时分秒
     */
    public static Date getDefaultDateTime(String dateTimeStr) {
        return DateUtil.getDate(dateTimeStr, DateUtil.DEFAULT_DATETIME_MASK);
    }

    /**
     * 获取默认时间格式的日期开始时间
     *
     * @param dateTimeStr yyyy-MM-dd HH:mm:ss
     * @return Date 日期的开始时间；例如：2017-08-12 00:00:00格式的时间
     */
    public static Date getDefaultDateBegin(String dateTimeStr) {
        Date dateTime = DateUtil.getDefaultDateTime(dateTimeStr);
        return DateUtil.getBeginTime(dateTime);
    }

    /**
     * 获取默认时间格式的日期结束时间
     *
     * @param dateTimeStr yyyy-MM-dd HH:mm:ss
     * @return Date 日期的开始时间；例如：2017-08-12 23:59:59格式的时间
     */
    public static Date getDefaultDateEnd(String dateTimeStr) {
        Date dateTime = DateUtil.getDefaultDateTime(dateTimeStr);
        return DateUtil.getEndTime(dateTime);
    }

    /**
     * 获取当前的开始时间
     *
     * @param date
     * @return
     */
    public static Date getBeginTime(Date date) {
        if (date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.HOUR, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            return calendar.getTime();
        }
        return null;
    }

    /**
     * 获取当前的结束时间
     *
     * @param date
     * @return
     */
    public static Date getEndTime(Date date) {
        if (date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.HOUR, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND, 0);
            return calendar.getTime();
        }
        return null;
    }

    /**
     * @param dateStr
     * @return
     */
    public static Date getDefaultDateTimeBegin(String dateStr) {
        Date date = DateUtil.getDefaultDate(dateStr);
        return DateUtil.getBeginTime(date);
    }

    /**
     * @param dateStr
     * @return
     */
    public static Date getDefaultDateTimeEnd(String dateStr) {
        Date date = DateUtil.getDefaultDate(dateStr);
        return DateUtil.getEndTime(date);
    }

    /**
     * @return
     */
    public static Date getTodayTimeBegin() {
        Date date = new Date();
        return DateUtil.getBeginTime(date);
    }

    /**
     * @return
     */
    public static Date getTodayTimeEnd() {
        Date date = new Date();
        return DateUtil.getEndTime(date);
    }

    /**
     * <p>
     * Description: 当前月的起始时间
     * </p>
     *
     * @return 当前月的起始时间
     */
    public static Date getCurrentMonthTimeBegin() {
        Calendar calendar;
        calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date date = calendar.getTime();
        return DateUtil.getBeginTime(date);
    }

    /**
     * <p>
     * Description: 当前月的结束时间
     * </p>
     *
     * @return 当前月的结束时间
     */
    public static Date getCurrentMonthTimeEnd() {
        Calendar calendar;
        calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH,
                calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date date = calendar.getTime();
        return DateUtil.getEndTime(date);
    }

    /**
     * 返回yyyyMMdd格式的日期字符串
     *
     * @return 日期字符串
     * @author zhangwei
     */
    public static String getCurDateYMDStr() {
        Date curDate = new Date();
        return DateUtil.getDateStr(curDate, "yyyyMMdd");
    }

    /**
     * 返回yyyy-MM-dd格式的日期字符串
     *
     * @return 日期字符串
     * @author zhangwei
     */
    public static String getCurDateDefaultStr() {
        Date curDate = new Date();
        return DateUtil.getDateStr(curDate, DEFAULT_DATE_MASK);
    }


    /**
     * 获取当前年月，格式yyyy-MM
     *
     * @return Date
     */
    public static String getCurrentTimeYyyyMmStr() {
        return monthFormat.format(new Date());
    }

    /**
     * <p>
     * Description: 获取参数日期之前的日期字符串数组
     * </p>
     *
     * @param date 日期
     * @return 日期字符串数组
     */
    public static String[] getBeforeDefaultDateArr(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATE_MASK);

        Date monthStart = getMonthStart(date);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int index = calendar.get(Calendar.DAY_OF_MONTH);

        String[] dateArr;
        dateArr = new String[index];
        int num = 0;

        while (!monthStart.after(date)) {
            dateArr[num++] = sdf.format(monthStart);
            monthStart = getNext(monthStart);
        }
        return dateArr;
    }

    /**
     * <p>
     * Description: 根据日期获取当前月的第一天
     * </p>
     *
     * @param date 日期
     * @return 当前月第一天
     */
    public static Date getMonthStart(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int index = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.add(Calendar.DATE, (1 - index));
        return calendar.getTime();
    }

    /**
     * <p>
     * Description: 根据日期获取当前月的最后天
     * </p>
     *
     * @param date 日期
     * @return 当前月最后天
     */
    public static Date getMonthEnd(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        int index = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.add(Calendar.DATE, (-index));
        return calendar.getTime();
    }

    /**
     * <p>
     * Description: 根据日期获取第二天的日期
     * </p>
     *
     * @param date 日期
     * @return 第二天的日期
     */
    public static Date getNext(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        return calendar.getTime();
    }

    /**
     * <p>
     * Description: 根据日期获取第二天的日期
     * </p>
     *
     * @param date 日期
     * @return 第二天的日期
     */
    public static Date getYesterday(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -1);
        return calendar.getTime();
    }

    /**
     * <p>
     * Description: 获取与当前日期相间隔的的日期
     * </p>
     *
     * @param date 日期
     * @param day  间隔天数
     * @return 日期
     */
    public static Date getDayTime(Date date, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, day);
        return calendar.getTime();
    }

    /**
     * 显示两个时间段的每一天 格式yyyy-MM-dd
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return dates
     */
    public static List<String> displayBetweenTwoDay(String startTime, String endTime) {
        List<String> dates;
        dates = new ArrayList<String>();
        SimpleDateFormat dayFormat = new SimpleDateFormat(DEFAULT_DATETIME_MASK);
        try {
            Date start;
            start = dayFormat.parse(startTime);
            Date end;
            end = dayFormat.parse(endTime);
            Calendar calendar;
            calendar = new GregorianCalendar();
            calendar.setTime(start);
            while (calendar.getTime().before(end)) {
                dates.add(dayFormat.format(calendar.getTime()));
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }
            dates.add(dayFormat.format(end));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dates;
    }

    /**
     * 显示两个时间段的每一月 格式yyyy-MM
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return dates
     */
    public static List<String> displayBetweenTwoMonth(String startTime, String endTime) {
        List<String> dates;
        dates = new ArrayList<String>();
        try {
            Date start;
            start = monthFormat.parse(startTime);
            Date end;
            end = monthFormat.parse(endTime);
            Calendar calendar;
            calendar = new GregorianCalendar();
            calendar.setTime(start);
            while (calendar.getTime().before(end)) {
                dates.add(monthFormat.format(calendar.getTime()));
                calendar.add(Calendar.MONTH, 1);
            }
            dates.add(monthFormat.format(end));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dates;
    }


    /**
     * 说明：周显示逻辑，第一周从本年的第一个周一开始计算 2016年是52周
     *
     * @param date 日期
     * @return 周数
     * @author liniansheng
     */
    public static int getWeekOfYear(Date date) {
        Calendar calendar;
        calendar = Calendar.getInstance();
        // 每周以周一开始
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        // 每年的第一周必须大于或等于7天，否则就算上一年的最后一周
        calendar.setMinimalDaysInFirstWeek(DateUtil.MAX_DAYS_IN_WEEK);
        calendar.setTime(date);
        return calendar.get(Calendar.WEEK_OF_YEAR);

    }

    /**
     * <p>
     * Description: 准确获得周几
     * </p>
     *
     * @param calendar calendar对象
     * @return 周几
     * @author liniansheng
     */
    public static int getDayOfWeek(Calendar calendar) {
        // 每周以周一开始
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        // 每年的第一周必须大于或等于7天，否则就算上一年的最后一周
        calendar.setMinimalDaysInFirstWeek(DateUtil.MAX_DAYS_IN_WEEK);
        boolean isFirstSunday;
        isFirstSunday = (calendar.getFirstDayOfWeek() == Calendar.MONDAY);
        // 获取周几
        int weekDay;
        weekDay = calendar.get(Calendar.DAY_OF_WEEK);
        // 若一周第一天为星期天，则-1
        if (isFirstSunday) {
            weekDay = weekDay - 1;
            if (weekDay == 0) {
                weekDay = 7;
            }
        }
        return weekDay;
    }

    /**
     * 得到某年某周的第一天
     *
     * @param year 年份
     * @param week 周数
     * @return 周开始日期
     * @author liniansheng
     */
    public static Date getFirstDayOfWeek(int year, int week) {
        Calendar c;
        c = new GregorianCalendar();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, Calendar.JANUARY);
        c.set(Calendar.DATE, 1);
        Calendar cal = (GregorianCalendar) c.clone();
        cal.add(Calendar.DATE, week * MAX_DAYS_IN_WEEK);
        return getFirstDayOfWeek(cal.getTime());
    }

    /**
     * 得到某年某周的最后一天
     *
     * @param year 年份
     * @param week 周数
     * @return 周开始日期
     * @author liniansheng
     */
    public static Date getLastDayOfWeek(int year, int week) {
        Calendar c;
        c = new GregorianCalendar();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, Calendar.JANUARY);
        c.set(Calendar.DATE, 1);
        Calendar cal = (GregorianCalendar) c.clone();
        cal.add(Calendar.DATE, week * MAX_DAYS_IN_WEEK);
        return getLastDayOfWeek(cal.getTime());
    }

    /**
     * 取得指定日期所在周的第一天
     *
     * @param date 日期
     * @return 开始日期
     * @author liniansheng
     */
    public static Date getFirstDayOfWeek(Date date) {
        Calendar c;
        c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday
        return c.getTime();
    }

    /**
     * 取得指定日期所在周的最后一天
     *
     * @param date 日期
     * @return 周最后的日期
     * @author liniansheng
     */
    public static Date getLastDayOfWeek(Date date) {
        Calendar c;
        c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK,
                (c.getFirstDayOfWeek() + MAX_DAYS_IN_WEEK - 1)); // Sunday
        return c.getTime();
    }

    /**
     * 取得业务上的该年的周数
     *
     * @param year 日期
     * @return 周数
     * @author liniansheng
     */
    public static int getWeekCountOfYear(String year) {
        Calendar calendar;
        calendar = Calendar.getInstance();
        // 每周以周一开始
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        // 每年的第一周必须大于或等于7天，否则就算上一年的最后一周
        calendar.setMinimalDaysInFirstWeek(MAX_DAYS_IN_WEEK);
        // 取得year-12-31号所在的周
        calendar.set(Integer.valueOf(year), Calendar.DECEMBER, MAX_DAYS_IN_MONTH);
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * dateFirst比dateEnd多的天数
     *
     * @param dateFirst 开始时间
     * @param dateEnd   结束时间
     * @return 结果集
     */
    public static int differentDays(Date dateFirst, Date dateEnd) {
        Calendar calFirst;
        calFirst = Calendar.getInstance();
        calFirst.setTime(dateFirst);
        Calendar calEnd;
        calEnd = Calendar.getInstance();
        calEnd.setTime(dateEnd);
        int dayFirst;
        dayFirst = calFirst.get(Calendar.DAY_OF_YEAR);
        int dayEnd;
        dayEnd = calEnd.get(Calendar.DAY_OF_YEAR);
        int yearFirst;
        yearFirst = calFirst.get(Calendar.YEAR);
        int yearEnd;
        yearEnd = calEnd.get(Calendar.YEAR);
        if (yearFirst != yearEnd) { // 同一年
            int timeDistance = 0;
            for (int i = yearFirst; i < yearEnd; i++) {
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) { // 闰年
                    timeDistance += 366;
                } else { // 不是闰年
                    timeDistance += 365;
                }
            }

            return Math.abs((timeDistance + dayEnd - dayFirst));
        } else { // 不同年
            return Math.abs(dayFirst - dayEnd);
        }
    }

    /**
     * 获取7位的时间格式字符串，其中月份只有1位，格式为yyyyMdd，月份为用16进制表示
     *
     * @param date
     * @return
     */
    public static String getDateStr7(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar;
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);
        String dayStr = DateUtil.supplyInt(day, "0", 2);
        String monthStr = Integer.toHexString(month + 1);
        return year + monthStr + dayStr;
    }

    /**
     * 向前补齐数字，
     *
     * @param num 需要补齐的数字
     * @param s   补齐的字符串
     * @param len 补齐后字符串长度
     * @return
     */
    public static String supplyInt(int num, String s, int len) {
        String numStr = num + "";
        int l = numStr.length();
        int div = len - l;
        if (div <= 0) {
            return numStr;
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < div; i++) {
            builder.append(s);
        }
        builder.append(numStr);
        return builder.toString();
    }

    public static boolean isBetween(Date effectDate, Date invalidDate, Date currentDate) {
        if (currentDate.compareTo(effectDate) >= 0 && currentDate.compareTo(invalidDate) <= 0) {
            return true;
        }
        return false;
    }

    public static double calHousr(Date start, Date end) {
        long startTime = start.getTime();
        long endTime = end.getTime();
        return (endTime - startTime) / 1000 / 3600.0;
    }
}

package com.core.tools.format;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeHelper {

    /**
     * 日期格式到天
     */

    private static SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");


    public static String date2String(long dateCount) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentTime_1 = new Date(dateCount);
        if (currentTime_1 == null)
            return "";
        String dateString = formatter.format(currentTime_1);
        return dateString;

    }

    public static String date2String(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentTime_1 = date;
        if (currentTime_1 == null)
            return "";
        String dateString = formatter.format(currentTime_1);
        return dateString;

    }

    public static long string2Date(String dateString) {
        // Parse the previous string back into a Date.
        if (dateString == null)
            return 0;
        //if (SchUtil.DEBUG) System.out.println("not null");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        Date currentTime_2 = formatter.parse(dateString.trim(), pos);
        if (currentTime_2 == null)
            return 0;
        //if (SchUtil.DEBUG) System.out.println("parse not null");
        return currentTime_2.getTime();
    }

    public static Date string2DateTime(String dateString) {
        // Parse the previous string back into a Date.
        if (dateString == null)
            return null;
        //if (SchUtil.DEBUG) System.out.println("not null");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        Date currentTime_2 = formatter.parse(dateString.trim(), pos);
        //if (SchUtil.DEBUG) System.out.println("parse not null");
        return currentTime_2;
    }

    /**
     * 取得两个时间段的间隔天数。
     * <p>
     * 如果两者相等，包括都等于null；则直接返回0;<br>
     * 如果两者之一等于null；则返回{@link Integer#MAX_VALUE};
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int getBetweenDays(Date date1, Date date2) {
        if (date1 == null ? date2 == null : date1.equals(date2)) {
            return 0;
        }
        if (date1 == null || date2 == null) {
            return Integer.MAX_VALUE;
        }
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(date1);
        c2.setTime(date2);
        if (c1.after(c2)) {
            Calendar c = c1;
            c1 = c2;
            c2 = c;
        }
        int betweenYears = c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR);
        int betweenDays = c2.get(Calendar.DAY_OF_YEAR)
                - c1.get(Calendar.DAY_OF_YEAR);
        for (int i = 0; i < betweenYears; i++) {
            c1.set(Calendar.YEAR, (c1.get(Calendar.YEAR) + 1));
            betweenDays += c1.getMaximum(Calendar.DAY_OF_YEAR);
        }
        return betweenDays;
    }

    /**
     * 取得两个时间段的时间间隔，当开始时间大于结束时间时，返回0
     *
     * @param dateString1：yyyy-MM-dd格式的时间字符串
     * @param dateString2：yyyy-MM-dd格式的时间字符串
     * @return t2 与t1的间隔天数
     * @throws ParseException：如果输入的日期格式不是yyyy-MM-dd格式抛出异常
     */
    public static int getBetweenDays(String dateString1, String dateString2)
            throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        int betweenDays = 0;
        Date d1 = format.parse(dateString1);
        Date d2 = format.parse(dateString2);
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(d1);
        c2.setTime(d2);
        // 保证第二个时间一定大于第一个时间 
        if (c1.after(c2)) {
            return 0;
        }
        int betweenYears = c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR);
        betweenDays = c2.get(Calendar.DAY_OF_YEAR)
                - c1.get(Calendar.DAY_OF_YEAR);
        for (int i = 0; i < betweenYears; i++) {
            c1.set(Calendar.YEAR, (c1.get(Calendar.YEAR) + 1));
            betweenDays += c1.getMaximum(Calendar.DAY_OF_YEAR);
        }
        return betweenDays;
    }

    /**
     * 将毫秒转换为指定格式的日期字符串
     *
     * @param millSeconds：毫秒
     * @param dateFormatter：日期格式化字符串
     * @return 指定格式的日期字符串
     */
    public static String long2String(long millSeconds, String dateFormatter) {
        if (null == dateFormatter || dateFormatter.equalsIgnoreCase("")) {
            dateFormatter = "yyyy-MM-dd";
        }
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormatter);
        Date currentTime_1 = new Date(millSeconds);
        if (currentTime_1 == null)
            return "";
        String dateString = formatter.format(currentTime_1);
        return dateString;

    }


    /**
     * 将自然日期时间的描述格式，转换为Quartz软件可识别的Cron表达式格式。
     * <p>如：将如下自然时间表达式转换为Cron表达式：
     * <li>16:15:00 day 转换为0 15 16 * * ?</li>
     * <li>16:15:00 week 1,5转换为0 15 16 ? * 1,5</li>
     * <li>16:15:00 month 1,8,11转换为0 15 16 1,8,11 * ?</li>
     * <li>05:00:00 month 1,8,11转换为0 0 5 1,8,11 * ?</li></p>
     *
     * @return Cron表达式格式。
     */
    public static String toCronExpression(String s) {
        StringBuffer retVal = new StringBuffer();
        try {
            String[] cells = s.trim().split(" ");
            String time = cells[0];
            String[] timeCells = time.split(":");
            //去除时间前缀的0，如：05需要改为5.
            for (int i = 0; i < timeCells.length; i++) {
                if (timeCells[i].startsWith("0")) {
                    timeCells[i] = timeCells[i].substring(1);
                }
            }
            String hour = timeCells[0];
            String minute = timeCells[1];
            String second = timeCells[2];
            retVal.append(second).append(" ");
            retVal.append(minute).append(" ");
            retVal.append(hour).append(" ");

            //构造后续的Cron表达式
            if (cells[1].equals("day")) {
                retVal.append("* * ?");
            } else if (cells[1].equals("week")) {
                String dayOfWeeks = cells[2];
                retVal.append("? * ").append(dayOfWeeks);
            } else if (cells[1].equals("month")) {
                String dayOfMonths = cells[2];
                retVal.append(dayOfMonths).append(" * ?");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Illegal datetime format.", e);
        }
        return retVal.toString();
    }

    /**
     * 将Quartz软件可识别的Cron表达式，转换为自然日期时间的描述格式。
     * <p>如：将如下Cron表达式转换为自然时间表达式：
     * <li>0 15 16 * * ? 转换为16:15:00 day</li>
     * <li>0 15 16 * * 1,5转换为16:15:00 week 1,5</li>
     * <li>0 15 16 1,8,11 * ?转换为16:15:00 month 1,8,11</li>
     * <li>0 0 5 1,8,11 * ?转换为05:00:00 month 1,8,11</li></p>
     *
     * @return 自然时间表达式。
     */
    public static String expressionToCron(String s) {
        StringBuffer time_sb = new StringBuffer();
        try {
            String[] cells = s.trim().split(" ");
            cells[0] = cells[0].trim().length() == 2 ? cells[0].trim() : "0" + cells[0].trim();
            cells[1] = cells[1].trim().length() == 2 ? cells[1].trim() : "0" + cells[1].trim();
            cells[2] = cells[2].trim().length() == 2 ? cells[2].trim() : "0" + cells[2].trim();
            time_sb.append(cells[2]);
            time_sb.append(":");
            time_sb.append(cells[1]);
            time_sb.append(":");
            time_sb.append(cells[0]);
            cells[3] = cells[3].trim();
            cells[4] = cells[4].trim();
            cells[5] = cells[5].trim();
            if (cells[3].equals("*") && cells[4].equals("*") && cells[5].equals("?")) {
                time_sb.append(" day");
            } else if (cells[3].equals("?") && cells[4].equals("*")) {
                time_sb.append(" week ");
                time_sb.append(cells[5]);
            } else {
                time_sb.append(" month ");
                time_sb.append(cells[3]);
            }
            toCronExpression(time_sb.toString());
        } catch (Exception e) {
            throw new IllegalArgumentException("Illegal datetime format.", e);
        }
        return time_sb.toString();
    }

    /**
     * 将时间转换为yyyy-MM-dd格式
     *
     * @param date 时间
     * @return 返回
     * @author liujie
     */
    public static String changeDayToYyyyMmDdStr(Date date) {
        return dayFormat.format(date);
    }
}

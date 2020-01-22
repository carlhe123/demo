package com.carl.demo.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Slf4j
public class DateUtil {

    /**
     * @author Carl.he
     * @description 将日期转换为指定的格式的字符串
     * @date 2020/1/17 15:19
     * @param oldDate 需要转换的日期
     * @param pattern 日期格式 eg. yyyy-MM-dd HH:mm:ss
     **/
    public static String formatDate2Str(Date oldDate, String pattern) {
        if (oldDate == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String newDateStr = sdf.format(oldDate);
        return newDateStr;
    }

    /**
     * @author Carl.he
     * @description 将日期字符串转换为指定格式的日期类型
     * @date 2020/1/17 15:51
     * @param dateStr 日期字符串
     * @param pattern 日期格式
     **/
    public static Date formatStr2Date(String dateStr,String pattern){
        if (StringUtils.isBlank(dateStr)){
            return null;
        }
        Date newDate = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            newDate = sdf.parse(dateStr);
        }catch (Exception ex){
            log.error("转换日期出现异常,cause={} {}",ex.getMessage(),ex.getStackTrace());
            ex.printStackTrace();
        }
        return newDate;
    }

    /**
     * 不考虑时间求天数差
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @param pattern   日期格式
     */
    public static long getDaysBetweenIgnoreTime(String startDate, String endDate,String pattern) {
        SimpleDateFormat localFormat = new SimpleDateFormat(pattern);
        try {
            java.util.Date startDay = localFormat.parse(startDate);
            java.util.Date endDay = localFormat.parse(endDate);
            return getDaysBetweenIgnoreTime(startDay, endDay);
        } catch (Exception e) {
            log.error("转换日期出现异常,cause={} {}",e.getMessage(),e.getStackTrace());
            return 0;
        }
    }

    /**
     * 不考虑时间求天数差
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     */
    public static long getDaysBetweenIgnoreTime(Date startDate, Date endDate) {
        startDate = getRoundedDay(startDate);
        endDate = getNextDay(endDate);
        return getBetweenDate(startDate, endDate);
    }

    /**
     * 生成某天零时的日期对象 例如：若输入时间为（2004-08-01 11:30:58），将获得（2004-08-01 00:00:00）的日期对象
     *
     * @return Date java.util.Date对象
     */
    public static Date getRoundedDay(Date dt) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(dt);
        return new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
                .getTime();
    }

    /**
     * 获得给定时间的第二天零时的日期对象 例如：若给定时间为（2004-08-01 11:30:58），将获得（2004-08-02
     * 00:00:00）的日期对象 若给定时间为（2004-08-31 11:30:58），将获得（2004-09-01 00:00:00）的日期对象
     *
     * @param dt Date 给定的java.util.Date对象
     * @return Date java.util.Date对象
     */

    public static Date getNextDay(Date dt) {

        if (dt == null) {
            return dt;
        }

        Calendar cal = new GregorianCalendar();
        cal.setTime(dt);

        cal.add(Calendar.DAY_OF_YEAR, 1);
        return cal.getTime();

    }

    public static long getBetweenDate(Date startDate, Date endDate) {
        long startDateTime = startDate.getTime();
        long endDateTime = endDate.getTime();
        long dayTime = 24 * 60 * 60 * 1000;
        long days = (endDateTime - startDateTime) / dayTime;
        return days;
    }

    /**
     * @author Carl.he
     * @description 在指定的日期上增加/减去对应的天数
     * @date 2020/1/17 16:02
     * @param date 需要增加/减去天数的日期
     * @param amount 天数，可以为负数
     **/
    public static Date addDays(Date date, int amount) {
        return DateUtils.addDays(date, amount);
    }

    /**
     * @author Carl.he
     * @description 在指定的日期上增加/减去对应的月份
     * @date 2020/1/17 16:02
     * @param date 需要增加/减去月份的日期
     * @param amount 月份，可以为负数
     **/
    public static Date addMonths(Date date, int amount) {
        return DateUtils.addMonths(date, amount);
    }

    /**
     * @author Carl.he
     * @description 在指定的日期上增加/减去对应的年数
     * @date 2020/1/17 16:02
     * @param date 需要增加/减去年数的日期
     * @param amount 年数，可以为负数
     **/
    public static Date addYears(Date date, int amount) {
        return DateUtils.addYears(date, amount);
    }

}

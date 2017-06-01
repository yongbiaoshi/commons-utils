package com.tsingda.commons.utils;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

public class DateUtil {

    public static final ZoneId ZONE = ZoneId.systemDefault();

    public static final String MINI_FMT_STR = "yyyyMMddHHmmss";
    private static final DateTimeFormatter MINI_FMT = DateTimeFormatter.ofPattern(MINI_FMT_STR);

    public static final String MD_FMT_STR = "MMdd";
    private static final DateTimeFormatter MD_FMT = DateTimeFormatter.ofPattern(MD_FMT_STR);

    public static final String DEFAULT_FMT_STR = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter DEFAULT_FMT = DateTimeFormatter.ofPattern(DEFAULT_FMT_STR);

    public static final String SLASH_DATE_FMT_STR = "yyyy/MM/dd HH:mm:ss";
    public static final DateTimeFormatter SLASH_DATE_FMT = DateTimeFormatter.ofPattern(SLASH_DATE_FMT_STR);

    public static final String FULL_FMT_STR = "yyyy-MM-dd HH:mm:ss.SSS";

    public static final String DAY_ZERO_FMT_STR = "yyyy-MM-dd 00:00:00";

    public static final String DAY_END_FMT_STR = "yyyy-MM-dd 23:59:59.999";

    public static final String YMD_FMT_STR = "yyyy-MM-dd";
    public static final DateTimeFormatter YMD_FMT = DateTimeFormatter.ofPattern(YMD_FMT_STR);

    public static final String HMS_FMT_STR = "HH:mm:ss";
    public static final DateTimeFormatter HMS_FMT = DateTimeFormatter.ofPattern(HMS_FMT_STR);

    public static final String HMS_SSS_FMT_STR = "HH:mm:ss.SSS";
    public static final DateTimeFormatter HMS_SSS_FMT = DateTimeFormatter.ofPattern(HMS_SSS_FMT_STR);

    public static Date now() {
        return new Date();
    }

    public static String miniFormat(Date date) {
        return toLocal(date).format(MINI_FMT);
    }

    public static String mdFormat(Date date) {
        return toLocalDate(date).format(MD_FMT);
    }

    public static Date ymdParse(String dateStr) {
        return toDate(LocalDate.parse(dateStr, YMD_FMT));
    }

    public static String ymdFormat(Date date) {
        return toLocal(date).format(YMD_FMT);
    }

    public static Date hmsParse(String dateStr) {
        LocalDateTime ldt = LocalDateTime.parse(dateStr, HMS_FMT);
        return toDate(ldt);
    }

    public static String hmsFormat(Date date) {
        return toLocal(date).format(HMS_FMT);
    }

    public static Date parse(String dateStr) {
        return toDate(LocalDateTime.parse(dateStr, DEFAULT_FMT));
    }

    public static String format(Date date) {
        return toLocal(date).format(DEFAULT_FMT);
    }

    public static String slashFormat(Date date) {
        return toLocal(date).format(SLASH_DATE_FMT);
    }

    public static String format(long timestamp) {
        return toLocal(new Date(timestamp)).format(DEFAULT_FMT);
    }

    public static String slashFormat(long timestamp) {
        return toLocal(new Date(timestamp)).format(SLASH_DATE_FMT);
    }

    public static Date yesterday() {
        return dayBefore(new Date(), 1);
    }

    public static Date tomorrow() {
        return dayAfter(new Date(), 1);
    }

    /**
     * 今天零点
     * 
     * @return 今天零点
     * @throws ParseException
     */
    public static Date zeroOfToday() {
        return toDate(LocalDateTime.of(LocalDate.now(), LocalTime.MIN));
    }

    /**
     * 指定日期的零�?
     * 
     * @param date
     *            日期
     * @return 指定日期的零�?
     * @throws ParseException
     */
    public static Date zeroOf(Date date) {
        return toDate(LocalDateTime.of(toLocal(date).toLocalDate(), LocalTime.MIN));
    }

    /**
     * 今天的结束时�?
     * 
     * @return 今天的结�?
     * @throws ParseException
     */
    public static Date endOfToday() {
        return toDate(LocalDateTime.of(LocalDate.now(), LocalTime.MAX));
    }

    /**
     * 指定日期的结束时�?
     * 
     * @param date
     *            日期
     * @return 指定日期的结束时�?
     * @throws ParseException
     */
    public static Date endOf(Date date) {
        return toDate(LocalDateTime.of(toLocal(date).toLocalDate(), LocalTime.MAX));
    }

    public static Date dayBefore(Date date, int l) {
        return minusDays(date, l);
    }

    public static Date dayAfter(Date date, int l) {
        return plusDays(date, l);
    }

    public static Date plusDays(Date date, int days) {
        LocalDate ld = toLocalDate(date);
        ld = ld.plusDays(days);
        return toDate(LocalDateTime.of(ld, toLocalTime(date)));
    }

    public static Date minusDays(Date date, int days) {
        LocalDate ld = toLocalDate(date);
        ld = ld.minusDays(days);
        return toDate(LocalDateTime.of(ld, toLocalTime(date)));
    }

    public static long diff(Date d1, Date d2) {
        return d2.getTime() - d1.getTime();
    }

    public static Date monthBeginning(int year, int month) {
        return toDate(LocalDate.of(year, month, 1));
    }

    public static Date lastMonthBeginning() {
        return toDate(LocalDate.now().plusMonths(-1).withDayOfMonth(1));
    }

    public static Date lastMonthEnding() {
        return toDate(LocalDate.now().withDayOfMonth(1).plusDays(-1));
    }

    public static List<Date> monthDays(int year, int month) {
        List<Date> list = new ArrayList<>();
        LocalDate f = LocalDate.of(year, month, 1);
        int l = f.lengthOfMonth();
        list.add(toDate(f));
        for (int i = 1; i < l; i++) {
            list.add(toDate(f.plusDays(i)));
        }
        return list;
    }

    /**
     * 两个时间段取交集
     * 
     * @param fStart
     *            时间�?1的开始时�?
     * @param fEnd
     *            时间�?1的结束时�?
     * @param sStart
     *            时间�?2的开始时�?
     * @param sEnd
     *            时间�?2的结束时�?
     * @return 两个时间段的交集，单位毫秒（ms），如果没有交集返回0�?
     */
    public static long intersection(Date fStart, Date fEnd, Date sStart, Date sEnd) {
        long fst = fStart.getTime();
        long fet = fEnd.getTime();
        long sst = sStart.getTime();
        long set = sEnd.getTime();
        long st = Long.max(fst, sst);
        long et = Long.min(fet, set);
        return Long.max(0, et - st);
    }

    /**
     * 周一0�?
     * 
     * @param date
     *            日期
     * @return 周一0�?
     * @throws ParseException
     */
    public static Date mondayZero(Date date) {
        LocalDate ld = toLocalDate(date);
        return toDate(ld.minusDays(ld.getDayOfWeek().getValue() - 1));
    }

    /**
     * 周日结束时间
     * 
     * @param date
     *            日期
     * @return 周日结束时间
     * @throws ParseException
     */
    public static Date sundayEnd(Date date) {
        LocalDate ld = toLocalDate(date);
        return toDate(ld.plusDays(DayOfWeek.SUNDAY.getValue() - ld.getDayOfWeek().getValue()), LocalTime.MAX);
    }

    /**
     * 返回指定日期的当前月份的第一�?
     * 
     * @param date
     * @return
     */
    public static Date monthStart(Date date) {
        LocalDate ld = toLocalDate(date);
        ld = LocalDate.of(ld.getYear(), ld.getMonthValue(), 1);
        return toDate(ld);
    }

    /**
     * 返回指定日期当前月份的最后一�?
     * 
     * @param date
     * @return
     */
    public static Date monthEnd(Date date) {
        LocalDate ld = toLocalDate(date);
        ld = LocalDate.of(ld.getYear(), ld.getMonthValue(), ld.lengthOfMonth());
        return toDate(ld);
    }

    /**
     * 功能描述：返回年�?
     * 
     * @param date
     *            Date 日期
     * @return 返回年份
     */
    public static int getYear(Date date) {
        return toLocalDate(date).getYear();
    }

    /**
     * 功能描述：返回月�?
     * 
     * @param date
     *            Date 日期
     * @return 返回月份
     */
    public static int getMonth(Date date) {
        return toLocalDate(date).getMonthValue();
    }

    public static LocalDateTime toLocal(Date d) {
        return LocalDateTime.ofInstant(d.toInstant(), ZONE);
    }

    public static LocalDate toLocalDate(Date d) {
        LocalDateTime ldt = LocalDateTime.ofInstant(d.toInstant(), ZONE);
        return ldt.toLocalDate();
    }

    public static LocalTime toLocalTime(Date d) {
        LocalDateTime ldt = LocalDateTime.ofInstant(d.toInstant(), ZONE);
        return ldt.toLocalTime();
    }

    public static Date toDate(LocalDateTime localDateTime) {
        Instant instant = localDateTime.atZone(ZONE).toInstant();
        return Date.from(instant);
    }

    public static Date toDate(LocalDate localDate) {
        LocalDateTime ldt = localDate.atStartOfDay();
        return toDate(ldt);
    }

    public static Date toDate(LocalTime localTime) {
        return toDate(LocalDate.now(ZONE), localTime);
    }

    public static Date toDate(LocalDate localDate, LocalTime localTime) {
        LocalDateTime ldt = localTime.atDate(localDate);
        return toDate(ldt);
    }

    public static void main(String[] args) {
        long s = now().getTime();
        IntStream.range(0, 1000).parallel().forEach(i -> {
            ymdParse("2017-05-13");
        });
        long e = now().getTime();
        System.out.println(e - s);
        Date yestoday = plusDays(now(), -1);
        System.out.println(yestoday);
        System.out.println(zeroOfToday());
        System.out.println(endOfToday());
        System.out.println(zeroOf(now()));
        System.out.println(endOf(now()));
        System.out.println(monthBeginning(2017, 2));
        System.out.println(lastMonthBeginning());
        System.out.println(lastMonthEnding());
        System.out.println(monthDays(2017, 2));
        System.out.println(monthDays(2017, 5));
        LocalDate ld = LocalDate.now();
        LocalDate monday = ld.minusDays(ld.getDayOfWeek().getValue() - 1);
        System.out.println(monday);

        System.out.println(mondayZero(now()));
        System.out.println(sundayEnd(now()));

        System.out.println(monthStart(lastMonthBeginning()));
        System.out.println(monthEnd(lastMonthBeginning()));
    }
}

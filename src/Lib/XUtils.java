package Lib;

import Model.Order;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class XUtils {
    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    static SimpleDateFormat month = new SimpleDateFormat("M");
    static SimpleDateFormat year = new SimpleDateFormat("yyyy");
    static SimpleDateFormat day = new SimpleDateFormat("dd");
    static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    static SimpleDateFormat month_name = new SimpleDateFormat("MMMM");
    public static String convertDatetoMonthInteger(Date date) {
        try {
            return month.format(date);
        } catch (Exception e) {
            return month.format(new Date());
        }
    }
    public static String convertDatetoMonthName(Date date) {
        try {
            return month_name.format(date);
        } catch (Exception e) {
            return month_name.format(new Date());
        }
    }
    public static Date convertStringtoDate(String text) {
        try {
            return sdf.parse(text);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String convertDatetoString(Date date) {
        try {
            return sdf.format(date);
        } catch (Exception e) {
            return sdf.format(new Date());
        }
    }
    public static int convertMonth(String text) {
        DateTimeFormatter parser = DateTimeFormatter.ofPattern("MMMM")
                .withLocale(Locale.ENGLISH);
        TemporalAccessor accessor = parser.parse(text);
        return accessor.get(ChronoField.MONTH_OF_YEAR);
    }
    public static Date findClosest(List<Order> orderList) {
        LocalDate[] days = new LocalDate[orderList.size()];
        int i = 0;
        Date temp;

        for (Order o: orderList) {
            temp = o.getDate();
            days[i] = LocalDate.of(Integer.parseInt(day.format(temp)), Integer.parseInt(month.format(temp)), Integer.parseInt(year.format(temp)));
            i++;
        }
        LocalDate targetDate = LocalDate.now();

        LocalDate closestDay = null;
        long minDistance = Long.MAX_VALUE;
        for (LocalDate day : days) {
            long distance = ChronoUnit.DAYS.between(targetDate, day);
            if (Math.abs(distance) < Math.abs(minDistance)) {
                minDistance = distance;
                closestDay = day;
            }
        }
        Date date = Date.from(closestDay.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return date;
    }
}

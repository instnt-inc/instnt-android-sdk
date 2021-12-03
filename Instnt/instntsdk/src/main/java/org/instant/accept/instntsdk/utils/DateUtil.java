package org.instant.accept.instntsdk.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static final String DATE_FORMAT = "yyyy-MM-dd";

    public static String convertDateFormat(String source, String inputFormat, String outputFormat){
        SimpleDateFormat inputFormatter = new SimpleDateFormat(inputFormat);
        SimpleDateFormat outputFormatter = new SimpleDateFormat(outputFormat);

        try {
            Date inputDate = inputFormatter.parse(source);
            return outputFormatter.format(inputDate);
        }catch (Exception e) {
            return "";
        }
    }

    public static String convertMilliToDate(String dateFormat, long milliseconds){
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        return formatter.format(calendar.getTime());
    }

    public static Date getDateFromString(String dateFormat, String dateStr) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        try {
            return formatter.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new Date();
    }
}

package com.gehtsoft.date;

import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by dkuzmin on 8/22/2016.
 */
public class GMTService implements IDateService {

    final static Logger logger = Logger.getLogger("date");

    public Date getDate(int field, int amount) {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Calendar calendar = Calendar.getInstance(timeZone);
        calendar.add(field, amount);
        String format = "EE MMM dd HH:mm:ss yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.US);
        simpleDateFormat.setTimeZone(timeZone);
        SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(format, Locale.US);
        try {
            return localSimpleDateFormat.parse(simpleDateFormat.format(calendar.getTime()));
        } catch (ParseException e) {
            if(logger.isDebugEnabled()) {
                logger.debug(e.getStackTrace());
            }
        }
        return null;
    }

    public Date getNow() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Calendar calendar = Calendar.getInstance(timeZone);
        String format = "EE MMM dd HH:mm:ss yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.US);
        simpleDateFormat.setTimeZone(timeZone);
        SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(format, Locale.US);
        try {
            return localSimpleDateFormat.parse(simpleDateFormat.format(calendar.getTime()));
        } catch (ParseException e) {
            if(logger.isDebugEnabled()) {
                logger.debug(e.getStackTrace());
            }
        }
        return null;
    }
}

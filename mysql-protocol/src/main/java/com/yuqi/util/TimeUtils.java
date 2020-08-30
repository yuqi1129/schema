package com.yuqi.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 30/8/20 21:30
 **/
public class TimeUtils {

    public static final Logger LOGGER = LoggerFactory.getLogger(TimeUtils.class);
    public static final SimpleDateFormat DATE_FORMATTER =
            new SimpleDateFormat("yyyy-MM-dd");

    public static final SimpleDateFormat TIMESTAMP_FORMATTER =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static Long getDate(String date) {
        try {
            return DATE_FORMATTER.parse(date).getTime();
        } catch (ParseException e1) {
            LOGGER.error("Parser date '{}' using format '{}' error, try to use format '{}'",
                    date,
                    DATE_FORMATTER.toPattern(),
                    TIMESTAMP_FORMATTER.toPattern()
            );
            try {
                return TIMESTAMP_FORMATTER.parse(date).getTime();
            } catch (ParseException e2) {
                LOGGER.error("Parser date '{}' using format '{}' error, return null",
                        date,
                        TIMESTAMP_FORMATTER.toPattern());
                return null;
            }
        }
    }

    public static String formatDate(Date date) {
        //treat null as "1970-01-01"
        return null == date ? "1970-01-01" : DATE_FORMATTER.format(date);
    }
}

package com.yuqi.engine.data.type;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;
import java.util.function.Function;

import static java.time.ZoneOffset.UTC;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_TIME;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 5/8/20 14:43
 **/
public class TimestampType extends DataType<Long> implements FixedWidthType {
    public static final int ID_WITH_TZ = 11;
    public static final int ID_WITHOUT_TZ = 15;

    public static final TimestampType INSTANCE_WITH_TZ = new TimestampType(
            ID_WITH_TZ,
            "timestamp with time zone",
            TimestampType::parseTimestamp,
            Precedence.TIMESTAMP_WITH_TIME_ZONE);

    public static final TimestampType INSTANCE_WITHOUT_TZ = new TimestampType(
            ID_WITHOUT_TZ,
            "timestamp without time zone",
            TimestampType::parseTimestampIgnoreTimeZone,
            Precedence.TIMESTAMP);

    private final int id;
    private final String name;
    private final Function<String, Long> parse;
    private final Precedence precedence;

    private TimestampType(int id, String name, Function<String, Long> parse, Precedence precedence) {
        this.id = id;
        this.name = name;
        this.parse = parse;
        this.precedence = precedence;
    }

    @Override
    public int id() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Precedence precedence() {
        return precedence;
    }

    @Override
    public Streamer<Long> streamer() {
        return this;
    }

    @Override
    public Long value(Object value) throws ClassCastException {
        if (value == null) {
            return null;
        }
        if (value instanceof String) {
            return parse.apply((String) value);
        }
        // we treat float and double values as seconds with milliseconds as fractions
        // see timestamp documentation
        if (value instanceof Double) {
            return ((Number) (((Double) value) * 1000)).longValue();
        }
        if (value instanceof Float) {
            return ((Number) (((Float) value) * 1000)).longValue();
        }
        if (!(value instanceof Long)) {
            return ((Number) value).longValue();
        }
        return (Long) value;
    }

    @Override
    public int compare(Long val1, Long val2) {
        return Long.compare(val1, val2);
    }

    @Override
    public int fixedSize() {
        return 0;
    }

    @Override
    public Long readValueFrom(InputStream in) throws IOException {
        return null;
    }

    @Override
    public void writeValueTo(OutputStream out) throws IOException {

    }

    static long parseTimestamp(String timestamp) {
        try {
            return Long.parseLong(timestamp);
        } catch (NumberFormatException e) {
            TemporalAccessor dt;
            try {
                dt = TIMESTAMP_PARSER.parseBest(
                        timestamp, OffsetDateTime::from, LocalDateTime::from, LocalDate::from);
            } catch (DateTimeParseException e1) {
                throw new IllegalArgumentException(e1.getMessage());
            }

            if (dt instanceof LocalDateTime) {
                LocalDateTime localDateTime = LocalDateTime.from(dt);
                return localDateTime.toInstant(UTC).toEpochMilli();
            } else if (dt instanceof LocalDate) {
                LocalDate localDate = LocalDate.from(dt);
                return localDate.atStartOfDay(UTC).toInstant().toEpochMilli();
            }

            OffsetDateTime offsetDateTime = OffsetDateTime.from(dt);
            return offsetDateTime.toInstant().toEpochMilli();
        }
    }

    static long parseTimestampIgnoreTimeZone(String timestamp) {
        try {
            return Long.parseLong(timestamp);
        } catch (NumberFormatException e) {
            TemporalAccessor dt;
            try {
                dt = TIMESTAMP_PARSER.parseBest(
                        timestamp, LocalDateTime::from, LocalDate::from);
            } catch (DateTimeParseException e1) {
                throw new IllegalArgumentException(e1.getMessage());
            }

            if (dt instanceof LocalDate) {
                LocalDate localDate = LocalDate.from(dt);
                return localDate.atStartOfDay(UTC).toInstant().toEpochMilli();
            }

            LocalDateTime localDateTime = LocalDateTime.from(dt);
            return localDateTime.toInstant(UTC).toEpochMilli();
        }
    }

    private static final DateTimeFormatter TIMESTAMP_PARSER = new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .append(ISO_LOCAL_DATE)
            .optionalStart()
            .padNext(1)
            .optionalStart()
            .appendLiteral('T')
            .optionalEnd()
            .append(ISO_LOCAL_TIME)
            .optionalStart()
            .appendPattern("[Z][VV][x][xx][xxx]")
            .toFormatter(Locale.ENGLISH).withResolverStyle(ResolverStyle.STRICT);
}

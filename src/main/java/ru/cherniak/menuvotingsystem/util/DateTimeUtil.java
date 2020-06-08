package ru.cherniak.menuvotingsystem.util;

import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import ru.cherniak.menuvotingsystem.util.exception.OutsideTimeException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {

    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm";
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);

    private static LocalTime timeBorder = LocalTime.of(11, 0, 0, 0);

    private static final LocalDate MIN_DATE = LocalDate.of(1, 1, 1);
    private static final LocalDate MAX_DATE = LocalDate.of(3000, 1, 1);

    private DateTimeUtil() {
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }

    public static @Nullable
    LocalDate parseLocalDate(@Nullable String str) {
        return StringUtils.isEmpty(str) ? null : LocalDate.parse(str);
    }

    public static LocalDate getStartDate(LocalDate localDate) {
        return localDate != null ? localDate : MIN_DATE;
    }

    public static LocalDate getEndDate(LocalDate localDate) {
        return localDate != null ? localDate : MAX_DATE;
    }

    public static void checkTimeBorder() {
        if (LocalDateTime.now().toLocalTime().isAfter(timeBorder)) {
            throw new OutsideTimeException("Time is after " + timeBorder + " -  the vote can't be changed");
        }
    }
}


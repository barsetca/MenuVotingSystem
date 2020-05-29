package ru.cherniak.menuvotingsystem.util;

import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import ru.cherniak.menuvotingsystem.util.exception.NotFoundException;
import ru.cherniak.menuvotingsystem.util.exception.OutsideTimeException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private static LocalTime timeBorder = LocalTime.of(11, 0, 0, 0);

    // HSQLDB doesn't support LocalDate.MIN/MAX
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

    public static @Nullable
    LocalTime parseLocalTime(@Nullable String str) {
        return StringUtils.isEmpty(str) ? null : LocalTime.parse(str);
    }

    public static LocalDate getStartDate(LocalDate localDate) {
        return localDate != null ? localDate : MIN_DATE;
    }

    public static LocalDate getEndDate(LocalDate localDate) {
        return localDate != null ? localDate : MAX_DATE;
    }


    public static void checkTimeBorder() {
        if (LocalDateTime.now().toLocalTime().isAfter(timeBorder)) {
            throw new OutsideTimeException("Time is after "+  timeBorder + " -  the vote can't be changed");
        }
    }
}


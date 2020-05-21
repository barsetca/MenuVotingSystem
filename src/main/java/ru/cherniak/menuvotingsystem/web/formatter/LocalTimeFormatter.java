package ru.cherniak.menuvotingsystem.web.formatter;

import org.springframework.format.Formatter;
import ru.cherniak.menuvotingsystem.util.DateTimeUtil;


import java.text.ParseException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LocalTimeFormatter implements Formatter<LocalTime> {
    @Override
    public LocalTime parse(String text, Locale locale) throws ParseException {
        if (text.length() == 0) {
            return null;
        }
        return DateTimeUtil.parseLocalTime(text);
    }

    @Override
    public String print(LocalTime localTime, Locale locale) {
        if (localTime == null) {
            return "";
        }
        return localTime.format(DateTimeFormatter.ISO_LOCAL_TIME);
    }
}

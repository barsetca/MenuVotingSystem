package ru.cherniak.menuvotingsystem.web.formatter;

import org.springframework.format.Formatter;
import ru.cherniak.menuvotingsystem.util.DateTimeUtil;


import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;


public class LocalDateFormatter implements Formatter<LocalDate> {
    @Override
    public LocalDate parse(String text, Locale locale) throws ParseException {
        if (text.length() == 0) {
            return null;
        }
        return DateTimeUtil.parseLocalDate(text);
    }

    @Override
    public String print(LocalDate localDate, Locale locale) {
        if (localDate == null) {
            return "";
        }
        return localDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }
}

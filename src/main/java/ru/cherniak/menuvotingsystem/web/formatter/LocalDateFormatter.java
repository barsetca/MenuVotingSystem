package ru.cherniak.menuvotingsystem.web.formatter;

import org.springframework.format.Formatter;
import ru.cherniak.menuvotingsystem.util.DateTimeUtil;


import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static ru.cherniak.menuvotingsystem.util.DateTimeUtil.parseLocalDate;


public class LocalDateFormatter implements Formatter<LocalDate> {
    @Override
    public LocalDate parse(String text, Locale locale) {
        return parseLocalDate(text);
    }

    @Override
    public String print(LocalDate lt, Locale locale) {
        return lt.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }
}

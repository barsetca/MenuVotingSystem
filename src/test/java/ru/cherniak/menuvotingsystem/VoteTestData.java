package ru.cherniak.menuvotingsystem;


import ru.cherniak.menuvotingsystem.model.Vote;
import ru.cherniak.menuvotingsystem.util.DateTimeUtil;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.cherniak.menuvotingsystem.DishTestData.DATE_290420;
import static ru.cherniak.menuvotingsystem.DishTestData.DATE_300420;
import static ru.cherniak.menuvotingsystem.model.AbstractBase.START_SEQ;


public class VoteTestData {

    public static TestMatcher<Vote> VOTE_MATCHER = TestMatcher.usingFieldsComparator(Vote.class, "user", "restaurant");

    private final static LocalTime TIME_BORDER = LocalTime.of(11, 0, 0, 0);
    private final static LocalTime TIME_BORDER_PLUS = LocalTime.now().withSecond(0).withNano(0).plusMinutes(1);
    private final static LocalTime TIME_BORDER_MINUS = LocalTime.now().withSecond(0).withNano(0).minusMinutes(1);


    public static final long VOTE_ID = START_SEQ + 12;

    public static final Vote VOTE_1 = new Vote(VOTE_ID, DATE_290420);
    public static final Vote VOTE_2 = new Vote(VOTE_ID + 1, DATE_290420);
    public static final Vote VOTE_3 = new Vote(VOTE_ID + 2, DATE_300420);

    public static final List<Vote> ALL_VOTES = List.of(VOTE_3, VOTE_1, VOTE_2);

    public static final List<Vote> USER_ID_VOTES = List.of(VOTE_3, VOTE_1);


    public static Vote getCreatedToday() {
        return new Vote(LocalDate.now());
    }

    public static Vote getUpdated() {
        return new Vote(VOTE_2.getDate());
    }


    public static void timeBorderPlus() throws Exception {
        Field field = getTimeBorder();
        field.setAccessible(true);
        field.set(LocalDate.class, TIME_BORDER_PLUS);
        field.setAccessible(false);
    }

    public static void timeBorderMinus() throws Exception {
        Field field = getTimeBorder();
        field.setAccessible(true);
        field.set(LocalDate.class, TIME_BORDER_MINUS);
        field.setAccessible(false);
    }

    public static void timeBorderFix() throws Exception {
        Field field = getTimeBorder();
        field.setAccessible(true);
        field.set(LocalDate.class, TIME_BORDER);
        field.setAccessible(false);
    }

    private static Field getTimeBorder() throws NoSuchFieldException {
        Class clazz = DateTimeUtil.class;
        return clazz.getDeclaredField("timeBorder");
    }


}

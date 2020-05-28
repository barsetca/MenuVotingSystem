package ru.cherniak.menuvotingsystem;


import org.springframework.test.web.servlet.ResultMatcher;
import ru.cherniak.menuvotingsystem.model.Vote;
import ru.cherniak.menuvotingsystem.util.DateTimeUtil;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.cherniak.menuvotingsystem.DishTestData.DATE_300520;
import static ru.cherniak.menuvotingsystem.DishTestData.DATE_310520;
import static ru.cherniak.menuvotingsystem.model.AbstractBase.START_SEQ;
import static ru.cherniak.menuvotingsystem.web.TestUtil.readFromJsonMvcResult;
import static ru.cherniak.menuvotingsystem.web.TestUtil.readListFromJsonMvcResult;


public class VoteTestData {
    private final static LocalTime TIME_BORDER = LocalTime.of(11, 0, 0, 0);
    private final static LocalTime TIME_BORDER_PLUS = LocalTime.now().plusHours(1);
    private final static LocalTime TIME_BORDER_MINUS = LocalTime.now().minusMinutes(1);


    public static final long VOTE_ID = START_SEQ + 12;

    public static final Vote VOTE_1 = new Vote(VOTE_ID, DATE_300520);
    public static final Vote VOTE_2 = new Vote(VOTE_ID + 1, DATE_300520);
    public static final Vote VOTE_3 = new Vote(VOTE_ID + 2, DATE_310520);

    public static final List<Vote> ALL_VOTES = List.of(VOTE_3, VOTE_1, VOTE_2);

    public static final List<Vote> USER_ID_VOTES = List.of(VOTE_3, VOTE_1);


    public static Vote getCreatedToday() {
        return new Vote(LocalDate.now());
    }

    public static Vote getUpdated() {
        return new Vote(VOTE_2.getDate());
    }


    public static void assertMatch(Vote actual, Vote expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "user", "restaurant");
    }


    public static void assertMatch(Iterable<Vote> actual, Vote... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Vote> actual, Iterable<Vote> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("restaurant", "user").isEqualTo(expected);
    }

    public static ResultMatcher contentJson(Vote... expected) {
        return result -> assertMatch(readListFromJsonMvcResult(result, Vote.class), List.of(expected));
    }

    public static ResultMatcher contentJson(Vote expected) {
        return result -> assertMatch(readFromJsonMvcResult(result, Vote.class), expected);
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

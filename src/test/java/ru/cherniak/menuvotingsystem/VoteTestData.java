package ru.cherniak.menuvotingsystem;


import ru.cherniak.menuvotingsystem.model.Dish;
import ru.cherniak.menuvotingsystem.model.Restaurant;
import ru.cherniak.menuvotingsystem.model.Vote;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.cherniak.menuvotingsystem.DishTestData.DATE_300520;
import static ru.cherniak.menuvotingsystem.DishTestData.DATE_310520;
import static ru.cherniak.menuvotingsystem.model.AbstractBase.START_SEQ;


public class VoteTestData {
    public static final long VOTE_ID = START_SEQ + 12;


    public static final Vote VOTE_1 = new Vote(VOTE_ID, DATE_300520);
    public static final Vote VOTE_2 = new Vote(VOTE_ID+1, DATE_300520);
    public static final Vote VOTE_3 = new Vote(VOTE_ID+2, DATE_310520);
    //public static final Vote VOTE_4 = new Vote(VOTE_ID+3, DATE_310520);

    public static final List<Vote> ALL_VOTES = List.of(VOTE_3, VOTE_1, VOTE_2);

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
}

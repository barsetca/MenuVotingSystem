package ru.cherniak.menuvotingsystem;


import ru.cherniak.menuvotingsystem.model.Restaurant;
import ru.cherniak.menuvotingsystem.model.Role;
import ru.cherniak.menuvotingsystem.model.User;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.cherniak.menuvotingsystem.model.AbstractBase.START_SEQ;


public class RestaurantTestData {
    public static final long RESTAURANT1_ID = START_SEQ + 2;
    public static final long RESTAURANT2_ID = START_SEQ + 3;

    public static final Restaurant RESTAURANT1 = new Restaurant(RESTAURANT1_ID, "McDonalds", "пл. Ленина, д.1", "315-25-25");
    public static final Restaurant RESTAURANT2 = new Restaurant(RESTAURANT2_ID, "Pizza Hut", "пл. Стачек, д.1", "374-52-52", "pizza-hut.spb.ru");

    public static void assertMatch(Restaurant actual, Restaurant expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "registered", "dishes", "votes");
    }

    public static void assertMatch(Iterable<Restaurant> actual, Restaurant... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Restaurant> actual, Iterable<Restaurant> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("registered", "dishes", "votes").isEqualTo(expected);
    }
}

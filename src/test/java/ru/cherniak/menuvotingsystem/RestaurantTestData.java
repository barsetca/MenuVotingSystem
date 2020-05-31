package ru.cherniak.menuvotingsystem;


import org.springframework.test.web.servlet.ResultMatcher;
import ru.cherniak.menuvotingsystem.model.Restaurant;
import ru.cherniak.menuvotingsystem.to.RestaurantTo;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.cherniak.menuvotingsystem.model.AbstractBase.START_SEQ;
import static ru.cherniak.menuvotingsystem.web.TestUtil.readFromJsonMvcResult;
import static ru.cherniak.menuvotingsystem.web.TestUtil.readListFromJsonMvcResult;

public class RestaurantTestData {
    public static final long RESTAURANT1_ID = START_SEQ + 2;
    public static final long RESTAURANT2_ID = START_SEQ + 3;

    public static final Restaurant RESTAURANT1 = new Restaurant(RESTAURANT1_ID, "McDonalds", "Фастфуд", "пл. Ленина, д.1", "315-25-25", "McDonalds.ru");
    public static final Restaurant RESTAURANT2 = new Restaurant(RESTAURANT2_ID, "Pizza_Hut", "Итальянский", "пл. Стачек, д.1", "374-52-52", "pizzahut.ru");

    public static void assertMatch(Restaurant actual, Restaurant expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "registered", "dishes", "votes");
    }

    public static void assertMatch(Iterable<Restaurant> actual, Restaurant... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Restaurant> actual, Iterable<Restaurant> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("registered", "dishes", "votes").isEqualTo(expected);
    }

    public static ResultMatcher contentJson(Restaurant... expected) {
        return result -> assertMatch(readListFromJsonMvcResult(result, Restaurant.class), List.of(expected));
    }

    public static ResultMatcher contentJson(Restaurant expected) {
        return result -> assertMatch(readFromJsonMvcResult(result, Restaurant.class), expected);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static void assertMatchTo(RestaurantTo actual, RestaurantTo expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected);
    }

    public static void assertMatchTo(Iterable<RestaurantTo> actual, RestaurantTo... expected) {
        assertMatchTo(actual, Arrays.asList(expected));
    }

    public static void assertMatchTo(Iterable<RestaurantTo> actual, Iterable<RestaurantTo> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields().isEqualTo(expected);
    }

    public static ResultMatcher contentJsonTo(RestaurantTo... expected) {
        return result -> assertMatchTo(readListFromJsonMvcResult(result, RestaurantTo.class), List.of(expected));
    }

    public static ResultMatcher contentJsonTo(RestaurantTo expected) {
        return result -> assertMatchTo(readFromJsonMvcResult(result, RestaurantTo.class), expected);
    }

}

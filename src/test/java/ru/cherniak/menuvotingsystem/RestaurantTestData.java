package ru.cherniak.menuvotingsystem;


import org.springframework.test.web.servlet.ResultMatcher;
import ru.cherniak.menuvotingsystem.model.Restaurant;
import ru.cherniak.menuvotingsystem.model.Vote;
import ru.cherniak.menuvotingsystem.to.RestaurantTo;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.cherniak.menuvotingsystem.model.AbstractBase.START_SEQ;
import static ru.cherniak.menuvotingsystem.TestUtil.readFromJsonMvcResult;
import static ru.cherniak.menuvotingsystem.TestUtil.readListFromJsonMvcResult;

public class RestaurantTestData {
    public static TestMatcher<Restaurant> RESTAURANT_MATCHER = TestMatcher.usingFieldsComparator(Restaurant.class, "registered", "dishes", "votes");
    public static TestMatcher<RestaurantTo> RES_TO_MATCHER = TestMatcher.usingFieldsComparator(RestaurantTo.class);

    public static final long RESTAURANT1_ID = START_SEQ + 2;
    public static final long RESTAURANT2_ID = START_SEQ + 3;

    public static final Restaurant RESTAURANT1 = new Restaurant(RESTAURANT1_ID, "McDonalds", "Фастфуд", "пл. Ленина, д.1", "315-25-25", "McDonalds.ru");
    public static final Restaurant RESTAURANT2 = new Restaurant(RESTAURANT2_ID, "Pizza_Hut", "Итальянский", "пл. Стачек, д.1", "374-52-52", "pizzahut.ru");

    }

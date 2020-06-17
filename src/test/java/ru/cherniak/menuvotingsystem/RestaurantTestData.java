package ru.cherniak.menuvotingsystem;


import ru.cherniak.menuvotingsystem.model.Restaurant;
import ru.cherniak.menuvotingsystem.to.RestaurantTo;

import static ru.cherniak.menuvotingsystem.model.AbstractBase.START_SEQ;

public class RestaurantTestData {
    public static TestMatcher<Restaurant> RESTAURANT_MATCHER = TestMatcher.usingFieldsComparator(Restaurant.class, "registered", "dishes", "votes");
    public static TestMatcher<RestaurantTo> RES_TO_MATCHER = TestMatcher.usingEquals(RestaurantTo.class);

    public static final long RESTAURANT1_ID = START_SEQ + 2;
    public static final long RESTAURANT2_ID = START_SEQ + 3;

    public static final Restaurant RESTAURANT1 = new Restaurant(RESTAURANT1_ID, "McDonalds", "пл. Ленина, д.1", "315-25-25", "McDonalds.ru");
    public static final Restaurant RESTAURANT2 = new Restaurant(RESTAURANT2_ID, "Pizza_Hut", "пл. Стачек, д.1", "374-52-52", "pizzahut.ru");

}

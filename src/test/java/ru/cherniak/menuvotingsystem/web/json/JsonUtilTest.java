package ru.cherniak.menuvotingsystem.web.json;

import org.junit.jupiter.api.Test;
import ru.cherniak.menuvotingsystem.RestaurantTestData;
import ru.cherniak.menuvotingsystem.model.Dish;
import ru.cherniak.menuvotingsystem.model.Restaurant;

import java.util.List;

import static ru.cherniak.menuvotingsystem.DishTestData.ALL_DISHES;
import static ru.cherniak.menuvotingsystem.DishTestData.DISH_MATCHER;


public class JsonUtilTest {
    @Test
    void readWriteValue() {
        Restaurant newRestaurant = new Restaurant("CreateRest","Cafe", "пл. Новая, д.1", "315-00-00");
        String json = JsonUtil.writeValue(newRestaurant);
        System.out.println(json);
        Restaurant restaurant = JsonUtil.readValue(json, Restaurant.class);
        RestaurantTestData.RESTAURANT_MATCHER.assertMatch(restaurant, newRestaurant);
    }

    @Test
    void readWriteValues() {
        String json = JsonUtil.writeValue(ALL_DISHES);
        System.out.println(json);
        List<Dish> dishes = JsonUtil.readValues(json, Dish.class);
        DISH_MATCHER.assertMatch(dishes, ALL_DISHES);
    }
}

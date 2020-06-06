package ru.cherniak.menuvotingsystem.web.json;

import org.junit.jupiter.api.Test;
import ru.cherniak.menuvotingsystem.RestaurantTestData;
import ru.cherniak.menuvotingsystem.UserTestData;
import ru.cherniak.menuvotingsystem.model.Dish;
import ru.cherniak.menuvotingsystem.model.Restaurant;
import ru.cherniak.menuvotingsystem.model.User;

import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @Test
    void writeOnlyAccess() { //где используется?
        String json = JsonUtil.writeValue(UserTestData.USER);
        System.out.println(json);
        assertThat(json, not(containsString("password")));
        String jsonWithPass = UserTestData.jsonWithPassword(UserTestData.USER, "newPass");
        System.out.println(jsonWithPass);
        User user = JsonUtil.readValue(jsonWithPass, User.class);
        assertEquals(user.getPassword(), "newPass");
    }
}

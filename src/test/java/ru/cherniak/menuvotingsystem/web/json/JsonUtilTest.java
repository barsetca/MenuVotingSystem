package ru.cherniak.menuvotingsystem.web.json;

import org.junit.jupiter.api.Test;
import ru.cherniak.menuvotingsystem.DishTestData;
import ru.cherniak.menuvotingsystem.model.Dish;

import java.util.List;

import static ru.cherniak.menuvotingsystem.DishTestData.*;

public class JsonUtilTest {
    @Test
    void readWriteValue() throws Exception {
        String json = JsonUtil.writeValue(DISH_1);
        System.out.println(json);
        Dish dish = JsonUtil.readValue(json, Dish.class);
        assertMatch(dish, DISH_1 );
    }

    @Test
    void readWriteValues() throws Exception {
        String json = JsonUtil.writeValue(ALL_DISHES);
        System.out.println(json);
        List<Dish> dishes = JsonUtil.readValues(json, Dish.class);
        assertMatch(dishes, ALL_DISHES );
    }
}

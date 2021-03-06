package ru.cherniak.menuvotingsystem.web.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.cherniak.menuvotingsystem.model.Dish;
import ru.cherniak.menuvotingsystem.service.DishService;
import ru.cherniak.menuvotingsystem.web.AbstractControllerTest;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.cherniak.menuvotingsystem.DishTestData.DISH_MATCHER;
import static ru.cherniak.menuvotingsystem.DishTestData.getCreatedToday;
import static ru.cherniak.menuvotingsystem.RestaurantTestData.RESTAURANT1_ID;
import static ru.cherniak.menuvotingsystem.TestUtil.userHttpBasic;
import static ru.cherniak.menuvotingsystem.UserTestData.USER;

class UserDishRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = "/rest/user/restaurants/" + RESTAURANT1_ID + "/dishes" + '/';

    @Autowired
    DishService dishService;

    @Test
    void getTodayMenuByRestaurant() throws Exception {
        Dish today1 = dishService.create(getCreatedToday(), RESTAURANT1_ID);
        Dish newDish = getCreatedToday();
        newDish.setName("Второе Имя");
        Dish today2 = dishService.create(newDish, RESTAURANT1_ID);
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(USER)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(today1, today2));
    }

    @Test
    void getUnAuth() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }
}
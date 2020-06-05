package ru.cherniak.menuvotingsystem.web.dish;

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
import static ru.cherniak.menuvotingsystem.DishTestData.*;
import static ru.cherniak.menuvotingsystem.RestaurantTestData.RESTAURANT1_ID;
import static ru.cherniak.menuvotingsystem.UserTestData.USER;
import static ru.cherniak.menuvotingsystem.TestUtil.userHttpBasic;

class UserDishRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = UserDishRestController.REST_USER_DISHES + '/';

    @Autowired
    DishService dishService;

    @Test
    void getTodayMenu() throws Exception {
        Dish today1 = dishService.create(getCreatedToday(), RESTAURANT1_ID);
        Dish newDish = getCreatedToday();
        newDish.setName("Второе Имя");
        Dish today2 = dishService.create(newDish, RESTAURANT1_ID);
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "by?restaurantId=" + RESTAURANT1_ID)
                .with(userHttpBasic(USER)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(today2, today1));
    }

    @Test
    void getUnAuth() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "by?restaurantId=" + RESTAURANT1_ID))
                .andExpect(status().isUnauthorized());
    }
}
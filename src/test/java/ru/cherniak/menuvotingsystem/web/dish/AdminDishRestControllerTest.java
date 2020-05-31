package ru.cherniak.menuvotingsystem.web.dish;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.cherniak.menuvotingsystem.DishTestData;
import ru.cherniak.menuvotingsystem.model.Dish;
import ru.cherniak.menuvotingsystem.service.DishService;
import ru.cherniak.menuvotingsystem.web.AbstractControllerTest;
import ru.cherniak.menuvotingsystem.web.TestUtil;
import ru.cherniak.menuvotingsystem.web.json.JsonUtil;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.cherniak.menuvotingsystem.DishTestData.*;
import static ru.cherniak.menuvotingsystem.RestaurantTestData.RESTAURANT1_ID;

class AdminDishRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = "/" + AdminDishRestController.REST_ADMIN_DISHES + '/';

    @Autowired
    DishService dishService;

    @Test
    void createWithLocation() throws Exception {
        Dish newDish = dishService.create(DishTestData.getCreatedToday(), RESTAURANT1_ID);
        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.post(REST_URL + "/by")
                .param("restaurantId", "100002")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newDish)))
                .andExpect(status().isCreated());

        Dish created = TestUtil.readFromJson(action, Dish.class);
        Long newID = created.getId();
        newDish.setId(newID);

        DishTestData.assertMatch(created, newDish);
        DishTestData.assertMatch(dishService.get(newID), newDish);
    }

    @Test
    void update() throws Exception {
        Dish updated = DishTestData.getUpdated(DISH_1);
        mockMvc.perform(MockMvcRequestBuilders.put(REST_URL + "/by")
                .param("restaurantId", "100002")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());
        DishTestData.assertMatch(dishService.get(DISH_ID), updated);
    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(REST_URL + DISH_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        DishTestData.assertMatch(dishService.getDayMenu(DATE_290420, RESTAURANT1_ID), DISH_2);
    }

    @Test
    void get() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + DISH_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(DISH_1));
    }

    @Test
    void getAllWithRestaurant() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DishTestData.contentJson(DISH_5, DISH_6, DISH_8, DISH_7, DISH_1, DISH_2, DISH_4, DISH_3));
    }

    @Test
    void getAllByRestaurant() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "/by")
                .param("restaurantId", "100003"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(DISH_8, DISH_7, DISH_4, DISH_3));
    }

    @Test
    void getAllByRestaurantBetweenDatesInclusive() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "filter")
                .param("startDate", "2020-04-29")
                .param("endDate", "2020-04-30")
                .param("restaurantId", "100002"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DishTestData.contentJson(DISH_5, DISH_6, DISH_1, DISH_2));
    }
}

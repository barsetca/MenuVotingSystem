package ru.cherniak.menuvotingsystem.web.restaurant;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.cherniak.menuvotingsystem.DishTestData;
import ru.cherniak.menuvotingsystem.service.DishService;
import ru.cherniak.menuvotingsystem.service.RestaurantService;
import ru.cherniak.menuvotingsystem.to.RestaurantTo;
import ru.cherniak.menuvotingsystem.util.RestaurantUtil;
import ru.cherniak.menuvotingsystem.web.AbstractControllerTest;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.cherniak.menuvotingsystem.RestaurantTestData.*;
import static ru.cherniak.menuvotingsystem.TestUtil.userHttpBasic;
import static ru.cherniak.menuvotingsystem.UserTestData.ADMIN;
import static ru.cherniak.menuvotingsystem.UserTestData.USER;

class UserRestaurantRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = UserRestaurantRestController.REST_USER_RESTAURANTS + '/';

    @Autowired
    RestaurantService restaurantService;

    @Autowired
    DishService dishService;

    @Test
    void getWithCountVotes() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT1_ID)
                .with(userHttpBasic(USER)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RES_TO_MATCHER.contentJson(RestaurantUtil.createTo(restaurantService.getWithVotes(RESTAURANT1_ID))));
    }

    @Test
    void getWithCountVotesNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + 1)
                .with(userHttpBasic(USER)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }

    @Test
    void getUnAuth() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT1_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getByNameWithCountVotes() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "byName?name=" + RESTAURANT1.getName())
                .with(userHttpBasic(USER)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RES_TO_MATCHER.contentJson(RestaurantUtil.createTo(restaurantService.getByNameWithVotes(RESTAURANT1.getName()))));
    }

    @Test
    void getByNameWithCountVotesNotFount() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "byName?name=" + "unknown")
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }

    @Test
    void getAllWithCountVotes() throws Exception {
        List<RestaurantTo> restaurantTos = RestaurantUtil.getRestaurantTosSortedByCountVotes(restaurantService.getAllWithVotes());
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(USER)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RES_TO_MATCHER.contentJson(restaurantTos.get(0), restaurantTos.get(1)));
    }

    @Test
    void getAllWithTodayMenu() throws Exception {
        DishTestData.getCreatedToday();
        dishService.create(DishTestData.getCreatedToday(), RESTAURANT1_ID);
        //dishService.create(DishTestData.getCreatedToday(), RESTAURANT2_ID);
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL+ "dishes/today")
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(List.of(RESTAURANT1)));
    }
}
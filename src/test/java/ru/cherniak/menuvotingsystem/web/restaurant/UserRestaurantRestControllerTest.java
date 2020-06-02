package ru.cherniak.menuvotingsystem.web.restaurant;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.cherniak.menuvotingsystem.service.RestaurantService;
import ru.cherniak.menuvotingsystem.to.RestaurantTo;
import ru.cherniak.menuvotingsystem.util.RestaurantUtil;
import ru.cherniak.menuvotingsystem.web.AbstractControllerTest;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.cherniak.menuvotingsystem.RestaurantTestData.*;
import static ru.cherniak.menuvotingsystem.UserTestData.USER;
import static ru.cherniak.menuvotingsystem.web.TestUtil.userHttpBasic;

class UserRestaurantRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = UserRestaurantRestController.REST_USER_RESTAURANTS + '/';

    @Autowired
    RestaurantService restaurantService;

    @Test
    void getWithVotes() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT1_ID)
                .with(userHttpBasic(USER)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJsonTo(RestaurantUtil.createTo(restaurantService.getWithVotes(RESTAURANT1_ID))));
    }

    @Test
    void getUnAuth() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT1_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getByNameWithVotes() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "by?name=" + RESTAURANT1.getName())
                .with(userHttpBasic(USER)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJsonTo(RestaurantUtil.createTo(restaurantService.getByNameWithVotes(RESTAURANT1.getName()))));
    }

    @Test
    void getAllWithVotes() throws Exception {
        List<RestaurantTo> restaurantTos = RestaurantUtil.getRestaurantTosSortedByCountVotes(restaurantService.getAllWithVotes());
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(USER)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJsonTo(restaurantTos.get(0), restaurantTos.get(1)));
    }
}
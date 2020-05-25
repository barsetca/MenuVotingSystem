package ru.cherniak.menuvotingsystem.web.restaurant;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.cherniak.menuvotingsystem.RestaurantTestData;
import ru.cherniak.menuvotingsystem.model.Restaurant;
import ru.cherniak.menuvotingsystem.service.RestaurantService;
import ru.cherniak.menuvotingsystem.web.AbstractControllerTest;
import ru.cherniak.menuvotingsystem.web.TestUtil;
import ru.cherniak.menuvotingsystem.web.json.JsonUtil;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.cherniak.menuvotingsystem.RestaurantTestData.*;



class AdminRestaurantRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = "/" + AdminRestaurantRestController.REST_ADMIN_RESTAURANTS + '/';

    @Autowired
    RestaurantService restaurantService;

    @Test
    void get() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT1_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(RESTAURANT1));
    }

    @Test
    void createWithLocation() throws Exception {
        Restaurant newRestaurant = new Restaurant("CreateRest", "пл. Новая, д.1", "315-00-00");
        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newRestaurant)))
                .andExpect(status().isCreated());

        Restaurant created = TestUtil.readFromJson(action, Restaurant.class);
        Long newID = created.getId();
        newRestaurant.setId(newID);

        RestaurantTestData.assertMatch(created, newRestaurant);
        RestaurantTestData.assertMatch(restaurantService.get(newID), newRestaurant);
    }

    @Test
    void update() throws Exception {
        Restaurant updated = new Restaurant(RESTAURANT2);
        updated.setName("updateName");
        mockMvc.perform(MockMvcRequestBuilders.put(REST_URL + RESTAURANT2_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());
        assertMatch(restaurantService.get(RESTAURANT2_ID), updated);
    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(REST_URL + RESTAURANT1_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertMatch(restaurantService.getAll(), RESTAURANT2);
    }

    @Test
    void getByName() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "by?name=" + RESTAURANT1.getName()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(RESTAURANT1));
    }

    @Test
    void getAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(RESTAURANT1, RESTAURANT2));
    }
}
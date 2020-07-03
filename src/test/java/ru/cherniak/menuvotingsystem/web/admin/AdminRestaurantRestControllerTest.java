package ru.cherniak.menuvotingsystem.web.admin;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.cherniak.menuvotingsystem.DishTestData;
import ru.cherniak.menuvotingsystem.TestUtil;
import ru.cherniak.menuvotingsystem.model.Dish;
import ru.cherniak.menuvotingsystem.model.Restaurant;
import ru.cherniak.menuvotingsystem.service.DishService;
import ru.cherniak.menuvotingsystem.service.RestaurantService;
import ru.cherniak.menuvotingsystem.web.AbstractControllerTest;
import ru.cherniak.menuvotingsystem.web.admin.AdminRestaurantRestController;
import ru.cherniak.menuvotingsystem.web.json.JsonUtil;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.cherniak.menuvotingsystem.RestaurantTestData.*;
import static ru.cherniak.menuvotingsystem.TestUtil.userHttpBasic;
import static ru.cherniak.menuvotingsystem.UserTestData.ADMIN;
import static ru.cherniak.menuvotingsystem.UserTestData.USER;


class AdminRestaurantRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = AdminRestaurantRestController.REST_ADMIN_RESTAURANTS + '/';


    @Autowired
    RestaurantService restaurantService;

    @Autowired
    private DishService dishService;


    @Test
    void getUnAuth() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT1_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getForbidden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT1_ID)
                .with(userHttpBasic(USER)))
                .andExpect(status().isForbidden());
    }

    @Test
    void createWithLocation() throws Exception {
        Restaurant newRestaurant = getCreated();
        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newRestaurant)))
                .andExpect(status().isCreated());

        Restaurant created = TestUtil.readFromJson(action, Restaurant.class);
        Long newID = created.getId();
        newRestaurant.setId(newID);

        RESTAURANT_MATCHER.assertMatch(created, newRestaurant);
        RESTAURANT_MATCHER.assertMatch(restaurantService.get(newID), newRestaurant);
    }

    @Transactional(propagation = Propagation.NEVER)
    @Test
    void createValidationError() throws Exception {
        Restaurant newRestaurant = getCreated();
        newRestaurant.setName("R");
        newRestaurant.setAddress("Adrs");
        newRestaurant.setPhone("123");
//        newRestaurant.setName(" ");
//        newRestaurant.setAddress(" ");
//        newRestaurant.setPhone(" ");
        mockMvc.perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newRestaurant)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }

    @Transactional(propagation = Propagation.NEVER)
    @Test
    void createDuplicateName() throws Exception {
        Restaurant duplicateNameRestaurant = new Restaurant("McDonalds", "пл. Новая, д.1", "315-00-00");
        mockMvc.perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(duplicateNameRestaurant)))
                .andExpect(status().isConflict())
                .andDo(print());
    }

    @Test
    void update() throws Exception {
        Restaurant updated = new Restaurant(RESTAURANT2);
        updated.setName("updateName");

        mockMvc.perform(MockMvcRequestBuilders.put(REST_URL + RESTAURANT2_ID)
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());

        RESTAURANT_MATCHER.assertMatch(restaurantService.get(RESTAURANT2_ID), updated);
    }

    @Test
    void updateValidationError() throws Exception {
        Restaurant updated = new Restaurant(RESTAURANT2);
        updated.setName("U");
        updated.setAddress("Adrs");
        updated.setPhone("123");
//        updated.setName(" ");
//        updated.setAddress(" ");
//        updated.setPhone(" ");
        mockMvc.perform(MockMvcRequestBuilders.put(REST_URL + RESTAURANT2_ID)
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }

    @Transactional(propagation = Propagation.NEVER)
    @Test
    void updateDuplicateName() throws Exception {
        Restaurant updated = new Restaurant(RESTAURANT2);
        updated.setName(RESTAURANT1.getName());
        mockMvc.perform(MockMvcRequestBuilders.put(REST_URL + RESTAURANT2_ID)
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isConflict())
                .andDo(print());
    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(REST_URL + RESTAURANT1_ID)
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isNoContent());

        RESTAURANT_MATCHER.assertMatch(restaurantService.getAll(), RESTAURANT2);
    }

    @Test
    void getAllWithTodayMenu() throws Exception {

        dishService.create(new Dish("Uno", 100), RESTAURANT1_ID);

        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "dishes")
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(List.of(RESTAURANT1)));
    }
}
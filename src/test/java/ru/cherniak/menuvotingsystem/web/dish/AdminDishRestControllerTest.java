package ru.cherniak.menuvotingsystem.web.dish;

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
import ru.cherniak.menuvotingsystem.service.DishService;
import ru.cherniak.menuvotingsystem.web.AbstractControllerTest;
import ru.cherniak.menuvotingsystem.web.json.JsonUtil;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.cherniak.menuvotingsystem.DishTestData.*;
import static ru.cherniak.menuvotingsystem.RestaurantTestData.RESTAURANT1_ID;
import static ru.cherniak.menuvotingsystem.TestUtil.userHttpBasic;
import static ru.cherniak.menuvotingsystem.UserTestData.ADMIN;
import static ru.cherniak.menuvotingsystem.UserTestData.USER;

class AdminDishRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = "/rest/admin/restaurants/" + RESTAURANT1_ID + "/dishes" + '/';

    @Autowired
    DishService dishService;

    @Test
    void createWithLocation() throws Exception {
        Dish newDish = getCreatedToday();
        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newDish)))
                .andExpect(status().isCreated());

        Dish created = TestUtil.readFromJson(action, Dish.class);
        Long newID = created.getId();
        newDish.setId(newID);

        DISH_MATCHER.assertMatch(created, newDish);
        DISH_MATCHER.assertMatch(dishService.get(newID, RESTAURANT1_ID), newDish);
    }

    @Transactional(propagation = Propagation.NEVER)
    @Test
    void createValidationError() throws Exception {
        Dish newDish = getCreatedToday();
        newDish.setName("R");
        newDish.setPrice(-1);
        newDish.setDate(LocalDate.now().minusDays(1));
//        newDish.setName(" ");
        mockMvc.perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newDish)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }

    @Test
    void createNotOwner() throws Exception {
        Dish newDish = getCreatedToday();
        mockMvc.perform(MockMvcRequestBuilders.post("/rest/admin/restaurants/" + 1 + "/dishes")
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newDish)))
                .andExpect(status().isConflict());
    }

    @Transactional(propagation = Propagation.NEVER)
    @Test
    void createDuplicateNameOneDateByRestaurant() throws Exception {
        dishService.create(new Dish("NewName", LocalDate.now(), 100), RESTAURANT1_ID);
        Dish newDish = new Dish("NewName", LocalDate.now(), 1000);
        mockMvc.perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newDish)))
                .andExpect(status().isConflict())
                .andDo(print());
    }

    @Test
    void update() throws Exception {
        Dish updated = dishService.create(getCreatedToday(), RESTAURANT1_ID);
        updated.setName("UpdateName");
        mockMvc.perform(MockMvcRequestBuilders.put(REST_URL)
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());

        DISH_MATCHER.assertMatch(dishService.get(updated.getId(), RESTAURANT1_ID), updated);
    }

    @Test
    void updateNotOwner() throws Exception {
        Dish updated = dishService.create(getCreatedToday(), RESTAURANT1_ID);
        updated.setName("UpdateName");
        mockMvc.perform(MockMvcRequestBuilders.put("/rest/admin/restaurants/" + 1 + "/dishes")
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }

    @Test
    void updateValidationError() throws Exception {
        Dish updated = dishService.create(getCreatedToday(), RESTAURANT1_ID);
        updated.setName("R");
        updated.setPrice(-1);
        updated.setDate(LocalDate.now().minusDays(1));
//        newDish.setName(" ");
        mockMvc.perform(MockMvcRequestBuilders.put(REST_URL)
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }

    @Transactional(propagation = Propagation.NEVER)
    @Test
    void updateDuplicateNameOneDateByRestaurant() throws Exception {
        dishService.create(new Dish("FirstName", LocalDate.now(), 100), RESTAURANT1_ID);
        Dish second = dishService.create(new Dish("SecondNewName", LocalDate.now(), 1000), RESTAURANT1_ID);
        second.setName("FirstName");
        mockMvc.perform(MockMvcRequestBuilders.put(REST_URL)
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(second)))
                .andExpect(status().isConflict())
                .andDo(print());
    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(REST_URL + DISH_ID)
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isNoContent());
        DISH_MATCHER.assertMatch(dishService.getDayMenu(DATE_290420, RESTAURANT1_ID), DISH_2);
    }

    @Test
    void get() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + DISH_ID)
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(DISH_1));
    }

    @Test
    void getNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + 1)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }

    @Test
    void getUnAuth() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + DISH_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getForbidden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + DISH_ID)
                .with(userHttpBasic(USER)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getAllByRestaurant() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(DISH_5, DISH_6, DISH_1, DISH_2));
    }

    @Test
    void getAllByRestaurantBetweenDatesInclusive() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "filter")
                .param("startDate", "2020-04-29")
                .param("endDate", "2020-04-30")
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(DISH_5, DISH_6, DISH_1, DISH_2));
    }
}

package ru.cherniak.menuvotingsystem.web.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.cherniak.menuvotingsystem.model.User;
import ru.cherniak.menuvotingsystem.service.UserService;
import ru.cherniak.menuvotingsystem.to.UserTo;
import ru.cherniak.menuvotingsystem.util.UserUtil;
import ru.cherniak.menuvotingsystem.web.AbstractControllerTest;
import ru.cherniak.menuvotingsystem.TestUtil;
import ru.cherniak.menuvotingsystem.web.json.JsonUtil;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.cherniak.menuvotingsystem.UserTestData.*;
import static ru.cherniak.menuvotingsystem.TestUtil.userHttpBasic;
import static ru.cherniak.menuvotingsystem.web.user.ProfileUserRestController.REST_URL;

class ProfileUserRestControllerTest extends AbstractControllerTest {

    @Autowired
    private UserService userService;


    @Test
    void get() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(USER)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(USER));
    }

    @Test
    void getUnAuth() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void update() throws Exception {
        UserTo updated = UserUtil.asTo(USER);
        updated.setName("UpdatedName");

        mockMvc.perform(MockMvcRequestBuilders.put(REST_URL)
                .with(userHttpBasic(USER))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());
        USER_MATCHER.assertMatch(userService.get(USER_ID), UserUtil.updateFromTo(new User(USER), updated));
    }

    @Test
    void updateValidationError() throws Exception {
        UserTo updated = UserUtil.asTo(USER);
        updated.setName("U");
        updated.setEmail("U");
        updated.setPassword("U");
        mockMvc.perform(MockMvcRequestBuilders.put(REST_URL)
                .with(userHttpBasic(USER))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
           }



    @Test
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(REST_URL)
                .with(userHttpBasic(USER)))
                .andExpect(status().isNoContent());

        USER_MATCHER.assertMatch(userService.getAll(), ADMIN);
    }

    @Test
    void register()  throws Exception {
        UserTo newUserTo = new UserTo(null, "Create", "create@user.ru", "createPass");

        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.post(REST_URL + "/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newUserTo)))
                .andExpect(status().isCreated());
        User created = TestUtil.readFromJson(action, User.class);
        Long newID = created.getId();

        USER_MATCHER.assertMatch(userService.get(newID), created);
    }

    @Test
    void registerValidationError()  throws Exception {
        UserTo newUserTo = new UserTo(null, "C", "c", "c");

        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.post(REST_URL + "/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newUserTo)))
                .andExpect(status().isUnprocessableEntity());

    }
}
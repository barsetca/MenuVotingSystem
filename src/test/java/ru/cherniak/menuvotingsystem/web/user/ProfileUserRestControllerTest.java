package ru.cherniak.menuvotingsystem.web.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.Assert;
import ru.cherniak.menuvotingsystem.model.User;
import ru.cherniak.menuvotingsystem.service.UserService;
import ru.cherniak.menuvotingsystem.to.UserTo;
import ru.cherniak.menuvotingsystem.util.UserUtil;
import ru.cherniak.menuvotingsystem.web.AbstractControllerTest;
import ru.cherniak.menuvotingsystem.web.json.JsonUtil;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.cherniak.menuvotingsystem.UserTestData.*;
import static ru.cherniak.menuvotingsystem.web.TestUtil.userHttpBasic;
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
                .andExpect(contentJson(USER));
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
        assertMatch(userService.get(USER_ID), UserUtil.updateFromTo(new User(USER), updated));
    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(REST_URL)
                .with(userHttpBasic(USER)))
                .andExpect(status().isNoContent());
        assertMatch(userService.getAll(), ADMIN);
    }


}
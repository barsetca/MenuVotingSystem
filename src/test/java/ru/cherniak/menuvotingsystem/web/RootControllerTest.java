package ru.cherniak.menuvotingsystem.web;

import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.cherniak.menuvotingsystem.TestUtil.userAuth;
import static ru.cherniak.menuvotingsystem.UserTestData.ADMIN;


class RootControllerTest extends AbstractControllerTest {

    @Test
    void unAuth() throws Exception {
        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    void getRootView() throws Exception {
        mockMvc.perform(get("/")
                .with(userAuth(ADMIN)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/index.jsp"));
    }
}
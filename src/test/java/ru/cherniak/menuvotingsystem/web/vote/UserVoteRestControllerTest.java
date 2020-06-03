package ru.cherniak.menuvotingsystem.web.vote;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.cherniak.menuvotingsystem.TestUtil;
import ru.cherniak.menuvotingsystem.UserTestData;
import ru.cherniak.menuvotingsystem.model.Vote;
import ru.cherniak.menuvotingsystem.service.VoteService;
import ru.cherniak.menuvotingsystem.web.AbstractControllerTest;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.cherniak.menuvotingsystem.RestaurantTestData.RESTAURANT1_ID;
import static ru.cherniak.menuvotingsystem.TestUtil.userHttpBasic;
import static ru.cherniak.menuvotingsystem.UserTestData.USER;
import static ru.cherniak.menuvotingsystem.UserTestData.USER_ID;
import static ru.cherniak.menuvotingsystem.VoteTestData.*;

class UserVoteRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = UserVoteRestController.REST_USER_VOTES + '/';

    @Autowired
    VoteService voteService;

    @Test
    void createUpdateWithLocation() throws Exception {
        timeBorderPlus();
        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.post(REST_URL + "/by")
                .param("restaurantId", "100002")
                .with(userHttpBasic(USER))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        Vote newVote = new Vote(LocalDate.now());
        Vote created = TestUtil.readFromJson(action, Vote.class);
        Long newID = created.getId();
        newVote.setId(newID);

        VOTE_MATCHER.assertMatch(created, newVote);
        VOTE_MATCHER.assertMatch(voteService.getWithRestaurant(newID, UserTestData.USER_ID), newVote);
        timeBorderFix();
    }

    @Test
    void createUpdateNotFound() throws Exception {
        timeBorderPlus();
        mockMvc.perform(MockMvcRequestBuilders.post(REST_URL + "/by")
                .param("restaurantId", "1")
                .with(userHttpBasic(USER))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
        timeBorderFix();
    }

    @Test
    void createUpdateOutsideTime() throws Exception {
        timeBorderMinus();
        mockMvc.perform(MockMvcRequestBuilders.post(REST_URL + "/by")
                .param("restaurantId", "100002")
                .with(userHttpBasic(USER))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isRequestedRangeNotSatisfiable())
                .andDo(print());
        timeBorderFix();
    }

    @Test
    void delete() throws Exception {
        timeBorderPlus();
        Vote created = voteService.save(new Vote(LocalDate.now()), UserTestData.USER_ID, RESTAURANT1_ID);
        VOTE_MATCHER.assertMatch(voteService.getAllByUserIdWithRestaurant(USER_ID), created, VOTE_3, VOTE_1);

        mockMvc.perform(MockMvcRequestBuilders.delete(REST_URL)
                .with(userHttpBasic(USER)))
                .andDo(print())
                .andExpect(status().isNoContent());

        VOTE_MATCHER.assertMatch(voteService.getAllByUserIdWithRestaurant(USER_ID), VOTE_3, VOTE_1);
        timeBorderFix();
    }

    @Test
    void deleteNotFound() throws Exception {
        timeBorderPlus();
        mockMvc.perform(MockMvcRequestBuilders.delete(REST_URL)
                .with(userHttpBasic(USER)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
        timeBorderFix();
    }

    @Test
    void deleteOutsideTime() throws Exception {
        timeBorderMinus();
        mockMvc.perform(MockMvcRequestBuilders.delete(REST_URL)
                .with(userHttpBasic(USER)))
                .andExpect(status().isRequestedRangeNotSatisfiable())
                .andDo(print());
        timeBorderFix();
    }

    @Test
    void getWithRestaurant() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + VOTE_ID)
                .with(userHttpBasic(USER)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(VOTE_1));
    }

    @Test
    void getWithRestaurantNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + 1)
                .with(userHttpBasic(USER)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }

    @Test
    void getUnAuth() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + VOTE_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getAllByUserIdWithRestaurant() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(USER)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(VOTE_3, VOTE_1));
    }

    @Test
    void getAllWithRestaurantByUserIdBetween() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "filter")
                .param("startDate", "2020-04-29")
                .param("endDate", "2020-04-30")
                .with(userHttpBasic(USER)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(VOTE_3, VOTE_1));
    }

    @Test
    void getAllWithRestaurantByUserIdBetweenWithNull() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "filter")
                .param("startDate", "")
                .param("endDate", "")
                .with(userHttpBasic(USER)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(VOTE_3, VOTE_1));
    }
}
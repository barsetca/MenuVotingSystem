package ru.cherniak.menuvotingsystem.web.vote;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.cherniak.menuvotingsystem.RestaurantTestData;
import ru.cherniak.menuvotingsystem.TestUtil;
import ru.cherniak.menuvotingsystem.UserTestData;
import ru.cherniak.menuvotingsystem.VoteTestData;
import ru.cherniak.menuvotingsystem.model.Vote;
import ru.cherniak.menuvotingsystem.service.VoteService;
import ru.cherniak.menuvotingsystem.to.VoteTo;
import ru.cherniak.menuvotingsystem.web.AbstractControllerTest;
import ru.cherniak.menuvotingsystem.web.json.JsonUtil;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.cherniak.menuvotingsystem.RestaurantTestData.*;
import static ru.cherniak.menuvotingsystem.TestUtil.userHttpBasic;
import static ru.cherniak.menuvotingsystem.UserTestData.*;
import static ru.cherniak.menuvotingsystem.VoteTestData.*;

class UserVoteRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = UserVoteRestController.REST_USER_VOTES + '/';

    @Autowired
    VoteService voteService;

    @Test
    void createWithLocation() throws Exception {
        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.post(REST_URL + "/by")
                .param("restaurantId", "100002")
                .with(userHttpBasic(USER))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        Vote newVote = new Vote(LocalDate.now());
        VoteTo created = TestUtil.readFromJson(action, VoteTo.class);
        Long newID = created.getId();
        newVote.setId(newID);

        VOTE_TO_MATCHER.assertMatch(created, VoteTestData.getVoteTo(newVote, RESTAURANT1));
        VOTE_TO_MATCHER.assertMatch(voteService.getVoteTo(newID, UserTestData.USER_ID), VoteTestData.getVoteTo(newVote, RESTAURANT1));
    }

    @Test
    void createNotOwner() throws Exception {
                mockMvc.perform(MockMvcRequestBuilders.post(REST_URL + "/by")
                .param("restaurantId", "1")
                .with(userHttpBasic(USER))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andDo(print());
          }

    @Test
    void updateOutsideTime() throws Exception {
        voteService.save(ADMIN_ID, RESTAURANT2_ID);
        timeBorderMinus();
        mockMvc.perform(MockMvcRequestBuilders.put(REST_URL + "/by")
                .param("restaurantId", "" + RESTAURANT1_ID)
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isRequestedRangeNotSatisfiable())
                .andDo(print());
        timeBorderFix();
    }

    @Test
    void updateBeforeTimeBorder() throws Exception {
       voteService.save(ADMIN_ID, RESTAURANT2_ID);
       timeBorderPlus();
        mockMvc.perform(MockMvcRequestBuilders.put(REST_URL + "/by")
                .param("restaurantId", "" + RESTAURANT1_ID)
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

       RestaurantTestData.RESTAURANT_MATCHER.assertMatch(voteService.getWithRestaurantToday(ADMIN_ID).getRestaurant(), RESTAURANT1);
       timeBorderFix();
    }


    @Test
    void delete() throws Exception {
        timeBorderPlus();
        Vote created = voteService.save(UserTestData.USER_ID, RESTAURANT1_ID);
        VOTE_TO_MATCHER.assertMatch(voteService.getAllVoteTos(USER_ID), VoteTestData.getVoteTo(created, RESTAURANT1),
                VOTE_TO_3, VOTE_TO_1);

        mockMvc.perform(MockMvcRequestBuilders.delete(REST_URL)
                .with(userHttpBasic(USER)))
                .andDo(print())
                .andExpect(status().isNoContent());

        VOTE_TO_MATCHER.assertMatch(voteService.getAllVoteTos(USER_ID), VOTE_TO_3, VOTE_TO_1);
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
                .andExpect(VOTE_TO_MATCHER.contentJson(VOTE_TO_1));
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
                .andExpect(VOTE_TO_MATCHER.contentJson(VOTE_TO_3, VOTE_TO_1));
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
                .andExpect(VOTE_TO_MATCHER.contentJson(VOTE_TO_3, VOTE_TO_1));
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
                .andExpect(VOTE_TO_MATCHER.contentJson(VOTE_TO_3, VOTE_TO_1));
    }
}
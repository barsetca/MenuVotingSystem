package ru.cherniak.menuvotingsystem.web.vote;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.cherniak.menuvotingsystem.UserTestData;
import ru.cherniak.menuvotingsystem.VoteTestData;
import ru.cherniak.menuvotingsystem.model.Vote;
import ru.cherniak.menuvotingsystem.service.VoteService;
import ru.cherniak.menuvotingsystem.web.AbstractControllerTest;
import ru.cherniak.menuvotingsystem.web.TestUtil;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.cherniak.menuvotingsystem.RestaurantTestData.RESTAURANT1_ID;
import static ru.cherniak.menuvotingsystem.UserTestData.USER_ID;
import static ru.cherniak.menuvotingsystem.VoteTestData.*;

class UserVoteRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = "/" + UserVoteRestController.REST_USER_VOTES + '/';

    @Autowired
    VoteService voteService;


    @Test
    void createUpdateWithLocation() throws Exception {
        timeBorderPlus();
        Vote newVote = getCreatedToday();
        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.post(REST_URL + RESTAURANT1_ID)
                        .contentType(MediaType.APPLICATION_JSON)
//                .content(JsonUtil.writeValue(newVote))
        )
                .andExpect(status().isCreated());

        Vote created = TestUtil.readFromJson(action, Vote.class);
        Long newID = created.getId();
        newVote.setId(newID);

        assertMatch(created, newVote);
        assertMatch(voteService.get(LocalDate.now(), UserTestData.USER_ID), newVote);
        timeBorderFix();
    }

    @Test
    void delete() throws Exception {
        timeBorderPlus();
        Vote created = voteService.save(new Vote(LocalDate.now()), UserTestData.USER_ID, RESTAURANT1_ID);
        VoteTestData.assertMatch(voteService.getAllByUserIdWithRestaurant(USER_ID), created, VOTE_3, VOTE_1);
        mockMvc.perform(MockMvcRequestBuilders.delete(REST_URL))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertMatch(voteService.getAllByUserIdWithRestaurant(USER_ID), VOTE_3, VOTE_1);
        timeBorderFix();
    }

//    @Test
//    void getAllWithRestaurant() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andExpect(contentJson(VOTE_3, VOTE_1, VOTE_2));
//
//    }

    @Test
    void getAllWithRestaurantByUserIdBetween() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "filter")
                .param("startDate", "2020-04-29")
                .param("endDate", "2020-04-30"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(VOTE_3, VOTE_1));
    }

    @Test
    void getAllWithRestaurantByUserIdBetweenWithNull() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "filter")
                .param("startDate", "")
                .param("endDate", ""))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(VOTE_3, VOTE_1));
    }
}
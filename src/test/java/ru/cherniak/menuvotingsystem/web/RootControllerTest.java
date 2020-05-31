package ru.cherniak.menuvotingsystem.web;

import org.assertj.core.matcher.AssertionMatcher;
import org.junit.jupiter.api.Test;
import ru.cherniak.menuvotingsystem.DishTestData;
import ru.cherniak.menuvotingsystem.RestaurantTestData;
import ru.cherniak.menuvotingsystem.VoteTestData;
import ru.cherniak.menuvotingsystem.model.Dish;
import ru.cherniak.menuvotingsystem.model.Restaurant;
import ru.cherniak.menuvotingsystem.model.User;
import ru.cherniak.menuvotingsystem.model.Vote;

import java.util.List;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.cherniak.menuvotingsystem.DishTestData.ALL_DISHES;
import static ru.cherniak.menuvotingsystem.RestaurantTestData.RESTAURANT1;
import static ru.cherniak.menuvotingsystem.RestaurantTestData.RESTAURANT2;
import static ru.cherniak.menuvotingsystem.UserTestData.*;


class RootControllerTest extends AbstractControllerTest {

    @Test
    void getUsers() throws Exception {
        mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("users"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/users.jsp"))
                .andExpect(model().attribute("users",
                        new AssertionMatcher<List<User>>() {
                            @Override
                            public void assertion(List<User> actual) throws AssertionError {
                                assertMatch(actual, ADMIN, USER);
                            }
                        }
                ));
    }

    @Test
    void getRestaurants() throws Exception {
        mockMvc.perform(get("/restaurants"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("restaurants"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/restaurants.jsp"))
                .andExpect(model().attribute("restaurants", new AssertionMatcher<List<Restaurant>>() {
                    @Override
                    public void assertion(List<Restaurant> actual) throws AssertionError {
                        RestaurantTestData.assertMatch(actual, RESTAURANT1, RESTAURANT2);
                    }
                }));
    }

    @Test
    void getDishesByRestaurant() throws Exception {
        mockMvc.perform((get("/dishes")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("dishes"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/dishes.jsp"))
                .andExpect(model().attribute("dishes", new AssertionMatcher<List<Dish>>() {
                    @Override
                    public void assertion(List<Dish> actual) throws AssertionError {
                        DishTestData.assertMatch(actual, ALL_DISHES);
                    }
                }));
    }

    @Test
    void getVotes() throws Exception {
        mockMvc.perform((get("/votes")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("votes"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/votes.jsp"))
                .andExpect(model().attribute("votes", new AssertionMatcher<List<Vote>>() {
                    @Override
                    public void assertion(List<Vote> actual) throws AssertionError {
                        VoteTestData.assertMatch(actual, VoteTestData.USER_ID_VOTES);
                    }
                }));
    }

    @Test
    void getRating() throws Exception {
        mockMvc.perform(get("/rating"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("ratings"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/ratings.jsp"))
                .andExpect(model().attribute("ratings", new AssertionMatcher<List<Restaurant>>() {
                    @Override
                    public void assertion(List<Restaurant> actual) throws AssertionError {
                        Set<Vote> votes1 = actual.get(0).getVotes();
                        Set<Vote> votes2 = actual.get(1).getVotes();
                        VoteTestData.assertMatch(votes2, VoteTestData.VOTE_1);
                        VoteTestData.assertMatch(votes1, VoteTestData.VOTE_3, VoteTestData.VOTE_2);
                        RestaurantTestData.assertMatch(actual, RESTAURANT2, RESTAURANT1);
                    }
                }));
    }
}
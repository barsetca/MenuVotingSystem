package ru.cherniak.menuvotingsystem.service;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.cherniak.menuvotingsystem.RestaurantTestData;
import ru.cherniak.menuvotingsystem.VoteTestData;
import ru.cherniak.menuvotingsystem.model.Restaurant;
import ru.cherniak.menuvotingsystem.model.User;
import ru.cherniak.menuvotingsystem.model.Vote;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static ru.cherniak.menuvotingsystem.DishTestData.DATE_300520;
import static ru.cherniak.menuvotingsystem.DishTestData.DATE_310520;
import static ru.cherniak.menuvotingsystem.RestaurantTestData.*;
import static ru.cherniak.menuvotingsystem.UserTestData.assertMatch;
import static ru.cherniak.menuvotingsystem.UserTestData.*;
import static ru.cherniak.menuvotingsystem.VoteTestData.assertMatch;
import static ru.cherniak.menuvotingsystem.VoteTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})

@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class VoteServiceTest {

    @Autowired
    private VoteService service;

    @Test
    public void create() {
        Vote created = service.save(getCreatedToday(), ADMIN_ID, RESTAURANT2_ID);
        assertMatch(service.get(LocalDate.now(), ADMIN_ID), created);
    }

    @Test
    public void update() {
        VoteTestData.assertMatch(service.getAll(), VOTE_3, VOTE_1, VOTE_2);
        Vote updated = service.save(VOTE_2, ADMIN_ID, RESTAURANT1_ID);
        assertMatch(service.get(DATE_300520, ADMIN_ID), updated);
        VoteTestData.assertMatch(service.getAll(), VOTE_3, VOTE_1, updated);
    }

    @Test
    public void createDuplicate() {

        Vote created = service.save(getCreatedToday(), ADMIN_ID, RESTAURANT2_ID);
        VoteTestData.assertMatch(service.getAll(), VOTE_3, VOTE_1, VOTE_2, created);

        Vote duplicatedDateUserId = service.save(getCreatedToday(), ADMIN_ID, RESTAURANT2_ID);
        VoteTestData.assertMatch(service.getAll(), VOTE_3, VOTE_1, VOTE_2, duplicatedDateUserId);
    }

    @Test
    public void get() {
        Vote vote = service.get(DATE_300520, USER_ID);
        assertMatch(VOTE_1, vote);
    }

    @Test
    public void delete() {
        VoteTestData.assertMatch(service.getAll(), VOTE_3, VOTE_1, VOTE_2);
        service.delete(DATE_300520, USER_ID);
        VoteTestData.assertMatch(service.getAll(), VOTE_3, VOTE_2);
    }

    @Test
    public void countByDateAndRestaurant() {
        long numberR13005 = service.countByDateAndRestaurant(DATE_300520, RESTAURANT1_ID);
        assertEquals(numberR13005, 1);
        long numberR13105 = service.countByDateAndRestaurant(DATE_310520, RESTAURANT1_ID);
        assertEquals(numberR13105, 0);
    }

    @Test
    public void countByRestaurant() {
        long R1 = service.countByRestaurant(RESTAURANT1_ID);
        long R2 = service.countByRestaurant(RESTAURANT2_ID);
        assertEquals(R1, 1);
        assertEquals(R2, 2);
    }

    @Test
    public void countByRestaurantAndDateBetween() {
        service.save(getCreatedToday(), USER_ID, RESTAURANT1_ID);
        long between1 = service.countByRestaurantAndDateBetweenInclusive(DATE_300520, DATE_310520, RESTAURANT1_ID);
        long between2 = service.countByRestaurantAndDateBetweenInclusive(LocalDate.now(), DATE_310520, RESTAURANT1_ID);
        assertEquals(between1, 1);
        assertEquals(between2, 2);
    }

    @Test
    public void getAll() {
        List<Vote> getAll = service.getAll();
        assertMatch(getAll, ALL_VOTES);
    }

    @Test
    public void getOneByDateWithUserAndRestaurant() {
        Vote vote = service.getOneByDateWithUserAndRestaurant(DATE_300520, USER_ID);
        Restaurant restaurant = vote.getRestaurant();
        User user = vote.getUser();
        RestaurantTestData.assertMatch(restaurant, RESTAURANT1);
        assertMatch(user, USER);

    }

    @Test
    public void getAllByDateWithRestaurantAndUser() {
        List<Vote> votes = service.getAllByDateWithRestaurantAndUser(DATE_300520);
        assertEquals(votes.size(), 2);
        List<Restaurant> restaurants = List.of(votes.get(0).getRestaurant(), votes.get(1).getRestaurant());
        List<User> users = List.of(votes.get(0).getUser(), votes.get(1).getUser());
        RestaurantTestData.assertMatch(restaurants, RESTAURANT1, RESTAURANT2);
        assertMatch(users, USER, ADMIN);
    }
}
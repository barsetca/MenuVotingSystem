package ru.cherniak.menuvotingsystem.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.cherniak.menuvotingsystem.RestaurantTestData;
import ru.cherniak.menuvotingsystem.VoteTestData;
import ru.cherniak.menuvotingsystem.model.Restaurant;
import ru.cherniak.menuvotingsystem.model.User;
import ru.cherniak.menuvotingsystem.model.Vote;
import ru.cherniak.menuvotingsystem.repository.vote.VoteRepository;
import ru.cherniak.menuvotingsystem.util.exception.NotFoundException;
import ru.cherniak.menuvotingsystem.util.exception.OutsideTimeException;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.cherniak.menuvotingsystem.DishTestData.DATE_300520;
import static ru.cherniak.menuvotingsystem.DishTestData.DATE_310520;
import static ru.cherniak.menuvotingsystem.RestaurantTestData.*;
import static ru.cherniak.menuvotingsystem.UserTestData.assertMatch;
import static ru.cherniak.menuvotingsystem.UserTestData.*;
import static ru.cherniak.menuvotingsystem.VoteTestData.assertMatch;
import static ru.cherniak.menuvotingsystem.VoteTestData.*;


class VoteServiceTest extends AbstractServiceTest {

    @Autowired
    private VoteService service;

    @Autowired
    VoteRepository voteRepository;

    @Test
    void create() throws Exception {
        timeBorderPlus();
        Vote created = service.save(getCreatedToday(), ADMIN_ID, RESTAURANT2_ID);
        assertMatch(service.get(LocalDate.now(), ADMIN_ID), created);
        timeBorderFix();
    }

    @Test
    void createAfterTimeBorder() throws Exception {
        timeBorderMinus();
        assertThrows(OutsideTimeException.class, () ->
                service.save(getCreatedToday(), ADMIN_ID, RESTAURANT2_ID));
        timeBorderFix();
    }

    @Test
    void update() throws Exception {
        VoteTestData.assertMatch(service.getAll(), VOTE_3, VOTE_1, VOTE_2);
        timeBorderPlus();
        Vote updated = service.save(VOTE_2, ADMIN_ID, RESTAURANT1_ID);
        assertMatch(service.get(DATE_300520, ADMIN_ID), updated);
        VoteTestData.assertMatch(service.getAll(), VOTE_3, VOTE_1, updated);
        timeBorderFix();
    }

    @Test
    void updateAfterTimeBorder() throws Exception {
        timeBorderMinus();
        assertThrows(OutsideTimeException.class, () ->
                service.save(VOTE_2, ADMIN_ID, RESTAURANT1_ID));
        timeBorderFix();
    }

    @Test
    void createDuplicate() throws Exception {
        timeBorderPlus();
        Vote created = service.save(getCreatedToday(), ADMIN_ID, RESTAURANT2_ID);
        VoteTestData.assertMatch(service.getAll(), VOTE_3, VOTE_1, VOTE_2, created);
        Vote duplicatedDateUserId = service.save(getCreatedToday(), ADMIN_ID, RESTAURANT2_ID);
        VoteTestData.assertMatch(service.getAll(), VOTE_3, VOTE_1, VOTE_2, duplicatedDateUserId);
        timeBorderFix();
    }

    @Test
    void get() {
        Vote vote = service.get(DATE_300520, USER_ID);
        assertMatch(VOTE_1, vote);
    }

    @Test
    public void getNotFound() throws Exception {
        assertThrows(NotFoundException.class, () ->
                service.get(LocalDate.now(), 1));
    }

    @Test
    void delete() throws Exception {
        VoteTestData.assertMatch(service.getAll(), VOTE_3, VOTE_1, VOTE_2);
        timeBorderPlus();
        service.delete(DATE_300520, USER_ID);
        VoteTestData.assertMatch(service.getAll(), VOTE_3, VOTE_2);
        timeBorderFix();
    }

    @Test
    public void deletedNotFound() throws Exception {
        timeBorderPlus();
        assertThrows(NotFoundException.class, () ->
                service.delete(LocalDate.now(), 1));
        timeBorderFix();
    }

    @Test
    void deleteAfterTimeBorder() throws Exception {
        timeBorderMinus();
        assertThrows(OutsideTimeException.class, () -> service.delete(DATE_300520, USER_ID));
        timeBorderFix();
    }

    @Test
    void countByDateAndRestaurant() {
        long numberR13005 = service.countByDateAndRestaurant(DATE_300520, RESTAURANT1_ID);
        assertEquals(numberR13005, 1);
        long numberR13105 = service.countByDateAndRestaurant(DATE_310520, RESTAURANT1_ID);
        assertEquals(numberR13105, 0);
    }

    @Test
    void countByRestaurant() {
        long R1 = service.countByRestaurant(RESTAURANT1_ID);
        long R2 = service.countByRestaurant(RESTAURANT2_ID);
        assertEquals(R1, 1);
        assertEquals(R2, 2);
    }

    @Test
    void countByRestaurantAndDateBetween() {
        voteRepository.save(getCreatedToday(), USER_ID, RESTAURANT1_ID);
        long between1 = service.countByRestaurantAndDateBetweenInclusive(DATE_300520, DATE_310520, RESTAURANT1_ID);
        long between2 = service.countByRestaurantAndDateBetweenInclusive(LocalDate.now(), DATE_310520, RESTAURANT1_ID);
        assertEquals(between1, 1);
        assertEquals(between2, 2);
    }

    @Test
    void getAll() {
        List<Vote> getAll = service.getAll();
        assertMatch(getAll, ALL_VOTES);
    }

    @Test
    void getOneByDateWithUserAndRestaurant() {
        Vote vote = service.getOneByDateWithUserAndRestaurant(DATE_300520, USER_ID);
        Restaurant restaurant = vote.getRestaurant();
        User user = vote.getUser();
        RestaurantTestData.assertMatch(restaurant, RESTAURANT1);
        assertMatch(user, USER);
    }

    @Test
    public void getOneByDateWithUserAndRestaurantNotFound() throws Exception {
        assertThrows(NotFoundException.class, () ->
                service.get(LocalDate.now(), 1));
    }

    @Test
    void getAllByDateWithRestaurantAndUser() {
        List<Vote> votes = service.getAllByDateWithRestaurantAndUser(DATE_300520);
        assertEquals(votes.size(), 2);
        List<Restaurant> restaurants = List.of(votes.get(0).getRestaurant(), votes.get(1).getRestaurant());
        List<User> users = List.of(votes.get(0).getUser(), votes.get(1).getUser());
        RestaurantTestData.assertMatch(restaurants, RESTAURANT1, RESTAURANT2);
        assertMatch(users, USER, ADMIN);
    }

    @Test
    void getAllWithRestaurant() {
        List<Vote> votes = service.getAllWithRestaurant();
        List<Restaurant> restaurants = List.of(votes.get(0).getRestaurant(), votes.get(1).getRestaurant(), votes.get(2).getRestaurant());
        assertMatch(votes, ALL_VOTES);
        RestaurantTestData.assertMatch(restaurants, RESTAURANT2, RESTAURANT1, RESTAURANT2);
    }

    @Test
    void getAllByUserIdWithRestaurant() {
        List<Vote> votes = service.getAllByUserIdWithRestaurant(USER_ID);
        assertMatch(votes, VOTE_3, VOTE_1);
        RestaurantTestData.assertMatch(votes.get(0).getRestaurant(), RESTAURANT2);
        RestaurantTestData.assertMatch(votes.get(1).getRestaurant(), RESTAURANT1);
    }

    @Test
    void getAllWithRestaurantByUserIdBetween() {

        Vote today = voteRepository.save(getCreatedToday(), USER_ID, RESTAURANT1_ID);
        List<Vote> votes1 = service.getAllWithRestaurantByUserIdBetween(DATE_300520, DATE_310520, USER_ID);
        List<Vote> votes2 = service.getAllWithRestaurantByUserIdBetween(LocalDate.now(), DATE_310520, USER_ID);

        assertMatch(votes1, VOTE_3, VOTE_1);
        assertMatch(votes2, VOTE_3, VOTE_1, today);

        RestaurantTestData.assertMatch(votes1.get(0).getRestaurant(), RESTAURANT2);
        RestaurantTestData.assertMatch(votes1.get(1).getRestaurant(), RESTAURANT1);

        RestaurantTestData.assertMatch(votes2.get(2).getRestaurant(), RESTAURANT1);
    }

    @Test
    void getAllWithRestaurantByUserIdBetweenWithNull() {
        Vote today = voteRepository.save(getCreatedToday(), USER_ID, RESTAURANT1_ID);
        List<Vote> votes1 = service.getAllWithRestaurantByUserIdBetween(DATE_300520, null, USER_ID);
        List<Vote> votes2 = service.getAllWithRestaurantByUserIdBetween(null, DATE_310520, USER_ID);
        List<Vote> votes3 = service.getAllWithRestaurantByUserIdBetween(null, null, USER_ID);

        assertMatch(votes1, VOTE_3, VOTE_1);
        assertMatch(votes2, VOTE_3, VOTE_1, today);
        assertMatch(votes3, VOTE_3, VOTE_1, today);
    }

    @Test
    void createWithException() throws Exception {
        timeBorderPlus();
        validateRootCause(() -> service.save(new Vote(LocalDate.of(2020, Month.APRIL, 30)), USER_ID, RESTAURANT1_ID),
                ConstraintViolationException.class);
        timeBorderFix();
    }
}
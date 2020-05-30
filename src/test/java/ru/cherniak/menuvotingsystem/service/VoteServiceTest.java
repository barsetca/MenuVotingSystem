package ru.cherniak.menuvotingsystem.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.cherniak.menuvotingsystem.RestaurantTestData;
import ru.cherniak.menuvotingsystem.VoteTestData;
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
import static ru.cherniak.menuvotingsystem.DishTestData.DATE_290420;
import static ru.cherniak.menuvotingsystem.DishTestData.DATE_300420;
import static ru.cherniak.menuvotingsystem.RestaurantTestData.*;
import static ru.cherniak.menuvotingsystem.UserTestData.ADMIN_ID;
import static ru.cherniak.menuvotingsystem.UserTestData.USER_ID;
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
        timeBorderPlus();
        Vote created = service.save(new Vote(LocalDate.now()), USER_ID, RESTAURANT1_ID);
        VoteTestData.assertMatch(service.getAllByUserIdWithRestaurant(USER_ID), created, VOTE_3, VOTE_1);
        Vote updated = service.save(new Vote(LocalDate.now()), USER_ID, RESTAURANT2_ID);
        assertMatch(service.get(LocalDate.now(), USER_ID), updated);
        VoteTestData.assertMatch(service.getAllByUserIdWithRestaurant(USER_ID), updated, VOTE_3, VOTE_1);
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
        Vote created = service.save(new Vote(LocalDate.now()), ADMIN_ID, RESTAURANT2_ID);
        VoteTestData.assertMatch(service.getAllByUserIdWithRestaurant(ADMIN_ID), created, VOTE_2);
        Vote duplicatedDateUserId = service.save(new Vote(LocalDate.now()), ADMIN_ID, RESTAURANT2_ID);
        VoteTestData.assertMatch(service.getAllByUserIdWithRestaurant(ADMIN_ID), duplicatedDateUserId, VOTE_2);
        timeBorderFix();
    }

    @Test
    void get() {
        Vote vote = service.get(DATE_290420, USER_ID);
        assertMatch(VOTE_1, vote);
    }

    @Test
    public void getNotFound() throws Exception {
        assertThrows(NotFoundException.class, () ->
                service.get(LocalDate.now(), 1));
    }

    @Test
    void delete() throws Exception {
        timeBorderPlus();
        Vote created = service.save(new Vote(LocalDate.now()), USER_ID, RESTAURANT2_ID);
        VoteTestData.assertMatch(service.getAllByUserIdWithRestaurant(USER_ID), created, VOTE_3, VOTE_1);
        service.delete(USER_ID);
        VoteTestData.assertMatch(service.getAllByUserIdWithRestaurant(USER_ID), VOTE_3, VOTE_1);
        timeBorderFix();
    }

    @Test
    public void deletedNotFound() throws Exception {
        timeBorderPlus();
        assertThrows(NotFoundException.class, () ->
                service.delete(1));
        timeBorderFix();
    }

    @Test
    void deleteAfterTimeBorder() throws Exception {
        timeBorderMinus();
        assertThrows(OutsideTimeException.class, () -> service.delete(USER_ID));
        timeBorderFix();
    }

    @Test
    void countByRestaurant() {
        long R1 = service.countByRestaurant(RESTAURANT1_ID);
        long R2 = service.countByRestaurant(RESTAURANT2_ID);
        assertEquals(R1, 1);
        assertEquals(R2, 2);
    }


    @Test
    public void getOneByDateWithUserAndRestaurantNotFound() throws Exception {
        assertThrows(NotFoundException.class, () ->
                service.get(LocalDate.now(), 1));
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

        Vote today = voteRepository.save(new Vote(LocalDate.now()), USER_ID, RESTAURANT1_ID);
        List<Vote> votes1 = service.getAllWithRestaurantByUserIdBetween(DATE_290420, DATE_300420, USER_ID);
        List<Vote> votes2 = service.getAllWithRestaurantByUserIdBetween(DATE_300420, LocalDate.now(), USER_ID);

        assertMatch(votes1, VOTE_3, VOTE_1);
        assertMatch(votes2, today, VOTE_3);

        RestaurantTestData.assertMatch(votes1.get(0).getRestaurant(), RESTAURANT2);
        RestaurantTestData.assertMatch(votes1.get(1).getRestaurant(), RESTAURANT1);

        RestaurantTestData.assertMatch(votes2.get(0).getRestaurant(), RESTAURANT1);
    }

    @Test
    void getAllWithRestaurantByUserIdBetweenWithNull() {
        Vote today = voteRepository.save(new Vote(LocalDate.now()), USER_ID, RESTAURANT1_ID);
        List<Vote> votes1 = service.getAllWithRestaurantByUserIdBetween(DATE_300420, null, USER_ID);
        List<Vote> votes2 = service.getAllWithRestaurantByUserIdBetween(null, DATE_300420, USER_ID);
        List<Vote> votes3 = service.getAllWithRestaurantByUserIdBetween(null, null, USER_ID);

        assertMatch(votes1, today, VOTE_3);
        assertMatch(votes2, VOTE_3, VOTE_1);
        assertMatch(votes3, today, VOTE_3, VOTE_1);
    }

    @Test
    void createWithException() throws Exception {
        timeBorderPlus();
        validateRootCause(() -> service.save(new Vote(LocalDate.of(2020, Month.APRIL, 30)), USER_ID, RESTAURANT1_ID),
                ConstraintViolationException.class);
        timeBorderFix();
    }
}
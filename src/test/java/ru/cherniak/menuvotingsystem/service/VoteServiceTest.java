package ru.cherniak.menuvotingsystem.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;
import ru.cherniak.menuvotingsystem.VoteTestData;
import ru.cherniak.menuvotingsystem.model.Vote;
import ru.cherniak.menuvotingsystem.to.VoteTo;
import ru.cherniak.menuvotingsystem.util.VoteUtil;
import ru.cherniak.menuvotingsystem.util.exception.NotFoundException;
import ru.cherniak.menuvotingsystem.util.exception.OutsideTimeException;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.cherniak.menuvotingsystem.DishTestData.DATE_290420;
import static ru.cherniak.menuvotingsystem.DishTestData.DATE_300420;
import static ru.cherniak.menuvotingsystem.RestaurantTestData.*;
import static ru.cherniak.menuvotingsystem.UserTestData.ADMIN_ID;
import static ru.cherniak.menuvotingsystem.UserTestData.USER_ID;
import static ru.cherniak.menuvotingsystem.VoteTestData.*;

class VoteServiceTest extends AbstractServiceTest {

    @Autowired
    private VoteService service;

    @Test
    void create() {
        service.save(ADMIN_ID, RESTAURANT2_ID);
        RESTAURANT_MATCHER.assertMatch(service.getWithRestaurantToday(ADMIN_ID).getRestaurant(), RESTAURANT2);
    }

    @Test
    void updateBeforeTimeBorder() throws Exception {
        service.save(ADMIN_ID, RESTAURANT2_ID);
        timeBorderPlus();
        service.save(ADMIN_ID, RESTAURANT1_ID);
        RESTAURANT_MATCHER.assertMatch(service.getWithRestaurantToday(ADMIN_ID).getRestaurant(), RESTAURANT1);
        timeBorderFix();
    }

    @Test
    void updateAfterTimeBorder() throws Exception {
        service.save(ADMIN_ID, RESTAURANT2_ID);
        timeBorderMinus();
        assertThrows(OutsideTimeException.class, () ->
                service.save(ADMIN_ID, RESTAURANT1_ID));
        timeBorderFix();
    }

    @Test
    void createUpdateNotOwner() throws Exception {
        timeBorderPlus();
        assertThrows(DataIntegrityViolationException.class, () ->
                service.save(ADMIN_ID, 1));
        timeBorderFix();
    }

    @Test
    void saveDuplicate() throws Exception {
        Vote created = service.save(ADMIN_ID, RESTAURANT2_ID);
        VOTE_MATCHER.assertMatch(service.getAllByUserIdWithRestaurant(ADMIN_ID), created, VOTE_2);

        timeBorderPlus();
        Vote duplicatedDateUserId = service.save(ADMIN_ID, RESTAURANT2_ID);
        VOTE_MATCHER.assertMatch(service.getAllByUserIdWithRestaurant(ADMIN_ID), duplicatedDateUserId, VOTE_2);
        timeBorderFix();
    }

    @Test
    void getVoteTo() {
        VoteTo voteTo = service.getVoteTo(VOTE_ID, USER_ID);
        VOTE_TO_MATCHER.assertMatch(VOTE_TO_1, voteTo);
        assertEquals(VOTE_TO_1.getName(), RESTAURANT1.getName());
    }

    @Test
    public void getWithRestaurantNotFound() {
        assertThrows(NotFoundException.class, () ->
                service.getVoteTo(VOTE_ID, 1));
    }

    @Test
    void delete() throws Exception {
        Vote created = service.save(USER_ID, RESTAURANT2_ID);
        VOTE_MATCHER.assertMatch(service.getAllByUserIdWithRestaurant(USER_ID), created, VOTE_3, VOTE_1);

        timeBorderPlus();
        service.delete(USER_ID);
        VOTE_MATCHER.assertMatch(service.getAllByUserIdWithRestaurant(USER_ID), VOTE_3, VOTE_1);
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
        service.save(USER_ID, RESTAURANT2_ID);
        timeBorderMinus();
        assertThrows(OutsideTimeException.class, () -> service.delete(USER_ID));
        timeBorderFix();
    }

    @Test
    void getAllVoteTos() {
        List<VoteTo> voteTos = service.getAllVoteTos(USER_ID);
        VOTE_TO_MATCHER.assertMatch(voteTos, VOTE_TO_3, VOTE_TO_1);
        assertEquals(RESTAURANT2.getName(), voteTos.get(0).getName());
        assertEquals(RESTAURANT1.getName(), voteTos.get(1).getName());
    }

    @Test
    void getVoteTosBetweenInclusive() {
        Vote today = service.save(USER_ID, RESTAURANT1_ID);

        List<VoteTo> voteTos1 = service.getVoteTosBetweenInclusive(DATE_290420, DATE_300420, USER_ID);
        List<VoteTo> voteTos2 = service.getVoteTosBetweenInclusive(DATE_300420, LocalDate.now(), USER_ID);

        VOTE_TO_MATCHER.assertMatch(voteTos1, VOTE_TO_3, VOTE_TO_1);
        VOTE_TO_MATCHER.assertMatch(voteTos2, VoteTestData.getVoteTo(today, RESTAURANT1), VOTE_TO_3);

        assertEquals(RESTAURANT2.getName(), voteTos1.get(0).getName());
        assertEquals(RESTAURANT1.getName(), voteTos1.get(1).getName());

        assertEquals(RESTAURANT1.getName(), voteTos2.get(0).getName());
    }

    @Test
    void getVoteTosBetweenInclusiveBetweenWithNull() {

        Vote today = service.save(USER_ID, RESTAURANT1_ID);
        List<VoteTo> voteTos1 = service.getVoteTosBetweenInclusive(DATE_300420, null, USER_ID);
        List<VoteTo> voteTos2 = service.getVoteTosBetweenInclusive(null, DATE_300420, ADMIN_ID);
        List<VoteTo> voteTos3 = service.getVoteTosBetweenInclusive(null, null, USER_ID);

        VOTE_TO_MATCHER.assertMatch(voteTos1, VoteTestData.getVoteTo(today, RESTAURANT1), VOTE_TO_3);
        VOTE_TO_MATCHER.assertMatch(voteTos2, VOTE_TO_2);
        VOTE_TO_MATCHER.assertMatch(voteTos3, VoteTestData.getVoteTo(today, RESTAURANT1), VOTE_TO_3, VOTE_TO_1);
    }

    @Test
    void getWithRestaurantToday() {
        service.save(ADMIN_ID, RESTAURANT2_ID);
        RESTAURANT_MATCHER.assertMatch(service.getWithRestaurantToday(ADMIN_ID).getRestaurant(), RESTAURANT2);
    }

    @Test
    void getWithRestaurantTodayNotFound() {
        assertThrows(NotFoundException.class, () ->
                service.getWithRestaurantToday(1));
    }

    @Test
    @Transactional
    void getVoteToToday() {
        Vote vote = service.save(ADMIN_ID, RESTAURANT2_ID);
        VoteTo today = service.getVoteToToday(ADMIN_ID);
        VoteTo voteTo = VoteUtil.createTo(vote);
        VOTE_TO_MATCHER.assertMatch(today, voteTo);
    }
}
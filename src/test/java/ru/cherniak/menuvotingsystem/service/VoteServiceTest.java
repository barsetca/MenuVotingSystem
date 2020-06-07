package ru.cherniak.menuvotingsystem.service;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import ru.cherniak.menuvotingsystem.VoteTestData;
import ru.cherniak.menuvotingsystem.model.Vote;
import ru.cherniak.menuvotingsystem.repository.vote.VoteRepository;
import ru.cherniak.menuvotingsystem.to.VoteTo;
import ru.cherniak.menuvotingsystem.util.exception.NotFoundException;
import ru.cherniak.menuvotingsystem.util.exception.OutsideTimeException;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @Autowired
    VoteRepository voteRepository;

    @Test
    void create() throws Exception {
        VoteTestData.timeBorderPlus();
        Vote created = service.save(ADMIN_ID, RESTAURANT2_ID);
        VOTE_MATCHER.assertMatch(voteRepository.getWithRestaurant(created.getId(), ADMIN_ID), created);
        VoteTestData.timeBorderFix();
    }

    @Test
    void createUpdateAfterTimeBorder() throws Exception {
        timeBorderMinus();
        assertThrows(OutsideTimeException.class, () ->
                service.save(ADMIN_ID, RESTAURANT2_ID));
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
    void createUpdateDuplicate() throws Exception {
        timeBorderPlus();
        Vote created = service.save(ADMIN_ID, RESTAURANT2_ID);
        VOTE_MATCHER.assertMatch(voteRepository.getAllByUserIdWithRestaurant(ADMIN_ID), created, VOTE_2);

        Vote duplicatedDateUserId = service.save(ADMIN_ID, RESTAURANT2_ID);
        VOTE_MATCHER.assertMatch(voteRepository.getAllByUserIdWithRestaurant(ADMIN_ID), duplicatedDateUserId, VOTE_2);
        timeBorderFix();
    }

    @Test
    void update() throws Exception {
        timeBorderPlus();
        Vote created = service.save(USER_ID, RESTAURANT1_ID);
        VOTE_MATCHER.assertMatch(voteRepository.getAllByUserIdWithRestaurant(USER_ID), created, VOTE_3, VOTE_1);

        Vote updated = service.save(USER_ID, RESTAURANT2_ID);
        VOTE_MATCHER.assertMatch(voteRepository.getWithRestaurant(updated.getId(), USER_ID), updated);
        VOTE_MATCHER.assertMatch(voteRepository.getAllByUserIdWithRestaurant(USER_ID), updated, VOTE_3, VOTE_1);
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
        timeBorderPlus();
        Vote created = service.save(USER_ID, RESTAURANT2_ID);
        VOTE_MATCHER.assertMatch(voteRepository.getAllByUserIdWithRestaurant(USER_ID), created, VOTE_3, VOTE_1);
        service.delete(USER_ID);
        VOTE_MATCHER.assertMatch(voteRepository.getAllByUserIdWithRestaurant(USER_ID), VOTE_3, VOTE_1);
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
        timeBorderPlus();
        service.save(USER_ID, RESTAURANT2_ID);
        timeBorderFix();
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
    void getAllVoteTos() {
        List<VoteTo> voteTos = service.getAllVoteTos(USER_ID);
        VOTE_TO_MATCHER.assertMatch(voteTos, VOTE_TO_3, VOTE_TO_1);
        Assert.assertEquals(RESTAURANT2.getName(), voteTos.get(0).getName());
        Assert.assertEquals(RESTAURANT1.getName(), voteTos.get(1).getName());

    }

    @Test
    void getVoteTosBetweenInclusive() {

        Vote today = voteRepository.save(new Vote(LocalDate.now()), USER_ID, RESTAURANT1_ID);
        List<VoteTo> voteTos1 = service.getVoteTosBetweenInclusive(DATE_290420, DATE_300420, USER_ID);
        List<VoteTo> voteTos2 = service.getVoteTosBetweenInclusive(DATE_300420, LocalDate.now(), USER_ID);

        VOTE_TO_MATCHER.assertMatch(voteTos1, VOTE_TO_3, VOTE_TO_1);
        VOTE_TO_MATCHER.assertMatch(voteTos2, VoteTestData.getVoteTo(today, RESTAURANT1), VOTE_TO_3);

        Assert.assertEquals(RESTAURANT2.getName(), voteTos1.get(0).getName());
        Assert.assertEquals(RESTAURANT1.getName(), voteTos1.get(1).getName());

        Assert.assertEquals(RESTAURANT1.getName(), voteTos2.get(0).getName());

    }

    @Test
    void getVoteTosBetweenInclusiveBetweenWithNull() {
        Vote today = voteRepository.save(new Vote(LocalDate.now()), USER_ID, RESTAURANT1_ID);
        List<VoteTo> voteTos1 = service.getVoteTosBetweenInclusive(DATE_300420, null, USER_ID);
        List<VoteTo> voteTos2 = service.getVoteTosBetweenInclusive(null, DATE_300420, ADMIN_ID);
        List<VoteTo> voteTos3 = service.getVoteTosBetweenInclusive(null, null, USER_ID);

        VOTE_TO_MATCHER.assertMatch(voteTos1, VoteTestData.getVoteTo(today, RESTAURANT1), VOTE_TO_3);
        VOTE_TO_MATCHER.assertMatch(voteTos2, VOTE_TO_2);
        VOTE_TO_MATCHER.assertMatch(voteTos3, VoteTestData.getVoteTo(today, RESTAURANT1), VOTE_TO_3, VOTE_TO_1);
    }
}
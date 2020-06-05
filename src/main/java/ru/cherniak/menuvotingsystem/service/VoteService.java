package ru.cherniak.menuvotingsystem.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.cherniak.menuvotingsystem.model.Vote;
import ru.cherniak.menuvotingsystem.repository.vote.VoteRepository;
import ru.cherniak.menuvotingsystem.to.VoteTo;
import ru.cherniak.menuvotingsystem.util.DateTimeUtil;
import ru.cherniak.menuvotingsystem.util.VoteUtil;

import java.time.LocalDate;
import java.util.List;

import static ru.cherniak.menuvotingsystem.util.DateTimeUtil.checkTimeBorder;
import static ru.cherniak.menuvotingsystem.util.ValidationUtil.*;

@Service
public class VoteService {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final VoteRepository repository;

    @Autowired
    public VoteService(VoteRepository repository) {

        this.repository = repository;
    }

    public Vote save(long userId, long restaurantId) {
        checkTimeBorder();
        Vote vote = new Vote(LocalDate.now());
        log.info("save {} by user {} restaurant {}", vote, userId, restaurantId);
        Assert.notNull(vote, "vote must not be null");
        return checkNotFoundWithMsg(repository.save(vote, userId, restaurantId), "restaurantId" + restaurantId);
    }

    public VoteTo getVoteTo(long id, long userId) {
        log.info("get {} by user {}", id, userId);
        Vote voteWithRestaurant = checkNotFoundWithId(repository.getWithRestaurant(id, userId), id);
        return VoteUtil.createTo(voteWithRestaurant);
    }

    public void delete(long userId) {
        checkTimeBorder();
        LocalDate date = LocalDate.now();
        log.info("delete by user {} date {}", userId, date);
        checkNotFound(repository.delete(date, userId), "date: " + date);
    }

    public long countByRestaurant(long restaurantId) {
        log.info("countByRestaurant by restaurant {}", restaurantId);
        return repository.countByRestaurant(restaurantId);
    }

    public List<VoteTo> getAllVoteTos(long userId) {
        log.info("getAllVoteTos by user {}", userId);
        List<Vote> votesWithRestaurant = repository.getAllByUserIdWithRestaurant(userId);
        return VoteUtil.getVoteTos(votesWithRestaurant);
    }

    public List<VoteTo> getVoteTosBetweenInclusive(@Nullable LocalDate startDate, @Nullable LocalDate endDate, long userId) {
        log.info("getAllWithRestaurantByUserIdBetween {} - {} of restaurant {}", startDate, endDate, userId);
        List<Vote> votesWithRestaurantBetween =  repository.getAllWithRestaurantByUserIdBetween(
                DateTimeUtil.getStartDate(startDate),
                DateTimeUtil.getEndDate(endDate), userId);
        return VoteUtil.getVoteTos(votesWithRestaurantBetween);
    }


}

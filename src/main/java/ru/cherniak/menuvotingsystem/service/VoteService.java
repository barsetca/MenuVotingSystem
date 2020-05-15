package ru.cherniak.menuvotingsystem.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.cherniak.menuvotingsystem.model.Vote;
import ru.cherniak.menuvotingsystem.repository.VoteRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class VoteService {

    protected final Logger log = LoggerFactory.getLogger(VoteService.class);

    private final VoteRepository repository;

    @Autowired
    public VoteService(VoteRepository repository) {

        this.repository = repository;
    }

    public Vote save(Vote vote, long userId, long restaurantId) {
        log.info("save {} by user {} restaurant {}", vote, userId, restaurantId);
        Assert.notNull(vote, "vote must not be null");
        return repository.save(vote, userId, restaurantId);
    }

    public Vote get(@Nullable LocalDate date, long userId) {
        log.info("get by user {} date {}", userId, date);
        //        checkNotFoundWithId(repository.get(date, userId), date);
        return repository.get(date, userId);
    }

    public boolean delete(@Nullable LocalDate date, long userId) {
        log.info("delete by user {} date {}", userId, date);
        //checkNotFoundWithId(repository.delete(date, userId), id);
        return repository.delete(date, userId);
    }

    public long getRestaurantVotesByDate(@Nullable LocalDate date, long restaurantId) {
        log.info("getRestaurantVotesByDate by restaurant {} date {}", restaurantId, date);
        return repository.getRestaurantVotesByDate(date, restaurantId);
    }

    public long getAllRestaurantVotes(long restaurantId) {
        log.info("getAllRestaurantVotes by restaurant {}", restaurantId);
        return repository.getAllRestaurantVotes(restaurantId);
    }

    public long getAllRestaurantVotesBetweenDatesInclusive(@Nullable LocalDate startDate, @Nullable LocalDate endDate,
                                                           long restaurantId) {
        log.info("getAllRestaurantVotesBetweenDatesInclusive {} - {} of restaurant {}", startDate, endDate, restaurantId);
        return repository.getAllRestaurantVotesBetween(startDate, endDate, restaurantId);
    }

    public List<Vote> getAll() {
        log.info("getAll");
        return repository.getAll();
    }
}

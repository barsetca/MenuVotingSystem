package ru.cherniak.menuvotingsystem.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.cherniak.menuvotingsystem.model.Vote;
import ru.cherniak.menuvotingsystem.repository.vote.VoteRepository;

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

    public long countByDateAndRestaurant(@Nullable LocalDate date, long restaurantId) {
        log.info("countByDateAndRestaurant by restaurant {} date {}", restaurantId, date);
        return repository.countByDateAndRestaurant(date, restaurantId);
    }

    public long countByRestaurant(long restaurantId) {
        log.info("countByRestaurant by restaurant {}", restaurantId);
        return repository.countByRestaurant(restaurantId);
    }

    public long countByRestaurantAndDateBetweenInclusive(@Nullable LocalDate startDate, @Nullable LocalDate endDate,
                                                         long restaurantId) {
        log.info("countByRestaurantAndDateBetweenInclusive {} - {} of restaurant {}", startDate, endDate, restaurantId);
        return repository.countByRestaurantAndDateBetween(startDate, endDate, restaurantId);
    }

    public List<Vote> getAll() {
        log.info("getAll");
        return repository.getAll();
    }

    public Vote getOneByDateWithUserAndRestaurant(@Nullable LocalDate date, long userId) {
        log.info("getOneByDateWithUserAndRestaurant by user {} date {}", userId, date);
        return repository.getOneByDateWithUserAndRestaurant(date, userId);
    }

    public List<Vote> getAllByDateWithRestaurantAndUser(@Nullable LocalDate date) {
        log.info("getAllByDateWithRestaurantAndUser by date {}", date);
        return repository.getAllByDateWithRestaurantAndUser(date);
    }

    public List<Vote> getAllWithRestaurant(){
        log.info("getAllWithRestaurant");
        return repository.getAllWithRestaurant();
    }
}

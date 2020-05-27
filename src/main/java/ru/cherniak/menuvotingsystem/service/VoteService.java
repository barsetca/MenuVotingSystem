package ru.cherniak.menuvotingsystem.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.cherniak.menuvotingsystem.model.Vote;
import ru.cherniak.menuvotingsystem.repository.vote.VoteRepository;
import ru.cherniak.menuvotingsystem.util.DateTimeUtil;

import java.time.LocalDate;
import java.util.List;

import static ru.cherniak.menuvotingsystem.util.DateTimeUtil.checkTimeBorder;
import static ru.cherniak.menuvotingsystem.util.ValidationUtil.checkNotFoundWithId;

@Service
public class VoteService {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final VoteRepository repository;

    @Autowired
    public VoteService(VoteRepository repository) {

        this.repository = repository;
    }

    public Vote save(Vote vote, long userId, long restaurantId) {
        log.info("save {} by user {} restaurant {}", vote, userId, restaurantId);
        checkTimeBorder();
        Assert.notNull(vote, "vote must not be null");
        return repository.save(vote, userId, restaurantId);
    }

    public Vote get(@Nullable LocalDate date, long userId) {
        log.info("get by user {} date {}", userId, date);
        return checkNotFoundWithId(repository.get(date, userId), userId);

    }

    public void delete(@Nullable LocalDate date, long userId) {
        log.info("delete by user {} date {}", userId, date);
        checkTimeBorder();
        checkNotFoundWithId(repository.delete(date, userId), userId);
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
        return checkNotFoundWithId(repository.getOneByDateWithUserAndRestaurant(date, userId), userId);

    }

    public List<Vote> getAllByDateWithRestaurantAndUser(@Nullable LocalDate date) {
        log.info("getAllByDateWithRestaurantAndUser by date {}", date);
        return repository.getAllByDateWithRestaurantAndUser(date);
    }

    public List<Vote> getAllWithRestaurant() {
        log.info("getAllWithRestaurant");
        return repository.getAllWithRestaurant();
    }

    public List<Vote> getAllByUserIdWithRestaurant(long userId) {
        log.info("getAllByUserIdWithRestaurant by user {}", userId);
        return repository.getAllByUserIdWithRestaurant(userId);
    }

    public List<Vote> getAllWithRestaurantByUserIdBetween(@Nullable LocalDate startDate, @Nullable LocalDate endDate, long userId) {
        log.info("getAllWithRestaurantByUserIdBetween {} - {} of restaurant {}", startDate, endDate, userId);
        return repository.getAllWithRestaurantByUserIdBetween(DateTimeUtil.getStartDate(startDate),
                DateTimeUtil.getEndDate(endDate), userId);
    }
}

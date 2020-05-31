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

    public Vote getWithRestaurant(long id, long userId) {
        log.info("get {} by user {}", id, userId);
        return checkNotFoundWithId(repository.getWithRestaurant(id, userId), id);
    }

    public void delete(long userId) {
        checkTimeBorder();
        LocalDate date = LocalDate.now();
        log.info("delete by user {} date {}", userId, date);
        checkNotFoundWithId(repository.delete(date, userId), userId);
    }

    public long countByRestaurant(long restaurantId) {
        log.info("countByRestaurant by restaurant {}", restaurantId);
        return repository.countByRestaurant(restaurantId);
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

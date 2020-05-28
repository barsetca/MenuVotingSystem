package ru.cherniak.menuvotingsystem.web.vote;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import ru.cherniak.menuvotingsystem.model.Vote;
import ru.cherniak.menuvotingsystem.service.VoteService;

import java.time.LocalDate;
import java.util.List;

import static ru.cherniak.menuvotingsystem.web.SecurityUtil.authUserId;

public class AbstractVoteController {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private VoteService voteService;


    public Vote save(Vote vote, long restaurantId) {
        long userId = authUserId();
        log.info("save {} by user {} restaurant {}", vote, userId, restaurantId);
        return voteService.save(vote, authUserId(), restaurantId);
    }

//пока для клиента не использую
    public Vote get(@Nullable LocalDate date) {
        long userId = authUserId();
        log.info("get by user {} date {}", userId, date);
        return voteService.get(date, userId);
    }

    public void delete() {
        long userId = authUserId();
        LocalDate date = LocalDate.now();
        log.info("delete by user {} date {}", userId, date);
        voteService.delete(date, userId);
    }

    public long countByDateAndRestaurant(@Nullable LocalDate date, long restaurantId) {
        log.info("countByDateAndRestaurant by restaurant {} date {}", restaurantId, date);
        return voteService.countByDateAndRestaurant(date, restaurantId);
    }

    public long countByRestaurant(long restaurantId) {
        log.info("countByRestaurant by restaurant {}", restaurantId);
        return voteService.countByRestaurant(restaurantId);
    }

    public long countByRestaurantAndDateBetweenInclusive(@Nullable LocalDate startDate, @Nullable LocalDate endDate,
                                                         long restaurantId) {
        log.info("countByRestaurantAndDateBetweenInclusive {} - {} of restaurant {}", startDate, endDate, restaurantId);
        return voteService.countByRestaurantAndDateBetweenInclusive(startDate, endDate, restaurantId);
    }

    //getAll в таком виде не нужен
    public List<Vote> getAll() {
        log.info("getAll");
        return voteService.getAll();
    }

    public Vote getOneByDateWithUserAndRestaurant(@Nullable LocalDate date, long userId) {
        log.info("getOneByDateWithUserAndRestaurant by user {} date {}", userId, date);
        return voteService.getOneByDateWithUserAndRestaurant(date, userId);
    }

    public List<Vote> getAllByDateWithRestaurantAndUser(@Nullable LocalDate date) {
        log.info("getAllByDateWithRestaurantAndUser by date {}", date);
        return voteService.getAllByDateWithRestaurantAndUser(date);
    }

    //getAllWithRestaurant в таком виде не нужен
    public List<Vote> getAllWithRestaurant() {
        log.info("getAllWithRestaurant");
        return voteService.getAllWithRestaurant();
    }

    public List<Vote> getAllWithRestaurantByUserIdBetween(@Nullable LocalDate startDate, @Nullable LocalDate endDate) {
        long userId = authUserId();
        log.info("getAllWithRestaurantByUserIdBetween {} - {} of user {}", startDate, endDate, userId);
        return voteService.getAllWithRestaurantByUserIdBetween(startDate, endDate, userId);
    }
}

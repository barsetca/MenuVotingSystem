package ru.cherniak.menuvotingsystem.repository.vote;

import org.springframework.lang.Nullable;
import ru.cherniak.menuvotingsystem.model.Vote;

import java.time.LocalDate;
import java.util.List;

public interface VoteRepository {

    Vote save(Vote vote, long userId, long restaurantId);

    Vote get(@Nullable LocalDate date, long userId);

    boolean delete(@Nullable LocalDate date, long userId);

    long countByDateAndRestaurant(@Nullable LocalDate date, long restaurantId);

    long countByRestaurant(long restaurantId);

    long countByRestaurantAndDateBetween(@Nullable LocalDate startDate, @Nullable LocalDate endDate, long restaurantId);

    List<Vote> getAll();

    Vote getOneByDateWithUserAndRestaurant(@Nullable LocalDate date, long userId);

    List<Vote> getAllByDateWithRestaurantAndUser(@Nullable LocalDate date);

    List<Vote> getAllWithRestaurant();

    List<Vote> getAllByUserIdWithRestaurant(long userId);

    List<Vote> getAllWithRestaurantByUserIdBetween(@Nullable LocalDate startDate, @Nullable LocalDate endDate, long userId);

}

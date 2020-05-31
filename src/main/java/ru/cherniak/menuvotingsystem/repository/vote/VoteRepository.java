package ru.cherniak.menuvotingsystem.repository.vote;

import org.springframework.lang.Nullable;
import ru.cherniak.menuvotingsystem.model.Vote;

import java.time.LocalDate;
import java.util.List;

public interface VoteRepository {

    Vote save(Vote vote, long userId, long restaurantId);

    Vote getWithRestaurant(long id, long userId);

    boolean delete(@Nullable LocalDate date, long userId);

    long countByRestaurant(long restaurantId);

    List<Vote> getAllByUserIdWithRestaurant(long userId);

    List<Vote> getAllWithRestaurantByUserIdBetween(@Nullable LocalDate startDate, @Nullable LocalDate endDate, long userId);

}

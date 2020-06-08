package ru.cherniak.menuvotingsystem.repository.vote;

import ru.cherniak.menuvotingsystem.model.Vote;

import java.time.LocalDate;
import java.util.List;

public interface VoteRepository {

    Vote save(Vote vote, long userId, long restaurantId);

    Vote getWithRestaurant(long id, long userId);

    boolean delete(LocalDate date, long userId);

    List<Vote> getAllByUserIdWithRestaurant(long userId);

    List<Vote> getAllWithRestaurantByUserIdBetween(LocalDate startDate, LocalDate endDate, long userId);

}

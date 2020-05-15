package ru.cherniak.menuvotingsystem.repository;

import org.springframework.lang.Nullable;
import ru.cherniak.menuvotingsystem.model.Vote;

import java.time.LocalDate;
import java.util.List;

public interface VoteRepository {

    Vote save(Vote vote, long userId, long restaurantId);

    Vote get(@Nullable LocalDate date, long userId);

    boolean delete(@Nullable LocalDate date, long userId);

    long getRestaurantVotesByDate(@Nullable LocalDate date, long restaurantId);

    long getAllRestaurantVotes(long restaurantId);

    long getAllRestaurantVotesBetween(@Nullable LocalDate startDate, @Nullable LocalDate endDate, long restaurantId);

    List<Vote> getAll();

}

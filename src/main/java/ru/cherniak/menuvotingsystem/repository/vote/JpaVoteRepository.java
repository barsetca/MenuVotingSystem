package ru.cherniak.menuvotingsystem.repository.vote;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.cherniak.menuvotingsystem.model.Vote;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface JpaVoteRepository extends JpaRepository<Vote, Long> {


    @Modifying
    @Query("DELETE FROM Vote v WHERE v.date=:date AND v.user.id=:userId")
    int delete(@Param("date") LocalDate date, @Param("userId") long userId);

    Optional<Vote> findByDateAndUserId(@Param("date") LocalDate date, @Param("userId") long userId);

    long countAllByDateAndRestaurantId(@Param("date") LocalDate date, @Param("restaurantId") long restaurantId);

    long countAllByRestaurantId(long restaurantId);

    long countAllByRestaurantIdAndDateBetween(long restaurantId, LocalDate startDate, LocalDate endDate);


    @Query("SELECT v FROM Vote v JOIN FETCH v.restaurant JOIN FETCH v.user WHERE v.date=:date AND v.user.id=:userId")
    Vote getOneByDateWithUserAndRestaurant(@Param("date") LocalDate date, @Param("userId") long userId);


    @Query("SELECT v FROM Vote v JOIN FETCH v.restaurant JOIN FETCH v.user WHERE v.date=:date ORDER BY v.restaurant.id")
    List<Vote> getAllByDateWithRestaurantAndUser(@Param("date") LocalDate date);
}

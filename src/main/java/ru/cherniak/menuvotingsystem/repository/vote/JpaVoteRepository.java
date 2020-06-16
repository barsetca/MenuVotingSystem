package ru.cherniak.menuvotingsystem.repository.vote;

import org.springframework.data.domain.Sort;
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


    @Query("SELECT v FROM Vote v JOIN FETCH v.restaurant WHERE v.id=:id AND v.user.id=:userId")
    Optional<Vote> findByDateAndUserIdWithRestaurant(@Param("id") long id, @Param("userId") long userId);

    @Query("SELECT v FROM Vote v JOIN FETCH v.restaurant WHERE v.user.id=:userId")
    List<Vote> findAllByUserIdWithRestaurant(@Param("userId") long userId, Sort sort);

    @Query("SELECT v FROM Vote v JOIN FETCH v.restaurant WHERE v.date>= :startDate AND v.date<= :endDate AND v.user.id=:userId")
    List<Vote> findAllWithRestaurantByUserIdAndDateBetween(@Param("startDate") LocalDate startDate,
                                                           @Param("endDate") LocalDate endDate,
                                                           @Param("userId") long userId, Sort sort);


    @Query("SELECT v FROM Vote v WHERE v.date=:date AND v.user.id=:userId")
    Optional<Vote> findOneByDateAndUserId(@Param("date") LocalDate now, @Param("userId") long userId);


    @Query("SELECT v FROM Vote v JOIN FETCH v.restaurant WHERE v.date=:date AND v.user.id=:userId")
    Optional<Vote> findOneWithRestaurantByDateAndUserId(@Param("date") LocalDate now, @Param("userId") long userId);
}


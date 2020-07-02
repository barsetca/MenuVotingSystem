package ru.cherniak.menuvotingsystem.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.cherniak.menuvotingsystem.model.Dish;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface JpaDishRepository extends JpaRepository<Dish, Long> {

    @Modifying
    @Query("DELETE FROM Dish d WHERE d.id=:id  AND d.restaurant.id=:restaurantId")
    int delete(@Param("id") long id, @Param("restaurantId") long restaurantId);

    @Query("SELECT d FROM Dish d WHERE d.id=:id AND d.restaurant.id=:restaurantId")
    Optional<Dish> findOneByRestaurant(@Param("id") long id, @Param("restaurantId") long restaurantId);

    List<Dish> findAllByDateAndRestaurantId(@Param("date") LocalDate date, @Param("restaurantId") long restaurantId, Sort sort);

    List<Dish> findAllByRestaurantId(@Param("restaurantId") long restaurantId, Sort sort);

    List<Dish> findAllByRestaurantIdAndDateBetween(@Param("restaurantId") long restaurantId,
                                                   @Param("startDate") LocalDate startDate,
                                                   @Param("endDate") LocalDate endDate, Sort sort);
}


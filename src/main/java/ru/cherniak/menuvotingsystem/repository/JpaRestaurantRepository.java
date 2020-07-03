package ru.cherniak.menuvotingsystem.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.cherniak.menuvotingsystem.model.Restaurant;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface JpaRestaurantRepository extends JpaRepository<Restaurant, Long> {

    @Modifying
    @Query("DELETE FROM Restaurant r WHERE r.id =:id")
    int deleteById(@Param("id") long id);

    Optional<Restaurant> getByName(@Param("name") String name);

    @Transactional
    //    https://stackoverflow.com/a/46013654/548473
    @EntityGraph(attributePaths = {"dishes"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT r FROM Restaurant r WHERE r.id=:id")
    Restaurant findOneWithDishes(@Param("id") long id);

    @Transactional
    Optional<Restaurant> findById(long id);

    @Transactional
    @Query("SELECT DISTINCT r FROM Restaurant r INNER JOIN FETCH r.dishes d WHERE d.date=:date")
    List<Restaurant> findAllWithTodayMenu(@Param("date") LocalDate date, Sort sort);


}

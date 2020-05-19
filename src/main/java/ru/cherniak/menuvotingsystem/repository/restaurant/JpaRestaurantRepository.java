package ru.cherniak.menuvotingsystem.repository.restaurant;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.cherniak.menuvotingsystem.model.Restaurant;

import java.util.List;


@Transactional(readOnly = true)
public interface JpaRestaurantRepository extends JpaRepository<Restaurant, Long> {


    @Modifying
    @Query("DELETE FROM Restaurant r WHERE r.id =:id")
    int deleteById(@Param("id") long id);

    Restaurant getByName(@Param("name") String name);

    @EntityGraph(attributePaths = {"votes"})
    @Query("SELECT r FROM Restaurant r WHERE r.id=:id")
    Restaurant findOneWithVotes(@Param("id") long id);

    @EntityGraph(attributePaths = {"dishes"})
    @Query("SELECT r FROM Restaurant r WHERE r.id=:id")
    Restaurant findOneWithDishes(@Param("id") long id);

    @EntityGraph(attributePaths = {"dishes"})
    @Query("SELECT r FROM Restaurant r")
    List<Restaurant> findAllWithDishes(Sort sort);

    @EntityGraph(attributePaths = {"votes"})
    @Query("SELECT r FROM Restaurant r")
    List<Restaurant> findAllWithVotes(Sort sort);

    //@EntityGraph(attributePaths = {"votes", "dishes"})

//    @Query("SELECT r FROM Restaurant r LEFT OUTER JOIN FETCH r.dishes LEFT OUTER JOIN FETCH r.votes WHERE r.id=:id")
//    Restaurant findOneByIdWithDishesAndVotes(@Param("id") long id, Sort sort);

}

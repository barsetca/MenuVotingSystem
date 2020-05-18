package ru.cherniak.menuvotingsystem.repository.restaurant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.cherniak.menuvotingsystem.model.Restaurant;


@Transactional(readOnly = true)
public interface JpaRestaurantRepository extends JpaRepository<Restaurant, Long> {


    @Modifying
    @Query("DELETE FROM Restaurant r WHERE r.id =:id")
    int deleteById(@Param("id") long id);

    Restaurant getByName(@Param("name") String name);


    @Query("SELECT r FROM Restaurant r LEFT OUTER JOIN FETCH r.votes WHERE r.id=:id")
    Restaurant findOneWithVotes(@Param("id") long id);


    @Query("SELECT r FROM Restaurant r LEFT OUTER JOIN FETCH r.dishes WHERE r.id=:id")
    Restaurant findOneWithDishes(@Param("id") long id);
}

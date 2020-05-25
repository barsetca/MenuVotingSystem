package ru.cherniak.menuvotingsystem.repository.dish;


import org.springframework.lang.Nullable;
import ru.cherniak.menuvotingsystem.model.Dish;

import java.time.LocalDate;
import java.util.List;

public interface DishRepository {

    Dish save(Dish dish, long restaurantId);

    Dish get(long id);

    boolean delete(long dishId);

    List<Dish> getAllWithRestaurant();

    List<Dish> getAllByRestaurant(long restaurantId);

    List<Dish> getDayMenu(@Nullable LocalDate date, long restaurantId);

    List<Dish> getAllByRestaurantBetweenInclusive(@Nullable LocalDate startDate, @Nullable LocalDate endDate, long restaurantId);

    Dish getWithRestaurant(long id, long restaurantId);

    List<Dish> getDayMenuByDateWithRestaurant(@Nullable LocalDate date, long restaurantId);

    List<Dish> getAllDayMenuByDateWithRestaurant (@Nullable LocalDate date);
}

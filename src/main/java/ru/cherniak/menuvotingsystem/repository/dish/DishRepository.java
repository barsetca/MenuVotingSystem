package ru.cherniak.menuvotingsystem.repository.dish;


import org.springframework.lang.Nullable;
import ru.cherniak.menuvotingsystem.model.Dish;

import java.time.LocalDate;
import java.util.List;

public interface DishRepository {

    Dish save(Dish dish, long restaurantId);

    Dish get(long id, long restaurantId);

    boolean delete(long dishId, long restaurantId);

    List<Dish> getAll(long restaurantId);

    List<Dish> getDayMenu(@Nullable LocalDate date, long restaurantId);

    List<Dish> getAllBetweenInclusive(@Nullable LocalDate startDate, @Nullable LocalDate endDate, long restaurantId);

    Dish getWithRestaurant(long id, long restaurantId);

    List<Dish> getDayMenuByDateWithRestaurant(LocalDate date, long restaurantId);
}
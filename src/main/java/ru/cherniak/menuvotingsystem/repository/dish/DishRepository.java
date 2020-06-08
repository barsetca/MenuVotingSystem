package ru.cherniak.menuvotingsystem.repository.dish;


import ru.cherniak.menuvotingsystem.model.Dish;

import java.time.LocalDate;
import java.util.List;

public interface DishRepository {

    Dish save(Dish dish, long restaurantId);

    Dish get(long id);

    boolean delete(long dishId);

    List<Dish> getAllWithRestaurant();

    List<Dish> getAllByRestaurant(long restaurantId);

    List<Dish> getAllByRestaurantBetweenInclusive(LocalDate startDate, LocalDate endDate, long restaurantId);

    List<Dish> getDayMenu(LocalDate date, long restaurantId);

    Dish getWithRestaurant(long id, long restaurantId);

}

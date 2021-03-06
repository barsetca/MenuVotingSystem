package ru.cherniak.menuvotingsystem.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import ru.cherniak.menuvotingsystem.model.Dish;
import ru.cherniak.menuvotingsystem.service.DishService;

import java.time.LocalDate;
import java.util.List;

public abstract class AbstractDishController {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    DishService dishService;

    public Dish create(Dish dish, long restaurantId) {
        log.info("create dish {} of restaurant {}", dish, restaurantId);
        return dishService.create(dish, restaurantId);
    }

    public void update(Dish dish, long restaurantId) {
        log.info("update dish {} of restaurant {}", dish, restaurantId);
        dishService.update(dish, restaurantId);
    }

    public void delete(long id, long restaurantId) {
        log.info("delete {}", id);
        dishService.delete(id, restaurantId);
    }

    public Dish get(long restaurantId, long id) {
        log.info("get {}", id);
        return dishService.get(restaurantId, id);
    }

    public List<Dish> getAllByRestaurant(long restaurantId) {
        log.info("getAllByRestaurant of restaurant {}", restaurantId);
        return dishService.getAllByRestaurant(restaurantId);
    }

    public List<Dish> getTodayMenu(long restaurantId) {
        log.info("getTodayMenu of restaurant {}", restaurantId);
        return dishService.getTodayMenuByRestaurant(restaurantId);
    }

    public List<Dish> getAllByRestaurantBetweenDatesInclusive(@Nullable LocalDate startDate, @Nullable LocalDate endDate,
                                                              long restaurantId) {
        log.info("getAllByRestaurantBetweenDatesInclusive {} - {} of restaurant {}", startDate, endDate, restaurantId);
        return dishService.getAllByRestaurantBetweenDatesInclusive(startDate, endDate, restaurantId);
    }
}


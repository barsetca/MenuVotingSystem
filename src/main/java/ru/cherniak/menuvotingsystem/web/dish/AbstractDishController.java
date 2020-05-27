package ru.cherniak.menuvotingsystem.web.dish;

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

    public boolean delete(long id) {
        log.info("delete {}", id);
        return dishService.delete(id);
    }

    public Dish get(long id) {
        log.info("get {}", id);
        return dishService.get(id);
    }

    public List<Dish> getAllWithRestaurant(){
        log.info("getAllWithRestaurant");
        return dishService.getAllWithRestaurant();
    }

    public List<Dish> getAllByRestaurant(long restaurantId) {
        log.info("getAll of restaurant {}", restaurantId);
        return dishService.getAllByRestaurant(restaurantId);
    }

    public List<Dish> getDayMenu(@Nullable LocalDate date, long restaurantId) {
        log.info("getDayMenu {} of restaurant {}", date, restaurantId);
        return dishService.getDayMenu(date, restaurantId);

    }

    public List<Dish> getTodayMenu(long restaurantId) {
        log.info("getTodayMenu of restaurant {}", restaurantId);
        return dishService.getDayMenu(LocalDate.now(), restaurantId);
    }


    public List<Dish> getAllByRestaurantBetweenDatesInclusive(@Nullable LocalDate startDate, @Nullable LocalDate endDate,
                                                              long restaurantId) {
        log.info("getAllBetweenDatesInclusive {} - {} of restaurant {}", startDate, endDate, restaurantId);
        return dishService.getAllByRestaurantBetweenDatesInclusive(startDate, endDate, restaurantId);
    }

    public Dish getWithRestaurant(long id, long restaurantId) {
        return dishService.getWithRestaurant(id, restaurantId);
    }

    public List<Dish> getDayMenuByDateWithRestaurant(@Nullable LocalDate date, long restaurantId) {
        return dishService.getDayMenuByDateWithRestaurant(date, restaurantId);
    }

    public List<Dish> getAllDayMenuByDateWithRestaurant (@Nullable LocalDate date){
        return dishService.getAllDayMenuByDateWithRestaurant(date);
    }
}

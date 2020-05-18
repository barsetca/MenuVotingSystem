package ru.cherniak.menuvotingsystem.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.cherniak.menuvotingsystem.model.Dish;
import ru.cherniak.menuvotingsystem.repository.dish.DishRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class DishService {

    protected final Logger log = LoggerFactory.getLogger(DishService.class);

    private final DishRepository repository;

    @Autowired
    public DishService(DishRepository repository) {
        this.repository = repository;
    }

    public Dish create(Dish dish, long restaurantId) {
        log.info("create dish {} of restaurant {}", dish, restaurantId);
        Assert.notNull(dish, "dish must not be null");
        return repository.save(dish, restaurantId);
    }

    public void update(Dish dish, long restaurantId) {
        log.info("update dish {} of restaurant {}", dish, restaurantId);
        Assert.notNull(dish, "dish must not be null");
        //checkNotFoundWithId(repository.save(dish, restaurantId), dish.getId());
        repository.save(dish, restaurantId);
    }

    public boolean delete(long id, long restaurantId) {
        log.info("delete {} of restaurant {}", id, restaurantId);
        //checkNotFoundWithId(repository.delete(id, restaurantId), id);
        return repository.delete(id, restaurantId);
    }

    public Dish get(long id, long restaurantId) {
        log.info("get {} of restaurant {}", id, restaurantId);
//        checkNotFoundWithId(repository.get(id, userId), id);
        return repository.get(id, restaurantId);
    }

    public List<Dish> getAll(long restaurantId) {
        log.info("getAll of restaurant {}", restaurantId);
        return repository.getAll(restaurantId);
    }

    public List<Dish> getDayMenu(@Nullable LocalDate date, long restaurantId) {
        log.info("getDayMenu {} of restaurant {}", date, restaurantId);
        return repository.getDayMenu(date, restaurantId);

    }

    public List<Dish> getTodayMenu(long restaurantId) {
        log.info("getTodayMenu of restaurant {}", restaurantId);
        return repository.getDayMenu(LocalDate.now(), restaurantId);
    }


    public List<Dish> getAllBetweenDatesInclusive(@Nullable LocalDate startDate, @Nullable LocalDate endDate,
                                                  long restaurantId) {
        log.info("getAllBetweenDatesInclusive {} - {} of restaurant {}", startDate, endDate, restaurantId);
        return repository.getAllBetweenInclusive(startDate, endDate, restaurantId);
    }

    public Dish getWithRestaurant(long id, long restaurantId) {
        return repository.getWithRestaurant(id, restaurantId);
    }

    public List<Dish> getDayMenuByDateWithRestaurant(@Nullable LocalDate date, long restaurantId) {
        return repository.getDayMenuByDateWithRestaurant(date, restaurantId);
    }
}

package ru.cherniak.menuvotingsystem.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.cherniak.menuvotingsystem.model.Dish;
import ru.cherniak.menuvotingsystem.repository.DishRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class DishService {

    private final Logger log = LoggerFactory.getLogger(DishService.class);

    private final DishRepository repository;

    @Autowired
    public DishService(DishRepository repository) {
        this.repository = repository;
    }

    public Dish create(Dish dish, long restaurantId) {
        Assert.notNull(dish, "dish must not be null");
        return repository.save(dish, restaurantId);
    }

    public void update(Dish dish, long restaurantId) {
        Assert.notNull(dish, "dish must not be null");
        //checkNotFoundWithId(repository.save(dish, restaurantId), dish.getId());
        repository.save(dish, restaurantId);
    }

    public boolean delete(long id, long restaurantId) {
        //checkNotFoundWithId(repository.delete(id, restaurantId), id);
        return repository.delete(id, restaurantId);
    }

    public Dish get(long id, long restaurantId) {
//        checkNotFoundWithId(repository.get(id, userId), id);
        return repository.get(id, restaurantId);
    }

    public List<Dish> getAll(long restaurantId) {
        return repository.getAll(restaurantId);

    }

    public List<Dish> getDayMenu(@Nullable LocalDate date, long restaurantId) {
        return repository.getDayMenu(date, restaurantId);

    }

    public List<Dish> getTodayMenu(long restaurantId) {
        return repository.getDayMenu(LocalDate.now(), restaurantId);
    }


    public List<Dish> getAllBetweenDatesInclusive(@Nullable LocalDate startDate, @Nullable LocalDate endDate, long restaurantId) {
        return repository.getAllBetweenInclusive(startDate, endDate, restaurantId);
    }

//    public List<Dish> getDayMenuWithRestaurant(@Nullable LocalDate date, int restaurantId) {
//        return repository.getDayMenuWithRestaurant(date, restaurantId);
//    }
}

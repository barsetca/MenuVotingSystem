package ru.cherniak.menuvotingsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.cherniak.menuvotingsystem.model.Dish;
import ru.cherniak.menuvotingsystem.repository.dish.DishRepository;
import ru.cherniak.menuvotingsystem.util.DateTimeUtil;

import java.time.LocalDate;
import java.util.List;

import static ru.cherniak.menuvotingsystem.util.ValidationUtil.checkNotFoundWithId;
import static ru.cherniak.menuvotingsystem.util.ValidationUtil.checkNotFoundWithMsg;

@Service
public class DishService {

    private final DishRepository repository;

    @Autowired
    public DishService(DishRepository repository) {
        this.repository = repository;
    }

    public Dish create(Dish dish, long restaurantId) {
        Assert.notNull(dish, "dish must not be null");
        return checkNotFoundWithMsg(repository.save(dish, restaurantId), "restaurantId= " + restaurantId);
    }

    public void update(Dish dish, long restaurantId) {
        Assert.notNull(dish, "dish must not be null");
        checkNotFoundWithId(repository.save(dish, restaurantId), dish.getId());
    }

    public boolean delete(long id) {
        checkNotFoundWithId(repository.delete(id), id);
        return repository.delete(id);
    }

    public Dish get(long id) {
        return checkNotFoundWithId(repository.get(id), id);
    }

    public List<Dish> getAllWithRestaurant() {
        return repository.getAllWithRestaurant();
    }

    public List<Dish> getAllByRestaurant(long restaurantId) {
        return repository.getAllByRestaurant(restaurantId);
    }

    public List<Dish> getDayMenu(@Nullable LocalDate date, long restaurantId) {
        return repository.getDayMenu(date, restaurantId);

    }

    public List<Dish> getTodayMenu(long restaurantId) {
        return repository.getDayMenu(LocalDate.now(), restaurantId);
    }

    public List<Dish> getAllByRestaurantBetweenDatesInclusive(@Nullable LocalDate startDate, @Nullable LocalDate endDate,
                                                              long restaurantId) {
        return repository.getAllByRestaurantBetweenInclusive(DateTimeUtil.getStartDate(startDate),
                DateTimeUtil.getEndDate(endDate), restaurantId);
    }

    public Dish getWithRestaurant(long id, long restaurantId) {
        return checkNotFoundWithId(repository.getWithRestaurant(id, restaurantId), id);
    }
}

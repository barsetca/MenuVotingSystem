package ru.cherniak.menuvotingsystem.web.restaurant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.cherniak.menuvotingsystem.model.Restaurant;
import ru.cherniak.menuvotingsystem.service.RestaurantService;

import java.util.List;

import static ru.cherniak.menuvotingsystem.util.ValidationUtil.assureIdConsistent;
import static ru.cherniak.menuvotingsystem.util.ValidationUtil.checkNew;

public abstract class AbstractRestaurantController {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    RestaurantService restaurantService;

    public Restaurant get(long id) {
        log.info("get {}", id);
        return restaurantService.get(id);
    }

    public Restaurant create(Restaurant restaurant) {
        log.info("create {}", restaurant);
        checkNew(restaurant);
        return restaurantService.create(restaurant);
    }

    public Restaurant update(Restaurant restaurant, long id) {
        log.info("update {}", restaurant);
        assureIdConsistent(restaurant, id);
        return restaurantService.update(restaurant);
    }

    public void delete(long id) {
        log.info("delete {}", id);
        restaurantService.delete(id);
    }

    public Restaurant getByName(String name) {
        log.info("getByName {}", name);
        return restaurantService.getByName(name);
    }

    public List<Restaurant> getAll() {
        log.info("getAll");
        return restaurantService.getAll();
    }

    public Restaurant getWithListVotes(long id) {
        log.info("getWithListVotes {}", id);
        return restaurantService.getWithVotes(id);

    }

    public Restaurant getWithListDishes(long id) {
        log.info("getWithListDishes {}", id);
        return restaurantService.getWithDishes(id);
    }

    public List<Restaurant> getAllWithDishes() {
        log.info("findAllWithDishes");
        return restaurantService.getAllWithDishes();
    }

    public List<Restaurant> getAllWithVotes() {
        log.info("findAllWithVotes");
        return restaurantService.getAllWithVotes();
    }

    public List<Restaurant> getAllWithDishesAndVotes() {
        log.info("getWithDishesAndVotes");
        return restaurantService.getAllWithDishesAndVotes();
    }
}

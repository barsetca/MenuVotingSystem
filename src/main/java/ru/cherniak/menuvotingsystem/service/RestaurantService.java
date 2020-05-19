package ru.cherniak.menuvotingsystem.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.cherniak.menuvotingsystem.model.Restaurant;
import ru.cherniak.menuvotingsystem.repository.restaurant.RestaurantRepository;

import java.util.List;

@Service
public class RestaurantService {

    protected final Logger log = LoggerFactory.getLogger(RestaurantService.class);


    private final RestaurantRepository repository;

    @Autowired
    public RestaurantService(RestaurantRepository repository) {
        this.repository = repository;
    }

    public Restaurant get(long id) {
        log.info("get {}", id);
        // return checkNotFoundWithId(restaurantRepository.get(id), id);
        return repository.get(id);
    }

    public Restaurant create(Restaurant restaurant) {
        log.info("create {}", restaurant);
        Assert.notNull(restaurant, "user must not be null");
        return repository.save(restaurant);

    }

    public Restaurant update(Restaurant restaurant) {
        log.info("update {}", restaurant);
        Assert.notNull(restaurant, "user must not be null");
        // checkNotFoundWithId(repository.save(restaurant), restaurant.getId());
        return repository.save(restaurant);

    }

    public void delete(long id) {
        log.info("delete {}", id);
        //checkNotFoundWithId(repository.delete(id), id);
        repository.delete(id);
    }

    public Restaurant getByName(String name) {
        log.info("getByName {}", name);
        Assert.notNull(name, "name must not be null");
        // return checkNotFound(restaurantRepository.getByName(name), "name=" + name);
        return repository.getByName(name);
    }

    public List<Restaurant> getAll() {
        log.info("getAll");
        return repository.getAll();
    }

    public Restaurant getWithListVotes(long id) {
        log.info("getWithListVotes {}", id);
        return repository.getWithListVotes(id);
        //        return checkNotFoundWithId(repository.getWithListMeals(id), id);
    }

    public Restaurant getWithListDishes(long id) {
        log.info("getWithListDishes {}", id);
        return repository.getWithListDishes(id);
//        return checkNotFoundWithId(repository.getWithListMeals(id), id);
    }


    public List<Restaurant> getAllWithDishes(){
        log.info("findAllWithDishes");
        return repository.findAllWithDishes();
    }


    public List<Restaurant> getAllWithVotes(){
        log.info("findAllWithVotes");
        return repository.findAllWithVotes();
    }

    public List<Restaurant> getAllWithDishesAndVotes(){
        log.info("getWithDishesAndVotes");
        return repository.getAllWithDishesAndVotes();
    }
}

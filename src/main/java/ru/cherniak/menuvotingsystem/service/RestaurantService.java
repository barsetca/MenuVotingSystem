package ru.cherniak.menuvotingsystem.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.cherniak.menuvotingsystem.model.Restaurant;
import ru.cherniak.menuvotingsystem.repository.restaurant.RestaurantRepository;

import java.util.List;

import static ru.cherniak.menuvotingsystem.util.ValidationUtil.checkNotFound;
import static ru.cherniak.menuvotingsystem.util.ValidationUtil.checkNotFoundWithId;

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
        return checkNotFoundWithId(repository.get(id), id);

    }

    @CacheEvict(value = "restaurants", allEntries = true)
    public Restaurant create(Restaurant restaurant) {
        log.info("create {}", restaurant);
        Assert.notNull(restaurant, "user must not be null");
        return repository.save(restaurant);

    }

    @CacheEvict(value = "restaurants", allEntries = true)
    public Restaurant update(Restaurant restaurant) {
        log.info("update {}", restaurant);
        Assert.notNull(restaurant, "user must not be null");
        return checkNotFoundWithId(repository.save(restaurant), restaurant.getId());


    }

    @CacheEvict(value = "restaurants", allEntries = true)
    public void delete(long id) {
        log.info("delete {}", id);
        checkNotFoundWithId(repository.delete(id), id);
    }

    public Restaurant getByName(String name) {
        log.info("getByName {}", name);
        Assert.notNull(name, "name must not be null");
        return checkNotFound(repository.getByName(name), "name=" + name);

    }

    @Cacheable("restaurants")
    public List<Restaurant> getAll() {
        log.info("getAll");
        return repository.getAll();
    }

    public Restaurant getWithVotes(long id) {
        log.info("getWithListVotes {}", id);
        return checkNotFoundWithId(repository.getWithVotes(id), id);
    }

    public Restaurant getWithDishes(long id) {
        log.info("getWithListDishes {}", id);
        return checkNotFoundWithId(repository.getWithDishes(id), id);
    }

    public List<Restaurant> getAllWithDishes() {
        log.info("findAllWithDishes");
        return repository.getAllWithDishes();
    }

    public List<Restaurant> getAllWithVotes() {
        log.info("getAllWithVotes");
        return repository.getAllWithVotes();
    }

    public Restaurant getByNameWithVotes(String name) {
        Assert.notNull(name, "name must not be null");
        return checkNotFound(repository.getByNameWithVotes(name), "name=" + name);
    }
}

package ru.cherniak.menuvotingsystem.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.cherniak.menuvotingsystem.model.Restaurant;
import ru.cherniak.menuvotingsystem.repository.JpaRestaurantRepository;

import java.time.LocalDate;
import java.util.List;

import static ru.cherniak.menuvotingsystem.util.ValidationUtil.checkNotFound;
import static ru.cherniak.menuvotingsystem.util.ValidationUtil.checkNotFoundWithId;

@Service
@Transactional(readOnly = true)
public class RestaurantService {

    private static final Sort SORT_BY_NAME = Sort.by(Sort.Direction.ASC, "name");

    private final JpaRestaurantRepository restaurantRepository;

    @Autowired
    public RestaurantService(JpaRestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public Restaurant get(long id) {
        return checkNotFoundWithId(restaurantRepository.findById(id).orElse(null), id);
    }

    private Restaurant save(Restaurant restaurant) {
        if (!restaurant.isNew() && get(restaurant.id()) == null) {
            return null;
        }
        return restaurantRepository.save(restaurant);
    }

    @Transactional
    @CacheEvict(value = "restaurants", allEntries = true)
    public Restaurant create(Restaurant restaurant) {
        Assert.notNull(restaurant, "user must not be null");
        return save(restaurant);
    }

    @Transactional
    @CacheEvict(value = "restaurants", allEntries = true)
    public Restaurant update(Restaurant restaurant) {
        Assert.notNull(restaurant, "user must not be null");
        return checkNotFoundWithId(save(restaurant), restaurant.getId());
    }

    @Transactional
    @CacheEvict(value = "restaurants", allEntries = true)
    public void delete(long id) {
        checkNotFoundWithId(restaurantRepository.deleteById(id) != 0, id);
    }

    public Restaurant getByName(String name) {
        Assert.notNull(name, "name must not be null");
        return checkNotFound(restaurantRepository.getByName(name).orElse(null), "name=" + name);
    }

    public List<Restaurant> getAll() {
        return restaurantRepository.findAll(SORT_BY_NAME);
    }

    public Restaurant getWithDishes(long id) {
        return checkNotFoundWithId(restaurantRepository.findOneWithDishes(id), id);
    }

    @Transactional
    public List<Restaurant> getAllWithTodayMenu() {
        return restaurantRepository.findAllWithTodayMenu(LocalDate.now(), SORT_BY_NAME);
    }
}

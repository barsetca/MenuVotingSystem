package ru.cherniak.menuvotingsystem.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.cherniak.menuvotingsystem.model.Restaurant;
import ru.cherniak.menuvotingsystem.repository.restaurant.JpaRestaurantRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static ru.cherniak.menuvotingsystem.util.ValidationUtil.checkNotFound;
import static ru.cherniak.menuvotingsystem.util.ValidationUtil.checkNotFoundWithId;

@Service
@Transactional(readOnly = true)
public class RestaurantService {

    protected final Logger log = LoggerFactory.getLogger(RestaurantService.class);

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

    @Cacheable("restaurants")
    public List<Restaurant> getAll() {
        return restaurantRepository.findAll(SORT_BY_NAME);
    }

    public Restaurant getWithVotes(long id) {
        return checkNotFoundWithId(restaurantRepository.findOneWithVotes(id), id);
    }

    public Restaurant getWithDishes(long id) {
        return checkNotFoundWithId(restaurantRepository.findOneWithDishes(id), id);
    }


    public List<Restaurant> getAllWithDishes() {
        return restaurantRepository.findAllWithDishes(SORT_BY_NAME);
    }

    @Cacheable("restaurants")
    public List<Restaurant> getAllWithVotes() {
        return restaurantRepository.findAllWithVotes();
    }


    public Restaurant getByNameWithVotes(String name) {
        Assert.notNull(name, "name must not be null");
        return checkNotFound(restaurantRepository.findOneByNameWithVotes(name).orElse(null), "name=" + name);
    }

    public List<Restaurant> getAllWithTodayMenu() {
       List<Restaurant> restaurants = getAllWithDishes();
      restaurants.forEach(r-> r.setDishes(r.getDishes().stream().filter(d ->
              d.getDate().isEqual(LocalDate.now())).collect(Collectors.toSet())));
        return restaurants;
    }

}
